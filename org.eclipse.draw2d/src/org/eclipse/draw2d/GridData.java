/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Asim Ullah - Ported for use in draw2d (c.f Bugzilla 71684).[Sep 10, 2004]
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * <code>GridData</code> is the layout data object associated with
 * <code>GridLayout</code>. To set a <code>GridData</code> object into a
 * <code>Figure</code>, you use the <code>setConstraint()</code> method of
 * <code>GridLayout</code> to map the <code>Figure</code> to its layout
 * <code>GridData</code>.
 * <p>
 * There are two ways to create a <code>GridData</code> object with certain
 * fields set. The first is to set the fields directly, like this:
 * 
 * <pre>
 * GridData gridData = new GridData();
 * gridData.horizontalAlignment = GridData.FILL;
 * gridData.grabExcessHorizontalSpace = true;
 * 
 * // associate the figure to the GridData object
 * myGridlayout.setConstraint(myFigure, gridData);
 * </pre>
 * 
 * The second is to take advantage of convenience style bits defined by
 * <code>GridData</code>:
 * 
 * <pre>
 * GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
 *         | GridData.GRAB_HORIZONTAL);
 * </pre>
 * 
 * </p>
 * <p>
 * NOTE: Do not reuse <code>GridData</code> objects. Every child in the parent
 * <code>Figure</code> that is managed by the <code>GridLayout</code> must have
 * a unique <code>GridData</code> object. If the layout data for a Grid member
 * in a <code>GridLayout</code> is null at layout time, a unique
 * <code>GridData</code> object is created for it.
 * </p>
 * 
 * @see GridLayout
 */
public final class GridData {
    /**
     * verticalAlignment specifies how figures will be positioned vertically
     * within a cell.
     * 
     * The default value is CENTER.
     * 
     * Possible values are:
     * 
     * SWT.BEGINNING (or SWT.TOP): Position the figure at the top of the cell
     * SWT.CENTER: Position the figure in the vertical center of the cell
     * SWT.END (or SWT.BOTTOM): Position the figure at the bottom of the cell
     * SWT.FILL: Resize the figure to fill the cell vertically
     */
    public int verticalAlignment = CENTER;

    /**
     * horizontalAlignment specifies how figures will be positioned horizontally
     * within a cell.
     * 
     * The default value is BEGINNING.
     * 
     * Possible values are:
     * 
     * SWT.BEGINNING (or SWT.LEFT): Position the figure at the left of the cell
     * SWT.CENTER: Position the figure in the horizontal center of the cell
     * SWT.END (or SWT.RIGHT): Position the figure at the right of the cell
     * SWT.FILL: Resize the figure to fill the cell horizontally
     */
    public int horizontalAlignment = BEGINNING;

    /**
     * widthHint specifies a minimum width for the column. A value of
     * SWT.DEFAULT indicates that no minimum width is specified.
     * 
     * The default value is SWT.DEFAULT.
     */
    public int widthHint = SWT.DEFAULT;

    /**
     * heightHint specifies a minimum height for the row. A value of SWT.DEFAULT
     * indicates that no minimum height is specified.
     * 
     * The default value is SWT.DEFAULT.
     */
    public int heightHint = SWT.DEFAULT;

    /**
     * horizontalIndent specifies the number of pixels of indentation that will
     * be placed along the left side of the cell.
     * 
     * The default value is 0.
     */
    public int horizontalIndent = 0;

    /**
     * horizontalSpan specifies the number of column cells that the figure will
     * take up.
     * 
     * The default value is 1.
     */
    public int horizontalSpan = 1;

    /**
     * verticalSpan specifies the number of row cells that the figure will take
     * up.
     * 
     * The default value is 1.
     */
    public int verticalSpan = 1;

    /**
     * grabExcessHorizontalSpace specifies whether the cell will be made wide
     * enough to fit the remaining horizontal space.
     * 
     * The default value is false.
     */
    public boolean grabExcessHorizontalSpace = false;

    /**
     * grabExcessVerticalSpace specifies whether the cell will be made tall
     * enough to fit the remaining vertical space.
     * 
     * The default value is false.
     */
    public boolean grabExcessVerticalSpace = false;

    /**
     * Value for horizontalAlignment or verticalAlignment. Position the figure
     * at the top or left of the cell. Not recommended. Use SWT.BEGINNING,
     * SWT.TOP or SWT.LEFT instead.
     */
    public static final int BEGINNING = SWT.BEGINNING;

