/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.archimatetool.editor.diagram.sketch.ISketchEditor;
import com.archimatetool.model.ITextAlignment;


/**
 * Diagram Appearance Preferences Tab panel
 * 
 * @author Phillip Beauvoir
 */
public class DiagramAppearancePreferenceTab implements IPreferenceConstants {
    
    private Combo fDefaultGradientCombo;
    
    private String[] GRADIENT_STYLES = {
            Messages.DiagramAppearancePreferenceTab_16,
            Messages.DiagramAppearancePreferenceTab_17,
            Messages.DiagramAppearancePreferenceTab_18,
            Messages.DiagramAppearancePreferenceTab_19,
            Messages.DiagramAppearancePreferenceTab_20
    };
    
    private Spinner fDefaultArchimateFigureWidthSpinner, fDefaultArchimateFigureHeightSpinner;
    
    private Combo fWordWrapStyleCombo;
    
    private String[] WORD_WRAP_STYLES = {
            Messages.DiagramAppearancePreferenceTab_4,
            Messages.DiagramAppearancePreferenceTab_5,
            Messages.DiagramAppearancePreferenceTab_6
    };
    
    private Combo fDefaultSketchBackgroundCombo;
    
    private Combo fDefaultTextAlignmentCombo, fDefaultTextPositionCombo;
    
    private String[] TEXT_ALIGNMENTS = {
            Messages.DiagramAppearancePreferenceTab_8,
            Messages.DiagramAppearancePreferenceTab_9,
            Messages.DiagramAppearancePreferenceTab_10
    };
    
    private int[] TEXT_ALIGNMENT_VALUES = {
            ITextAlignment.TEXT_ALIGNMENT_LEFT,
            ITextAlignment.TEXT_ALIGNMENT_CENTER,
            ITextAlignment.TEXT_ALIGNMENT_RIGHT
    };
    
    private String[] TEXT_POSITIONS = {
            Messages.DiagramAppearancePreferenceTab_11,
            Messages.DiagramAppearancePreferenceTab_12,
            Messages.DiagramAppearancePreferenceTab_13
    };
    
    public Composite createContents(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // -------------- Figures ----------------------------
        
        Group figuresGroup = new Group(client, SWT.NULL);
        figuresGroup.setText(Messages.DiagramAppearancePreferenceTab_0);
        figuresGroup.setLayout(new GridLayout(2, false));
        figuresGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Default Gradient
        Label label = new Label(figuresGroup, SWT.NULL);
        label.setText(Messages.DiagramFiguresPreferencePage_9);
        fDefaultGradientCombo = new Combo(figuresGroup, SWT.READ_ONLY);
        fDefaultGradientCombo.setItems(GRADIENT_STYLES);
        fDefaultGradientCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Word wrap style
        label = new Label(figuresGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_7);
        fWordWrapStyleCombo = new Combo(figuresGroup, SWT.READ_ONLY);
        fWordWrapStyleCombo.setItems(WORD_WRAP_STYLES);
        fWordWrapStyleCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // -------------- Defaults ----------------------------
        
        Group defaultsGroup = new Group(client, SWT.NULL);
        defaultsGroup.setText(Messages.DiagramAppearancePreferenceTab_1);
        defaultsGroup.setLayout(new GridLayout(2, false));
        defaultsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Sizes
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_2);
        fDefaultArchimateFigureWidthSpinner = new Spinner(defaultsGroup, SWT.BORDER);
        fDefaultArchimateFigureWidthSpinner.setMinimum(30);
        fDefaultArchimateFigureWidthSpinner.setMaximum(300);
        
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_3);
        fDefaultArchimateFigureHeightSpinner = new Spinner(defaultsGroup, SWT.BORDER);
        fDefaultArchimateFigureHeightSpinner.setMinimum(30);
        fDefaultArchimateFigureHeightSpinner.setMaximum(300);
        
        // Default Text Alignment
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_14);
        fDefaultTextAlignmentCombo = new Combo(defaultsGroup, SWT.READ_ONLY);
        fDefaultTextAlignmentCombo.setItems(TEXT_ALIGNMENTS);
        fDefaultTextAlignmentCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Default Text Position
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_15);
        fDefaultTextPositionCombo = new Combo(defaultsGroup, SWT.READ_ONLY);
        fDefaultTextPositionCombo.setItems(TEXT_POSITIONS);
        fDefaultTextPositionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
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
    
    private IPreferenceStore getPreferenceStore() {
        return Preferences.STORE;
    }

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
    
    protected void performDefaults() {
        fDefaultGradientCombo.select(getPreferenceStore().getDefaultInt(DEFAULT_GRADIENT) + 1); // Starts at -1
        fWordWrapStyleCombo.select(getPreferenceStore().getDefaultInt(ARCHIMATE_FIGURE_WORD_WRAP_STYLE));
        
        fDefaultArchimateFigureWidthSpinner.setSelection(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_WIDTH));
        fDefaultArchimateFigureHeightSpinner.setSelection(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_HEIGHT));
        
        fDefaultTextAlignmentCombo.select(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT) / 2); // Value = 2
        fDefaultTextPositionCombo.select(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION));
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getDefaultInt(SKETCH_DEFAULT_BACKGROUND));
    }
}