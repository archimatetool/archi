/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.example;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Export
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MyExportHandler extends AbstractHandler {
    
    private static final String MY_EXTENSION = ".mex";
    private static final String MY_EXTENSION_WILDCARD = "*.mex";
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // Get active and selected model, if any
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
            
            // Open dialog to get file to save to
            File file = askSaveFile(window);
            if(file != null) {
                try {
                    // Export
                    new MyExporter().export(file, model);
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(window.getShell(), "Export", ex.getMessage());
                }
            }
        }

        return null;
    }
    
    /**
     * Ask user for file name to save to
     */
    private File askSaveFile(IWorkbenchWindow window) {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.SAVE);
        dialog.setText("Export Model");
        dialog.setFilterExtensions(new String[] { MY_EXTENSION_WILDCARD, "*.*" } );

        // Set to true for consistency on all OSs
        dialog.setOverwrite(true);
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(MY_EXTENSION)) {
            path += MY_EXTENSION;
        }
        
        return new File(path);
    }
}
