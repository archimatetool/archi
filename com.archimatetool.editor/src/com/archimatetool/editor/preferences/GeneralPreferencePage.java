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
    
    private Button fShowUnusedElementsInModelTreeButton;
    private Button fAutoSearchButton;
    private Button fWarnOnDeleteButton;
    
    private Button fScaleImagesButton;
    
    private Button fUseLabelExpressionsButton;

	public GeneralPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.GeneralPreferencePage_0);
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(createHorizontalGridData(1));
        
        // Automatically open views when opening a model file
        fOpenDiagramsOnLoadButton = new Button(fileGroup, SWT.CHECK);
        fOpenDiagramsOnLoadButton.setText(Messages.GeneralPreferencePage_1);
        fOpenDiagramsOnLoadButton.setLayoutData(createHorizontalGridData(2));

        // Backup file on save
        fBackupOnSaveButton = new Button(fileGroup, SWT.CHECK);
        fBackupOnSaveButton.setText(Messages.GeneralPreferencePage_5);
        fBackupOnSaveButton.setLayoutData(createHorizontalGridData(2));
        
        // Size of recently opened file list
        Label label = new Label(fileGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_2);
        
        fMRUSizeSpinner = new Spinner(fileGroup, SWT.BORDER);
        fMRUSizeSpinner.setMinimum(3);
        fMRUSizeSpinner.setMaximum(15);
        
        // Model Tree
        Group modelTreeGroup = new Group(client, SWT.NULL);
        modelTreeGroup.setText(Messages.GeneralPreferencePage_10);
        modelTreeGroup.setLayout(new GridLayout(2, false));
        modelTreeGroup.setLayoutData(createHorizontalGridData(1));
        
        fShowUnusedElementsInModelTreeButton = new Button(modelTreeGroup, SWT.CHECK);
        fShowUnusedElementsInModelTreeButton.setText(Messages.GeneralPreferencePage_11);
        fShowUnusedElementsInModelTreeButton.setLayoutData(createHorizontalGridData(2));
        
        fAutoSearchButton = new Button(modelTreeGroup, SWT.CHECK);
        fAutoSearchButton.setText(Messages.GeneralPreferencePage_6);
        fAutoSearchButton.setLayoutData(createHorizontalGridData(2));
        
        label = new Label(modelTreeGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_7);
        label.setLayoutData(createHorizontalGridData(2));
        
        fWarnOnDeleteButton = new Button(modelTreeGroup, SWT.CHECK);
        fWarnOnDeleteButton.setText(Messages.GeneralPreferencePage_16);
        fWarnOnDeleteButton.setLayoutData(createHorizontalGridData(2));
        
        // Label Expressions
        Group expressionsGroup = new Group(client, SWT.NULL);
        expressionsGroup.setText(Messages.GeneralPreferencePage_17);
        expressionsGroup.setLayout(new GridLayout(2, false));
        expressionsGroup.setLayoutData(createHorizontalGridData(1));
        
        fUseLabelExpressionsButton = new Button(expressionsGroup, SWT.CHECK);
        fUseLabelExpressionsButton.setText(Messages.GeneralPreferencePage_18);
        fUseLabelExpressionsButton.setLayoutData(createHorizontalGridData(2));
        
        // Other
        Group otherGroup = new Group(client, SWT.NULL);
        otherGroup.setText(Messages.GeneralPreferencePage_12);
        otherGroup.setLayout(new GridLayout(2, false));
        otherGroup.setLayoutData(createHorizontalGridData(1));
        
        fScaleImagesButton = new Button(otherGroup, SWT.CHECK);
        fScaleImagesButton.setText(Messages.GeneralPreferencePage_13);
        fScaleImagesButton.setLayoutData(createHorizontalGridData(2));
        label = new Label(otherGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_14);
        label.setLayoutData(createHorizontalGridData(2));
        
        setValues();
        
        return client;
    }

    private void setValues() {
        setSpinnerValues();
        fBackupOnSaveButton.setSelection(getPreferenceStore().getBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getBoolean(OPEN_DIAGRAMS_ON_LOAD));
        
        fShowUnusedElementsInModelTreeButton.setSelection(getPreferenceStore().getBoolean(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE));
        fAutoSearchButton.setSelection(getPreferenceStore().getBoolean(TREE_SEARCH_AUTO));
        fWarnOnDeleteButton.setSelection(getPreferenceStore().getBoolean(SHOW_WARNING_ON_DELETE_FROM_TREE));
        fUseLabelExpressionsButton.setSelection(getPreferenceStore().getBoolean(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE));

        fScaleImagesButton.setSelection(getPreferenceStore().getBoolean(SCALE_IMAGE_EXPORT));
    }
    
    private void setSpinnerValues() {
        fMRUSizeSpinner.setSelection(getPreferenceStore().getInt(MRU_MAX));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(BACKUP_ON_SAVE, fBackupOnSaveButton.getSelection());
        getPreferenceStore().setValue(OPEN_DIAGRAMS_ON_LOAD, fOpenDiagramsOnLoadButton.getSelection());
        getPreferenceStore().setValue(MRU_MAX, fMRUSizeSpinner.getSelection());
        
        getPreferenceStore().setValue(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE, fShowUnusedElementsInModelTreeButton.getSelection());
        getPreferenceStore().setValue(TREE_SEARCH_AUTO, fAutoSearchButton.getSelection());
        getPreferenceStore().setValue(SHOW_WARNING_ON_DELETE_FROM_TREE, fWarnOnDeleteButton.getSelection());
        getPreferenceStore().setValue(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE, fUseLabelExpressionsButton.getSelection());
        
        getPreferenceStore().setValue(SCALE_IMAGE_EXPORT, fScaleImagesButton.getSelection());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fBackupOnSaveButton.setSelection(getPreferenceStore().getDefaultBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getDefaultBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fMRUSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(MRU_MAX));
        
        fShowUnusedElementsInModelTreeButton.setSelection(getPreferenceStore().getDefaultBoolean(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE));
        fAutoSearchButton.setSelection(getPreferenceStore().getDefaultBoolean(TREE_SEARCH_AUTO));
        fWarnOnDeleteButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_WARNING_ON_DELETE_FROM_TREE));
        fUseLabelExpressionsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE));
        
        fScaleImagesButton.setSelection(getPreferenceStore().getDefaultBoolean(SCALE_IMAGE_EXPORT));
        
        super.performDefaults();
    }
    
    private GridData createHorizontalGridData(int span) {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = span;
        return gd;
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}
