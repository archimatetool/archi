/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.archimatetool.editor.actions.ArchimateEditorActionBarAdvisor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;



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
        
        // Status Line
        boolean doShowStatusLine = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_STATUS_LINE);
        configurer.setShowStatusLine(doShowStatusLine);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ArchimateEditorActionBarAdvisor(configurer);
    }
    
    String expiryDate = "23 October 2014";
    int year = 2014, month = 9, day = 23;
    
    @Override
    public void preWindowOpen() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        if(new Date().after(calendar.getTime())) {
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
                    "Archi 3.0 Beta", "This Beta version expired on " + expiryDate + ".\nPlease download the latest version.");
            System.exit(0);
        }
    }
    
    @Override
    public void postWindowOpen() {
        // Application specific launcher actions
        final IPlatformLauncher launcher = ArchimateEditorPlugin.INSTANCE.getPlatformLauncher();
        if(launcher != null) {
            launcher.postWindowOpen(getWindowConfigurer().getWindow());
        }
        
        Display.getCurrent().asyncExec(new Runnable() {
            public void run() {
                MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
                        "Archi 3.0 Beta", "This Beta version expires on " + expiryDate + ".");
            }
        });
    }
}
