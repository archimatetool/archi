/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import java.net.URL;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.editor.diagram.DiagramConstants;
import uk.ac.bolton.archimate.editor.ui.components.CompositeMultiImageDescriptor;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IInterfaceElement;


/**
 * Image Factory
 * 
 * @author Phillip Beauvoir
 */
public class ImageFactory {
    
    public static final String ECLIPSE_IMAGE_NEW_WIZARD = "new_wizard"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_IMPORT_PREF_WIZARD = "import_pref_wizard";  //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON = "properties_view"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_OUTLINE_VIEW_ICON = "outline_view"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_FILE = "file"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_FOLDER = "folder"; //$NON-NLS-1$

    
    private AbstractUIPlugin fPlugin;
    
    public ImageFactory(AbstractUIPlugin plugin) {
        fPlugin = plugin;
    }
    
    /**
     * Returns the shared image represented by the given key.
     * 
     * @param imageName
     *          the logical name of the image to retrieve
     * @return the shared image represented by the given key
     */
    public Image getImage(String imageName) {
        if(imageName == null || ECLIPSE_IMAGE_FILE.equals(imageName)) {
            return getSharedImage(ISharedImages.IMG_OBJ_FILE);
        }

        if(ECLIPSE_IMAGE_FOLDER.equals(imageName)) {
            return getSharedImage(ISharedImages.IMG_OBJ_FOLDER);
        }

        ImageRegistry registry = fPlugin.getImageRegistry();

        Image image = registry.get(imageName);
        
        // Make it and cache it
        if(image == null) {
            ImageDescriptor descriptor = getImageDescriptor(imageName);
            if(descriptor != null) {
                image = descriptor.createImage();
                if(image != null) {
                    registry.put(imageName, image);
                }
                else {
                    Logger.logError("ImageFactory: Error creating image for " + imageName); //$NON-NLS-1$
                }
            }
        }
        
        return image;
    }
    
    /**
     * Return a composite overlay image
     * 
     * @param imageName
     * @param overlayName
     * @param quadrant the quadrant (one of {@link IDecoration} 
     * ({@link IDecoration#TOP_LEFT}, {@link IDecoration#TOP_RIGHT},
     * {@link IDecoration#BOTTOM_LEFT}, {@link IDecoration#BOTTOM_RIGHT} 
     * or {@link IDecoration#UNDERLAY})
     * @return
     */
    public Image getOverlayImage(String imageName, String overlayName, int quadrant) {
        // Make a registry name, cached
        String key_name = imageName + overlayName + quadrant;
        
        Image image = getImage(key_name);
        
        // Make it and cache it
        if(image == null) {
            Image underlay = getImage(imageName);
            ImageDescriptor overlay = getImageDescriptor(overlayName);
            if(underlay != null && overlay != null) {
                image = new DecorationOverlayIcon(underlay, overlay, quadrant).createImage();
                if(image != null) {
                    ImageRegistry registry = fPlugin.getImageRegistry();
                    registry.put(key_name, image);
                }
            }
        }
        
        return image;
    }
    
    /**
     * Return a composite image consisting of many images
     * 
     * @param imageNames
     * @return
     */
    public Image getCompositeImage(String[] imageNames) {
        // Make a registry name, cached
        String key_name = "@";
        for(String name : imageNames) {
            key_name += name;
        }

        Image image = getImage(key_name);
        
        // Make it and cache it
        if(image == null) {
            ImageDescriptor[] desc = new ImageDescriptor[imageNames.length];
            for(int i = 0; i < imageNames.length; i++) {
                desc[i] = getImageDescriptor(imageNames[i]);
            }
            CompositeMultiImageDescriptor cid = new CompositeMultiImageDescriptor(desc);
            image = cid.createImage();
            if(image != null) {
                ImageRegistry registry = fPlugin.getImageRegistry();
                registry.put(key_name, image);
            }
        }

        return image;
    }

