/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.ArchimateDiagramPart;
import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.editparts.ArchimateRelationshipEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.editor.diagram.figures.connections.AssignmentConnectionFigure;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;



@SuppressWarnings("nls")
public class PropertiesLabelProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PropertiesLabelProviderTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // AbstractUIPlugin#createImageRegistry expects to see a non null Display.getCurrent()
        TestUtils.ensureDefaultDisplay();
    }

    PropertiesLabelProvider provider;
    
    @Before
    public void runOnceBeforeEachTest() {
        provider = new PropertiesLabelProvider();
    }
    
    @Test
    public void testGetImageNull() {
        // Null is OK
        assertNull(provider.getImage(new Object()));
    }
    
    @Test
    public void testGetImageElement() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        
        // Image for element
        Image image1 = provider.getImage(new StructuredSelection(element));
        assertNotNull(image1);
    
        // Image for DiagramModelArchimateObject
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        Image image2 = provider.getImage(new StructuredSelection(dmo));
        assertSame(image1, image2);
        
        // Image for EditPart
        EditPart editPart = new ArchimateElementEditPart();
        editPart.setModel(dmo);
        Image image3 = provider.getImage(new StructuredSelection(editPart));
        assertSame(image1, image3);
    }    

    @Test
    public void testGetImageRelation() {
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        
        // Image for relation
        Image image1 = provider.getImage(new StructuredSelection(relation));
        assertNotNull(image1);
    
        // Image for DiagramModelArchimateConnection
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setArchimateRelationship(relation);
        Image image2 = provider.getImage(new StructuredSelection(connection));
        assertSame(image1, image2);
        
        // Image for EditPart
        EditPart editPart = new ArchimateRelationshipEditPart(AssignmentConnectionFigure.class);
        editPart.setModel(connection);
        Image image3 = provider.getImage(new StructuredSelection(editPart));
        assertSame(image1, image3);
    }    

    @Test
    public void testGetTextEmpty() {
        assertEquals(" ", provider.getText(new Object()));
    }
    
    @Test
    public void testGetTextEmptySelection() {
        assertEquals(" ", provider.getText(new StructuredSelection()));
    }
    
    @Test
    public void testGetTextElement() {
        // Text for element
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        String text = provider.getText(new StructuredSelection(element));
        assertEquals("Artifact", text);
        
        // Text for DiagramModelArchimateObject
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        text = provider.getText(new StructuredSelection(dmo));
        assertEquals("Artifact", text);

        // Text for EditPart
        EditPart editPart = new ArchimateElementEditPart();
        editPart.setModel(dmo);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals("Artifact", text);
    }    

    @Test
    public void testGetTextRelation() {
        // Text for relation
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(IArchimateFactory.eINSTANCE.createBusinessActor());
        relation.setTarget(IArchimateFactory.eINSTANCE.createBusinessRole());
        relation.getSource().setName("BA1");
        relation.getTarget().setName("BR1");
        
        String expectedText = "Assignment relation (BA1 - BR1)";
        
        String text = provider.getText(new StructuredSelection(relation));
        assertEquals(expectedText, text);
        
        text = provider.getText(new StructuredSelection(relation));
        assertEquals(expectedText, text);
        
        // Text for DiagramModelArchimateConnection
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setArchimateRelationship(relation);
        text = provider.getText(new StructuredSelection(relation));
        assertEquals(expectedText, text);

        // Text for EditPart
        EditPart editPart = new ArchimateRelationshipEditPart(AssignmentConnectionFigure.class);
        editPart.setModel(connection);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals(expectedText, text);
    }

    @Test
    public void testGetTextDiagramModel() {
        // Text for diagram model
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        String text = provider.getText(new StructuredSelection(dm));
        assertEquals("View", text);
        
        // Text for EditPart
        ArchimateDiagramPart editPart = new ArchimateDiagramPart();
        editPart.setModel(dm);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals("View", text);
    }

    @Test
    public void testGetTextNote() {
        // Text for diagram model
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        String text = provider.getText(new StructuredSelection(note));
        assertEquals("Note", text);
        
        // Text for EditPart
        NoteEditPart editPart = new NoteEditPart();
        editPart.setModel(note);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals("Note", text);
    }

    @Test
    public void testGetAdaptable() {
        IAdaptable adaptable = new IAdaptable() {
            IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
            
            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public Object getAdapter(Class adapter) {
                if(adapter != null && (adapter.isInstance(element) || adapter.isInstance(this))) {
                    return element;
                }
                
                return null;
            }
        };
        
        String text = provider.getText(new StructuredSelection(adaptable));
        assertEquals("Artifact", text);
        
        Image image = provider.getImage(new StructuredSelection(adaptable));
        assertNotNull(image);
    }

    @Test
    public void testGetArchimateConceptText() {
        // Type of element
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        assertEquals("Artifact", provider.getArchimateConceptText(element));
        
        // Type of relation + source/target
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(IArchimateFactory.eINSTANCE.createBusinessActor());
        relation.setTarget(IArchimateFactory.eINSTANCE.createBusinessRole());
        assertEquals("Assignment relation (Business Actor - Business Role)", provider.getArchimateConceptText(relation));
        
        // Name + type + source/target
        relation.setName("Hello");
        assertEquals("Hello (Assignment relation) (Business Actor - Business Role)", provider.getArchimateConceptText(relation));
        
        // Null name is OK
        relation.setName(null);
        assertEquals("Assignment relation (Business Actor - Business Role)", provider.getArchimateConceptText(relation));
    }
    
}
