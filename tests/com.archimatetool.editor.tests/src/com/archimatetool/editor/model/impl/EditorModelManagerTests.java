/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.junit.Test;


import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.impl.EditorModelManager;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.tests.TestUtils;



@SuppressWarnings("nls")
public class EditorModelManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EditorModelManagerTests.class);
    }
    
    /**
     * Determine if model has an ECoreAdapter added
     */
    boolean hasECoreAdapter(IArchimateModel model) {
        Class<?> clazz = TestUtils.getMemberClass(EditorModelManager.class, "com.archimatetool.editor.model.impl.EditorModelManager$ECoreAdapter");
        for(Adapter a : model.eAdapters()) {
            if(clazz.isInstance(a)) {
                return true;
            }
        }
        
        return false;
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getModels_IsEmpty() {
        assertEquals("Should have no models", 0, IEditorModelManager.INSTANCE.getModels().size());
    }

    @Test
    public void createNewModel_IsValid() throws Exception {
        IArchimateModel model = IEditorModelManager.INSTANCE.createNewModel();
        assertNotNull("Model was null", model);
        
        // Has default folders
        assertFalse("No model folders", model.getFolders().isEmpty());
        
        // Has One Default View
        assertTrue("No default view", model.getFolder(FolderType.DIAGRAMS).getElements().get(0) instanceof IArchimateDiagramModel); //$NON-NLS-1$
        
        // Has a Command Stack
        assertTrue("No Command Stack", model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue("No Archive Manager", model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
        
        // Has an ECore Adapter
        assertTrue("No ECore Adapter", hasECoreAdapter(model));
        
        IEditorModelManager.INSTANCE.closeModel(model);
    }
    
    @Test
    public void openModel_File() throws Exception {
        File file = TestSupport.TEST_MODEL_FILE_ARCHISURANCE;
        
        IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
        assertNotNull("Model was null", model);
        
        IEditorModelManager.INSTANCE.closeModel(model);
    }
    
    @Test
    public void openModel_Model() throws Exception {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setName("Test");
        
        IEditorModelManager.INSTANCE.openModel(model);
        
        // Has a Command Stack
        assertTrue("No Command Stack", model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue("No Archive Manager", model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
        
        // Has an ECore Adapter
        assertTrue("No ECore Adapter", hasECoreAdapter(model));
        
        IEditorModelManager.INSTANCE.closeModel(model);
    }

    @Test
    public void loadModel_File() throws Exception {
        File file = TestSupport.TEST_MODEL_FILE_ARCHISURANCE;
        
        IArchimateModel model = IEditorModelManager.INSTANCE.loadModel(file);
        assertNotNull("Model was null", model);
        
        // File
        assertEquals("Wrong File", file, model.getFile());
        
        // Has a Command Stack
        assertTrue("No Command Stack", model.getAdapter(CommandStack.class) instanceof CommandStack);

        // Has an Archive Manager
        assertTrue("No Archive Manager", model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
        
        // Has an ECore Adapter
        assertTrue("No ECore Adapter", hasECoreAdapter(model));
        
        IEditorModelManager.INSTANCE.closeModel(model);
    }
    
    @Test
    public void isModelLoaded_File() throws Exception {
        File file = TestSupport.TEST_MODEL_FILE_ARCHISURANCE;
        assertFalse("Model Loaded", IEditorModelManager.INSTANCE.isModelLoaded(file));
        IArchimateModel model = IEditorModelManager.INSTANCE.loadModel(file);
        assertTrue("Model Not Loaded", IEditorModelManager.INSTANCE.isModelLoaded(file));
        IEditorModelManager.INSTANCE.closeModel(model);
    }

    @Test
    public void isModelDirty_Model() throws Exception {
        IArchimateModel model = IEditorModelManager.INSTANCE.createNewModel();
        assertFalse("Model Dirty", IEditorModelManager.INSTANCE.isModelDirty(model));
        
        // Execute simple command
        Command cmd = new EObjectFeatureCommand("", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello");
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        stack.execute(cmd);
        assertTrue("Model Not Dirty", IEditorModelManager.INSTANCE.isModelDirty(model));
        
        // So we can close the model without dialog
        stack.flush();
        
        IEditorModelManager.INSTANCE.closeModel(model);
    }

    @Test
    public void createNewArchiveManager_Created() throws Exception {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        IArchiveManager archiveManager = (IArchiveManager)TestUtils.invokePrivateMethod(IEditorModelManager.INSTANCE, "createNewArchiveManager",
                new Class[] { IArchimateModel.class }, new Object[] { model });
        
        assertNotNull("Archive Manager was null", archiveManager);
        assertTrue("No Archive Manager", model.getAdapter(IArchiveManager.class) instanceof IArchiveManager);
    }
}
