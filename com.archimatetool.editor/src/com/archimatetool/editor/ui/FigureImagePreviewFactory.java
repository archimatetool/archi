/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.Set;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.AutoscalingMode;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.util.ImagePrintFigureOperation;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;


/**
 * Factory class to create preview images of Figures
 * If the Figure has an alternate figures it will return that
 * 
 * @author Phillip Beauvoir
 */
public class FigureImagePreviewFactory {
    
    public static final int FIGURE_WIDTH = 120;
    public static final int FIGURE_HEIGHT = 55;
    
    /**
     * Preview figure consisting of parent that can have a dark or light background
     */
    private static class PreviewFigure extends Figure {
        private static Color darkColor = new Color(47, 47, 47);
        private IDiagramModelObjectFigure figure;
        
        public PreviewFigure(IDiagramModelObjectFigure figure) {
            this.figure = figure;
            
            setLayoutManager(new StackLayout());
            setOpaque(true);
            setSize(FIGURE_WIDTH, FIGURE_HEIGHT);
            add(figure);
            
            if(ThemeUtils.isDarkTheme()) {
                setBackgroundColor(darkColor);
            }
        }

        /**
         * Dispose of child IDiagramModelObjectFigure
         */
        public void dispose() {
            figure.dispose();
        }
    }
    
    /**
     * Figures of these types have text at the bottom
     */
    private static Set<EClass> BOTTOM_TEXT_FIGURE_TYPES = Set.of(
            IArchimatePackage.eINSTANCE.getAssessment(),
            IArchimatePackage.eINSTANCE.getBusinessActor(),
            IArchimatePackage.eINSTANCE.getCapability(),
            IArchimatePackage.eINSTANCE.getCommunicationNetwork(),
            IArchimatePackage.eINSTANCE.getCourseOfAction(),
            IArchimatePackage.eINSTANCE.getDistributionNetwork(),
            IArchimatePackage.eINSTANCE.getDriver(),
            IArchimatePackage.eINSTANCE.getEquipment(),
            IArchimatePackage.eINSTANCE.getFacility(),
            IArchimatePackage.eINSTANCE.getGap(),
            IArchimatePackage.eINSTANCE.getGoal(),
            IArchimatePackage.eINSTANCE.getLocation(),
            IArchimatePackage.eINSTANCE.getMaterial(),
            IArchimatePackage.eINSTANCE.getOutcome(),
            IArchimatePackage.eINSTANCE.getPlateau(),
            IArchimatePackage.eINSTANCE.getPrinciple(),
            IArchimatePackage.eINSTANCE.getSystemSoftware(),
            IArchimatePackage.eINSTANCE.getWorkPackage());
    
    /**
     * @return A preview image of the graphical Figure used by eClass of type
     */
    public static Image getPreviewImage(EClass eClass, int type) {
        // Only two types
        if(type < 0 || type > 1) {
            return null;
        }
        
        if(!(ObjectUIFactory.INSTANCE.getProviderForClass(eClass) instanceof IArchimateElementUIProvider uiProvider)) {
            return null;
        }
        
        // No alternate figure
        if(type > 0 && !uiProvider.hasAlternateFigure()) {
            return null;
        }

        return createImage(uiProvider, eClass, type);
    }
    
    /**
     * Create the figure
     */
    private static PreviewFigure createFigure(IArchimateElementUIProvider uiProvider, EClass eClass, int type) {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
        dmo.setName(uiProvider.getDefaultName());
        dmo.setType(type);
        dmo.setDeriveElementLineColor(false);
        
        // Dark theme needs a lighter text color
        if(ThemeUtils.isDarkTheme()) {
            dmo.setFontColor("#666666"); //$NON-NLS-1$
        }
        
        // Special case for text alignment
        if(eClass == IArchimatePackage.eINSTANCE.getGrouping() && type == 1) {
            dmo.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        }
        
        // Special cases for text position on icon type figures
        if(type == 1 && BOTTOM_TEXT_FIGURE_TYPES.contains(eClass)) {
            dmo.setTextPosition(ITextPosition.TEXT_POSITION_BOTTOM);
        }

        // Use inbuilt default colours
        dmo.setFillColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultFillColor(dmo)));
        dmo.setLineColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultLineColor(dmo)));

        GraphicalEditPart editPart = (GraphicalEditPart)uiProvider.createEditPart();
        editPart.setModel(dmo);

        IDiagramModelObjectFigure figure = (IDiagramModelObjectFigure)editPart.getFigure();
        figure.refreshVisuals();

        return new PreviewFigure(figure);
    }
    
    /**
     * Create the image using an ImageGcDrawer.
     * Image quality is good when moving between monitors but text is clipped.
     */
    private static Image createImage(IArchimateElementUIProvider uiProvider, EClass eClass, int type) {
        return new Image(Display.getDefault(), (gc, width, height) -> {
            SWTGraphics graphics = new SWTGraphics(gc);
            
            // Create a new figure on demand each time so we can call its dispose() method
            PreviewFigure previewFigure = createFigure(uiProvider, eClass, type);
            previewFigure.validate(); // Call this here
            previewFigure.paint(graphics);
            
            graphics.dispose();
            previewFigure.dispose(); // dispose of figure
        }, FIGURE_WIDTH, FIGURE_HEIGHT);
    }

    /**
     * Create the image using a ImagePrintFigureOperation.
     * Clipped text problem is solved but image quality is worse and bad if the images are created on a 100% monitor then moved to a 200% monitor.
     */
    @SuppressWarnings("unused")
    private static Image createImageFromImagePrintFigureOperation(IArchimateElementUIProvider uiProvider, EClass eClass, int type) {
        Shell shell = DPIUtils.createShellForImage();
        try {
            PreviewFigure previewFigure = createFigure(uiProvider, eClass, type);
            previewFigure.validate(); // Call this here
            
            ImagePrintFigureOperation imagePrintOperation = new ImagePrintFigureOperation(shell, previewFigure);
            Image image = imagePrintOperation.run();
            
            previewFigure.dispose(); // dispose of figure
            return image;
        }
        finally {
            shell.dispose();
        }
    }
    
    /**
     * Create the image using a GraphicalViewerImpl
     * Image quality is good when moving between monitors and text is not clipped on the right but text might be clipped on letters with a descender like "g".
     */
    @SuppressWarnings("unused")
    private static Image createImageFromGraphicalViewer(IArchimateElementUIProvider uiProvider, EClass eClass, int type) {
        return new Image(Display.getDefault(), (gc, width, height) -> {
            Shell shell = DPIUtils.createShellForImage();
            try {
                // Create a new figure on demand each time so we can call its dispose() method
                PreviewFigure previewFigure = createFigure(uiProvider, eClass, type);
                GraphicalViewer viewer = new GraphicalViewerImpl();
                viewer.createControl(shell).setAutoscalingMode(AutoscalingMode.ENABLED); // Stops horizontal text clipping on Windows
                
                viewer.setContents(new AbstractGraphicalEditPart() {
                    @Override
                    protected IFigure createFigure() {
                        return previewFigure;
                    }

                    @Override
                    protected void createEditPolicies() {
                    }
                });
                
                // Call this here otherwise text is clipped
                previewFigure.validate();
                
                SWTGraphics graphics = new SWTGraphics(gc);
                previewFigure.paint(graphics);
                
                graphics.dispose();
                previewFigure.dispose(); // dispose of figure
            }
            finally {
                shell.dispose();
            }
        }, FIGURE_WIDTH, FIGURE_HEIGHT);
    }
}
