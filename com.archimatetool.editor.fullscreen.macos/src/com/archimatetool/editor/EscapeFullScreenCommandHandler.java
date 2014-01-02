/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;



/**
 * Handles Exit of Mac OS X Lion Full Screen by escape key
 * 
 * @author Phillip Beauvoir
 */
public class EscapeFullScreenCommandHandler extends FullScreenCommandHandler {

    @Override
    public boolean isEnabled() {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        return super.isEnabled() && windows != null && windows.length >= 1 && windows[0].getShell().getFullScreen();
    }
}
