/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class MRUMenuManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MRUMenuManagerTests.class);
    }
    
    private MRUMenuManager menuManager;
    
    @Before
    public void runOnceBeforeEachTest() {
        // Clear prefs
        ArchiPlugin.PREFERENCES.setToDefault(IPreferenceConstants.MRU_MAX);
        for(int i = 0; i < 50; i++) {
            ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + i, "");
        }

        menuManager = new MRUMenuManager(mock(IWorkbenchWindow.class));
    }
    
    @Test
    public void testListIsEmpty() {
        List<File> list = menuManager.getMRUList();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListIsPopulated() {
        ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + "0", "someFile1");
        ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + "1", "someFile2");
        
        // Need a new instance now
        menuManager = new MRUMenuManager(mock(IWorkbenchWindow.class));
        
        List<File> list = menuManager.getMRUList();
        assertEquals(2, list.size());
    }

    @Test
    public void testAddToList() {
        List<File> list = menuManager.getMRUList();
        assertTrue(list.isEmpty());
        
        // Check Max size
        assertEquals(6, menuManager.getPreferencesMRUMax());
        
        // Set up 7 files
        File[] files = new File[7];
        for(int i = 0; i < files.length; i++) {
            files[i] = new File("file" + i);
        }
        
        // Just one entry
        menuManager.addToList(files[0]);
        assertEquals(1, list.size());
        
        // Add the same file again and the list should be the same size
        menuManager.addToList(files[0]);
        assertEquals(1, list.size());
        
        // Add another file, it should be at the top
        menuManager.addToList(files[1]);
        assertEquals(2, list.size());
        assertEquals(files[1], list.get(0));
        
        // Add more than max files to the list...
        for(int i = 2; i < files.length; i++) {
            menuManager.addToList(files[i]);
        }
        
        // List should be the same size as max
        assertEquals(6, list.size());
        
        // List should be last in at the top
        for(int i = 0; i < list.size(); i++) {
            assertEquals(files[6 - i], list.get(i));
        }
    }
    
    @Test
    public void testMenuItemsAreCorrect() {
        // Separator and Clear Action
        assertEquals(2, menuManager.getSize());
        assertTrue(menuManager.getItems()[0] instanceof Separator);
        assertTrue(menuManager.getItems()[1] instanceof ActionContributionItem);
        
        // Add some MRU files
        ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + "0", "someFile1");
        ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + "1", "someFile2");
        
        // Need a new instance
        menuManager = new MRUMenuManager(mock(IWorkbenchWindow.class));
        
        // Should be added
        assertEquals(4, menuManager.getSize());
        assertTrue(menuManager.getItems()[0] instanceof ActionContributionItem);
        assertTrue(menuManager.getItems()[1] instanceof ActionContributionItem);
        assertTrue(menuManager.getItems()[2] instanceof Separator);
    }
    
    @Test
    public void testClearAll() {
        // Add some MRU files
        ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + "0", "someFile1");
        ArchiPlugin.PREFERENCES.setValue(MRUMenuManager.MRU_PREFS_KEY + "1", "someFile2");
        
        // Need a new instance after 
        menuManager = new MRUMenuManager(mock(IWorkbenchWindow.class));
        
        // Should be added
        assertEquals(4, menuManager.getSize());
        assertTrue(menuManager.getItems()[0] instanceof ActionContributionItem);
        assertTrue(menuManager.getItems()[1] instanceof ActionContributionItem);
        assertTrue(menuManager.getItems()[2] instanceof Separator);
        
        assertEquals(2, menuManager.getMRUList().size());
        
        // Clear it
        menuManager.clearAll();
        
        assertEquals(2, menuManager.getSize());
        assertTrue(menuManager.getMRUList().isEmpty());
    }
    
    @Test
    public void testGetPreferencesMRUMax() {
        assertEquals(6, menuManager.getPreferencesMRUMax());
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.MRU_MAX, 8);
        assertEquals(8, menuManager.getPreferencesMRUMax());
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.MRU_MAX, 2);
        assertEquals(3, menuManager.getPreferencesMRUMax());
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.MRU_MAX, 16);
        assertEquals(15, menuManager.getPreferencesMRUMax());
    }
    
    
    @Test
    public void testModelPropertyChange() {
        List<File> list = menuManager.getMRUList();
        assertTrue(list.isEmpty());
        
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        // Open File event - but file does not exist so not on MRU list when opened
        File file = new File("aFile");
        assertFalse(file.exists());
        model.setFile(file);
        IEditorModelManager.INSTANCE.firePropertyChange(this, IEditorModelManager.PROPERTY_MODEL_OPENED, null, model);
        assertTrue(list.isEmpty());
        
        // Open File event - file does exist so it is added to MRU list
        model.setFile(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        IEditorModelManager.INSTANCE.firePropertyChange(this, IEditorModelManager.PROPERTY_MODEL_OPENED, null, model);
        assertEquals(1, list.size());
        
        // Save File event - file does exist so it is added to MRU list
        model.setFile(TestSupport.TEST_MODEL_FILE_1);
        IEditorModelManager.INSTANCE.firePropertyChange(this, IEditorModelManager.PROPERTY_MODEL_SAVED, null, model);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testIsTempFileInTempFolder() throws IOException {
        // File in temp dir
        File file = File.createTempFile("~architemplate", null);
        assertTrue(menuManager.isTempFile(file));
        file.delete();
    }
    
    @Test
    public void testIsTempFileInTempSubFolder() {
        File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
        
        // File in sub dir
        File file = new File(tmpFolder, "folder/folder/file.txt");
        assertTrue(menuManager.isTempFile(file));
        
        // Top
        file = new File(tmpFolder, "file.txt");
        assertTrue(menuManager.isTempFile(file));
    }

    @Test
    public void testIsNotTempFile() {
        // Normal file
        File file = new File("newfile.archimate");
        assertFalse(menuManager.isTempFile(file));
    }
    
    @Test
    public void testGetShortPath() throws IOException {
        File file = new File("/file.archimate");
        String s = menuManager.getShortPath(file);
        assertEquals(file.getCanonicalPath(), s);
        
        file = new File("/thepath/anotherpath/file.archimate");
        s = menuManager.getShortPath(file);
        assertEquals(file.getCanonicalPath(), s);
        
        // Exactly 38 chars for parent path part
        file = new File("/thepath/alongerpath_added/anotherpath/file.archimate");
        assertEquals(38, file.getParent().length());
        s = menuManager.getShortPath(file);
        assertEquals(file.getCanonicalPath(), s);
        
        // Longer path
        file = new File("/thepath/amuchlongerpath_added_here/anotherpath/file.archimate");
        assertEquals(62, file.getPath().length());
        s = menuManager.getShortPath(file);
        assertEquals(53, s.length());
        String expected = "/thepath/amuchlongerpath_added_here.../file.archimate".replace("/", File.separator); // platform specific path separator
        assertEquals(expected, s);
    }
    
}