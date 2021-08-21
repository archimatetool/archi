/*******************************************************************************
 * Copyright (c) 2010 Research Group Software Construction, RWTH Aachen University and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alexander Nyßen (Research Group Software Contruction, RWTH Aachen University) - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Represents a straight line within 2-dimensional Euclidean space.
 * 
 * @author Alexander Nyssen
 * @since 3.6
 */
public class Straight {

    /** position vector of this straight */
    public Vector position;

    /** direction vector of this straight */
    public Vector direction;

    /**
     * Constructs a new Straight with the given position and direction.
     * 
     * @param position
     * @param direction
     */
    public Straight(Vector position, Vector direction) {
        if (direction.isNull()) {
            throw new IllegalArgumentException(
                    "direction has to be unequal to (0,0)"); //$NON-NLS-1$
        }
        this.position = position;
        this.direction = direction;
    }

    /**
     * Constructs a new Straight between the two given Points.
     * 
     * @param point1
     *            a first waypoint of the Straight to be constructed
     * @param point2
     *            a second waypoint of the Straight to be constructed
     */
    public Straight(PrecisionPoint point1, PrecisionPoint point2) {
        this(new Vector(point1), new Vector(point1, point2));
    }

    /**
     * Checks whether this Straight and the provided one have an intersection
     * point.
     * 
     * @param other
     *            The Straight to use for the calculation.
     * @return true if the two Straights intersect, false otherwise.
     */
    public boolean intersects(Straight other) {
        return direction.getDotProduct(other.direction
                .getOrthogonalComplement()) != 0;
    }

    /**
     * Checks whether this Straight and the provided one have an intersection
     * point, which is inside the specified segment between segmentStart and
     * segmentEnd.
     * 
     * segmentStart a Vector indicating the start point of the segment. Has to
     * be a point on the straight.
     * 
     * @param segmentEnd
     *            a Vector indicating the end point of the segment. Has to be a
     *            point on the straight.
     * @param other
     *            the Straight to test
     * @return true if the true straights intersect and the intersection point
     *         is contained within the specified segment, false otherwise.
     * @since 3.2
     */
    public boolean intersectsWithinSegment(Vector segmentStart,
            Vector segmentEnd, Straight other) {
        // precondition: segment start and end have to be points on this
        // straight.
        if (!contains(segmentStart) || !contains(segmentEnd)) {
            throw new IllegalArgumentException(
                    "segment points have to be contained"); //$NON-NLS-1$
        }

        // check if segmentStart->segmentEnd is a legal segment or a single
        // point
        Vector segmentDirection = segmentEnd.getSubtracted(segmentStart);
        if (segmentDirection.isNull()) {
            return other.contains(segmentStart);
        }

        // legal segment, check if there is an intersection within the segment
        if (intersects(other)) {
            Vector intersection = getIntersection(other);
            return containsWithinSegment(segmentStart, segmentEnd, intersection);
        }
        return false;
    }

    /**
     * Computes the intersection point of this Straight and the provided one, if
     * it exists.
     * 
     * @param other
     *            The Straight to use for calculations.
     * @return A Vector pointing to the intersection point, if it exists, null
     *         if no intersection point exists (or the Straights are equal).
     */
    public Vector getIntersection(Straight other) {
        // first check if there is a single intersection point
        if (!intersects(other)) {
            return null;
        }
        // calculate intersection point
        Vector s1 = direction.getMultiplied(other.position
                .getDotProduct(other.direction.getOrthogonalComplement()));
        Vector s2 = other.direction.getMultiplied(position
                .getDotProduct(direction.getOrthogonalComplement()));
        return s1.getSubtracted(s2).getDivided(
                direction.getDotProduct(other.direction
                        .getOrthogonalComplement()));
    }

    /**
     * Returns the (smallest) angle between this Straight and the provided one.
     * 
     * @param other
     *            The Straight to be used for the calculation.
     * @return The angle spanned between the two Straights.
     */
    public double getAngle(Straight other) {
        return direction.getAngle(other.direction);
    }

    /**
     * Returns the projection of the given Vector onto this Straight, which is
     * the point on this Straight with the minimal distance to the point,
     * denoted by the provided Vector.
     * 
     * @param vector
     *            The Vector whose projection should be determined.
     * @return A new Vector representing the projection of the provided Vector
     *         onto this Straight.
     */
    public Vector getProjection(Vector vector) {
        return getIntersection(new Straight(vector,
                direction.getOrthogonalComplement()));
    }

    /**
     * Returns the distance of the provided Vector to this Straight, which is
     * the distance between the provided Vector and its projection onto this
     * Straight.
     * 
     * @param vector
     *            The Vector whose distance is to be calculated.
     * @return the distance between this Straight and the provided Vector.
     */
    public double getDistance(Vector vector) {
        return getProjection(vector).getSubtracted(vector).getLength();
    }

    /**
     * Calculates whether the point indicated by the provided Vector is a point
     * on this Straight.
     * 
     * @param vector
     *            the Vector who has to be checked.
     * @return true if the point indicated by the given Vector is a point of
     *         this Straight, false otherwise.
     */
    public boolean contains(Vector vector) {
        return getDistance(vector) == 0;
    }

    /**
     * Calculates whether the point indicated by the provided Vector is a point
     * on the straight segment between the given start and end points.
     * 
     * @param segmentStart
     *            a Vector indicating the start point of the segment. Has to be
     *            a point on the straight.
     * @param segmentEnd
     *            a Vector indicating the end point of the segment. Has to be a
     *            point on the straight.
     * @param vector
     *            the Vector who has to be checked.
     * @return true if point indicated by the given Vector is a point on this
     *         straight, within the specified segment, false otherwise.
     */
    public boolean containsWithinSegment(Vector segmentStart,
            Vector segmentEnd, Vector vector) {
        // precondition: segment start and end have to be points on this
        // straight.
        if (!contains(segmentStart) || !contains(segmentEnd)) {
            throw new IllegalArgumentException(
                    "segment points have to be contained"); //$NON-NLS-1$
        }

        // check if segmentStart->segmentEnd is a legal segment or a single
        // point
        Vector segmentDirection = segmentEnd.getSubtracted(segmentStart);
        if (segmentDirection.isNull()) {
            return segmentStart.equals(vector);
        }

        // legal segment
        if (new Straight(segmentStart, segmentDirection).contains(vector)) {
            // compute parameter s, so that vector = segmentStart + s *
            // (segmentEnd - segmentStart).
            double s = segmentDirection.isVertical() ? (vector.y - segmentStart.y)
                    / segmentDirection.y
                    : (vector.x - segmentStart.x) / segmentDirection.x;
            // if s is between 0 and 1, intersection point lies within
            // segment
            if (0 <= s && s <= 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether this Straight and the provided one are parallel to each
     * other.
     * 
     * @param other
     *            The Straight to test for parallelism.
     * @return true if the direction vectors of this Straight and the provided
     *         one are parallel, false otherwise.
     */
    public boolean isParallelTo(Straight other) {
        return direction.isParallelTo(other.direction);
    }

    /**
     * Checks whether this Straight is equal to the provided Straight. Two
     * Straights s1 and s2 are equal, if the position vector of s2 is a point on
     * s1 and the direction vectors of s1 and s2 are parallel.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Straight)) {
            return false;
        } else {
            Straight otherStraight = (Straight) other;
            return contains(otherStraight.position)
                    && isParallelTo(otherStraight);
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return position.hashCode() + direction.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return position.toString() + " + s * " + direction.toString(); //$NON-NLS-1$
    }

}