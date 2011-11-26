/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.model.commands.NonNotifyingCompoundCommand;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;
import uk.ac.bolton.archimate.editor.views.tree.commands.MoveFolderCommand;
import uk.ac.bolton.archimate.editor.views.tree.commands.MoveObjectCommand;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;



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
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK; 

    /**
     * Drop operations we support on the tree
     */
    private int fDropOperations = DND.DROP_MOVE; 

    /**
     * Flag to mark valid tree selection
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
            
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
                fIsValidTreeSelection = false; // Reset to default
            }

            public void dragSetData(DragSourceEvent event) {
                // For consistency set the data to the selection even though
                // the selection is provided by the LocalSelectionTransfer
                // to the drop target adapter.
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            public void dragStart(DragSourceEvent event) {
                // Drag started from the Tree
                IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection();
                setIsValidTreeSelection(selection);

                LocalSelectionTransfer.getTransfer().setSelection(selection);
                event.doit = true;
            }
        });
    }
    
    private void registerDropSupport() {
        fViewer.addDropSupport(fDropOperations, targetTransferTypes, new DropTargetListener() {
            public void dragEnter(DropTargetEvent event) {
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
                event.detail = getEventDetail(event);
            }

            public void dragOver(DropTargetEvent event) {
                event.detail = getEventDetail(event);
                
                if(event.detail == DND.DROP_NONE) {
                    event.feedback = DND.FEEDBACK_NONE;
                }
                
                event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
            }

            public void drop(DropTargetEvent event) {
                doDropOperation(event);
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
            private int getEventDetail(DropTargetEvent event) {
                return isValidSelection(event) && isValidDropTarget(event) ? DND.DROP_MOVE : DND.DROP_NONE;
            }
            
        });
    }
    
    /**
     * Set whether we have a valid selection of objects dragged from the Tree
     * Do it at the start of the drag operation.
     */
    private void setIsValidTreeSelection(IStructuredSelection selection) {
        fIsValidTreeSelection = true;
        
        IArchimateModel model = null;
        
        for(Object object : selection.toArray()) {
            // Can't drag Models
            if(object instanceof IArchimateModel) {
                fIsValidTreeSelection = false;
                break;
            }
            // Can only drag user folders
            if(object instanceof IFolder && ((IFolder)object).getType() != FolderType.USER) {
                fIsValidTreeSelection = false;
                break;
            }
            // Don't allow mixed parent models
            if(object instanceof IArchimateModelElement) {
                IArchimateModel m = ((IArchimateModelElement)object).getArchimateModel();
                if(model != null && m != model) {
                    fIsValidTreeSelection = false;
                    break;
                }
                model = m;
            }
        }
    }
    
    private boolean isValidSelection(DropTargetEvent event) {
        return fIsValidTreeSelection || isValidFileSelection(event);
    }

    private boolean isValidFileSelection(DropTargetEvent event) {
        return isFileDragOperation(event.currentDataType);
    }

    private void doDropOperation(DropTargetEvent event) {
        //boolean move = event.detail == DND.DROP_MOVE;
        
        // Local
        if(isLocalTreeDragOperation(event.currentDataType)) {
            Object parent = getTargetParent(event);
            if(parent instanceof IFolder) {
                moveTreeObjects((IFolder)parent);
            }
        }
        // File
        else if(isFileDragOperation(event.currentDataType)) {
            addFileObjects((String[])event.data);
        }
    }
    
    private void addFileObjects(final String[] paths) {
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                for(String path : paths) {
                    File file = new File(path);
                    // Archi
                    if(file.getName().toLowerCase().endsWith(IEditorModelManager.ARCHIMATE_FILE_EXTENSION)
                            && !IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                        IEditorModelManager.INSTANCE.openModel(file);
                    }
                }
            }
        });
    }
    
    /**
     * Move Tree Objects
     */
    private void moveTreeObjects(IFolder newParent) {
        final CompoundCommand compoundCommand = new NonNotifyingCompoundCommand() {
            @Override
            public String getLabel() {
                return getCommands().size() > 1 ? "Move Elements" : super.getLabel();
            }
        };
        
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toArray()) {
            if(object instanceof IFolder && !newParent.getFolders().contains(object)) {
                compoundCommand.add(new MoveFolderCommand(newParent, (IFolder)object));
            }
            else if(object instanceof INameable && !newParent.getElements().contains(object)) {
                compoundCommand.add(new MoveObjectCommand(newParent, (INameable)object));
            }
        }
        
        final CommandStack stack = (CommandStack)newParent.getAdapter(CommandStack.class);
        
        /*
         * Bug in Mac Carbon introduced in Eclipse 3.6.1 - http://bugs.eclipse.org/bugs/show_bug.cgi?id=341895
         * Refreshing a tree after DND causes NPE unless we put this on a thread
         */
        if(PlatformUtils.isMacCarbon()) {
            Display.getCurrent().asyncExec(new Runnable() {
                @Override
                public void run() {
                    stack.execute(compoundCommand);
                }
            });
        }
        else {
            stack.execute(compoundCommand);
        }
    }
    
    /**
     * Determine the target parent from the drop event
     * 
     * @param event
     * @return
     */
    private Object getTargetParent(DropTargetEvent event) {
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
    private boolean isValidDropTarget(DropTargetEvent event) {
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
                if(!hasCommonParentFolder(targetfolder, (EObject)object)) {
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
     * @param folder
     * @param object
     * @return
     */
    private boolean hasCommonParentFolder(IFolder targetfolder, EObject object) {
        EObject f1 = targetfolder;
        while(!(f1.eContainer() instanceof IArchimateModel)) {
            f1 = f1.eContainer();
        }
        
        EObject f2 = object.eContainer();
        while(f2 != null && !(f2.eContainer() instanceof IArchimateModel)) {
            f2 = f2.eContainer();
        }
        
        return f1 == f2;
    }

    /**
     * @param object
     * @param targetTreeItem
     * @return
     */
    private boolean canDropObject(Object object, TreeItem targetTreeItem) {
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

    private boolean isLocalTreeDragOperation(TransferData dataType) {
        return LocalSelectionTransfer.getTransfer().isSupportedType(dataType);
    }
    
    private boolean isFileDragOperation(TransferData dataType) {
        return FileTransfer.getInstance().isSupportedType(dataType);
    }
}
