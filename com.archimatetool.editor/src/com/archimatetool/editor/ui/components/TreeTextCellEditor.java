/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Tree Text CellEditor
 * 
 * @author Phillip Beauvoir
 */
public class TreeTextCellEditor extends TextCellEditor {
    
    private GlobalActionDisablementHandler globalActionHandler;

    public TreeTextCellEditor(Tree tree) {
        super(tree, SWT.BORDER);
        Text txt = (Text)getControl();
        
        // Filter out nasties
        UIUtils.applyInvalidCharacterFilter(txt);
        UIUtils.conformSingleTextControl(txt);
    }

    @Override
    public LayoutData getLayoutData() {
        Text txt = (Text)getControl();
        LayoutData data = super.getLayoutData();
        // Because we use SWT.BORDER this creates insets in the text control that varies according to OS
        data.minimumHeight = txt.getLineHeight() + (PlatformUtils.isLinux() ? 10 :  4);
        return data;
    }
    
    @Override
    public void activate() {
        // Clear global key binds
        globalActionHandler = new GlobalActionDisablementHandler();
        globalActionHandler.clearGlobalActions();
    }
    
    @Override
    public void deactivate() {
        super.deactivate();
        
        // Restore global key binds
        if(globalActionHandler != null) {
            globalActionHandler.restoreGlobalActions();
            globalActionHandler = null;
        }
    }
}
