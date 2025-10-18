/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.Logger;
import com.archimatetool.model.IDiagramModelArchimateComponent;



/**
 * Factory for Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ObjectUIFactory {

    public static final ObjectUIFactory INSTANCE = new ObjectUIFactory();
    
    static {
        INSTANCE.registerProviders();
    }
    
    Map<EClass, IObjectUIProvider> map = new HashMap<EClass, IObjectUIProvider>();
    
    ObjectUIFactory() {
    }
    
    private void registerProviders() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(IObjectUIProvider.EXTENSIONPOINT_ID)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                IObjectUIProvider provider = (IObjectUIProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
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
    
    void registerProvider(IObjectUIProvider provider) {
        if(provider != null && provider.providerFor() != null && map.get(provider.providerFor()) == null) {
            map.put(provider.providerFor(), provider);
        }
    }
    
    public IObjectUIProvider getProviderForClass(EClass eClass) {
        return eClass == null ? null : map.get(eClass);
    }
    
    public IObjectUIProvider getProvider(EObject eObject) {
        EClass eClass = null;
        
        // We need to unwrap these here as this is called from diagram model objects
        if(eObject instanceof IDiagramModelArchimateComponent dmc) {
            eClass = dmc.getArchimateConcept().eClass();
        }
        else if(eObject != null) {
            eClass = eObject.eClass();
        }

        // A UIProvider can use an EObject instance to determine an EObject feature rather than the static Eclass provider feature.
        // (Default Size for alternate figure, and icon for DiagramModelReference, folder colour, etc). Maybe more in the future.
        // So, make a new instance of the provider and set the eObject.
        // This is a much safer approach than allowing the instance to be set on the singleton Eclass provider.
        if(getProviderForClass(eClass) instanceof AbstractObjectUIProvider template) {
            try {
                return template.clone().setInstance(eObject);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return null;
    }
    
    public List<IObjectUIProvider> getProviders() {
        return new ArrayList<>(map.values());
    }
}
