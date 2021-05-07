/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;



/**
 * This is an example of overkill in the world of Unit Tests.
 * It uses Mockito's mock objects
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeModelViewerDragDropHandlerTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    
    // The real TreeModelViewerDragDropHandler to test
    private TreeModelViewerDragDropHandler dragHandler;
    
    @Mock
    private TreeViewer treeViewer; // Mock the TreeViewer
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TreeModelViewerDragDropHandlerTests.class);
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel(); // We need a real model and Command Stack for some operations
        dragHandler = new TreeModelViewerDragDropHandler(treeViewer);
    }
    
    @Test
    public void verifyDragHandlerRegisteredDragSupport() {
        // Verify treeViewer added drag support
        verify(treeViewer).addDragSupport(
                eq(DND.DROP_COPY | DND.DROP_MOVE),
                eq(dragHandler.sourceTransferTypes),
                any(DragSourceListener.class));
    }

    @Test
    public void verifyDragHandlerRegisteredDropSupport() {
        // Verify treeViewer added drop support
        verify(treeViewer).addDropSupport(
                eq(DND.DROP_MOVE),
                eq(dragHandler.targetTransferTypes),
                any(DropTargetListener.class));
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
    
    @Test
    public void testDoDropOperation() {
        // Source
        IFolder sourceParentFolder = model.getFolder(FolderType.BUSINESS);
        IArchimateElement childElement = IArchimateFactory.eINSTANCE.createBusinessActor();
        sourceParentFolder.getElements().add(childElement);
        assertTrue(sourceParentFolder.getElements().contains(childElement));
        
        // Target
        IFolder targetParentFolder = model.getFolder(FolderType.APPLICATION);
        assertTrue(targetParentFolder.getElements().isEmpty());
        assertTrue(targetParentFolder.getFolders().isEmpty());
        
        // Set up a mock DropTargetEvent for the DropOperation
        DropTargetEvent event = createMockDropTargetEvent(targetParentFolder);
        // Set the selection of elements we want to DnD
        LocalSelectionTransfer.getTransfer().setSelection(new StructuredSelection(new Object[] { childElement }));
        
        // And call the drop method
        dragHandler.doDropOperation(event);

        assertTrue(sourceParentFolder.getElements().isEmpty());
        assertTrue(targetParentFolder.getElements().contains(childElement));
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
    public void testGetTargetParent() {
        // A Folder is ok
        Object target = IArchimateFactory.eINSTANCE.createFolder();
        DropTargetEvent event = createMockDropTargetEvent(target);
        assertEquals(target, dragHandler.getTargetParent(event));
        
        // Anything else is not OK
        target = IArchimateFactory.eINSTANCE.createBusinessActor();
        event = createMockDropTargetEvent(target);
        assertNull(dragHandler.getTargetParent(event));
        
        // Simulate dropping on blank area of tree
        event.item = null;
        assertNull(dragHandler.getTargetParent(event));
    }
    
    @Test
    public void testIsValidDropTarget() {
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        IArchimateElement childElement = IArchimateFactory.eINSTANCE.createBusinessActor();
        folder.getElements().add(childElement);
        
        LocalSelectionTransfer.getTransfer().setSelection(new StructuredSelection(new Object[] { childElement }));

        // Can only drop onto a folder
        DropTargetEvent event = createMockDropTargetEvent(folder);
        assertTrue(dragHandler.isValidDropTarget(event));
        
        // And not something else
        event = createMockDropTargetEvent(childElement);
        assertFalse(dragHandler.isValidDropTarget(event));
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
        
        // Cannot drop onto itself
        DropTargetEvent event = createMockDropTargetEvent(e1);
        assertFalse(dragHandler.canDropObject(e1, (TreeItem)event.item));
        
        // OK
        assertTrue(dragHandler.canDropObject(e2, (TreeItem)event.item));

        // Can't test "If moving a folder check that target folder is not a descendant of the source folder"
        // Because parent tree items are created by the system
    }
    
    
    // ====================================================================================================
    
    /**
     * Create a mock DropTargetEvent
     */
    private DropTargetEvent createMockDropTargetEvent(Object target) {
        DropTargetEvent event = mock(DropTargetEvent.class);
        
        // Set event type to LocalSelectionTransfer type
        event.currentDataType = LocalSelectionTransfer.getTransfer().getSupportedTypes()[0];
        
        // Set item to a mock tree item
        event.item = mock(TreeItem.class);
        
        // The mock item should return the target
        when(event.item.getData()).thenReturn(target);

        return event;
    }
    

}
