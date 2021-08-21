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
package org.eclipse.draw2d.geometry;

/**
 * Stores four integers for top, left, bottom, and right measurements.
 */
public class Insets implements Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** distance from left */
    public int left;
    /** distance from top */
    public int top;
    /** distance from bottom */
    public int bottom;
    /** distance from right */
    public int right;

    /**
     * Constructs an Insets with all zeroes.
     * 
     * @since 2.0
     */
    public Insets() {
    }

    /**
     * Constructs a new Insets with initial values the same as the provided
     * Insets.
     * 
     * @param i
     *            The insets to copy.
     * @since 2.0
     */
    public Insets(Insets i) {
        this(i.top, i.left, i.bottom, i.right);
    }

    /**
     * Constructs a new Insets with all the sides set to the speicifed value.
     * 
     * @param i
     *            Value applied to all sides of new Insets.
     * @since 2.0
     */
    public Insets(int i) {
        this(i, i, i, i);
    }

    /**
     * Creates a new Insets with the specified top, left, bottom, and right
     * values.
     * 
     * @param top
     *            Value of the top space.
     * @param left
     *            Value of the left space.
     * @param bottom
     *            Value of the bottom space.
     * @param right
     *            Value of the right space.
     * @since 2.0
     */
    public Insets(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Adds the values of the specified Insets to this Insets' values.
     * 
     * @return <code>this</code> for convenience
     * @param insets
     *            the Insets being added
     * @since 2.0
     */
    public Insets add(Insets insets) {
        top += insets.top;
        bottom += insets.bottom;
        left += insets.left;
        right += insets.right;
        return this;
    }

    /**
     * Test for equality. The Insets are equal if their top, left, bottom, and
     * right values are equivalent.
     * 
     * @param o
     *            Object being tested for equality.
     * @return true if all values are the same.
     * @since 2.0
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Insets) {
            Insets i = (Insets) o;
            return i.top == top && i.bottom == bottom && i.left == left
                    && i.right == right;
        }
        return false;
    }

    /**
     * Creates an Insets representing the sum of this Insets with the specified
     * Insets.
     * 
     * @param insets
     *            Insets to be added
     * @return A new Insets
     * @since 2.0
     */
    public Insets getAdded(Insets insets) {
        return new Insets(this).add(insets);
    }

    /**
     * Returns the height for this Insets, equal to <code>top</code> +
     * <code>bottom</code>.
     * 
     * @return The sum of top + bottom
     * @see #getWidth()
     * @since 2.0
     */
    public int getHeight() {
        return top + bottom;
    }

    /**
     * Creates a new Insets with transposed values. Top and Left are transposed.
     * Bottom and Right are transposed.
     * 
     * @return New Insets with the transposed values.
     * @since 2.0
     */
    public Insets getTransposed() {
        return new Insets(this).transpose();
    }

    /**
     * Returns the width for this Insets, equal to <code>left</code> +
     * <code>right</code>.
     * 
     * @return The sum of left + right
     * @see #getHeight()
     * @since 2.0
     */
    public int getWidth() {
        return left + right;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return top * 7 + left * 2 + bottom * 31 + right * 37;
    }

    /**
     * Returns true if all values are 0.
     * 
     * @return true if all values are 0
     * @since 2.0
     */
    public boolean isEmpty() {
        return (left == 0 && right == 0 && top == 0 && bottom == 0);
    }

    /**
     * @return String representation.
     * @since 2.0
     */
    @Override
    public String toString() {
        return "Insets(t=" + top + ", l=" + left + //$NON-NLS-2$//$NON-NLS-1$
                ", b=" + bottom + ", r=" + right + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * Transposes this object. Top and Left are exchanged. Bottom and Right are
     * exchanged. Can be used in orientation changes.
     * 
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Insets transpose() {
        int temp = top;
        top = left;
        left = temp;
        temp = right;
        right = bottom;
        bottom = temp;
        return this;
    }

}
