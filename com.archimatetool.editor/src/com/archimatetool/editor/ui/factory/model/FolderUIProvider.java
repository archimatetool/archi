/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;



/**
 * Folder UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class FolderUIProvider extends AbstractObjectUIProvider {
    
    // This is the actual color in the default icon
    private static Color DEFAULT_COLOR = new Color(255, 199, 63);
    
    public static Color getFolderColor(FolderType type) {
        String colorValue = ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.FOLDER_COLOUR_PREFIX + type.getName());
        
        if(StringUtils.isSet(colorValue)) {
            return ColorFactory.get(colorValue);
        }
        
        return DEFAULT_COLOR;
    }
    
    public static Color getDefaultFolderColor(FolderType type) {
        String colorValue = ArchiPlugin.PREFERENCES.getDefaultString(IPreferenceConstants.FOLDER_COLOUR_PREFIX + type.getName());
        
        if(StringUtils.isSet(colorValue)) {
            return ColorFactory.get(colorValue);
        }
        
        return DEFAULT_COLOR;
    }
    
    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getFolder();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.FolderUIProvider_0;
    }

    @Override
    public Image getImage() {
        if(instance instanceof IFolder) {
            Color color = getFolderColor(getFolderType((IFolder)instance));
            if(!DEFAULT_COLOR.equals(color)) {
                return IArchiImages.ImageFactory.getImageWithRGB(IArchiImages.ICON_FOLDER_DEFAULT, color.getRGB());
            }
        }
        
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_FOLDER_DEFAULT);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        if(instance instanceof IFolder) {
            Color color = getFolderColor(getFolderType((IFolder)instance));
            if(!DEFAULT_COLOR.equals(color)) {
                return IArchiImages.ImageFactory.getImageDescriptorWithRGB(IArchiImages.ICON_FOLDER_DEFAULT, color.getRGB());
            }
        }
        
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FOLDER_DEFAULT);
    }
    
    private FolderType getFolderType(IFolder folder) {
        if(folder.getType() != FolderType.USER) {
            return folder.getType();
        }
        
        EObject eObject = folder;
        
        while(eObject.eContainer() instanceof IFolder) {
            eObject = eObject.eContainer();
        }
        
        return ((IFolder)eObject).getType();
    }
}
