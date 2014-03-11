/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.commands.DeleteDiagramModelCommand;
import com.archimatetool.editor.model.commands.DeleteElementCommand;
import com.archimatetool.editor.model.commands.DeleteFolderCommand;
import com.archimatetool.editor.views.tree.TreeModelViewer;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Handles Delete Commands for the Tree Model View. <br/>
 * This is intended to manage element/folder deletions specifically for the Models Tree. <br/>
 * <br/>
 * - Will select a parent node in the tree after the deletion <br/>
 * - Manages deletions from more than one model - each model will have a separate command stack <br/>
 * - Deletes associated relationships <br/>
 * - Deletes associate diagram objects <br/>
 * 
 * @author Phillip Beauvoir
 */
public class DeleteCommandHandler {
    
    /*
     * If deleting elements from more than one model in the tree we need to use the
     * Command Stack allocated to each model. And then allocate one CompoundCommand per Command Stack.
     */
    private Hashtable<CommandStack, CompoundCommand> fCommandMap = new Hashtable<CommandStack, CompoundCommand>();
    
    // Treeviewer
    private TreeModelViewer fViewer;
    
    // Selected objects in Tree
    private Object[] fSelectedObjects = new Object[1]; // default value
    
    // Top level objects to delete
    private List<Object> fElementsToDelete;
    
    // Elements to check including children of top elements to delete
    private List<Object> fElementsToCheck;
    
    // The object to select in the tree after the deletion
    private Object fObjectToSelectAfterDeletion;
    
