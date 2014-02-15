/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFolder;


@SuppressWarnings("nls")
public class DiagramModelArchimateObjectTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelArchimateObjectTests.class);
    }
    
    private IDiagramModelArchimateObject object;
    private IArchimateElement element;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        IDiagramModelArchimateObject object = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        object.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        return object;
    }

    @Before
    public void runBeforeEachDiagramModelArchimateObjectTest() {
        object = (IDiagramModelArchimateObject)getComponent();
        element = object.getArchimateElement();
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testGetChildren() {
        CommonTests.testList(object.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelArchimateObject());
        CommonTests.testList(object.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        CommonTests.testList(object.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelNote());
    }
    
    @Override
    @Test
    public void testGetName() {
        super.testGetName();
        
        // Set element name directly
        element.setName("another");
        assertEquals("another", object.getName());
    }

    @Test
    public void testGetArchimateElement() {
        assertSame(element, object.getArchimateElement());
    }

    
    @Test
    public void testGetType() {
        assertEquals(0, object.getType());
        object.setType(2);
        assertEquals(2, object.getType());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddArchimateElementToModel_AlreadyHasParent() {
        IFolder parent = IArchimateFactory.eINSTANCE.createFolder();
        parent.getElements().add(object.getArchimateElement());
        
        object.addArchimateElementToModel(null);
    }
    
    @Test
    public void testAdd_Remove_ArchimateElementToModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(dm).getElements().add(dm);
        dm.getChildren().add(object);
        
        // Passing null uses a default folder in the model
        IFolder expectedFolder = model.getDefaultFolderForElement(object.getArchimateElement());
        object.addArchimateElementToModel(null);
        assertSame(expectedFolder, object.getArchimateElement().eContainer());
        
        object.removeArchimateElementFromModel();
        assertNull(object.getArchimateElement().eContainer());
        
        expectedFolder = IArchimateFactory.eINSTANCE.createFolder();
        object.addArchimateElementToModel(expectedFolder);
        assertSame(expectedFolder, object.getArchimateElement().eContainer());
    }

    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        IDiagramModelArchimateObject copy = (IDiagramModelArchimateObject)object.getCopy();
        assertNotSame(copy, object);
        
        assertNotNull(copy.getArchimateElement());
        assertNotSame(copy.getArchimateElement(), object.getArchimateElement());
    }
}