    /**
     * Value for horizontalAlignment or verticalAlignment. Position the figure
     * in the vertical or horizontal center of the cell Not recommended. Use
     * SWT.CENTER instead.
     */
    public static final int CENTER = 2;

    /**
     * Value for horizontalAlignment or verticalAlignment. Position the figure
     * at the bottom or right of the cell Not recommended. Use SWT.END,
     * SWT.BOTTOM or SWT.RIGHT instead.
     */
    public static final int END = 3;

    /**
     * Value for horizontalAlignment or verticalAlignment. Resize the figure to
     * fill the cell horizontally or vertically. Not recommended. Use SWT.FILL
     * instead.
     */
    public static final int FILL = SWT.FILL;

    /**
     * Style bit for <code>new GridData(int)</code>. Position the figure at the
     * top of the cell. Not recommended. Use
     * <code>new GridData(int, SWT.BEGINNING, boolean, boolean)</code> instead.
     */
    public static final int VERTICAL_ALIGN_BEGINNING = 1 << 1;

    /**
     * Style bit for <code>new GridData(int)</code> to position the figure in
     * the vertical center of the cell. Not recommended. Use
     * <code>new GridData(int, SWT.CENTER, boolean, boolean)</code> instead.
     */
    public static final int VERTICAL_ALIGN_CENTER = 1 << 2;

    /**
     * Style bit for <code>new GridData(int)</code> to position the figure at
     * the bottom of the cell. Not recommended. Use
     * <code>new GridData(int, SWT.END, boolean, boolean)</code> instead.
     */
    public static final int VERTICAL_ALIGN_END = 1 << 3;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fill
     * the cell vertically. Not recommended. Use
     * <code>new GridData(int, SWT.FILL, boolean, boolean)</code> instead
     */
    public static final int VERTICAL_ALIGN_FILL = 1 << 4;

    /**
     * Style bit for <code>new GridData(int)</code> to position the figure at
     * the left of the cell. Not recommended. Use
     * <code>new GridData(SWT.BEGINNING, int, boolean, boolean)</code> instead.
     */
    public static final int HORIZONTAL_ALIGN_BEGINNING = 1 << 5;

    /**
     * Style bit for <code>new GridData(int)</code> to position the figure in
     * the horizontal center of the cell. Not recommended. Use
     * <code>new GridData(SWT.CENTER, int, boolean, boolean)</code> instead.
     */
    public static final int HORIZONTAL_ALIGN_CENTER = 1 << 6;

    /**
     * Style bit for <code>new GridData(int)</code> to position the figure at
     * the right of the cell. Not recommended. Use
     * <code>new GridData(SWT.END, int, boolean, boolean)</code> instead.
     */
    public static final int HORIZONTAL_ALIGN_END = 1 << 7;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fill
     * the cell horizontally. Not recommended. Use
     * <code>new GridData(SWT.FILL, int, boolean, boolean)</code> instead.
     */
    public static final int HORIZONTAL_ALIGN_FILL = 1 << 8;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fit
     * the remaining horizontal space. Not recommended. Use
     * <code>new GridData(int, int, true, boolean)</code> instead.
     */
    public static final int GRAB_HORIZONTAL = 1 << 9;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fit
     * the remaining vertical space. Not recommended. Use
     * <code>new GridData(int, int, boolean, true)</code> instead.
     */
    public static final int GRAB_VERTICAL = 1 << 10;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fill
     * the cell vertically and to fit the remaining vertical space.
     * FILL_VERTICAL = VERTICAL_ALIGN_FILL | GRAB_VERTICAL Not recommended. Use
     * <code>new GridData(int, SWT.FILL, boolean, true)</code> instead.
     */
    public static final int FILL_VERTICAL = VERTICAL_ALIGN_FILL | GRAB_VERTICAL;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fill
     * the cell horizontally and to fit the remaining horizontal space.
     * FILL_HORIZONTAL = HORIZONTAL_ALIGN_FILL | GRAB_HORIZONTAL Not
     * recommended. Use <code>new GridData(SWT.FILL, int, true, boolean)</code>
     * instead.
     */
    public static final int FILL_HORIZONTAL = HORIZONTAL_ALIGN_FILL
            | GRAB_HORIZONTAL;

    /**
     * Style bit for <code>new GridData(int)</code> to resize the figure to fill
     * the cell horizontally and vertically and to fit the remaining horizontal
     * and vertical space. FILL_BOTH = FILL_VERTICAL | FILL_HORIZONTAL Not
     * recommended. Use
     * <code>new GridData(SWT.FILL, SWT.FILL, true, true)</code> instead.
     */
    public static final int FILL_BOTH = FILL_VERTICAL | FILL_HORIZONTAL;

