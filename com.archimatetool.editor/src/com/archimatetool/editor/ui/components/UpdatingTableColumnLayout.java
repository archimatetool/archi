/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * TableColumnLayout with public method so we can re-layout when the host table adds/removes vertical scroll bar
 * It's a kludge to stop a bogus horizontal scroll bar being shown.
 * 
 * @author Phillip Beauvoir
 */
public class UpdatingTableColumnLayout extends TableColumnLayout {

    private Composite fParent;

    public UpdatingTableColumnLayout(Composite parent) {
        fParent = parent;
    }

    /**
     * This is the method to call after setting the input on a table
     */
    public void doRelayout() {
        layout(fParent, true);
    }
}