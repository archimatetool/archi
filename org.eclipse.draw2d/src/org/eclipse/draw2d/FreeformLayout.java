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

import java.util.ListIterator;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A layout for {@link org.eclipse.draw2d.FreeformFigure FreeformFigures}.
 * Supports option to set only positive (x,y) coordinates for children figures.
 */
@SuppressWarnings("rawtypes")
public class FreeformLayout extends XYLayout {

    /**
     * Option that forces only positive coordinates to children figures by
     * setting the layout origin appropriately
     */
    private static final int FLAG__POSITIVE_COORDINATES = 1;

    /**
     * Option flags
     */
    private int flags;

    /**
     * Layout origin point
     */
    private Point origin = null;

    /**
     * Returns the point (0,0) as the origin.
     * 
     * @see XYLayout#getOrigin(IFigure)
     */
    @Override
    public Point getOrigin(IFigure figure) {
        if (origin == null) {
            origin = new Point();
            if (isPositiveCoordinates()) {
                ListIterator children = figure.getChildren().listIterator();
                while (children.hasNext()) {
                    IFigure f = (IFigure) children.next();
                    Rectangle constraint = (Rectangle) getConstraint(f);
                    if (constraint != null) {
                        origin.x = Math.min(origin.x, constraint.x);
                        origin.y = Math.min(origin.y, constraint.y);
                    }
                }
                origin.negate();
            }
        }
        return origin;
    }

    /**
     * Checks whether the positive coordinates flag is on, e.g positive
     * coordinates for children are inforced by the layout
     * 
     * @return <code>boolean</code>
     * @since 3.6
     */
    public boolean isPositiveCoordinates() {
        return (flags & FLAG__POSITIVE_COORDINATES) != 0;
    }

    /**
     * Sets/unsets the positive coordinates flag for true/false parameters
     * respectively. If option is set to on then layout calculates positive
     * coordinates for children figures by adjusting the layout origin
     * accordingly.
     * 
     * @param positiveCoordinates
     * @since 3.6
     */
    public void setPositiveCoordinates(boolean positiveCoordinates) {
        if (positiveCoordinates != isPositiveCoordinates()) {
            if (positiveCoordinates) {
                flags |= FLAG__POSITIVE_COORDINATES;
            } else {
                flags &= ~FLAG__POSITIVE_COORDINATES;
            }
            invalidate();
        }
    }

    /**
     * @see org.eclipse.draw2d.AbstractLayout#invalidate()
     */
    @Override
    public void invalidate() {
        origin = null;
        super.invalidate();
    }

}
