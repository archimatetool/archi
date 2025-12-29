/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.testingtools.ArchimateTestModel;


public class TreeModelViewerDragDropHandlerTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    
    private TreeViewer treeViewer;
    private Shell shell;
    
    // The TreeModelViewerDragDropHandler to test
    private TreeModelViewerDragDropHandler dragHandler;
    
    @BeforeEach
    public void runOnceBeforeEachTest() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel(); // We need a real model and Command Stack for some operations
        shell = new Shell();
        treeViewer = new TreeViewer(shell);
        dragHandler = new TreeModelViewerDragDropHandler(treeViewer);
    }
    
    @AfterEach
    public void runOnceAfterEachTest() {
        shell.dispose();
    }
    
    @Test
    public void testIsValidTreeSelection() {
        // Null selection is not valid
        StructuredSelection selection = null;
        assertFalse(dragHandler.isValidTreeSelection(selection));
        
        // Set up some data
        IFolder userFolder = IArchimateFactory.eINSTANCE.createFolder();
        model.getFolder(FolderType.DIAGRAMS).getFolders().add(userFolder);
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        businessFolder.getElements().add(e1);
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        businessFolder.getElements().add(e2);
        
        // Elements from the same root parent are OK
        selection = new StructuredSelection( new Object[] { e1, e2 } );
        assertTrue(dragHandler.isValidTreeSelection(selection));
        
        // Can't DnD a model
        selection = new StructuredSelection( new Object[] { model } );
        assertFalse(dragHandler.isValidTreeSelection(selection));
        
        // Can DnD a user folder
        selection = new StructuredSelection( new Object[] { userFolder } );
        assertTrue(dragHandler.isValidTreeSelection(selection));
        
        // Can't DnD a system folder
        selection = new StructuredSelection( new Object[] { businessFolder } );
        assertFalse(dragHandler.isValidTreeSelection(selection));
        
        // Can't DnD Mixed parent models
        IArchimateModel model2 = IArchimateFactory.eINSTANCE.createArchimateModel();
        model2.setDefaults();
        IArchimateElement e3 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model2.getFolder(FolderType.BUSINESS).getElements().add(e3);
        
        selection = new StructuredSelection( new Object[] { e1, e3 } );
        assertFalse(dragHandler.isValidTreeSelection(selection));
    }
    
    /**
     * The famous DnD bug of drag and dropping a child folder onto its parent (fixed in Archi 2.4.1)
     */
    @Test
    public void moveTreeObjects_DropChildFolderOntoSameParentIsSame() {
        // Add a child folder to a parent folder
        IFolder parentFolder = model.getFolder(FolderType.BUSINESS);
        IFolder childFolder = IArchimateFactory.eINSTANCE.createFolder();
        parentFolder.getFolders().add(childFolder);
        
        // Check all is well
        assertEquals(1, parentFolder.getFolders().size());
        assertTrue(parentFolder.getFolders().contains(childFolder));
        assertTrue(parentFolder.getElements().isEmpty());
        
        // Move child folder to the same target parent folder
        dragHandler.moveTreeObjects(parentFolder, new Object[] { childFolder });
        
        // Should be the same
        assertEquals(1, parentFolder.getFolders().size());
        assertTrue(parentFolder.getFolders().contains(childFolder));
        // Elements should not be affected
        assertTrue(parentFolder.getElements().isEmpty());
    }
    
    /**
     * Drag and dropping a folder onto a different parent
     */
    @Test
    public void moveTreeObjects_DropChildFolderOntoDifferentParent() {
        // Add a child folder to a parent folder
        IFolder sourceParentFolder = model.getFolder(FolderType.BUSINESS);
        IFolder childFolder = IArchimateFactory.eINSTANCE.createFolder();
        sourceParentFolder.getFolders().add(childFolder);
        
        // Check all is well
        assertEquals(1, sourceParentFolder.getFolders().size());
        assertTrue(sourceParentFolder.getFolders().contains(childFolder));

        // New target parent folder
        IFolder targetParentFolder = model.getFolder(FolderType.APPLICATION);
        assertTrue(targetParentFolder.getFolders().isEmpty());
        assertTrue(targetParentFolder.getElements().isEmpty());
        
        // Move child folder to the different target parent folder
        dragHandler.moveTreeObjects(targetParentFolder, new Object[] { childFolder });
        
        // Source should be empty
        assertTrue(sourceParentFolder.getFolders().isEmpty());
        // Target should be populated
        assertEquals(1, targetParentFolder.getFolders().size());
        assertEquals(childFolder, targetParentFolder.getFolders().get(0));
        // Elements should not be affected
        assertTrue(targetParentFolder.getElements().isEmpty());
    }

    /**
     * Drag and dropping child elements onto same parent
     */
    @Test
    public void moveTreeObjects_DropChildElementsOntoSameParentIsSame() {
        // Add child elements to a  folder
        IFolder parentFolder = model.getFolder(FolderType.BUSINESS);
        
        IArchimateElement childElement1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        parentFolder.getElements().add(childElement1);
        IArchimateElement childElement2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        parentFolder.getElements().add(childElement2);
        
        // Check all is well
        assertTrue(parentFolder.getFolders().isEmpty());
        assertEquals(2, parentFolder.getElements().size());
        assertEquals(childElement1, parentFolder.getElements().get(0));
        assertEquals(childElement2, parentFolder.getElements().get(1));

        // Move child elements to the same parent folder
        dragHandler.moveTreeObjects(parentFolder, new Object[] { childElement1, childElement2 });
        
        // Shouldn't affect folders
        assertTrue(parentFolder.getFolders().isEmpty());
        // Should be the same
        assertEquals(2, parentFolder.getElements().size());
        assertEquals(childElement1, parentFolder.getElements().get(0));
        assertEquals(childElement2, parentFolder.getElements().get(1));
    }
    
    /**
     * Drag and dropping child elements onto different parent
     */
    @Test
    public void moveTreeObjects_DropChildElementsOntoDifferentParent() {
        // Add child elements to a  folder
        IFolder sourceParentFolder = model.getFolder(FolderType.BUSINESS);
        
        IArchimateElement childElement1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        sourceParentFolder.getElements().add(childElement1);
        IArchimateElement childElement2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        sourceParentFolder.getElements().add(childElement2);
        
        // Check all is well
        assertEquals(2, sourceParentFolder.getElements().size());
        assertEquals(childElement1, sourceParentFolder.getElements().get(0));
        assertEquals(childElement2, sourceParentFolder.getElements().get(1));
        
        // New target parent folder and all is well
        IFolder targetParentFolder = model.getFolder(FolderType.APPLICATION);
        assertTrue(targetParentFolder.getFolders().isEmpty());
        assertTrue(targetParentFolder.getElements().isEmpty());

        // Move child element to the different target parent folder
        dragHandler.moveTreeObjects(targetParentFolder, new Object[] { childElement1, childElement2 });
        
        // Source should be empty
        assertTrue(sourceParentFolder.getElements().isEmpty());

        // Shouldn't affect folders
        assertEquals(2, targetParentFolder.getElements().size());
        // Target should be populated
        assertEquals(childElement1, targetParentFolder.getElements().get(0));
        assertEquals(childElement2, targetParentFolder.getElements().get(1));
        assertTrue(targetParentFolder.getFolders().isEmpty());
    }    

    @Test
    public void testHasCommonAncestorFolder() {
        // Test with folders and elements that have a root model
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        IFolder appFolder = model.getFolder(FolderType.APPLICATION);
        __doTestHasCommonAncestorFolderWithTheseFolders(businessFolder, appFolder);
        
        // Test with folders and elements that have no root model
        IFolder parent1 = IArchimateFactory.eINSTANCE.createFolder();
        IFolder parent2 = IArchimateFactory.eINSTANCE.createFolder();
        __doTestHasCommonAncestorFolderWithTheseFolders(parent1, parent2);
    }
    
    private void __doTestHasCommonAncestorFolderWithTheseFolders(IFolder parentFolder1, IFolder parentFolder2) {
        /*
        
        businessFolder 
                   |-- f2
                        |-- e1
                   |-- f3
                        |-- e2
        appFolder
                   |-- f4
                        |-- e3
                   |-- f5
                        |-- e4
         
         */
      
        IFolder f2 = IArchimateFactory.eINSTANCE.createFolder();
        IFolder f3 = IArchimateFactory.eINSTANCE.createFolder();
        parentFolder1.getFolders().add(f2);
        parentFolder1.getFolders().add(f3);
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        f2.getElements().add(e1);
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        f3.getElements().add(e2);
        
        IFolder f4 = IArchimateFactory.eINSTANCE.createFolder();
        IFolder f5 = IArchimateFactory.eINSTANCE.createFolder();
        parentFolder2.getFolders().add(f4);
        parentFolder2.getFolders().add(f5);
        IArchimateElement e3 = IArchimateFactory.eINSTANCE.createApplicationComponent();
        f4.getElements().add(e3);
        IArchimateElement e4 = IArchimateFactory.eINSTANCE.createApplicationComponent();
        f5.getElements().add(e4);
        
        // Same common ancestor
        assertTrue(dragHandler.hasCommonAncestorFolder(f2, f3));
        assertTrue(dragHandler.hasCommonAncestorFolder(f2, e1));
        assertTrue(dragHandler.hasCommonAncestorFolder(f3, e1));
        assertTrue(dragHandler.hasCommonAncestorFolder(e1, e2));
        
        // Different common ancestors
        assertFalse(dragHandler.hasCommonAncestorFolder(f2, f4));
        assertFalse(dragHandler.hasCommonAncestorFolder(f3, f5));
        assertFalse(dragHandler.hasCommonAncestorFolder(f3, e3));
        assertFalse(dragHandler.hasCommonAncestorFolder(e1, e4));
    }
    
    @Test
    public void testCanDropObject() {
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createBusinessActor();

        // Null tree item is no good
        assertFalse(dragHandler.canDropObject(e1, null));
        
        TreeItem treeItem = new TreeItem(treeViewer.getTree(), 0);
        treeItem.setData(e1);

        // Cannot drop onto itself
        assertFalse(dragHandler.canDropObject(e1, treeItem));
        
        // OK
        assertTrue(dragHandler.canDropObject(e2, treeItem));
    }
}
