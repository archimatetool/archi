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

import com.archimatetool.editor.diagram.sketch.ISketchEditor;


/**
 * Diagram Appearance Preferences Tab panel
 * 
 * @author Phillip Beauvoir
 */
public class DiagramAppearancePreferenceTab implements IPreferenceConstants {
    
    private Button fShowShadowsButton;
    
    private Combo fDefaultSketchBackgroundCombo;
    
    
    public Composite createContents(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // -------------- Figures ----------------------------
        
        Group figuresGroup = new Group(client, SWT.NULL);
        figuresGroup.setText(Messages.DiagramAppearancePreferenceTab_0);
        figuresGroup.setLayout(new GridLayout());
        figuresGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fShowShadowsButton = new Button(figuresGroup, SWT.CHECK);
        fShowShadowsButton.setText(Messages.DiagramFiguresPreferencePage_9);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fShowShadowsButton.setLayoutData(gd);
        
        // -------------- Sketch ----------------------------

        Group sketchGroup = new Group(client, SWT.NULL);
        sketchGroup.setLayout(new GridLayout(2, false));
        sketchGroup.setText(Messages.DiagramPreferencePage_19);
        sketchGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Default Sketch background
        Label label = new Label(sketchGroup, SWT.NULL);
        label.setText(Messages.DiagramPreferencePage_20);
        fDefaultSketchBackgroundCombo = new Combo(sketchGroup, SWT.READ_ONLY);
        fDefaultSketchBackgroundCombo.setItems(ISketchEditor.BACKGROUNDS);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fDefaultSketchBackgroundCombo.setLayoutData(gd);

        setValues();

        return client;
    }
    
    
    private void setValues() {
        fShowShadowsButton.setSelection(getPreferenceStore().getBoolean(SHOW_SHADOWS));
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getInt(SKETCH_DEFAULT_BACKGROUND));        
    }
    
    private IPreferenceStore getPreferenceStore() {
        return Preferences.STORE;
    }

    public boolean performOk() {
        getPreferenceStore().setValue(SHOW_SHADOWS, fShowShadowsButton.getSelection());
        getPreferenceStore().setValue(SKETCH_DEFAULT_BACKGROUND, fDefaultSketchBackgroundCombo.getSelectionIndex());
        
        return true;
    }
    
    protected void performDefaults() {
        fShowShadowsButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_SHADOWS));
        fDefaultSketchBackgroundCombo.select(getPreferenceStore().getDefaultInt(SKETCH_DEFAULT_BACKGROUND));
    }
}