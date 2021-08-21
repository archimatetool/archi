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

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Locator;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * A Handle used to resize a GraphicalEditPart.
 */
public class ResizeHandle extends SquareHandle {

    private int cursorDirection = 0;

    /**
     * Creates a new ResizeHandle for the given GraphicalEditPart.
     * <code>direction</code> is the relative direction from the center of the
     * owner figure. For example, <code>SOUTH_EAST</code> would place the handle
     * in the lower-right corner of its owner figure. These direction constants
     * can be found in {@link org.eclipse.draw2d.PositionConstants}.
     * 
     * @param owner
     *            owner of the ResizeHandle
     * @param direction
     *            relative direction from the center of the owner figure
     */
    public ResizeHandle(GraphicalEditPart owner, int direction) {
        setOwner(owner);
        setLocator(new RelativeHandleLocator(owner.getFigure(), direction));
        setCursor(Cursors.getDirectionalCursor(direction, owner.getFigure()
                .isMirrored()));
        cursorDirection = direction;
    }

    /**
     * Creates a new ResizeHandle for the given GraphicalEditPart.
     * 
     * @see SquareHandle#SquareHandle(GraphicalEditPart, Locator, Cursor)
     */
    public ResizeHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
        super(owner, loc, c);
    }

    /**
     * Returns <code>null</code> for the DragTracker.
     * 
     * @return returns <code>null</code>
     */
    @Override
    protected DragTracker createDragTracker() {
        return new ResizeTracker(getOwner(), cursorDirection);
    }

}
