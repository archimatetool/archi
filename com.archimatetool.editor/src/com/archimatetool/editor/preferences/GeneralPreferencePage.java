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

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.utils.PlatformUtils;


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
    
    private Button fUseEdgeBrowserButton;
    
    private Button fEnableJSHintsButton;
    private Button fEnableExternalHostsHintsButton;

    private Button fUseLabelExpressionsButton;
    
    private Button fDoAnimationViewButton;
    private Spinner fAnimationViewTimeSpinner;
    private Button fAnimateVisualiserNodesButton;
    private Spinner fAnimationVisualiserTimeSpinner;

	public GeneralPreferencePage() {
		setPreferenceStore(ArchiPlugin.PREFERENCES);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.GeneralPreferencePage_0);
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(createHorizontalGridData(2));
        
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
        modelTreeGroup.setLayoutData(createHorizontalGridData(2));
        
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
        expressionsGroup.setLayoutData(createHorizontalGridData(2));
        
        fUseLabelExpressionsButton = new Button(expressionsGroup, SWT.CHECK);
        fUseLabelExpressionsButton.setText(Messages.GeneralPreferencePage_18);
        fUseLabelExpressionsButton.setLayoutData(createHorizontalGridData(2));
        
        // Other
        Group otherGroup = new Group(client, SWT.NULL);
        otherGroup.setText(Messages.GeneralPreferencePage_12);
        otherGroup.setLayout(new GridLayout(2, false));
        otherGroup.setLayoutData(createHorizontalGridData(2));
        
        fScaleImagesButton = new Button(otherGroup, SWT.CHECK);
        fScaleImagesButton.setText(Messages.GeneralPreferencePage_13);
        fScaleImagesButton.setLayoutData(createHorizontalGridData(2));
        label = new Label(otherGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_14);
        label.setLayoutData(createHorizontalGridData(2));
        
        // Internal Browser
        Group browserGroup = new Group(client, SWT.NULL);
        browserGroup.setText(Messages.GeneralPreferencePage_19);
        browserGroup.setLayout(new GridLayout(1, false));
        browserGroup.setLayoutData(createHorizontalGridData(2));

        // Edge Browser on Windows
        if(PlatformUtils.isWindows()) {
            fUseEdgeBrowserButton = new Button(browserGroup, SWT.CHECK);
            fUseEdgeBrowserButton.setText(Messages.GeneralPreferencePage_15);
        }
        
        fEnableJSHintsButton = new Button(browserGroup, SWT.CHECK);
        fEnableJSHintsButton.setText(Messages.GeneralPreferencePage_21);
        
        fEnableExternalHostsHintsButton = new Button(browserGroup, SWT.CHECK);
        fEnableExternalHostsHintsButton.setText(Messages.GeneralPreferencePage_22);

        // -------------- Animation ----------------------------
        
        if(AnimationUtil.supportsAnimation()) {
            Group animationGroup = new Group(client, SWT.NULL);
            animationGroup.setText(Messages.GeneralPreferencePage_3);
            animationGroup.setLayout(new GridLayout(4, true));
            animationGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            animationGroup.setLayoutData(createHorizontalGridData(2));
            
            // Animate View
            fDoAnimationViewButton = new Button(animationGroup, SWT.CHECK);
            fDoAnimationViewButton.setText(Messages.GeneralPreferencePage_4);
            fDoAnimationViewButton.setLayoutData(createHorizontalGridData(2));

            // Animation View Speed
            label = new Label(animationGroup, SWT.NULL);
            label.setText(Messages.GeneralPreferencePage_8);

            fAnimationViewTimeSpinner = new Spinner(animationGroup, SWT.BORDER);
            fAnimationViewTimeSpinner.setMinimum(10);
            fAnimationViewTimeSpinner.setMaximum(500);

            // Animate Visualiser
            fAnimateVisualiserNodesButton = new Button(animationGroup, SWT.CHECK);
            fAnimateVisualiserNodesButton.setText(Messages.GeneralPreferencePage_9);
            fAnimateVisualiserNodesButton.setLayoutData(createHorizontalGridData(2));
            
            // Animation Visualiser Speed
            label = new Label(animationGroup, SWT.NULL);
            label.setText(Messages.GeneralPreferencePage_8);

            fAnimationVisualiserTimeSpinner = new Spinner(animationGroup, SWT.BORDER);
            fAnimationVisualiserTimeSpinner.setMinimum(10);
            fAnimationVisualiserTimeSpinner.setMaximum(500);
        }
        
        setValues();
        
        return client;
    }

    private void setValues() {
        fMRUSizeSpinner.setSelection(getPreferenceStore().getInt(MRU_MAX));
        
        fBackupOnSaveButton.setSelection(getPreferenceStore().getBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getBoolean(OPEN_DIAGRAMS_ON_LOAD));
        
        fShowUnusedElementsInModelTreeButton.setSelection(getPreferenceStore().getBoolean(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE));
        fAutoSearchButton.setSelection(getPreferenceStore().getBoolean(TREE_SEARCH_AUTO));
        fWarnOnDeleteButton.setSelection(getPreferenceStore().getBoolean(SHOW_WARNING_ON_DELETE_FROM_TREE));
        fUseLabelExpressionsButton.setSelection(getPreferenceStore().getBoolean(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE));

        fScaleImagesButton.setSelection(getPreferenceStore().getBoolean(SCALE_IMAGE_EXPORT));
        
        if(fUseEdgeBrowserButton != null) {
            fUseEdgeBrowserButton.setSelection(getPreferenceStore().getBoolean(EDGE_BROWSER));
        }
        
        fEnableJSHintsButton.setSelection(getPreferenceStore().getBoolean(HINTS_BROWSER_JS_ENABLED));
        fEnableExternalHostsHintsButton.setSelection(getPreferenceStore().getBoolean(HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED));

        if(AnimationUtil.supportsAnimation()) {
            fDoAnimationViewButton.setSelection(getPreferenceStore().getBoolean(ANIMATE_VIEW));
            fAnimationViewTimeSpinner.setSelection(getPreferenceStore().getInt(ANIMATION_VIEW_TIME));
            fAnimateVisualiserNodesButton.setSelection(getPreferenceStore().getBoolean(ANIMATE_VISUALISER_NODES));
            fAnimationVisualiserTimeSpinner.setSelection(getPreferenceStore().getInt(ANIMATE_VISUALISER_TIME));
        }
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
        
        if(fUseEdgeBrowserButton != null) {
            getPreferenceStore().setValue(EDGE_BROWSER, fUseEdgeBrowserButton.getSelection());
        }
        
        getPreferenceStore().setValue(HINTS_BROWSER_JS_ENABLED, fEnableJSHintsButton.getSelection());
        getPreferenceStore().setValue(HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED, fEnableExternalHostsHintsButton.getSelection());

        if(AnimationUtil.supportsAnimation()) {
            getPreferenceStore().setValue(ANIMATE_VIEW, fDoAnimationViewButton.getSelection());
            getPreferenceStore().setValue(ANIMATION_VIEW_TIME, fAnimationViewTimeSpinner.getSelection());
            getPreferenceStore().setValue(ANIMATE_VISUALISER_NODES, fAnimateVisualiserNodesButton.getSelection());
            getPreferenceStore().setValue(ANIMATE_VISUALISER_TIME, fAnimationVisualiserTimeSpinner.getSelection());
        }
        
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
        
        if(fUseEdgeBrowserButton != null) {
            fUseEdgeBrowserButton.setSelection(getPreferenceStore().getDefaultBoolean(EDGE_BROWSER));
        }
        
        fEnableJSHintsButton.setSelection(getPreferenceStore().getDefaultBoolean(HINTS_BROWSER_JS_ENABLED));
        fEnableExternalHostsHintsButton.setSelection(getPreferenceStore().getDefaultBoolean(HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED));

        if(AnimationUtil.supportsAnimation()) {
            fDoAnimationViewButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE_VIEW));
            fAnimationViewTimeSpinner.setSelection(getPreferenceStore().getDefaultInt(ANIMATION_VIEW_TIME));
            fAnimateVisualiserNodesButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE_VISUALISER_NODES));
            fAnimationVisualiserTimeSpinner.setSelection(getPreferenceStore().getDefaultInt(ANIMATE_VISUALISER_TIME));
        }
        
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
