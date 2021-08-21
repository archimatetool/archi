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
 * AbsoluteBendpoint is a Bendpoint that defines its location simply as its X
 * and Y coordinates. It is used by bendable {@link Connection Connections}.
 */
public class AbsoluteBendpoint extends Point implements Bendpoint {

    /**
     * Creates a new AbsoluteBendpoint at the Point p.
     * 
     * @param p
     *            The absolute location of the bendpoint
     * @since 2.0
     */
    public AbsoluteBendpoint(Point p) {
        super(p);
    }

    /**
     * Creates a new AbsoluteBendpoint at the Point (x,y).
     * 
     * @param x
     *            The X coordinate
     * @param y
     *            The Y coordinate
     * @since 2.0
     */
    public AbsoluteBendpoint(int x, int y) {
        super(x, y);
    }

    /**
     * @see org.eclipse.draw2d.Bendpoint#getLocation()
     */
    @Override
    public Point getLocation() {
        return this;
    }

}
