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
 * Supports an anchor in the XY layout. This anchor exists independently without
 * an owner.
 */
public class XYAnchor extends ConnectionAnchorBase {

    private Point location;

    /**
     * Constructs an XYAnchor at the Point p.
     * 
     * @param p
     *            the point where this anchor will be located
     * @since 2.0
     */
    public XYAnchor(Point p) {
        location = new Point(p);
    }

    /**
     * Returns the location of this anchor relative to the reference point given
     * in as input. Since this is XY layout, the location of the point is
     * independent of the reference point.
     * 
     * @see ConnectionAnchor#getLocation(Point)
     */
    @Override
    public Point getLocation(Point reference) {
        return location;
    }

    /**
     * Returns <code>null</code> as these anchors inherently do not depend on
     * other figures for their location.
     * 
     * @see ConnectionAnchor#getOwner()
     * @since 2.0
     */
    @Override
    public IFigure getOwner() {
        return null;
    }

    /**
     * Returns the point which is used as the reference by this connection
     * anchor. In the case of the XYAnchor, this point is the same as its
     * location.
     * 
     * @see ConnectionAnchor#getReferencePoint()
     */
    @Override
    public Point getReferencePoint() {
        return location;
    }

    /**
     * Sets the location of this anchor and notifies all the listeners of the
     * update.
     * 
     * @param p
     *            the new location of this anchor
     * @see #getLocation(Point)
     * @since 2.0
     */
    public void setLocation(Point p) {
        location.setLocation(p);
        fireAnchorMoved();
    }

}
