/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;


/**
 * Global Save Action.<br>
 * We use our own Save Action and not Eclipse's for a number of reasons:<p>
 * 
 * We control the Asterisks.<br>
 * We control the Save dialog.<br>
 * We control the state.<p>
 * 
 * The Tree Model View can have more than one model open and therefore is "dirty" depending on the Selection
 * in the Tree, not for the whole Eclipse View.<p>
 * 
 * We also have more than one Eclipse Editor open per model.<p>
 * 
 * We don't use a RetargetAction because we want the enabled state to persist across Workbench Part selections.
 * 
 * @author Phillip Beauvoir
 */
public class SaveAction extends AbstractModelSelectionAction {
    
    /*
     * Listen to Command Stack changes to update state
     */
    private PropertyChangeListener commandStackListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if(propertyName == IEditorModelManager.COMMAND_STACK_CHANGED) {
                updateState();
            }
        }
    };
    
    public SaveAction(IWorkbenchWindow window) {
        super(Messages.SaveAction_0, window);
        
        setActionDefinitionId(IWorkbenchCommandConstants.FILE_SAVE);
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT_DISABLED));
        
        IEditorModelManager.INSTANCE.addPropertyChangeListener(commandStackListener);
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                IEditorModelManager.INSTANCE.saveModel(model);
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), Messages.SaveAction_1, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void updateState() {
        IArchimateModel model = getActiveArchimateModel();
        setEnabled(IEditorModelManager.INSTANCE.isModelDirty(model));
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(workbenchWindow != null) {
            Shell shell = workbenchWindow.getShell();
            if(shell != null && !shell.isDisposed()) {
                shell.setModified(enabled);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        IEditorModelManager.INSTANCE.removePropertyChangeListener(commandStackListener);
    }
}