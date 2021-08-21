/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Nyßen (Research Group Software Construction, RWTH Aachen University) - Contribution for Bugzilla 245182
 *
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Represents a vector within 2-dimensional Euclidean space.
 * 
 * @since 3.6
 */
public class Vector {

    /** the X value */
    public double x;
    /** the Y value */
    public double y;

    // internal constant used for comparisons.
    private static final Vector NULL = new Vector(0, 0);

    /**
     * Constructs a Vector pointed in the specified direction.
     * 
     * @param x
     *            X value.
     * @param y
     *            Y value.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Vector pointed in the direction specified by a Point.
     * 
     * @param p
     *            the point
     */
    public Vector(PrecisionPoint p) {
        x = p.preciseX();
        y = p.preciseY();
    }

    /**
     * Constructs a Vector representing the direction and magnitude between to
     * provided Points.
     * 
     * @param start
     *            starting point
     * @param end
     *            End Point
     */
    public Vector(PrecisionPoint start, PrecisionPoint end) {
        x = end.preciseX() - start.preciseX();
        y = end.preciseY() - start.preciseY();
    }

    /**
     * Constructs a Vector representing the difference between two provided
     * Vectors.
     * 
     * @param start
     *            The start Ray
     * @param end
     *            The end Ray
     */
    public Vector(Vector start, Vector end) {
        x = end.x - start.x;
        y = end.y - start.y;
    }

    /**
     * Calculates the magnitude of the cross product of this Vector with
     * another. Represents the amount by which two Vectors are directionally
     * different. Parallel Vectors return a value of 0.
     * 
     * @param other
     *            Vector being compared
     * @return The dissimilarity
     */
    public double getDissimilarity(Vector other) {
        return PrecisionGeometry.preciseAbs(getCrossProduct(other));
    }

    /**
     * Calculates whether this Vector and the provided one are parallel to each
     * other.
     * 
     * @param other
     *            The Vector to test for parallelism
     * @return true if this Vector and the provided one are parallel, false
     *         otherwise.
     */
    public boolean isParallelTo(Vector other) {
        return getDissimilarity(other) == 0;
    }

    /**
     * Calculates the dot product of this Vector with another.
     * 
     * @param other
     *            the Vector used to calculate the dot product
     * @return The dot product
     */
    public double getDotProduct(Vector other) {
        return PrecisionGeometry.preciseAdd(
                PrecisionGeometry.preciseMultiply(x, other.x),
                PrecisionGeometry.preciseMultiply(y, other.y));
    }

    /**
     * Calculates the cross product of this Vector with another.
     * 
     * @param other
     *            the Vector used to calculate the cross product
     * @return The cross product.
     */
    public double getCrossProduct(Vector other) {
        return PrecisionGeometry.preciseSubtract(
                PrecisionGeometry.preciseMultiply(x, other.y),
                PrecisionGeometry.preciseMultiply(y, other.x));
    }

    /**
     * Creates a new Vector which is the sum of this Vector with another.
     * 
     * @param other
     *            Vector to be added to this Vector
     * @return a new Vector representing the sum
     */
    public Vector getAdded(Vector other) {
        return new Vector(PrecisionGeometry.preciseAdd(x, other.x),
                PrecisionGeometry.preciseAdd(y, other.y));
    }

    /**
     * Creates a new Vector which is the difference of this Vector with the
     * provided Vector.
     * 
     * @param other
     *            Vector to be subtracted from this Vector
     * @return a new Vector representing the difference.
     */
    public Vector getSubtracted(Vector other) {
        return new Vector(PrecisionGeometry.preciseSubtract(x, other.x),
                PrecisionGeometry.preciseSubtract(y, other.y));
    }

    /**
     * Returns the angle (in degrees) between this Vector and the provided
     * Vector.
     * 
     * @param other
     *            Vector to calculate the angle.
     * @return the angle between the two Vectors in degrees.
     */
    public double getAngle(Vector other) {
        double cosAlpha = PrecisionGeometry.preciseDivide(
                getDotProduct(other),
                (PrecisionGeometry.preciseMultiply(getLength(),
                        other.getLength())));
        // compensate rounding effects that may occur
        if (cosAlpha > 1) {
            cosAlpha = 1;
        } else if (cosAlpha < -1) {
            cosAlpha = -1;
        }
        return Math.toDegrees(Math.acos(cosAlpha));
    }

    /**
     * Creates a new Vector which represents the average of this Vector with
     * another.
     * 
     * @param other
     *            Vector to calculate the average.
     * @return a new Vector
     */
    public Vector getAveraged(Vector other) {
        return new Vector(PrecisionGeometry.preciseDivide(
                PrecisionGeometry.preciseAdd(x, other.x), 2),
                PrecisionGeometry.preciseDivide(
                        PrecisionGeometry.preciseAdd(y, other.y), 2));
    }

    /**
     * Creates a new Vector which represents this Vector multiplied by the
     * provided scalar factor.
     * 
     * @param factor
     *            Value providing the amount to scale.
     * @return a new Vector
     */
    public Vector getMultiplied(double factor) {
        return new Vector(PrecisionGeometry.preciseMultiply(x, factor),
                PrecisionGeometry.preciseMultiply(y, factor));
    }

    /**
     * Creates a new Vector which represents this Vector divided by the provided
     * scalar factor.
     * 
     * @param factor
     *            Value providing the amount to scale.
     * @return a new Vector
     */
    public Vector getDivided(double factor) {
        return new Vector(PrecisionGeometry.preciseDivide(x, factor),
                PrecisionGeometry.preciseDivide(y, factor));
    }

    /**
     * Returns the orthogonal complement of this Vector, which is defined to be
     * (-y, x).
     * 
     * @return the orthogonal complement of this Vector
     */
    public Vector getOrthogonalComplement() {
        return new Vector(PrecisionGeometry.preciseNegate(y), x);
    }

    /**
     * Returns the length of this Vector.
     * 
     * @return Length of this Vector
     */
    public double getLength() {
        return Math.sqrt(getDotProduct(this));
    }

    /**
     * Calculates the similarity of this Vector with another. Similarity is
     * defined as the absolute value of the dotProduct(). Orthogonal vectors
     * return a value of 0.
     * 
     * @param other
     *            Vector being tested for similarity
     * @return the Similarity
     * @see #getDissimilarity(Vector)
     */
    public double getSimilarity(Vector other) {
        return PrecisionGeometry.preciseAbs(getDotProduct(other));
    }

    /**
     * Calculates whether this Vector and the provided one are orthogonal to
     * each other.
     * 
     * @param other
     *            Vector being tested for orthogonality
     * @return true, if this Vector and the provide one are orthogonal, false
     *         otherwise
     */
    public boolean isOrthogonalTo(Vector other) {
        return getSimilarity(other) == 0;
    }

    /**
     * Checks whether this vector has a horizontal component.
     * 
     * @return true if x != 0, false otherwise.
     */
    public boolean isHorizontal() {
        return x != 0;
    }

    /**
     * Checks whether this vector has a vertical component.
     * 
     * @return true if y != 0, false otherwise.
     */
    public boolean isVertical() {
        return y != 0;
    }

    /**
     * Checks whether this vector equals (0,0);
     * 
     * @return true if x == 0 and y == 0.
     */
    public boolean isNull() {
        return equals(NULL);
    }

    /**
     * Returns a point representation of this Vector.
     * 
     * @return a PrecisionPoint representation
     */
    public PrecisionPoint toPoint() {
        return new PrecisionPoint(x, y);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Vector) {
            Vector r = (Vector) obj;
            return x == r.x && y == r.y;
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) x + (int) y;
    }

}
