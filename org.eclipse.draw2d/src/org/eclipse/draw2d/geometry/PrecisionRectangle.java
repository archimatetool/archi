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
 * A Rectangle implementation using floating point values which are truncated
 * into the inherited integer fields. The use of floating point prevents
 * rounding errors from accumulating.
 * 
 * @author hudsonr Created on Apr 9, 2003
 */
public final class PrecisionRectangle extends Rectangle {

    private static final long serialVersionUID = 1L;

    /**
     * Double value for height
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseHeight(double)} and
     *             {@link #preciseHeight()} instead. This field will become
     *             private in the future.
     */
    public double preciseHeight;

    /**
     * Double value for width
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseWidth(double)} and
     *             {@link #preciseWidth()} instead. This field will become
     *             private in the future.
     */
    public double preciseWidth;

    /**
     * Double value for X
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseX(double)} and {@link #preciseX()}
     *             instead. This field will become private in the future.
     */
    public double preciseX;

    /**
     * Double value for Y
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseX(double)} and {@link #preciseY()}
     *             instead. This field will become private in the future.
     */
    public double preciseY;

    /**
     * Constructs a new PrecisionRectangle with all values 0.
     */
    public PrecisionRectangle() {
    }

    /**
     * Constructs a PrecisionRectangle with the provided values.
     * 
     * @param x
     *            X location
     * @param y
     *            Y location
     * @param width
     *            Width of the rectangle
     * @param height
     *            Height of the rectangle
     * @since 3.7
     */
    public PrecisionRectangle(double x, double y, double width, double height) {
        setPreciseLocation(x, y);
        setPreciseSize(width, height);
    }

    /**
     * 
     * Constructs a new PrecisionRectangle from a given Point and a Dimension
     * 
     * @param p
     *            The Point to specify x and y location of the
     *            PrecisionRectangle
     * @param d
     *            The Dimension to use for width and height of the
     *            PrecisionRectangle
     * @since 3.7
     */
    public PrecisionRectangle(Point p, Dimension d) {
        this(p.preciseX(), p.preciseY(), d.preciseWidth(), d.preciseHeight());
    }

