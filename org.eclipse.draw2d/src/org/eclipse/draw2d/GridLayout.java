/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Asim Ullah - Ported for use in draw2d (c.f Bugzilla 71684). [Sep 10, 2004]
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Lays out children into a Grid arrangement in which overall aligment and
 * spacing can be configured, as well as specific layout requirements for the
 * each individual member of the GridLayout. This layout is a Draw2D port of the
 * swt GridLayout.
 * 
 * <code>GridLayout</code> has a number of configuration fields, and the Figures
 * it lays out can have an associated layout data object, called
 * <code>GridData</code> (similar to the SWT GridData object). The power of
 * <code>GridLayout</code> lies in the ability to configure
 * <code>GridData</code> for each Figure in the layout.
 * <p>
 * The following code creates a container Figure managed by a
 * <code>GridLayout</code> with 2 columns, containing 3 RectangleFigure shapes,
 * the last of which has been given further layout instructions. Note that it is
 * the <code>GridLayout</code> method <code>setConstraint</code> that binds the
 * child <code>Figure</code> to its layout <code>GridData</code> object.
 * 
 * <pre>
 * Figure container = new Figure();
 * GridLayout gridLayout = new GridLayout();
 * gridLayout.numColumns = 2;
 * container.setLayout(gridLayout);
 * 
 * Shape rect;
 * rect = new RectangleFigure();
 * container.add(rect);
 * 
 * rect = new RectangleFigure();
 * container.add(rect);
 * 
 * rect = new RectangleFigure();
 * GridData gridData = new GridData();
 * gridData.widthHint = 150;
 * layout.setConstraint(rect, gridData);
 * 
 * container.add(rect);
 * </pre>
 * 
 * <p>
 * The <code>numColumns</code> field is the most important field in a
 * <code>GridLayout</code>. Widgets are laid out in columns from left to right,
 * and a new row is created when <code>numColumns</code>+ 1 figures are added to
 * the <code>Figure<code> parent container.
 * 
 * @see GridData
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GridLayout extends AbstractHintLayout {

    /**
     * numColumns specifies the number of cell columns in the layout.
     * 
     * The default value is 1.
     */
    public int numColumns = 1;

    /**
     * makeColumnsEqualWidth specifies whether all columns in the layout will be
     * forced to have the same width.
     * 
     * The default value is false.
     */
    public boolean makeColumnsEqualWidth = false;

    /**
     * marginWidth specifies the number of pixels of horizontal margin that will
     * be placed along the left and right edges of the layout.
     * 
     * The default value is 5.
     */
    public int marginWidth = 5;

    /**
     * marginHeight specifies the number of pixels of vertical margin that will
     * be placed along the top and bottom edges of the layout.
     * 
     * The default value is 5.
     */
    public int marginHeight = 5;

    /**
     * horizontalSpacing specifies the number of pixels between the right edge
     * of one cell and the left edge of its neighbouring cell to the right.
     * 
     * The default value is 5.
     */
    public int horizontalSpacing = 5;

    /**
     * verticalSpacing specifies the number of pixels between the bottom edge of
     * one cell and the top edge of its neighbouring cell underneath.
     * 
     * The default value is 5.
     */
    public int verticalSpacing = 5;

    /** The layout contraints */
    protected Map constraints = new HashMap();

    /**
     * Default Constructor
     */
    public GridLayout() {
        super();
    }

    /**
     * Constructs a new instance of this class given the number of columns, and
     * whether or not the columns should be forced to have the same width.
     * 
     * @param numColumns
     *            the number of columns in the grid
     * @param makeColumnsEqualWidth
     *            whether or not the columns will have equal width
     * 
     */
    public GridLayout(int numColumns, boolean makeColumnsEqualWidth) {
        this.numColumns = numColumns;
        this.makeColumnsEqualWidth = makeColumnsEqualWidth;
    }

    /**
     * @param child
     * @param wHint
     * @param hHint
     * @return the child size.
     */
    protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
        return child.getPreferredSize(wHint, hHint);
    }

    GridData getData(IFigure[][] grid, int row, int column, int rowCount,
            int columnCount, boolean first) {
        IFigure figure = grid[row][column];
        if (figure != null) {
            GridData data = (GridData) getConstraint(figure);
            int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
            int vSpan = Math.max(1, data.verticalSpan);
            int i = first ? row + vSpan - 1 : row - vSpan + 1;
            int j = first ? column + hSpan - 1 : column - hSpan + 1;
            if (0 <= i && i < rowCount) {
                if (0 <= j && j < columnCount) {
                    if (figure == grid[i][j])
                        return data;
                }
            }
        }
        return null;
    }

    void initChildren(IFigure container) {
        List children = container.getChildren();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            if (child.getLayoutManager() == null)
                child.setLayoutManager(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.
     * draw2d.IFigure, int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint,
            int hHint) {
        // Remove the size of the border from the wHint and hHint in case a size
        // different to the preferred size is used
        int borderWidth = container.getInsets().getWidth();
        int borderHeight = container.getInsets().getHeight();

        if (wHint != SWT.DEFAULT)
            wHint -= borderWidth;
        if (hHint != SWT.DEFAULT)
            hHint -= borderHeight;

        Dimension size = layout(container, false, 0, 0, wHint, hHint, /* flushCache */
                true);
        if (wHint != SWT.DEFAULT)
            size.width = wHint;
        if (hHint != SWT.DEFAULT)
            size.height = hHint;

        // Add the size of the border again before returning the calculated size
        size.expand(borderWidth, borderHeight);

        // the border preferred size might enlarge the size (in most cases it
        // should be empty)
        size.union(getBorderPreferredSize(container));

        return size;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void layout(IFigure container) {
        // initChildren( container);
        Rectangle rect = container.getClientArea();
        layout(container, true, rect.x, rect.y, rect.width, rect.height, /* flushCache */
                true);

    }

    @SuppressWarnings("null")
    Dimension layout(IFigure container, boolean move, int x, int y, int width,
            int height, boolean flushCache) {
        if (numColumns < 1)
            return new Dimension(marginWidth * 2, marginHeight * 2);
        List children = container.getChildren();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);

            GridData data = (GridData) getConstraint(child);
            if (data == null)
                setConstraint(child, data = new GridData());
            if (flushCache)
                data.flushCache();
            data.computeSize(child, flushCache);
        }

        /* Build the grid */
        int row = 0, column = 0, rowCount = 0, columnCount = numColumns;
        IFigure[][] grid = new IFigure[4][columnCount];
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            GridData data = (GridData) getConstraint(child);
            int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
            int vSpan = Math.max(1, data.verticalSpan);
            while (true) {
                int lastRow = row + vSpan;
                if (lastRow >= grid.length) {
                    IFigure[][] newGrid = new IFigure[lastRow + 4][columnCount];
                    System.arraycopy(grid, 0, newGrid, 0, grid.length);
                    grid = newGrid;
                }
                if (grid[row] == null) {
                    grid[row] = new IFigure[columnCount];
                }
                while (column < columnCount && grid[row][column] != null) {
                    column++;
                }
                int endCount = column + hSpan;
                if (endCount <= columnCount) {
                    int index = column;
                    while (index < endCount && grid[row][index] == null) {
                        index++;
                    }
                    if (index == endCount)
                        break;
                    column = index;
                }
                if (column + hSpan >= columnCount) {
                    column = 0;
                    row++;
                }
            }
            for (int j = 0; j < vSpan; j++) {
                if (grid[row + j] == null) {
                    grid[row + j] = new IFigure[columnCount];
                }
                for (int k = 0; k < hSpan; k++) {
                    grid[row + j][column + k] = child;
                }
            }
            rowCount = Math.max(rowCount, row + vSpan);
            column += hSpan;
        }

        /* Column widths */
        int availableWidth = width - horizontalSpacing * (columnCount - 1)
                - marginWidth * 2;
        int expandCount = 0;
        int[] widths = new int[columnCount];
        int[] minWidths = new int[columnCount];
        boolean[] expandColumn = new boolean[columnCount];
        for (int j = 0; j < columnCount; j++) {
            for (int i = 0; i < rowCount; i++) {
                GridData data = getData(grid, i, j, rowCount, columnCount, true);
                if (data != null) {
                    int hSpan = Math.max(1,
                            Math.min(data.horizontalSpan, columnCount));
                    if (hSpan == 1) {
                        int w = data.cacheWidth + data.horizontalIndent;
                        widths[j] = Math.max(widths[j], w);
                        if (data.grabExcessHorizontalSpace) {
                            if (!expandColumn[j])
                                expandCount++;
                            expandColumn[j] = true;
                        }
                        if (data.widthHint != SWT.DEFAULT
                                || !data.grabExcessHorizontalSpace) {
                            minWidths[j] = Math.max(minWidths[j], w);
                        }
                    }
                }
            }
            for (int i = 0; i < rowCount; i++) {
                GridData data = getData(grid, i, j, rowCount, columnCount,
                        false);
                if (data != null) {
                    int hSpan = Math.max(1,
                            Math.min(data.horizontalSpan, columnCount));
                    if (hSpan > 1) {
                        int spanWidth = 0, spanMinWidth = 0, spanExpandCount = 0;
                        for (int k = 0; k < hSpan; k++) {
                            spanWidth += widths[j - k];
                            spanMinWidth += minWidths[j - k];
                            if (expandColumn[j - k])
                                spanExpandCount++;
                        }
                        if (data.grabExcessHorizontalSpace
                                && spanExpandCount == 0) {
                            expandCount++;
                            expandColumn[j] = true;
                        }
                        int w = data.cacheWidth + data.horizontalIndent
                                - spanWidth - (hSpan - 1) * horizontalSpacing;
                        if (w > 0) {
                            if (spanExpandCount == 0) {
                                widths[j] += w;
                            } else {
                                int delta = w / spanExpandCount;
                                int remainder = w % spanExpandCount, last = -1;
                                for (int k = 0; k < hSpan; k++) {
                                    if (expandColumn[j - k]) {
                                        widths[last = j - k] += delta;
                                    }
                                }
                                if (last > -1)
                                    widths[last] += remainder;
                            }
                        }
                        if (data.widthHint != SWT.DEFAULT
                                || !data.grabExcessHorizontalSpace) {
                            w = data.cacheWidth + data.horizontalIndent
                                    - spanMinWidth - (hSpan - 1)
                                    * horizontalSpacing;
                            if (w > 0) {
                                if (spanExpandCount == 0) {
                                    minWidths[j] += w;
                                } else {
                                    int delta = w / spanExpandCount;
                                    int remainder = w % spanExpandCount, last = -1;
                                    for (int k = 0; k < hSpan; k++) {
                                        if (expandColumn[j - k]) {
                                            minWidths[last = j - k] += delta;
                                        }
                                    }
                                    if (last > -1)
                                        minWidths[last] += remainder;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (makeColumnsEqualWidth) {
            int minColumnWidth = 0;
            int columnWidth = 0;
            for (int i = 0; i < columnCount; i++) {
                minColumnWidth = Math.max(minColumnWidth, minWidths[i]);
                columnWidth = Math.max(columnWidth, widths[i]);
            }
            columnWidth = width == SWT.DEFAULT || expandCount == 0 ? columnWidth
                    : Math.max(minColumnWidth, availableWidth / columnCount);
            for (int i = 0; i < columnCount; i++) {
                expandColumn[i] = expandCount > 0;
                widths[i] = columnWidth;
            }
        } else {
            if (width != SWT.DEFAULT && expandCount > 0) {
                int totalWidth = 0;
                for (int i = 0; i < columnCount; i++) {
                    totalWidth += widths[i];
                }
                int count = expandCount;
                int delta = (availableWidth - totalWidth) / count;
                int remainder = (availableWidth - totalWidth) % count;
                int last = -1;
                while (totalWidth != availableWidth) {
                    for (int j = 0; j < columnCount; j++) {
                        if (expandColumn[j]) {
                            if (widths[j] + delta > minWidths[j]) {
                                widths[last = j] = widths[j] + delta;
                            } else {
                                widths[j] = minWidths[j];
                                expandColumn[j] = false;
                                count--;
                            }
                        }
                    }
                    if (last > -1)
                        widths[last] += remainder;

                    for (int j = 0; j < columnCount; j++) {
                        for (int i = 0; i < rowCount; i++) {
                            GridData data = getData(grid, i, j, rowCount,
                                    columnCount, false);
                            if (data != null) {
                                int hSpan = Math.max(1, Math.min(
                                        data.horizontalSpan, columnCount));
                                if (hSpan > 1) {
                                    if (data.widthHint != SWT.DEFAULT
                                            || !data.grabExcessHorizontalSpace) {
                                        int spanWidth = 0, spanExpandCount = 0;
                                        for (int k = 0; k < hSpan; k++) {
                                            spanWidth += widths[j - k];
                                            if (expandColumn[j - k])
                                                spanExpandCount++;
                                        }
                                        int w = data.cacheWidth
                                                + data.horizontalIndent
                                                - spanWidth - (hSpan - 1)
                                                * horizontalSpacing;
                                        if (w > 0) {
                                            if (spanExpandCount == 0) {
                                                widths[j] += w;
                                            } else {
                                                int delta2 = w
                                                        / spanExpandCount;
                                                int remainder2 = w
                                                        % spanExpandCount, last2 = -1;
                                                for (int k = 0; k < hSpan; k++) {
                                                    if (expandColumn[j - k]) {
                                                        widths[last2 = j - k] += delta2;
                                                    }
                                                }
                                                if (last2 > -1)
                                                    widths[last2] += remainder2;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (count == 0)
                        break;
                    totalWidth = 0;
                    for (int i = 0; i < columnCount; i++) {
                        totalWidth += widths[i];
                    }
                    delta = (availableWidth - totalWidth) / count;
                    remainder = (availableWidth - totalWidth) % count;
                    last = -1;
                }
            }
        }

        /* Wrapping */
        GridData[] flush = null;
        int flushLength = 0;
        if (width != SWT.DEFAULT) {
            for (int j = 0; j < columnCount; j++) {
                for (int i = 0; i < rowCount; i++) {
                    GridData data = getData(grid, i, j, rowCount, columnCount,
                            false);
                    if (data != null) {
                        if (data.heightHint == SWT.DEFAULT) {
                            IFigure child = grid[i][j];
                            // TEMPORARY CODE
                            int hSpan = Math.max(1,
                                    Math.min(data.horizontalSpan, columnCount));
                            int currentWidth = 0;
                            for (int k = 0; k < hSpan; k++) {
                                currentWidth += widths[j - k];
                            }
                            currentWidth += (hSpan - 1) * horizontalSpacing
                                    - data.horizontalIndent;
                            if ((currentWidth != data.cacheWidth && data.horizontalAlignment == SWT.FILL)
                                    || (data.cacheWidth > currentWidth)) {
                                int trim = 0;
                                /*
                                 * // *Note*: Left this in place from SWT //
                                 * GridLayout. Not sure if Draw2D Borders or //
                                 * Scrollbars 'trim' will need to be takeninto
                                 * account.
                                 * 
                                 * if (child instanceof Group) { Group g
                                 * =(Group)child; trim = g.getSize ().x -
                                 * g.getClientArea ().width; } else if (child
                                 * instanceof Scrollable) { Rectangle rect =
                                 * ((Scrollable) child).computeTrim (0, 0, 0,0);
                                 * trim = rect.width; } else { trim =
                                 * child.getBorderWidth () * 2; }
                                 */
                                int oldWidthHint = data.widthHint;
                                data.widthHint = Math.max(0, currentWidth
                                        - trim);
                                data.cacheWidth = data.cacheHeight = SWT.DEFAULT;
                                data.computeSize(child, false);
                                data.widthHint = oldWidthHint;
                                if (flush == null)
                                    flush = new GridData[children.size()];
                                flush[flushLength++] = data;
                            }
                        }
                    }
                }
            }
        }

        /* Row heights */
        int availableHeight = height - verticalSpacing * (rowCount - 1)
                - marginHeight * 2;
        expandCount = 0;
        int[] heights = new int[rowCount];
        int[] minHeights = new int[rowCount];
        boolean[] expandRow = new boolean[rowCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                GridData data = getData(grid, i, j, rowCount, columnCount, true);
                if (data != null) {
                    int vSpan = Math.max(1,
                            Math.min(data.verticalSpan, rowCount));
                    if (vSpan == 1) {
                        int h = data.cacheHeight; // + data.verticalIndent;
                        heights[i] = Math.max(heights[i], h);
                        if (data.grabExcessVerticalSpace) {
                            if (!expandRow[i])
                                expandCount++;
                            expandRow[i] = true;
                        }
                        if (data.heightHint != SWT.DEFAULT
                                || !data.grabExcessVerticalSpace) {
                            minHeights[i] = Math.max(minHeights[i], h);
                        }
                    }
                }
            }
            for (int j = 0; j < columnCount; j++) {
                GridData data = getData(grid, i, j, rowCount, columnCount,
                        false);
                if (data != null) {
                    int vSpan = Math.max(1,
                            Math.min(data.verticalSpan, rowCount));
                    if (vSpan > 1) {
                        int spanHeight = 0, spanMinHeight = 0, spanExpandCount = 0;
                        for (int k = 0; k < vSpan; k++) {
                            spanHeight += heights[i - k];
                            spanMinHeight += minHeights[i - k];
                            if (expandRow[i - k])
                                spanExpandCount++;
                        }
                        if (data.grabExcessVerticalSpace
                                && spanExpandCount == 0) {
                            expandCount++;
                            expandRow[i] = true;
                        }
                        int h = data.cacheHeight - spanHeight - (vSpan - 1)
                                * verticalSpacing; // + data.verticalalIndent
                        if (h > 0) {
                            if (spanExpandCount == 0) {
                                heights[i] += h;
                            } else {
                                int delta = h / spanExpandCount;
                                int remainder = h % spanExpandCount, last = -1;
                                for (int k = 0; k < vSpan; k++) {
                                    if (expandRow[i - k]) {
                                        heights[last = i - k] += delta;
                                    }
                                }
                                if (last > -1)
                                    heights[last] += remainder;
                            }
                        }
                        if (data.heightHint != SWT.DEFAULT
                                || !data.grabExcessVerticalSpace) {
                            h = data.cacheHeight - spanMinHeight - (vSpan - 1)
                                    * verticalSpacing; // + data.verticalIndent
                            if (h > 0) {
                                if (spanExpandCount == 0) {
                                    minHeights[i] += h;
                                } else {
                                    int delta = h / spanExpandCount;
                                    int remainder = h % spanExpandCount, last = -1;
                                    for (int k = 0; k < vSpan; k++) {
                                        if (expandRow[i - k]) {
                                            minHeights[last = i - k] += delta;
                                        }
                                    }
                                    if (last > -1)
                                        minHeights[last] += remainder;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (height != SWT.DEFAULT && expandCount > 0) {
            int totalHeight = 0;
            for (int i = 0; i < rowCount; i++) {
                totalHeight += heights[i];
            }
            int count = expandCount;
            int delta = (availableHeight - totalHeight) / count;
            int remainder = (availableHeight - totalHeight) % count;
            int last = -1;
            while (totalHeight != availableHeight) {
                for (int i = 0; i < rowCount; i++) {
                    if (expandRow[i]) {
                        if (heights[i] + delta > minHeights[i]) {
                            heights[last = i] = heights[i] + delta;
                        } else {
                            heights[i] = minHeights[i];
                            expandRow[i] = false;
                            count--;
                        }
                    }
                }
                if (last > -1)
                    heights[last] += remainder;

                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        GridData data = getData(grid, i, j, rowCount,
                                columnCount, false);
                        if (data != null) {
                            int vSpan = Math.max(1,
                                    Math.min(data.verticalSpan, rowCount));
                            if (vSpan > 1) {
                                if (data.heightHint != SWT.DEFAULT
                                        || !data.grabExcessVerticalSpace) {
                                    int spanHeight = 0, spanExpandCount = 0;
                                    for (int k = 0; k < vSpan; k++) {
                                        spanHeight += heights[i - k];
                                        if (expandRow[i - k])
                                            spanExpandCount++;
                                    }
                                    int h = data.cacheHeight - spanHeight
                                            - (vSpan - 1) * verticalSpacing; // +
                                    // data.verticalIndent
                                    if (h > 0) {
                                        if (spanExpandCount == 0) {
                                            heights[i] += h;
                                        } else {
                                            int delta2 = h / spanExpandCount;
                                            int remainder2 = h
                                                    % spanExpandCount, last2 = -1;
                                            for (int k = 0; k < vSpan; k++) {
                                                if (expandRow[i - k]) {
                                                    heights[last2 = i - k] += delta2;
                                                }
                                            }
                                            if (last2 > -1)
                                                heights[last2] += remainder2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (count == 0)
                    break;
                totalHeight = 0;
                for (int i = 0; i < rowCount; i++) {
                    totalHeight += heights[i];
                }
                delta = (availableHeight - totalHeight) / count;
                remainder = (availableHeight - totalHeight) % count;
                last = -1;
            }
        }

        /* Position the IFigures */
        if (move) {
            int gridY = y + marginHeight;
            for (int i = 0; i < rowCount; i++) {
                int gridX = x + marginWidth;
                for (int j = 0; j < columnCount; j++) {
                    GridData data = getData(grid, i, j, rowCount, columnCount,
                            true);
                    if (data != null) {
                        int hSpan = Math.max(1,
                                Math.min(data.horizontalSpan, columnCount));
                        int vSpan = Math.max(1, data.verticalSpan);
                        int cellWidth = 0, cellHeight = 0;
                        for (int k = 0; k < hSpan; k++) {
                            cellWidth += widths[j + k];
                        }
                        for (int k = 0; k < vSpan; k++) {
                            cellHeight += heights[i + k];
                        }
                        cellWidth += horizontalSpacing * (hSpan - 1);
                        int childX = gridX + data.horizontalIndent;
                        int childWidth = Math.min(data.cacheWidth, cellWidth);
                        switch (data.horizontalAlignment) {
                        case SWT.CENTER:
                        case GridData.CENTER:
                            childX = gridX
                                    + Math.max(0, (cellWidth - childWidth) / 2);
                            break;
                        case SWT.RIGHT:
                        case SWT.END:
                        case GridData.END:
                            childX = gridX
                                    + Math.max(0, cellWidth - childWidth);
                            break;
                        case SWT.FILL:
                            childWidth = cellWidth - data.horizontalIndent;
                            break;
                        }
                        cellHeight += verticalSpacing * (vSpan - 1);
                        int childY = gridY; // + data.verticalIndent;
                        int childHeight = Math
                                .min(data.cacheHeight, cellHeight);
                        switch (data.verticalAlignment) {
                        case SWT.CENTER:
                        case GridData.CENTER:
                            childY = gridY
                                    + Math.max(0,
                                            (cellHeight - childHeight) / 2);
                            break;
                        case SWT.BOTTOM:
                        case SWT.END:
                        case GridData.END:
                            childY = gridY
                                    + Math.max(0, cellHeight - childHeight);
                            break;
                        case SWT.FILL:
                            childHeight = cellHeight; // -
                            // data.verticalIndent;
                            break;
                        }
                        IFigure child = grid[i][j];
                        if (child != null) {
                            // following param could be replaced by
                            // Rectangle.SINGLETON
                            child.setBounds(new Rectangle(childX, childY,
                                    childWidth, childHeight));
                        }
                    }
                    gridX += widths[j] + horizontalSpacing;
                }
                gridY += heights[i] + verticalSpacing;
            }
        }

        // clean up cache
        for (int i = 0; i < flushLength; i++) {
            flush[i].cacheWidth = flush[i].cacheHeight = -1;
        }

        int totalDefaultWidth = 0;
        int totalDefaultHeight = 0;
        for (int i = 0; i < columnCount; i++) {
            totalDefaultWidth += widths[i];
        }
        for (int i = 0; i < rowCount; i++) {
            totalDefaultHeight += heights[i];
        }
        totalDefaultWidth += horizontalSpacing * (columnCount - 1)
                + marginWidth * 2;
        totalDefaultHeight += verticalSpacing * (rowCount - 1) + marginHeight
                * 2;
        return new Dimension(totalDefaultWidth, totalDefaultHeight);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.draw2d.LayoutManager#getConstraint(org.eclipse.draw2d.IFigure
     * )
     */
    @Override
    public Object getConstraint(IFigure child) {
        return constraints.get(child);
    }

    /**
     * Sets the layout constraint of the given figure. The constraints can only
     * be of type {@link GridData}.
     * 
     * @see LayoutManager#setConstraint(IFigure, Object)
     */
    @Override
    public void setConstraint(IFigure figure, Object newConstraint) {
        super.setConstraint(figure, newConstraint);
        if (newConstraint != null) {
            constraints.put(figure, newConstraint);

        }
    }

}