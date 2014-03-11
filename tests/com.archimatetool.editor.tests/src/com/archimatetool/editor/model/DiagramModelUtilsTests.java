/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.editor.TestSupport;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IRelationship;


/**
 * DiagramModelUtils Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class DiagramModelUtilsTests {
    
    private static ArchimateTestModel tm;
    private static IArchimateModel model;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelUtilsTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        tm.loadModel();
        model = tm.getModel();
    }
    
    // =================================================================================================
    
    // Test Data
    
    String[][] data1 = {
            { "521" } ,                                         // element id
            { "4025", "3761", "3722", "3999", "4165", "4224" }  // expected ids of diagrams
    };
    
    String[][] data2 = {
            { "1399" } ,                // element id
            { "4056", "4279", "4318" }  // expected ids of diagrams
    };

    @Test
    public void testFindReferencedDiagramsForElement() {
        IArchimateElement element = null; 
        
        // Null element should at least return an empty list
        List<IDiagramModel> list = DiagramModelUtils.findReferencedDiagramsForElement(element);
        assertNotNull(list);
        assertEquals(0, list.size());
        
        testFindReferencedDiagramsForElement(data1);
        testFindReferencedDiagramsForElement(data2);
    }
    
    private void testFindReferencedDiagramsForElement(String[][] data) {
        IArchimateElement element = (IArchimateElement)tm.getObjectByID(data[0][0]);
        assertNotNull(element);
        
        List<IDiagramModel> list = DiagramModelUtils.findReferencedDiagramsForElement(element);
        assertEquals(data[1].length, list.size());
        
        for(int i = 0; i < data[1].length; i++) {
            String id = data[1][i];
            assertEquals(id, list.get(i).getId());
        }
    }
    
    @Test
    public void testIsElementReferencedInDiagrams() {
        // This should be in a diagram
        IArchimateElement element = (IArchimateElement)tm.getObjectByID(data1[0][0]);
        assertNotNull(element);
        assertTrue(DiagramModelUtils.isElementReferencedInDiagrams(element));
        
        // This should be in a diagram
        element = (IArchimateElement)tm.getObjectByID(data2[0][0]);
        assertNotNull(element);
        assertTrue(DiagramModelUtils.isElementReferencedInDiagrams(element));
        
        // This should not be in a diagram
        element = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element).getElements().add(element);
        assertFalse(DiagramModelUtils.isElementReferencedInDiagrams(element));
        
        // Unless we add it to a diagram
        // And we'll nest it inside a Group to be cunning
        IDiagramModelArchimateObject dmo = ArchimateTestModel.createDiagramModelArchimateObject(element);
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.getChildren().add(dmo);
        model.getDefaultDiagramModel().getChildren().add(group);
        assertTrue(DiagramModelUtils.isElementReferencedInDiagrams(element));
    }
    
    @Test
    public void testIsElementReferencedInDiagram() {
        testIsElementReferencedInDiagram(data1);
        testIsElementReferencedInDiagram(data2);
        
        // This should not be in a diagram
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element).getElements().add(element);
        assertFalse(DiagramModelUtils.isElementReferencedInDiagram(model.getDefaultDiagramModel(), element));
    }    
    
    private void testIsElementReferencedInDiagram(String[][] data) {
        IArchimateElement element = (IArchimateElement)tm.getObjectByID(data[0][0]);
        assertNotNull(element);
        
        for(int i = 0; i < data[1].length; i++) {
            IDiagramModel dm = (IDiagramModel)tm.getObjectByID(data[1][i]);
            assertNotNull(dm);
            assertTrue(DiagramModelUtils.isElementReferencedInDiagram(dm, element));
        }
    }
    
    // =================================================================================================

    // Test data
    private IDiagramModelArchimateObject dmo1, dmo2, dmo3, dmo4;
    private IDiagramModelArchimateConnection conn1, conn2, conn3;
    
    /*
      dm
       |-- dmo1
       |-- dmo2
             |-- dmo3
                  |-- dmo4
                  
       dmo1 <-- conn1 --> dmo2
       dmo3 <-- conn2 --> dmo4
       dmo4 <-- conn3 --> dmo1
    */
    
    private void createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(IArchimateElement element, IDiagramModel parent) {
        dmo1 = ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(element, parent);
        dmo2 = ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(element, parent);
        dmo3 = ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(element, dmo2);
        dmo4 = ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(element, dmo3);
    }
    
    private void createTestDiagramModelConnectionsForFindDiagramModelComponents(IRelationship relationship) {
        conn1 = ArchimateTestModel.createDiagramModelArchimateConnection(relationship);
        conn1.connect(dmo1, dmo2);
        conn2 = ArchimateTestModel.createDiagramModelArchimateConnection(relationship);
        conn2.connect(dmo3, dmo4);
        conn3 = ArchimateTestModel.createDiagramModelArchimateConnection(relationship);
        conn3.connect(dmo4, dmo1);
    }

    @Test
    public void testFindDiagramModelComponentsForElement_WithArchimateElement() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModel diagramModel = tm.addNewArchimateDiagramModel();
        
        // Should not be found
        List<IDiagramModelComponent> list = DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, element);
        assertTrue(list.isEmpty());
        
        // Add the element to various IDiagramModelArchimateObject objects
        createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(element, diagramModel);
        
        // Should be found in diagram
        list = DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, element);
        assertEquals(4, list.size());
        assertTrue(list.contains(dmo1));
        assertTrue(list.contains(dmo2));
        assertTrue(list.contains(dmo3));
        assertTrue(list.contains(dmo4));
    }
    
    @Test
    public void testFindDiagramModelComponentsForElement_WithRelationship() {
        IRelationship relationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IDiagramModel diagramModel = tm.addNewArchimateDiagramModel();
        
        // Should not be found
        List<IDiagramModelComponent> list = DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, relationship);
        assertTrue(list.isEmpty());
        
        // Create various IDiagramModelArchimateObject objects
        createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(IArchimateFactory.eINSTANCE.createBusinessActor(), diagramModel);
        
        // And make some connections using the relationship
        createTestDiagramModelConnectionsForFindDiagramModelComponents(relationship);
        
        // Found in diagram
        list = DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, relationship);
        assertEquals(3, list.size());
        assertTrue(list.contains(conn1));
        assertTrue(list.contains(conn2));
        assertTrue(list.contains(conn3));
    }
    
    @Test
    public void testFindDiagramModelObjectsForElement() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModel diagramModel = tm.addNewArchimateDiagramModel();
        
        // Should not be found
        List<IDiagramModelArchimateObject> list = DiagramModelUtils.findDiagramModelObjectsForElement(diagramModel, element);
        assertTrue(list.isEmpty());        
        
        // Add the element to various IDiagramModelArchimateObject objects
        createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(element, diagramModel);
        
        // Should be found in containers
        list = DiagramModelUtils.findDiagramModelObjectsForElement(diagramModel, element);
        assertEquals(4, list.size());
        
        list = DiagramModelUtils.findDiagramModelObjectsForElement(dmo2, element);
        assertEquals(2, list.size());
        
        list = DiagramModelUtils.findDiagramModelObjectsForElement(dmo3, element);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testFindDiagramModelConnectionsForRelation() {
        IRelationship relationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IDiagramModel diagramModel = tm.addNewArchimateDiagramModel();
        
        // Should not be found
        List<IDiagramModelArchimateConnection> list = DiagramModelUtils.findDiagramModelConnectionsForRelation(diagramModel, relationship);
        assertTrue(list.isEmpty());
        
        // Create various IDiagramModelArchimateObject objects
        createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(IArchimateFactory.eINSTANCE.createBusinessActor(), diagramModel);
        
        // And make some connections using the relationship
        createTestDiagramModelConnectionsForFindDiagramModelComponents(relationship);
        
        // Should be found in some containers
        list = DiagramModelUtils.findDiagramModelConnectionsForRelation(diagramModel, relationship);
        assertEquals(3, list.size());
        
        list = DiagramModelUtils.findDiagramModelConnectionsForRelation(dmo2, relationship);
        assertEquals(2, list.size());
        
        list = DiagramModelUtils.findDiagramModelConnectionsForRelation(dmo3, relationship);
        assertEquals(1, list.size());
        
        // But not this one because we only search in the child objects
        list = DiagramModelUtils.findDiagramModelConnectionsForRelation(dmo4, relationship);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testfindDiagramModelReferences() {
        IDiagramModel diagramModel1 = tm.addNewArchimateDiagramModel();
        IDiagramModel diagramModel2 = tm.addNewArchimateDiagramModel();
        IDiagramModel diagramModel3 = tm.addNewArchimateDiagramModel();
        
        // Should not be found
        List<IDiagramModelReference> list = DiagramModelUtils.findDiagramModelReferences(diagramModel3, diagramModel1);
        assertTrue(list.isEmpty());
        
        // Create some child objects
        createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(IArchimateFactory.eINSTANCE.createBusinessActor(), diagramModel3);
        
        // Create some refs
        IDiagramModelReference ref1 = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        ref1.setReferencedModel(diagramModel1);
        diagramModel3.getChildren().add(ref1);

        IDiagramModelReference ref2 = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        ref2.setReferencedModel(diagramModel2);
        dmo4.getChildren().add(ref2);
        
        list = DiagramModelUtils.findDiagramModelReferences(diagramModel3, diagramModel1);
        assertEquals(1, list.size());
        list = DiagramModelUtils.findDiagramModelReferences(diagramModel3, diagramModel2);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testHasDiagramModelArchimateConnection() {
        IRelationship relationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IRelationship relationship2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IDiagramModel diagramModel = tm.addNewArchimateDiagramModel();
        
        // Create various IDiagramModelArchimateObject objects
        createTestDiagramModelArchimateObjectsForFindDiagramModelComponents(IArchimateFactory.eINSTANCE.createBusinessActor(), diagramModel);
        
        // And make some connections using the relationship
        createTestDiagramModelConnectionsForFindDiagramModelComponents(relationship);

        // Should not be found
        boolean result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo1, dmo2, relationship2);
        assertFalse(result);
        
        // Should be found
        result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo1, dmo2, relationship);
        assertTrue(result);
        
        result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo3, dmo4, relationship);
        assertTrue(result);
        
        result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo4, dmo1, relationship);
        assertTrue(result);
        
        // Reverse the target and source
        result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo2, dmo1, relationship);
        assertFalse(result);
        
        result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo4, dmo3, relationship);
        assertFalse(result);
        
        result = DiagramModelUtils.hasDiagramModelArchimateConnection(dmo1, dmo4, relationship);
        assertFalse(result);
    }
    
    // =================================================================================================

    @Test
    public void testGetAncestorContainer() {
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IDiagramModelGroup group1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        IDiagramModelGroup group2 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        IDiagramModelObject child = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(group1);
        group1.getChildren().add(group2);
        
        assertNull(DiagramModelUtils.getAncestorContainer(child));
        
        group2.getChildren().add(child);
        assertEquals(group1, DiagramModelUtils.getAncestorContainer(child));
    }
    
    @Test
    public void testHasExistingConnectionType() {
        IDiagramModelObject source = ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createArtifact());
        IDiagramModelObject target = ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createArtifact());
        IAssignmentRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        IDiagramModelConnection conn = ArchimateTestModel.createDiagramModelArchimateConnection(relation);
        conn.connect(source, target);
        
        assertTrue(DiagramModelUtils.hasExistingConnectionType(source, target, relation.eClass()));
        assertFalse(DiagramModelUtils.hasExistingConnectionType(source, target, IArchimatePackage.eINSTANCE.getAccessRelationship()));
    }
    
    @Test
    public void testHasCycle() {
        IDiagramModelObject source = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        IDiagramModelObject target = IArchimateFactory.eINSTANCE.createDiagramModelGroup();;
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        
        conn.connect(source, target);
        assertFalse(DiagramModelUtils.hasCycle(source, target));
        
        conn.connect(target, source);
        assertTrue(DiagramModelUtils.hasCycle(source, target));
        
        conn.disconnect();
        assertFalse(DiagramModelUtils.hasCycle(source, target));
    }
    
}