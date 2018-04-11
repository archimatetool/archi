/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.viewers.ISelectionProvider;

import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IDiagramModel;



/**
 * Open Diagram Action
 * 
 * @author Phillip Beauvoir
 */
public class OpenDiagramAction extends ViewerAction {
    
    public OpenDiagramAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.OpenDiagramAction_0);
        setEnabled(false);
    }
    
    @Override
    public void run() {
        for(Object selected : getSelection().toArray()) {
            // Diagram - open diagram
            if(selected instanceof IDiagramModel) {
                EditorManager.openDiagramEditor((IDiagramModel)selected);
            }
        }
    }

    @Override
    public void update() {
        Object selected = getSelection().getFirstElement();
        setEnabled(selected instanceof IDiagramModel);
    }

}
