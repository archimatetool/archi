/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.actions;

import java.io.IOException;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * Close Model Action
 * 
 * @author Phillip Beauvoir
 */
public class CloseModelAction extends ViewerAction {
    
    public CloseModelAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.CloseModelAction_0);
        setEnabled(false);
    }
    
    @Override
    public void run() {
        for(Object selected : getSelection().toArray()) {
            if(selected instanceof IArchimateModel) {
                try {
                    boolean result = IEditorModelManager.INSTANCE.closeModel((IArchimateModel)selected);
                    if(!result) {
                        break;
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(IStructuredSelection selection) {
        Object selected = selection.getFirstElement();
        setEnabled(selected instanceof IArchimateModel);
    }

}