    int cacheWidth = -1, cacheHeight = -1;
    int[][] cache = new int[2][4];
    int cacheIndex = -1;

    /**
     * Constructs a new instance of GridData using default values.
     */
    public GridData() {
        super();
    }

    /**
     * Constructs a new instance based on the GridData style. This constructor
     * is not recommended.
     * 
     * @param style
     *            the GridData style
     */
    public GridData(int style) {
        super();
        if ((style & VERTICAL_ALIGN_BEGINNING) != 0)
            verticalAlignment = BEGINNING;
        if ((style & VERTICAL_ALIGN_CENTER) != 0)
            verticalAlignment = CENTER;
        if ((style & VERTICAL_ALIGN_FILL) != 0)
            verticalAlignment = FILL;
        if ((style & VERTICAL_ALIGN_END) != 0)
            verticalAlignment = END;
        if ((style & HORIZONTAL_ALIGN_BEGINNING) != 0)
            horizontalAlignment = BEGINNING;
        if ((style & HORIZONTAL_ALIGN_CENTER) != 0)
            horizontalAlignment = CENTER;
        if ((style & HORIZONTAL_ALIGN_FILL) != 0)
            horizontalAlignment = FILL;
        if ((style & HORIZONTAL_ALIGN_END) != 0)
            horizontalAlignment = END;
        grabExcessHorizontalSpace = (style & GRAB_HORIZONTAL) != 0;
        grabExcessVerticalSpace = (style & GRAB_VERTICAL) != 0;
    }

    /**
     * Constructs a new instance of GridData according to the parameters.
     * 
     * @param horizontalAlignment
     *            how figure will be positioned horizontally within a cell
     * @param verticalAlignment
     *            how figure will be positioned vertically within a cell
     * @param grabExcessHorizontalSpace
     *            whether cell will be made wide enough to fit the remaining
     *            horizontal space
     * @param grabExcessVerticalSpace
     *            whether cell will be made high enough to fit the remaining
     *            vertical space
     * 
     */
    public GridData(int horizontalAlignment, int verticalAlignment,
            boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace) {
        this(horizontalAlignment, verticalAlignment, grabExcessHorizontalSpace,
                grabExcessVerticalSpace, 1, 1);
    }

    /**
     * Constructs a new instance of GridData according to the parameters.
     * 
     * @param horizontalAlignment
     *            how figure will be positioned horizontally within a cell
     * @param verticalAlignment
     *            how figure will be positioned vertically within a cell
     * @param grabExcessHorizontalSpace
     *            whether cell will be made wide enough to fit the remaining
     *            horizontal space
     * @param grabExcessVerticalSpace
     *            whether cell will be made high enough to fit the remaining
     *            vertical space
     * @param horizontalSpan
     *            the number of column cells that the figure will take up
     * @param verticalSpan
     *            the number of row cells that the figure will take up
     * 
     */
    public GridData(int horizontalAlignment, int verticalAlignment,
            boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace,
            int horizontalSpan, int verticalSpan) {
        super();
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
        this.grabExcessVerticalSpace = grabExcessVerticalSpace;
        this.horizontalSpan = horizontalSpan;
        this.verticalSpan = verticalSpan;
    }

    /**
     * Constructs a new instance of GridData according to the parameters. A
     * value of SWT.DEFAULT indicates that no minimum width or no minumum height
     * is specified.
     * 
     * @param width
     *            a minimum width for the column
     * @param height
     *            a minimum height for the row
     * 
     */
    public GridData(int width, int height) {
        super();
        this.widthHint = width;
        this.heightHint = height;
    }

    Dimension computeSize(IFigure figure, boolean flushCache) {
        if (flushCache) {
            flushCache();
        } else {
            if (cacheWidth != -1 && cacheHeight != -1) {
                return new Dimension(cacheWidth, cacheHeight);
            }
            for (int i = 0; i < cacheIndex + 1; i++) {
                if (cache[i][0] == widthHint && cache[i][1] == heightHint) {
                    cacheWidth = cache[i][2];
                    cacheHeight = cache[i][3];
                    return new Dimension(cacheWidth, cacheHeight);
                }
            }
        }

        Dimension size = figure.getPreferredSize(widthHint, heightHint)
                .getCopy();
        if (widthHint != -1)
            size.width = widthHint;
        if (heightHint != -1)
            size.height = heightHint;

        if (cacheIndex < cache.length - 1)
            cacheIndex++;
        cache[cacheIndex][0] = widthHint;
        cache[cacheIndex][1] = heightHint;
        cacheWidth = cache[cacheIndex][2] = size.width;
        cacheHeight = cache[cacheIndex][3] = size.height;
        return size;
    }

