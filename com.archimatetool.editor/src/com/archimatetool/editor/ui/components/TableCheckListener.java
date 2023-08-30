/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Listen to clicking on the left side of a table row if we clicked on a check mark
 * 
 * @author Phillip Beauvoir
 */
public abstract class TableCheckListener {
    public TableCheckListener(final Table table) {
        table.addListener(SWT.MouseDown, e -> {
            // Get Table item
            Point pt = new Point(e.x, e.y);
            TableItem item = table.getItem(pt);
            if(item != null && item.getData() != null) {
                Rectangle rect = item.getBounds(0);
                rect.width = 18;
                rect.x -= 2;
                if(rect.contains(pt)) {
                    hit(item.getData());
                }
            }
        });
    }
    
    /**
     * The table received a hit on the check mark
     * @param object The object checked
     */
    protected abstract void hit(Object object);
}