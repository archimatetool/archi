/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * DiagramModelUtils Tests specifically for Nested Relations functions
 * 
 * These depend on the user's preferences in ConnectionPreferences
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelUtilsNestedRelationsTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private IDiagramModel dm;
    private IDiagramModelArchimateObject dmo1, dmo2, dmo3, dmo4, dmo5;
    private IArchimateElement element1, element2, element3;
    private IRelationship relationship1, relationship2;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelUtilsNestedRelationsTests.class);
    }
    
    /*

    dm
     |-- dmo1 (element1)
     |     |-- dmo2 (element2)
     |
     |-- dmo3 (element1)
           |-- dmo4  (element2)
                |-- dmo5  (element3)

     */

    
    @Before
    public void runOnceBeforeAllTests() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel();
        dm = model.getDefaultDiagramModel();
        
        // Elements
        element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        element2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        element3 = IArchimateFactory.eINSTANCE.createBusinessActor();
        
        // A nested type relationship
        relationship1 = IArchimateFactory.eINSTANCE.createCompositionRelationship();
        relationship1.setSource(element1);
        relationship1.setTarget(element2);
        model.getArchimateModel().getFolder(FolderType.RELATIONS).getElements().add(relationship1);
        
        // A non-nested type relationship
        relationship2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relationship2.setSource(element2);
        relationship2.setTarget(element3);
        model.getArchimateModel().getFolder(FolderType.RELATIONS).getElements().add(relationship2);
        
        // Add some objects to the diagram
        dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(element1);
        dm.getChildren().add(dmo1);
        
        dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(element2);
        dmo1.getChildren().add(dmo2);
        
        dmo3 = tm.createDiagramModelArchimateObjectAndAddToModel(element1);
        dm.getChildren().add(dmo3);
        
        dmo4 = tm.createDiagramModelArchimateObjectAndAddToModel(element2);
        dmo3.getChildren().add(dmo4);
        
        dmo5 = tm.createDiagramModelArchimateObjectAndAddToModel(element3);
        dmo4.getChildren().add(dmo5);
    }
    
    // =================================================================================================
    

    @Test
    public void testFindNestedComponentsForRelationship() {
        // Find them
        List<IDiagramModelArchimateObject[]> list = DiagramModelUtils.findNestedComponentsForRelationship(dm, relationship1);
        assertEquals(2, list.size());
        
        IDiagramModelArchimateObject[] pair1 = list.get(0);
        assertEquals(dmo1, pair1[0]);
        assertEquals(dmo2, pair1[1]);
        
        IDiagramModelArchimateObject[] pair2 = list.get(1);
        assertEquals(dmo3, pair2[0]);
        assertEquals(dmo4, pair2[1]);
        
        // Swap parent/child for no nesting
        relationship1.setSource(element2);
        relationship1.setTarget(element1);
        list = DiagramModelUtils.findNestedComponentsForRelationship(dm, relationship1);
        assertEquals(0, list.size());
        
        // And a non-nested type relationship
        list = DiagramModelUtils.findNestedComponentsForRelationship(dm, relationship2);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testIsNestedRelationship() {
        assertTrue(DiagramModelUtils.isNestedRelationship(dmo1, dmo2));
        assertTrue(DiagramModelUtils.isNestedRelationship(dmo3, dmo4));
        
        // Swap parent/child for no nesting
        assertFalse(DiagramModelUtils.isNestedRelationship(dmo2, dmo1));
        
        // Use a non-nested type relationship
        assertFalse(DiagramModelUtils.isNestedRelationship(dmo4, dmo5));
    }
    
    @Test
    public void testHasNestedConnectionTypeRelationship() {
        assertTrue(DiagramModelUtils.hasNestedConnectionTypeRelationship(element1, element2));
        
        // Swap parent/child for no nesting
        assertFalse(DiagramModelUtils.hasNestedConnectionTypeRelationship(element2, element1));
        
        // Use a non-nested type relationship
        assertFalse(DiagramModelUtils.hasNestedConnectionTypeRelationship(element2, element3));
    }
    
    @Test
    public void testIsNestedConnectionTypeRelationship() {
        IRelationship rel = IArchimateFactory.eINSTANCE.createCompositionRelationship();
        assertTrue(DiagramModelUtils.isNestedConnectionTypeRelationship(rel));
        
        rel = IArchimateFactory.eINSTANCE.createAggregationRelationship();
        assertTrue(DiagramModelUtils.isNestedConnectionTypeRelationship(rel));
        
        // Not junctions
        rel.setSource(IArchimateFactory.eINSTANCE.createJunction());
        rel.setTarget(IArchimateFactory.eINSTANCE.createJunction());
        assertFalse(DiagramModelUtils.isNestedConnectionTypeRelationship(rel));
        
        rel = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        assertFalse(DiagramModelUtils.isNestedConnectionTypeRelationship(rel));
    }
    
    @Test
    public void testIsNestedConnectionTypeElement() {
        // All these types are OK
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            assertTrue(DiagramModelUtils.isNestedConnectionTypeElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass)));
        }
        
        // Except for Junctions
        assertFalse(DiagramModelUtils.isNestedConnectionTypeElement(IArchimateFactory.eINSTANCE.createJunction()));
        assertFalse(DiagramModelUtils.isNestedConnectionTypeElement(IArchimateFactory.eINSTANCE.createOrJunction()));
        assertFalse(DiagramModelUtils.isNestedConnectionTypeElement(IArchimateFactory.eINSTANCE.createAndJunction()));
    }
    
    @Test
    public void testShouldBeHiddenConnection() {
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setRelationship(relationship1);
        connection.connect(dmo1, dmo2);
        assertTrue(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // swap
        connection.connect(dmo2, dmo1);
        assertFalse(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // Set prefs to say no
        Preferences.STORE.setValue(IPreferenceConstants.USE_NESTED_CONNECTIONS, false);

        connection.connect(dmo3, dmo4);
        assertFalse(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // Set prefs to say yes
        Preferences.STORE.setValue(IPreferenceConstants.USE_NESTED_CONNECTIONS, true);
        
        assertTrue(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        connection.setRelationship(relationship2);
        connection.connect(dmo4, dmo5);
        assertFalse(DiagramModelUtils.shouldBeHiddenConnection(connection));
    }
}