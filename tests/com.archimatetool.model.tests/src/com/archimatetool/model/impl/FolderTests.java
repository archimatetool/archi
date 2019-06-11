/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;


public class FolderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FolderTests.class);
    }
    
    private IFolder folder;
    private IArchimateModel model;

    @Before
    public void runBeforeEachTest() {
        folder = IArchimateFactory.eINSTANCE.createFolder();
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
    }

    @Test
    public void testGetName() {
        CommonTests.testGetName(folder);
    }

    @Test
    public void testGetID() {
        assertNotNull(folder.getId());
    }

    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(folder);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(folder);
    }

    @Test
    public void testGetElements() {
        CommonTests.testList(folder.getElements(), IArchimatePackage.eINSTANCE.getFolder());
        CommonTests.testList(folder.getElements(), IArchimatePackage.eINSTANCE.getArtifact());
    }
  
    @Test
    public void testGetType() {
        assertEquals(FolderType.USER, folder.getType());
        folder.setType(FolderType.BUSINESS);
        assertEquals(FolderType.BUSINESS, folder.getType());
    }
    
    @Test
    public void testGetAdapter() {
        CommonTests.testGetAdapter(folder);
    }
        
    @Test
    public void testGetArchimateModel() {
        assertNull(folder.getArchimateModel());
        
        model.getFolders().add(folder);
        assertSame(model, folder.getArchimateModel());
    }
    
    @Test
    public void testGetFolders() {
        CommonTests.testList(folder.getFolders(), folder.eClass());
    }
}
