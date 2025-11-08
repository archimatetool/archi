/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.FigureIconFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ILegendOptions;
import com.archimatetool.model.IProfile;


/**
 * Legend Figure graphics helper
 * 
 * @author Phillip Beauvoir
 */
class LegendGraphics {
    
    private record ClassInfo(EObject eObject, String label, Color backgroundColor) {
        EClass eClass() {
            return eObject instanceof IProfile profile ? profile.getConceptClass() : (EClass)eObject;
        }
    };
    
    private static final int LEFT_MARGIN = 5;
    private static final int ICON_MARGIN = 26;
    private static final int VERTICAL_START = 5;
    private static final int VERTICAL_SPACING = 22;
    
    private static final Color ICON_FOREGROUND_COLOR = new Color(0, 0, 0);
    
    private IDiagramModelNote note;
    private List<ClassInfo> concepts;
    private int width, height;
    
    private int rowsPerColumn;
    private int numColumns;
    private int userWidthOffset;
    
    // Cache specialization images
    private Map<String, Image> imageCache = new HashMap<>();
    
    private IPreferenceStore prefsStore = ArchiPlugin.getInstance().getPreferenceStore();
    
    LegendGraphics(IDiagramModelNote note) {
        this.note = note;
        update();
    }
    
    void update() {
        if(!isValid()) {
            return;
        }
        
        clearImageCache();
        
        ILegendOptions options = note.getLegendOptions();
        
        // Get all concepts on the diagram, if any
        Set<ClassInfo> conceptsSet = new HashSet<>();
        
        for(Iterator<EObject> iter = note.getDiagramModel().eAllContents(); iter.hasNext();) {
            if(iter.next() instanceof IDiagramModelArchimateComponent dmc) {
                IProfile profile = dmc.getArchimateConcept().getPrimaryProfile();
                EClass eClass = dmc.getArchimateConcept().eClass();
                boolean isElement = dmc.getArchimateConcept() instanceof IArchimateElement;
                boolean isRelationship = dmc.getArchimateConcept() instanceof IArchimateRelationship;
                
                // Background color
                Color backgroundColor = switch(options.getColorScheme()) {
                    case ILegendOptions.COLORS_CORE -> ColorFactory.getInbuiltDefaultFillColor(eClass);
                    case ILegendOptions.COLORS_USER -> ColorFactory.getDefaultFillColor(eClass);
                    default -> null;
                };
                
                // Not a Specialization
                if(profile == null) {
                    if((options.displayElements() && isElement) || (options.displayRelations() && isRelationship)) {
                        String userPref = prefsStore.getString(IPreferenceConstants.LEGEND_LABEL_PREFIX + eClass.getName());
                        String userLabel = StringUtils.isSet(userPref) ? userPref : ArchiLabelProvider.INSTANCE.getDefaultName(eClass);
                        conceptsSet.add(new ClassInfo(eClass, userLabel, backgroundColor));
                    }
                }
                // Specialization
                else {
                    if((options.displaySpecializationElements() && isElement) || (options.displaySpecializationRelations() && isRelationship)) {
                        conceptsSet.add(new ClassInfo(profile, profile.getName(), backgroundColor));
                    }
                }
            }
        }
        
        // Add to List
        concepts = new ArrayList<>(conceptsSet);
        
        // Sort by element then relationship and by label name
        Collator collator = Collator.getInstance();
        concepts.sort(Comparator.comparingInt((ClassInfo classInfo) -> {
            // Relationships last
            if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(classInfo.eClass())) {
                return 50;
            }
            
            // Elements sorted by name
            if(options.getSortMethod() == ILegendOptions.SORT_NAME) {
                return 0;
            }
            
            // Elements sorted by category
            if(IArchimatePackage.eINSTANCE.getStrategyElement().isSuperTypeOf(classInfo.eClass())) {
                return 0;
            }
            if(IArchimatePackage.eINSTANCE.getBusinessElement().isSuperTypeOf(classInfo.eClass())) {
                return 1;
            }
            if(IArchimatePackage.eINSTANCE.getApplicationElement().isSuperTypeOf(classInfo.eClass())) {
                return 2;
            }
            if(IArchimatePackage.eINSTANCE.getTechnologyElement().isSuperTypeOf(classInfo.eClass())) {
                return 3;
            }
            if(IArchimatePackage.eINSTANCE.getPhysicalElement().isSuperTypeOf(classInfo.eClass())) {
                return 4;
            }
            if(IArchimatePackage.eINSTANCE.getMotivationElement().isSuperTypeOf(classInfo.eClass())) {
                return 5;
            }
            if(IArchimatePackage.eINSTANCE.getImplementationMigrationElement().isSuperTypeOf(classInfo.eClass())) {
                return 6;
            }
            return 10;
            
        }).thenComparing(ClassInfo::label, collator::compare));
        
