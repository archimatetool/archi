/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.sketch.ISketchEditor;

/**
 * Diagram Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class DiagramPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    private static String HELP_ID = "uk.ac.bolton.archimate.help.prefsDiagram"; //$NON-NLS-1$
    
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
    
    private Combo fDefaultSketchBackgroundCombo;
    
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

        GridData gd;
        Label label;
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // -------------- Layout ----------------------------
        
        Group layoutGroup = new Group(client, SWT.NULL);
        layoutGroup.setText(Messages.DiagramPreferencePage_0);
        layoutGroup.setLayout(new GridLayout(2, false));
        layoutGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Grid Size
        label = new Label(layoutGroup, SWT.NULL);
        label.setText(Messages.DiagramPreferencePage_1);
        
        fGridSizeSpinner = new Spinner(layoutGroup, SWT.BORDER);
        fGridSizeSpinner.setMinimum(5);
        fGridSizeSpinner.setMaximum(100);
        
        // Animate Layout
        fDoAnimationButton = new Button(layoutGroup, SWT.CHECK);
        fDoAnimationButton.setText(Messages.DiagramPreferencePage_2);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoAnimationButton.setLayoutData(gd);

        // Animation Speed
        label = new Label(layoutGroup, SWT.NULL);
        label.setText(Messages.DiagramPreferencePage_3);

        fAnimationSpeedSpinner = new Spinner(layoutGroup, SWT.BORDER);
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
        
        // -------------- Sketch ----------------------------

        Group sketchGroup = new Group(client, SWT.NULL);
        sketchGroup.setLayout(new GridLayout(2, false));
        sketchGroup.setText(Messages.DiagramPreferencePage_19);
        sketchGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Default Sketch background
        label = new Label(sketchGroup, SWT.NULL);
        label.setText(Messages.DiagramPreferencePage_20);
        fDefaultSketchBackgroundCombo = new Combo(sketchGroup, SWT.READ_ONLY);
        fDefaultSketchBackgroundCombo.setItems(ISketchEditor.BACKGROUNDS);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fDefaultSketchBackgroundCombo.setLayoutData(gd);
        
        setValues();
        
        return client;
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
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getInt(SKETCH_DEFAULT_BACKGROUND));
        
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
        
        getPreferenceStore().setValue(SKETCH_DEFAULT_BACKGROUND, fDefaultSketchBackgroundCombo.getSelectionIndex());
        
        getPreferenceStore().setValue(EDIT_NAME_ON_NEW_OBJECT, fEditNameOnNewObjectButton.getSelection());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
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
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getDefaultInt(SKETCH_DEFAULT_BACKGROUND));
        
        fEditNameOnNewObjectButton.setSelection(getPreferenceStore().getDefaultBoolean(EDIT_NAME_ON_NEW_OBJECT));
        
        super.performDefaults();
    }

    public void init(IWorkbench workbench) {
    }
    
}