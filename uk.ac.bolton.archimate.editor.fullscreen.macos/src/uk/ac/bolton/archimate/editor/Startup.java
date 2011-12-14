/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Show Full Screen widget on Mac OS X 10.7 and above if available
 * 
 * @author Phillip Beauvoir
 */
public class Startup implements IStartup {

    @Override
    public void earlyStartup() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {
            public void run() {
                IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
                for(IWorkbenchWindow window : windows) {
                    showMacFullScreenWidget(window.getShell());
                }
            }
        });
    }

    private void showMacFullScreenWidget(Shell shell) {
        try {
            // Show the full-screen widget. The equivalent of:
            // NSWindow nswindow = shell.view.window();
            // nswindow.setCollectionBehavior(1 << 7);
            
            Object nsWindow = MacOSReflect.getNSWindow(shell);
            
            Method methodSetCollectionBehavior = nsWindow.getClass().getDeclaredMethod("setCollectionBehavior", MacOSReflect.NSUInteger);
            methodSetCollectionBehavior.invoke(nsWindow, 1 << 7);
            
            // Get rid of the dummy toolbar created in CocoaUIEnhancer. The equivalent of:
            // NSWindow nswindow = shell.view.window();
            // nswindow.setToolbar(null);
            
            // Wait for CocoaUIEnhancer to create the dummy toolbar
            Method methodToolbar = nsWindow.getClass().getDeclaredMethod("toolbar");
            Display display = shell.getDisplay();
            int safeCount = 0;
            while(methodToolbar.invoke(nsWindow) == null && safeCount++ < 100) {
                if(!display.readAndDispatch()) {
                    display.sleep();
                }
            }
          
            // Set it to null
            Class<?> classNSToolbar = Class.forName("org.eclipse.swt.internal.cocoa.NSToolbar");
            Method methodSetToolbar = nsWindow.getClass().getDeclaredMethod("setToolbar", classNSToolbar);
            methodSetToolbar.invoke(nsWindow, new Object[] { null });
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