    /**
     * Constructs a new PrecisionRectangle from the given integer Rectangle.
     * 
     * @param rect
     *            the base rectangle
     */
    public PrecisionRectangle(Rectangle rect) {
        this(rect.preciseX(), rect.preciseY(), rect.preciseWidth(), rect
                .preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#contains(double, double)
     */
    @Override
    public boolean contains(double x, double y) {
        return containsPrecise(x, y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#contains(int, int)
     */
    @Override
    public boolean contains(int x, int y) {
        return containsPrecise(x, y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#contains(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public boolean contains(Point p) {
        return containsPrecise(p.preciseX(), p.preciseY());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#contains(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public boolean contains(Rectangle rect) {
        return preciseX() <= rect.preciseX() && preciseY() <= rect.preciseY()
                && right() >= rect.right() && bottom() >= rect.bottom();
    }

    /**
     * Returns whether the given coordinates are within the boundaries of this
     * Rectangle. The boundaries are inclusive of the top and left edges, but
     * exclusive of the bottom and right edges.
     * 
     * @param x
     *            X value
     * @param y
     *            Y value
     * @return true if the coordinates are within this Rectangle
     * @since 3.7
     */
    private boolean containsPrecise(double x, double y) {
        return y >= preciseY() && y < preciseY() + preciseHeight()
                && x >= preciseX() && x < preciseX() + preciseWidth();
    }

    /**
     * @see Rectangle#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof PrecisionRectangle) {
            PrecisionRectangle rect = (PrecisionRectangle) o;
            return super.equals(o)
                    && Math.abs(rect.preciseX() - preciseX()) < 0.000000001
                    && Math.abs(rect.preciseY() - preciseY()) < 0.000000001
                    && Math.abs(rect.preciseWidth() - preciseWidth()) < 0.000000001
                    && Math.abs(rect.preciseHeight() - preciseHeight()) < 0.00000001;
        }

        return super.equals(o);
    }

    /**
     * Expands the horizontal and vertical sides of this Rectangle with the
     * values provided as input, and returns this for convenience. The location
     * of its center is kept constant.
     * 
     * @param h
     *            Horizontal increment
     * @param v
     *            Vertical increment
     * @return <code>this</code> for convenience
     * @since 3.4
     */
    @Override
    public Rectangle expand(double h, double v) {
        return expandPrecise(h, v);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#expand(org.eclipse.draw2d.geometry.Insets)
     */
    @Override
    public Rectangle expand(Insets insets) {
        if (insets == null)
            return this;
        setPreciseX(preciseX() - insets.left);
        setPreciseY(preciseY() - insets.top);
        setPreciseWidth(preciseWidth() + insets.getWidth());
        setPreciseHeight(preciseHeight() + insets.getHeight());
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#expand(int, int)
     */
    @Override
    public Rectangle expand(int h, int v) {
        return expandPrecise(h, v);
    }

    /**
     * Expands the horizontal and vertical sides of this Rectangle with the
     * values provided as input, and returns this for convenience. The location
     * of its center is kept constant.
     * 
     * @param h
     *            Horizontal increment
     * @param v
     *            Vertical increment
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionRectangle expandPrecise(double h, double v) {
        return shrinkPrecise(-h, -v);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getBottom()
     */
    @Override
    public Point getBottom() {
        return new PrecisionPoint(preciseX() + preciseWidth() / 2, bottom());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getBottomLeft()
     */
    @Override
    public Point getBottomLeft() {
        return new PrecisionPoint(preciseX(), preciseY() + preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getBottomRight()
     */
    @Override
    public Point getBottomRight() {
        return new PrecisionPoint(preciseX() + preciseWidth(), preciseY()
                + preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getCenter()
     */
    @Override
    public Point getCenter() {
        return new PrecisionPoint(preciseX() + preciseWidth() / 2.0, preciseY()
                + preciseHeight() / 2.0);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getCopy()
     */
    @Override
    public Rectangle getCopy() {
        return getPreciseCopy();
    }

    /**
     * Returns a precise copy of this.
     * 
     * @return a precise copy
     */
    public PrecisionRectangle getPreciseCopy() {
        return new PrecisionRectangle(preciseX(), preciseY(), preciseWidth(),
                preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getTop()
     */
    @Override
    public Point getTop() {
        return new PrecisionPoint(preciseX() + preciseWidth() / 2, preciseY());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getTopLeft()
     */
    @Override
    public Point getTopLeft() {
        return new PrecisionPoint(preciseX(), preciseY());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#getTopRight()
     */
    @Override
    public Point getTopRight() {
        return new PrecisionPoint(preciseX() + preciseWidth(), preciseY());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#intersect(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public Rectangle intersect(Rectangle rect) {
        return intersectPrecise(rect);
    }

    /**
     * Sets the size of this Rectangle to the intersection region with the
     * Rectangle supplied as input, and returns this for convenience. The
     * location and dimensions are set to zero if there is no intersection with
     * the input Rectangle.
     * 
     * @param rect
     *            Rectangle for the calculating intersection.
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionRectangle intersectPrecise(Rectangle rect) {
        double x1 = Math.max(preciseX(), rect.preciseX());
        double x2 = Math.min(preciseX() + preciseWidth(), rect.preciseX()
                + rect.preciseWidth());
        double y1 = Math.max(preciseY(), rect.preciseY());
        double y2 = Math.min(preciseY() + preciseHeight(), rect.preciseY()
                + rect.preciseHeight());
        if (((x2 - x1) < 0) || ((y2 - y1) < 0))
            return setPreciseBounds(0, 0, 0, 0); // no intersection
        else {
            return setPreciseBounds(x1, y1, x2 - x1, y2 - y1);
        }
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#performScale(double)
     */
    @Override
    public void performScale(double factor) {
        setPreciseX(preciseX() * factor);
        setPreciseY(preciseY() * factor);
        setPreciseWidth(preciseWidth() * factor);
        setPreciseHeight(preciseHeight() * factor);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#performTranslate(int, int)
     */
    @Override
    public void performTranslate(int dx, int dy) {
        setPreciseX(preciseX() + dx);
        setPreciseY(preciseY() + dy);
    }

    /**
     * Returns the bottom coordinte in double precision.
     * 
     * @return the precise bottom
     */
    public double preciseBottom() {
        return preciseHeight() + preciseY();
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#preciseHeight()
     */
    @Override
    public double preciseHeight() {
        updatePreciseHeightDouble();
        return preciseHeight;
    }

    /**
     * Returns the right side in double precision.
     * 
     * @return the precise right
     */
    public double preciseRight() {
        return preciseWidth() + preciseX();
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#preciseWidth()
     */
    @Override
    public double preciseWidth() {
        updatePreciseWidthDouble();
        return preciseWidth;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#preciseX()
     */
    @Override
    public double preciseX() {
        updatePreciseXDouble();
        return preciseX;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#preciseY()
     */
    @Override
    public double preciseY() {
        updatePreciseYDouble();
        return preciseY;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#resize(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Rectangle resize(Dimension d) {
        return resizePrecise(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#resize(double, double)
     */
    @Override
    public Rectangle resize(double w, double h) {
        return resizePrecise(w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#resize(int, int)
     */
    @Override
    public Rectangle resize(int w, int h) {
        return resizePrecise(w, h);
    }

    /**
     * Resizes this Rectangle by adding the values supplied, returning this for
     * convenience.
     * 
     * @param w
     *            Amount by which width is to be resized
     * @param h
     *            Amount by which height is to be resized
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionRectangle resizePrecise(double w, double h) {
        setPreciseWidth(preciseWidth() + w);
        setPreciseHeight(preciseHeight() + h);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setBounds(int, int, int, int)
     */
    @Override
    public Rectangle setBounds(int x, int y, int width, int height) {
        return setPreciseBounds(x, y, width, height);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setBounds(org.eclipse.draw2d.geometry.Point,
     *      org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Rectangle setBounds(Point location, Dimension size) {
        return setPreciseBounds(location.preciseX(), location.preciseY(),
                size.preciseWidth(), size.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setBounds(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public Rectangle setBounds(Rectangle rect) {
        return setPreciseBounds(rect.preciseX(), rect.preciseY(),
                rect.preciseWidth(), rect.preciseHeight());
    }

    /**
     * Sets the height.
     * 
     * @param value
     *            the new height
     * @deprecated Use {@link #setPreciseHeight(double)} instead.
     */
    public void setHeight(double value) {
        setPreciseHeight(value);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setHeight(int)
     */
    @Override
    public Rectangle setHeight(int height) {
        return setPreciseHeight(height);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setLocation(int, int)
     */
    @Override
    public Rectangle setLocation(int x, int y) {
        return setPreciseLocation(x, y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setLocation(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Rectangle setLocation(Point loc) {
        return setPreciseLocation(loc.preciseX(), loc.preciseY());
    }

    /**
     * Sets the preciseX, preciseY, preciseWidth, and preciseHeight values of
     * this PrecisionRectangle to the provided values and updates the integer
     * values of x, y, width, and height accordingly.
     * 
     * @param x
     *            The new x
     * @param y
     *            The new y
     * @param width
     *            The new width
     * @param height
     *            The new height
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionRectangle setPreciseBounds(double x, double y,
            double width, double height) {
        setPreciseLocation(x, y);
        setPreciseSize(width, height);
        return this;
    }

    /**
     * Sets the height of this PrecisionRectangle to the specified value.
     * 
     * @param value
     *            The new height.
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionRectangle setPreciseHeight(double value) {
        preciseHeight = value;
        updateHeightInt();
        return this;
    }

    /**
     * Sets the preciseX and preciseY values of this PrecisionRectangle to the
     * provided values and updates the integer values of x and y accordingly.
     * 
     * @param x
     *            The new x value
     * @param y
     *            The new y value
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionRectangle setPreciseLocation(double x, double y) {
        setPreciseX(x);
        setPreciseY(y);
        return this;
    }

    /**
     * Sets the precise location of this PrecisionRectangle
     * 
     * @param loc
     *            The new location
     * @return this for convenience.
     * @since 3.7
     */
    public PrecisionRectangle setPreciseLocation(PrecisionPoint loc) {
        return setPreciseLocation(loc.preciseX(), loc.preciseY());
    }

    /**
     * Sets the preciseWidth and preciseHeight values of this PrecisionRectangle
     * to the provided values and updates the integer values of width and height
     * accordingly.
     * 
     * @param w
     *            The new width
     * @param h
     *            The new height
     * @return this for convenience.
     * @since 3.7
     */
    public PrecisionRectangle setPreciseSize(double w, double h) {
        setPreciseWidth(w);
        setPreciseHeight(h);
        return this;
    }

    /**
     * Set the size of this PrecisionRectangle to the given dimension's width
     * and height. Returns this for convenience.
     * 
     * @param size
     *            The new size
     * @return this for convenience.
     * @since 3.7
     */
    public PrecisionRectangle setPreciseSize(PrecisionDimension size) {
        return setPreciseSize(size.preciseWidth(), size.preciseHeight());
    }

    /**
     * Sets the width of this PrecisionRectangle to the specified one.
     * 
     * @param value
     *            The new width
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionRectangle setPreciseWidth(double value) {
        preciseWidth = value;
        updateWidthInt();
        return this;
    }

    /**
     * Sets the x value.
     * 
     * @param value
     *            The new x value
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionRectangle setPreciseX(double value) {
        preciseX = value;
        updateXInt();
        return this;
    }

    /**
     * Sets the y value.
     * 
     * @param value
     *            the new y value
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionRectangle setPreciseY(double value) {
        preciseY = value;
        updateYInt();
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setSize(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Rectangle setSize(Dimension d) {
        return setPreciseSize(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setSize(int, int)
     */
    @Override
    public Rectangle setSize(int w, int h) {
        return setPreciseSize(w, h);
    }

    /**
     * Sets the width.
     * 
     * @param value
     *            the new width
     * @deprecated Use {@link #setPreciseWidth(double)} instead.
     */
    public void setWidth(double value) {
        setPreciseWidth(value);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setWidth(int)
     */
    @Override
    public Rectangle setWidth(int width) {
        return setPreciseWidth(width);
    }

    /**
     * Sets the x value.
     * 
     * @param value
     *            the new x value
     * @deprecated Use {@link #setPreciseX(double)} instead.
     */
    public void setX(double value) {
        setPreciseX(value);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setX(int)
     */
    @Override
    public Rectangle setX(int value) {
        return setPreciseX(value);
    }

    /**
     * Sets the y value.
     * 
     * @param value
     *            the new y value
     * @deprecated Use {@link #setPreciseX(double)} instead.
     */
    public void setY(double value) {
        setPreciseY(value);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#setY(int)
     */
    @Override
    public Rectangle setY(int value) {
        return setPreciseY(value);
    }

    /**
     * Shrinks the sides of this Rectangle by the horizontal and vertical values
     * provided as input, and returns this Rectangle for convenience. The center
     * of this Rectangle is kept constant.
     * 
     * @param h
     *            Horizontal reduction amount
     * @param v
     *            Vertical reduction amount
     * @return <code>this</code> for convenience
     * @since 3.4
     */
    @Override
    public Rectangle shrink(double h, double v) {
        return shrinkPrecise(h, v);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#shrink(org.eclipse.draw2d.geometry.Insets)
     */
    @Override
    public Rectangle shrink(Insets insets) {
        if (insets == null)
            return this;
        setPreciseX(preciseX() + insets.left);
        setPreciseY(preciseY() + insets.top);
        setPreciseWidth(preciseWidth() - insets.getWidth());
        setPreciseHeight(preciseHeight() - insets.getHeight());
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#shrink(int, int)
     */
    @Override
    public Rectangle shrink(int h, int v) {
        return shrinkPrecise(h, v);
    }

    /**
     * Shrinks the sides of this Rectangle by the horizontal and vertical values
     * provided as input, and returns this Rectangle for convenience. The center
     * of this Rectangle is kept constant.
     * 
     * @param h
     *            Horizontal reduction amount
     * @param v
     *            Vertical reduction amount
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionRectangle shrinkPrecise(double h, double v) {
        setPreciseX(preciseX() + h);
        setPreciseWidth(preciseWidth() - (h + h));
        setPreciseY(preciseY() + v);
        setPreciseHeight(preciseHeight() - (v + v));
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#touches(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public boolean touches(Rectangle rect) {
        return rect.preciseX() <= preciseX() + preciseWidth()
                && rect.preciseY() <= preciseY() + preciseHeight()
                && rect.preciseX() + rect.preciseWidth() >= preciseX()
                && rect.preciseY() + rect.preciseHeight() >= preciseY();
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#translate(double, double)
     */
    @Override
    public Rectangle translate(double dx, double dy) {
        return translatePrecise(dx, dy);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#translate(int, int)
     */
    @Override
    public Rectangle translate(int dx, int dy) {
        return translatePrecise(dx, dy);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#translate(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Rectangle translate(Point p) {
        return translatePrecise(p.preciseX(), p.preciseY());
    }

    /**
     * Moves this Rectangle horizontally by dx and vertically by dy, then
     * returns this Rectangle for convenience.
     * 
     * @param dx
     *            Shift along X axis
     * @param dy
     *            Shift along Y axis
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionRectangle translatePrecise(double dx, double dy) {
        setPreciseX(preciseX() + dx);
        setPreciseY(preciseY() + dy);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#transpose()
     */
    @Override
    public Rectangle transpose() {
        double temp = preciseX();
        setPreciseX(preciseY());
        setPreciseY(temp);
        temp = preciseWidth();
        setPreciseWidth(preciseHeight());
        setPreciseHeight(temp);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#union(double, double)
     */
    @Override
    public Rectangle union(double x, double y) {
        return unionPrecise(x, y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#union(double, double, double,
     *      double)
     */
    @Override
    public Rectangle union(double x, double y, double w, double h) {
        return unionPrecise(x, y, w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#union(int, int)
     */
    @Override
    public Rectangle union(int x, int y) {
        return unionPrecise(x, y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#union(int, int, int, int)
     */
    @Override
    public Rectangle union(int x, int y, int w, int h) {
        return unionPrecise(x, y, w, h);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#union(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public void union(Point p) {
        unionPrecise(p.preciseX(), p.preciseY());
    }

    /**
     * Unions the given PrecisionRectangle with this rectangle and returns
     * <code>this</code> for convenience.
     * 
     * @since 3.0
     * @param rect
     *            the rectangle being unioned
     * @return <code>this</code> for convenience
     * @deprecated Use {@link #union(Rectangle)} instead
     */
    public PrecisionRectangle union(PrecisionRectangle rect) {
        if (rect == null || rect.isEmpty()) {
            return this;
        }
        return unionPrecise(rect.preciseX(), rect.preciseY(),
                rect.preciseWidth(), rect.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#union(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public Rectangle union(Rectangle rect) {
        if (rect == null || rect.isEmpty())
            return this;
        return unionPrecise(rect.preciseX(), rect.preciseY(),
                rect.preciseWidth(), rect.preciseHeight());
    }

    /**
     * Updates this Rectangle's bounds to the minimum size which can hold both
     * this Rectangle and the coordinate (x,y).
     * 
     * @return <code>this</code> for convenience
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @since 3.7
     */
    private PrecisionRectangle unionPrecise(double x, double y) {
        if (x < preciseX()) {
            setPreciseWidth(preciseWidth() + (preciseX() - x));
            setPreciseX(x);
        } else {
            double right = preciseX() + preciseWidth();
            if (x > right) {
                setPreciseWidth(x - preciseX());
            }
        }
        if (y < preciseY()) {
            setPreciseHeight(preciseHeight() + (preciseY() - y));
            setPreciseY(y);
        } else {
            double bottom = preciseY() + preciseHeight();
            if (y > bottom) {
                setPreciseHeight(y - preciseY());
            }
        }
        return this;
    }

    /**
     * Updates this Rectangle's dimensions to the minimum size which can hold
     * both this Rectangle and the rectangle (x, y, w, h).
     * 
     * @param x
     *            X coordinate of desired union.
     * @param y
     *            Y coordinate of desired union.
     * @param w
     *            Width of desired union.
     * @param h
     *            Height of desired union.
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    private PrecisionRectangle unionPrecise(double x, double y, double w,
            double h) {
        double right = Math.max(preciseX() + preciseWidth(), x + w);
        double bottom = Math.max(preciseY() + preciseHeight(), y + h);
        setPreciseX(Math.min(preciseX(), x));
        setPreciseY(Math.min(preciseY(), y));
        setPreciseWidth(right - preciseX());
        setPreciseHeight(bottom - preciseY());
        return this;
    }

    /**
     * Updates the height integer field using the value of preciseHeight.
     */
    private final void updateHeightInt() {
        height = PrecisionGeometry.doubleToInteger(preciseHeight);
    }

    /**
     * Updates the integer values based on the current precise values.
     * 
     * @deprecated This method should not be accessed by clients any more (it
     *             will be made private in future releases). The update of
     *             integer and precision fields is performed automatically if
     *             {@link #preciseX}, {@link #preciseY}, {@link #preciseWidth},
     *             and {@link #preciseHeight} field values are not manipulated
     *             directly, but only via respective methods offered by this
     *             class.
     * 
     * @since 3.0
     */
    public void updateInts() {
        updateXInt();
        updateYInt();
        updateWidthInt();
        updateHeightInt();
    }

    /**
     * Updates the preciseHeight double field using the value of height.
     */
    private final void updatePreciseHeightDouble() {
        if (height != PrecisionGeometry.doubleToInteger(preciseHeight)) {
            preciseHeight = height;
        }
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
     * Updates the preciseX double field using the value of x.
     */
    private final void updatePreciseXDouble() {
        if (x != PrecisionGeometry.doubleToInteger(preciseX)) {
            preciseX = x;
        }
    }

    /**
     * Updates the preciseY double field using the value of y.
     */
    private final void updatePreciseYDouble() {
        if (y != PrecisionGeometry.doubleToInteger(preciseY)) {
            preciseY = y;
        }
    }

    /**
     * Updates the width integer field using the value of preciseWidth.
     */
    private final void updateWidthInt() {
        width = PrecisionGeometry.doubleToInteger(preciseWidth);
    }

    /**
     * Updates the x integer field using the value of preciseX.
     */
    private final void updateXInt() {
        x = PrecisionGeometry.doubleToInteger(preciseX);
    }

    /**
     * Updates the y integer field using the value of preciseY.
     */
    private final void updateYInt() {
        y = PrecisionGeometry.doubleToInteger(preciseY);
    }

}
