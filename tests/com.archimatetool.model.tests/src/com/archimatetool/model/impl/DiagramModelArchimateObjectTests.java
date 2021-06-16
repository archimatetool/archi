/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.archimatetool.model.IApplicationInterface;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFolder;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class DiagramModelArchimateObjectTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelArchimateObjectTests.class);
    }
    
    private IDiagramModelArchimateObject object;
    
    private IArchimateElement element;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        object = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        element = IArchimateFactory.eINSTANCE.createBusinessActor();
        object.setArchimateElement(element);
        return object;
    }

    
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
        object.getArchimateElement().setName("another");
        assertEquals("another", object.getName());
    }

    @Test
    public void testGetArchimateElement() {
        assertSame(element, object.getArchimateElement());
    }
    
    @Test
    public void testGetArchimateConcept() {
        assertSame(element, object.getArchimateConcept());
    }
    
    @Test
    public void testSetArchimateConcept() {
        IApplicationInterface e = IArchimateFactory.eINSTANCE.createApplicationInterface();
        object.setArchimateConcept(e);
        assertSame(e, object.getArchimateConcept());
        assertSame(e, object.getArchimateElement());
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
        
        object.addArchimateConceptToModel(null);
    }
    
    @Test
    public void testAdd_Remove_ArchimateElementToModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        dm.getChildren().add(object);
        
        // Passing null uses a default folder in the model
        IFolder expectedFolder = model.getDefaultFolderForObject(object.getArchimateElement());
        object.addArchimateConceptToModel(null);
        assertSame(expectedFolder, object.getArchimateElement().eContainer());
        
        object.removeArchimateConceptFromModel();
        assertNull(object.getArchimateElement().eContainer());
        
        expectedFolder = IArchimateFactory.eINSTANCE.createFolder();
        object.addArchimateConceptToModel(expectedFolder);
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
    
    @Test
    public void testSetImageSource() {
        assertEquals(IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE_DEFAULT, object.getImageSource());
        object.setImageSource(IDiagramModelArchimateObject.IMAGE_SOURCE_CUSTOM);
        assertEquals(IDiagramModelArchimateObject.IMAGE_SOURCE_CUSTOM, object.getImageSource());
    }
    
    @Test
    public void testUseProfileImage() {
        assertTrue(object.useProfileImage());
        object.setImageSource(IDiagramModelArchimateObject.IMAGE_SOURCE_CUSTOM);
        assertFalse(object.useProfileImage());
    }
    
}
