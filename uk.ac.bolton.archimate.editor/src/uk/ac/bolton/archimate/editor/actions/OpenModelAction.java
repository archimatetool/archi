/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimateModel;

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
        setText("&Open...");
        setToolTipText("Open Model");
        setId("uk.ac.bolton.archimate.editor.action.openModel");
        setActionDefinitionId(getId()); // register key binding
    }
    
    @Override
    public void run() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { IEditorModelManager.ARCHIMATE_FILE_WILDCARD, "*.xml", "*.*" } );
        String path = dialog.open();
        if(path != null) {
            File file = new File(path);
            
            // Check it's not already open
            IArchimateModel model = getModel(file);
            if(model != null) {
                MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
                                                "Open Model",
                                                "File '" + file.getName() + "' " +
                                                "is already open." +
                                                " Its model name is '" + model.getName() + "'.");
                return;
            }
            
            IEditorModelManager.INSTANCE.openModel(file);
        }
    }
    
    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_OPEN_16);
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

    public void dispose() {
    } 
}