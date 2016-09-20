/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

/**
 * SelectAllAction including connections
 * 
 * @author Phillip Beauvoir
 */
public class SelectAllAction extends Action {

    private IWorkbenchPart part;

    /**
     * Constructs a <code>SelectAllAction</code> and associates it with the
     * given part.
     * 
     * @param part The workbench part associated with this SelectAllAction
     */
    public SelectAllAction(IWorkbenchPart part) {
        this.part = part;
        setText(Messages.SelectAllAction_0);
        setToolTipText(Messages.SelectAllAction_1);
        setId(ActionFactory.SELECT_ALL.getId());
    }

    /**
     * Selects all edit parts and connections in the active workbench part.
     */
    @Override
    public void run() {
        GraphicalViewer viewer = part.getAdapter(GraphicalViewer.class);
        if(viewer != null) {
            viewer.setSelection(new StructuredSelection(getSelectableEditParts(viewer.getContents()).toArray()));
        }
    }

    /**
     * Retrieves edit parts which can be selected and their connections
     * 
     * @param editpart from which the edit parts are to be retrieved
     * @return list of selectable EditParts
     */
    Set<GraphicalEditPart> getSelectableEditParts(EditPart editpart) {
        // Use a HashSet for uniqueness
        Set<GraphicalEditPart> selected = new HashSet<GraphicalEditPart>();
        
        for(Object child : editpart.getChildren()) {
            if(child instanceof GraphicalEditPart) {
                GraphicalEditPart childPart = (GraphicalEditPart)child;
                
                if(childPart.isSelectable()) {
                    selected.add(childPart);
                    
                    // Add connections if selectable
                    for(Object o : childPart.getSourceConnections()) {
                        GraphicalEditPart connectionEditPart = (GraphicalEditPart)o;
                        if(connectionEditPart.isSelectable()) {
                            selected.add(connectionEditPart);
                        }
                    }
                    for(Object o : childPart.getTargetConnections()) {
                        GraphicalEditPart connectionEditPart = (GraphicalEditPart)o;
                        if(connectionEditPart.isSelectable()) {
                            selected.add(connectionEditPart);
                        }
                    }
                    
                    // Recurse into children
                    selected.addAll(getSelectableEditParts(childPart));
                }
            }
        }
        
        return selected;
    }
}
