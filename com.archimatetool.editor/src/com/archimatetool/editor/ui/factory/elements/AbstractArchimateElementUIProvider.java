/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;



/**
 * Abstract Archimate Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateElementUIProvider extends AbstractGraphicalObjectUIProvider
implements IArchimateElementUIProvider {
    
    private static ImageRegistry fImageRegistry = new ImageRegistry();
    
    protected AbstractArchimateElementUIProvider() {
    }
    
    protected AbstractArchimateElementUIProvider(EObject instance) {
        super(instance);
    }
    
    @Override
    public Dimension getDefaultSize() {
        return DefaultRectangularSize;
    }

    @Override
    public Dimension getUserDefaultSize() {
        int width = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH);
        int height = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT);
        return new Dimension(width, height);
    }
    
    @Override
    public boolean hasAlternateFigure() {
        return false;
    }
    
    /**
     * Create a new ImageDescriptor substituting the user's preference for fill color
     */
    protected ImageDescriptor getImageDescriptorWithUserFillColor(String imageName) {
        // Not a preference
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_FILL_COLORS_IN_GUI)) {
            return IArchiImages.ImageFactory.getImageDescriptor(imageName);
        }
        
        ImageDescriptor newImageDescriptor = fImageRegistry.getDescriptor(imageName);
        
        // Create new ImageDescriptor
        if(newImageDescriptor == null) {
            ImageDescriptor originalImageDescriptor = IArchiImages.ImageFactory.getImageDescriptor(imageName);
            
            Color color = ColorFactory.getUserDefaultFillColor(providerFor());

            // No user default color
            if(color == null) {
                return originalImageDescriptor;
            }

            ImageData imageData = originalImageDescriptor.getImageData(100);

            for(int i = 0; i < imageData.width; i++) {
                for(int j = 0; j < imageData.height; j++) {
                    RGB rgb = imageData.palette.getRGB(imageData.getPixel(i, j));
                    if(rgb.red > 0) {
                        imageData.setPixel(i, j, ColorFactory.getPixelValue(color.getRGB()));
                    }
                }
            }

            newImageDescriptor = ImageDescriptor.createFromImageDataProvider(zoom -> imageData); // Not sure how this works!
            fImageRegistry.put(imageName, newImageDescriptor);
        }

        return newImageDescriptor;
    }

    /**
     * Get a new Image substituting the user's preference for fill color.
     * If there is no user fill color or preference not set, then
     * 
     * We can't dispose of any previous Image as it will still be referenced elsewhere in the application
     */
    protected Image getImageWithUserFillColor(String imageName) {
        // Not a preference
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_FILL_COLORS_IN_GUI)) {
            return IArchiImages.ImageFactory.getImage(imageName);
        }
        
        Image image = fImageRegistry.get(imageName);
        
        if(image == null) {
            // Create local ImageDescriptor and try again
            getImageDescriptorWithUserFillColor(imageName);
            image = fImageRegistry.get(imageName);
            
            // If image is still null then we didn't make a new one and so need the default image
            if(image == null) {
                return IArchiImages.ImageFactory.getImage(imageName);
            }
        }

        return image;
    }
}
