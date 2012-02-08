/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;

public class PlatformLauncher implements IPlatformLauncher {

    @Override
    public void startup() {
        logOpenFile();
    }

    @Override
    public void postWindowOpen(IWorkbenchWindow window) {
        /*
         * See if there is a file to open that user opened from desktop
         */
        checkOpenFile();

        /*
         * When window is activated check if it was activated by opening from
         * desktop file - notifyOpenedWindow()
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

    /**
     * Log the file the user launched to a temp file
     */
    private void logOpenFile() {
        String[] args = Platform.getApplicationArgs();
        if(args != null && args.length > 0) {
            File file = new File(args[0]);
            if(file.isFile()) {
                WindowState state = WindowState.get(WindowState.OPENING);
                state.getProperties().setProperty(WindowState.K_OPENING_FILE, file.toString());
                state.saveProperties();
            }
        }
    }

    /**
     * Open any user launched file
     */
    private void checkOpenFile() {
        WindowState state = WindowState.get(WindowState.OPENING);
        if(state.exists()) {
            String s = state.getProperties().getProperty(WindowState.K_OPENING_FILE);
            
            // Delete this now before opening model, otherwise we might activate again
            state.delete();
            
            if(s != null) {
                File file = new File(s);
                if(file.isFile() && !IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                    IEditorModelManager.INSTANCE.openModel(file);
                }
            }
        }
    }

    @Override
    public boolean shouldApplicationExitEarly() {
        WindowState state = WindowState.get(WindowState.WINDOW);
        if(!state.exists()) {
            return false;
        }

        Properties properties = state.getProperties();
        if(properties.isEmpty()) {
            return false;
        }

        int hWnd = getOpenedWindowHandle(properties);
        if(hWnd == 0 || !isValidWindow(hWnd)) {
            return false;
        }

        notifyOpenedWindow(hWnd);
        return true;
    }

    private boolean isValidWindow(int hWnd) {
        // This is the Win32 SWT specific OS method
        // int length = OS.GetWindowTextLength(hWnd);
        
        int length = 0;
        
        // We'll get it by reflection...
        try {
            length = (Integer)invokeWindowsOSMethod("GetWindowTextLength", hWnd, int.class); //$NON-NLS-1$
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return length > 0;
    }

    private int getOpenedWindowHandle(Properties properties) {
        String value = properties.getProperty(WindowState.K_PRIMARY_WINDOW);
        if(value != null && !"".equals(value)) { //$NON-NLS-1$
            try {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /*
     * Bring the window into focus
     */
    private void notifyOpenedWindow(int hWnd) {
        // These are the Win32 SWT specific OS methods
        // OS.SetForegroundWindow(hWnd);
        // OS.SetFocus(hWnd);
        
        // We'll do it by reflection...
        try {
            invokeWindowsOSMethod("SetForegroundWindow", hWnd, int.class); //$NON-NLS-1$
            invokeWindowsOSMethod("SetFocus", hWnd, int.class); //$NON-NLS-1$
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Invoke a Windows OS SWT Specific method by reflection
     */
    private Object invokeWindowsOSMethod(String methodName, int hWnd, Class<?>... parameterTypes) throws Exception {
        Class<?> c = Class.forName("org.eclipse.swt.internal.win32.OS"); //$NON-NLS-1$
        Method m = c.getDeclaredMethod(methodName, parameterTypes);
        return m.invoke(c, hWnd);
    }
}
