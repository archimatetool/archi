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

/**
 * An interface for objects that can be either horizontally or vertically
 * oriented.
 */
public interface Orientable extends PositionConstants, IFigure {

    /**
     * A constant representing a horizontal orientation.
     */
    int HORIZONTAL = 0;
    /**
     * A constant representing a vertical orientation.
     */
    int VERTICAL = 1;

    /**
     * Sets the orientation. Can be either {@link #HORIZONTAL} or
     * {@link #VERTICAL}.
     * 
     * @param orientation
     *            The orientation
     */
    void setOrientation(int orientation);

    /**
     * Sets the direction the orientable figure will face. Can be one of many
     * directional constants defined in {@link PositionConstants}.
     * 
     * @param direction
     *            The direction
     */
    void setDirection(int direction);

}
