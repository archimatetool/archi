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
    
    private static File testFolder;
    
    public static File TEST_MODEL_FILE_ARCHISURANCE = new File(getTestDataFolder(), "models/Archisurance.archimate"); //$NON-NLS-1$

    public static File getTestDataFolder() {
        if(testFolder == null) {
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.model.tests", "testdata");
        }
        return testFolder;
    }

    public static File saveModel(IArchimateModel model) throws IOException {
        File file = TestUtils.createTempFile(".archimate");
        
        Resource resource = ArchimateResourceFactory.createNewResource(file);
        resource.getContents().add(model);
        resource.save(null);
    
        return file;
    }

    public static IArchimateModel loadModel(File file) throws IOException {
        Resource resource = ArchimateResourceFactory.createNewResource(file);
        resource.load(null);
        return (IArchimateModel)resource.getContents().get(0);
    }
    
}
