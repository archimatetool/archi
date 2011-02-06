/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IWorkbenchPart;

import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Bring Forward Action
 * 
 * @author Phillip Beauvoir
 */
public class BringForwardAction extends SelectionAction {
    
    public static final String ID = "BringForwardAction";
    public static final String TEXT = "Bring Forward";
    
    public BringForwardAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        
        /*
         * Set the selection provider to the viewer and not the global selection provider so that
         * viewer and selection concur.
         * See SelectionAction#update()
         */
        setSelectionProvider((ISelectionProvider)part.getAdapter(GraphicalViewer.class));
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> selected = getSelectedObjects();
        
        // Quick checks
        if(selected.isEmpty()) {
            return false;
        }
        
        for(Object object : selected) {
            if(!(object instanceof EditPart)) {
                return false;
            }
        }

        Command command = createCommand(selected);
        if(command == null) {
            return false;
        }
        return command.canExecute();
    }

    @Override
    public void run() {
        execute(createCommand(getSelectedObjects()));
    }
    
    private Command createCommand(List<?> selection) {
        GraphicalViewer viewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
        
        CompoundCommand result = new CompoundCommand("Bring Forward");
        
        /*
         * We have to work with the bounds of the actual Figures not the IDiagramModelObject
         * because the IDiagramModelObject can have -1 as a width and height 
         */
        
        for(Object object : selection) {
            if(object instanceof GraphicalEditPart) {
                GraphicalEditPart editPart = (GraphicalEditPart)object;
                Object model = editPart.getModel();
                
                // This can happen if we do things wrong
                if(viewer != editPart.getViewer()) {
                    System.err.println("Wrong selection for viewer in " + getClass());
                }
                
                if(model instanceof IDiagramModelObject) {
                    IDiagramModelObject diagramObject = (IDiagramModelObject)model;
                    IDiagramModelContainer parent = (IDiagramModelContainer)diagramObject.eContainer();
                    
                    /*
                     * Parent can be null when objects are selected (with marquee tool) and transferred from one container
                     * to another and the Diagram Editor updates the enablement state of Actions.
                     */
                    if(parent == null) {
                        continue;
                    }
                    
                    // Get siblings
                    EList<IDiagramModelObject> modelChildren = parent.getChildren();

                    Rectangle bounds = editPart.getFigure().getBounds();

                    int originalPos = modelChildren.indexOf(diagramObject);

                    // If at top don't bother
                    if(originalPos == modelChildren.size() - 1) {
                        continue;
                    }

                    int newPos = originalPos;

                    for(IDiagramModelObject child : modelChildren) {
                        if(child == diagramObject) {
                            continue; // same one
                        }

                        // Check for intersections with other sibling editparts
                        GraphicalEditPart editPartSibling = (GraphicalEditPart)viewer.getEditPartRegistry().get(child);
                        if(editPartSibling != null) { // this can happen
                            Rectangle boundsSibling = editPartSibling.getFigure().getBounds();
                            if(bounds.touches(boundsSibling)) {
                                int pos = modelChildren.indexOf(child);
                                if(pos > newPos) {
                                    newPos = pos;
                                }
                            }
                        }
                    }

                    // If we didn't intersect with any other objects bring it forward 1 position
                    if(newPos == originalPos && newPos < modelChildren.size()) {
                        newPos++;
                    }

                    if(newPos > originalPos) {
                        result.add(new BringForwardCommand(parent, newPos, originalPos));
                    }
                }
            }
        }

        return result.unwrap();
    }
    
    private static class BringForwardCommand extends Command {
        private IDiagramModelContainer fParent;
        private int fNewPos, fOldPos;
        
        public BringForwardCommand(IDiagramModelContainer parent, int newPos, int oldPos) {
            fParent = parent;
            fNewPos = newPos;
            fOldPos = oldPos;
            setLabel("Bring Forward");
        }

        @Override
        public boolean canExecute() {
            return fParent != null && fNewPos < fParent.getChildren().size();
        }
        
        @Override
        public void execute() {
            fParent.getChildren().move(fNewPos, fOldPos);
        }
        
        @Override
        public void undo() {
            fParent.getChildren().move(fOldPos, fNewPos);
        }
        
        @Override
        public void dispose() {
            fParent = null;
        }
    }
}
