/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.sketch.SketchEditor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class ArchimateDiagramModelFactoryTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateDiagramModelFactoryTests.class);
    }
    
    @Test
    public void testCreateDiagramModelArchimateObject() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessProcess();
        
        IPreferenceStore store = ArchiPlugin.PREFERENCES;
        
        store.setValue(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + element.eClass().getName(), 1);
        store.setValue(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR, true);
        store.setValue(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX + element.eClass().getName(), "#ededed");
        
        IDiagramModelArchimateObject dmo = ArchimateDiagramModelFactory.createDiagramModelArchimateObject(element);
        assertNotNull(dmo);
        assertEquals(element, dmo.getArchimateElement());

        assertEquals(1, dmo.getType());
        assertEquals("#ededed", dmo.getFillColor());
        
        store.setToDefault(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + element.eClass().getName());
        store.setToDefault(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR);
        store.setToDefault(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX + element.eClass().getName());
    }

    @Test
    public void testIsUsedFor() {
        ICreationFactory factory = new ArchimateDiagramModelFactory(null);
        assertTrue(factory.isUsedFor(new ArchimateDiagramEditor()));
        assertFalse(factory.isUsedFor(new SketchEditor()));
    }
    
    @Test
    public void testGetNewObjectArchimateConnection() {
        IPreferenceStore store = ArchiPlugin.PREFERENCES;
        
        store.setValue(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR, true);
        store.setValue(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR, "#ababab");
        
        ICreationFactory factory = new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getAssignmentRelationship());
        IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)factory.getNewObject();
        assertTrue(connection.getArchimateRelationship() instanceof IAssignmentRelationship);
        assertEquals("#ababab", connection.getLineColor());
        assertEquals("", connection.getName());
        
        store.setToDefault(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR);
        store.setToDefault(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR);
    }
    
    @Test
    public void testGetNewObjectArchimateDiagramObject() {
        ICreationFactory factory = new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getBusinessActor());
        IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)factory.getNewObject();
        assertTrue(dmo.getArchimateElement() instanceof IBusinessActor);
        assertEquals("Business Actor", dmo.getName());
    }

    @Test
    public void testGetNewObjectGroup() {
        ICreationFactory factory = new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        IDiagramModelGroup group = (IDiagramModelGroup)factory.getNewObject();
        assertEquals("Group", group.getName());
    }
    
    @Test
    public void testGetNewObjectNote() {
        ICreationFactory factory = new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelNote());
        IDiagramModelNote note = (IDiagramModelNote)factory.getNewObject();
        assertEquals("", note.getName());
    }
    
    @Test
    public void testGetNewObjectConnection() {
        ICreationFactory factory = new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelConnection());
        IDiagramModelConnection connection = (IDiagramModelConnection)factory.getNewObject();
        assertEquals("", connection.getName());
    }
}
