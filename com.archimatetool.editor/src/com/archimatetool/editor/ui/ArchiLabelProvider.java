/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProfile;



/**
 * Main Label Provider for Archi
 * 
 * @author Phillip Beauvoir
 */
public class ArchiLabelProvider {
    
    public static ArchiLabelProvider INSTANCE = new ArchiLabelProvider();

    private ArchiLabelProvider() {
    }
    
    /**
     * @return A name for an object.<br>
     *         This will be the element's name if of type INameable or a default name as specified in its ElementUIProvider.
     */
    public String getLabel(Object object) {
        if(object == null) {
            return ""; //$NON-NLS-1$
        }
        
        object = getAdaptableObject(object);
        
        String name = null;
        
        // Get Name
        if(object instanceof INameable nameable) {
            name = nameable.getName();
        }
        
        // It's blank. Get a default name from its eClass
        if(!StringUtils.isSet(name) && object instanceof EObject eObject) {
            name = getDefaultName(eObject.eClass());
        }
        
        return StringUtils.safeString(name);
    }

    /**
     * @return A name for an object.<br>
     *         This will be the element's name if of type INameable or a default name as specified in its ElementUIProvider.
     *         All newline instances will be replaced by a single space
     */
    public String getLabelNormalised(Object object) {
        return StringUtils.normaliseNewLineCharacters(getLabel(object));
    }
    
    /**
     * Get a default human-readable name for an EClass
     * @param eClass The Class
     * @return A name or an empty string
     */
    public String getDefaultName(EClass eClass) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
        if(provider != null) {
            return provider.getDefaultName();
        }
        
