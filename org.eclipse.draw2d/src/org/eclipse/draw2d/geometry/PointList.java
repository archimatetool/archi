/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Shatalin (Borland) - Contribution for Bug 238874
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Represents a List of Points. This class is used for building an
 * <code>int[]</code>. The array is internal, and is constructed and queried by
 * the client using {@link Point Points}. SWT uses integer arrays when painting
 * polylines and polygons.
 */
public class PointList implements java.io.Serializable, Translatable {

    private int[] points = new int[0];
    private Rectangle bounds;
    private int size = 0;

    static final long serialVersionUID = 1;

    /**
     * Constructs an empty PointList.
     * 
     * @since 2.0
     */
    public PointList() {
    }

    /**
     * Constructs a PointList with the given points.
     * 
     * @param points
     *            int array where two consecutive ints form the coordinates of a
     *            point
     * @since 3.1
     */
    public PointList(int points[]) {
        this.points = points;
        this.size = points.length / 2;
    }

    /**
     * Constructs a PointList with initial capacity <i>size</i>, but no points.
     * 
     * @param size
     *            Number of points to hold.
     * @since 2.0
     */
    public PointList(int size) {
        points = new int[size * 2];
    }

    /**
     * Appends all of the given points to this PointList.
     * 
     * @param source
     *            the source pointlist
     */
    public void addAll(PointList source) {
        ensureCapacity(size + source.size);
        System.arraycopy(source.points, 0, points, size * 2, source.size * 2);
        size += source.size;
    }

    /**
     * Adds Point <i>p</i> to this PointList.
     * 
     * @param p
     *            the point to be added
     * @see #removePoint(int)
     * @since 2.0
     */
    public void addPoint(Point p) {
        addPoint(p.x, p.y);
    }

    /**
     * Adds the input point values to this PointList.
     * 
     * @param x
     *            X value of a point to add
     * @param y
     *            Y value of a point to add
     * @since 2.0
     */
    public void addPoint(int x, int y) {
        bounds = null;
        int index = size * 2;
        ensureCapacity(size + 1);
        points[index] = x;
        points[index + 1] = y;
        size++;
    }

    private void ensureCapacity(int newSize) {
        newSize *= 2;
        if (points.length < newSize) {
            int old[] = points;
            points = new int[Math.max(newSize, size * 4)];
            System.arraycopy(old, 0, points, 0, size * 2);
        }
    }

    /**
     * Returns the smallest Rectangle which contains all Points.
     * 
     * @return The smallest Rectangle which contains all Points.
     * @since 2.0
     */
    public Rectangle getBounds() {
        if (bounds != null)
            return bounds;
        bounds = new Rectangle();
        if (size > 0) {
            bounds.setLocation(getPoint(0));
            for (int i = 0; i < size; i++)
                bounds.union(getPoint(i));
        }
        return bounds;
    }

    /**
     * Creates a copy
     * 
     * @return PointList A copy of this PointList
     */
    public PointList getCopy() {
        PointList result = new PointList(size);
        System.arraycopy(points, 0, result.points, 0, size * 2);
        result.size = size;
        result.bounds = null;
        return result;
    }

    /**
     * Returns the first Point in the list.
     * 
     * @return The first point in the list.
     * @throws IndexOutOfBoundsException
     *             if the list is empty
     * @since 2.0
     */
    public Point getFirstPoint() {
        return getPoint(0);
    }

    /**
     * Returns the last point in the list.
     * 
     * @throws IndexOutOfBoundsException
     *             if the list is empty
     * @return The last Point in the list
     * @since 2.0
     */
    public Point getLastPoint() {
        return getPoint(size - 1);
    }

    /**
     * Returns the midpoint of the list of Points. The midpoint is the median of
     * the List, unless there are 2 medians (size is even), then the middle of
     * the medians is returned.
     * 
     * @return The midpoint
     * @throws IndexOutOfBoundsException
     *             if the list is empty
     */
    public Point getMidpoint() {
        if (size() % 2 == 0)
            return getPoint(size() / 2 - 1).getTranslated(getPoint(size() / 2))
                    .scale(0.5f);
        return getPoint(size() / 2);
    }

