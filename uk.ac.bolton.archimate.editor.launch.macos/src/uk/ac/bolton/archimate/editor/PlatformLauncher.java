/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;

/**
 * PlatformLauncher for Mac
 * 
 * @author Phillip Beauvoir
 */
public class PlatformLauncher implements IPlatformLauncher {

    @Override
    public void startup() {
    }

    @Override
    public void postWindowOpen(IWorkbenchWindow window) {
        /*
         * Check now if there is a file to open that the user launched from the desktop
         */
        checkOpenFile();
        
        /*
         * When the window shell gets the focus see if it was activated by the user launching a desktop file
         * in which case the script will have written the file's path into the "opening" file
         */
        if(window != null) {
            Shell shell = window.getShell();
            if(shell != null && !shell.isDisposed()) {
                shell.addShellListener(new ShellAdapter() {
                    @Override
                    public void shellActivated(ShellEvent e) {
                        checkOpenFile();
                    }
                });
            }
        }
    }
    
    private void checkOpenFile() {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            File openingFile = new File(instanceLoc.getURL().getPath(), "opening"); //$NON-NLS-1$
            if(openingFile.isFile() && openingFile.canRead()) {
                try {
                    String fileName = readFileAsString(openingFile);
                    if(fileName != null) {
                        File file = new File(fileName);
                        if(file.isFile() && !IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                            IEditorModelManager.INSTANCE.openModel(file);
                        }
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
                
                openingFile.delete();
            }
        }
    }

    private String readFileAsString(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            is.read(buffer);
        }
        finally {
            if(is != null) {
                is.close();
            }
        }
        return new String(buffer);
    }

    @Override
    public boolean shouldApplicationExitEarly() {
        // Not needed on Mac
        return false;
    }
}
