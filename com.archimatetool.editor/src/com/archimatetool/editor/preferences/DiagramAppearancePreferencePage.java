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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.sketch.ISketchEditor;
import com.archimatetool.model.ITextAlignment;


/**
 * Diagram Appearance Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class DiagramAppearancePreferencePage extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static String HELP_ID = "com.archimatetool.help.prefsDiagram"; //$NON-NLS-1$
    
    private Combo fDefaultGradientCombo;
    
    private String[] GRADIENT_STYLES = {
            Messages.DiagramAppearancePreferencePage_16,
            Messages.DiagramAppearancePreferencePage_17,
            Messages.DiagramAppearancePreferencePage_18,
            Messages.DiagramAppearancePreferencePage_19,
            Messages.DiagramAppearancePreferencePage_20
    };
    
    private Spinner fDefaultArchimateFigureWidthSpinner, fDefaultArchimateFigureHeightSpinner;
    
    private Combo fWordWrapStyleCombo;
    
    private String[] WORD_WRAP_STYLES = {
            Messages.DiagramAppearancePreferencePage_4,
            Messages.DiagramAppearancePreferencePage_5,
            Messages.DiagramAppearancePreferencePage_6
    };
    
    private Combo fDefaultSketchBackgroundCombo;
    
    private Combo fDefaultTextAlignmentCombo, fDefaultTextPositionCombo;
    
    private String[] TEXT_ALIGNMENTS = {
            Messages.DiagramAppearancePreferencePage_8,
            Messages.DiagramAppearancePreferencePage_9,
            Messages.DiagramAppearancePreferencePage_10
    };
    
    private int[] TEXT_ALIGNMENT_VALUES = {
            ITextAlignment.TEXT_ALIGNMENT_LEFT,
            ITextAlignment.TEXT_ALIGNMENT_CENTER,
            ITextAlignment.TEXT_ALIGNMENT_RIGHT
    };
    
    private String[] TEXT_POSITIONS = {
            Messages.DiagramAppearancePreferencePage_11,
            Messages.DiagramAppearancePreferencePage_12,
            Messages.DiagramAppearancePreferencePage_13
    };
    
    public DiagramAppearancePreferencePage() {
        setPreferenceStore(ArchiPlugin.PREFERENCES);
    }

    @Override
    public Composite createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        // -------------- Global ----------------------------
        
        Group globalGroup = new Group(client, SWT.NULL);
        globalGroup.setText(Messages.DiagramAppearancePreferencePage_0);
        globalGroup.setLayout(new GridLayout(2, false));
        globalGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Word wrap style
        Label label = new Label(globalGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_7);
        fWordWrapStyleCombo = new Combo(globalGroup, SWT.READ_ONLY);
        fWordWrapStyleCombo.setItems(WORD_WRAP_STYLES);
        fWordWrapStyleCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // -------------- Defaults ----------------------------
        
        Group defaultsGroup = new Group(client, SWT.NULL);
        defaultsGroup.setText(Messages.DiagramAppearancePreferencePage_1);
        defaultsGroup.setLayout(new GridLayout(2, false));
        defaultsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Sizes
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_2);
        fDefaultArchimateFigureWidthSpinner = new Spinner(defaultsGroup, SWT.BORDER);
        fDefaultArchimateFigureWidthSpinner.setMinimum(30);
        fDefaultArchimateFigureWidthSpinner.setMaximum(300);
        
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_3);
        fDefaultArchimateFigureHeightSpinner = new Spinner(defaultsGroup, SWT.BORDER);
        fDefaultArchimateFigureHeightSpinner.setMinimum(30);
        fDefaultArchimateFigureHeightSpinner.setMaximum(300);
        
        // Default Text Alignment
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_14);
        fDefaultTextAlignmentCombo = new Combo(defaultsGroup, SWT.READ_ONLY);
        fDefaultTextAlignmentCombo.setItems(TEXT_ALIGNMENTS);
        fDefaultTextAlignmentCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Default Text Position
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_15);
        fDefaultTextPositionCombo = new Combo(defaultsGroup, SWT.READ_ONLY);
        fDefaultTextPositionCombo.setItems(TEXT_POSITIONS);
        fDefaultTextPositionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Default Gradient
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_21);
        fDefaultGradientCombo = new Combo(defaultsGroup, SWT.READ_ONLY);
        fDefaultGradientCombo.setItems(GRADIENT_STYLES);
        fDefaultGradientCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        
        // -------------- Sketch ----------------------------

        Group sketchGroup = new Group(client, SWT.NULL);
        sketchGroup.setLayout(new GridLayout(2, false));
        sketchGroup.setText(Messages.DiagramAppearancePreferencePage_22);
        sketchGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Default Sketch background
        label = new Label(sketchGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferencePage_23);
        fDefaultSketchBackgroundCombo = new Combo(sketchGroup, SWT.READ_ONLY);
        fDefaultSketchBackgroundCombo.setItems(ISketchEditor.BACKGROUNDS);
        fDefaultSketchBackgroundCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        setValues();

        return client;
    }
    
    private void setValues() {
        fDefaultGradientCombo.select(getPreferenceStore().getInt(DEFAULT_GRADIENT) + 1); // Starts at -1
        fWordWrapStyleCombo.select(getPreferenceStore().getInt(ARCHIMATE_FIGURE_WORD_WRAP_STYLE));
        
        fDefaultArchimateFigureWidthSpinner.setSelection(getPreferenceStore().getInt(DEFAULT_ARCHIMATE_FIGURE_WIDTH));
        fDefaultArchimateFigureHeightSpinner.setSelection(getPreferenceStore().getInt(DEFAULT_ARCHIMATE_FIGURE_HEIGHT));
        
        // The values of these are 1, 2 and 4
        fDefaultTextAlignmentCombo.select(getPreferenceStore().getInt(DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT) / 2);
        
        // The values of these are 0, 1 and 2
        fDefaultTextPositionCombo.select(getPreferenceStore().getInt(DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION));
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getInt(SKETCH_DEFAULT_BACKGROUND));        
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(DEFAULT_GRADIENT, fDefaultGradientCombo.getSelectionIndex() - 1); // Starts at -1
        getPreferenceStore().setValue(ARCHIMATE_FIGURE_WORD_WRAP_STYLE, fWordWrapStyleCombo.getSelectionIndex());
        
        getPreferenceStore().setValue(DEFAULT_ARCHIMATE_FIGURE_WIDTH, fDefaultArchimateFigureWidthSpinner.getSelection());
        getPreferenceStore().setValue(DEFAULT_ARCHIMATE_FIGURE_HEIGHT, fDefaultArchimateFigureHeightSpinner.getSelection());
        
        getPreferenceStore().setValue(DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT, TEXT_ALIGNMENT_VALUES[fDefaultTextAlignmentCombo.getSelectionIndex()]);
        getPreferenceStore().setValue(DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION, fDefaultTextPositionCombo.getSelectionIndex());
        
        getPreferenceStore().setValue(SKETCH_DEFAULT_BACKGROUND, fDefaultSketchBackgroundCombo.getSelectionIndex());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fDefaultGradientCombo.select(getPreferenceStore().getDefaultInt(DEFAULT_GRADIENT) + 1); // Starts at -1
        fWordWrapStyleCombo.select(getPreferenceStore().getDefaultInt(ARCHIMATE_FIGURE_WORD_WRAP_STYLE));
        
        fDefaultArchimateFigureWidthSpinner.setSelection(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_WIDTH));
        fDefaultArchimateFigureHeightSpinner.setSelection(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_HEIGHT));
        
        fDefaultTextAlignmentCombo.select(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT) / 2); // Value = 2
        fDefaultTextPositionCombo.select(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION));
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getDefaultInt(SKETCH_DEFAULT_BACKGROUND));
        
        super.performDefaults();
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }

}