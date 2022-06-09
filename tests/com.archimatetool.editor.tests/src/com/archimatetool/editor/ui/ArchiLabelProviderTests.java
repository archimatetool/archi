/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.INameable;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;



@SuppressWarnings("nls")
public class ArchiLabelProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchiLabelProviderTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // AbstractUIPlugin#createImageRegistry expects to see a non null Display.getCurrent()
        TestUtils.ensureDefaultDisplay();
    }

    @Test
    public void testGetLabel() {
        // Null object
        assertEquals("", ArchiLabelProvider.INSTANCE.getLabel(null));
        
        // Any object
        assertEquals("", ArchiLabelProvider.INSTANCE.getLabel(""));
        
        // Nameable
        INameable nameable = IArchimateFactory.eINSTANCE.createBusinessActor();
        nameable.setName("Hello");
        assertEquals("Hello", ArchiLabelProvider.INSTANCE.getLabel(nameable));
        
        // View
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        assertEquals("View", ArchiLabelProvider.INSTANCE.getLabel(dm));
        
        // Sketch
        ISketchModel sm = IArchimateFactory.eINSTANCE.createSketchModel();
        assertEquals("Sketch", ArchiLabelProvider.INSTANCE.getLabel(sm));
        
        // Image
        IDiagramModelImage di = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        assertEquals("Image", ArchiLabelProvider.INSTANCE.getLabel(di));
    }
    
    @Test
    public void testGetImage() {
        // Null object
        assertNull(ArchiLabelProvider.INSTANCE.getImage(null));
        
        // Object
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        Image image1 = ArchiLabelProvider.INSTANCE.getImage(actor);
        assertNotNull(image1);
        
        // Same image for object's class
        Image image2 = ArchiLabelProvider.INSTANCE.getImage(actor.eClass());
        assertSame(image1, image2);
    }
    
    @Test
    public void testGetGraphicsIconForDiagramModel() {
        // Null object
        assertNull(ArchiLabelProvider.INSTANCE.getGraphicsIconForDiagramModel(null));
        
        // Models
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        assertNotNull(ArchiLabelProvider.INSTANCE.getGraphicsIconForDiagramModel(dm));
        
        ISketchModel sm = IArchimateFactory.eINSTANCE.createSketchModel();
        assertNotNull(ArchiLabelProvider.INSTANCE.getGraphicsIconForDiagramModel(sm));
    }

    @Test
    public void testGetImageDescriptor() {
        // Null object
        assertNull(ArchiLabelProvider.INSTANCE.getImageDescriptor(null));
        
        // EClass
        EClass eClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        ImageDescriptor id = ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass);
        assertNotNull(id);
    }
    
    @Test
    public void testGetDefaultName() {
        // Null object
        assertEquals("", ArchiLabelProvider.INSTANCE.getDefaultName(null));
        
        // EClass
        EClass eClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        assertEquals("Business Actor", ArchiLabelProvider.INSTANCE.getDefaultName(eClass));
    }
    
    @Test
    public void testGetRelationshipSentence() {
        // Null object
        assertEquals("", ArchiLabelProvider.INSTANCE.getRelationshipSentence(null));
        
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        actor.setName("Fred");
        IBusinessRole role = IArchimateFactory.eINSTANCE.createBusinessRole();
        role.setName("Nobody");

        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(actor);
        relation.setTarget(role);
        assertEquals("Fred is assigned to Nobody", ArchiLabelProvider.INSTANCE.getRelationshipSentence(relation));
        
        relation.setSource(role);
        relation.setTarget(actor);
        assertEquals("Nobody is assigned to Fred", ArchiLabelProvider.INSTANCE.getRelationshipSentence(relation));
        
        // Junctions
        IJunction j1 = IArchimateFactory.eINSTANCE.createJunction();
        j1.setName("Johnny");
        
        relation.setSource(j1);
        relation.setTarget(actor);
        assertEquals("Johnny connects to Fred", ArchiLabelProvider.INSTANCE.getRelationshipSentence(relation));
        
        relation.setSource(actor);
        relation.setTarget(j1);
        assertEquals("Fred connects to Johnny", ArchiLabelProvider.INSTANCE.getRelationshipSentence(relation));
    }
    
    @Test
    public void testGetRelationshipPhrase() {
        // Null object
        assertEquals("", ArchiLabelProvider.INSTANCE.getRelationshipPhrase(null, true));
        
        EClass eClass = IArchimatePackage.eINSTANCE.getAssignmentRelationship();
        assertEquals("Assigned from", ArchiLabelProvider.INSTANCE.getRelationshipPhrase(eClass, true));
        assertEquals("Assigned to", ArchiLabelProvider.INSTANCE.getRelationshipPhrase(eClass, false));
    }
}
