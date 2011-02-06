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
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
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
    public static String HELPID = "uk.ac.bolton.archimate.help.prefsGeneral"; //$NON-NLS-1$
    
    private Button fOpenDiagramsOnLoadButton;
    private Button fFilterShowEmptyFoldersButton;
    
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
        
        Group treeGroup = new Group(client, SWT.NULL);
        treeGroup.setText("Model Tree");
        treeGroup.setLayout(new GridLayout(2, false));
        treeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fFilterShowEmptyFoldersButton = new Button(treeGroup, SWT.CHECK);
        fFilterShowEmptyFoldersButton.setText("Show empty folders when filtering and searching");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fFilterShowEmptyFoldersButton.setLayoutData(gd);
        
        setValues();
        
        return client;
    }

    private void setValues() {
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fFilterShowEmptyFoldersButton.setSelection(getPreferenceStore().getBoolean(FILTER_SHOW_EMPTY_FOLDERS));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(OPEN_DIAGRAMS_ON_LOAD, fOpenDiagramsOnLoadButton.getSelection());
        getPreferenceStore().setValue(FILTER_SHOW_EMPTY_FOLDERS, fFilterShowEmptyFoldersButton.getSelection());
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getDefaultBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fFilterShowEmptyFoldersButton.setSelection(getPreferenceStore().getDefaultBoolean(FILTER_SHOW_EMPTY_FOLDERS));
        super.performDefaults();
    }

    public void init(IWorkbench workbench) {
    }
}