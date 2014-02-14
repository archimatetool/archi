/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies.snaptogrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.model.impl.EditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;

public class ExtendedConnectionBendpointTrackerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ExtendedConnectionBendpointTrackerTests.class);
    }
    
    @Before
    public void runBeforeEachTest() {
    }

    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testIsSnapToGridEnabled() {
        IArchimateModel model = new EditorModelManager().openModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        IDiagramModel dm = model.getDiagramModels().get(1);
        IDiagramModelEditor editor = EditorManager.openDiagramEditor(dm);
        
        ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker();
        tracker.setViewer(editor.getGraphicalViewer());
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SNAP, true);
        assertTrue(tracker.isSnapToGridEnabled());
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SNAP, false);
        assertFalse(tracker.isSnapToGridEnabled());
    }

    @Test
    public void testGetSnapGridSize() {
        IArchimateModel model = new EditorModelManager().openModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        IDiagramModel dm = model.getDiagramModels().get(1);
        IDiagramModelEditor editor = EditorManager.openDiagramEditor(dm);
        
        ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker();
        tracker.setViewer(editor.getGraphicalViewer());
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SIZE, 12);
        assertEquals(12, tracker.getSnapGridSize());
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SIZE, 50);
        assertEquals(50, tracker.getSnapGridSize());
    }
    
}