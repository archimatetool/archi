/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.xmi.ClassNotFoundException;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.eclipse.emf.ecore.xmi.UnresolvedReferenceException;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.xml.sax.SAXParseException;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;



/**
 * Utility to check whether models are compatible
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ModelCompatibility {
    
    private Resource fResource;
    
    public ModelCompatibility(Resource resource) {
        fResource = resource;
    }
    
    public void checkErrors() throws IncompatibleModelException {
        String exceptionMessage = "";
        
        for(Diagnostic diagnostic : fResource.getErrors()) {
            if(isCatastrophicException(diagnostic)) {
                exceptionMessage += diagnostic.getMessage() + "\n";
            }
        }
        
        // Is it catastrophic? If it is, throw an IncompatibleModelException
        if(exceptionMessage.length() > 0) {
            throw new IncompatibleModelException(exceptionMessage);
        }
    }
    
    /**
     * Check a model version against another version number
     * @param presentVersion The version to check against
     */
    public boolean isLaterModelVersion(String presentVersion)  {
        IArchimateModel model = (IArchimateModel)fResource.getContents().get(0);
        String version = model.getVersion();
        return version != null && StringUtils.compareVersionNumbers(version, presentVersion) > 0;
    }
    
    /**
     * @return A list of Exceptions that should be non-catastrophic
     */
    public List<Diagnostic> getAcceptableExceptions() {
        List<Diagnostic> list = new ArrayList<>();
        
        for(Diagnostic diagnostic : fResource.getErrors()) {
            if(isFeatureNotFoundException(diagnostic)) {
                list.add(diagnostic);
            }
        }
        
        return list;
    }
    
    protected boolean isFeatureNotFoundException(Diagnostic diagnostic) {
        return diagnostic instanceof FeatureNotFoundException;
    }

    protected boolean isCatastrophicException(Diagnostic diagnostic) {
        return switch(diagnostic) {
            // Package not found - total disaster
            case PackageNotFoundException ex -> true;
            
            // Class not found that matches xml declaration - not good
            case ClassNotFoundException ex -> true;
            
            // Unresolved reference - not good
            case UnresolvedReferenceException ex -> true;
            
            // Other XMIException
            // Sax parse error - total disaster
            // Allow an IllegalValueException because an illegal value will default to a default value
            // Allow a FeatureNotFoundException because a feature might get deprecated
            case XMIException ex -> ex.getCause() instanceof SAXParseException;
            
            default -> false;
        };
    }
    
    /**
     * Fix any compatibility issues in registered handlers
     * @throws CompatibilityHandlerException 
     */
    public void fixCompatibility() throws CompatibilityHandlerException {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(ICompatibilityHandler.EXTENSION_ID)) {
            try {
                ICompatibilityHandler handler = (ICompatibilityHandler)configurationElement.createExecutableExtension("class");
                if(handler != null) {
                    handler.fixCompatibility(fResource);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
}
