/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A clipping strategy used to specify the clipping region for child figures.
 * 
 * @author Alexander Nyssen
 * 
 * @since 3.6
 */
public interface IClippingStrategy {

    /**
     * Specifies the clipping region for the given child figure. That is, all
     * parts of the figure, which are not covered by one of the returned
     * rectangles are masked out and will not get painted. Each returned
     * rectangle is considered to be specified in coordinates relative to the
     * given child figure's bounds.
     * 
     * @param childFigure
     *            The child figure, which clipping region has to be returned.
     * @return An array of rectangles to specify the clipping region of the
     *         figure, i.e. the areas in which the figure should not get
     *         clipped. May return an empty array in case the figure should not
     *         be visible at all, may not return <code>null</code>.
     */
    Rectangle[] getClip(IFigure childFigure);
}