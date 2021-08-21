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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Places a figure relative to a point determined by the subclass. The figure
 * may be placed in some location relative to the point with a configurable
 * amount of spacing.
 */
public abstract class AbstractLocator implements Locator {

    private int relativePosition = PositionConstants.CENTER;
    private int gap;

    /**
     * Creates a new AbstractLocator.
     */
    public AbstractLocator() {
    }

    /**
     * Returns the number of pixels to leave between the figure being located
     * and the reference point. Only used if {@link #getRelativePosition()}
     * returns something other than {@link PositionConstants#CENTER}.
     * 
     * @return The gap
     * @since 2.0
     */
    public int getGap() {
        return gap;
    }

    /**
     * Returns the reference point in absolute coordinates used to calculate the
     * location.
     * 
     * @return The reference point in absolute coordinates
     * @since 2.0
     */
    protected abstract Point getReferencePoint();

    /**
     * Recalculate the location of the figure according to its desired position
     * relative to the center point.
     * 
     * @param size
     *            The size of the figure
     * @param center
     *            The center point
     * @return The new bounds
     * @since 2.0
     */
    protected Rectangle getNewBounds(Dimension size, Point center) {
        Rectangle bounds = new Rectangle(center, size);

        bounds.x -= bounds.width / 2;
        bounds.y -= bounds.height / 2;

        int xFactor = 0, yFactor = 0;
        int position = getRelativePosition();

        if ((position & PositionConstants.NORTH) != 0)
            yFactor = -1;
        else if ((position & PositionConstants.SOUTH) != 0)
            yFactor = 1;

        if ((position & PositionConstants.WEST) != 0)
            xFactor = -1;
        else if ((position & PositionConstants.EAST) != 0)
            xFactor = 1;

        bounds.x += xFactor * (bounds.width / 2 + getGap());
        bounds.y += yFactor * (bounds.height / 2 + getGap());

        return bounds;
    }

    /**
     * Returns the position of the figure with respect to the center point.
     * Possible values can be found in {@link PositionConstants} and include
     * CENTER, NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, or
     * SOUTH_WEST.
     * 
     * @return An int constant representing the relative position
     * @since 2.0
     */
    public int getRelativePosition() {
        return relativePosition;
    }

    /**
     * Recalculates the position of the figure and returns the updated bounds.
     * 
     * @param target
     *            The figure to relocate
     */
    @Override
    public void relocate(IFigure target) {
        Dimension prefSize = target.getPreferredSize();
        Point center = getReferencePoint();
        target.translateToRelative(center);
        target.setBounds(getNewBounds(prefSize, center));
    }

    /**
     * Sets the gap between the reference point and the figure being placed.
     * Only used if getRelativePosition() returns something other than
     * {@link PositionConstants#CENTER}.
     * 
     * @param i
     *            The gap
     * @since 2.0
     */
    public void setGap(int i) {
        gap = i;
    }

    /**
     * Sets the position of the figure with respect to the center point.
     * Possible values can be found in {@link PositionConstants} and include
     * CENTER, NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, or
     * SOUTH_WEST.
     * 
     * @param pos
     *            The relative position
     * @since 2.0
     */
    public void setRelativePosition(int pos) {
        relativePosition = pos;
    }

}
