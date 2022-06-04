/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;


/**
 * String ComboBox CellEditor
 * Uses Strings as input rather than Integer index
 * 
 * @author Phillip Beauvoir
 */
public class StringComboBoxCellEditor extends ComboBoxCellEditor {

    public StringComboBoxCellEditor(Composite parent, String[] items, boolean editable) {
        super(parent, items);
        setEditable(editable);
        ((CCombo)getControl()).setVisibleItemCount(12);
    }

    @Override
    protected Object doGetValue() {
        CCombo combobox = (CCombo)getControl();
        return combobox.getText();
    }
    
    @Override
    protected void doSetValue(Object value) {
        CCombo combobox = (CCombo)getControl();
        Assert.isTrue(value instanceof String);
        combobox.setText((String)value);
    }
    
    public void setEditable(boolean val) {
        CCombo combobox = (CCombo)getControl();
        combobox.setEditable(val);
    }
    
    @Override
    public void activate() {
        // Set font from parent in case user changed it in preferences
        getControl().setFont(getControl().getParent().getFont());
    }
}
