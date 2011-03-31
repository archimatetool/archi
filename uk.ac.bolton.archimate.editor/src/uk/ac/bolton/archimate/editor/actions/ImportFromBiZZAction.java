/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import uk.ac.bolton.archimate.editor.model.IModelImporter;
import uk.ac.bolton.archimate.editor.model.importer.ImportManager;

/**
 * Import BiZZdesign Architect Action
 * 
 * @author Phillip Beauvoir
 */
public class ImportFromBiZZAction extends Action
implements IWorkbenchAction {
    
    private IWorkbenchWindow workbenchWindow;
    
    public ImportFromBiZZAction(IWorkbenchWindow window) {
        super("BiZZdesign Architect Model...");
        setId("uk.ac.bolton.archimate.editor.action.importBiZZ");
        workbenchWindow = window;
    }
    
    @Override
    public void run() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.xma", "*.*" } );
        String path = dialog.open();
        if(path != null) {
            File file = new File(path);
            try {
                IModelImporter importer = ImportManager.getImporter(file);
                if(importer != null) {
                    importer.doImport();
                }
                else {
                    MessageDialog.openError(workbenchWindow.getShell(), "Import BiZZdesign Architect Model",
                            "Cannot import. Unknown format.");
                }
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), "Error opening file", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void dispose() {
        workbenchWindow = null;
    }
}