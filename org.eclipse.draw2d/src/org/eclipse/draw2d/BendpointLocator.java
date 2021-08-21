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

import org.eclipse.draw2d.geometry.Point;

/**
 * Places a figure relative to a specified bend in a {@link Connection}. A
 * bendpoint is one of the points returned in the connection's
 * {@link Connection#getPoints()} method. It is not related to the bendpoint
 * class used as routing constraints.
 */
public class BendpointLocator extends ConnectionLocator {

    private int index;

    /**
     * Creates a BendpointLocator associated with passed Connection c and index
     * i.
     * 
     * @param c
     *            Connection associated with BendpointLocator
     * @param i
     *            Index of bendpoint, represents the position of the bendpoint
     *            on Connection c
     * @since 2.0
     */
    public BendpointLocator(Connection c, int i) {
        super(c);
        index = i;
    }

    /**
     * Returns the index of this BendpointLocator. This index is the position of
     * the reference point in this BendpointLocator's {@link Connection}.
     * 
     * @return The index
     * @since 2.0
     */
    protected int getIndex() {
        return index;
    }

    /**
     * Returns reference point associated with the BendpointLocator. This Point
     * is taken from the BendpointLocator's connection and is point number
     * 'index'.
     * 
     * @return The reference point
     * @since 2.0
     */
    @Override
    protected Point getReferencePoint() {
        Point p = getConnection().getPoints().getPoint(Point.SINGLETON,
                getIndex());
        getConnection().translateToAbsolute(p);
        return p;
    }

}
