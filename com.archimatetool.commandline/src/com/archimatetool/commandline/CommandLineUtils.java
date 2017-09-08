/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ModelCompatibility;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;

/**
 * Command Line Utils
 * 
 * @author Phillip Beauvoir
 */
public class CommandLineUtils {

    /**
     * Load model from file.
     * This is a simpler version of com.archimatetool.editor.model.impl.EditorModelManager
     * @param file
     * @return The model
     * @throws IOException 
     */
    public static IArchimateModel loadModel(File file) throws IOException {
        if(file == null || !file.exists()) {
            return null;
        }
        
        // Ascertain if this is an archive file
        boolean useArchiveFormat = IArchiveManager.FACTORY.isArchiveFile(file);

        // Create the Resource
        Resource resource = ArchimateResourceFactory.createNewResource(useArchiveFormat ?
                                                       IArchiveManager.FACTORY.createArchiveModelURI(file) :
                                                       URI.createFileURI(file.getAbsolutePath()));

        // Check model compatibility
        ModelCompatibility modelCompatibility = new ModelCompatibility(resource);
        
        // Load model
        resource.load(null);
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        // And then fix any backward compatibility issues
        try {
            modelCompatibility.fixCompatibility();
        }
        catch(CompatibilityHandlerException ex) {
        }

        model.setFile(file);
        model.setDefaults();
        
        // Add an Archive Manager and load images
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        archiveManager.loadImages();

        return model;
    }
    
}
