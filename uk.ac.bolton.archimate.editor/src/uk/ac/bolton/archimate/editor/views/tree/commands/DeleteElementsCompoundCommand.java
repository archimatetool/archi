/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.model.commands.NonNotifyingCompoundCommand;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;

/**
 * This Compound Command stores the state of elements so that 
 * when elements are deleted the tree nodes can be re-selected.
 * It turns off refresh in the Tree, saving on slow redraws.
 * 
 * @author Phillip Beauvoir
 */
public class DeleteElementsCompoundCommand extends NonNotifyingCompoundCommand {

    private List<?> fSelected;
    private Object fParentToSelect;

    public DeleteElementsCompoundCommand(List<?> selected) {
        fSelected = selected;

        // Find parent
        Object o = fSelected.get(0);
        if(o instanceof EObject) {
            fParentToSelect = ((EObject)o).eContainer();
        }
//          Object o = fSelected.get(0);
//          if(o instanceof IFolder) {
//              fParentToSelect = o;
//          }
//          else if(o instanceof EObject) {
//              fParentToSelect = ((EObject)o).eContainer();
//          }
    }

    @Override
    public String getLabel() {
        return getCommands().size() > 1 ? "Delete Elements" : super.getLabel();
    }

    @Override
    public void execute() {
        super.execute();

        // Select parent
        if(fParentToSelect != null) {
            IEditorModelManager.INSTANCE.firePropertyChange(this,
                    ITreeModelView.PROPERTY_SELECTION_CHANGED, null, fParentToSelect);
        }
    }

    @Override
    public void undo() {
        super.undo();

        // Select nodes
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                ITreeModelView.PROPERTY_SELECTION_CHANGED, null, fSelected);
    }

    @Override
    public void redo() { // redo() as called by CompoundCommand is *not* the same as execute()!
        super.redo();

        // Select parent
        if(fParentToSelect != null) {
            IEditorModelManager.INSTANCE.firePropertyChange(this,
                    ITreeModelView.PROPERTY_SELECTION_CHANGED, null, fParentToSelect);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        fSelected = null;
        fParentToSelect = null;
    }
}