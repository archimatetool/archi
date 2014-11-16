/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.Logger;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Factory for Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ElementUIFactory {

    public static final ElementUIFactory INSTANCE = new ElementUIFactory();
    
    static {
        INSTANCE.registerProviders();
    }
    
    Map<EClass, IElementUIProvider> map = new HashMap<EClass, IElementUIProvider>();
    
    ElementUIFactory() {
    }
    
    private void registerProviders() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(IElementUIProvider.EXTENSIONPOINT_ID)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                IElementUIProvider provider = (IElementUIProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && provider != null) {
                    registerProvider(provider);
                }
            } 
            catch(CoreException ex) {
                Logger.logError("Cannot register UI Provider", ex); //$NON-NLS-1$
                ex.printStackTrace();
            } 
        }
    }
    
    void registerProvider(IElementUIProvider provider) {
        map.put(provider.providerFor(), provider);
    }
    
    public IElementUIProvider getProvider(EClass eClass) {
        return map.get(eClass);
    }
    
    public IElementUIProvider getProvider(EObject eObject) {
        EClass eClass = null;
        
        if(eObject instanceof IDiagramModelArchimateObject) {
            eClass = ((IDiagramModelArchimateObject)eObject).getArchimateElement().eClass();
        }
        else if(eObject instanceof IDiagramModelArchimateConnection) {
            eClass = ((IDiagramModelArchimateConnection)eObject).getRelationship().eClass();
        }
        else {
            eClass = eObject.eClass();
        }
        
        return getProvider(eClass);
    }
}
