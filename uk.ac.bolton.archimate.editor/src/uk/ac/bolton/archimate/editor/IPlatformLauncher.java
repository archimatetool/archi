/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import org.eclipse.ui.IWorkbenchWindow;


/**
 * Platform Specific Launcher
 * 
 * @author Phillip Beauvoir
 */
public interface IPlatformLauncher {
    
    /**
     * Do any actions needed when the application starts
     */
    void startup();
    
    /**
     * Do any actions needed when the application's windows are open
     */
    void postWindowOpen(IWorkbenchWindow window);

    /**
     * @return True if App already open
     */
    boolean shouldApplicationExitEarly();

}
