/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.ui.findreplace.AbstractFindReplaceProvider;
import com.archimatetool.editor.ui.findreplace.AbstractFindReplaceProviderTests;
import com.archimatetool.editor.ui.findreplace.IFindReplaceProvider;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.INameable;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestEditor;
import com.archimatetool.testingtools.ArchimateTestModel;


@SuppressWarnings("nls")
public class DiagramEditorFindReplaceProviderTests extends AbstractFindReplaceProviderTests {
    
    private static ArchimateTestModel tm;
    private static IArchimateModel model;
    private static IArchimateDiagramModel dm;
    private static ArchimateTestEditor editor;

    private DiagramEditorFindReplaceProvider provider;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramEditorFindReplaceProviderTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        tm = new ArchimateTestModel(new File(TestSupport.getTestDataFolder(), "models/testFindReplace.archimate"));
        model = tm.loadModelWithCommandStack();
        dm = (IArchimateDiagramModel)ArchimateModelUtils.getObjectByID(model, "6a346079");
        
        editor = new ArchimateTestEditor();
        editor.setDiagramModel(dm);
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        // Deselect all nodes in Viewer
        editor.getGraphicalViewer().setSelection(StructuredSelection.EMPTY);
    }

    @Override
    protected AbstractFindReplaceProvider getProvider() {
        provider = new DiagramEditorFindReplaceProvider(editor.getGraphicalViewer());
        return provider;
    }
    
    @Test
    public void testFind() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        
        boolean result = provider.find("FindMe");
        assertTrue(result);
        assertEquals(1, ((IStructuredSelection)editor.getGraphicalViewer().getSelection()).size());
    }

    @Test
    public void testFindAll() {
        // Set to Find All
        provider.setParameter(IFindReplaceProvider.PARAM_ALL, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        // Ignore case
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        assertTrue(provider.find("findme"));
        assertEquals(5, ((IStructuredSelection)editor.getGraphicalViewer().getSelection()).size());
        
        // Don't ignore case
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        assertFalse(provider.find("findme"));
        
        // No relations
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, false);
        assertTrue(provider.find("FindMe"));
        assertEquals(4, ((IStructuredSelection)editor.getGraphicalViewer().getSelection()).size());
    }
    
    @Test
    public void testFindNextElement_Forward_NotFound() {
        String searchString = "FindMex";
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        
        assertNull(provider.findNextEditPart(null, searchString));
    }

    @Test
    public void testFindNextElement_Backward_NotFound() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        
        // Start from end element
        String searchString = "FindMex";

        // Start from no selection
        assertNull(provider.findNextEditPart(null, searchString));
    }

    @Test
    public void testFindNextElement_Forward_CaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        String searchString = "Find";
        
        EditPart editPart = provider.findNextEditPart(null, searchString);
        assertEquals("FindMe 1", ((INameable)editPart.getModel()).getName());
        
        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 9", ((INameable)editPart.getModel()).getName());
        
        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 2", ((INameable)editPart.getModel()).getName());

        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 3", ((INameable)editPart.getModel()).getName());

        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 4", ((INameable)editPart.getModel()).getName());

        // should cycle back to the beginning
        editPart = provider.findNextEditPart(null, searchString);
        assertEquals("FindMe 1", ((INameable)editPart.getModel()).getName());
    }

    @Test
    public void testFindNextElement_Backward_NotCaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        String searchString = "find";
        
        EditPart editPart = provider.findNextEditPart(null, searchString);
        assertEquals("FindMe 4", ((INameable)editPart.getModel()).getName());
        
        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 3", ((INameable)editPart.getModel()).getName());
        
        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 2", ((INameable)editPart.getModel()).getName());

        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 9", ((INameable)editPart.getModel()).getName());

        editPart = provider.findNextEditPart(editPart, searchString);
        assertEquals("FindMe 1", ((INameable)editPart.getModel()).getName());

        // should cycle back to the beginning
        editPart = provider.findNextEditPart(null, searchString);
        assertEquals("FindMe 4", ((INameable)editPart.getModel()).getName());
    }
}
