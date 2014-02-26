/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;


/**
 * Manages Label Provider Extensions
 * 
 * @author Phillip Beauvoir
 */
public class LabelProviderExtensionHandler {
    
    public static LabelProviderExtensionHandler INSTANCE = new LabelProviderExtensionHandler();
    
    private List<IEditorLabelProvider> factories = new ArrayList<IEditorLabelProvider>();
    
    private LabelProviderExtensionHandler() {
        registerProviders();
    }
    
    /**
     * @return All Registered Label Providers 
     */
    public List<IEditorLabelProvider> getRegisteredProviders() {
        return factories;
    }
    
    private void registerProviders() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(IEditorLabelProvider.EXTENSIONPOINT)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                IEditorLabelProvider provider = (IEditorLabelProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && provider != null) {
                    factories.add(provider);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }

    /**
     * Return a text label for a given element in a registered Label Provider Extension
     * @param element The element to find the label for
     * @return The label if found, or null
     */
    public String getLabel(Object element) {
        for(IEditorLabelProvider provider : getRegisteredProviders()) {
            String label = provider.getLabel(element);
            if(label != null) {
                return label;
            }
        }
        
        return null;
    }
    
    /**
     * Return an image for a given element in a registered Label Provider Extension
     * @param element The element to find the image for
     * @return The image if found, or null
     */
    public Image getImage(Object element) {
        for(IEditorLabelProvider provider : getRegisteredProviders()) {
            Image image = provider.getImage(element);
            if(image != null) {
                return image;
            }
        }
        
        return null;
    }
}
