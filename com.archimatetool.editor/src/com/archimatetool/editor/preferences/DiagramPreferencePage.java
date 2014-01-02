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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * Diagram Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class DiagramPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    private static String HELP_ID = "com.archimatetool.help.prefsDiagram"; //$NON-NLS-1$
    
    private Spinner fGridSizeSpinner;
    
    private Button fViewTooltipsButton;
    
    private Button fDoAnimationButton;
    private Spinner fAnimationSpeedSpinner;
    
    private Button fPaletteStateButton;
        
    private Button fViewpointsFilterModelTreeButton;
    private Button fViewpointsHidePaletteElementsButton;
    private Button fViewpointsGhostDiagramElementsButton;
    private Button fViewpointsHideDiagramElementsButton;
    private Button fViewpointsHideMagicConnectorElementsButton;
    
    private Button fEditNameOnNewObjectButton;
    
    private TabFolder fTabfolder;
    
    private DiagramFiguresPreferenceTab fDiagramFiguresPreferenceTab;
    private DiagramAppearancePreferenceTab fDiagramAppearancePreferenceTab;
    
    
	/**
	 * Constructor
	 */
	public DiagramPreferencePage() {
		setPreferenceStore(Preferences.STORE);
		//setDescription("Diagram Preferences.");
	}
	

    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        fTabfolder = new TabFolder(parent, SWT.NONE);

        createGeneralTab();
        createAppearanceTab();
        createDefaultElementsTab();
        
        return fTabfolder;
    }
    
    private void createGeneralTab() {
        GridData gd;
        Label label;
        
        Composite client = new Composite(fTabfolder, SWT.NULL);
        client.setLayout(new GridLayout());
        
        TabItem item = new TabItem(fTabfolder, SWT.NONE);
        item.setText(Messages.DiagramPreferencePage_5);
        item.setControl(client);

        Composite c = new Composite(client, SWT.NULL);
        c.setLayout(new GridLayout(2, false));
        c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Grid Size
        label = new Label(c, SWT.NULL);
        label.setText(Messages.DiagramPreferencePage_1);
        
        fGridSizeSpinner = new Spinner(c, SWT.BORDER);
        fGridSizeSpinner.setMinimum(5);
        fGridSizeSpinner.setMaximum(100);
        
        // -------------- Animation ----------------------------
        
        Group animationGroup = new Group(client, SWT.NULL);
        animationGroup.setText(Messages.DiagramPreferencePage_0);
        animationGroup.setLayout(new GridLayout(2, false));
        animationGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Animate Layout
        fDoAnimationButton = new Button(animationGroup, SWT.CHECK);
        fDoAnimationButton.setText(Messages.DiagramPreferencePage_2);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoAnimationButton.setLayoutData(gd);

        // Animation Speed
        label = new Label(animationGroup, SWT.NULL);
        label.setText(Messages.DiagramPreferencePage_3);

        fAnimationSpeedSpinner = new Spinner(animationGroup, SWT.BORDER);
        fAnimationSpeedSpinner.setMinimum(10);
        fAnimationSpeedSpinner.setMaximum(500);
        
        // -------------- View ----------------------------

        Group viewGroup = new Group(client, SWT.NULL);
        viewGroup.setText(Messages.DiagramPreferencePage_4);
        viewGroup.setLayout(new GridLayout(1, false));
        viewGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fPaletteStateButton = new Button(viewGroup, SWT.CHECK);
        fPaletteStateButton.setText(Messages.DiagramPreferencePage_6);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fPaletteStateButton.setLayoutData(gd);
        
        fViewTooltipsButton = new Button(viewGroup, SWT.CHECK);
        fViewTooltipsButton.setText(Messages.DiagramPreferencePage_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fViewTooltipsButton.setLayoutData(gd);
        
        fEditNameOnNewObjectButton = new Button(viewGroup, SWT.CHECK);
        fEditNameOnNewObjectButton.setText(Messages.DiagramPreferencePage_24);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fEditNameOnNewObjectButton.setLayoutData(gd);
        
        // -------------- Viewpoints ----------------------------

        Group viewpointsGroup = new Group(client, SWT.NULL);
        viewpointsGroup.setText(Messages.DiagramPreferencePage_13);
        viewpointsGroup.setLayout(new GridLayout(2, false));
        viewpointsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fViewpointsFilterModelTreeButton = new Button(viewpointsGroup, SWT.CHECK);
        fViewpointsFilterModelTreeButton.setText(Messages.DiagramPreferencePage_14);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewpointsFilterModelTreeButton.setLayoutData(gd);
        
        fViewpointsHidePaletteElementsButton = new Button(viewpointsGroup, SWT.CHECK);
        fViewpointsHidePaletteElementsButton.setText(Messages.DiagramPreferencePage_15);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewpointsHidePaletteElementsButton.setLayoutData(gd);
        
        fViewpointsHideMagicConnectorElementsButton = new Button(viewpointsGroup, SWT.CHECK);
        fViewpointsHideMagicConnectorElementsButton.setText(Messages.DiagramPreferencePage_16);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewpointsHideMagicConnectorElementsButton.setLayoutData(gd);

        fViewpointsGhostDiagramElementsButton = new Button(viewpointsGroup, SWT.RADIO);
        fViewpointsGhostDiagramElementsButton.setText(Messages.DiagramPreferencePage_17);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fViewpointsGhostDiagramElementsButton.setLayoutData(gd);
        
        fViewpointsHideDiagramElementsButton = new Button(viewpointsGroup, SWT.RADIO);
        fViewpointsHideDiagramElementsButton.setText(Messages.DiagramPreferencePage_18);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fViewpointsHideDiagramElementsButton.setLayoutData(gd);
        
        setValues();
    }
    
    private void createDefaultElementsTab() {
        fDiagramFiguresPreferenceTab = new DiagramFiguresPreferenceTab();
        Composite client = fDiagramFiguresPreferenceTab.createContents(fTabfolder);

        TabItem item = new TabItem(fTabfolder, SWT.NONE);
        item.setText(Messages.DiagramPreferencePage_8);
        item.setControl(client);
    }
    
    private void createAppearanceTab() {
        fDiagramAppearancePreferenceTab = new DiagramAppearancePreferenceTab();
        Composite client = fDiagramAppearancePreferenceTab.createContents(fTabfolder);

        TabItem item = new TabItem(fTabfolder, SWT.NONE);
        item.setText(Messages.DiagramPreferencePage_9);
        item.setControl(client);        
    }
    
    private void setValues() {
        setSpinnerValues();
        
        fDoAnimationButton.setSelection(getPreferenceStore().getBoolean(ANIMATE));
        
        fPaletteStateButton.setSelection(getPreferenceStore().getBoolean(PALETTE_STATE));
        fViewTooltipsButton.setSelection(getPreferenceStore().getBoolean(VIEW_TOOLTIPS));
        
        fViewpointsFilterModelTreeButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_FILTER_MODEL_TREE));
        fViewpointsHidePaletteElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_PALETTE_ELEMENTS));
        fViewpointsHideMagicConnectorElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS));
        
        fViewpointsGhostDiagramElementsButton.setSelection(!getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        fViewpointsHideDiagramElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        
        fEditNameOnNewObjectButton.setSelection(getPreferenceStore().getBoolean(EDIT_NAME_ON_NEW_OBJECT));
    }
    
    private void setSpinnerValues() {
        fGridSizeSpinner.setSelection(getPreferenceStore().getInt(GRID_SIZE));
        fAnimationSpeedSpinner.setSelection(getPreferenceStore().getInt(ANIMATION_SPEED));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(GRID_SIZE, fGridSizeSpinner.getSelection());

        getPreferenceStore().setValue(ANIMATE, fDoAnimationButton.getSelection());
        getPreferenceStore().setValue(ANIMATION_SPEED, fAnimationSpeedSpinner.getSelection());
        
        getPreferenceStore().setValue(PALETTE_STATE, fPaletteStateButton.getSelection());
        getPreferenceStore().setValue(VIEW_TOOLTIPS, fViewTooltipsButton.getSelection());
        
        getPreferenceStore().setValue(VIEWPOINTS_FILTER_MODEL_TREE, fViewpointsFilterModelTreeButton.getSelection());
        getPreferenceStore().setValue(VIEWPOINTS_HIDE_PALETTE_ELEMENTS, fViewpointsHidePaletteElementsButton.getSelection());
        getPreferenceStore().setValue(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS, fViewpointsHideMagicConnectorElementsButton.getSelection());
        getPreferenceStore().setValue(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS, fViewpointsHideDiagramElementsButton.getSelection());
        
        getPreferenceStore().setValue(EDIT_NAME_ON_NEW_OBJECT, fEditNameOnNewObjectButton.getSelection());
        
        fDiagramFiguresPreferenceTab.performOk();
        fDiagramAppearancePreferenceTab.performOk();
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        switch(fTabfolder.getSelectionIndex()) {
            case 0:
                performGeneralDefaults();
                break;

            case 1:
                fDiagramAppearancePreferenceTab.performDefaults();
                break;
                
            case 2:
                fDiagramFiguresPreferenceTab.performDefaults();
                break;
           
            default:
                break;
        }
        
        super.performDefaults();
    }
    
    private void performGeneralDefaults() {
        fGridSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(GRID_SIZE));

        fDoAnimationButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE));
        fAnimationSpeedSpinner.setSelection(getPreferenceStore().getDefaultInt(ANIMATION_SPEED));
        
        fPaletteStateButton.setSelection(getPreferenceStore().getDefaultBoolean(PALETTE_STATE));
        fViewTooltipsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEW_TOOLTIPS));
        
        fViewpointsFilterModelTreeButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_FILTER_MODEL_TREE));
        fViewpointsHidePaletteElementsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_PALETTE_ELEMENTS));
        fViewpointsHideMagicConnectorElementsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS));
        
        fViewpointsGhostDiagramElementsButton.setSelection(!getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        fViewpointsHideDiagramElementsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        
        fEditNameOnNewObjectButton.setSelection(getPreferenceStore().getDefaultBoolean(EDIT_NAME_ON_NEW_OBJECT));
    }

    public void init(IWorkbench workbench) {
    }
    
}