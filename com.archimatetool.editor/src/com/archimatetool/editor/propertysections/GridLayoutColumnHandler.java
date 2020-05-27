/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;

/**
 * Handler that adjusts layout to show single or multi-column depending on user preference
 * 
 * @author Phillip Beauvoir
 */
public class GridLayoutColumnHandler {
    
    public static void create(Composite parent, int maxColumns) {
        new GridLayoutColumnHandler(parent, maxColumns).updateColumns();
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
            Preferences.STORE.removePropertyChangeListener(listener);
        });
        
        Preferences.STORE.addPropertyChangeListener(listener);
    }
    
    private void updateColumns() {
        ((GridLayout)parent.getLayout()).numColumns = Preferences.STORE.getBoolean(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN) ? 1 : maxColumns;
    }
}
