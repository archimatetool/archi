/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.tests.TestData;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;



@SuppressWarnings("nls")
public class EditorModelManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EditorModelManagerTests.class);
    }
    
    
    private IEditorModelManager editorModelManager;
    
    @Before
    public void runBeforeEachTest() {
        editorModelManager = new EditorModelManager();
    }

    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getModels_IsEmpty() {
        assertEquals(0, editorModelManager.getModels().size());
    }

    @Test
    public void createNewModel_IsValid() {
        IArchimateModel model = editorModelManager.createNewModel();
        assertNotNull(model);
        
        // Has default folders
        assertFalse(model.getFolders().isEmpty());
        
        // Has One Default View
        assertTrue(model.getFolder(FolderType.DIAGRAMS).getElements().get(0) instanceof IArchimateDiagramModel); //$NON-NLS-1$
        
        // Has a Command Stack
        assertTrue(model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue(model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
    }
    
    @Test
    public void openModel_File_Null() {
        IArchimateModel model = editorModelManager.openModel((File)null);
        assertNull(model);
    }

    @Test
    public void openModel_File() {
        File file = TestData.TEST_MODEL_FILE_ARCHISURANCE;
        
        IArchimateModel model = editorModelManager.openModel(file);
        assertNotNull(model);
        
        // Is registered
        assertEquals(1, editorModelManager.getModels().size());
        assertTrue(editorModelManager.getModels().contains(model));
        
        // Do it again, should be the same
        IArchimateModel model2 = editorModelManager.openModel(file);
        assertEquals(model2, model);

        // Is not registered twice
        editorModelManager.openModel(model);
        assertEquals(1, editorModelManager.getModels().size());
    }
    
    @Test
    public void openModel_Model() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setName("Test");
        
        editorModelManager.openModel(model);
        
        // Has a Command Stack
        assertTrue(model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue(model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
        
        // Is registered
        assertEquals(1, editorModelManager.getModels().size());
        assertTrue(editorModelManager.getModels().contains(model));
        
        // Is not registered twice
        editorModelManager.openModel(model);
        assertEquals(1, editorModelManager.getModels().size());
    }

    @Test
    public void loadModel_File_Null() {
        IArchimateModel model = editorModelManager.loadModel(null);
        assertNull(model);
    }
        
    @Test
    public void loadModel_File() {
        File file = TestData.TEST_MODEL_FILE_ARCHISURANCE;
        
        IArchimateModel model = editorModelManager.loadModel(file);
        assertNotNull(model);
        
        // File
        assertEquals(file, model.getFile());
        
        // Has a Command Stack
        assertTrue(model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue(model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
        
        // Is registered
        assertEquals(1, editorModelManager.getModels().size());
        assertTrue(editorModelManager.getModels().contains(model));
        
        // Do it again, should be the same
        IArchimateModel model2 = editorModelManager.loadModel(file);
        assertEquals(model2, model);

        // Is not registered twice
        editorModelManager.openModel(model);
        assertEquals(1, editorModelManager.getModels().size());
    }
    
    @Test
    public void load() throws Exception {
        File file = TestData.TEST_MODEL_FILE_ARCHISURANCE;
        
        IArchimateModel model = editorModelManager.load(file);
        assertNotNull(model);
        
        // File
        assertEquals(file, model.getFile());
        
        // Has a Command Stack
        assertTrue(model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue(model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
        
        // Is *not* registered
        assertEquals(0, editorModelManager.getModels().size());
        assertFalse(editorModelManager.getModels().contains(model));
        
        // Do it again, should *not* be the same
        IArchimateModel model2 = editorModelManager.loadModel(file);
        assertNotEquals(model2, model);
    }

    @Test
    public void isModelLoaded_File() {
        File file = TestData.TEST_MODEL_FILE_ARCHISURANCE;
        assertFalse(editorModelManager.isModelLoaded(file));
        
        editorModelManager.loadModel(file);
        assertTrue(editorModelManager.isModelLoaded(file));
    }

    @Test
    public void isModelDirty_Model() {
        IArchimateModel model = editorModelManager.createNewModel();
        assertFalse(editorModelManager.isModelDirty(model));
        
        // Execute simple command on Command Stack
        Command cmd = new EObjectFeatureCommand("", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello");
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        stack.execute(cmd);
        assertTrue(editorModelManager.isModelDirty(model));
        
        // Flush the Command Stack so we can close the model without a dialog asking us to save
        stack.flush();
    }

    @Test
    public void createNewArchiveManager_Created() throws Exception {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        IArchiveManager archiveManager = (IArchiveManager)TestUtils.invokePrivateMethod(editorModelManager, "createNewArchiveManager",
                new Class[] { IArchimateModel.class }, new Object[] { model });
        
        assertNotNull(archiveManager);
        assertTrue(model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
    }
    
    @Test
    public void saveModel() throws Exception {
        File file = TestData.TEST_MODEL_FILE_ARCHISURANCE;
        
        IArchimateModel model = editorModelManager.loadModel(file);
        assertNotNull(model);
        
        File tmpFile = TestUtils.createTempFile(".archimate");
        model.setFile(tmpFile);
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.BACKUP_ON_SAVE, false);
        
        boolean result = editorModelManager.saveModel(model);
        assertTrue(result);
    }
}
