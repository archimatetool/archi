/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation 
 *     Mariot Chauvin <mariot.chauvin@obeo.fr> - bug 260740
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

import org.eclipse.draw2d.PositionConstants;

/**
 * Represents a Rectangle(x, y, width, height). This class provides various
 * methods for manipulating this Rectangle or creating new derived geometrical
 * Objects.
 */
public class Rectangle implements Cloneable, java.io.Serializable, Translatable {

    private static final long serialVersionUID = 1L;

    /**
     * A singleton for use in short calculations. Use to avoid newing
     * unnecessary objects.
     */
    public static final Rectangle SINGLETON = new Rectangle();

    /**
     * The height
     */
    public int height;

    /**
     * The width
     */
    public int width;

    /**
     * The x value
     */
    public int x;

    /**
     * The y value
     */
    public int y;

    /**
     * Constructs a Rectangle at the origin with zero width and height.
     * 
     * @since 2.0
     */
    public Rectangle() {
    }

    /**
     * Constructs a Rectangle with the provided values.
     * 
     * @param x
     *            X location
     * @param y
     *            Y location
     * @param width
     *            Width of the rectangle
     * @param height
     *            Height of the rectangle
     * @since 2.0
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a copy of the provided SWT
     * {@link org.eclipse.swt.graphics.Rectangle}.
     * 
     * @param rect
     *            The SWT Rectangle being copied
     * @since 2.0
     */
    public Rectangle(org.eclipse.swt.graphics.Rectangle rect) {
        this(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Constructs a Rectangle given a location and size.
     * 
     * @param p
     *            the location
     * @param size
     *            the size
     * @since 2.0
     */
    public Rectangle(Point p, Dimension size) {
        this(p.x(), p.y(), size.width(), size.height());
    }

    /**
     * Constructs the smallest Rectangle that contains the specified Points.
     * 
     * @param p1
     *            Upper left hand corner
     * @param p2
     *            Lower right hand corner
     * @since 2.0
     */
    public Rectangle(Point p1, Point p2) {
        this.x = Math.min(p1.x(), p2.x());
        this.y = Math.min(p1.y(), p2.y());
        this.width = Math.abs(p2.x() - p1.x()) + 1;
        this.height = Math.abs(p2.y() - p1.y()) + 1;
    }

    /**
     * Constructs a copy of the provided Rectangle.
     * 
     * @param rect
     *            Rectangle supplying the initial values
     * @since 2.0
     */
    public Rectangle(Rectangle rect) {
        this(rect.x(), rect.y(), rect.width(), rect.height());
    }

    /**
     * Returns the y-coordinate of the bottom of this Rectangle.
     * 
     * @return The Y coordinate of the bottom
     * @since 2.0
     */
    public int bottom() {
        return y + height;
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
     * @since 3.8
     */
    public boolean contains(double x, double y) {
        return contains((int) x, (int) y);
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
     * @since 2.0
     */
    public boolean contains(int x, int y) {
        return y >= this.y && y < this.y + this.height && x >= this.x
                && x < this.x + this.width;
    }

    /**
     * Returns whether the given point is within the boundaries of this
     * Rectangle. The boundaries are inclusive of the top and left edges, but
     * exclusive of the bottom and right edges.
     * 
     * @param p
     *            Point being tested for containment
     * @return true if the Point is within this Rectangle
     * @since 2.0
     */
    public boolean contains(Point p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns <code>true</code> if the given rectangle is contained within the
     * boundaries of this Rectangle.
     * 
     * @param rect
     *            the Rectangle to test
     * @return true if the Rectangle is within this Rectangle
     */
    public boolean contains(Rectangle rect) {
        return x <= rect.x() && y <= rect.y() && right() >= rect.right()
                && bottom() >= rect.bottom();
    }

    /**
     * Crops this rectangle by the amount specified in <code>insets</code>.
     * 
     * @param insets
     *            Insets to be removed from the Rectangle
     * @return <code>this</code> for convenience
     * @since 2.0
     * @deprecated Use {@link #shrink(Insets)} instead.
     */
    public Rectangle crop(Insets insets) {
        return shrink(insets);
    }

    /**
     * Returns <code>true</code> if this Rectangle's x, y, width, and height
     * values are identical to the provided ones.
     * 
     * @param x
     *            The x value to test
     * @param y
     *            The y value to test
     * @param width
     *            The width value to test
     * @param height
     *            The height value to test
     * @return <code>true</code> if this Rectangle's x, y, width, and height
     *         values are identical to the provided ones, <code>false</code>
     *         otherwise
     * @since 3.7
     */
    public boolean equals(int x, int y, int width, int height) {
        return this.x == x && this.y == y && this.width == width
                && this.height == height;
    }

    /**
     * Returns whether the input object is equal to this Rectangle or not.
     * Rectangles are equivalent if their x, y, height, and width values are the
     * same.
     * 
     * @param o
     *            Object being tested for equality
     * @return Returns the result of the equality test
     * @since 2.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Rectangle) {
            Rectangle r = (Rectangle) o;
            return (x == r.x()) && (y == r.y()) && (width == r.width())
                    && (height == r.height());
        }
        return false;
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
     * @since 3.8
     */
    public Rectangle expand(double h, double v) {
        return expand((int) h, (int) v);
    }

    /**
     * Expands the horizontal and vertical sides of this Rectangle by the width
     * and height of the given Insets, and returns this for convenience.
     * 
     * @param insets
     *            contains the amounts to expand on each side
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle expand(Insets insets) {
        x -= insets.left;
        y -= insets.top;
        width += insets.getWidth();
        height += insets.getHeight();
        return this;
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
     * @since 2.0
     */
    public Rectangle expand(int h, int v) {
        return shrink(-h, -v);
    }

    /**
     * Returns a new Point representing the middle point of the bottom side of
     * this Rectangle.
     * 
     * @return Point at the bottom of the Rectangle
     * @since 2.0
     */
    public Point getBottom() {
        return new Point(x + width / 2, bottom());
    }

    /**
     * Returns a new Point representing the bottom left point of this Rectangle.
     * 
     * @return Point at the bottom left of the rectangle
     * @since 2.0
     */
    public Point getBottomLeft() {
        return new Point(x, y + height);
    }

    /**
     * Returns a new Point representing the bottom right point of this
     * Rectangle.
     * 
     * @return Point at the bottom right of the rectangle
     * @since 2.0
     */
    public Point getBottomRight() {
        return new Point(x + width, y + height);
    }

    /**
     * Returns a new point representing the center of this Rectangle.
     * 
     * @return Point at the center of the rectangle
     */
    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }

    /**
     * Returns a new Rectangle which has the exact same parameters as this
     * Rectangle.
     * 
     * @return Copy of this Rectangle
     * @since 2.0
     */
    public Rectangle getCopy() {
        // FIXME: This is inconsistent with the behavior of getCopy() within
        // Point and Dimension and should only return a new Rectangle, leaving
        // the property copying of others to subclasses. Clients should
        // not relay on this method performing a clone()
        if (getClass() == Rectangle.class) {
            /* avoid clone() call cost see bug #260740 */
            return new Rectangle(this);
        } else {
            try {
                return (Rectangle) clone();
            } catch (CloneNotSupportedException exc) {
                return new Rectangle(this);
            }
        }
    }

    /**
     * Returns a new Rectangle with the specified insets cropped.
     * 
     * @param insets
     *            Insets being cropped from the Rectangle
     * @return Cropped new Rectangle
     * @deprecated Use {@link #getShrinked(Insets)} instead.
     */
    public Rectangle getCropped(Insets insets) {
        return getShrinked(insets);
    }

    /**
     * Returns a new incremented Rectangle, where the sides are expanded by the
     * horizontal and vertical values provided. The center of the Rectangle is
     * maintained constant.
     * 
     * @param h
     *            Horizontal increment
     * @param v
     *            Vertical increment
     * @return A new expanded Rectangle
     * @since 3.8
     */
    public Rectangle getExpanded(double h, double v) {
        return getCopy().expand(h, v);
    }

    /**
     * Creates and returns a new Rectangle with the bounds of <code>this</code>
     * Rectangle, expanded by the given Insets.
     * 
     * @param insets
     *            The insets used to expand this rectangle
     * @return A new expanded Rectangle
     * @since 2.0
     */
    public Rectangle getExpanded(Insets insets) {
        return getCopy().expand(insets);
    }

    /**
     * Returns a new incremented Rectangle, where the sides are expanded by the
     * horizontal and vertical values provided. The center of the Rectangle is
     * maintained constant.
     * 
     * @param h
     *            Horizontal increment
     * @param v
     *            Vertical increment
     * @return A new expanded Rectangle
     * @since 2.0
     */
    public Rectangle getExpanded(int h, int v) {
        return getCopy().expand(h, v);
    }

    /**
     * Returns a new Rectangle which has the intersection of this Rectangle and
     * the rectangle provided as input. Returns an empty Rectangle if there is
     * no intersection.
     * 
     * @param rect
     *            Rectangle provided to test for intersection
     * @return A new Rectangle representing the intersection
     * @since 2.0
     */
    public Rectangle getIntersection(Rectangle rect) {
        return getCopy().intersect(rect);
    }

    /**
     * Returns a new Point representing the middle point of the left hand side
     * of this Rectangle.
     * 
     * @return Point at the left of the Rectangle
     */
    public Point getLeft() {
        return new Point(x, y + height / 2);
    }

    /**
     * Returns the upper left hand corner of the rectangle.
     * 
     * @return Location of the rectangle
     * @see #setLocation(Point)
     */
    public Point getLocation() {
        return new Point(x, y);
    }

    /**
     * <P>
     * Returns an integer which represents the position of the given point with
     * respect to this rectangle. Possible return values are bitwise ORs of the
     * constants WEST, EAST, NORTH, and SOUTH as found in
     * {@link org.eclipse.draw2d.PositionConstants}.
     * 
     * <P>
     * Returns PositionConstant.NONE if the given point is inside this
     * Rectangle.
     * 
     * @param p
     *            The Point whose position has to be determined
     * @return An <code>int</code> which is a PositionConstant
     * @see org.eclipse.draw2d.PositionConstants
     * @since 2.0
     */
    public int getPosition(Point p) {
        int result = PositionConstants.NONE;

        if (contains(p))
            return result;

        if (p.x() < x)
            result = PositionConstants.WEST;
        else if (p.x() >= (x + width))
            result = PositionConstants.EAST;

        if (p.y() < y)
            result = result | PositionConstants.NORTH;
        else if (p.y() >= (y + height))
            result = result | PositionConstants.SOUTH;

        return result;
    }

    /**
     * Returns a new Rectangle which is equivalent to this Rectangle with its
     * dimensions modified by the passed Dimension <i>d</i>.
     * 
     * @param d
     *            Dimensions by which the rectangle's size should be modified
     * @return The new rectangle with the modified dimensions
     * @since 2.0
     */
    public Rectangle getResized(Dimension d) {
        return getCopy().resize(d);
    }

    /**
     * Returns a new Rectangle which is equivalent to this Rectangle with its
     * dimensions modified by the passed width <i>w</i> and height <i>h</i>.
     * 
     * @param w
     *            Amount by which width is to be resized
     * @param h
     *            Amount by which height is to be resized
     * @return a new rectangle with its width and height modified
     * @since 3.8
     */
    public Rectangle getResized(double w, double h) {
        return getCopy().resize(w, h);
    }

    /**
     * Returns a new Rectangle which is equivalent to this Rectangle with its
     * dimensions modified by the passed width <i>w</i> and height <i>h</i>.
     * 
     * @param w
     *            Amount by which width is to be resized
     * @param h
     *            Amount by which height is to be resized
     * @return a new rectangle with its width and height modified
     */
    public Rectangle getResized(int w, int h) {
        return getCopy().resize(w, h);
    }

    /**
     * Returns a new Point which represents the middle point of the right hand
     * side of this Rectangle.
     * 
     * @return Point at the right of the Rectangle
     * @since 2.0
     */
    public Point getRight() {
        return new Point(right(), y + height / 2);
    }

    /**
     * Returns a new Rectangle, where the sides are shrinked by the horizontal
     * and vertical values supplied. The center of this Rectangle is kept
     * constant.
     * 
     * @param h
     *            Horizontal reduction amount
     * @param v
     *            Vertical reduction amount
     * @return <code>this</code> for convenience
     * @since 3.8
     */
    public Rectangle getShrinked(double h, double v) {
        return getCopy().shrink(h, v);
    }

    /**
     * Returns a new Rectangle shrinked by the specified insets.
     * 
     * @param insets
     *            Insets being cropped from the Rectangle
     * @return Shrinked new Rectangle
     * @since 3.7
     */
    public Rectangle getShrinked(Insets insets) {
        return getCopy().shrink(insets);
    }

    /**
     * Returns a new Rectangle, where the sides are shrinked by the horizontal
     * and vertical values supplied. The center of this Rectangle is kept
     * constant.
     * 
     * @param h
     *            Horizontal reduction amount
     * @param v
     *            Vertical reduction amount
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    public Rectangle getShrinked(int h, int v) {
        return getCopy().shrink(h, v);
    }

    /**
     * Retuns the dimensions of this Rectangle.
     * 
     * @return Size of this Rectangle as a Dimension
     * @since 2.0
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * Returns a new Point which represents the middle point of the top side of
     * this Rectangle.
     * 
     * @return Point at the top of the Rectangle
     * @since 2.0
     */
    public Point getTop() {
        return new Point(x + width / 2, y);
    }

    /**
     * Returns a new Point which represents the top left hand corner of this
     * Rectangle.
     * 
     * @return Point at the top left of the rectangle
     * @since 2.0
     */
    public Point getTopLeft() {
        return new Point(x, y);
    }

    /**
     * Returns a new Point which represents the top right hand corner of this
     * Rectangle.
     * 
     * @return Point at the top right of the rectangle
     * @since 2.0
     */
    public Point getTopRight() {
        return new Point(x + width, y);
    }

    /**
     * Returns a new Rectangle which is shifted along each axis by the passed
     * values.
     * 
     * @param dx
     *            Displacement along X axis
     * @param dy
     *            Displacement along Y axis
     * @return The new translated rectangle
     * @since 3.8
     */
    public Rectangle getTranslated(double dx, double dy) {
        return getCopy().translate(dx, dy);
    }

    /**
     * Returns a new Rectangle which is shifted along each axis by the passed
     * values.
     * 
     * @param dx
     *            Displacement along X axis
     * @param dy
     *            Displacement along Y axis
     * @return The new translated rectangle
     * @since 2.0
     */
    public Rectangle getTranslated(int dx, int dy) {
        return getCopy().translate(dx, dy);
    }

    /**
     * Returns a new Rectangle which is shifted by the position of the given
     * Point.
     * 
     * @param pt
     *            Point providing the amount of shift along each axis
     * @return The new translated Rectangle
     * @since 2.0
     */
    public Rectangle getTranslated(Point pt) {
        return getCopy().translate(pt);
    }

    /**
     * Returns a new rectangle whose width and height have been interchanged, as
     * well as its x and y values. This can be useful in orientation changes.
     * 
     * @return The transposed rectangle
     * @since 2.0
     */
    public Rectangle getTransposed() {
        return getCopy().transpose();
    }

    /**
     * Returns a new Rectangle which contains both this Rectangle and the Point
     * supplied as input.
     * 
     * @param p
     *            Point for calculating union
     * @return A new unioned Rectangle
     * @since 3.7
     */
    public Rectangle getUnion(Point p) {
        Rectangle copy = getCopy();
        copy.union(p);
        return copy;
    }

    /**
     * Returns a new Rectangle which contains both this Rectangle and the
     * Rectangle supplied as input.
     * 
     * @param rect
     *            Rectangle for calculating union
     * @return A new unioned Rectangle
     * @since 2.0
     */
    public Rectangle getUnion(Rectangle rect) {
        return getCopy().union(rect);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (x + height + 1) * (y + width + 1) ^ x ^ y;
    }

    /**
     * Returns the current height of this Rectangle
     * 
     * @return The current height
     * @since 3.7
     */
    public int height() {
        return height;
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
     * @since 2.0
     */
    public Rectangle intersect(Rectangle rect) {
        int x1 = Math.max(x, rect.x());
        int x2 = Math.min(x + width, rect.x() + rect.width());
        int y1 = Math.max(y, rect.y());
        int y2 = Math.min(y + height, rect.y() + rect.height());
        if (((x2 - x1) < 0) || ((y2 - y1) < 0))
            return setBounds(0, 0, 0, 0); // no intersection
        else {
            return setBounds(x1, y1, x2 - x1, y2 - y1);
        }
    }

    /**
     * Returns <code>true</code> if the input Rectangle intersects this
     * Rectangle.
     * 
     * @param rect
     *            Rectangle for the intersection test
     * @return <code>true</code> if the input Rectangle intersects this
     *         Rectangle
     * @since 2.0
     */
    public boolean intersects(Rectangle rect) {
        return !getIntersection(rect).isEmpty();
    }

    /**
     * Returns <code>true</code> if this Rectangle's width or height is less
     * than or equal to 0.
     * 
     * @return <code>true</code> if this Rectangle is empty
     * @since 2.0
     */
    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }

    /**
     * @see Translatable#performScale(double)
     */
    @Override
    public void performScale(double factor) {
        scale(factor);
    }

    /**
     * @see Translatable#performTranslate(int, int)
     */
    @Override
    public void performTranslate(int dx, int dy) {
        translate(dx, dy);
    }

    /**
     * Returns <code>double</code> height
     * 
     * @return <code>double</code> height
     * @since 3.4
     */
    public double preciseHeight() {
        return height;
    }

    /**
     * Returns <code>double</code> width
     * 
     * @return <code>double</code> width
     * @since 3.4
     */
    public double preciseWidth() {
        return width;
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
     * Resizes this Rectangle by the Dimension provided as input and returns
     * this for convenience. This Rectange's width will become this.width +
     * sizeDelta.width. Likewise for height.
     * 
     * @param d
     *            Resize data as a Dimension
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle resize(Dimension d) {
        width += d.width();
        height += d.height();
        return this;
    }

    /**
     * Resizes this Rectangle by the values supplied as input and returns this
     * for convenience. This Rectangle's width will become this.width + dw. This
     * Rectangle's height will become this.height + dh.
     * 
     * @param w
     *            Amount by which width is to be resized
     * @param h
     *            Amount by which height is to be resized
     * @return <code>this</code> for convenience
     * @since 3.8
     */
    public Rectangle resize(double w, double h) {
        return resize((int) w, (int) h);
    }

    /**
     * Resizes this Rectangle by the values supplied as input and returns this
     * for convenience. This Rectangle's width will become this.width + dw. This
     * Rectangle's height will become this.height + dh.
     * 
     * @param w
     *            Amount by which width is to be resized
     * @param h
     *            Amount by which height is to be resized
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle resize(int w, int h) {
        width += w;
        height += h;
        return this;
    }

    /**
     * Returns the x-coordinate of the right side of this Rectangle.
     * 
     * @return The X coordinate of the right side
     * @since 2.0
     */
    public int right() {
        return x + width;
    }

    /**
     * Scales the location and size of this Rectangle by the given scale and
     * returns this for convenience.
     * 
     * @param scaleFactor
     *            The factor by which this rectangle will be scaled
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public final Rectangle scale(double scaleFactor) {
        return scale(scaleFactor, scaleFactor);
    }

    /**
     * Scales the location and size of this Rectangle by the given scales and
     * returns this for convenience.
     * 
     * @param scaleX
     *            the factor by which the X dimension has to be scaled
     * @param scaleY
     *            the factor by which the Y dimension has to be scaled
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle scale(double scaleX, double scaleY) {
        int oldX = x;
        int oldY = y;
        x = (int) (Math.floor(x * scaleX));
        y = (int) (Math.floor(y * scaleY));
        width = (int) (Math.ceil((oldX + width) * scaleX)) - x;
        height = (int) (Math.ceil((oldY + height) * scaleY)) - y;
        return this;
    }

    /**
     * Sets the x, y, width, and height values of this Rectangle to the provided
     * values.
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
    public Rectangle setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Sets the location and size of this rectangle to the provided ones.
     * 
     * @param location
     *            The new location
     * @param size
     *            The new size
     * @return this for convenience
     * @since 3.7
     */
    public Rectangle setBounds(Point location, Dimension size) {
        return setBounds(location.x(), location.y(), size.width(),
                size.height());
    }

    /**
     * Sets the parameters of this Rectangle from the Rectangle passed in and
     * returns this for convenience.
     * 
     * @return <code>this</code> for convenience
     * @param rect
     *            Rectangle providing the bounding values
     * @since 2.0
     */
    public Rectangle setBounds(Rectangle rect) {
        return setBounds(rect.x(), rect.y(), rect.width(), rect.height());
    }

    /**
     * Sets the height of this Rectangle to the specified one.
     * 
     * @param height
     *            The new height
     * @return this for convenience.
     * @since 3.7
     */
    public Rectangle setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * Sets the location of this Rectangle to the coordinates given as input and
     * returns this for convenience.
     * 
     * @param x
     *            The new X coordinate
     * @param y
     *            The new Y coordinate
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets the location of this Rectangle to the point given as input and
     * returns this for convenience.
     * 
     * @return <code>this</code> for convenience
     * @param p
     *            New position of this Rectangle
     * @since 2.0
     */
    public Rectangle setLocation(Point p) {
        return setLocation(p.x(), p.y());
    }

    /**
     * Sets the width and height of this Rectangle to the width and height of
     * the given Dimension and returns this for convenience.
     * 
     * @param d
     *            The new Dimension
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle setSize(Dimension d) {
        return setSize(d.width(), d.height());
    }

    /**
     * Sets the width of this Rectangle to <i>w</i> and the height of this
     * Rectangle to <i>h</i> and returns this for convenience.
     * 
     * @return <code>this</code> for convenience
     * @param w
     *            The new width
     * @param h
     *            The new height
     * @since 2.0
     */
    public Rectangle setSize(int w, int h) {
        width = w;
        height = h;
        return this;
    }

    /**
     * Sets the width of this Rectangle to the specified one.
     * 
     * @param width
     *            The new width
     * @return this for convenience.
     * @since 3.7
     */
    public Rectangle setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the x value of the Rectangle and returns this for convenience.
     * 
     * @return <code>this</code> for convenience
     * @param x
     *            The new x value
     * @since 3.7
     */
    public Rectangle setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the y value of the Rectangle and returns this for convenience.
     * 
     * @return <code>this</code> for convenience
     * @param y
     *            The new y value
     * @since 3.7
     */
    public Rectangle setY(int y) {
        this.y = y;
        return this;
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
     * @since 3.8
     */
    public Rectangle shrink(double h, double v) {
        return shrink((int) h, (int) v);
    }

    /**
     * Shrinks this rectangle by the amount specified in <code>insets</code>.
     * 
     * @param insets
     *            Insets to be removed from the Rectangle
     * @return <code>this</code> for convenience
     * @since 3.7
     */
    public Rectangle shrink(Insets insets) {
        if (insets == null)
            return this;
        x += insets.left;
        y += insets.top;
        width -= (insets.getWidth());
        height -= (insets.getHeight());
        return this;
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
     * @since 2.0
     */
    public Rectangle shrink(int h, int v) {
        x += h;
        width -= (h + h);
        y += v;
        height -= (v + v);
        return this;
    }

    /**
     * Returns the description of this Rectangle.
     * 
     * @return String containing the description
     * @since 2.0
     */
    @Override
    public String toString() {
        return "Rectangle(" + preciseX() + ", " + preciseY() + ", " + //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
                preciseWidth() + ", " + preciseHeight() + ")";//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * Returns <code>true</code> if the input Rectangle touches this Rectangle.
     * 
     * @param rect
     *            Rectangle being checked for contact
     * @return <code>true</code> if rect touches this Rectangle
     * @since 2.0
     */
    public boolean touches(Rectangle rect) {
        return rect.x() <= x + width && rect.y() <= y + height
                && rect.x() + rect.width() >= x
                && rect.y() + rect.height() >= y;
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
     * @since 3.8
     */
    public Rectangle translate(double dx, double dy) {
        return translate((int) dx, (int) dy);
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
     * @since 2.0
     */
    public Rectangle translate(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }

    /**
     * Moves this Rectangle horizontally by the x value of the given Point and
     * vertically by the y value of the given Point, then returns this Rectangle
     * for convenience.
     * 
     * @param p
     *            Point which provides translation information
     * @return <code>this</code> for convenience
     */
    public Rectangle translate(Point p) {
        x += p.x;
        y += p.y;
        return this;
    }

    /**
     * Switches the x and y values, as well as the width and height of this
     * Rectangle. Useful for orientation changes.
     * 
     * @return <code>this</code> for convenience
     * @since 2.0
     */
    public Rectangle transpose() {
        int temp = x;
        x = y;
        y = temp;
        temp = width;
        width = height;
        height = temp;
        return this;
    }

    /**
     * Unions this Rectangle's width and height with the specified Dimension.
     * 
     * @param d
     *            Dimension being unioned
     * @return <code>this</code> for convenience
     * @since 2.0
     * @deprecated Union with a dimension generally does not make much sense,
     *             thus deprecating this. Use
     *             {@link Dimension#max(Dimension, Dimension)} and
     *             {@link #setSize(Dimension)} to implement the desired behavior
     *             instead.
     */
    public Rectangle union(Dimension d) {
        width = Math.max(width, d.width);
        height = Math.max(height, d.height);
        return this;
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
     * @since 3.8
     */
    public Rectangle union(double x, double y) {
        return union((int) x, (int) y);
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
     * @since 3.8
     */
    public Rectangle union(double x, double y, double w, double h) {
        return union((int) x, (int) y, (int) w, (int) h);
    }

    /**
     * Updates this Rectangle's bounds to the minimum size which can hold both
     * this Rectangle and the coordinate (x,y).
     * 
     * @return <code>this</code> for convenience
     * @param x1
     *            X coordinate
     * @param y1
     *            Y coordinate
     * @since 2.0
     */
    public Rectangle union(int x1, int y1) {
        if (x1 < x) {
            width += (x - x1);
            x = x1;
        } else {
            int right = x + width;
            if (x1 >= right) {
                right = x1 + 1;
                width = right - x;
            }
        }
        if (y1 < y) {
            height += (y - y1);
            y = y1;
        } else {
            int bottom = y + height;
            if (y1 >= bottom) {
                bottom = y1 + 1;
                height = bottom - y;
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
     * @since 2.0
     */
    public Rectangle union(int x, int y, int w, int h) {
        int right = Math.max(this.x + width, x + w);
        int bottom = Math.max(this.y + height, y + h);
        this.x = Math.min(this.x, x);
        this.y = Math.min(this.y, y);
        this.width = right - this.x;
        this.height = bottom - this.y;
        return this;
    }

    /**
     * Updates this Rectangle's bounds to the minimum size which can hold both
     * this Rectangle and the given Point.
     * 
     * @param p
     *            Point to be unioned with this Rectangle
     * @since 2.0
     */
    public void union(Point p) {
        // TODO: This should for the sake of consistency also return this
        // by convenience; however, this is regarded as API break so it
        // may be done not sooner than in 4.0; if this is done, it has
        // to be ensured that the overwritten method in PrecisionRectangle
        // is adjusted as well.
        union(p.x(), p.y());
    }

    /**
     * Updates this Rectangle's dimensions to the minimum size which can hold
     * both this Rectangle and the given Rectangle.
     * 
     * @return <code>this</code> for convenience
     * @param rect
     *            Rectangle to be unioned with this Rectangle
     * @since 2.0
     */
    public Rectangle union(Rectangle rect) {
        if (rect == null || rect.isEmpty())
            return this;
        return union(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Returns the current width of this Rectangle
     * 
     * @return The current width
     * @since 3.7
     */
    public int width() {
        return width;
    }

    /**
     * Returns the x value of this Rectangle.
     * 
     * @return The current x value
     * @since 3.7
     */
    public int x() {
        return x;
    }

    /**
     * Returns the y value of the Rectangle
     * 
     * @return The current y value
     * @since 3.7
     */
    public int y() {
        return y;
    }

}
