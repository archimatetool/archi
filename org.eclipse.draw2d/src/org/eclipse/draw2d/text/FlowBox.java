/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

/**
 * A Geometric object for representing a region on a line of Text. This class
 * adds the notion of a baseline to
 * {@link org.eclipse.draw2d.geometry.Rectangle}. <i>Ascent</i> is the distance
 * above the baseline. <i>Descent</i> is the distance below the baseline.
 * <P>
 * This class should not be treated as a <code>Rectangle</code> by clients. It
 * is important to use getters when available for lazy calculation of values.
 * 
 * @author hudsonr
 * @since 2.1
 */
public abstract class FlowBox {

    int width;

    /**
     * The x location
     */
    private int x;

    /**
     * This method must be called on a block that is completely positioned and
     * committed.
     * 
     * @param x
     *            X
     * @param y
     *            Y
     * @return <code>true</code> if the FlowBox contains the point
     */
    public abstract boolean containsPoint(int x, int y);

    /**
     * Returns the amount of line content in pixels which is above the baseline.
     * Ascent and descent are used to space consecutive lines apart. Certain
     * types of line content, such as borders, extend beyond the ascent and
     * descent.
     * 
     * @return the <i>descent</i> in pixels below the baseline
     */
    public abstract int getAscent();

    /**
     * Returns y coordinate for the box's baseline.
     * 
     * @return the baseline location
     * @since 3.1
     */
    public abstract int getBaseline();

    /**
     * Returns the amount of line content in pixels which is below the baseline.
     * 
     * @return the <i>descent</i> in pixels
     * @see #getAscent()
     */
    public abstract int getDescent();

    /**
     * Returns the root LineBox in which this box is placed. The root line is
     * interesting when painting selection or hit testing. All boxes in a line
     * should render selection at the same top and bottom location.
     * 
     * @return the line root.
     * @since 3.1
     */
    abstract LineRoot getLineRoot();

    /**
     * Returns the outer ascent of this box. The outer ascent is the ascent
     * above the baseline including the border size and margin. This is used
     * when adding content into a LineBox. The linebox's own border must be
     * drawn around the children.
     */
    int getOuterAscent() {
        return getAscent();
    }

    /**
     * Returns the outer descent of this box. The outer descent is the space
     * below the baseline including the border size and margin. This is used
     * when adding content into a LineBox. The linebox's own border must be
     * drawn around the children.
     */
    int getOuterDescent() {
        return getDescent();
    }

    int getAscentWithBorder() {
        throw new RuntimeException("Not valid on this box type"); //$NON-NLS-1$
    }

    int getDescentWithBorder() {
        throw new RuntimeException("Not valid on this box type"); //$NON-NLS-1$
    }

    /**
     * Returns the width of the box.
     * 
     * @return the box's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the X coordinate of the box.
     * 
     * @return the x coordinate
     * @since 3.1
     */
    public int getX() {
        return x;
    }

    /**
     * Returns <code>true</code> if any of the children are bi-directional.
     * Default implementation returns false.
     * 
     * @return <code>true</code> if the box is bi-directional
     * @since 3.1
     */
    public boolean requiresBidi() {
        return false;
    }

    /**
     * Sets the line root.
     * 
     * @param root
     *            the line root
     * @since 3.1
     */
    void setLineRoot(LineRoot root) {
    }

    /**
     * Sets the width of the box.
     * 
     * @param width
     *            the new width
     * @since 3.1
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the x coordinate for this box.
     * 
     * @param x
     *            the x coordinate
     * @since 3.1
     */
    public void setX(int x) {
        this.x = x;
    }

}
