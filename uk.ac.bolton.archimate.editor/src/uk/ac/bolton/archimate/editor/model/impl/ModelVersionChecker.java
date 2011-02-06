/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.model.impl;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;

import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.ModelVersion;


/**
 * Utility to check whether models are incompatible
 * 
 * @author Phillip Beauvoir
 */
public class ModelVersionChecker {
    
    public static class LaterModelVersionException extends Exception {
        private String version;
        
        public LaterModelVersionException(String version) {
            this.version = version;
        }
        
        
        public String getVersion() {
            return version;
        }
    }
    
    public static class IncompatibleModelVersionException extends Exception {
        private Diagnostic diagnostic;
        
        public IncompatibleModelVersionException(Diagnostic diagnostic) {
            this.diagnostic = diagnostic;
        }
        
        public Diagnostic getDiagnostic() {
            return diagnostic;
        }
    }

    public static void checkErrors(Resource resource) throws Exception {
        // Log errors
        for(Diagnostic diagnostic : resource.getErrors()) {
            System.err.println(diagnostic);
            Logger.logError(diagnostic.getMessage());
        }

        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        String version = model.getVersion();
        
        // Is it catastrophic? If it is, throw an exception
        for(Diagnostic diagnostic : resource.getErrors()) {
            if(isCatastrophic(diagnostic, version)) {
                throw new IncompatibleModelVersionException(diagnostic);
            }
        }
    }
    
    public static void checkVersion(Resource resource) throws LaterModelVersionException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        String version = model.getVersion();
        
        // Loading a newer version model into older version of Archi might be OK
        if(StringUtils.isSet(version) && version.compareTo(ModelVersion.VERSION) > 0) {
            throw new LaterModelVersionException(version);
        }
    }

    private static boolean isCatastrophic(Diagnostic diagnostic, String version) {
        // TODO Do some logic to check the integrity of the model
//        if(diagnostic instanceof FeatureNotFoundException) {
//        }
//        else if(diagnostic instanceof ClassNotFoundException) {
//        }
        return false;
    }

}
