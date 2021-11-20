/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateModel;


/**
 * Open Model Action
 * 
 * @author Phillip Beauvoir
 */
public class OpenModelAction
extends Action
implements IWorkbenchAction
{
    
    public OpenModelAction(IWorkbenchWindow window) {
        setText(Messages.OpenModelAction_0);
        setToolTipText(Messages.OpenModelAction_1);
        setId("com.archimatetool.editor.action.openModel"); //$NON-NLS-1$
        setActionDefinitionId(getId()); // register key binding
    }
    
    @Override
    public void run() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { IEditorModelManager.ARCHIMATE_FILE_WILDCARD, "*.xml", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        if(path != null) {
            final File file = new File(path);
            
            // Check it's not already open
            IArchimateModel model = getModel(file);
            if(model != null) {
                MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
                                                Messages.OpenModelAction_2,
                                                NLS.bind(Messages.OpenModelAction_3,
                                                        file.getName(), model.getName()));
                return;
            }
            
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                @Override
                public void run() {
                    IEditorModelManager.INSTANCE.openModel(file);
                }
            });
        }
    }
    
    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_OPEN);
    }
    
    /**
     * Get model if it is already open
     */
    private IArchimateModel getModel(File file) {
        if(file != null) {
            for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                if(file.equals(model.getFile())) {
                    return model;
                }
            }
        }
        
        return null;
    }

    @Override
    public void dispose() {
    } 
}