/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;



/**
 * Abstract Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractElementUIProvider implements IElementUIProvider {
    
    private static ImageRegistry fImageRegistry = new ImageRegistry();
    
    @Override
    public Image getImage(EObject instance) {
        return getImage();
    }
    
    @Override
    public String getDefaultShortName() {
        return getDefaultName();
    }
    
    @Override
    public Color getDefaultColor() {
        return ColorConstants.white;
    }
    
    /**
     * Create a new ImageDescriptor substituting the user's preference for fill color
     */
    protected ImageDescriptor getImageDescriptorWithUserFillColor(String imageName) {
        // Not a preference
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_FILL_COLORS_IN_GUI)) {
            return IArchimateImages.ImageFactory.getImageDescriptor(imageName);
        }
        
        ImageDescriptor newImageDescriptor = fImageRegistry.getDescriptor(imageName);
        
        // Create new ImageDescriptor
        if(newImageDescriptor == null) {
            ImageDescriptor originalImageDescriptor = IArchimateImages.ImageFactory.getImageDescriptor(imageName);
            
            Color color = ColorFactory.getDefaultFillColor(providerFor());

            // No default color
            if(color == null) {
                return originalImageDescriptor;
            }

            ImageData imageData = originalImageDescriptor.getImageData();

            for(int i = 0; i < imageData.width; i++) {
                for(int j = 0; j < imageData.height; j++) {
                    RGB rgb = imageData.palette.getRGB(imageData.getPixel(i, j));
                    if(rgb.red > 0) {
                        imageData.setPixel(i, j, ColorFactory.getPixelValue(color.getRGB()));
                    }
                }
            }

            newImageDescriptor = ImageDescriptor.createFromImageData(imageData);
            fImageRegistry.put(imageName, newImageDescriptor);
        }

        return newImageDescriptor;
    }

    /**
     * Create a new Image substituting the user's preference for fill color
     * 
     * We can't dispose of any previous Image as it will still be referenced elsewhere in the application
     */
    protected Image getImageWithUserFillColor(String imageName) {
        // Not a preference
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_FILL_COLORS_IN_GUI)) {
            return IArchimateImages.ImageFactory.getImage(imageName);
        }
        
        Image image = fImageRegistry.get(imageName);
        
        if(image == null) {
            // Create ImageDescriptor and try again
            getImageDescriptorWithUserFillColor(imageName);
            image = fImageRegistry.get(imageName);
        }

        return image;
    }
}
