/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * License which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
        for(Object nsWindow : getNSWindows()) {
            toggleFullScreen(nsWindow);
        }
        
        return null;
    }
    
    protected Object[] getNSWindows() {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        Object[] nsWindows = new Object[windows.length];
        for(int i = 0; i < windows.length; i++) {
            nsWindows[i] = MacOSReflect.getNSWindow(windows[i].getShell());
        }
        return nsWindows;
    }
    
    public void toggleFullScreen(Object nsWindow) {
        long toggleFullScreen = MacOSReflect.selector("toggleFullScreen:");
        long target = MacOSReflect.getID(nsWindow);
        MacOSReflect.objc_msgSend(target, toggleFullScreen, 0);
    }

    public boolean isFullScreen(Object nsWindow) {
        long styleMask = MacOSReflect.executeLong(nsWindow, "styleMask");
        return (((styleMask >> 14) & 1) == 1);
    }

    public void setFullScreen(Object nsWindow, boolean fullScreen) {
        if(isFullScreen(nsWindow) != fullScreen) {
            toggleFullScreen(nsWindow);
        }
    }

}
