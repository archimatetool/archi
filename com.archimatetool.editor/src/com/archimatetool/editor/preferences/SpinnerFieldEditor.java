/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

/**
 * Field Editor Spinner
 * 
 * @author Phillip Beauvoir
 */
public class SpinnerFieldEditor extends FieldEditor {

    private int fMin;
    private int fMax;
    private Spinner fSpinner;

    public SpinnerFieldEditor() {
    }

    public SpinnerFieldEditor(String preferenceName, String labelText, int min, int max, Composite parent) {
        fMin = min;
        fMax = max;
        init(preferenceName, labelText);
        createControl(parent);
    }

    @Override
    protected void adjustForNumColumns(int numColumns) {
    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        getLabelControl(parent);
        getSpinnerControl(parent);
    }


    private Spinner getSpinnerControl(Composite parent) {
        if(fSpinner == null) {
            fSpinner = new Spinner(parent, SWT.BORDER);
            fSpinner.setMinimum(fMin);
            fSpinner.setMaximum(fMax);
        }
        return fSpinner;
    }

    @Override
    protected void doLoad() {
        fSpinner.setSelection(getPreferenceStore().getInt(getPreferenceName()));
    }

    @Override
    protected void doLoadDefault() {
        fSpinner.setSelection(getPreferenceStore().getDefaultInt(getPreferenceName()));
    }

    @Override
    protected void doStore() {
        getPreferenceStore().setValue(getPreferenceName(), fSpinner.getSelection());
    }

    @Override
    public int getNumberOfControls() {
        return 2;
    }
}