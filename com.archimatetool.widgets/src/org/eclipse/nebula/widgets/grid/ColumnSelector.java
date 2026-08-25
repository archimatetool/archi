/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.eclipse.nebula.widgets.grid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

/**
 * Provides multi-column selection capabilities for the Eclipse Nebula {@link Grid} widget.
 * 
 * <p>By default, the Nebula Grid natively supports complex row and cell selections, 
 * but lacks built-in keyboard-modifier support (Shift/Ctrl/Cmd) for selecting entire columns 
 * via header clicks. This utility bridges that gap by tracking system modifier keys and 
 * manipulating cell-state selections dynamically.</p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><b>Single Selection:</b> Clicking a column header selects that column and clears others.</li>
 *   <li><b>Toggle Selection (Mod1):</b> Holding Ctrl (Windows/Linux) or Command (macOS) toggles individual columns on/off.</li>
 *   <li><b>Range Selection (Shift):</b> Holding Shift selects a continuous block of columns from the last selected index.</li>
 * </ul>
 * 
 * <h3>Prerequisites:</h3>
 * <p>The target {@code Grid} instance must have cell selection enabled prior to use:</p>
 * <pre>{@code
 * grid.setCellSelectionEnabled(true);
 * }</pre>
 * <p>The target {@code Grid} instance must have created its columns prior to use.</p>
 * 
 * @see org.eclipse.nebula.widgets.grid.Grid
 * @see org.eclipse.nebula.widgets.grid.GridColumn
 * 
 * @author Phillip Beauvoir
 */
public class ColumnSelector {
    private final Grid grid;
    
    private boolean modKeyPressed;
    private boolean shiftKeyPressed;
    private int lastColumnSelected;
    
    /**
     * Install the Grid's columns onto this ColumnSelector. The columns must be
     * created before creating a new ColumnSelector.
     * @param grid The Grid
     * @return A new ColumnSelector
     */
    public static ColumnSelector install(Grid grid) {
        return new ColumnSelector(grid);
    }
    
    private ColumnSelector(Grid grid) {
        this.grid = grid;
        
        // Capture key state on Grid mouse down
        grid.addListener(SWT.MouseDown, e -> {
            modKeyPressed = (e.stateMask & SWT.MOD1) == SWT.MOD1;
            shiftKeyPressed = (e.stateMask & SWT.MOD2) == SWT.MOD2;
        });
        
        // Add a column selection listener for each column
        for(GridColumn column : grid.getColumns()) {
            column.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
                int columnIndex = grid.indexOf(column);
                
                // Shift key down
                if(shiftKeyPressed) {
                    int start = Math.min(lastColumnSelected, columnIndex);
                    int end = Math.max(lastColumnSelected, columnIndex);
                    for(int i = start; i <= end; i++) {
                        selectColumn(i);
                    }
                }
                // Mod key down
                else if(modKeyPressed) {
                    if(hasColumnSelectedCells(columnIndex)) {
                        deselectColumn(columnIndex);
                    }
                    else {
                        selectColumn(columnIndex);
                    }
                }
                // No key down
                else {
                    grid.deselectAll();
                    selectColumn(columnIndex);
                }
                
                lastColumnSelected = columnIndex;
            }));
        }
    }
    
    /**
     * Select a column by index
     * @param columnIndex the column index
     */
    public void selectColumn(int columnIndex) {
        grid.selectColumn(columnIndex);
    }
    
    /**
     * Deselect a column by index
     * @param columnIndex the column index
     */
    public void deselectColumn(int columnIndex) {
        Point[] cells = new Point[grid.getItemCount()];
        for(int i = 0; i < grid.getItemCount(); i++) {
            cells[i] = new Point(columnIndex, i);
        }
        grid.deselectCells(cells);
    }
    
    // Return true if column at index has any selected cells
    private boolean hasColumnSelectedCells(int columnIndex) {
        for(Point cell : grid.getCellSelection()) {
            if(cell.x == columnIndex) {
                return true;
            }
        }
        
        return false;
    }
}
