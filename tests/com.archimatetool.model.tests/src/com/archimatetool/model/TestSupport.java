/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.tests.TestUtils;


/**
 * Testing Support
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestSupport {
    
    public static File saveModel(IArchimateModel model) throws IOException {
        File file = TestUtils.createTempFile(".archimate");
        
        Resource resource = ArchimateResourceFactory.createNewResource(file);
        resource.getContents().add(model);
        
        // Catch all exceptions
        try {
            resource.save(null);
        }
        catch(Exception ex) {
            throw new IOException(ex);
        }
    
        return file;
    }

    public static IArchimateModel loadModel(File file) throws IOException {
        Resource resource = ArchimateResourceFactory.createNewResource(file);
        resource.load(null);
        return (IArchimateModel)resource.getContents().get(0);
    }
    
}
