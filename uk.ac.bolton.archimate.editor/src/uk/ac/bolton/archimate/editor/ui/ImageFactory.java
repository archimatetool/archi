/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import uk.ac.bolton.archimate.editor.ui.components.CompositeMultiImageDescriptor;


/**
 * Image Factory
 * 
 * @author Phillip Beauvoir
 */
public class ImageFactory {
    
    public static final String ECLIPSE_IMAGE_NEW_WIZARD = "new_wizard"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_IMPORT_PREF_WIZARD = "import_pref_wizard";  //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_EXPORT_PREF_WIZARD = "export_pref_wizard"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_IMPORT_DIR_WIZARD = "import_dir_wizard"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_EXPORT_DIR_WIZARD = "export_dir_wizard"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON = "properties_view"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_OUTLINE_VIEW_ICON = "outline_view"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_FILE = "file"; //$NON-NLS-1$
    public static final String ECLIPSE_IMAGE_FOLDER = "folder"; //$NON-NLS-1$

    
    private AbstractUIPlugin fPlugin;
    
    /**
     * @param plugin The plug-in where we can find the images
     */
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
        if(image == null) {
            // Image will be created in registry.get(imageName) after image descriptor is put into registry
            getImageDescriptor(imageName);
            image = registry.get(imageName);
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

        ImageRegistry registry = fPlugin.getImageRegistry();
        Image image = registry.get(key_name);
        
        if(image == null) {
            // Image will be created in registry.get(imageName) after image descriptor is put into registry
            getCompositeImageDescriptor(imageNames);
            image = registry.get(key_name);
        }

        return image;
    }
    
    /**
     * Return a composite image consisting of many images
     * 
     * @param imageNames
     * @return
     */
    public CompositeImageDescriptor getCompositeImageDescriptor(String[] imageNames) {
        // Make a registry name, cached
        String key_name = "@";
        for(String name : imageNames) {
            key_name += name;
        }
        
        ImageRegistry registry = fPlugin.getImageRegistry();
        
        CompositeImageDescriptor cid = (CompositeImageDescriptor)registry.getDescriptor(key_name);
        
        // Make it and cache it
        if(cid == null) {
            ImageDescriptor[] desc = new ImageDescriptor[imageNames.length];
            for(int i = 0; i < imageNames.length; i++) {
                desc[i] = getImageDescriptor(imageNames[i]);
            }
            cid = new CompositeMultiImageDescriptor(desc);
            registry.put(key_name, cid);
        }
        
        return cid;
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
        // Export Prefs Wizard
        if(ECLIPSE_IMAGE_EXPORT_PREF_WIZARD.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/wizban/exportpref_wiz.png"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // Import Dir Wizard
        if(ECLIPSE_IMAGE_IMPORT_DIR_WIZARD.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/wizban/importdir_wiz.png"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // Export Dir Wizard
        if(ECLIPSE_IMAGE_EXPORT_DIR_WIZARD.equals(imageName)) {
            return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/wizban/exportdir_wiz.png"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // User image, cache it
        ImageRegistry registry = fPlugin.getImageRegistry();
        ImageDescriptor id = registry.getDescriptor(imageName);
        if(id == null) {
            id = AbstractUIPlugin.imageDescriptorFromPlugin(fPlugin.getBundle().getSymbolicName(), imageName);
            registry.put(imageName, id); // The image will be created next when registry.get(imageName) is called
        }
        
        return id;
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
     * Rescale an image to given width and height
     * @param source The Image source
     * @param width New width
     * @param height New height
     * @return A new scaled image
     */
    public static Image getScaledImage(Image source, int width, int height) {
        // This method uses far less memory than scaling the Image's ImageData and gives a better result whe anti-aliasing is on
        Rectangle srcBounds = source.getBounds();
        Image image = new Image(Display.getCurrent(), width, height);
        GC gc = new GC(image);
        gc.setAntialias(SWT.ON); // Slower, but draws without artifacts
        gc.drawImage(source, 0, 0, srcBounds.width, srcBounds.height, 0, 0, width, height);
        gc.dispose();
        return image;
    }
    
    /**
     * Rescale the Image to max size (width or height)
     * @param source the Image source
     * @param maxSize the maximum width or size. Will always be a minimum of 10.
     * @return A new scaled image
     */
    public static Image getScaledImage(Image source, int maxSize) {
        if(maxSize < 10) {
            maxSize = 10;
        }
        
        Rectangle srcBounds = source.getBounds();
        int width = srcBounds.width;
        int height = srcBounds.height;

        if(height > maxSize) {
            width *= ((float)maxSize / height);
            height = maxSize;
        }
        if(width > maxSize) {
            height *= ((float)maxSize / width);
            width = maxSize;
        }
        
        return getScaledImage(source, width, height);
    }
}