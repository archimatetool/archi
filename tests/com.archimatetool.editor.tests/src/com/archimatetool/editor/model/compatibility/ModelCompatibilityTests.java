/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;


@SuppressWarnings("nls")
public class ModelCompatibilityTests {
    
    private ModelCompatibility mc;
    private Resource resource;
    
    File file1 = new File(TestSupport.getTestDataFolder(), "models/compatibility_test1.archimate");
    File file2 = new File(TestSupport.getTestDataFolder(), "models/compatibility_test2.archimate");
    File file3 = new File(TestSupport.getTestDataFolder(), "models/compatibility_test3.archimate");
    
    @Test
    public void shouldThrowIOException1() {
        resource = ArchimateResourceFactory.createNewResource(file1);
        IOException ex = assertThrows(IOException.class, () -> {
            resource.load(null);
        });
        assertTrue(ex.getMessage().contains("Feature 'something' not found."));
    }

    @Test
    public void shouldThrowIOException2() {
        resource = ArchimateResourceFactory.createNewResource(file2);
        IOException ex = assertThrows(IOException.class, () -> {
            resource.load(null);
        });
        assertTrue(ex.getMessage().contains("Package with uri 'http://www.archimatetool.com/Bogus' not found."));
    }

    @Test
    public void checkErrors_ThrowsIncompatibleModelException1() {
        createResource(file2);
        IncompatibleModelException ex = assertThrows(IncompatibleModelException.class, () -> {
            mc.checkErrors();
        });
        assertTrue(ex.getMessage().contains("Package with uri 'http://www.archimatetool.com/Bogus' not found."));
        assertTrue(ex.getMessage().contains("Class 'model' is not found or is abstract."));
    }
    
    @Test
    public void checkErrors_ThrowsIncompatibleModelException2() {
        createResource(file3);
        IncompatibleModelException ex = assertThrows(IncompatibleModelException.class, () -> {
            mc.checkErrors();
        });
        assertTrue(ex.getMessage().contains("Class 'Bogus1' is not found or is abstract."));
        assertTrue(ex.getMessage().contains("Class 'Bogus2' is not found or is abstract."));
        assertTrue(ex.getMessage().contains("Class 'Bogus3' is not found or is abstract."));
    }
    
    @Test
    public void checkErrors_NotCatastrophic() throws IncompatibleModelException {
        createResource(file1);
        mc.checkErrors();
    }

    @Test
    public void isLaterModelVersion_IsLater() {
        createResource(file1);
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        assertEquals("10.0.0", model.getVersion());
        assertTrue(mc.isLaterModelVersion("2.6.1"));
    }

    @Test
    public void isLaterModelVersion_IsNotLater() {
        createResource(file1);
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        model.setVersion("3.4.1");
        assertFalse(mc.isLaterModelVersion("10.0.0"));
        
        model.setVersion("99.9.9");
        assertFalse(mc.isLaterModelVersion("99.9.9"));
    }

    @Test
    public void getAcceptableExceptions() {
        createResource(file1);
        assertEquals(2,  mc.getAcceptableExceptions().size());
        
        createResource(file2);
        assertEquals(0,  mc.getAcceptableExceptions().size());

        createResource(file3);
        assertEquals(0,  mc.getAcceptableExceptions().size());
    }

    @Test
    public void isFeatureNotFoundException() {
        createResource(file1);
        
        assertEquals(2, resource.getErrors().size());
        
        Diagnostic diagnostic = resource.getErrors().get(0);
        assertTrue(mc.isFeatureNotFoundException(diagnostic));
        
        diagnostic = resource.getErrors().get(1);
        assertTrue(mc.isFeatureNotFoundException(diagnostic));
    }

    @Test
    public void isCatastrophicException() {
        createResource(file2);
        
        assertEquals(2,  resource.getErrors().size());
        
        Diagnostic diagnostic = resource.getErrors().get(0);
        assertTrue(mc.isCatastrophicException(diagnostic));
        
        diagnostic = resource.getErrors().get(1);
        assertTrue(mc.isCatastrophicException(diagnostic));
    }
    
    private void createResource(File file) {
        resource = ArchimateResourceFactory.createNewResource(file);
        mc = new ModelCompatibility(resource);
        
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Should happen
        }
    }
}