    /**
     * Returns the Point in the list at the specified index.
     * 
     * @param index
     *            Index of the desired Point
     * @return The requested Point
     * @throws IndexOutOfBoundsException
     *             If the specified index is out of range
     * @since 2.0
     */
    public Point getPoint(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        index *= 2;
        return new Point(points[index], points[index + 1]);
    }

    /**
     * Copies the x and y values at given index into a specified Point. This
     * method exists to avoid the creation of a new <code>Point</code>.
     * 
     * @see #getPoint(int)
     * @param p
     *            The Point which will be set with the &lt;x, y&gt; values
     * @param index
     *            The index being requested
     * @return The parameter <code>p</code> is returned for convenience
     * @since 2.0
     */
    public Point getPoint(Point p, int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        index *= 2;
        p.x = points[index];
        p.y = points[index + 1];
        return p;
    }

    /**
     * Inserts a given point at a specified index.
     * 
     * @param p
     *            Point to be inserted.
     * @param index
     *            Position where the point is to be inserted.
     * @exception IndexOutOfBoundsException
     *                if the index is invalid
     * @see #setPoint(Point, int)
     * @since 2.0
     */
    public void insertPoint(Point p, int index) {
        if (bounds != null && !bounds.contains(p))
            bounds = null;
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        index *= 2;

        int length = points.length;
        int old[] = points;
        points = new int[length + 2];
        System.arraycopy(old, 0, points, 0, index);
        System.arraycopy(old, index, points, index + 2, length - index);

        points[index] = p.x;
        points[index + 1] = p.y;
        size++;
    }

