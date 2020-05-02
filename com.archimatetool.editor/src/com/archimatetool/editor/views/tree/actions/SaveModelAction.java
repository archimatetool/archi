/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.model.IArchimateModel;



/**
 * Save Model Action
 * 
 * @author Phillip Beauvoir
 */
public class SaveModelAction extends ViewerAction {
    
    private ITreeModelView fView;
    
    public SaveModelAction(ITreeModelView view) {
        super(view.getSelectionProvider());
        setText(Messages.SaveModelAction_0);
        
        fView = view;
        
        // Ensures key binding is displayed
        setActionDefinitionId(IWorkbenchCommandConstants.FILE_SAVE);
    }
    
    @Override
    public void run() {
        // Get selected Model and save it and any Diagrams via EditorModel Manager
        IArchimateModel model = getModel();
        if(model != null) {
            try {
                IEditorModelManager.INSTANCE.saveModel(model);
            }
            catch(IOException ex) {
                MessageDialog.openError(fView.getSite().getShell(), Messages.SaveModelAction_1, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void update() {
        setEnabled(IEditorModelManager.INSTANCE.isModelDirty(getModel()));
    }

    private IArchimateModel getModel() {
        return fView.getAdapter(IArchimateModel.class);
    }
}