    /**
     * Returns the shared image description represented by the given key.
     * 
     * @param imageName
     *          the logical name of the image description to retrieve
     * @return the shared image description represented by the given name
     */
    public ImageDescriptor getImageDescriptor(String imageName) {
        // Null or File
        if(imageName == null || ECLIPSE_IMAGE_FILE.equals(imageName)) {
            return getSharedImageDescriptor(ISharedImages.IMG_OBJ_FILE);
        }
        // Folder
        if(ECLIPSE_IMAGE_FOLDER.equals(imageName)) {
            return getSharedImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
        }
        // View
        if(ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui.views", "$nl$/icons/full/eview16/prop_ps.gif"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // Outline View
        if(ECLIPSE_IMAGE_OUTLINE_VIEW_ICON.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui.views", "$nl$/icons/full/eview16/outline_co.gif"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // New Wizard
        if(ECLIPSE_IMAGE_NEW_WIZARD.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/wizban/new_wiz.png"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // Import Prefs Wizard
        if(ECLIPSE_IMAGE_IMPORT_PREF_WIZARD.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/wizban/importpref_wiz.png"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // User
        return AbstractUIPlugin.imageDescriptorFromPlugin(fPlugin.getBundle().getSymbolicName(), imageName);
    }

    /**
     * @param name
     * @return A shared Image from the system
     */
    public static Image getSharedImage(String name) {
        return PlatformUI.getWorkbench().getSharedImages().getImage(name);
    }
    
    /**
     * @param name
     * @return A shared ImageDescriptor from the system
     */
    public static ImageDescriptor getSharedImageDescriptor(String name) {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(name);
    }
    
    /**
     * Get a scaled image 
     * @param uri The URI to access the image from.  If local, prefix with "file:///"
     * @param maxHeight The maximum height to resize the image to.  If -1, no resizing takes place
     * @return The Image or null
     */
    public static Image getScaledImage(URL url, int maxHeight) {
        if(url == null) {
            return null;
        }
        
        Image image = null;
        
        // This needs caching!!!!!
        
        try {
            ImageDescriptor id = ImageDescriptor.createFromURL(url);
            ImageData imageData = id.getImageData();
            if(imageData == null) {
                return null;
            }
            
            // Make smaller (within reason)
            if(maxHeight > 4) {
                int height = imageData.height;
                int width = imageData.width;
                if(height > maxHeight) {
                    height = maxHeight;
                    width *= ((float)maxHeight / imageData.height);
                    if(width > 10){ // Ensure a sensible size
                        imageData = imageData.scaledTo(width, height);
                    }
                }
            }
            
            image = new Image(Display.getCurrent(), imageData);
    
        }
        catch(Exception ex) {
            //ex.printStackTrace();
        }
        
        return image;
    }
    
    /**
     * @param image
     * @param width
     * @param height
     * @return A scaled image to width and height
     */
    public static Image getScaledImage(Image image, int width, int height) {
        if(image == null) {
            return null;
        }

        // This needs caching!!!!!
        
        ImageData imageData = image.getImageData();
        imageData = imageData.scaledTo(width, height);

        return new Image(Display.getCurrent(), imageData);
    }

    /**
     * @param eObject
     * @return An Image for Object
     */
    public static Image getImage(EObject eObject) {
        Object type = null;
        if(eObject instanceof IInterfaceElement) {
            type = ((IInterfaceElement)eObject).getInterfaceType();
        }
        return getImage(eObject.eClass(), type);
    }
    
    public static final Image getImage(EClass eClass, Object type) {
        return IArchimateImages.ImageFactory.getImage(getImageName(eClass, type));
    }

    public static final Image getImage(EClass eClass) {
        return getImage(eClass, null);
    }
    
    public static ImageDescriptor getImageDescriptor(EObject eObject) {
        return getImageDescriptor(eObject.eClass());
    }
    
    public static final ImageDescriptor getImageDescriptor(EClass eClass, Object type) {
        return IArchimateImages.ImageFactory.getImageDescriptor(getImageName(eClass, type));
    }

    public static final ImageDescriptor getImageDescriptor(EClass eClass) {
        return getImageDescriptor(eClass, null);
    }
    
    private static final String getImageName(EClass eClass, Object type) {
        switch(eClass.getClassifierID()) {
            // Business
            case IArchimatePackage.BUSINESS_ACTIVITY:
                return IArchimateImages.ICON_BUSINESS_ACTIVITY_16;
            case IArchimatePackage.BUSINESS_ACTOR:
                return IArchimateImages.ICON_BUSINESS_ACTOR_16;
            case IArchimatePackage.BUSINESS_COLLABORATION:
                return IArchimateImages.ICON_BUSINESS_COLLABORATION_16;
            case IArchimatePackage.CONTRACT:
                return IArchimateImages.ICON_BUSINESS_CONTRACT_16;
            case IArchimatePackage.BUSINESS_EVENT:
                return IArchimateImages.ICON_BUSINESS_EVENT_16;
            case IArchimatePackage.BUSINESS_FUNCTION:
                return IArchimateImages.ICON_BUSINESS_FUNCTION_16;
            case IArchimatePackage.BUSINESS_INTERACTION:
                return IArchimateImages.ICON_BUSINESS_INTERACTION_16;
            case IArchimatePackage.BUSINESS_INTERFACE:
                if(type != null && type.equals(IInterfaceElement.REQUIRED)) {
                    return IArchimateImages.ICON_INTERFACE_REQUIRED_16;
                }
                return IArchimateImages.ICON_BUSINESS_INTERFACE_16;
            case IArchimatePackage.MEANING:
                return IArchimateImages.ICON_BUSINESS_MEANING_16;
            case IArchimatePackage.BUSINESS_OBJECT:
                return IArchimateImages.ICON_BUSINESS_OBJECT_16;
            case IArchimatePackage.BUSINESS_PROCESS:
                return IArchimateImages.ICON_BUSINESS_PROCESS_16;
            case IArchimatePackage.PRODUCT:
                return IArchimateImages.ICON_BUSINESS_PRODUCT_16;
            case IArchimatePackage.REPRESENTATION:
                return IArchimateImages.ICON_BUSINESS_REPRESENTATION_16;
            case IArchimatePackage.BUSINESS_ROLE:
                return IArchimateImages.ICON_BUSINESS_ROLE_16;
            case IArchimatePackage.BUSINESS_SERVICE:
                return IArchimateImages.ICON_BUSINESS_SERVICE_16;
            case IArchimatePackage.VALUE:
                return IArchimateImages.ICON_BUSINESS_VALUE_16;
                
            // Application
            case IArchimatePackage.APPLICATION_COLLABORATION:
                return IArchimateImages.ICON_APPLICATION_COLLABORATION_16;
            case IArchimatePackage.APPLICATION_COMPONENT:
                return IArchimateImages.ICON_APPLICATION_COMPONENT_16;
            case IArchimatePackage.APPLICATION_FUNCTION:
                return IArchimateImages.ICON_APPLICATION_FUNCTION_16;
            case IArchimatePackage.APPLICATION_INTERACTION:
                return IArchimateImages.ICON_APPLICATION_INTERACTION_16;
            case IArchimatePackage.APPLICATION_INTERFACE:
                if(type != null && type.equals(IInterfaceElement.REQUIRED)) {
                    return IArchimateImages.ICON_INTERFACE_REQUIRED_16;
                }
                return IArchimateImages.ICON_APPLICATION_INTERFACE_16;
            case IArchimatePackage.DATA_OBJECT:
                return IArchimateImages.ICON_APPLICATION_DATA_OBJECT_16;
            case IArchimatePackage.APPLICATION_SERVICE:
                return IArchimateImages.ICON_APPLICATION_SERVICE_16;

            // Technology
            case IArchimatePackage.ARTIFACT:
                return IArchimateImages.ICON_TECHNOLOGY_ARTIFACT_16;
            case IArchimatePackage.COMMUNICATION_PATH:
                return IArchimateImages.ICON_TECHNOLOGY_COMMUNICATION_PATH_16;
            case IArchimatePackage.NETWORK:
                return IArchimateImages.ICON_TECHNOLOGY_NETWORK_16;
            case IArchimatePackage.INFRASTRUCTURE_INTERFACE:
                if(type != null && type.equals(IInterfaceElement.REQUIRED)) {
                    return IArchimateImages.ICON_INTERFACE_REQUIRED_16;
                }
                return IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_INTERFACE_16;
            case IArchimatePackage.INFRASTRUCTURE_SERVICE:
                return IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_SERVICE_16;
            case IArchimatePackage.NODE:
                return IArchimateImages.ICON_TECHNOLOGY_NODE_16;
            case IArchimatePackage.SYSTEM_SOFTWARE:
                return IArchimateImages.ICON_TECHNOLOGY_SYSTEM_SOFTWARE_16;
            case IArchimatePackage.DEVICE:
                return IArchimateImages.ICON_TECHNOLOGY_DEVICE_16;
                
            // Junctions
            case IArchimatePackage.JUNCTION:
                return IArchimateImages.ICON_JUNCTION_16;
            case IArchimatePackage.AND_JUNCTION:
                return IArchimateImages.ICON_JUNCTION_AND_16;
            case IArchimatePackage.OR_JUNCTION:
                return IArchimateImages.ICON_JUNCTION_OR_16;
                
            // Relationships
            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return IArchimateImages.ICON_ACESS_CONNECTION_16;
            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return IArchimateImages.ICON_AGGREGATION_CONNECTION_16;
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return IArchimateImages.ICON_ASSIGNMENT_CONNECTION_16;
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return IArchimateImages.ICON_ASSOCIATION_CONNECTION_16;
            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return IArchimateImages.ICON_COMPOSITION_CONNECTION_16;
            case IArchimatePackage.FLOW_RELATIONSHIP:
                return IArchimateImages.ICON_FLOW_CONNECTION_16;
            case IArchimatePackage.REALISATION_RELATIONSHIP:
                return IArchimateImages.ICON_REALISATION_CONNECTION_16;
            case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                return IArchimateImages.ICON_SPECIALISATION_CONNECTION_16;
            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return IArchimateImages.ICON_TRIGGERING_CONNECTION_16;
            case IArchimatePackage.USED_BY_RELATIONSHIP:
                return IArchimateImages.ICON_USED_BY_CONNECTION_16;
                
            // Other
            case IArchimatePackage.ARCHIMATE_MODEL:
                return IArchimateImages.ICON_MODELS_16;
            case IArchimatePackage.DIAGRAM_MODEL:
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE:
                return IArchimateImages.ICON_DIAGRAM_16;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP:
                return IArchimateImages.ICON_GROUP_16;
            case IArchimatePackage.FOLDER:
                return "folder";
            case IArchimatePackage.DIAGRAM_MODEL_NOTE:
                return IArchimateImages.ICON_NOTE_16;
            
            // Sketch
            case IArchimatePackage.SKETCH_MODEL:
                return IArchimateImages.ICON_SKETCH_16;
            case IArchimatePackage.SKETCH_MODEL_ACTOR:
                return IArchimateImages.ICON_ACTOR_16;
            case IArchimatePackage.SKETCH_MODEL_STICKY:
                return IArchimateImages.ICON_STICKY_16;
                
            // Other connections
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION:
                if(DiagramConstants.CONNECTION_ARROW.equals(type)) {
                    return IArchimateImages.ICON_TRIGGERING_CONNECTION_16;
                }
                if(DiagramConstants.CONNECTION_DASHED_ARROW.equals(type)) {
                    return IArchimateImages.ICON_FLOW_CONNECTION_16;
                }
                return IArchimateImages.ICON_ASSOCIATION_CONNECTION_16;
        }
        
        return null;
    }

}