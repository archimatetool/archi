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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

/**
 * A small square handle approximately 7x7 pixels in size, that is either black
 * or white.
 */
public abstract class SquareHandle extends AbstractHandle {

    /**
     * The default size for square handles.
     */
    protected static final int DEFAULT_HANDLE_SIZE = 7;

    {
        init();
    }

    /**
     * Null constructor
     */
    public SquareHandle() {
    }

    /**
     * Creates a SquareHandle for the given <code>GraphicalEditPart</code> with
     * the given <code>Locator</code>.
     * 
     * @param owner
     *            the owner
     * @param loc
     *            the locator
     */
    public SquareHandle(GraphicalEditPart owner, Locator loc) {
        super(owner, loc);
    }

    /**
     * Creates a SquareHandle for the given <code>GraphicalEditPart</code> with
     * the given <code>Cursor</code> using the given <code>Locator</code>.
     * 
     * @param owner
     *            The editpart which provided this handle
     * @param loc
     *            The locator to position the handle
     * @param c
     *            The cursor to display when the mouse is over the handle
     */
    public SquareHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
        super(owner, loc, c);
    }

    /**
     * Returns the color for the outside of the handle.
     * 
     * @return the color for the border
     */
    protected Color getBorderColor() {
        return (isPrimary()) ? ColorConstants.white : ColorConstants.black;
    }

    /**
     * Returns the color for the inside of the handle.
     * 
     * @return the color of the handle
     */
    protected Color getFillColor() {
        return (isPrimary()) ? ColorConstants.black : ColorConstants.white;
    }

    /**
     * Initializes the handle.
     */
    protected void init() {
        setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE, DEFAULT_HANDLE_SIZE));
    }

    /**
     * Returns <code>true</code> if the handle's owner is the primary selection.
     * 
     * @return <code>true</code> if the handles owner has primary selection.
     */
    protected boolean isPrimary() {
        return getOwner().getSelected() == EditPart.SELECTED_PRIMARY;
    }

    /**
     * Draws the handle with fill color and outline color dependent on the
     * primary selection status of the owner editpart.
     * 
     * @param g
     *            The graphics used to paint the figure.
     */
    @Override
    public void paintFigure(Graphics g) {
        Rectangle r = getBounds();
        r.shrink(1, 1);
        try {
            g.setBackgroundColor(getFillColor());
            g.fillRectangle(r.x, r.y, r.width, r.height);
            g.setForegroundColor(getBorderColor());
            g.drawRectangle(r.x, r.y, r.width, r.height);
        } finally {
            // We don't really own rect 'r', so fix it.
            r.expand(1, 1);
        }
    }

}