    /**
     * Determines whether any of the line segments represented by this PointList
     * intersect the given Rectangle. If a segment touches the given rectangle,
     * that's considered intersection.
     * 
     * @param r
     *            the rectangle
     * @return <code>true</code> if the given rectangle intersects any of the
     *         line segments represented by this PointList
     * @since 3.1
     */
    public boolean intersects(Rectangle r) {
        if (r.isEmpty())
            return false;
        for (int i = 0; i < size * 2; i += 2) {
            if (r.contains(points[i], points[i + 1]))
                return true;
        }
        int diagonal1x1 = r.x, diagonal1y1 = r.y, diagonal1x2 = r.x + r.width
                - 1, diagonal1y2 = r.y + r.height - 1, diagonal2x1 = r.x
                + r.width - 1, diagonal2y1 = r.y, diagonal2x2 = r.x, diagonal2y2 = r.y
                + r.height - 1;
        for (int i = 0; i < (size - 1) * 2; i += 2) {
            if (Geometry.linesIntersect(diagonal1x1, diagonal1y1, diagonal1x2,
                    diagonal1y2, points[i], points[i + 1], points[i + 2],
                    points[i + 3])
                    || Geometry.linesIntersect(diagonal2x1, diagonal2y1,
                            diagonal2x2, diagonal2y2, points[i], points[i + 1],
                            points[i + 2], points[i + 3]))
                return true;
        }
        return false;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Translatable#performScale(double)
     */
    @Override
    public void performScale(double factor) {
        for (int i = 0; i < points.length; i++)
            points[i] = (int) Math.floor(points[i] * factor);
        bounds = null;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Translatable#performTranslate(int, int)
     */
    @Override
    public void performTranslate(int dx, int dy) {
        for (int i = 0; i < size * 2; i += 2) {
            points[i] += dx;
            points[i + 1] += dy;
        }
        if (bounds != null)
            bounds.translate(dx, dy);
    }

    /**
     * Removes all the points stored by this list. Resets all the properties
     * based on the point information.
     * 
     * @since 2.0
     */
    public void removeAllPoints() {
        bounds = null;
        size = 0;
    }

    /**
     * Removes the point at the specified index from the PointList, and returns
     * it.
     * 
     * @since 2.0
     * @see #addPoint(Point)
     * @param index
     *            Index of the point to be removed.
     * @return The point which has been removed
     * @throws IndexOutOfBoundsException
     *             if the removal index is beyond the list capacity
     */
    public Point removePoint(int index) {
        bounds = null;
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$

        index *= 2;
        Point pt = new Point(points[index], points[index + 1]);
        if (index != size * 2 - 2)
            System.arraycopy(points, index + 2, points, index, size * 2 - index
                    - 2);
        size--;
        return pt;
    }

    /**
     * Reverses the order of the points in the list.
     * 
     * @since 3.2
     */
    public void reverse() {
        int temp;
        for (int i = 0, j = size * 2 - 2; i < size; i += 2, j -= 2) {
            temp = points[i];
            points[i] = points[j];
            points[j] = temp;
            temp = points[i + 1];
            points[i + 1] = points[j + 1];
            points[j + 1] = temp;
        }
    }

    /**
     * Overwrites a point at a given index in the list with the specified Point.
     * 
     * @param pt
     *            Point which is to be stored at the index.
     * @param index
     *            Index where the given point is to be stored.
     * @since 2.0
     */
    public void setPoint(Point pt, int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        if (bounds != null && !bounds.contains(pt))
            bounds = null;
        points[index * 2] = pt.x;
        points[index * 2 + 1] = pt.y;
    }

    /**
     * Sets the size of this PointList.
     * 
     * @param newSize
     *            the new size
     */
    public void setSize(int newSize) {
        if (points.length > newSize * 2) {
            size = newSize;
            return;
        }
        int[] newArray = new int[newSize * 2];
        System.arraycopy(points, 0, newArray, 0, points.length);
        points = newArray;
        size = newSize;
    }

    /**
     * Returns the number of points in this PointList.
     * 
     * @return The number of points
     * @since 2.0
     */
    public int size() {
        return size;
    }

    /**
     * Returns the contents of this PointList as an integer array. The returned
     * array is by reference. Any changes made to the array will also be
     * changing the original PointList.
     * 
     * @return the integer array of points by reference
     * @since 2.0
     */
    public int[] toIntArray() {
        if (points.length != size * 2) {
            int[] old = points;
            points = new int[size * 2];
            System.arraycopy(old, 0, points, 0, size * 2);
        }
        return points;
    }

    /**
     * Moves the origin (0,0) of the coordinate system of all the points to the
     * Point <i>pt</i>. This updates the position of all the points in this
     * PointList.
     * 
     * @param pt
     *            Position by which all the points will be shifted.
     * @see #translate(int,int)
     * @since 2.0
     */
    public final void translate(Point pt) {
        translate(pt.x, pt.y);
    }

    /**
     * Moves the origin (0,0) of the coordinate system of all the points to the
     * Point (x,y). This updates the position of all the points in this
     * PointList.
     * 
     * @param x
     *            Amount by which all the points will be shifted on the X axis.
     * @param y
     *            Amount by which all the points will be shifted on the Y axis.
     * @see #translate(Point)
     * @since 2.0
     */
    public void translate(int x, int y) {
        if (x == 0 && y == 0)
            return;
        if (bounds != null)
            bounds.translate(x, y);
        for (int i = 0; i < size * 2; i += 2) {
            points[i] += x;
            points[i + 1] += y;
        }
    }

    /**
     * Transposes all x and y values. Useful for orientation changes.
     * 
     * @since 3.2
     */
    public void transpose() {
        int temp;
        if (bounds != null)
            bounds.transpose();
        for (int i = 0; i < size * 2; i += 2) {
            temp = points[i];
            points[i] = points[i + 1];
            points[i + 1] = temp;
        }
    }

    /**
     * @param x
     *            - X coordinate of the point
     * @param y
     *            - Y coordinate of the point
     * 
     * @return true if specified point belongs to the polygon drawn using this
     *         PointList
     * @see Geometry#polygonContainsPoint(PointList, int, int)
     * @since 3.5
     */
    public boolean polygonContainsPoint(int x, int y) {
        return Geometry.polygonContainsPoint(this, x, y);
    }

    /**
     * @param x
     *            - X coordinate of the point
     * @param y
     *            - Y coordinate of the point
     * @param tolerance
     *            - allowed distance between point and polyline segment
     * 
     * @return true if the least distance between specified point and polyline
     *         drawn using this PointList is less then specified tolerance
     * @see Geometry#polylineContainsPoint(PointList, int, int, int)
     * @since 3.5
     */
    public boolean polylineContainsPoint(int x, int y, int tolerance) {
        return Geometry.polylineContainsPoint(this, x, y, tolerance);
    }

}
