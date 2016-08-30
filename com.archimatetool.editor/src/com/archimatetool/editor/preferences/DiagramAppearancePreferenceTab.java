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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.archimatetool.editor.diagram.sketch.ISketchEditor;


/**
 * Diagram Appearance Preferences Tab panel
 * 
 * @author Phillip Beauvoir
 */
public class DiagramAppearancePreferenceTab implements IPreferenceConstants {
    
    private Button fShowGradientButton;
    
    private Spinner fDefaultArchimateFigureWidthSpinner, fDefaultArchimateFigureHeightSpinner;
    
    private Button[] fWordWrapStyleButtons;
    
    private String[] WORD_WRAP_STYLES = {
            Messages.DiagramAppearancePreferenceTab_4,
            Messages.DiagramAppearancePreferenceTab_5,
            Messages.DiagramAppearancePreferenceTab_6
    };
    
    private Combo fDefaultSketchBackgroundCombo;
    
    
    public Composite createContents(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // -------------- Figures ----------------------------
        
        Group figuresGroup = new Group(client, SWT.NULL);
        figuresGroup.setText(Messages.DiagramAppearancePreferenceTab_0);
        figuresGroup.setLayout(new GridLayout());
        figuresGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Gradient
        fShowGradientButton = new Button(figuresGroup, SWT.CHECK);
        fShowGradientButton.setText(Messages.DiagramFiguresPreferencePage_9);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fShowGradientButton.setLayoutData(gd);
        
        // Default sizes
        Label label = new Label(figuresGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_1);
        
        Composite sizeGroup = new Composite(figuresGroup, SWT.NULL);
        sizeGroup.setLayout(new GridLayout(4, false));
        sizeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        label = new Label(sizeGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_2);
        fDefaultArchimateFigureWidthSpinner = new Spinner(sizeGroup, SWT.BORDER);
        fDefaultArchimateFigureWidthSpinner.setMinimum(30);
        fDefaultArchimateFigureWidthSpinner.setMaximum(300);
        
        label = new Label(sizeGroup, SWT.NULL);
        label.setText(Messages.DiagramAppearancePreferenceTab_3);
        fDefaultArchimateFigureHeightSpinner = new Spinner(sizeGroup, SWT.BORDER);
        fDefaultArchimateFigureHeightSpinner.setMinimum(30);
        fDefaultArchimateFigureHeightSpinner.setMaximum(300);
        
        // Word wrap style
        Group figuresWordWrapStyleGroup = new Group(figuresGroup, SWT.NULL);
        figuresWordWrapStyleGroup.setText(Messages.DiagramAppearancePreferenceTab_7);
        figuresWordWrapStyleGroup.setLayout(new GridLayout());
        figuresWordWrapStyleGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fWordWrapStyleButtons = new Button[3];
        for(int i = 0; i < fWordWrapStyleButtons.length; i++) {
            fWordWrapStyleButtons[i] = new Button(figuresWordWrapStyleGroup, SWT.RADIO);
            fWordWrapStyleButtons[i].setText(WORD_WRAP_STYLES[i]);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            fWordWrapStyleButtons[i].setLayoutData(gd);
        }
        
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
        fShowGradientButton.setSelection(getPreferenceStore().getBoolean(SHOW_GRADIENT));
        
        fDefaultArchimateFigureWidthSpinner.setSelection(getPreferenceStore().getInt(DEFAULT_ARCHIMATE_FIGURE_WIDTH));
        fDefaultArchimateFigureHeightSpinner.setSelection(getPreferenceStore().getInt(DEFAULT_ARCHIMATE_FIGURE_HEIGHT));
        
        for(int i = 0; i < fWordWrapStyleButtons.length; i++) {
            fWordWrapStyleButtons[i].setSelection(getPreferenceStore().getInt(ARCHIMATE_FIGURE_WORD_WRAP_STYLE) == i);
        }
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getInt(SKETCH_DEFAULT_BACKGROUND));        
    }
    
    private IPreferenceStore getPreferenceStore() {
        return Preferences.STORE;
    }

    public boolean performOk() {
        getPreferenceStore().setValue(SHOW_GRADIENT, fShowGradientButton.getSelection());
        
        getPreferenceStore().setValue(DEFAULT_ARCHIMATE_FIGURE_WIDTH, fDefaultArchimateFigureWidthSpinner.getSelection());
        getPreferenceStore().setValue(DEFAULT_ARCHIMATE_FIGURE_HEIGHT, fDefaultArchimateFigureHeightSpinner.getSelection());
        
        for(int i = 0; i < fWordWrapStyleButtons.length; i++) {
            if(fWordWrapStyleButtons[i].getSelection()) {
                getPreferenceStore().setValue(ARCHIMATE_FIGURE_WORD_WRAP_STYLE, i);
            }
        }
        
        getPreferenceStore().setValue(SKETCH_DEFAULT_BACKGROUND, fDefaultSketchBackgroundCombo.getSelectionIndex());
        
        return true;
    }
    
    protected void performDefaults() {
        fShowGradientButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_GRADIENT));
        
        fDefaultArchimateFigureWidthSpinner.setSelection(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_WIDTH));
        fDefaultArchimateFigureHeightSpinner.setSelection(getPreferenceStore().getDefaultInt(DEFAULT_ARCHIMATE_FIGURE_HEIGHT));
        
        for(int i = 0; i < fWordWrapStyleButtons.length; i++) {
            fWordWrapStyleButtons[i].setSelection(getPreferenceStore().getDefaultInt(ARCHIMATE_FIGURE_WORD_WRAP_STYLE) == i);
        }
        
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getDefaultInt(SKETCH_DEFAULT_BACKGROUND));
    }
}