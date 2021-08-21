/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.text;

import org.eclipse.draw2d.geometry.Translatable;

/**
 * Stores positional information about where a caret should be placed. This
 * structure currently only offers integer precision. Scaling operations will
 * result in rounding.
 * 
 * @since 3.1
 */
public class CaretInfo implements Translatable {

    private int ascent, lineAscent, descent, lineDescent, baseline, x;

    /**
     * Constructor for use by TextFlow. Constructs a new CaretInfo with the
     * figure's ascent and descent and line information.
     * <P>
     * <EM>WARNING:</EM> This constructor should not be called by clients. It is
     * for use by {@link TextFlow}, and may change in future releases.
     * 
     * @param x
     *            the x location
     * @param y
     *            the y location of the top of the caret
     * @param ascent
     *            the ascent
     * @param descent
     *            the descent
     * @param lineAscent
     *            the ascent of the line on which the caret is placed
     * @param lineDescent
     *            the descent of the line on which the caret is placed
     */
    protected CaretInfo(int x, int y, int ascent, int descent, int lineAscent,
            int lineDescent) {
        this.x = x;
        this.baseline = y + ascent;
        this.ascent = ascent;
        this.descent = descent;
        this.lineAscent = lineAscent;
        this.lineDescent = lineDescent;
    }

    /**
     * Constructs a CaretInfo object by copying the values from another
     * instance.
     * 
     * @param info
     *            the reference
     * @since 3.2
     */
    protected CaretInfo(CaretInfo info) {
        this.ascent = info.ascent;
        this.baseline = info.baseline;
        this.descent = info.descent;
        this.lineAscent = info.lineAscent;
        this.lineDescent = info.lineDescent;
        this.x = info.x;
    }

    /**
     * Returns the y location of the baseline.
     * 
     * @return the y coordinate of the baseline
     */
    public int getBaseline() {
        return baseline;
    }

    /**
     * Returns the total height of the caret. The height is the sum of the
     * ascent and descent.
     * 
     * @return the height
     */
    public int getHeight() {
        return ascent + descent;
    }

    /**
     * @return the total height of the line on which the caret is placed
     */
    public int getLineHeight() {
        return lineAscent + lineDescent;
    }

    /**
     * @return the y location of the line on which the caret is placed
     */
    public int getLineY() {
        return baseline - lineAscent;
    }

    /**
     * Returns the x location of the caret.
     * 
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y location of the caret.
     * 
     * @return the y coordinate
     */
    public int getY() {
        return baseline - ascent;
    }

    /**
     * @see Translatable#performScale(double)
     */
    @Override
    public void performScale(double factor) {
        x *= factor;
        baseline *= factor;
        descent *= factor;
        ascent *= factor;
        lineAscent *= factor;
        lineDescent *= factor;
    }

    /**
     * @see Translatable#performTranslate(int, int)
     */
    @Override
    public void performTranslate(int dx, int dy) {
        x += dx;
        baseline += dy;
    }

}