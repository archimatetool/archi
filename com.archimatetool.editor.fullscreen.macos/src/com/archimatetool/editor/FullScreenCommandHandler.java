/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Handles Enter and Exit of Mac OS X Lion Full Screen
 * 
 * @author Phillip Beauvoir
 */
public class FullScreenCommandHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        if(windows != null && windows.length > 0) {
            Shell shell = windows[0].getShell();
            shell.setFullScreen(!shell.getFullScreen());
        }
        
        return null;
    }
    
    @Override
    public boolean isEnabled() {
        return isSupportedVersion();
    }
    
    /**
     * @return true if we are on OS X 10.7 and above
     */
    public static boolean isSupportedVersion() {
        return Platform.WS_COCOA.equals(Platform.getWS()) && System.getProperty("os.version").compareTo("10.7") >= 0; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
