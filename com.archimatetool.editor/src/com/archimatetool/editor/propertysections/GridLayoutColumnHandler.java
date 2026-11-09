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
import org.eclipse.swt.widgets.Control;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;

/**
 * Handler that adjusts layout to show single (compact) or multi-column depending on user preference
 * 
 * @author Phillip Beauvoir
 */
public class GridLayoutColumnHandler {
    
    /**
     * @deprecated Use GridLayoutColumnHandler(parent).expandedColumns(columns);
     */
    public static GridLayoutColumnHandler create(Composite parent, int maxColumns) {
        return new GridLayoutColumnHandler(parent).expandedColumns(maxColumns);
    }
    
    /**
     * Factory method for new GridLayoutColumnHandler
     * @param parent The parent Composite
     * @return a new GridLayoutColumnHandler
     * @since 5.11
     */
    public static GridLayoutColumnHandler create(Composite parent) {
        return new GridLayoutColumnHandler(parent);
    }
    
    private int compactColumns = 1;
    private int expandedColumns = 2;
    private int columnWidth = ITabbedLayoutConstants.COMPOSITE_WIDTH;
    
    private Composite parent;
    
    private IPropertyChangeListener listener = event -> {
        if(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN.equals(event.getProperty())) {
            updateColumns();
            parent.requestLayout();
        }
    };
    
    private GridLayoutColumnHandler(Composite parent) {
        this.parent = parent;
        
        parent.addDisposeListener(e -> {
            ArchiPlugin.getInstance().getPreferenceStore().removePropertyChangeListener(listener);
            this.parent = null;
        });
        
        ArchiPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(listener);
    }
    
    /**
     * @param compactColumns the number of GridLayout columns to use when single column (compact) mode is on. Default is 1.
     * @since 5.11.0
     */
    public GridLayoutColumnHandler compactColumns(int compactColumns) {
        this.compactColumns = compactColumns;
        return this;
    }
    
    /**
     * @param expandedColumns the number of GridLayout columns to use when single column (compact) mode is off. Default is 2.
     * @since 5.11.0
     */
    public GridLayoutColumnHandler expandedColumns(int expandedColumns) {
        this.expandedColumns = expandedColumns;
        return this;
    }
    
    /**
     * @param width The column width to use for all child controls when there is more than one column.
     * Can be set to SWT.DEFAULT for default widths.
     * Default is ITabbedLayoutConstants.COMPOSITE_WIDTH.
     * @since 5.11.0
     */
    public GridLayoutColumnHandler columnWidth(int width) {
        columnWidth = width;
        return this;
    }
    
    public void updateColumns() {
        // Set Grid Layout columns
        if(!(parent.getLayout() instanceof GridLayout gridLayout)) {
            return;
        }
        
        int numColumns = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN) ? compactColumns : expandedColumns;
        gridLayout.numColumns = numColumns;

        // If columnWidth is set, set each child control's GridData widthHint to columnWidth except the last one.
        // The last column should be set to default width so there is no excess padding on the right.
        // If numColumns == 1 set to SWT.DEFAULT
        if(columnWidth != SWT.DEFAULT) {
            Control[] children = parent.getChildren();
            
            for(int i = 0; i < children.length; i++) {
                if(children[i].getLayoutData() instanceof GridData gridData) {
                    int width = SWT.DEFAULT; // Have to set this to default in case the layout is changed from multi-column to single
                    if(numColumns > 1 && i < children.length - 1) {
                        width = columnWidth;
                    }
                    gridData.widthHint = width;
                }
            }
        }
    }
}
