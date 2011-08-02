/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import uk.ac.bolton.archimate.editor.actions.ArchimateEditorActionBarAdvisor;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;

/**
 * Workbench Window Advisor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateEditorWorkbenchWindowAdvisor
extends WorkbenchWindowAdvisor {
    
    /**
     * Constructor
     * @param configurer
     */
    public ArchimateEditorWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ArchimateEditorActionBarAdvisor(configurer);
    }
    
    @Override
    public void postWindowOpen() {
        // Application specific launcher actions
        final IPlatformLauncher launcher = ArchimateEditorPlugin.INSTANCE.getPlatformLauncher();
        if(launcher != null) {
            launcher.postWindowOpen(getWindowConfigurer().getWindow());
        }
        
        showMacFullScreenWidget();
    }
    
    /**
     * Show Full Screen widget on Mac OS X 10.7 and above if available
     */
    private void showMacFullScreenWidget() {
        if(!PlatformUtils.isMac()) {
            return;
        }
        
        if(System.getProperty("os.version").startsWith("10.6")) {
            return;
        }
        
        // It translates to this...
        //getWindowConfigurer().getWindow().getShell().view.window().setCollectionBehavior(1 << 7);

        try {
            Shell shell = getWindowConfigurer().getWindow().getShell();
            
            Field fieldView = Control.class.getDeclaredField("view");
            Object nsView = fieldView.get(shell);
            
            Method methodWindow = fieldView.getType().getDeclaredMethod("window", new Class[] {});
            Object nsWindow = methodWindow.invoke(nsView, new Object[] {});
            
            Method methodSetCollectionBehavior = nsWindow.getClass().getDeclaredMethod("setCollectionBehavior", long.class);
            methodSetCollectionBehavior.invoke(nsWindow, 1 << 7);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
