/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;

/**
 * Handler that adjusts layout to show single or multi-column depending on user preference
 * 
 * @author Phillip Beauvoir
 */
public class GridLayoutColumnHandler {
    
    public static GridLayoutColumnHandler create(Composite parent, int maxColumns) {
        return new GridLayoutColumnHandler(parent, maxColumns);
    }
    
    private int maxColumns;
    private Composite parent;
    
    IPropertyChangeListener listener = event -> {
        if(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN.equals(event.getProperty())) {
            updateColumns();
            parent.requestLayout();
        }
    };
    
    private GridLayoutColumnHandler(Composite parent, int maxColumns) {
        this.parent = parent;
        this.maxColumns = maxColumns;
        
        parent.addDisposeListener(e -> {
            ArchiPlugin.getInstance().getPreferenceStore().removePropertyChangeListener(listener);
            this.parent = null;
        });
        
        ArchiPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(listener);
    }
    
    public void updateColumns() {
        // Set Grid Layout columns
        int numColumns = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN) ? 1 : maxColumns;
        ((GridLayout)parent.getLayout()).numColumns = numColumns;
        
        // If numColumns == 1 set width of each child control to SWT.DEFAULT
        // Else set all child controls except the last one to ITabbedLayoutConstants.COMPOSITE_WIDTH
        for(int i = 0; i < parent.getChildren().length; i++) {
            int width = numColumns == 1 ? SWT.DEFAULT : i < parent.getChildren().length - 1  ? ITabbedLayoutConstants.COMPOSITE_WIDTH : SWT.DEFAULT;
            ((GridData)parent.getChildren()[i].getLayoutData()).widthHint = width;
        }
    }
}
