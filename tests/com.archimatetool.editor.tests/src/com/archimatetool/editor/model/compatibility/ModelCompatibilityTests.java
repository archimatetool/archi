/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class ModelCompatibilityTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ModelCompatibilityTests.class);
    }
    
    private ModelCompatibility mc;
    private Resource resource;
    
    File file1 = new File(TestSupport.getTestDataFolder(), "models/compatibility_test1.archimate");
    File file2 = new File(TestSupport.getTestDataFolder(), "models/compatibility_test2.archimate");
    
    @Test(expected=IOException.class)
    public void testShouldThrowException1() throws IOException {
        resource = ArchimateResourceFactory.createNewResource(file1);
        resource.load(null);
    }

    @Test(expected=IOException.class)
    public void testShouldThrowException2() throws IOException {
        resource = ArchimateResourceFactory.createNewResource(file2);
        resource.load(null);
    }

    @Test(expected=IncompatibleModelException.class)
    public void testCheckErrors_ThrowsException() throws IncompatibleModelException {
        createResource(file2);
        mc.checkErrors();
    }
    
    @Test
    public void testCheckErrors_NotCatastrophic() throws IncompatibleModelException {
        createResource(file1);
        mc.checkErrors();
    }

    @Test
    public void testCheckErrors_IsCatastrophic() {
        createResource(file2);
        try {
            mc.checkErrors();
        }
        catch(IncompatibleModelException ex) {
            return;
        }
        
        fail();
    }

    @Test
    public void testIsLaterModelVersion_IsLater() {
        createResource(file1);
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        assertEquals("10.0.0", model.getVersion());
        assertTrue(mc.isLaterModelVersion("2.6.1"));
    }

    @Test
    public void testIsLaterModelVersion_IsNotLater() {
        createResource(file1);
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        model.setVersion("3.4.1");
        assertFalse(mc.isLaterModelVersion("10.0.0"));
        
        model.setVersion("99.9.9");
        assertFalse(mc.isLaterModelVersion("99.9.9"));
    }

    @Test
    public void testgetAcceptableExceptions() {
        createResource(file1);
        assertEquals(2,  mc.getAcceptableExceptions().size());
        
        createResource(file2);
        assertEquals(0,  mc.getAcceptableExceptions().size());
    }

    @Test
    public void testIsFeatureNotFoundException() {
        createResource(file1);
        
        assertEquals(2,  resource.getErrors().size());
        
        Diagnostic diagnostic = resource.getErrors().get(0);
        assertTrue(mc.isFeatureNotFoundException(diagnostic));
        
        diagnostic = resource.getErrors().get(1);
        assertTrue(mc.isFeatureNotFoundException(diagnostic));
    }

    @Test
    public void testIsCatastrophicException() {
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
        mc.doLog = false;
        
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Should happen
        }
    }
}
