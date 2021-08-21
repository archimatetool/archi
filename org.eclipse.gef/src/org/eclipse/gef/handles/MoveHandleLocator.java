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
package org.eclipse.gef.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Locator used to place {@link MoveHandle}s. By default, a MoveHandle's
 * bounds are equal to its owner figure's bounds, expanded by the handle's
 * {@link Insets}.
 */
public class MoveHandleLocator implements Locator {

    private IFigure reference;

    /**
     * Creates a new MoveHandleLocator and sets its reference figure to
     * <code>ref</code>. The reference figure should be the handle's owner
     * figure.
     * 
     * @param ref
     *            the handle's owner
     */
    public MoveHandleLocator(IFigure ref) {
        setReference(ref);
    }

    /**
     * Returns the reference figure for this locator.
     * 
     * @return the handle's owner
     */
    protected IFigure getReference() {
        return reference;
    }

    /**
     * Sets the handle's bounds to that of its owner figure's bounds, expanded
     * by the handle's Insets.
     * 
     * @param target
     *            The IFigure to relocate
     */
    @Override
    public void relocate(IFigure target) {
        Insets insets = target.getInsets();
        Rectangle bounds;
        if (getReference() instanceof HandleBounds)
            bounds = ((HandleBounds) getReference()).getHandleBounds();
        else
            bounds = getReference().getBounds();
        bounds = new PrecisionRectangle(bounds.getResized(-1, -1));
        getReference().translateToAbsolute(bounds);
        target.translateToRelative(bounds);
        bounds.translate(-insets.left, -insets.top);
        bounds.resize(insets.getWidth() + 1, insets.getHeight() + 1);
        target.setBounds(bounds);
    }

    /**
     * Sets the reference figure.
     * 
     * @param follow
     *            the reference figure, should be the handle's owner figure
     */
    public void setReference(IFigure follow) {
        this.reference = follow;
    }

}
