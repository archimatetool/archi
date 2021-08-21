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
 * A helper used to calculate the point at which a
 * {@link org.eclipse.draw2d.Connection} should bend. A bendpoint returns a
 * point <em>relative</em> to the connection figure on which it is being used.
 * This was chosen so that fixed bendpoints would be easy to implement. A fixed
 * bendpoint will have a fixed x and y value. Although the absolute x and y
 * location change during zoom and scrolling, the relative values stay the same.
 */
public interface Bendpoint {

    /**
     * Returns the location of the bendpoint <em>relative</em> to the
     * connection. The returned value may be by reference. The caller should NOT
     * modify the returned value.
     * 
     * @return the location of the bendpoint relative to the Connection
     */
    Point getLocation();

}
