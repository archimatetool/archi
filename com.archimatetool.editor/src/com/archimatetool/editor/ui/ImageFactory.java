/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.components.CompositeMultiImageDescriptor;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Image Factory
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class ImageFactory {
    
    private AbstractUIPlugin fPlugin;
    
    /**
     * @return The actual device zoom level.
     */
    public static int getDeviceZoom() {
        // Note - Not sure if we need this any more...but just in case
        Display.getDefault();
        
        return DPIUtil.getDeviceZoom();
        
        // Alternate method which I suppose is the official way
        //String deviceZoom = System.getProperty("org.eclipse.swt.internal.deviceZoom"); //$NON-NLS-1$
        //return deviceZoom == null ? 100 : Integer.parseInt(deviceZoom);
    }

    /**
     * @return The zoom level for creating images.
     * Windows OS with scaling > 100 and Mac Retina since Eclipse 4.12 needs to export images at x2 size
     * If Preferences are set to not use a scaled device zoom then return 100
     */
    public static int getImageDeviceZoom() {
        boolean scaleImages = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SCALE_IMAGE_EXPORT);
        int deviceZoom = getDeviceZoom();
        // If scaling prefs x2 is true and device zoom is 100 then return 200
        // Else return device zoom
        // Else 100
        return scaleImages ? (deviceZoom == 100) ? 200 : deviceZoom : 100;
    }
    
    /**
     * @return The zoom level for creating cursors.
     * Windows uses the device zoom
     * Mac and Linux uses 100
     */
    public static int getCursorDeviceZoom() {
        return PlatformUtils.isWindows() ? getDeviceZoom() : 100;
    }
    
    /**
     * @param plugin The plug-in where we can find the images
     */
    public ImageFactory(AbstractUIPlugin plugin) {
        fPlugin = plugin;
    }
    
    /**
     * @return The plugin that owns this ImageFactory
     */
    public AbstractUIPlugin getPlugin() {
        return fPlugin;
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
     * Return a composite image with overlay image
     * 
     * @param underlay The underlay image
     * @param overlayName Name of the overlay image
     * @param quadrant the quadrant (one of {@link IDecoration} 
     * ({@link IDecoration#TOP_LEFT}, {@link IDecoration#TOP_RIGHT},
     * {@link IDecoration#BOTTOM_LEFT}, {@link IDecoration#BOTTOM_RIGHT} 
     * or {@link IDecoration#UNDERLAY})
     * @return The image
     */
    public Image getOverlayImage(Image underlay, String overlayName, int quadrant) {
        String key = underlay.hashCode() + overlayName + quadrant;
        
        Image newImage = getImage(key);
        if(newImage == null) {
            ImageDescriptor overlayDescripter = getImageDescriptor(overlayName);
            if(overlayDescripter != null) {
                newImage = new DecorationOverlayIcon(underlay, overlayDescripter, quadrant).createImage();
                if(newImage != null) {
                    ImageRegistry registry = fPlugin.getImageRegistry();
                    registry.put(key, newImage);
                }
            }
        }
        
        return newImage != null ? newImage : underlay;
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
     * Get an Image with the given RGB value instead of the original palette value
     */
    public Image getImageWithRGB(String imageName, RGB rgb) {
        String rgbName = imageName + ColorFactory.convertRGBToString(rgb);
        ImageRegistry registry = fPlugin.getImageRegistry();
        
        Image image = registry.get(rgbName);
        
        // Create local ImageDescriptor and try again
        if(image == null) {
            getImageDescriptorWithRGB(imageName, rgb);
            image = registry.get(rgbName);
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
            id = ResourceLocator.imageDescriptorFromBundle(fPlugin.getBundle().getSymbolicName(), imageName).orElse(null);
            if(id != null) {
                registry.put(imageName, id); // The image will be created next when registry.get(imageName) is called
            }
            else {
                // Can be null in the case of overlay images where the image name is a composite name
                //System.err.println("Could not get Image Descriptor for: " + imageName); //$NON-NLS-1$
            }
        }
        
        return id;
    }

    /**
     * Get an ImageDescriptor with the given RGB value instead of the original palette value
     */
    public ImageDescriptor getImageDescriptorWithRGB(String imageName, RGB rgb) {
        String rgbName = imageName + ColorFactory.convertRGBToString(rgb);
        
        ImageRegistry registry = fPlugin.getImageRegistry();
        ImageDescriptor newImageDescriptor = registry.getDescriptor(rgbName);
        
        // Create a new ImageDescriptor that sets the pixel to RGB
        if(newImageDescriptor == null) {
            ImageDescriptor id = getImageDescriptor(imageName);
            if(id != null) {
                newImageDescriptor = new ImageDescriptor() {
                    @Override
                    public ImageData getImageData(int zoom) {
                        ImageData imageData = getImageDescriptor(imageName).getImageData(zoom);
                        if(imageData != null) {
                            int pixel = imageData.palette.getPixel(rgb);
                            for(int x = 0; x < imageData.width; x++) {
                                for(int y = 0; y < imageData.height; y++) {
                                    imageData.setPixel(x, y, pixel);
                                }
                            }
                        }
                        return imageData;
                    }
                };
                
                registry.put(rgbName, newImageDescriptor);
            }
         }

        return newImageDescriptor;
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
        ImageData sourceImageData = source.getImageData(getDeviceZoom());
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
        
        Rectangle newSize = getScaledImageSize(source, maxSize);
        return getScaledImage(source, newSize.width, newSize.height);
    }
    
    /**
     * Calculate a new relative image size so that either the width or height will be no bigger than maxSize
     * @param source the Image source
     * @param maxSize the maximum width or height. 
     * @return The new scaled size
     */
    public static Rectangle getScaledImageSize(Image source, int maxSize) {
        Rectangle srcBounds = source.getBounds();
        return getScaledSize(srcBounds.width, srcBounds.height, maxSize);
    }
    
    /**
     * Calculate a new relative size so that either the width or height will be no bigger than maxSize
     * @param width the original width
     * @param height the original height
     * @param maxSize the maximum width or height. 
     * @return The new scaled size
     */
    public static Rectangle getScaledSize(int width, int height, int maxSize) {
        if(width <= 0 || height <= 0) {
            return new Rectangle(0, 0, 0, 0);
        }

        // If already small enough, return original
        if(width <= maxSize && height <= maxSize) {
            return new Rectangle(0, 0, width, height);
        }

        float w = width;
        float h = height;
        float ratio = Math.min(maxSize / w, maxSize / h);

        return new Rectangle(0, 0, Math.round(w * ratio), Math.round(h * ratio));
    }
    
    /**
     * Flip an image.
     * This can be used on Mac Sonoma and later because there is a problem with Control#setBackgroundImage
     * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/772
     * 
     * @return The flipped image. Caller is responsible for disposing of it.
     * 
     * This code taken from https://github.com/knime/knime-product/commit/095c88e22ba6d84e22a480f50734141836098169
     */
    public static Image getFlippedImage(Image source) {
        Rectangle bounds = source.getBounds();
        Image newImage = new Image(source.getDevice(), bounds.width, bounds.height);
        
        GC gc = new GC(newImage);
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        
        Transform transform = new Transform(source.getDevice());
        // flip down
        transform.setElements(1, 0, 0, -1, 0, 0);
        // move up
        transform.translate(0, -bounds.height);
        gc.setTransform(transform);
        
        gc.drawImage(source, 0, 0, bounds.width, bounds.height,
                0, 0, bounds.width, bounds.height);
        
        gc.dispose();
        transform.dispose();
        
        return newImage;
    }

}