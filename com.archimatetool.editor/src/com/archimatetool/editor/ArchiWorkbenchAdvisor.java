/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.archimatetool.editor.perspectives.MainPerspective;
import com.archimatetool.editor.ui.ThemeUtils;


/**
 * The workbench advisor for standalone RCP.
 * Configures the workbench as needed, including specifying the default perspective id.
 * Configures each new workbench window as it is being opened.
 * 
 * @author Phillip Beauvoir
 */
public class ArchiWorkbenchAdvisor
extends WorkbenchAdvisor
{
	/**
	 * Constructor
	 */
	public ArchiWorkbenchAdvisor() {
	}
	
    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        
        // Save and restore stuff
        configurer.setSaveAndRestore(true);
        
        // Rounded tabs are set on by default
        PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        
        // Progress
        // PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, true);

        // Show Help Button by default on Dialogs
        TrayDialog.setDialogHelpAvailable(true);
        
        // Ensure we have a default theme or light/dark theme as per OS
        ThemeUtils.setDefaultTheme();
    }
    
    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ArchiWorkbenchWindowAdvisor(configurer);
    }

	@Override
    public String getInitialWindowPerspectiveId() {
	    // If null then we could use WorkbenchWindowAdvisor.createEmptyWindowContents(Composite parent)
	    //return null; 
		return MainPerspective.ID;
	}
	
    @Override
    public void eventLoopIdle(Display display) {
        // See if the user has opened files from the Desktop
        // See http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fproduct_open_file.htm
        OpenDocumentHandler.getInstance().openQueuedFiles();
        super.eventLoopIdle(display);
    }
    
}
