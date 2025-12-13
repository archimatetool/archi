/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.ui.UIUtils;
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
    private Button fShowSpecializationsModelTreeMenuButton;
    private Button fShowSpecializationIconsModelTreeButton;
    private Button fAlphanumericSortModelTreeButton;
    
    private Text fTreeDisplayIncrementText;
    
    private Button fAddDocumentationNoteButton;
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
		setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayoutFactory.fillDefaults().applyTo(client);
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.GeneralPreferencePage_0);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(fileGroup);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fileGroup);
        
        // Automatically open views when opening a model file
        fOpenDiagramsOnLoadButton = new Button(fileGroup, SWT.CHECK);
        fOpenDiagramsOnLoadButton.setText(Messages.GeneralPreferencePage_1);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fOpenDiagramsOnLoadButton);

        // Backup file on save
        fBackupOnSaveButton = new Button(fileGroup, SWT.CHECK);
        fBackupOnSaveButton.setText(Messages.GeneralPreferencePage_5);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fBackupOnSaveButton);
        
        // Size of recently opened file list
        Label label = new Label(fileGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_2);
        
        fMRUSizeSpinner = new Spinner(fileGroup, SWT.BORDER);
        fMRUSizeSpinner.setMinimum(3);
        fMRUSizeSpinner.setMaximum(15);
        
        // Model Tree
        Group modelTreeGroup = new Group(client, SWT.NULL);
        modelTreeGroup.setText(Messages.GeneralPreferencePage_10);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(modelTreeGroup);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(modelTreeGroup);
        
        fShowUnusedElementsInModelTreeButton = new Button(modelTreeGroup, SWT.CHECK);
        fShowUnusedElementsInModelTreeButton.setText(Messages.GeneralPreferencePage_11);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fShowUnusedElementsInModelTreeButton);
        
        fAutoSearchButton = new Button(modelTreeGroup, SWT.CHECK);
        fAutoSearchButton.setText(Messages.GeneralPreferencePage_6);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fAutoSearchButton);
        fAutoSearchButton.setToolTipText(Messages.GeneralPreferencePage_7);
        
        fWarnOnDeleteButton = new Button(modelTreeGroup, SWT.CHECK);
        fWarnOnDeleteButton.setText(Messages.GeneralPreferencePage_16);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fWarnOnDeleteButton);
        
        fShowSpecializationsModelTreeMenuButton = new Button(modelTreeGroup, SWT.CHECK);
        fShowSpecializationsModelTreeMenuButton.setText(Messages.GeneralPreferencePage_24);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fShowSpecializationsModelTreeMenuButton);
        
        fShowSpecializationIconsModelTreeButton = new Button(modelTreeGroup, SWT.CHECK);
        fShowSpecializationIconsModelTreeButton.setText(Messages.GeneralPreferencePage_26);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fShowSpecializationIconsModelTreeButton);
        
        fAlphanumericSortModelTreeButton = new Button(modelTreeGroup, SWT.CHECK);
        fAlphanumericSortModelTreeButton.setText(Messages.GeneralPreferencePage_27);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fAlphanumericSortModelTreeButton);
        
        label = new Label(modelTreeGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_25);
        fTreeDisplayIncrementText = UIUtils.createSingleTextControl(modelTreeGroup, SWT.BORDER, false);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(1, 1).applyTo(fTreeDisplayIncrementText);
        UIUtils.applyNumberVerifyListener(fTreeDisplayIncrementText, 0, 1000000);
        
        // Label Expressions
        Group expressionsGroup = new Group(client, SWT.NULL);
        expressionsGroup.setText(Messages.GeneralPreferencePage_17);
        GridLayoutFactory.swtDefaults().applyTo(expressionsGroup);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(expressionsGroup);
        
        fUseLabelExpressionsButton = new Button(expressionsGroup, SWT.CHECK);
        fUseLabelExpressionsButton.setText(Messages.GeneralPreferencePage_18);
        
        // Other
        Group otherGroup = new Group(client, SWT.NULL);
        otherGroup.setText(Messages.GeneralPreferencePage_12);
        GridLayoutFactory.swtDefaults().applyTo(otherGroup);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(otherGroup);
        
        fAddDocumentationNoteButton = new Button(otherGroup, SWT.CHECK);
        fAddDocumentationNoteButton.setText(Messages.GeneralPreferencePage_20);
        fAddDocumentationNoteButton.setToolTipText(Messages.GeneralPreferencePage_23);
        
        fScaleImagesButton = new Button(otherGroup, SWT.CHECK);
        fScaleImagesButton.setText(Messages.GeneralPreferencePage_13);
        fScaleImagesButton.setToolTipText(Messages.GeneralPreferencePage_14);
        
        // Internal Browser
        Group browserGroup = new Group(client, SWT.NULL);
        browserGroup.setText(Messages.GeneralPreferencePage_19);
        GridLayoutFactory.swtDefaults().applyTo(browserGroup);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(browserGroup);

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
            GridLayoutFactory.swtDefaults().spacing(20, 5).numColumns(4).equalWidth(true).applyTo(animationGroup);
            GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(animationGroup);
            
            // Animate View
            fDoAnimationViewButton = new Button(animationGroup, SWT.CHECK);
            fDoAnimationViewButton.setText(Messages.GeneralPreferencePage_4);
            GridDataFactory.create(GridData.HORIZONTAL_ALIGN_BEGINNING).span(2, 1).applyTo(fDoAnimationViewButton);

            // Animation View Speed
            label = new Label(animationGroup, SWT.NULL);
            label.setText(Messages.GeneralPreferencePage_8);

            fAnimationViewTimeSpinner = new Spinner(animationGroup, SWT.BORDER);
            fAnimationViewTimeSpinner.setMinimum(10);
            fAnimationViewTimeSpinner.setMaximum(500);

            // Animate Visualiser
            fAnimateVisualiserNodesButton = new Button(animationGroup, SWT.CHECK);
            fAnimateVisualiserNodesButton.setText(Messages.GeneralPreferencePage_9);
            GridDataFactory.create(GridData.HORIZONTAL_ALIGN_BEGINNING).span(2, 1).applyTo(fAnimateVisualiserNodesButton);
            
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
        fShowSpecializationsModelTreeMenuButton.setSelection(getPreferenceStore().getBoolean(SHOW_SPECIALIZATIONS_IN_MODEL_TREE_MENU));
        fShowSpecializationIconsModelTreeButton.setSelection(getPreferenceStore().getBoolean(SHOW_SPECIALIZATION_ICONS_IN_MODEL_TREE));
        fAlphanumericSortModelTreeButton.setSelection(getPreferenceStore().getBoolean(TREE_ALPHANUMERIC_SORT));
        fTreeDisplayIncrementText.setText(Integer.toString(getPreferenceStore().getInt(TREE_DISPLAY_NODE_INCREMENT)));
        
        fUseLabelExpressionsButton.setSelection(getPreferenceStore().getBoolean(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE));

        fScaleImagesButton.setSelection(getPreferenceStore().getBoolean(SCALE_IMAGE_EXPORT));
        
        fAddDocumentationNoteButton.setSelection(getPreferenceStore().getBoolean(ADD_DOCUMENTATION_NOTE_ON_RELATION_CHANGE));
        
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
        getPreferenceStore().setValue(SHOW_SPECIALIZATIONS_IN_MODEL_TREE_MENU, fShowSpecializationsModelTreeMenuButton.getSelection());
        getPreferenceStore().setValue(SHOW_SPECIALIZATION_ICONS_IN_MODEL_TREE, fShowSpecializationIconsModelTreeButton.getSelection());
        getPreferenceStore().setValue(TREE_ALPHANUMERIC_SORT, fAlphanumericSortModelTreeButton.getSelection());
        getPreferenceStore().setValue(TREE_DISPLAY_NODE_INCREMENT, fTreeDisplayIncrementText.getText());

        getPreferenceStore().setValue(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE, fUseLabelExpressionsButton.getSelection());
        
        getPreferenceStore().setValue(SCALE_IMAGE_EXPORT, fScaleImagesButton.getSelection());
        
        getPreferenceStore().setValue(ADD_DOCUMENTATION_NOTE_ON_RELATION_CHANGE, fAddDocumentationNoteButton.getSelection());
        
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
        fShowSpecializationsModelTreeMenuButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_SPECIALIZATIONS_IN_MODEL_TREE_MENU));
        fShowSpecializationIconsModelTreeButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_SPECIALIZATION_ICONS_IN_MODEL_TREE));
        fAlphanumericSortModelTreeButton.setSelection(getPreferenceStore().getDefaultBoolean(TREE_ALPHANUMERIC_SORT));
        fTreeDisplayIncrementText.setText(Integer.toString(getPreferenceStore().getDefaultInt(TREE_DISPLAY_NODE_INCREMENT)));
        
        fUseLabelExpressionsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE));
        
        fScaleImagesButton.setSelection(getPreferenceStore().getDefaultBoolean(SCALE_IMAGE_EXPORT));
        
        fAddDocumentationNoteButton.setSelection(getPreferenceStore().getDefaultBoolean(ADD_DOCUMENTATION_NOTE_ON_RELATION_CHANGE));
        
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
    
    @Override
    public void init(IWorkbench workbench) {
    }
}
