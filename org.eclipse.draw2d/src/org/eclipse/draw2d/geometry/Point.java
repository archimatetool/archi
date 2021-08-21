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

import org.eclipse.draw2d.PositionConstants;

/**
 * Represents a point (x, y) in 2-dimensional space. This class provides various
 * methods for manipulating this Point or creating new derived geometrical
 * Objects.
 */
public class Point implements Cloneable, java.io.Serializable, Translatable {

    private static final long serialVersionUID = 1L;

    /**
     * A singleton for use in short calculations
     */
    public static final Point SINGLETON = new Point();

    /**
     * Creates a new Point representing the MAX of two provided Points.
     * 
     * @param p1
     *            first point
     * @param p2
     *            second point
     * @return A new Point representing the Max()
     */
    public static Point max(Point p1, Point p2) {
        return new Rectangle(p1, p2).getBottomRight().translate(-1, -1);
    }

    /**
     * Creates a new Point representing the MIN of two provided Points.
     * 
     * @param p1
     *            first point
     * @param p2
     *            second point
     * @return A new Point representing the Min()
     */
    public static Point min(Point p1, Point p2) {
        return new Rectangle(p1, p2).getTopLeft();
    }

    /**
     * x value
     */
    public int x;

    /**
     * y value
     */
    public int y;

    /**
     * Constructs a Point at location (0,0).
     * 
     * @since 2.0
     */
    public Point() {
    }

    /**
     * Constructs a Point at the specified x and y locations.
     * 
     * @param x
     *            x value
     * @param y
     *            y value
     * @since 2.0
     * @deprecated Use {@link PrecisionPoint} or {@link #Point(int, int)}
     *             instead.
     */
    public Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    /**
     * Constructs a Point at the specified x and y locations.
     * 
     * @param x
     *            x value
     * @param y
     *            y value
     * @since 2.0
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Point at the same location as the given SWT Point.
     * 
     * @param p
     *            Point from which the initial values are taken.
     * @since 2.0
     */
    public Point(org.eclipse.swt.graphics.Point p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Constructs a Point at the same location as the given Point.
     * 
     * @param p
     *            Point from which the initial values are taken.
     * @since 2.0
     */
    public Point(Point p) {
        x = p.x();
        y = p.y();
    }

    /**
     * Returns <code>true</code> if this Points x and y are equal to the given x
     * and y.
     * 
     * @param x
     *            the x value
     * @param y
     *            the y value
     * @return <code>true</code> if this point's x and y are equal to those
     *         given.
     * @since 3.7
     */
    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    /**
     * Test for equality.
     * 
     * @param o
     *            Object being tested for equality
     * @return true if both x and y values are equal
     * @since 2.0
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return p.x() == x && p.y() == y;
        }
        return false;
    }

    /**
     * @return a copy of this Point
     * @since 2.0
     */
    public Point getCopy() {
        return new Point(this);
    }

    /**
     * Calculates the difference in between this Point and the one specified.
     * 
     * @param p
     *            The Point being subtracted from this Point
     * @return A new Dimension representing the difference
     * @since 2.0
     */
    public Dimension getDifference(Point p) {
        return new Dimension(this.x - p.x(), this.y - p.y());
    }

    /**
     * Calculates the distance from this Point to the one specified.
     * 
     * @param p
     *            The Point being compared to this
     * @return The distance
     * @since 2.0
     */
    public double getDistance(Point p) {
        double i = p.preciseX() - preciseX();
        double j = p.preciseY() - preciseY();
        return Math.sqrt(i * i + j * j);
    }

    /**
     * Calculates the distance squared between this Point and the one specified.
     * If the distance squared is larger than the maximum integer value, then
     * <code>Integer.MAX_VALUE</code> will be returned.
     * 
     * @param p
     *            The reference Point
     * @return distance<sup>2</sup>
     * @since 2.0
     * @deprecated Use {@link #getDistance(Point)} and square the result
     *             instead.
     */
    public int getDistance2(Point p) {
        long i = p.x() - x;
        long j = p.y() - y;
        long result = i * i + j * j;
        if (result > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (int) result;
    }

    /**
     * Calculates the orthogonal distance to the specified point. The orthogonal
     * distance is the sum of the horizontal and vertical differences.
     * 
     * @param p
     *            The reference Point
     * @return the orthogonal distance
     * @deprecated May not be guaranteed by precision subclasses and should thus
     *             not be used any more.
     */
    public int getDistanceOrthogonal(Point p) {
        return Math.abs(y - p.y()) + Math.abs(x - p.x());
    }

    /**
     * Creates a Point with negated x and y values.
     * 
     * @return A new Point
     * @since 2.0
     */
    public Point getNegated() {
        return getCopy().negate();
    }

    /**
     * Calculates the relative position of the specified Point to this Point.
     * 
     * @param p
     *            The reference Point
     * @return NORTH, SOUTH, EAST, or WEST, as defined in
     *         {@link PositionConstants}
     */
    public int getPosition(Point p) {
        int dx = p.x() - x;
        int dy = p.y() - y;
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx < 0)
                return PositionConstants.WEST;
            return PositionConstants.EAST;
        }
        if (dy < 0)
            return PositionConstants.NORTH;
        return PositionConstants.SOUTH;
    }

    /**
     * Creates a new Point from this Point by scaling by the specified amount.
     * 
     * @param factor
     *            scale factor
     * @return A new Point
     * @since 2.0
     */
    public Point getScaled(double factor) {
        return getCopy().scale(factor);
    }

    /**
     * Creates a new Point from this Point by scaling by the specified x and y
     * factors.
     * 
     * @param xFactor
     *            x scale factor
     * @param yFactor
     *            y scale factor
     * @return A new Point
     * @since 3.8
     */
    public Point getScaled(double xFactor, double yFactor) {
        return getCopy().scale(xFactor, yFactor);
    }