        return ""; //$NON-NLS-1$
    }
    
    /**
     * @param element
     * @return An image for an object
     */
    public Image getImage(Object object) {
        if(object == null) {
            return null;
        }
        
        object = getAdaptableObject(object);
        
        // This first, since EClass is an EObject
        if(object instanceof EClass eClass) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
            if(provider != null) {
                return provider.getImage();
            }
        }
        else if(object instanceof EObject eObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(eObject);
            if(provider != null) {
                return provider.getImage();
            }
        }
        
        return null;
    }
    
    /**
     * @param eClass
     * @return An ImageDescriptor for an EClass
     */
    public ImageDescriptor getImageDescriptor(EClass eClass) {
        if(eClass == null) {
            return null;
        }
        
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
        if(provider != null) {
            return provider.getImageDescriptor();
        }
        
        return null;
    }
    
    /**
     * Create an ImageDescriptor icon for a Specialization from its image
     * The image data size is 16x16 or 32x32 depending on zoom.
     * The user image is scaled to fit and centred on the background image.
     * The background color is set to something unlikely to be used in the actual image so that we can set the transparent pixel.
     */
    public ImageDescriptor getImageDescriptorForSpecialization(IProfile profile) {
        // If no image path set return default icon for concept class
        if(!StringUtils.isSet(profile.getImagePath())) {
            return getImageDescriptor(profile.getConceptClass());
        }
        
        // Create a new ImageDescriptor
        return new ImageDescriptor() {
            // Set background to this color so we can make it transparent
            static final Color transparentColor = new Color(255, 255, 254);
            // Palette icon size
            static final int iconSize = 16;
            
            @Override
            public ImageData getImageData(int zoom) {
                final Image image;
                try {
                    IArchiveManager archiveManager = (IArchiveManager)profile.getAdapter(IArchiveManager.class);
                    image = archiveManager.createImage(profile.getImagePath());
                    if(image == null) {
                        return getImageDescriptor(profile.getConceptClass()).getImageData(zoom);
                    }
                }
                catch(Exception ex) {
                    Logger.error("Could not create specialization image", ex); //$NON-NLS-1$
                    // Return default imageData rather than null
                    return getImageDescriptor(profile.getConceptClass()).getImageData(zoom);
                }
                
                // Image bounds
                final Rectangle imageBounds = image.getBounds();

                // Scaled size to 16x16
                final Rectangle scaledSize = ImageFactory.getScaledImageSize(image, iconSize);
                
                // Blank icon image for background and size
                Image iconImage = new Image(Display.getDefault(), iconSize, iconSize);
                
                GC gc = new GC(iconImage);
                gc.setAntialias(SWT.ON);
                gc.setInterpolation(SWT.HIGH);
                gc.setAdvanced(true);

                // Set background to this color so we can make it transparent
                gc.setBackground(transparentColor);
                gc.fillRectangle(0, 0, iconSize, iconSize);

                // Centre the image
                int x = (iconSize - scaledSize.width) / 2;
                int y = (iconSize - scaledSize.height) / 2;

                // Draw scaled image onto icon image
                gc.drawImage(image, 0, 0, imageBounds.width, imageBounds.height,
                        x, y, scaledSize.width, scaledSize.height);

                ImageData data = iconImage.getImageData(zoom);
                
                // Set transparent pixel to background color on *this* ImageData 
                data.transparentPixel = data.palette.getPixel(transparentColor.getRGB());

                gc.dispose();
                image.dispose();
                iconImage.dispose();

                return data;
            }
        };
    }
    
    /**
     * @deprecated Use getAdaptableObject(Object object)
     */
    public Object getWrappedElement(Object object) {
        return getAdaptableObject(object);
    }
    
    /**
     * If the object is an EditPart get the EditPart's model first
     * Then if the object is an IDiagramModelArchimateComponent return the IArchimateConcept
     * Else return the object itself
     * @param object The object to unwrap
     * @return The actual model element
     */
    public Object getAdaptableObject(Object object) {
        // If object is an EditPart it will be IAdaptable and return the underlying object
        // Or classes like AbstractIssueType (in the Model Checker) use IAdaptable to return the type
        if(object instanceof IAdaptable adaptable) {
            object = adaptable.getAdapter(IArchimateModelObject.class);
        }
        
        if(object instanceof IDiagramModelArchimateComponent dmac) {
            return dmac.getArchimateConcept();
        }
        
        return object;
    }
    
    /**
     * @param relation
     * @return A sentence that describes the relationship between the relation's source and target elements
     */
    public String getRelationshipSentence(IArchimateRelationship relation) {
        if(relation != null && relation.getSource() != null && relation.getTarget() != null) {
            return getRelationshipSentence(relation.eClass(), relation.getSource(), relation.getTarget());
        }
        
        return ""; //$NON-NLS-1$
    }

    /**
     * @param relation
     * @return A sentence that describes the relationship between the relation's source and target elements
     */
    public String getRelationshipSentence(EClass relationshipType, IArchimateConcept source, IArchimateConcept target) {
        String nameSource = getLabel(source);
        String nameTarget = getLabel(target);
        
        // If it's a Junction then do something special
        if(source instanceof IJunction || target instanceof IJunction) {
            return NLS.bind(Messages.ArchiLabelProvider_0, nameSource, nameTarget);
        }

        switch(relationshipType.getClassifierID()) {
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_3, nameSource, nameTarget);

            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_4, nameSource, nameTarget);

            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_5, nameSource, nameTarget);

            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_6, nameSource, nameTarget);

            case IArchimatePackage.FLOW_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_7, nameSource, nameTarget);

            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_8, nameSource, nameTarget);

            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_9, nameSource, nameTarget);

            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_10, nameSource, nameTarget);

            case IArchimatePackage.REALIZATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_11, nameSource, nameTarget);

            case IArchimatePackage.SERVING_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_12, nameSource, nameTarget);

            case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_13, nameSource, nameTarget);

            default:
                return ""; //$NON-NLS-1$
        }
    }

    /**
     * @param eClass The Relationship class
     * @param reverseDirection If this is true then the phrase is from target to source
     * @return A phrase that describes the relationship, for example "Is Realised by", "Flows to"
     */
    public String getRelationshipPhrase(EClass eClass, boolean reverseDirection) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        switch(eClass.getClassifierID()) {
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_14 : Messages.ArchiLabelProvider_15;

            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_16 : Messages.ArchiLabelProvider_17;

            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_18 : Messages.ArchiLabelProvider_19;

            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_20 : Messages.ArchiLabelProvider_21;

            case IArchimatePackage.FLOW_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_22 : Messages.ArchiLabelProvider_23;

            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_24 : Messages.ArchiLabelProvider_25;

            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_26 : Messages.ArchiLabelProvider_27;

            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_28 : Messages.ArchiLabelProvider_29;

            case IArchimatePackage.REALIZATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_30 : Messages.ArchiLabelProvider_31;

            case IArchimatePackage.SERVING_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_32 : Messages.ArchiLabelProvider_33;

            case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_34 : Messages.ArchiLabelProvider_35;

            default:
                return ""; //$NON-NLS-1$
        }
    }
}
