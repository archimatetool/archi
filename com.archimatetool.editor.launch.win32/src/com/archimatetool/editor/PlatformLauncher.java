/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IEditorModelManager;


public class PlatformLauncher implements IPlatformLauncher {

    @Override
    public void startup() {
    }

    @Override
    public void displayCreated(final Display display) {
        /*
         * Add a listener to the main Display to monitor *.archimate files opened from the desktop.
         * This will work on first application launch and thereafter.
         */
        display.addListener(SWT.OpenDocument, new Listener() {
            public void handleEvent(Event paramEvent) {
                if(!PlatformUI.isWorkbenchRunning()) {
                    return;
                }
                
                String str = paramEvent.text;

                File localFile = new File(str);
                try {
                    str = localFile.getCanonicalPath();
                }
                catch(Exception ex) {
                    str = localFile.getAbsolutePath();
                }

                final File file = new File(str);
                
                if(file.exists() && file.isFile()) {
                    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

                    // If the WorkbenchWindow has been created
                    if(window != null) {
                        // Restore the Shell even if we have the file open in case it is minimised
                        window.getShell().setMinimized(false);

                        // Force app to be active shell
                        window.getShell().forceActive();
                        
                        // If user has a modal dialog open don't bother
                        if(display.getActiveShell() != window.getShell()) {
                            return;
                        }
                        
                        // Open the file *not* on a thread
                        display.syncExec(new Runnable() {
                            public void run() {
                                if(!IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                                    IEditorModelManager.INSTANCE.openModel(file);
                                }
                            };
                        });
                    }
                    // Workbench not created, App is being launched from *.archimate file on desktop
                    else {
                        // Open the file *on* a thread so the Workbench can have time to open and then open any diagrams if set in Prefs
                        display.asyncExec(new Runnable() {
                            public void run() {
                                if(!IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                                    IEditorModelManager.INSTANCE.openModel(file);
                                }
                            };
                        });
                    }                    
                }
            }
        });
    }

    @Override
    public void postWindowOpen(IWorkbenchWindow window) {
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

        Number hWnd = getOpenedWindowHandle(properties);
        if(hWnd.intValue() == 0 || !isValidWindow(hWnd)) {
            return false;
        }

        notifyOpenedWindow(hWnd);
        return true;
    }

    private boolean isValidWindow(Number hWnd) {
        // This is the Win SWT specific OS method
        // int length = OS.GetWindowTextLength(hWnd);
        
        int length = 0;
        
        // We'll get it by reflection...
        try {
            length = (Integer)invokeWindowsOSMethod("GetWindowTextLength", hWnd, getWordClass()); //$NON-NLS-1$
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return length > 0;
    }

    private Number getOpenedWindowHandle(Properties properties) {
        String value = properties.getProperty(WindowState.K_PRIMARY_WINDOW);
        if(value != null && !"".equals(value)) { //$NON-NLS-1$
            try {
                if(is64bitOS()) {
                    return new Long(value);
                }
                else {
                    return new Integer(value);
                }
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
    private void notifyOpenedWindow(Number hWnd) {
        // These are the Win SWT specific OS methods
        // OS.SetForegroundWindow(hWnd);
        // OS.SetFocus(hWnd);
        
        // We'll do it by reflection...
        try {
            invokeWindowsOSMethod("SetForegroundWindow", hWnd, getWordClass()); //$NON-NLS-1$
            invokeWindowsOSMethod("SetFocus", hWnd, getWordClass()); //$NON-NLS-1$
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Invoke a Windows OS SWT Specific method by reflection
     */
    private Object invokeWindowsOSMethod(String methodName, Number hWnd, Class<?>... parameterTypes) throws Exception {
        Class<?> c = Class.forName("org.eclipse.swt.internal.win32.OS"); //$NON-NLS-1$
        Method m = c.getDeclaredMethod(methodName, parameterTypes);
        return m.invoke(c, hWnd);
    }
    
    private boolean is64bitOS() {
        return Platform.getOSArch().equals(Platform.ARCH_X86_64);
    }
    
    private Class<?> getWordClass() {
        return is64bitOS() ? long.class : int.class;
    }
}
