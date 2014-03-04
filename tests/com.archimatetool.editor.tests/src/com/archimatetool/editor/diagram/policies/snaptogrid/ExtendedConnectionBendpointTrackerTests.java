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

import org.eclipse.gef.GraphicalViewer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.tests.TestUtils;

public class ExtendedConnectionBendpointTrackerTests {
    
    private static ArchimateTestModel tm;
    private static IArchimateModel model;
    private static IDiagramModel dm;
    private static GraphicalViewer viewer;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ExtendedConnectionBendpointTrackerTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel();
        dm = model.getDefaultDiagramModel();
        
        IDiagramModelEditor editor = EditorManager.openDiagramEditor(dm);
        viewer = editor.getGraphicalViewer();
    }

    @AfterClass
    public static void runOnceAfterAllTests() {
        TestUtils.closeAllEditors();
    }

    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testIsSnapToGridEnabled() {
        ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker();
        tracker.setViewer(viewer);
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SNAP, true);
        assertTrue(tracker.isSnapToGridEnabled());
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SNAP, false);
        assertFalse(tracker.isSnapToGridEnabled());
    }

    @Test
    public void testGetSnapGridSize() {
        ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker();
        tracker.setViewer(viewer);
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SIZE, 12);
        assertEquals(12, tracker.getSnapGridSize());
        
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SIZE, 50);
        assertEquals(50, tracker.getSnapGridSize());
    }
    
}