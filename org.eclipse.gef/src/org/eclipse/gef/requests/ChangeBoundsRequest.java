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
package org.eclipse.gef.requests;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Request to change the bounds of the EditPart(s).
 */
public class ChangeBoundsRequest extends GroupRequest implements DropRequest {

    private Point moveDelta = new Point();
    private Dimension resizeDelta = new Dimension();
    private int resizeDirection;
    private Point mouseLocation;
    private int flags = 0;
    private static final int CONSTRAINED_RESIZE = 1;
    private static final int CENTERED_RESIZE = 2;
    private static final int CONSTRAINED_MOVE = 4;
    private static final int SNAP_TO = 16;

    /**
     * Default constructor.
     */
    public ChangeBoundsRequest() {
    }

    /**
     * Creates a ChangeBoundsRequest with the given type.
     * 
     * @param type
     *            The type of Request.
     */
    public ChangeBoundsRequest(Object type) {
        setType(type);
    }

    /**
     * Returns the location of the mouse pointer.
     * 
     * @return The location of the mouse pointer.
     */
    @Override
    public Point getLocation() {
        return mouseLocation;
    }

    /**
     * @deprecated Use {@link #getLocation() }
     * @return The location of the mouse pointer.
     */
    public Point getMouseLocation() {
        return getLocation();
    }

    /**
     * Returns a Point representing the distance the EditPart has moved.
     * 
     * @return A Point representing the distance the EditPart has moved.
     */
    public Point getMoveDelta() {
        return moveDelta;
    }

    /**
     * Returns the direction the figure is being resized. Possible values are
     * <ul>
     * <li>{@link org.eclipse.draw2d.PositionConstants#EAST}
     * <li>{@link org.eclipse.draw2d.PositionConstants#WEST}
     * <li>{@link org.eclipse.draw2d.PositionConstants#NORTH}
     * <li>{@link org.eclipse.draw2d.PositionConstants#SOUTH}
     * <li>{@link org.eclipse.draw2d.PositionConstants#NORTH_EAST}
     * <li>{@link org.eclipse.draw2d.PositionConstants#NORTH_WEST}
     * <li>{@link org.eclipse.draw2d.PositionConstants#SOUTH_EAST}
     * <li>{@link org.eclipse.draw2d.PositionConstants#SOUTH_WEST}
     * </ul>
     * 
     * @return the resize direction
     */
    public int getResizeDirection() {
        return resizeDirection;
    }

    /**
     * Returns a Dimension representing how much the EditPart has been resized.
     * 
     * @return A Dimension representing how much the EditPart has been resized.
     */
    public Dimension getSizeDelta() {
        return resizeDelta;
    }

    /**
     * Transforms a copy of the passed in rectangle to account for the move
     * and/or resize deltas and returns this copy.
     * 
     * @param rect
     *            the rectangle to transform
     * @return a copy of the passed in rectangle representing the new bounds
     */
    public Rectangle getTransformedRectangle(Rectangle rect) {
        return rect.getCopy().translate(moveDelta).resize(resizeDelta);
    }

    /**
     * Returns true if the request is for a centered resize.
     * 
     * @since 3.0
     * @return <code>true</code> if centered resize
     */
    public boolean isCenteredResize() {
        return (flags & CENTERED_RESIZE) != 0;
    }

    /**
     * Returns <code>true</code> if the request is for a constrained move
     * 
     * @since 3.0
     * @return <code>true</code> if a constrained move
     */
    public boolean isConstrainedMove() {
        return (flags & CONSTRAINED_MOVE) != 0;
    }

    /**
     * Returns <code>true</code> if the request is for a constrained resize
     * 
     * @since 3.0
     * @return <code>true</code> if a constrained resize
     */
    public boolean isConstrainedResize() {
        return (flags & CONSTRAINED_RESIZE) == CONSTRAINED_RESIZE;
    }

    /**
     * Returns <code>true</code> if snap-to is enabled
     * 
     * @since 3.7
     * @return <code>true</code> if the request is for a creation with snap-to
     *         enabled
     */
    public boolean isSnapToEnabled() {
        return (flags & SNAP_TO) != 0;
    }

    /**
     * Used to set whether a centered resize is being performed.
     * 
     * @since 3.0
     * @param value
     *            <code>true</code> if the request is for a centered resize
     */
    public void setCenteredResize(boolean value) {
        flags = value ? (flags | CENTERED_RESIZE) : (flags & ~CENTERED_RESIZE);
    }

    /**
     * Used to set whether a constrained move is being performed.
     * 
     * @since 3.0
     * @param value
     *            <code>true</code> if the request is for a constrained move
     */
    public void setConstrainedMove(boolean value) {
        flags = value ? (flags | CONSTRAINED_MOVE)
                : (flags & ~CONSTRAINED_MOVE);
    }

    /**
     * Used to set whether a constrained resize is being performed.
     * 
     * @since 3.0
     * @param value
     *            <code>true</code> if the request is for a constrained resize
     */
    public void setConstrainedResize(boolean value) {
        flags = value ? (flags | CONSTRAINED_RESIZE)
                : (flags & ~CONSTRAINED_RESIZE);
    }

    /**
     * Sets the location of the mouse pointer.
     * 
     * @param p
     *            The location of the mouse pointer.
     */
    public void setLocation(Point p) {
        mouseLocation = p;
    }

    /**
     * @deprecated Use {@link #setLocation(Point)}
     * @param p
     *            The location of the mouse pointer.
     */
    public void setMouseLocation(Point p) {
        setLocation(p);
    }

    /**
     * Sets the move delta.
     * 
     * @param p
     *            The Point representing the move delta
     */
    public void setMoveDelta(Point p) {
        moveDelta = p;
    }

    /**
     * Sets the direction the figure is being resized.
     * 
     * @param dir
     *            the direction of the resize
     * @see #getResizeDirection()
     */
    public void setResizeDirection(int dir) {
        resizeDirection = dir;
    }

    /**
     * Sets the size delta.
     * 
     * @param d
     *            The Dimension representing the size delta.
     */
    public void setSizeDelta(Dimension d) {
        resizeDelta = d;
    }

    /**
     * Used to set whether snap-to is being performed.
     * 
     * @since 3.7
     * @param value
     *            <code>true</code> if the request is for a creation with
     *            snap-to enabled
     */
    public void setSnapToEnabled(boolean value) {
        flags = value ? (flags | SNAP_TO) : (flags & ~SNAP_TO);
    }

}
