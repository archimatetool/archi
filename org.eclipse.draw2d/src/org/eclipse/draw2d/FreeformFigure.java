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

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A figure that allows its children to extend into negative coordinates. This
 * figure must be placed in a {@link org.eclipse.draw2d.FreeformViewport}. Also,
 * you shouldn't call {@link IFigure#setBounds(Rectangle)} on this figure. The
 * bounds will be calculated based on the extent of its children. A
 * FreeformFigure's bounds will be the smallest rectangle that will contain all
 * of its children.
 */
public interface FreeformFigure extends IFigure {

    /**
     * Adds a FreeformListener to this FreeformFigure.
     * 
     * @param listener
     *            the listener
     */
    void addFreeformListener(FreeformListener listener);

    /**
     * Notifies listeners that the freeform extent (i.e. bounds) has changed.
     */
    void fireExtentChanged();

    /**
     * Returns the freeform extent, essentially the bounds of the
     * FreeformFigure. This is based on the extent of its children.
     * 
     * @return the freeform extent
     */
    Rectangle getFreeformExtent();

    /**
     * Removes the given listener from this FreeformFigure.
     * 
     * @param listener
     *            the listener
     */
    void removeFreeformListener(FreeformListener listener);

    /**
     * Sets the freeform bounds of this FreeformFigure.
     * 
     * @param bounds
     *            the new freeform bounds
     */
    void setFreeformBounds(Rectangle bounds);

}
