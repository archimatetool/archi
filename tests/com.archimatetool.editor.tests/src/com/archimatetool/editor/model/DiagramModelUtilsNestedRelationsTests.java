/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;


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
    private IArchimateRelationship relationship1, relationship2;
    
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
    public void testIsNestedConnectionTypeRelationship() {
        // Should be all relations
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            IArchimateRelationship rel = (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(eClass);
            assertTrue(DiagramModelUtils.isNestedConnectionTypeRelationship(rel));
        }
    }
    
    @Test
    public void testShouldBeHiddenConnection() {
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setArchimateRelationship(relationship1);
        connection.connect(dmo1, dmo2);
        assertTrue(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // swap
        connection.connect(dmo2, dmo1);
        assertTrue(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // Set prefs to say no
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.USE_NESTED_CONNECTIONS, false);

        connection.connect(dmo3, dmo4);
        assertFalse(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // Set prefs to say yes
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.USE_NESTED_CONNECTIONS, true);
        
        assertTrue(DiagramModelUtils.shouldBeHiddenConnection(connection));
        
        // Another one to be sure - association
        connection.setArchimateRelationship(relationship2);
        connection.connect(dmo4, dmo5);
        assertTrue(DiagramModelUtils.shouldBeHiddenConnection(connection));
    }
}