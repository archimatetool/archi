/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;


@SuppressWarnings("nls")
public class CommandsTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeEach
    public void runBeforeEachTest() throws IOException {
        tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testDeleteDiagramModelCommand() {
        IDiagramModel dm = (IDiagramModel)tm.getObjectByID("3641"); // "ArchiMate View"
        assertNotNull(dm);
        int position = model.getDiagramModels().indexOf(dm);

        DeleteDiagramModelCommand cmd = new DeleteDiagramModelCommand(dm);
        cmd.execute();
        
        assertNull(dm.eContainer());
        assertFalse(model.getDiagramModels().contains(dm));
        
        cmd.undo();
        assertEquals(position, model.getDiagramModels().indexOf(dm));
    }

    @Test
    public void testDeleteArchimateElementCommand() {
        IArchimateElement element = (IArchimateElement)tm.getObjectByID("1544");
        assertNotNull(element);
        
        IFolder parent = (IFolder)element.eContainer();
        int position = parent.getElements().indexOf(element);

        DeleteArchimateElementCommand cmd = new DeleteArchimateElementCommand(element);
        cmd.execute();
        
        assertNull(element.eContainer());
        assertFalse(parent.getElements().contains(element));
        
        cmd.undo();
        assertEquals(position, parent.getElements().indexOf(element));
    }

    @Test
    public void testDeleteArchimateRelationshipCommand() {
        IArchimateRelationship relationship = (IArchimateRelationship)tm.getObjectByID("670aa5ed");
        assertNotNull(relationship);
        
        assertTrue(relationship.getSource().getSourceRelationships().contains(relationship));
        assertTrue(relationship.getTarget().getTargetRelationships().contains(relationship));

        IFolder parent = (IFolder)relationship.eContainer();
        int position = parent.getElements().indexOf(relationship);
        
        DeleteArchimateRelationshipCommand cmd = new DeleteArchimateRelationshipCommand(relationship);
        cmd.execute();
        
        assertNull(relationship.eContainer());
        assertFalse(parent.getElements().contains(relationship));
        
        assertFalse(relationship.getSource().getSourceRelationships().contains(relationship));
        assertFalse(relationship.getTarget().getTargetRelationships().contains(relationship));

        cmd.undo();
        assertEquals(position, parent.getElements().indexOf(relationship));
    }
    
    @Test
    public void testDeleteFolderCommand() {
        IFolder folder = (IFolder)tm.getObjectByID("403e5717");
        assertNotNull(folder);
        
        IFolder parent = (IFolder)folder.eContainer();
        int position = parent.getFolders().indexOf(folder);

        DeleteFolderCommand cmd = new DeleteFolderCommand(folder);
        cmd.execute();
        
        assertNull(folder.eContainer());
        assertFalse(parent.getFolders().contains(folder));
        
        cmd.undo();
        assertEquals(position, parent.getFolders().indexOf(folder));
    }
    
    @Test
    public void testEObjectFeatureCommand() {
        assertEquals("Archisurance", model.getName());
        
        // New name
        Command cmd = new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello");
        cmd.execute();
        assertEquals("Hello", model.getName());
        
        assertTrue(cmd.canExecute());

        cmd.undo();
        assertEquals("Archisurance", model.getName());
        
        // Same value
        cmd = new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Archisurance");
        assertFalse(cmd.canExecute());
    }
    
    @Test
    public void testEObjectNonNotifyingCompoundCommand() {
        final boolean[] execute = new boolean[1];
        
        model.eAdapters().add(new EContentAdapter() {
            @Override
            public void notifyChanged(Notification msg) {
                super.notifyChanged(msg);
                
                if(msg.getEventType() == EObjectNonNotifyingCompoundCommand.START) {
                    assertEquals(model, msg.getNewValue());
                    assertEquals(execute[0] ? "Archisurance" : "Hello3", model.getName());
                }
                else if(msg.getEventType() == EObjectNonNotifyingCompoundCommand.END) {
                    assertEquals(model, msg.getNewValue());
                    assertEquals(execute[0] ? "Hello3" : "Archisurance", model.getName());
                }
            }
        });
        
        CompoundCommand compoundCmd = new EObjectNonNotifyingCompoundCommand(model);
        compoundCmd.add(new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello1"));
        compoundCmd.add(new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello2"));
        compoundCmd.add(new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello3"));
        
        execute[0] = true;
        compoundCmd.execute();
        
        execute[0] = false;
        compoundCmd.undo();
        
        execute[0] = true;
        compoundCmd.redo();
    }
    
    @Test
    public void testNonNotifyingCompoundCommand() {
        final String[] expectedPropertyName = new String[2];
        expectedPropertyName[0] = IEditorModelManager.PROPERTY_ECORE_EVENTS_START;
        expectedPropertyName[1] = IEditorModelManager.PROPERTY_ECORE_EVENTS_END;
        
        PropertyChangeListener listener = new PropertyChangeListener() {
            int sequence = 0;
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                assertEquals(expectedPropertyName[sequence], evt.getPropertyName());
                sequence += sequence == 0 ? 1 : -1;
            }
        };
        
        IEditorModelManager.INSTANCE.addPropertyChangeListener(listener);
        
        CompoundCommand compoundCmd = new NonNotifyingCompoundCommand("command");
        compoundCmd.add(new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello1"));
        compoundCmd.add(new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello2"));
        compoundCmd.add(new EObjectFeatureCommand("Rename", model, IArchimatePackage.Literals.NAMEABLE__NAME, "Hello3"));
        
        compoundCmd.execute();
        compoundCmd.undo();
        compoundCmd.redo();
        
        IEditorModelManager.INSTANCE.removePropertyChangeListener(listener);
    }

}
