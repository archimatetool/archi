/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;



/**
 * Manages IImageExportProvider extension providers
 * 
 * @author Phillip Beauvoir
 */
public class ImageExportProviderManager {
    
    public static class ImageExportProviderInfo {
        private IImageExportProvider provider;
        private String id, label;
        private List<String> extensions;

        public ImageExportProviderInfo(IImageExportProvider provider, String id, String label, String extensions) {
            this.provider = provider;
            this.id = id;
            this.label = label;
            extensions = extensions.replace(" ", ""); // remove all spaces //$NON-NLS-1$ //$NON-NLS-2$
            this.extensions = Arrays.asList(extensions.split(",")); //$NON-NLS-1$
        }
        
        public IImageExportProvider getProvider() {
            return provider;
        }
        
        public String getID() {
            return id;
        }
        
        public String getLabel() {
            return label;
        }
        
        public List<String> getExtensions() {
            return extensions;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + " [id: " + id + ", name: " + label + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }
    
    
    private static List<ImageExportProviderInfo> imageProviders;;

    public static List<ImageExportProviderInfo> getImageExportProviders() {
        if(imageProviders == null) {
            registerImageExportProviders();
            
            Collections.sort(imageProviders, new Comparator<ImageExportProviderInfo>() {
                @Override
                public int compare(ImageExportProviderInfo p1, ImageExportProviderInfo p2) {
                    return p1.getLabel().compareTo(p2.getLabel());
                }
            });
        }
        
        return new ArrayList<ImageExportProviderInfo>(imageProviders);
    }
    

    /**
     * Register all export providers
     */
    private static void registerImageExportProviders() {
        imageProviders = new ArrayList<ImageExportProviderInfo>();
        
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(IImageExportProvider.FORMATPROVIDEREXTENSION_ID)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                String label = configurationElement.getAttribute("label"); //$NON-NLS-1$
                String extensions = configurationElement.getAttribute("extensions"); //$NON-NLS-1$
                IImageExportProvider provider = (IImageExportProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && label != null && provider != null) {
                    ImageExportProviderInfo providerInfo = new ImageExportProviderInfo(provider, id, label, extensions);
                    imageProviders.add(providerInfo);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
    

}
