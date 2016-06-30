/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.findreplace.AbstractFindReplaceProvider;
import com.archimatetool.editor.ui.findreplace.AbstractFindReplaceProviderTests;
import com.archimatetool.editor.ui.findreplace.IFindReplaceProvider;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.INameable;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class TreeModelViewerFindReplaceProviderTests extends AbstractFindReplaceProviderTests {
    
    private static IArchimateModel model1;
    private static IArchimateModel model2;
    
    private static TreeModelViewer treeViewer;
    
    private TreeModelViewerFindReplaceProvider provider;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TreeModelViewerFindReplaceProviderTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // Load two models
        model1 = IEditorModelManager.INSTANCE.loadModel(new File(TestSupport.getTestDataFolder(), "models/testFindReplace.archimate"));
        model2 = IEditorModelManager.INSTANCE.loadModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        
        treeViewer = new TreeModelViewer(new Shell(), SWT.NONE);
        treeViewer.setInput(IEditorModelManager.INSTANCE);
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() {
        treeViewer.getControl().getShell().dispose();
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        // Deselect all nodes in Tree
        treeViewer.setSelection(StructuredSelection.EMPTY);
    }

    @Override
    protected AbstractFindReplaceProvider getProvider() {
        provider = new TreeModelViewerFindReplaceProvider(treeViewer);
        return provider;
    }
    
    @Test
    public void testFind() {
        // Find in all models
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        
        boolean result = provider.find("FindMe");
        assertTrue(result);
        assertEquals(1, ((IStructuredSelection)treeViewer.getSelection()).size());
    }

    @Test
    public void testFindAll() {
        // Set to Find All
        provider.setParameter(IFindReplaceProvider.PARAM_ALL, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        // Find in all models
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        _testFindAll();

        // Find in selected model
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, false);
        treeViewer.setSelection(new StructuredSelection(model1));
        _testFindAll();
    }
    
    private void _testFindAll() {
        // Ignore case
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        boolean result = provider.find("findme");
        assertTrue(result);
        assertEquals(11, ((IStructuredSelection)treeViewer.getSelection()).size());
        
        // Don't ignore case
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        result = provider.find("findme");
        assertFalse(result);
    }

    @Test
    public void testFindNextElement_Forward_NotFound() {
        String searchString = "FindMex";
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        
        assertNull(provider.findNextElement(null, searchString));
    }

    @Test
    public void testFindNextElement_Backward_NotFound() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        
        // Start from end element
        String searchString = "FindMex";
        Object startElement = ArchimateModelUtils.getObjectByID(model1, "2b0cb580");
        assertNotNull(startElement);
        assertNull(provider.findNextElement(startElement, searchString));
        
        // Start from no selection
        assertNull(provider.findNextElement(null, searchString));
    }

    @Test
    public void testFindNextElement_Forward_CaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        String searchString = "Find";
        
        INameable element = provider.findNextElement(null, searchString);
        assertEquals("FindMe 0", element.getName());
        
        for(int i = 1; i < 11; i++) {
            element = provider.findNextElement(element, searchString);
            assertEquals("FindMe " + i, element.getName());
        }
        
        // No more
        assertNull(provider.findNextElement(element, searchString));
    }

    @Test
    public void testFindNextElement_Forward_NotCaseSensitive_1() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        String searchString = "findme";
        
        INameable element = provider.findNextElement(null, searchString);
        assertEquals("FindMe 0", element.getName());
        
        for(int i = 1; i < 11; i++) {
            element = provider.findNextElement(element, searchString);
            assertEquals("FindMe " + i, element.getName());
        }
        
        // No more
        assertNull(provider.findNextElement(element, searchString));
    }

    @Test
    public void testFindNextElement_Forward_NotCaseSensitive_2() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        
        String searchString = "director";
        
        INameable element = provider.findNextElement(null, searchString);
        assertEquals("Director of Finance", element.getName());
        element = provider.findNextElement(element, searchString);
        assertEquals("Director of Operations", element.getName());
        element = provider.findNextElement(element, searchString);
        assertEquals("Director of Sales", element.getName());
        
        // No more
        assertNull(provider.findNextElement(element, searchString));
    }

    @Test
    public void testFindNextElement_Backward_CaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        String searchString = "Find";
        
        Object startElement = ArchimateModelUtils.getObjectByID(model1, "2b0cb580");
        assertNotNull(startElement);
        
        INameable element = provider.findNextElement(startElement, searchString);
        assertEquals("FindMe 10", element.getName());
        
        for(int i = 9; i >= 0; i--) {
            element = provider.findNextElement(element, searchString);
            assertEquals("FindMe " + i, element.getName());
        }

        // No more
        assertNull(provider.findNextElement(element, searchString));
    }

    @Test
    public void testFindNextElement_Backward_NotCaseSensitive_1() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        String searchString = "findme";
        
        Object startElement = ArchimateModelUtils.getObjectByID(model1, "2b0cb580");
        assertNotNull(startElement);
        
        INameable element = provider.findNextElement(startElement, searchString);
        assertEquals("FindMe 10", element.getName());
        
        for(int i = 9; i >= 0; i--) {
            element = provider.findNextElement(element, searchString);
            assertEquals("FindMe " + i, element.getName());
        }

        // No more
        assertNull(provider.findNextElement(element, searchString));
    }
    
    @Test
    public void testFindNextElement_Backward_NotCaseSensitive_2() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        
        String searchString = "director";
        
        Object startElement = ArchimateModelUtils.getObjectByID(model2, "16fe3cf9");
        assertNotNull(startElement);
        
        INameable element = provider.findNextElement(startElement, searchString);
        assertEquals("Director of Sales", element.getName());
        element = provider.findNextElement(element, searchString);
        assertEquals("Director of Operations", element.getName());
        element = provider.findNextElement(element, searchString);
        assertEquals("Director of Finance", element.getName());
        
        // No more
        assertNull(provider.findNextElement(element, searchString));
    }

    @Test
    public void testGetAllMatchingElements_NotFound() {
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        
        String searchString = "FindMex";
        assertTrue(provider.getAllMatchingElements(searchString).isEmpty());
    }
    
    @Test
    public void testGetAllMatchingElements_CaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        assertEquals(11, provider.getAllMatchingElements("Find").size());
        assertEquals(1, provider.getAllMatchingElements("Business Role").size());
        assertEquals(13, provider.getAllMatchingElements("Customer").size());
    }

    @Test
    public void testGetAllMatchingElements_NotCaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        assertEquals(11, provider.getAllMatchingElements("findme").size());
        assertEquals(1, provider.getAllMatchingElements("business Role").size());
        assertEquals(14, provider.getAllMatchingElements("customer").size());
    }
    
    @Test
    public void testGetAllMatchingElements_CaseSensitive_WholeWord() {
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        provider.setParameter(IFindReplaceProvider.PARAM_WHOLE_WORD, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        assertEquals(11, provider.getAllMatchingElements("FindMe").size());
        assertEquals(1, provider.getAllMatchingElements("Business Role").size());
        assertEquals(5, provider.getAllMatchingElements("Business").size());
        assertEquals(13, provider.getAllMatchingElements("Customer").size());
        
        assertEquals(0, provider.getAllMatchingElements("Find").size());
        assertEquals(0, provider.getAllMatchingElements("Business Rol").size());
        assertEquals(0, provider.getAllMatchingElements("Busines").size());
        assertEquals(0, provider.getAllMatchingElements("Custom").size());
    }

    @Test
    public void testGetAllMatchingElements_NotCaseSensitive_WholeWord() {
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        provider.setParameter(IFindReplaceProvider.PARAM_ALL_MODELS, true);
        provider.setParameter(IFindReplaceProvider.PARAM_WHOLE_WORD, true);
        // Relations as well
        provider.setParameter(IFindReplaceProvider.PARAM_INCLUDE_RELATIONS, true);
        
        assertEquals(11, provider.getAllMatchingElements("findMe").size());
        assertEquals(1, provider.getAllMatchingElements("BusIness rolE").size());
        assertEquals(5, provider.getAllMatchingElements("busiNEss").size());
        assertEquals(14, provider.getAllMatchingElements("customer").size());
        
        assertEquals(0, provider.getAllMatchingElements("find").size());
        assertEquals(0, provider.getAllMatchingElements("busIness rol").size());
        assertEquals(0, provider.getAllMatchingElements("busines").size());
        assertEquals(0, provider.getAllMatchingElements("custom").size());
    }
}
