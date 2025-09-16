/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.example;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * Command Action Handler for Import
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MyImportHandler extends AbstractHandler {
    
    private static final String MY_EXTENSION_WILDCARD = "*.mex";
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        
        // Ask to open the file
        File file = askOpenFile(window);
        if(file != null) {
            // Import
            new MyImporter().doImport(file);
        }
        
        return null;
    }

    /**
     * Ask to open a file.
     */
    private File askOpenFile(IWorkbenchWindow window) {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { MY_EXTENSION_WILDCARD, "*.*" } );
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }
}
