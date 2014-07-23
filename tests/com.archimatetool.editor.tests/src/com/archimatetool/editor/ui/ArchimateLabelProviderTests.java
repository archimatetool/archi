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
import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.tests.TestUtils;



@SuppressWarnings("nls")
public class ArchimateLabelProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateLabelProviderTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // These tests indirectly reference AbstractElementUIProvider which instantiates an ImageRegistry which hits a null Display.getCurrent()
        // Calling Display.getDefault() will set Display.getCurrent() to non-null
        TestUtils.ensureDefaultDisplay();
    }

    @Test
    public void testGetLabel() {
        // Null object
        assertEquals("", ArchimateLabelProvider.INSTANCE.getLabel(null));
        
        // Any object
        assertEquals("", ArchimateLabelProvider.INSTANCE.getLabel(""));
        
        // Nameable
        INameable nameable = IArchimateFactory.eINSTANCE.createBusinessActor();
        nameable.setName("Hello");
        assertEquals("Hello", ArchimateLabelProvider.INSTANCE.getLabel(nameable));
        
        // View
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        assertEquals("View", ArchimateLabelProvider.INSTANCE.getLabel(dm));
        
        // Sketch
        ISketchModel sm = IArchimateFactory.eINSTANCE.createSketchModel();
        assertEquals("Sketch", ArchimateLabelProvider.INSTANCE.getLabel(sm));
        
        // Image
        IDiagramModelImage di = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        assertEquals("Image", ArchimateLabelProvider.INSTANCE.getLabel(di));
    }
    
    @Test
    public void testGetImage() {
        // Null object
        assertNull(ArchimateLabelProvider.INSTANCE.getImage(null));
        
        // Object
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        Image image1 = ArchimateLabelProvider.INSTANCE.getImage(actor);
        assertNotNull(image1);
        
        // Same image for object's class
        Image image2 = ArchimateLabelProvider.INSTANCE.getImage(actor.eClass());
        assertSame(image1, image2);
    }
    
    @Test
    public void testGetGraphicsIcon() {
        // Null object
        assertNull(ArchimateLabelProvider.INSTANCE.getGraphicsIcon(null));
        
        // Models
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        assertNotNull(ArchimateLabelProvider.INSTANCE.getGraphicsIcon(dm));
        
        ISketchModel sm = IArchimateFactory.eINSTANCE.createSketchModel();
        assertNotNull(ArchimateLabelProvider.INSTANCE.getGraphicsIcon(sm));
    }

    @Test
    public void testGetImageDescriptor() {
        // Null object
        assertNull(ArchimateLabelProvider.INSTANCE.getImageDescriptor(null));
        
        // EClass
        EClass eClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        ImageDescriptor id = ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass);
        assertNotNull(id);
    }
    
    @Test
    public void testGetDefaultName() {
        // Null object
        assertEquals("", ArchimateLabelProvider.INSTANCE.getDefaultName(null));
        
        // EClass
        EClass eClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        assertEquals("Business Actor", ArchimateLabelProvider.INSTANCE.getDefaultName(eClass));
    }
    
    @Test
    public void testGetDefaultShortName() {
        // Null object
        assertEquals("", ArchimateLabelProvider.INSTANCE.getDefaultShortName(null));
        
        // EClass
        EClass eClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        assertEquals("Actor", ArchimateLabelProvider.INSTANCE.getDefaultShortName(eClass));
    }
    
    @Test
    public void testGetRelationshipSentence() {
        // Null object
        assertEquals("", ArchimateLabelProvider.INSTANCE.getRelationshipSentence(null));
        
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        actor.setName("Fred");
        IBusinessRole role = IArchimateFactory.eINSTANCE.createBusinessRole();
        role.setName("Nobody");

        IRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(actor);
        relation.setTarget(role);
        assertEquals("Fred is assigned to Nobody", ArchimateLabelProvider.INSTANCE.getRelationshipSentence(relation));
        
        relation.setSource(role);
        relation.setTarget(actor);
        assertEquals("Nobody is assigned to Fred", ArchimateLabelProvider.INSTANCE.getRelationshipSentence(relation));
    }
    
    @Test
    public void testGetRelationshipPhrase() {
        // Null object
        assertEquals("", ArchimateLabelProvider.INSTANCE.getRelationshipPhrase(null, true));
        
        EClass eClass = IArchimatePackage.eINSTANCE.getAssignmentRelationship();
        assertEquals("Assigned from", ArchimateLabelProvider.INSTANCE.getRelationshipPhrase(eClass, true));
        assertEquals("Assigned to", ArchimateLabelProvider.INSTANCE.getRelationshipPhrase(eClass, false));
    }
}
