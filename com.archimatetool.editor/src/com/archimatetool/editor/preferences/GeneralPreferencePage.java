/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * General Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class GeneralPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    private static String HELP_ID = "com.archimatetool.help.prefsGeneral"; //$NON-NLS-1$
    
    private Button fOpenDiagramsOnLoadButton;
    private Button fBackupOnSaveButton;
    
    private Spinner fMRUSizeSpinner;
    
    private Button fUseCurvedTabsButton;
    
    private Button fAnimateVisualiserNodesButton;
    
	public GeneralPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        GridData gd;
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.GeneralPreferencePage_0);
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Automatically open views when opening a model file
        fOpenDiagramsOnLoadButton = new Button(fileGroup, SWT.CHECK);
        fOpenDiagramsOnLoadButton.setText(Messages.GeneralPreferencePage_1);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fOpenDiagramsOnLoadButton.setLayoutData(gd);

        // Backup file on save
        fBackupOnSaveButton = new Button(fileGroup, SWT.CHECK);
        fBackupOnSaveButton.setText(Messages.GeneralPreferencePage_5);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fBackupOnSaveButton.setLayoutData(gd);
        
        // Size of recently opened file list
        Label label = new Label(fileGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_2);
        
        fMRUSizeSpinner = new Spinner(fileGroup, SWT.BORDER);
        fMRUSizeSpinner.setMinimum(3);
        fMRUSizeSpinner.setMaximum(15);
        
        // Appearance
        Group appearanceGroup = new Group(client, SWT.NULL);
        appearanceGroup.setText(Messages.GeneralPreferencePage_3);
        appearanceGroup.setLayout(new GridLayout(2, false));
        appearanceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fUseCurvedTabsButton = new Button(appearanceGroup, SWT.CHECK);
        fUseCurvedTabsButton.setText(Messages.GeneralPreferencePage_4);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseCurvedTabsButton.setLayoutData(gd);
        
        // Visualiser
        Group visualiserGroup = new Group(client, SWT.NULL);
        visualiserGroup.setText(Messages.GeneralPreferencePage_6);
        visualiserGroup.setLayout(new GridLayout(2, false));
        visualiserGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fAnimateVisualiserNodesButton = new Button(visualiserGroup, SWT.CHECK);
        fAnimateVisualiserNodesButton.setText(Messages.GeneralPreferencePage_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fAnimateVisualiserNodesButton.setLayoutData(gd);
        
        setValues();
        
        return client;
    }

    private void setValues() {
        setSpinnerValues();
        fBackupOnSaveButton.setSelection(getPreferenceStore().getBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fUseCurvedTabsButton.setSelection(!PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        fAnimateVisualiserNodesButton.setSelection(getPreferenceStore().getBoolean(ANIMATE_VISUALISER_NODES));
    }
    
    private void setSpinnerValues() {
        fMRUSizeSpinner.setSelection(getPreferenceStore().getInt(MRU_MAX));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(BACKUP_ON_SAVE, fBackupOnSaveButton.getSelection());
        getPreferenceStore().setValue(OPEN_DIAGRAMS_ON_LOAD, fOpenDiagramsOnLoadButton.getSelection());
        getPreferenceStore().setValue(MRU_MAX, fMRUSizeSpinner.getSelection());
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, !fUseCurvedTabsButton.getSelection());
        getPreferenceStore().setValue(ANIMATE_VISUALISER_NODES, fAnimateVisualiserNodesButton.getSelection());
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fBackupOnSaveButton.setSelection(getPreferenceStore().getDefaultBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getDefaultBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fMRUSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(MRU_MAX));
        fUseCurvedTabsButton.setSelection(!PlatformUI.getPreferenceStore().getDefaultBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        fAnimateVisualiserNodesButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE_VISUALISER_NODES));
        super.performDefaults();
    }
    
    public void init(IWorkbench workbench) {
    }
}