/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.io.IOException;

import org.eclipse.jface.viewers.ISelectionProvider;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;



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
    public void update() {
        Object selected = getSelection().getFirstElement();
        setEnabled(selected instanceof IArchimateModel);
    }

}
