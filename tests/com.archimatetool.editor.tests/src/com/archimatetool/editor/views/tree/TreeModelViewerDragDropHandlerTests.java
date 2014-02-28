/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IFolder;
import com.archimatetool.tests.ArchimateTestModel;
import com.archimatetool.tests.TestUtils;


/**
 * TreeModelViewerDragDropHandlerTests
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TreeModelViewerDragDropHandlerTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private TreeModelViewerDragDropHandler dragHandler;

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TreeModelViewerDragDropHandlerTests.class);
    }
    
    @Before
    public void runBeforeEachTest() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel();
        dragHandler = new TreeModelViewerDragDropHandler(new TreeViewer(new Shell(), 0));
    }
    
    /**
     * The famous DnD bug of drag and dropping a folder onto the same parent (fixed in Archi 2.4.1)
     */
    @Test
    public void moveTreeObjects_Folder_DNDBugDragFolder1() throws Exception {
        // Add a child folder to a top-level folder
        IFolder childFolder =  IArchimateFactory.eINSTANCE.createFolder();
        IFolder parentFolder = model.getFolder(FolderType.BUSINESS);
        parentFolder.getFolders().add(childFolder);
        
        assertEquals("Wrong child size", 1, parentFolder.getFolders().size());
        assertTrue(parentFolder.getFolders().contains(childFolder));

        // Simulate Move child folder to the same parent folder
        TestUtils.invokePrivateMethod(dragHandler, "moveTreeObjects",
                new Class[] { IFolder.class, Object[].class }, new Object[] { parentFolder, new Object[] { childFolder } });
        
        assertEquals("Wrong child size", 1, parentFolder.getFolders().size());
        assertTrue(parentFolder.getFolders().contains(childFolder));
        assertEquals("Wrong child size", 0, parentFolder.getElements().size());
    }
    
    /**
     * Drag and dropping a folder onto a different parent
     */
    @Test
    public void moveTreeObjects_Folder_DNDBugDragFolder2() throws Exception {
        // Add a child folder to a top-level folder
        IFolder childFolder =  IArchimateFactory.eINSTANCE.createFolder();
        IFolder oldParent = model.getFolder(FolderType.BUSINESS);
        oldParent.getFolders().add(childFolder);
        
        assertEquals("Wrong child size", 1, oldParent.getFolders().size());
        assertTrue(oldParent.getFolders().contains(childFolder));

        // New target parent folder
        IFolder newParent = model.getFolder(FolderType.APPLICATION);
        
        // Simulate Move child folder to the different target parent folder
        TestUtils.invokePrivateMethod(dragHandler, "moveTreeObjects",
                new Class[] { IFolder.class, Object[].class }, new Object[] { newParent, new Object[] { childFolder } });
        
        assertEquals("Wrong child size", 0, oldParent.getFolders().size());
        assertEquals("Wrong child size", 1, newParent.getFolders().size());
        assertEquals("Wrong child size", 0, newParent.getElements().size());
        assertEquals("Wrong folder contents", childFolder, newParent.getFolders().get(0));
    }

    /**
     * Drag and dropping elements onto same parent
     */
    @Test
    public void moveTreeObjects_Folder_Elements() throws Exception {
        // Add a child folder to a top-level folder
        IFolder parentFolder = model.getFolder(FolderType.BUSINESS);
        IBusinessActor childElement = IArchimateFactory.eINSTANCE.createBusinessActor();
        parentFolder.getElements().add(childElement);
        
        assertEquals("Wrong child size", 1, parentFolder.getElements().size());
        assertTrue(parentFolder.getElements().contains(childElement));

        // Simulate Move child element to the same parent folder
        TestUtils.invokePrivateMethod(dragHandler, "moveTreeObjects",
                new Class[] { IFolder.class, Object[].class }, new Object[] { parentFolder, new Object[] { childElement } });
        
        assertEquals("Wrong child size", 1, parentFolder.getElements().size());
        assertEquals("Wrong folder contents", childElement, parentFolder.getElements().get(0));
    }    
}
