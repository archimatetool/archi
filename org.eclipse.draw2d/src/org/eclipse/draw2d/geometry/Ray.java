/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Represents a 2-dimensional directional Vector, or Ray.
 * {@link java.util.Vector} is commonly imported, so the name Ray was chosen.
 * 
 * @deprecated Use {@link Vector} instead, which offers double precision instead
 *             of integer precision.
 */
public final class Ray {

    /** the X value */
    public int x;
    /** the Y value */
    public int y;

    /**
     * Constructs a Ray &lt;0, 0&gt; with no direction and magnitude.
     * 
     * @since 2.0
     */
    public Ray() {
    }

    /**
     * Constructs a Ray pointed in the specified direction.
     * 
     * @param x
     *            X value.
     * @param y
     *            Y value.
     * @since 2.0
     */
    public Ray(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Ray pointed in the direction specified by a Point.
     * 
     * @param p
     *            the Point
     * @since 2.0
     */
    public Ray(Point p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Constructs a Ray representing the direction and magnitude between to
     * provided Points.
     * 
     * @param start
     *            Strarting Point
     * @param end
     *            End Point
     * @since 2.0
     */
    public Ray(Point start, Point end) {
        x = end.x - start.x;
        y = end.y - start.y;
    }

    /**
     * Constructs a Ray representing the difference between two provided Rays.
     * 
     * @param start
     *            The start Ray
     * @param end
     *            The end Ray
     * @since 2.0
     */
    public Ray(Ray start, Ray end) {
        x = end.x - start.x;
        y = end.y - start.y;
    }

    /**
     * Calculates the magnitude of the cross product of this Ray with another.
     * Represents the amount by which two Rays are directionally different.
     * Parallel Rays return a value of 0.
     * 
     * @param r
     *            Ray being compared
     * @return The assimilarity
     * @see #similarity(Ray)
     * @since 2.0
     */
    public int assimilarity(Ray r) {
        return Math.abs(x * r.y - y * r.x);
    }

    /**
     * Calculates the dot product of this Ray with another.
     * 
     * @param r
     *            the Ray used to perform the dot product
     * @return The dot product
     * @since 2.0
     */
    public int dotProduct(Ray r) {
        return x * r.x + y * r.y;
    }

    /**
     * Calculates the dot product of this Ray with another.
     * 
     * @param r
     *            the Ray used to perform the dot product
     * @return The dot product as <code>long</code> to avoid possible integer
     *         overflow
     * @since 3.4.1
     */
    long dotProductL(Ray r) {
        return (long) x * r.x + (long) y * r.y;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Ray) {
            Ray r = (Ray) obj;
            return x == r.x && y == r.y;
        }
        return false;
    }

    /**
     * Creates a new Ray which is the sum of this Ray with another.
     * 
     * @param r
     *            Ray to be added with this Ray
     * @return a new Ray
     * @since 2.0
     */
    public Ray getAdded(Ray r) {
        return new Ray(r.x + x, r.y + y);
    }

    /**
     * Creates a new Ray which represents the average of this Ray with another.
     * 
     * @param r
     *            Ray to calculate the average.
     * @return a new Ray
     * @since 2.0
     */
    public Ray getAveraged(Ray r) {
        return new Ray((x + r.x) / 2, (y + r.y) / 2);
    }

    /**
     * Creates a new Ray which represents this Ray scaled by the amount
     * provided.
     * 
     * @param s
     *            Value providing the amount to scale.
     * @return a new Ray
     * @since 2.0
     */
    public Ray getScaled(int s) {
        return new Ray(x * s, y * s);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (x * y) ^ (x + y);
    }

    /**
     * Returns true if this Ray has a non-zero horizontal comonent.
     * 
     * @return true if this Ray has a non-zero horizontal comonent
     * @since 2.0
     */
    public boolean isHorizontal() {
        return x != 0;
    }

    /**
     * Returns the length of this Ray.
     * 
     * @return Length of this Ray
     * @since 2.0
     */
    public double length() {
        return Math.sqrt(dotProductL(this));
    }

    /**
     * Calculates the similarity of this Ray with another. Similarity is defined
     * as the absolute value of the dotProduct()
     * 
     * @param r
     *            Ray being tested for similarity
     * @return the Similarity
     * @see #assimilarity(Ray)
     * @since 2.0
     */
    public int similarity(Ray r) {
        return Math.abs(dotProduct(r));
    }

    /**
     * @return a String representation
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
    }

}
