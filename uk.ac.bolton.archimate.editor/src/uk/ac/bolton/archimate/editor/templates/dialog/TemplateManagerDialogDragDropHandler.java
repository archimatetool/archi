/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.templates.dialog;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;

import uk.ac.bolton.archimate.editor.templates.ITemplate;
import uk.ac.bolton.archimate.editor.templates.ITemplateGroup;



/**
 * Drag Drop Handler
 * 
 * @author Phillip Beauvoir
 */
public class TemplateManagerDialogDragDropHandler {

    private TreeViewer fTreeViewer;
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    // Can only drag local type
    Transfer[] sourceTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    public TemplateManagerDialogDragDropHandler(TableViewer tableViewer, TreeViewer treeViewer) {
        fTreeViewer = treeViewer;
        registerDragSupport(tableViewer);
        registerDropSupport(treeViewer);
    }
    
    /**
     * Register drag support that starts from the Table
     */
    private void registerDragSupport(final StructuredViewer viewer) {
        viewer.addDragSupport(fDragOperations, sourceTransferTypes, new DragSourceListener() {
            
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }

            public void dragSetData(DragSourceEvent event) {
            }

            public void dragStart(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(viewer.getSelection());
                event.doit = true;
            }
        });
    }
    
    /**
     * Register drop support that ends on the Tree
     */
    private void registerDropSupport(final StructuredViewer viewer) {
        viewer.addDropSupport(fDragOperations, sourceTransferTypes, new DropTargetListener() {
            int operations = DND.DROP_NONE;
            
            public void dragEnter(DropTargetEvent event) {
                operations = isValidSelection() ? event.detail : DND.DROP_NONE;
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
                operations = isValidSelection() ? event.detail : DND.DROP_NONE;
            }

            public void dragOver(DropTargetEvent event) {
                event.detail = isValidDropTarget(event) ? operations : DND.DROP_NONE;
                
                if(operations == DND.DROP_NONE) {
                    event.feedback = DND.FEEDBACK_NONE;
                }
                else {
                    event.feedback = getFeedbackType(event);
                    event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
                }
            }

            public void drop(DropTargetEvent event) {
                if((event.detail == DND.DROP_COPY)) {
                    doDropOperation(event);
                }
                else if((event.detail == DND.DROP_MOVE)) {
                    doDropOperation(event);
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }
    
    private void doDropOperation(DropTargetEvent event) {
        //boolean move = event.detail == DND.DROP_MOVE;
        
        Object parent = getTargetParent(event);
        if(parent instanceof ITemplateGroup) {
            copy((ITemplateGroup)parent);
        }
    }
    
    private void copy(ITemplateGroup parent) {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        
        // From Table to Tree
        for(Object o : selection.toArray()) {
            if(o instanceof ITemplate) {
                if(!parent.getTemplates().contains(o)) {
                    parent.addTemplate((ITemplate)o);
                }
            }
        }
        fTreeViewer.refresh();
    }

    private boolean isValidSelection() {
        return true;
    }
    
    /**
     * Determine the feedback type for dropping
     */
    private int getFeedbackType(DropTargetEvent event) {
        if(event.item == null) {
            return DND.FEEDBACK_NONE;
        }
        
        return DND.FEEDBACK_SELECT;
    }
    
    /**
     * Determine the target parent from the drop event
     */
    private Object getTargetParent(DropTargetEvent event) {
        // Dropped on blank area, null
        if(event.item == null) {
            return null;
        }
        
        Object objectDroppedOn = event.item.getData();
        
        // Group
        if(objectDroppedOn instanceof ITemplateGroup) {
            return objectDroppedOn;
        }
        
        // Otherwise null
        return null;
    }

    /**
     * @return True if target is valid
     */
    private boolean isValidDropTarget(DropTargetEvent event) {
        Object parent = getTargetParent(event);
        if(parent instanceof ITemplateGroup) {
            ITemplateGroup targetGroup = (ITemplateGroup)parent;
            IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
            for(Object object : selection.toList()) {
                if(targetGroup.getTemplates().contains(object)) {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
}
