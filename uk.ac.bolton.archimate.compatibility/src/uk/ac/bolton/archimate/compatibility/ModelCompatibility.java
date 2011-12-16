/**
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.compatibility;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.xmi.ClassNotFoundException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.xml.sax.SAXParseException;

import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.ModelVersion;


/**
 * Utility to check whether models are compatible
 * 
 * @author Phillip Beauvoir
 */
public class ModelCompatibility {
    
    public static void checkErrors(Resource resource) throws IncompatibleModelException {
        // Log errors first
        for(Diagnostic diagnostic : resource.getErrors()) {
            System.err.println(diagnostic);
            if(isCatastrophicError(diagnostic)) {
                Logger.logError(diagnostic.getMessage());
            }
            else {
                Logger.logWarning(diagnostic.getMessage());
            }
        }

        // Is it catastrophic? If it is, throw an exception
        for(Diagnostic diagnostic : resource.getErrors()) {
            if(isCatastrophicError(diagnostic)) {
                IncompatibleModelException ex = new IncompatibleModelException(diagnostic.getMessage());
                Logger.logError("Error opening model", ex);
                ex.printStackTrace();
                throw ex;
            }
        }
    }
    
    public static void checkVersion(Resource resource) throws LaterModelVersionException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        String version = model.getVersion();
        
        // Loading a newer version model into older version of Archi might be OK
        if(version != null && version.compareTo(ModelVersion.VERSION) > 0) {
            throw new LaterModelVersionException(version);
        }
    }

    private static boolean isCatastrophicError(Diagnostic diagnostic) {
        // Package not found - total disaster
        if(diagnostic instanceof PackageNotFoundException) {
            return true;
        }
        
        // Class not found that matches xml declaration - not good
        if(diagnostic instanceof ClassNotFoundException) {
            return true;
        }
        
        // Allow an IllegalValueException because an illegal value will default to a default value

        // Allow a FeatureNotFoundException because a feature might get deprecated
        
        // Last case is a Sax parse error
        if(diagnostic instanceof XMIException) {
            XMIException ex = (XMIException)diagnostic;
            if(ex.getCause() instanceof SAXParseException) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Fix any compatibility issues in registered handlers
     * @param resource The Resource to check
     * @throws CompatibilityHandlerException 
     */
    public static void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(ICompatibilityHandler.EXTENSION_ID)) {
            try {
                ICompatibilityHandler handler = (ICompatibilityHandler)configurationElement.createExecutableExtension("class");
                if(handler != null) {
                    handler.fixCompatibility(resource);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
}
