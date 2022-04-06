/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;


/**
 * Global Save As Action
 * 
 * @author Phillip Beauvoir
 */
public class SaveAsAction extends AbstractModelAction {
    
    public SaveAsAction(IWorkbenchWindow window) {
        super(Messages.SaveAsAction_0, window);

        setActionDefinitionId(IWorkbenchCommandConstants.FILE_SAVE_AS);
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEAS_EDIT));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEAS_EDIT_DISABLED));
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                IEditorModelManager.INSTANCE.saveModelAs(model);
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), Messages.SaveAsAction_1, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}