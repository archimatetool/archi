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

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.technology.TechnologyArtifactEditPart;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
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
    public void testGetImage() {
        // Null is OK
        assertNull(provider.getImage(new Object()));
        
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
    public void testGetText() {
        // Null is OK
        assertNull(provider.getImage(new Object()));
        
        // Text for element
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        String text = provider.getText(new StructuredSelection(element));
        assertEquals("Artifact", text);
        
        // Text for EditPart
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        TechnologyArtifactEditPart editPart = new TechnologyArtifactEditPart();
        editPart.setModel(dmo);
        text = provider.getText(new StructuredSelection(editPart));
        assertEquals("Artifact", text);
        
        // No selection
        text = provider.getText(new StructuredSelection());
        assertEquals(" ", text);
    }    

    @Test
    public void testGetArchimateElementText() {
        // Type of element
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        assertEquals("Artifact", provider.getArchimateElementText(element));
        
        // Type of relation
        element = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        assertEquals("Assignment relation", provider.getArchimateElementText(element));
        
        // Name + type
        element.setName("Hello");
        assertEquals("Hello (Assignment relation)", provider.getArchimateElementText(element));
        
        // Null is OK
        element.setName(null);
        assertEquals("Assignment relation", provider.getArchimateElementText(element));
    }
    
}
