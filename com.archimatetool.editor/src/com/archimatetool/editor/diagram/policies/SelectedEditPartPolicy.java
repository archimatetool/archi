/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

import com.archimatetool.editor.diagram.figures.ISelectableFigure;


/**
 * 
 * Handles selection of EditParts.
 * The selection state is forwarded on to the figure if it implements ISelectableFigure
 * The default is to forwward on all selected edit parts unless onlyPrimarySelection is true
 * 
 * @author Phillip Beauvoir
 */
public class SelectedEditPartPolicy extends SelectionEditPolicy {
    
    private boolean onlyPrimarySelection;
    
    public SelectedEditPartPolicy() {
    }

    /**
     * @param onlyPrimarySelection If true only the primary selection is selected
     */
    public SelectedEditPartPolicy(boolean onlyPrimarySelection) {
        this.onlyPrimarySelection = onlyPrimarySelection;
    }
    
    @Override
    protected void hideSelection() {
        setSelected(false);
    }

    @Override
    protected void showSelection() {
        setSelected(!onlyPrimarySelection);
    }
    
    @Override
    protected void showPrimarySelection() {
        setSelected(true);
    }
    
    protected void setSelected(boolean selected) {
        if(((GraphicalEditPart)getHost()).getFigure() instanceof ISelectableFigure figure) {
            figure.setSelected(selected);
        }
    }
}