    /**
     * @param element
     * @return True if we can delete this object
     */
    public static boolean canDelete(Object element) {
        // Elements and Diagrams
        if(element instanceof IArchimateElement || element instanceof IDiagramModel) {
            return true;
        }
        
        // Certain Folders
        if(element instanceof IFolder) {
            IFolder folder = (IFolder)element;
            if(folder.getType().equals(FolderType.DERIVED) || folder.getType().equals(FolderType.USER)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Default constructor
     */
    public DeleteCommandHandler() {
    }

    public DeleteCommandHandler(TreeModelViewer viewer, Object[] objects) {
        fViewer = viewer;
        setObjectsToBeDeleted(objects);
    }
    
    /**
     * Set the objects to be deleted
     * @param objects The, er, objects to be deleted
     */
    public void setObjectsToBeDeleted(Object[] objects) {
        fSelectedObjects = objects;
    }
    
    /**
     * @return True if any of the objects to be deleted are referenced in a diagram
     */
    public boolean hasDiagramReferences() {
        for(Object object : fSelectedObjects) {
            boolean result = hasDiagramReferences(object);
            if(result) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @return True if object has references in a diagram model
     */
    private boolean hasDiagramReferences(Object object) {
        if(object instanceof IFolder) {
            for(EObject element : ((IFolder)object).getElements()) {
                boolean result = hasDiagramReferences(element);
                if(result) {
                    return true;
                }
            }
            for(IFolder f : ((IFolder)object).getFolders()) {
                boolean result = hasDiagramReferences(f);
                if(result) {
                    return true;
                }
            }
        }
        
        else if(object instanceof IArchimateElement) {
            return DiagramModelUtils.isElementReferencedInDiagrams((IArchimateElement)object);
        }
        
        return false;
    }
    
    /**
     * Delete the objects.
     * Once this occurs this DeleteCommandHandler is disposed.
     */
    public void delete() {
        // Find the object to select after the deletion
        fObjectToSelectAfterDeletion = findObjectToSelectAfterDeletion();
        
        // Gather the elements to delete
        getElementsToDelete();
        
        // Create the Commands
        createCommands();
        
        // Execute the Commands on the CommandStack(s) - there could be more than one if more than one model open in the Tree
        for(Entry<CommandStack, CompoundCommand> entry : fCommandMap.entrySet()) {
            entry.getKey().execute(entry.getValue());
        }
        
        dispose();
    }
    
    /**
     * Create the Delete Commands
     */
    private void createCommands() {
        /*
         *  We need to ensure that the Delete Diagram Model Commands are called first in order to close
         *  any open diagram editors before removing their models from parent folders.
         */
        for(Object object : fElementsToDelete) {
            if(object instanceof IDiagramModel) {
                CompoundCommand compoundCommand = getCompoundCommand((IAdapter)object);
                if(compoundCommand != null) {
                    Command cmd = new DeleteDiagramModelCommand((IDiagramModel)object);
                    compoundCommand.add(cmd);
                }
                else {
                    System.err.println("Could not get CompoundCommand in " + getClass()); //$NON-NLS-1$
                }
            }
        }
        
        /*
         * Then the other types
         */
        for(Object object : fElementsToDelete) {
            if(object instanceof IDiagramModel) { // already done
                continue;
            }
            
            CompoundCommand compoundCommand = getCompoundCommand((IAdapter)object);
            if(compoundCommand == null) { // sanity check
                System.err.println("Could not get CompoundCommand in " + getClass()); //$NON-NLS-1$
                continue;
            }

            if(object instanceof IFolder) {
                Command cmd = new DeleteFolderCommand((IFolder)object);
                compoundCommand.add(cmd);
            }
            else if(object instanceof IArchimateElement) {
                Command cmd = new DeleteElementCommand((IArchimateElement)object);
                compoundCommand.add(cmd);
            }
            else if(object instanceof IDiagramModelObject) {
                Command cmd = DiagramCommandFactory.createDeleteDiagramObjectCommand((IDiagramModelObject)object);
                compoundCommand.add(cmd);
            }
            else if(object instanceof IDiagramModelConnection) {
                Command cmd = DiagramCommandFactory.createDeleteDiagramConnectionCommand((IDiagramModelConnection)object);
                compoundCommand.add(cmd);
            }
        }
    }
    
    /**
     * Create the list of objects to delete and check
     * @return
     */
    private void getElementsToDelete() {
        // Actual elements to delete
        fElementsToDelete = new ArrayList<Object>();
        
        // Elements to check against for diagram references and other uses
        fElementsToCheck = new ArrayList<Object>();
        
        // First, gather up the list of Archimate objects to be deleted...
        for(Object object : fSelectedObjects) {
            if(canDelete(object)) {
                addToList(object, fElementsToDelete);
                addFolderChildElements(object);
                addElementRelationships(object);
            }
        }
        
        // Gather referenced diagram objects to be deleted checking that the parent diagram model is not also selected to be deleted
        for(Object object : fElementsToCheck) {
            // Archimate Elements
            if(object instanceof IArchimateElement) {
                IArchimateElement element = (IArchimateElement)object;
                for(IDiagramModel diagramModel : element.getArchimateModel().getDiagramModels()) {
                    // Check diagram model is not selected to be deleted - no point in deleting any of its children
                    if(!fElementsToDelete.contains(diagramModel)) {
                        for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, element)) {
                            addToList(dc, fElementsToDelete);
                        }
                    }
                }
            }
            
            // Diagram Models and their references
            if(object instanceof IDiagramModel) {
                IDiagramModel diagramModelDeleted = (IDiagramModel)object;
                for(IDiagramModel diagramModel : diagramModelDeleted.getArchimateModel().getDiagramModels()) {
                    List<IDiagramModelReference> list = DiagramModelUtils.findDiagramModelReferences(diagramModel, diagramModelDeleted); // is there one?
                    fElementsToDelete.addAll(list);
                }
            }
        }
    }
    
    /**
     * Gather elements in folders that need checking for referenced diagram objects and other checks
     */
    private void addFolderChildElements(Object object) {
        // Folder
        if(object instanceof IFolder) {
            for(EObject element : ((IFolder)object).getElements()) {
                addFolderChildElements(element);
            }

            // Child folders
            for(IFolder f : ((IFolderContainer)object).getFolders()) {
                addFolderChildElements(f);
            }
        }
        else {
            // Add to check list
            addToList(object, fElementsToCheck);
            // Diagram models need to be deleted explicitly with their own command in case they need closing in the editor
            if(object instanceof IDiagramModel) {
                addToList(object, fElementsToDelete);
            }
        }
    }
    
    /**
     * Add any connected relationships for an Element
     */
    private void addElementRelationships(Object object) {
        // Folder
        if(object instanceof IFolder) {
            for(EObject element : ((IFolder)object).getElements()) {
                addElementRelationships(element);
            }

            // Child folders
            for(IFolder f : ((IFolderContainer)object).getFolders()) {
                addElementRelationships(f);
            }
        }
        // Element
        else if(object instanceof IArchimateElement && !(object instanceof IRelationship)) {
            for(IRelationship relationship : ArchimateModelUtils.getRelationships((IArchimateElement)object)) {
                addToList(relationship, fElementsToDelete);
                addToList(relationship, fElementsToCheck);
            }
        }
    }
    
    /**
     * Add object to list if not already in list
     */
    private void addToList(Object object, List<Object> list) {
        if(object != null && !list.contains(object)) {
            list.add(object);
        }
    }
    
    /**
     * Get, and if need be create, a CompoundCommand to which to add the object to be deleted command
     */
    private CompoundCommand getCompoundCommand(IAdapter object) {
        // Get the Command Stack registered to the object
        CommandStack stack = (CommandStack)object.getAdapter(CommandStack.class);
        if(stack == null) {
            System.err.println("CommandStack was null in " + getClass()); //$NON-NLS-1$
            return null;
        }
        
        // Now get or create a Compound Command
        CompoundCommand compoundCommand = fCommandMap.get(stack);
        if(compoundCommand == null) {
            compoundCommand = new DeleteElementsCompoundCommand(fObjectToSelectAfterDeletion);
            fCommandMap.put(stack, compoundCommand);
        }
        
        return compoundCommand;
    }
    
    /**
     * Find the best object to select after the deletion
     */
    private Object findObjectToSelectAfterDeletion() {
        Object firstObject = fSelectedObjects[0];
        
        TreeItem item = fViewer.findTreeItem(firstObject);
        if(item == null) {
            return null;
        }
        
        TreeItem parentTreeItem = item.getParentItem();
        
        // Parent Item not found so must be at top level
        if(parentTreeItem == null) {
            Tree tree = item.getParent();
            int index = tree.indexOf(item);
            if(index < 1) { // At root or not found
                return null;
            }
            return tree.getItem(index - 1).getData();
        }

        Object selected = null;
        
        // Item index is greater than 1 so select previous sibling item
        int index = parentTreeItem.indexOf(item);
        if(index > 1) {
            selected = parentTreeItem.getItem(index - 1).getData();
        }
        
        // Was null so select parent of first object
        if(selected == null && firstObject instanceof EObject) {
            selected = ((EObject)firstObject).eContainer();
        }
        
        return selected;
    }
    
    private void dispose() {
        fSelectedObjects = null;
        fElementsToDelete = null;
        fViewer = null;
        fCommandMap = null;
    }
}