    /**
     * Creates a new SWT {@link org.eclipse.swt.graphics.Point Point} from this
     * Point.
     * 
     * @return A new SWT Point
     * @since 2.0
     */
    public org.eclipse.swt.graphics.Point getSWTPoint() {
        return new org.eclipse.swt.graphics.Point(x, y);
    }

    /**
     * Creates a new Point which is translated by the values of the input
     * Dimension.
     * 
     * @param d
     *            Dimension which provides the translation amounts.
     * @return A new Point
     * @since 2.0
     */
    public Point getTranslated(Dimension d) {
        return getCopy().translate(d);
    }

    /**
     * Creates a new Point which is translated by the specified x and y values
     * 
     * @param x
     *            horizontal component
     * @param y
     *            vertical component
     * @return A new Point
     * @since 3.8
     */
    public Point getTranslated(double x, double y) {
        return getCopy().translate(x, y);
    }

    /**
     * Creates a new Point which is translated by the specified x and y values
     * 
     * @param x
     *            horizontal component
     * @param y
     *            vertical component
     * @return A new Point
     * @since 2.0
     */
    public Point getTranslated(int x, int y) {
        return getCopy().translate(x, y);
    }

    /**
     * Creates a new Point which is translated by the values of the provided
     * Point.
     * 
     * @param p
     *            Point which provides the translation amounts.
     * @return A new Point
     * @since 2.0
     */
    public Point getTranslated(Point p) {
        return getCopy().translate(p);
    }

    /**
     * Creates a new Point with the transposed values of this Point. Can be
     * useful in orientation change calculations.
     * 
     * @return A new Point
     * @since 2.0
     */
    public Point getTransposed() {
        return getCopy().transpose();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (x * y) ^ (x + y);
    }

    /**
     * Negates the x and y values of this Point.
     * 
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Point negate() {
        x = -x;
        y = -y;
        return this;
    }

    /** @see Translatable#performScale(double) */
    @Override
    public void performScale(double factor) {
        scale(factor);
    }

    /** @see Translatable#performTranslate(int, int) */
    @Override
    public void performTranslate(int dx, int dy) {
        translate(dx, dy);
    }

    /**
     * Returns <code>double</code> x coordinate
     * 
     * @return <code>double</code> x coordinate
     * @since 3.4
     */
    public double preciseX() {
        return x;
    }

    /**
     * Returns <code>double</code> y coordinate
     * 
     * @return <code>double</code> y coordinate
     * @since 3.4
     */
    public double preciseY() {
        return y;
    }

    /**
     * Scales this Point by the specified amount.
     * 
     * @return <code>this</code> for convenience
     * @param factor
     *            scale factor
     * @since 2.0
     */
    public Point scale(double factor) {
        return scale(factor, factor);
    }

    /**
     * Scales this Point by the specified values.
     * 
     * @param xFactor
     *            horizontal scale factor
     * @param yFactor
     *            vertical scale factor
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Point scale(double xFactor, double yFactor) {
        x = (int) Math.floor(x * xFactor);
        y = (int) Math.floor(y * yFactor);
        return this;
    }

    /**
     * Sets the location of this Point to the provided x and y locations.
     * 
     * @return <code>this</code> for convenience
     * @param x
     *            the x location
     * @param y
     *            the y location
     * @since 2.0
     */
    public Point setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets the location of this Point to the specified Point.
     * 
     * @return <code>this</code> for convenience
     * @param p
     *            the Location
     * @since 2.0
     */
    public Point setLocation(Point p) {
        x = p.x();
        y = p.y();
        return this;
    }

    /**
     * Sets the x value of this Point to the given value.
     * 
     * @param x
     *            The new x value
     * @return this for convenience
     * @since 3.7
     */
    public Point setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the y value of this Point to the given value;
     * 
     * @param y
     *            The new y value
     * @return this for convenience
     * @since 3.7
     */
    public Point setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * @return String representation.
     * @since 2.0
     */
    @Override
    public String toString() {
        return "Point(" + preciseX() + ", " + preciseY() + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * Shifts this Point by the values of the Dimension along each axis, and
     * returns this for convenience.
     * 
     * @param d
     *            Dimension by which the origin is being shifted.
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Point translate(Dimension d) {
        return translate(d.width(), d.height());
    }

    /**
     * Shifts this Point by the values supplied along each axes, and returns
     * this for convenience.
     * 
     * @param x
     *            Amount by which point is shifted along X axis.
     * @param y
     *            Amount by which point is shifted along Y axis.
     * @return <code>this</code> for convenience
     * @since 3.8
     */
    public Point translate(double x, double y) {
        return translate((int) x, (int) y);
    }

    /**
     * Shifts this Point by the values supplied along each axes, and returns
     * this for convenience.
     * 
     * @param dx
     *            Amount by which point is shifted along X axis.
     * @param dy
     *            Amount by which point is shifted along Y axis.
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Point translate(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }

    /**
     * Shifts the location of this Point by the location of the input Point
     * along each of the axes, and returns this for convenience.
     * 
     * @param p
     *            Point to which the origin is being shifted.
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Point translate(Point p) {
        return translate(p.x(), p.y());
    }

    /**
     * Transposes this object. X and Y values are exchanged.
     * 
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Point transpose() {
        int temp = x;
        x = y;
        y = temp;
        return this;
    }

    /**
     * Returns the x value of this Point.
     * 
     * @return The current x value
     * @since 3.7
     */
    public int x() {
        return x;
    }

    /**
     * Returns the y value of this Point.
     * 
     * @return The current y value
     * @since 3.7
     */
    public int y() {
        return y;
    }

}
