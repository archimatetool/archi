/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IEditorModelManager;

/**
 * Handles Opening Documents from the Desktop on Windows and Mac
 * using the SWT.OpenDocument event
 */
public class OpenDocumentHandler {

    private static final File[] EMPTY_QUEUE = new File[0];

    private class OpenDocumentHook implements Listener {
        @Override
        public void handleEvent(Event event) {
            String str = event.text;
            if(str != null && !"".equals(str)) { //$NON-NLS-1$
                File localFile = new File(str);
                try {
                    str = localFile.getCanonicalPath();
                }
                catch(Exception ex) {
                    str = localFile.getAbsolutePath();
                }

                File file = new File(str);
                if(file.exists() && file.isFile()) {
                    enqueue(file);
                }
            }
        }
    }

    public static OpenDocumentHandler getInstance() {
        return instance;
    }

    private static final OpenDocumentHandler instance = new OpenDocumentHandler();

    private List<File> files = new ArrayList<File>();

    private Listener hook = null;

    private OpenDocumentHandler() {
    }

    public void hook(Display display) {
        if(hook == null) {
            hook = new OpenDocumentHook();
        }
        display.addListener(SWT.OpenDocument, hook);
    }

    public void unhook(Display display) {
        if(hook != null) {
            display.removeListener(SWT.OpenDocument, hook);
        }
    }

    public void openQueuedFiles() {
        File[] files = drain();
        
        if(files.length > 0) {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if(window != null) {
                Shell shell = window.getShell();
                if(shell != null && !shell.isDisposed()) {
                    shell.setMinimized(false);
                    shell.forceActive();
                }
            }

            openFiles(files);
        }
    }
    
    private void openFiles(File[] files) {
        for(File file : files) {
            if(!IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                IEditorModelManager.INSTANCE.openModel(file);
            }
        }
    }

    private void enqueue(File file) {
        synchronized(this) {
            files.add(file);
        }
    }

    private File[] drain() {
        synchronized(this) {
            if(files.isEmpty()) {
                return EMPTY_QUEUE;
            }
        }
        File[] array = files.toArray(new File[files.size()]);
        files.clear();
        return array;
    }
    
}
