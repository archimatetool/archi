/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
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
 * @author Randy Hudson
 */
public class PrecisionDimension extends Dimension {

    private static final long serialVersionUID = 1L;

    /**
     * The height in double precision.
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseHeight(double)} and
     *             {@link #preciseHeight()} instead. This field will become
     *             private in the future.
     */
    public double preciseHeight;
    /**
     * The width in double precision.
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseWidth(double)} and
     *             {@link #preciseWidth()} instead. This field will become
     *             private in the future.
     */
    public double preciseWidth;

    /**
     * Constructs a new precision dimension.
     */
    public PrecisionDimension() {
    }

    /**
     * Constructs a precision representation of the given dimension.
     * 
     * @param d
     *            the reference dimension
     */
    public PrecisionDimension(Dimension d) {
        this(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * Constructs a new precision dimension with the given values.
     * 
     * @param w
     *            the width
     * @param h
     *            the height
     */
    public PrecisionDimension(double w, double h) {
        setPreciseSize(w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#contains(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public boolean contains(Dimension d) {
        return preciseWidth() >= d.preciseWidth()
                && preciseHeight() >= d.preciseHeight();
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#containsProper(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public boolean containsProper(Dimension d) {
        return preciseWidth() > d.preciseWidth()
                && preciseHeight() > d.preciseHeight();
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof PrecisionDimension) {
            PrecisionDimension d = (PrecisionDimension) o;
            return d.preciseWidth() == preciseWidth()
                    && d.preciseHeight() == preciseHeight();
        }
        return super.equals(o);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#expand(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Dimension expand(Dimension d) {
        return expandPrecise(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#expand(double, double)
     */
    @Override
    public Dimension expand(double w, double h) {
        return expandPrecise(w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#expand(int, int)
     */
    @Override
    public Dimension expand(int w, int h) {
        return expandPrecise(w, h);
    }

    /**
     * Expands the size of this PrecisionDimension by the specified width and
     * height.
     * 
     * @param w
     *            Value by which the width should be increased
     * @param h
     *            Value by which the height should be increased
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionDimension expandPrecise(double w, double h) {
        setPreciseWidth(preciseWidth() + w);
        setPreciseHeight(preciseHeight() + h);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#getCopy()
     */
    @Override
    public Dimension getCopy() {
        return getPreciseCopy();
    }

    /**
     * Returns a precise copy of this.
     * 
     * @return a precise copy
     * @since 3.7
     */
    public PrecisionDimension getPreciseCopy() {
        PrecisionDimension result = new PrecisionDimension();
        result.setPreciseWidth(preciseWidth());
        result.setPreciseHeight(preciseHeight());
        return result;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#intersect(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Dimension intersect(Dimension d) {
        setPreciseWidth(Math.min(d.preciseWidth(), preciseWidth()));
        setPreciseHeight(Math.min(d.preciseHeight(), preciseHeight()));
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#performScale(double)
     */
    @Override
    public void performScale(double factor) {
        setPreciseWidth(preciseWidth() * factor);
        setPreciseHeight(preciseHeight() * factor);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#preciseHeight()
     */
    @Override
    public double preciseHeight() {
        updatePreciseHeightDouble();
        return preciseHeight;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#preciseWidth()
     */
    @Override
    public double preciseWidth() {
        updatePreciseWidthDouble();
        return preciseWidth;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#scale(double, double)
     */
    @Override
    public Dimension scale(double widthFactor, double heightFactor) {
        setPreciseWidth(preciseWidth() * widthFactor);
        setPreciseHeight(preciseHeight() * heightFactor);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#setHeight(int)
     */
    @Override
    public Dimension setHeight(int h) {
        return setPreciseHeight(h);
    }

    /**
     * Sets the height.
     * 
     * @param h
     *            the new height
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionDimension setPreciseHeight(double h) {
        preciseHeight = h;
        updateHeightInt();
        return this;
    }

    /**
     * Sets the size of this PrecisionDimension to the given width and height.
     * 
     * @param w
     *            The new width
     * @param h
     *            The new height
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionDimension setPreciseSize(double w, double h) {
        setPreciseWidth(w);
        setPreciseHeight(h);
        return this;
    }

    /**
     * Sets the size of this Dimension to the width and height of the given one.
     * 
     * @param d
     *            The PrecisionDimension specifying the new width and height
     *            values.
     * @return This for convenience
     * @since 3.7
     */
    public PrecisionDimension setPreciseSize(PrecisionDimension d) {
        return setPreciseSize(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * Sets the width.
     * 
     * @param w
     *            the new width
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionDimension setPreciseWidth(double w) {
        preciseWidth = w;
        updateWidthInt();
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#setSize(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public void setSize(Dimension d) {
        setPreciseSize(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#setSize(int, int)
     */
    @Override
    public Dimension setSize(int w, int h) {
        return setPreciseSize(w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#setWidth(int)
     */
    @Override
    public Dimension setWidth(int width) {
        return setPreciseWidth(width);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#shrink(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Dimension shrink(Dimension d) {
        return shrinkPrecise(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#shrink(int, int)
     */
    @Override
    public Dimension shrink(int w, int h) {
        return shrinkPrecise(w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#shrink(double, double)
     */
    @Override
    public Dimension shrink(double w, double h) {
        return shrinkPrecise(w, h);
    }

    /**
     * Reduces the width of this Dimension by <i>w</i>, and reduces the height
     * of this Dimension by <i>h</i>. Returns this for convenience.
     * 
     * @param w
     *            the value by which the width is to be reduced
     * @param h
     *            the value by which the height is to be reduced
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionDimension shrinkPrecise(double w, double h) {
        setPreciseWidth(preciseWidth() - w);
        setPreciseHeight(preciseHeight() - h);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#transpose()
     */
    @Override
    public Dimension transpose() {
        double temp = preciseWidth();
        setPreciseWidth(preciseHeight());
        setPreciseHeight(temp);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Dimension#union(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Dimension union(Dimension d) {
        setPreciseWidth(Math.max(preciseWidth(), d.preciseWidth()));
        setPreciseHeight(Math.max(preciseHeight(), d.preciseHeight()));
        return this;
    }

    /**
     * Updates the height integer field using the value of preciseHeight.
     */
    private final void updateHeightInt() {
        height = PrecisionGeometry.doubleToInteger(preciseHeight);
    }

    /**
     * Updates the integer fields using the precise versions.
     * 
     * @deprecated This method should not be accessed by clients any more (it
     *             will be made private in future releases). The update of
     *             integer and precision fields is performed automatically if
     *             {@link #preciseWidth} and {@link #preciseHeight} field values
     *             are not manipulated directly, but only via respective methods
     *             offered by this class.
     */
    public final void updateInts() {
        updateWidthInt();
        updateHeightInt();
    }

    /**
     * Updates the width integer field using the value of preciseWidth.
     */
    private final void updateWidthInt() {
        width = PrecisionGeometry.doubleToInteger(preciseWidth);
    }

    /**
     * Updates the preciseWidth double field using the value of width.
     */
    private final void updatePreciseWidthDouble() {
        if (width != PrecisionGeometry.doubleToInteger(preciseWidth)) {
            preciseWidth = width;
        }
    }

    /**
     * Updates the preciseHeight double field using the value of height.
     */
    private final void updatePreciseHeightDouble() {
        if (height != PrecisionGeometry.doubleToInteger(preciseHeight)) {
            preciseHeight = height;
        }
    }

}
