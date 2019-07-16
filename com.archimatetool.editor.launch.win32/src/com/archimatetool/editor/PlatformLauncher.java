/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.lang.reflect.Method;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * PlatformLauncher for Windows
 * 
 * @author Phillip Beauvoir
 */
public class PlatformLauncher implements IPlatformLauncher {

    @Override
    public void startup() {
    }

    @Override
    public void displayCreated(final Display display) {
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
                    return Long.valueOf(value);
                }
                else {
                    return Integer.valueOf(value);
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