        // Rows per column, number of columns and user offset
        rowsPerColumn = options.getRowsPerColumn();
        numColumns = (int)Math.ceil((double) concepts.size() / rowsPerColumn);
        userWidthOffset = options.getWidthOffset();
        
        // Need to reset these to zero so getDefaultSize() will return 0,0 if there are no concepts shown and then a default size can be set by the caller
        width = 0;
        height = 0;
    }
    
    void drawLegend(Rectangle bounds, Graphics graphics, Color foregroundColor) {
        if(!isValid() || concepts == null || concepts.isEmpty()) {
            return;
        }
        
        int icon_x = bounds.x + LEFT_MARGIN;
        int text_x = icon_x + ICON_MARGIN;
        int y = bounds.y + VERTICAL_START;
        
        width = LEFT_MARGIN;
        height = 0;
        
        int maxTextWidth = 0;
        
        // Be sure to use this font metrics not graphics.getFontMetrics() because buggy calculations on Windows at higher scales
        FontMetrics metrics = FigureUtilities.getFontMetrics(graphics.getFont());
        
        // Font height
        int fontHeight = metrics.getHeight();
        
        // Half font height
        int halfHeight = Math.max(fontHeight, VERTICAL_SPACING) / 2;
        
        int widthOffset = 0;

        graphics.pushState();
        
        // Set foreground color for text
        graphics.setForegroundColor(foregroundColor);
        
        for(int index = 0; index < concepts.size(); index++) {
            ClassInfo classInfo = concepts.get(index);
            
            // If this is not column 0, set some offsets
            if(index != 0 && index % rowsPerColumn == 0) {
                y = bounds.y + VERTICAL_START;
                
                // If this is not the last column add the user offset, if any
                int columnIndex = index / rowsPerColumn;
                if(columnIndex < numColumns) {
                    widthOffset += userWidthOffset;
                }
                
                // Add width offset
                icon_x += widthOffset;
                text_x += widthOffset;
                
                // reset this for the next column
                maxTextWidth = 0;
            }

            // Max text width
            maxTextWidth = Math.max(FigureUtilities.getTextWidth(classInfo.label(), graphics.getFont()), maxTextWidth);
            
            // Get width offset from max text width + margins
            widthOffset = maxTextWidth + LEFT_MARGIN + ICON_MARGIN;
            
            // Mid y point
            int midY = y + halfHeight;
            
            // Draw Specialization icon
            if(classInfo.eObject() instanceof IProfile profile && StringUtils.isSet(profile.getImagePath())) {
                drawSpecializationIcon(profile, graphics, icon_x, midY);
            }
            // Draw EClass icon
            else {
                drawIcon(classInfo.eClass(), graphics, ICON_FOREGROUND_COLOR, classInfo.backgroundColor(), icon_x, midY);
            }
            
            // Text offset if VERTICAL_SPACING is in play
            int y_offset = Math.max(0, (VERTICAL_SPACING - fontHeight) / 2);
            
            // Draw text
            graphics.drawText(classInfo.label(), text_x, y + y_offset);
            
            // draw a mid-line for visual testing
            // graphics.drawLine(icon_x, midY, icon_x + 150, midY);
            
            // Increase y by max of font height or vertical spacing
            y += Math.max(fontHeight, VERTICAL_SPACING);
            
            // Overall height
            height = Math.max(y - bounds.y + VERTICAL_START, height);
        }
        
        // Overall width
        width = icon_x + widthOffset - bounds.x;
        
        graphics.popState();
    }
    
    Dimension getDefaultSize() {
        return new Dimension(width, height);
    }
    
    private void drawIcon(EClass eClass, Graphics graphics, Color foregroundColor, Color backgroundColor, int icon_x, int midY) {
        // Each figure has a different point origin
        Point pt = switch(eClass.getClassifierID()) {
            // Elements
            case IArchimatePackage.APPLICATION_COMPONENT -> new Point(icon_x + 7, midY + 7);
            case IArchimatePackage.ARTIFACT ->  new Point(icon_x + 6, midY - 8);
            case IArchimatePackage.ASSESSMENT -> new Point(icon_x + 9, midY - 6);
            case IArchimatePackage.BUSINESS_ACTOR -> new Point(icon_x + 8, midY - 8);
            case IArchimatePackage.BUSINESS_COLLABORATION,
                 IArchimatePackage.APPLICATION_COLLABORATION,
                 IArchimatePackage.TECHNOLOGY_COLLABORATION -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.BUSINESS_EVENT,
                 IArchimatePackage.APPLICATION_EVENT,
                 IArchimatePackage.TECHNOLOGY_EVENT,
                 IArchimatePackage.IMPLEMENTATION_EVENT -> new Point(icon_x + 4, midY - 5);
            case IArchimatePackage.BUSINESS_FUNCTION,
                 IArchimatePackage.APPLICATION_FUNCTION,
                 IArchimatePackage.TECHNOLOGY_FUNCTION -> new Point(icon_x + 6, midY + 6);
            case IArchimatePackage.BUSINESS_INTERACTION,
                 IArchimatePackage.APPLICATION_INTERACTION,
                 IArchimatePackage.TECHNOLOGY_INTERACTION -> new Point(icon_x + 11, midY - 6);
            case IArchimatePackage.BUSINESS_INTERFACE,
                 IArchimatePackage.APPLICATION_INTERFACE,
                 IArchimatePackage.TECHNOLOGY_INTERFACE -> new Point(icon_x + 10, midY - 5);
            case IArchimatePackage.BUSINESS_OBJECT,
                 IArchimatePackage.DATA_OBJECT -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.BUSINESS_PROCESS,
                 IArchimatePackage.APPLICATION_PROCESS,
                 IArchimatePackage.TECHNOLOGY_PROCESS -> new Point(icon_x + 5, midY - 2);
            case IArchimatePackage.BUSINESS_ROLE -> new Point(icon_x + 4, midY - 4);
            case IArchimatePackage.BUSINESS_SERVICE,
                 IArchimatePackage.APPLICATION_SERVICE,
                 IArchimatePackage.TECHNOLOGY_SERVICE -> new Point(icon_x + 4, midY - 5);
            case IArchimatePackage.CAPABILITY -> new Point(icon_x + 5, midY - 6);
            case IArchimatePackage.COMMUNICATION_NETWORK -> new Point(icon_x + 4, midY + 1);
            case IArchimatePackage.CONSTRAINT -> new Point(icon_x + 8, midY - 5);
            case IArchimatePackage.CONTRACT -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.COURSE_OF_ACTION -> new Point(icon_x + 10, midY - 7);
            case IArchimatePackage.DELIVERABLE -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.DEVICE -> new Point(icon_x + 6, midY - 6);
            case IArchimatePackage.DISTRIBUTION_NETWORK -> new Point(icon_x + 5, midY);
            case IArchimatePackage.DRIVER -> new Point(icon_x + 5, midY - 6);
            case IArchimatePackage.EQUIPMENT -> new Point(icon_x + 6, midY + 1);
            case IArchimatePackage.FACILITY -> new Point(icon_x + 5, midY + 5);
            case IArchimatePackage.GAP -> new Point(icon_x + 5, midY - 6);
            case IArchimatePackage.GOAL -> new Point(icon_x + 5, midY - 6);
            case IArchimatePackage.GROUPING -> new Point(icon_x + 5, midY - 6);
            case IArchimatePackage.JUNCTION -> new Point(icon_x + 4, midY - 8);
            case IArchimatePackage.LOCATION -> new Point(icon_x + 11, midY + 7);
            case IArchimatePackage.MATERIAL -> new Point(icon_x + 11, midY);
            case IArchimatePackage.MEANING -> new Point(icon_x + 6, midY - 4);
            case IArchimatePackage.NODE -> new Point(icon_x + 5, midY - 4);
            case IArchimatePackage.OUTCOME -> new Point(icon_x + 5, midY - 6);
            case IArchimatePackage.PATH -> new Point(icon_x + 5, midY);
            case IArchimatePackage.PLATEAU -> new Point(icon_x + 4, midY + 3);
            case IArchimatePackage.PRINCIPLE -> new Point(icon_x + 5, midY - 7);
            case IArchimatePackage.PRODUCT -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.REPRESENTATION -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.REQUIREMENT -> new Point(icon_x + 8, midY - 5);
            case IArchimatePackage.RESOURCE -> new Point(icon_x + 4, midY - 5);
            case IArchimatePackage.STAKEHOLDER -> new Point(icon_x + 5, midY - 4);
            case IArchimatePackage.SYSTEM_SOFTWARE -> new Point(icon_x + 5, midY - 5);
            case IArchimatePackage.VALUE -> new Point(icon_x + 4, midY - 5);
            case IArchimatePackage.VALUE_STREAM -> new Point(icon_x + 4, midY - 5);
            case IArchimatePackage.WORK_PACKAGE -> new Point(icon_x + 5, midY - 5);

            // Relations all have the same origin
            case IArchimatePackage.ACCESS_RELATIONSHIP,
                 IArchimatePackage.AGGREGATION_RELATIONSHIP,
                 IArchimatePackage.ASSIGNMENT_RELATIONSHIP,
                 IArchimatePackage.ASSOCIATION_RELATIONSHIP,
                 IArchimatePackage.COMPOSITION_RELATIONSHIP,
                 IArchimatePackage.FLOW_RELATIONSHIP,
                 IArchimatePackage.INFLUENCE_RELATIONSHIP,
                 IArchimatePackage.REALIZATION_RELATIONSHIP,
                 IArchimatePackage.SERVING_RELATIONSHIP,
                 IArchimatePackage.SPECIALIZATION_RELATIONSHIP,
                 IArchimatePackage.TRIGGERING_RELATIONSHIP -> new Point(icon_x + 4, midY - 7);
            
             default -> new Point();
        };
        
        FigureIconFactory.drawIcon(eClass, graphics, foregroundColor, backgroundColor, pt);
    }
    
    private void drawSpecializationIcon(IProfile profile, Graphics graphics, int icon_x, int midY) {
        // Get cached image
        Image img = imageCache.computeIfAbsent(profile.getImagePath(),
                key -> ArchiLabelProvider.INSTANCE.getImageDescriptorForSpecialization(profile).createImage());
        
        // Draw image
        if(img != null) {
            graphics.drawImage(img, new Point(icon_x + 4, midY - 8));
        }
    }
    
    void clearImageCache() {
        for(Image image : imageCache.values()) {
            image.dispose();
        }
        imageCache.clear();
    }
    
    // Only for ArchiMate diagrams
    private boolean isValid() {
        return note.getDiagramModel() instanceof IArchimateDiagramModel;
    }
    
    void dispose() {
        clearImageCache();
        imageCache = null;
        note = null;
        concepts = null;
    }
}
