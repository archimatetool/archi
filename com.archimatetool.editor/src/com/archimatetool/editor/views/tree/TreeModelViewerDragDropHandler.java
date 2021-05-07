/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.TreeItem;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.views.tree.commands.MoveFolderCommand;
import com.archimatetool.editor.views.tree.commands.MoveObjectCommand;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IFolder;




/**
 * Model Tree Drag Drop Handler
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewerDragDropHandler {

    private StructuredViewer fViewer;
    
    /**
     * Drag operations we support
     */
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE;

    /**
     * Drop operations we support on the tree
     */
    private int fDropOperations = DND.DROP_MOVE;

    /**
     * Whether we have a valid tree selection
     */
    private boolean fIsValidTreeSelection;
    
    // Can only drag local type
    Transfer[] sourceTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    // Can drop local and file types
    Transfer[] targetTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance() };
    
    public TreeModelViewerDragDropHandler(StructuredViewer viewer) {
        fViewer = viewer;
        registerDragSupport();
        registerDropSupport();
    }
    
    /**
     * Register drag support that starts from the Tree
     */
    private void registerDragSupport() {
        fViewer.addDragSupport(fDragOperations, sourceTransferTypes, new DragSourceListener() {
            
            @Override
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
                fIsValidTreeSelection = false; // Reset to default
            }

            @Override
            public void dragSetData(DragSourceEvent event) {
                // For consistency set the data to the selection even though
                // the selection is provided by the LocalSelectionTransfer
                // to the drop target adapter.
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            @Override
            public void dragStart(DragSourceEvent event) {
                // Drag started from the Tree
                IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection();
                fIsValidTreeSelection = isValidTreeSelection(selection);

                LocalSelectionTransfer.getTransfer().setSelection(selection);
                event.doit = true;
            }
        });
    }
    
    private void registerDropSupport() {
        fViewer.addDropSupport(fDropOperations, targetTransferTypes, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetEvent event) {
            }

            @Override
            public void dragLeave(DropTargetEvent event) {
            }

            @Override
            public void dragOperationChanged(DropTargetEvent event) {
                event.detail = getEventDetail(event);
            }

            @Override
            public void dragOver(DropTargetEvent event) {
                event.detail = getEventDetail(event);
                
                if(event.detail == DND.DROP_NONE) {
                    event.feedback = DND.FEEDBACK_NONE;
                }
                
                event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
            }

            @Override
            public void drop(DropTargetEvent event) {
                doDropOperation(event);
            }

            @Override
            public void dropAccept(DropTargetEvent event) {
                // Need to check and veto this!!!!
                // User's mouse movements can still register DND in the background when
                // app is doing long running operation and create non-allowed drag operations!!!
                event.detail = getEventDetail(event);
            }
            
            private int getEventDetail(DropTargetEvent event) {
                return isValidSelection(event) && isValidDropTarget(event) ? DND.DROP_MOVE : DND.DROP_NONE;
            }
            
        });
    }
    
    /**
     * Determine whether we have a valid selection of objects dragged from the Tree
     * Do it at the start of the drag operation for optimal speed.
     */
    boolean isValidTreeSelection(IStructuredSelection selection) {
        if(selection == null || selection.isEmpty()) {
            return false;
        }
        
        IArchimateModel model = null;
        
        for(Object object : selection.toArray()) {
            // Can't drag Models
            if(object instanceof IArchimateModel) {
                return false;
            }
            // Can only drag user folders
            if(object instanceof IFolder && ((IFolder)object).getType() != FolderType.USER) {
                return false;
            }
            // Don't allow mixed parent models
            if(object instanceof IArchimateModelObject) {
                IArchimateModel m = ((IArchimateModelObject)object).getArchimateModel();
                if(model != null && m != model) {
                    return false;
                }
                model = m;
            }
        }
        
        return true;
    }
    
    boolean isValidSelection(DropTargetEvent event) {
        return fIsValidTreeSelection || isValidFileSelection(event);
    }

    boolean isValidFileSelection(DropTargetEvent event) {
        return isFileDragOperation(event.currentDataType);
    }

    void doDropOperation(DropTargetEvent event) {
        //boolean move = event.detail == DND.DROP_MOVE;
        
        // Local
        if(isLocalTreeDragOperation(event.currentDataType)) {
            Object parent = getTargetParent(event);
            if(parent instanceof IFolder) {
                IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
                moveTreeObjects((IFolder)parent, selection.toArray());
            }
        }
        // File
        else if(isFileDragOperation(event.currentDataType)) {
            addFileObjects((String[])event.data);
        }
    }
    
    /**
     * Add external file objects dragged from the desktop by opening each file as a model
     */
    private void addFileObjects(final String[] paths) {
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                for(String path : paths) {
                    File file = new File(path);
                    // Archimate model
                    if(file.getName().toLowerCase().endsWith(IEditorModelManager.ARCHIMATE_FILE_EXTENSION)) {
                        IEditorModelManager.INSTANCE.openModel(file);
                    }
                }
            }
        });
    }
    
    /**
     * Move Tree Objects
     */
    void moveTreeObjects(IFolder newParent, Object[] objects) {
        final CompoundCommand compoundCommand = new NonNotifyingCompoundCommand() {
            @Override
            public String getLabel() {
                return getCommands().size() > 1 ? Messages.TreeModelViewerDragDropHandler_0 : super.getLabel();
            }
        };
        
        for(Object object :objects) {
            if(object instanceof IFolder) { // This first - folders go in folders
                if(!newParent.getFolders().contains(object)) {
                    compoundCommand.add(new MoveFolderCommand(newParent, (IFolder)object));
                }
            }
            else if(object instanceof IArchimateModelObject) {
                if(!newParent.getElements().contains(object)) {
                    compoundCommand.add(new MoveObjectCommand(newParent, (IArchimateModelObject)object));                    
                }
            }
        }
        
        CommandStack stack = (CommandStack)newParent.getAdapter(CommandStack.class);
        stack.execute(compoundCommand);
    }
    
    /**
     * Determine the target parent from the drop event
     * 
     * @param event
     * @return
     */
    Object getTargetParent(DropTargetEvent event) {
        // Dropped on blank area, null
        if(event.item == null) {
            return null;
        }
        
        Object objectDroppedOn = event.item.getData();
        
        // Folder
        if(objectDroppedOn instanceof IFolder) {
            return objectDroppedOn;
        }
        
        // Otherwise null
        return null;
    }

    /**
     * @return True if target is valid
     */
    boolean isValidDropTarget(DropTargetEvent event) {
        // File from desktop onto blank area
        if(isFileDragOperation(event.currentDataType)) {
            return event.item == null;
        }

        // Local Tree Selection...
        
        // Dragging onto a Folder
        Object parent = getTargetParent(event);
        if(parent instanceof IFolder) {
            IFolder targetfolder = (IFolder)parent;
            IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
            for(Object object : selection.toList()) {
                // must have the same top folder type - a restriction which one day we should not enforce!
                if(!hasCommonAncestorFolder(targetfolder, (EObject)object)) {
                    return false;
                }
                if(!canDropObject(object, (TreeItem)event.item)) {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if eObject1 and eObject2 have a common ancestor folder
     * i.e dragged objects can only share the same hole
     */
    boolean hasCommonAncestorFolder(EObject eObject1, EObject eObject2) {
        while(eObject1.eContainer() instanceof IFolder) {
            eObject1 = eObject1.eContainer();
        }

        while(eObject2.eContainer() instanceof IFolder) {
            eObject2 = eObject2.eContainer();
        }
        
        return (eObject1 == eObject2);
    }

    /**
     * Return true if object can be dropped on a target tree item
     */
    boolean canDropObject(Object object, TreeItem targetTreeItem) {
        if(targetTreeItem == null) {  // Root tree
            return false;
        }
        
        if(object == targetTreeItem.getData()) {  // Cannot drop onto itself
            return false;
        }
        
        // If moving a folder check that target folder is not a descendant of the source folder
        if(object instanceof IFolder) {
            while((targetTreeItem = targetTreeItem.getParentItem()) != null) {
                if(targetTreeItem.getData() == object) {
                    return false;
                }
            }
        }
        
        return true;
    }

    boolean isLocalTreeDragOperation(TransferData dataType) {
        return LocalSelectionTransfer.getTransfer().isSupportedType(dataType);
    }
    
    boolean isFileDragOperation(TransferData dataType) {
        return FileTransfer.getInstance().isSupportedType(dataType);
    }
}
