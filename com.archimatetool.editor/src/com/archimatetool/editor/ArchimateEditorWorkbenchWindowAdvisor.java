/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

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

    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ArchimateEditorActionBarAdvisor(configurer);
    }
    
    @Override
    public void postWindowOpen() {
        // Application specific launcher actions
        IPlatformLauncher launcher = ArchimateEditorPlugin.INSTANCE.getPlatformLauncher();
        if(launcher != null) {
            launcher.postWindowOpen(getWindowConfigurer().getWindow());
        }
    }
}
