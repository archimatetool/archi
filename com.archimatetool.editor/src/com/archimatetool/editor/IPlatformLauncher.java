/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * Platform Specific Launcher
 * 
 * @author Phillip Beauvoir
 */
public interface IPlatformLauncher {
    
    /**
     * Do any actions needed when the application starts but before the main Display and Workbench are created
     */
    void startup();
    
    /**
     * Do any actions needed when the application's display is created
     */
    void displayCreated(Display display);

    /**
     * Do any actions needed when the application's windows are open and the workbench has been created
     */
    void postWindowOpen(IWorkbenchWindow window);

    /**
     * @return True if App already open
     */
    boolean shouldApplicationExitEarly();

}
