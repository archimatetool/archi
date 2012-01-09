/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.FontFactory;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;

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
    
    private Button fDoAntiAliasButton;
    private Button fPaletteStateButton;
    
    private Label fDefaultFontLabel;
    private Button fDefaultFontButton;

    private CLabel fFontPreviewLabel;
    private FontData fDefaultFontData;
    
    private Font fTempFont;
    
    private Button fViewpointsFilterModelTreeButton;
    private Button fViewpointsHidePaletteElementsButton;
    private Button fViewpointsGhostDiagramElementsButton;
    private Button fViewpointsHideDiagramElementsButton;
    private Button fViewpointsHideMagicConnectorElementsButton;
    
    private Button fShowSketchBackgroundButton;
    
    
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
        
        Group layoutGroup = new Group(client, SWT.NULL);
        layoutGroup.setText("Layout");
        layoutGroup.setLayout(new GridLayout(2, false));
        layoutGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        label = new Label(layoutGroup, SWT.NULL);
        label.setText("Grid Size:");
        
        fGridSizeSpinner = new Spinner(layoutGroup, SWT.BORDER);
        fGridSizeSpinner.setMinimum(5);
        fGridSizeSpinner.setMaximum(100);
        
        fDoAnimationButton = new Button(layoutGroup, SWT.CHECK);
        fDoAnimationButton.setText("Animate Layout");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoAnimationButton.setLayoutData(gd);

        label = new Label(layoutGroup, SWT.NULL);
        label.setText("Animation Speed (mS)");

        fAnimationSpeedSpinner = new Spinner(layoutGroup, SWT.BORDER);
        fAnimationSpeedSpinner.setMinimum(10);
        fAnimationSpeedSpinner.setMaximum(500);
        
        Group viewGroup = new Group(client, SWT.NULL);
        viewGroup.setText("View");
        viewGroup.setLayout(new GridLayout(2, false));
        viewGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fDoAntiAliasButton = new Button(viewGroup, SWT.CHECK);
        fDoAntiAliasButton.setText("Use anti-aliasing on connections");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoAntiAliasButton.setLayoutData(gd);
        
        fPaletteStateButton = new Button(viewGroup, SWT.CHECK);
        fPaletteStateButton.setText("Palette is open when opening Views");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fPaletteStateButton.setLayoutData(gd);
        
        fViewTooltipsButton = new Button(viewGroup, SWT.CHECK);
        fViewTooltipsButton.setText("Show tooltips in Views");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewTooltipsButton.setLayoutData(gd);
        
        Group fontGroup = new Group(client, SWT.NULL);
        fontGroup.setText("Font");
        fontGroup.setLayout(new GridLayout(2, false));
        fontGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fDefaultFontLabel = new Label(fontGroup, SWT.NULL);
        fDefaultFontLabel.setText("Default Font");
        
        fDefaultFontButton = new Button(fontGroup, SWT.PUSH);
        fDefaultFontButton.setText("Edit...");
        fDefaultFontButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FontDialog dialog = new FontDialog(getShell());
                dialog.setText("Select Font");
                dialog.setFontList(new FontData[] { fDefaultFontData });
                
                FontData fd = dialog.open();
                if(fd != null) {
                    fDefaultFontData = fd;
                    setDefaultFontValues();
                }
            }
        });
        
        label = new Label(fontGroup, SWT.NULL);
        label.setText("Preview:");
        
        fFontPreviewLabel = new CLabel(fontGroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        gd.widthHint = 400;
        fFontPreviewLabel.setLayoutData(gd);
        
        Group viewpointsGroup = new Group(client, SWT.NULL);
        viewpointsGroup.setText("Viewpoints");
        viewpointsGroup.setLayout(new GridLayout(2, false));
        viewpointsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fViewpointsFilterModelTreeButton = new Button(viewpointsGroup, SWT.CHECK);
        fViewpointsFilterModelTreeButton.setText("Grey out disallowed elements in the Model Tree");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewpointsFilterModelTreeButton.setLayoutData(gd);
        
        fViewpointsHidePaletteElementsButton = new Button(viewpointsGroup, SWT.CHECK);
        fViewpointsHidePaletteElementsButton.setText("Hide disallowed elements from the Palette");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewpointsHidePaletteElementsButton.setLayoutData(gd);
        
        fViewpointsHideMagicConnectorElementsButton = new Button(viewpointsGroup, SWT.CHECK);
        fViewpointsHideMagicConnectorElementsButton.setText("Hide disallowed elements from the Magic Connector");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fViewpointsHideMagicConnectorElementsButton.setLayoutData(gd);

        fViewpointsGhostDiagramElementsButton = new Button(viewpointsGroup, SWT.RADIO);
        fViewpointsGhostDiagramElementsButton.setText("Ghost disallowed elements in a View");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fViewpointsGhostDiagramElementsButton.setLayoutData(gd);
        
        fViewpointsHideDiagramElementsButton = new Button(viewpointsGroup, SWT.RADIO);
        fViewpointsHideDiagramElementsButton.setText("Hide disallowed elements in a View");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fViewpointsHideDiagramElementsButton.setLayoutData(gd);
        
        Group sketchGroup = new Group(client, SWT.NULL);
        sketchGroup.setText("Sketch");
        sketchGroup.setLayout(new GridLayout());
        sketchGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fShowSketchBackgroundButton = new Button(sketchGroup, SWT.CHECK);
        fShowSketchBackgroundButton.setText("Show Background");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fShowSketchBackgroundButton.setLayoutData(gd);
        
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
        
        fDoAnimationButton.setSelection(getPreferenceStore().getBoolean(ANIMATE));
        
        fDoAntiAliasButton.setSelection(getPreferenceStore().getBoolean(ANTI_ALIAS));
        fPaletteStateButton.setSelection(getPreferenceStore().getBoolean(PALETTE_STATE));
        fViewTooltipsButton.setSelection(getPreferenceStore().getBoolean(VIEW_TOOLTIPS));
        
        fDefaultFontData = FontFactory.getDefaultUserViewFontData();
        setDefaultFontValues();
        
        fViewpointsFilterModelTreeButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_FILTER_MODEL_TREE));
        fViewpointsHidePaletteElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_PALETTE_ELEMENTS));
        fViewpointsHideMagicConnectorElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS));
        
        fViewpointsGhostDiagramElementsButton.setSelection(!getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        fViewpointsHideDiagramElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        
        fShowSketchBackgroundButton.setSelection(getPreferenceStore().getBoolean(SKETCH_SHOW_BACKGROUND));
    }
    
    private void setSpinnerValues() {
        fGridSizeSpinner.setSelection(getPreferenceStore().getInt(GRID_SIZE));
        fAnimationSpeedSpinner.setSelection(getPreferenceStore().getInt(ANIMATION_SPEED));
    }
    
    private void setDefaultFontValues() {
        fFontPreviewLabel.setText(fDefaultFontData.getName() + " " +
                fDefaultFontData.getHeight() + " " +
                ((fDefaultFontData.getStyle() & SWT.BOLD) == SWT.BOLD ? "Bold" : "") + " " +
                ((fDefaultFontData.getStyle() & SWT.ITALIC) == SWT.ITALIC ? "Italic" : "") + " " +
                "\nThe quick brown fox jumps over the lazy dog.");
        
        disposeTempFont();
        fTempFont = new Font(null, fDefaultFontData);
        fFontPreviewLabel.setFont(fTempFont);
        
        fFontPreviewLabel.getParent().getParent().layout();
        fFontPreviewLabel.getParent().getParent().redraw();
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(GRID_SIZE, fGridSizeSpinner.getSelection());

        getPreferenceStore().setValue(ANIMATE, fDoAnimationButton.getSelection());
        getPreferenceStore().setValue(ANIMATION_SPEED, fAnimationSpeedSpinner.getSelection());
        
        getPreferenceStore().setValue(ANTI_ALIAS, fDoAntiAliasButton.getSelection());
        getPreferenceStore().setValue(PALETTE_STATE, fPaletteStateButton.getSelection());
        getPreferenceStore().setValue(VIEW_TOOLTIPS, fViewTooltipsButton.getSelection());
        
        FontFactory.setDefaultUserViewFont(fDefaultFontData);
        
        getPreferenceStore().setValue(VIEWPOINTS_FILTER_MODEL_TREE, fViewpointsFilterModelTreeButton.getSelection());
        getPreferenceStore().setValue(VIEWPOINTS_HIDE_PALETTE_ELEMENTS, fViewpointsHidePaletteElementsButton.getSelection());
        getPreferenceStore().setValue(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS, fViewpointsHideMagicConnectorElementsButton.getSelection());
        getPreferenceStore().setValue(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS, fViewpointsHideDiagramElementsButton.getSelection());
        
        getPreferenceStore().setValue(SKETCH_SHOW_BACKGROUND, fShowSketchBackgroundButton.getSelection());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fGridSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(GRID_SIZE));

        fDoAnimationButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE));
        fAnimationSpeedSpinner.setSelection(getPreferenceStore().getDefaultInt(ANIMATION_SPEED));
        
        fDoAntiAliasButton.setSelection(getPreferenceStore().getDefaultBoolean(ANTI_ALIAS));
        fPaletteStateButton.setSelection(getPreferenceStore().getDefaultBoolean(PALETTE_STATE));
        fViewTooltipsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEW_TOOLTIPS));
        
        fDefaultFontData = FontFactory.getDefaultViewOSFontData();
        setDefaultFontValues();
        
        fViewpointsFilterModelTreeButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_FILTER_MODEL_TREE));
        fViewpointsHidePaletteElementsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_PALETTE_ELEMENTS));
        fViewpointsHideMagicConnectorElementsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS));
        
        fViewpointsGhostDiagramElementsButton.setSelection(!getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        fViewpointsHideDiagramElementsButton.setSelection(getPreferenceStore().getDefaultBoolean(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS));
        
        fShowSketchBackgroundButton.setSelection(getPreferenceStore().getDefaultBoolean(SKETCH_SHOW_BACKGROUND));
        
        super.performDefaults();
    }

    public void init(IWorkbench workbench) {
    }
    
    @Override
    public void dispose() {
        super.dispose();
        disposeTempFont();
    }
    
    private void disposeTempFont() {
        if(fTempFont != null && !fTempFont.isDisposed()) {
            fTempFont.dispose();
            fTempFont = null;
        }
    }
}