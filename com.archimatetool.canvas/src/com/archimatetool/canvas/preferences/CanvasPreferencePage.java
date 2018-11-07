/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.canvas.CanvasEditorPlugin;


/**
 * Canvas Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class CanvasPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static String HELP_ID = "com.archimatetool.help.prefsCanvas"; //$NON-NLS-1$
    
    private Button fCanvasEditorEnabledButton;
    
	public CanvasPreferencePage() {
		setPreferenceStore(CanvasEditorPlugin.INSTANCE.getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        GridData gd;
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.CanvasPreferencePage_0);
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fCanvasEditorEnabledButton = new Button(fileGroup, SWT.CHECK);
        fCanvasEditorEnabledButton.setText(Messages.CanvasPreferencePage_1);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fCanvasEditorEnabledButton.setLayoutData(gd);
        
        setValues();
        
        return client;
    }

    private void setValues() {
        fCanvasEditorEnabledButton.setSelection(getPreferenceStore().getBoolean(CANVAS_EDITOR_ENABLED));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(CANVAS_EDITOR_ENABLED, fCanvasEditorEnabledButton.getSelection());
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fCanvasEditorEnabledButton.setSelection(getPreferenceStore().getDefaultBoolean(CANVAS_EDITOR_ENABLED));
        super.performDefaults();
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}