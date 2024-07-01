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

import com.archimatetool.editor.ui.UIUtils;


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
        setVisibleItemCount(12);
        
        // Filter out bad characters
        UIUtils.applyInvalidCharacterFilter(getControl());
        UIUtils.applyNewlineFilter(getControl());
        
        // Mac bug workaround
        UIUtils.applyMacUndoBugFilter(getControl());
    }

    public void setVisibleItemCount(int count) {
        getControl().setVisibleItemCount(count);
    }
    
    @Override
    public CCombo getControl() {
        return (CCombo)super.getControl();
    }
    
    @Override
    protected Object doGetValue() {
        return getControl().getText();
    }
    
    @Override
    protected void doSetValue(Object value) {
        Assert.isTrue(value instanceof String);
        getControl().setText((String)value);
    }
    
    public void setEditable(boolean val) {
        getControl().setEditable(val);
    }
    
    @Override
    public void activate() {
        // Set font from parent in case user changed it in preferences
        getControl().setFont(getControl().getParent().getFont());
    }
}