    void flushCache() {
        cacheWidth = cacheHeight = -1;
        cacheIndex = -1;
    }

    String getName() {
        String string = getClass().getName();
        int index = string.lastIndexOf('.');
        if (index == -1)
            return string;
        return string.substring(index + 1, string.length());
    }

    @Override
    public String toString() {

        String hAlign = ""; //$NON-NLS-1$
        switch (horizontalAlignment) {
        case SWT.FILL:
            hAlign = "SWT.FILL"; //$NON-NLS-1$
            break;
        case SWT.BEGINNING:
            hAlign = "SWT.BEGINNING"; //$NON-NLS-1$
            break;
        case SWT.LEFT:
            hAlign = "SWT.LEFT"; //$NON-NLS-1$
            break;
        case SWT.END:
            hAlign = "SWT.END"; //$NON-NLS-1$
            break;
        case END:
            hAlign = "GridData.END"; //$NON-NLS-1$
            break;
        case SWT.RIGHT:
            hAlign = "SWT.RIGHT"; //$NON-NLS-1$
            break;
        case SWT.CENTER:
            hAlign = "SWT.CENTER"; //$NON-NLS-1$
            break;
        case CENTER:
            hAlign = "GridData.CENTER"; //$NON-NLS-1$
            break;
        default:
            hAlign = "Undefined " + horizontalAlignment; //$NON-NLS-1$
            break;
        }
        String vAlign = ""; //$NON-NLS-1$
        switch (verticalAlignment) {
        case SWT.FILL:
            vAlign = "SWT.FILL"; //$NON-NLS-1$
            break;
        case SWT.BEGINNING:
            vAlign = "SWT.BEGINNING"; //$NON-NLS-1$
            break;
        case SWT.TOP:
            vAlign = "SWT.TOP"; //$NON-NLS-1$
            break;
        case SWT.END:
            vAlign = "SWT.END"; //$NON-NLS-1$
            break;
        case END:
            vAlign = "GridData.END"; //$NON-NLS-1$
            break;
        case SWT.BOTTOM:
            vAlign = "SWT.BOTTOM"; //$NON-NLS-1$
            break;
        case SWT.CENTER:
            vAlign = "SWT.CENTER"; //$NON-NLS-1$
            break;
        case CENTER:
            vAlign = "GridData.CENTER"; //$NON-NLS-1$
            break;
        default:
            vAlign = "Undefined " + verticalAlignment; //$NON-NLS-1$
            break;
        }
        String string = getName() + " {"; //$NON-NLS-1$
        string += "horizontalAlignment=" + hAlign + " "; //$NON-NLS-1$ //$NON-NLS-2$
        if (horizontalIndent != 0)
            string += "horizontalIndent=" + horizontalIndent + " "; //$NON-NLS-1$ //$NON-NLS-2$
        if (horizontalSpan != 1)
            string += "horizontalSpan=" + horizontalSpan + " "; //$NON-NLS-1$//$NON-NLS-2$
        if (grabExcessHorizontalSpace)
            string += "grabExcessHorizontalSpace=" + grabExcessHorizontalSpace //$NON-NLS-1$
                    + " "; //$NON-NLS-1$
        if (widthHint != SWT.DEFAULT)
            string += "widthHint=" + widthHint + " "; //$NON-NLS-1$ //$NON-NLS-2$
        string += "verticalAlignment=" + vAlign + " "; //$NON-NLS-1$ //$NON-NLS-2$
        if (verticalSpan != 1)
            string += "verticalSpan=" + verticalSpan + " "; //$NON-NLS-1$ //$NON-NLS-2$
        if (grabExcessVerticalSpace)
            string += "grabExcessVerticalSpace=" + grabExcessVerticalSpace //$NON-NLS-1$
                    + " "; //$NON-NLS-1$
        if (heightHint != SWT.DEFAULT)
            string += "heightHint=" + heightHint + " "; //$NON-NLS-1$ //$NON-NLS-2$
        string = string.trim();
        string += "}"; //$NON-NLS-1$
        return string;

    }

}
