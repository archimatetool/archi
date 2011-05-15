/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.utils.PlatformUtils;

/**
 * General Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class GeneralPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    public static String HELPID = "uk.ac.bolton.archimate.help.prefsGeneral"; //$NON-NLS-1$
    
    private Button fOpenDiagramsOnLoadButton;
    
    private Spinner fMRUSizeSpinner;
    
    private Button fUseCurvedTabsButton;
    
	public GeneralPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        GridData gd;
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText("Files");
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fOpenDiagramsOnLoadButton = new Button(fileGroup, SWT.CHECK);
        fOpenDiagramsOnLoadButton.setText("Automatically open Views in a Model when opening from file");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fOpenDiagramsOnLoadButton.setLayoutData(gd);
        
        Label label = new Label(fileGroup, SWT.NULL);
        label.setText("Size of recently opened file list:");
        
        fMRUSizeSpinner = new Spinner(fileGroup, SWT.BORDER);
        fMRUSizeSpinner.setMinimum(3);
        fMRUSizeSpinner.setMaximum(15);
        
        Group appearanceGroup = new Group(client, SWT.NULL);
        appearanceGroup.setText("Appearance");
        appearanceGroup.setLayout(new GridLayout(2, false));
        appearanceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fUseCurvedTabsButton = new Button(appearanceGroup, SWT.CHECK);
        fUseCurvedTabsButton.setText("Use curved tabs");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseCurvedTabsButton.setLayoutData(gd);
        
        setValues();
        
        return client;
    }

    private void setValues() {
        // Bug on Mac OS X Carbon - field is initially empty unless we thread this
        if(PlatformUtils.isMacCarbon()) {
            Display.getCurrent().asyncExec(new Runnable() {
                public void run() {
                    setSpinnerValues();
                }
            });
        }
        else {
            setSpinnerValues();
        }

        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fUseCurvedTabsButton.setSelection(!PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
    }
    
    private void setSpinnerValues() {
        fMRUSizeSpinner.setSelection(getPreferenceStore().getInt(MRU_MAX));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(OPEN_DIAGRAMS_ON_LOAD, fOpenDiagramsOnLoadButton.getSelection());
        getPreferenceStore().setValue(MRU_MAX, fMRUSizeSpinner.getSelection());
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, !fUseCurvedTabsButton.getSelection());
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getDefaultBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fMRUSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(MRU_MAX));
        fUseCurvedTabsButton.setSelection(!PlatformUI.getPreferenceStore().getDefaultBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        super.performDefaults();
    }
    
    public void init(IWorkbench workbench) {
    }
}