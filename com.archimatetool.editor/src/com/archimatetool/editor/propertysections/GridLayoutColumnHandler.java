/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
    
    IPropertyChangeListener listener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN.equals(event.getProperty())) {
                updateColumns();
                parent.requestLayout();
            }
        }
    };
    
    private GridLayoutColumnHandler(Composite parent, int maxColumns) {
        this.parent = parent;
        this.maxColumns = maxColumns;
        
        parent.addDisposeListener((e) -> {
            ArchiPlugin.PREFERENCES.removePropertyChangeListener(listener);
        });
        
        ArchiPlugin.PREFERENCES.addPropertyChangeListener(listener);
    }
    
    public void updateColumns() {
        // Set Grid Layout columns
        int numColumns = ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN) ? 1 : maxColumns;
        ((GridLayout)parent.getLayout()).numColumns = numColumns;
        
        // If there is more than one child composite set the width hint for the first child for the number of columns
        if(parent.getChildren().length > 1) {
            GridData gd = (GridData)parent.getChildren()[0].getLayoutData();
            gd.widthHint = numColumns == 1 ? SWT.DEFAULT : ITabbedLayoutConstants.COMPOSITE_WIDTH;
        }
    }
}
