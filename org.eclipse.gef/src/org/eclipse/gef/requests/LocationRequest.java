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

import org.eclipse.draw2d.geometry.Point;

/**
 * A Request that needs to keep track of a location.
 */
public class LocationRequest extends org.eclipse.gef.Request {

    private Point location;

    /**
     * Constructs a LocationRequest with no type.
     */
    public LocationRequest() {
    }

    /**
     * Constructs a LocationRequest with the given type.
     * 
     * @param type
     *            the type
     */
    public LocationRequest(Object type) {
        super(type);
    }

    /**
     * Returns the current location.
     * 
     * @return The current location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the current location.
     * 
     * @param p
     *            The current location.
     */
    public void setLocation(Point p) {
        location = p;
    }

}
