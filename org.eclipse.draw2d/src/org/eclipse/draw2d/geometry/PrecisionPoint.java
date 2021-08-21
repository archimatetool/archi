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
 * @author danlee
 */
public class PrecisionPoint extends Point {

    private static final long serialVersionUID = 1L;

    /**
     * Double value for X
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseX(double)} and {@link #preciseX()}
     *             instead. This field will become private in future versions.
     */
    public double preciseX;

    /**
     * Double value for Y
     * 
     * @noreference
     * @deprecated Use {@link #setPreciseY(double)} and {@link #preciseY()}
     *             instead. This field will become private in future versions.
     */
    public double preciseY;

    /**
     * Constructor for PrecisionPoint.
     */
    public PrecisionPoint() {
        super();
    }

    /**
     * Constructor for PrecisionPoint.
     * 
     * @param x
     *            X value
     * @param y
     *            Y value
     */
    public PrecisionPoint(double x, double y) {
        setPreciseLocation(x, y);
    }

    /**
     * Constructor for PrecisionPoint.
     * 
     * @param x
     *            X value
     * @param y
     *            Y value
     */
    public PrecisionPoint(int x, int y) {
        this((double) x, (double) y);
    }

    /**
     * Constructor for PrecisionPoint.
     * 
     * @param p
     *            Point from which the initial values are taken
     */
    public PrecisionPoint(Point p) {
        this(p.preciseX(), p.preciseY());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof PrecisionPoint) {
            PrecisionPoint p = (PrecisionPoint) o;
            return p.preciseX() == preciseX() && p.preciseY() == preciseY();
        }
        return super.equals(o);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#getCopy()
     */
    @Override
    public Point getCopy() {
        return getPreciseCopy();
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#getDifference(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Dimension getDifference(Point p) {
        return new PrecisionDimension(this.preciseX() - p.preciseX(),
                this.preciseY() - p.preciseY());
    }

    /**
     * Returns a precise copy of this.
     * 
     * @return a precise copy
     * @since 3.7
     */
    public PrecisionPoint getPreciseCopy() {
        return new PrecisionPoint(preciseX(), preciseY());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#performScale(double)
     */
    @Override
    public void performScale(double factor) {
        setPreciseX(preciseX() * factor);
        setPreciseY(preciseY() * factor);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#performTranslate(int, int)
     */
    @Override
    public void performTranslate(int dx, int dy) {
        setPreciseX(preciseX() + dx);
        setPreciseY(preciseY() + dy);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#preciseX()
     */
    @Override
    public double preciseX() {
        updatePreciseXDouble();
        return preciseX;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#preciseY()
     */
    @Override
    public double preciseY() {
        updatePreciseYDouble();
        return preciseY;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#scale(double, double)
     */
    @Override
    public Point scale(double xFactor, double yFactor) {
        setPreciseX(preciseX() * xFactor);
        setPreciseY(preciseY() * yFactor);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#setLocation(int, int)
     */
    @Override
    public Point setLocation(int x, int y) {
        return setPreciseLocation(x, y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#setLocation(Point)
     */
    @Override
    public Point setLocation(Point pt) {
        return setPreciseLocation(pt.preciseX(), pt.preciseY());
    }

    /**
     * Sets the precise location of this PrecisionPoint to the given x and y
     * values.
     * 
     * @param x
     *            The new x value
     * @param y
     *            The new y value
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionPoint setPreciseLocation(double x, double y) {
        setPreciseX(x);
        setPreciseY(y);
        return this;
    }

    /**
     * Sets the precise location of this PrecisionPoint to the x and y values of
     * the given one.
     * 
     * @param p
     *            The PrecisionPoint specifying the new x and y values.
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionPoint setPreciseLocation(PrecisionPoint p) {
        return setPreciseLocation(p.preciseX(), p.preciseY());
    }

    /**
     * Sets the precise x value of this PrecisionPoint to the given value.
     * 
     * @param x
     *            The new x value
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionPoint setPreciseX(double x) {
        preciseX = x;
        updateXInt();
        return this;
    }

    /**
     * Sets the precise y value of this PrecisionPoint to the given value.
     * 
     * @param y
     *            The new y value
     * @return this for convenience
     * @since 3.7
     */
    public PrecisionPoint setPreciseY(double y) {
        preciseY = y;
        updateYInt();
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#setX(int)
     */
    @Override
    public Point setX(int x) {
        return setPreciseX(x);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#setY(int)
     */
    @Override
    public Point setY(int y) {
        return setPreciseY(y);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#translate(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public Point translate(Dimension d) {
        return translatePrecise(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#translate(int, int)
     */
    @Override
    public Point translate(int dx, int dy) {
        return translatePrecise(dx, dy);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#translate(double, double)
     */
    @Override
    public Point translate(double dx, double dy) {
        return translatePrecise(dx, dy);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#translate(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Point translate(Point p) {
        return translatePrecise(p.preciseX(), p.preciseY());
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
     * @since 3.7
     */
    private PrecisionPoint translatePrecise(double dx, double dy) {
        setPreciseX(preciseX() + dx);
        setPreciseY(preciseY() + dy);
        return this;
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#transpose()
     */
    @Override
    public Point transpose() {
        double temp = preciseX();
        setPreciseX(preciseY());
        setPreciseY(temp);
        return this;
    }

    /**
     * Updates the integer fields using the precise versions.
     * 
     * @deprecated This method should not be accessed by clients any more (it
     *             will be made private in future releases). The update of
     *             integer and precision fields is performed automatically if
     *             {@link #preciseX} and {@link #preciseY} field values are not
     *             manipulated directly, but only via respective methods offered
     *             by this class.
     */
    public final void updateInts() {
        updateXInt();
        updateYInt();
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

}
