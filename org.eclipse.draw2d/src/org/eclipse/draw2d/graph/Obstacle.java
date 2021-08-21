/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An obstacle representation for the ShortestPathRouting. This is a subclass of
 * Rectangle.
 * 
 * This class is for internal use only.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
class Obstacle extends Rectangle {

    boolean exclude;
    Vertex topLeft, topRight, bottomLeft, bottomRight, center;
    private ShortestPathRouter router;

    /**
     * Creates a new obstacle from the given rectangle bounds.
     * 
     * @param rect
     *            the bounds
     */
    Obstacle(Rectangle rect, ShortestPathRouter router) {
        init(rect);
        this.router = router;
    }

    /**
     * Returns <code>true</code> if the given point is contained but not on the
     * boundary of this obstacle.
     * 
     * @param p
     *            a point
     * @return <code>true</code> if properly contained
     */
    public boolean containsProper(Point p) {
        return p.x > this.x && p.x < this.x + this.width - 1 && p.y > this.y
                && p.y < this.y + this.height - 1;
    }

    public int getSpacing() {
        return router.getSpacing();
    }

    private void growVertex(Vertex vertex) {
        if (vertex.totalCount > 0)
            vertex.grow();
    }

    /**
     * Grows all vertices on this obstacle.
     */
    void growVertices() {
        growVertex(topLeft);
        growVertex(topRight);
        growVertex(bottomLeft);
        growVertex(bottomRight);
    }

    /**
     * Initializes this obstacle to the values of the given rectangle
     * 
     * @param rect
     *            bounds of this obstacle
     */
    void init(Rectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;

        exclude = false;

        topLeft = new Vertex(x, y, this);
        topLeft.positionOnObstacle = PositionConstants.NORTH_WEST;
        topRight = new Vertex(x + width - 1, y, this);
        topRight.positionOnObstacle = PositionConstants.NORTH_EAST;
        bottomLeft = new Vertex(x, y + height - 1, this);
        bottomLeft.positionOnObstacle = PositionConstants.SOUTH_WEST;
        bottomRight = new Vertex(x + width - 1, y + height - 1, this);
        bottomRight.positionOnObstacle = PositionConstants.SOUTH_EAST;
        center = new Vertex(getCenter(), this);
    }

    /**
     * Requests a full reset on all four vertices of this obstacle.
     */
    void reset() {
        topLeft.fullReset();
        bottomLeft.fullReset();
        bottomRight.fullReset();
        topRight.fullReset();
    }

    private void shrinkVertex(Vertex vertex) {
        if (vertex.totalCount > 0)
            vertex.shrink();
    }

    /**
     * Shrinks all four vertices of this obstacle.
     */
    void shrinkVertices() {
        shrinkVertex(topLeft);
        shrinkVertex(topRight);
        shrinkVertex(bottomLeft);
        shrinkVertex(bottomRight);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Rectangle#toString()
     */
    @Override
    public String toString() {
        return "Obstacle(" + x + ", " + y + ", " + //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
                width + ", " + height + ")";//$NON-NLS-2$//$NON-NLS-1$
    }

}
