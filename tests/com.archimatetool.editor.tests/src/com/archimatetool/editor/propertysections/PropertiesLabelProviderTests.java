/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.ArchimateDiagramPart;
import com.archimatetool.editor.diagram.editparts.connections.AssignmentConnectionEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.editor.diagram.editparts.technology.TechnologyArtifactEditPart;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IRelationship;
import com.archimatetool.tests.TestUtils;



@SuppressWarnings("nls")
public class PropertiesLabelProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PropertiesLabelProviderTests.class);
    }
    
    PropertiesLabelProvider provider;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // These tests indirectly reference AbstractElementUIProvider which instantiates an ImageRegistry which hits a null Display.getCurrent()
        // Calling Display.getDefault() will set Display.getCurrent() to non-null
        TestUtils.ensureDefaultDisplay();
    }

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
        assertEquals(image1, image2);
        
        // Image for EditPart
        TechnologyArtifactEditPart editPart = new TechnologyArtifactEditPart();
        editPart.setModel(dmo);
        Image image3 = provider.getImage(new StructuredSelection(editPart));
        assertEquals(image1, image3);
    }    

    @Test
    public void testGetImageRelation() {
        IRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        
        // Image for relation
        Image image1 = provider.getImage(new StructuredSelection(relation));
        assertNotNull(image1);
    
        // Image for DiagramModelArchimateConnection
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setRelationship(relation);
        Image image2 = provider.getImage(new StructuredSelection(connection));
        assertEquals(image1, image2);
        
        // Image for EditPart
        AssignmentConnectionEditPart editPart = new AssignmentConnectionEditPart();
        editPart.setModel(connection);
        Image image3 = provider.getImage(new StructuredSelection(editPart));
        assertEquals(image1, image3);
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
        TechnologyArtifactEditPart editPart = new TechnologyArtifactEditPart();
        editPart.setModel(dmo);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals("Artifact", text);
    }    

    @Test
    public void testGetTextRelation() {
        // Text for relation
        IRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        String text = provider.getText(new StructuredSelection(relation));
        assertEquals("Assignment relation", text);
        
        // Text for DiagramModelArchimateConnection
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setRelationship(relation);
        text = provider.getText(new StructuredSelection(relation));
        assertEquals("Assignment relation", text);

        // Text for EditPart
        AssignmentConnectionEditPart editPart = new AssignmentConnectionEditPart();
        editPart.setModel(connection);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals("Assignment relation", text);
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
            
            @SuppressWarnings("rawtypes")
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
    public void testGetArchimateComponentText() {
        // Type of element
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        assertEquals("Artifact", provider.getArchimateComponentText(element));
        
        // Type of relation
        IRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        assertEquals("Assignment relation", provider.getArchimateComponentText(relation));
        
        // Name + type
        relation.setName("Hello");
        assertEquals("Hello (Assignment relation)", provider.getArchimateComponentText(relation));
        
        // Null is OK
        relation.setName(null);
        assertEquals("Assignment relation", provider.getArchimateComponentText(relation));
    }
    
}
