/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.archimatetool.editor.ui.components.CompositeMultiImageDescriptor;



/**
 * Image Factory
 * 
 * @author Phillip Beauvoir
 */
public class ImageFactory {
    
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
        if(imageName == null) {
            throw new IllegalArgumentException("Image name cannot be null"); //$NON-NLS-1$
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
        String key_name = "@"; //$NON-NLS-1$
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
        String key_name = "@"; //$NON-NLS-1$
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
        if(imageName == null) {
            throw new IllegalArgumentException("Image name cannot be null"); //$NON-NLS-1$
        }
        
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
        // ImageData#scaledTo does not use interpolation, so resized images look bad.
        // Here we draw to a GC which is better quality.
        
        Image image;
        
        // If there is a transparency pixel set copy the source ImageData to preserve it
        ImageData sourceImageData = source.getImageData();
        if(sourceImageData.transparentPixel != -1) {
            ImageData id = new ImageData(width, height, sourceImageData.depth, sourceImageData.palette);
            id.transparentPixel = sourceImageData.transparentPixel;
            image = new Image(source.getDevice(), id);
        }
        // Else use a blank image
        else {
            image = new Image(source.getDevice(), width, height);
        }
        
        GC gc = new GC(image);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        
        // This is necessary for images that have the transparency pixel set
        Color transparentColor = source.getBackground();
        if(transparentColor != null) {
            gc.setBackground(transparentColor);
            gc.fillRectangle(0, 0, width, height);
        }
        
        gc.drawImage(source, 0, 0, source.getBounds().width, source.getBounds().height, 0, 0, width, height);
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