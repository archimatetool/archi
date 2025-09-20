package com.archimatetool.editor.diagram.policies.snaptogrid;

/**
 * snap-to-grid patch by Jean-Baptiste Sarrodie (aka Jaiguru)
 * 
 * This class has been extended to allow overriding
 * of class ExtendedConnectionBendpointTracker
 */

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
//package org.eclipse.gef.handles;

import org.eclipse.draw2d.BendpointLocator;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.BendpointMoveHandle;
// snap-to-grid patch
// Use alternate ConnectionBendpointTracker 
//import org.eclipse.gef.tools.ConnectionBendpointTracker;
import org.eclipse.swt.graphics.Color;

/**
 * A BendpointHandle that is used to move an existing bendpoint.
 */
public class ExtendedBendpointMoveHandle extends BendpointMoveHandle {
    
    private Dimension selectedSize = new Dimension(9, 9);
    private Dimension unSelectedSize = new Dimension(8, 8);
    
	/**
	 * Creates a new BendpointMoveHandle.
	 */
	public ExtendedBendpointMoveHandle() {
	}

	/**
	 * Creates a new BendpointMoveHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link BendpointLocator}.
	 * 
	 * @param owner
	 *            the ConnectionEditPart owner
	 * @param index
	 *            the index
	 */
	public ExtendedBendpointMoveHandle(ConnectionEditPart owner, int index) {
	    super(owner, index);
	}

	/**
	 * Creates a new BendpointMoveHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link BendpointLocator} with the given <code>locatorIndex</code>.
	 * 
	 * @param owner
	 *            the ConnectionEditPart owner
	 * @param index
	 *            the index
	 * @param locatorIndex
	 *            the index to use for the locator
	 */
	public ExtendedBendpointMoveHandle(ConnectionEditPart owner, int index, int locatorIndex) {
	    super(owner, index, locatorIndex);
	}

	/**
	 * Creates a new BendpointMoveHandle and sets its owner to
	 * <code>owner</code>, sets its index to <code>index</code>, and sets its
	 * locator to <code>locator</code>.
	 * 
	 * @param owner
	 *            the ConnectionEditPart owner
	 * @param index
	 *            the index
	 * @param locator
	 *            the Locator
	 */
	public ExtendedBendpointMoveHandle(ConnectionEditPart owner, int index, Locator locator) {
	    super(owner, index, locator);
	}

	/**
	 * Creates and returns a new {@link ExtendedConnectionBendpointTracker}.
	 * 
	 * @return the new ConnectionBendpointTracker
	 */
	@Override
    protected DragTracker createDragTracker() {
		ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker((ConnectionEditPart) getOwner(), getIndex());
		tracker.setType(RequestConstants.REQ_MOVE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}

    /**
     * Over-ride for custom colors
     */
	@Override
    protected Color getBorderColor() {
	    return isPrimary() ? ColorConstants.white : ColorConstants.gray;
    }
	
    /**
     * Over-ride for custom colors
     */
	@Override
    protected Color getFillColor() {
	    return isPrimary() ? ColorConstants.black : ColorConstants.white;
    }
	
	/**
	 * Over-ride to return a smaller size when not primary selection
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
	    return isPrimary() ? selectedSize : unSelectedSize;
	}
	
    /**
     * Over-ride to draw a circle instead of a square
     */
    @Override
    public void paintFigure(Graphics g) {
        Rectangle r = getBounds();
        r.shrink(1, 1);
        try {
            g.setBackgroundColor(getFillColor());
            g.fillOval(r);
            g.setForegroundColor(getBorderColor());
            g.drawOval(r);
        }
        finally {
            // We don't really own rect 'r', so fix it.
            r.expand(1, 1);
        }
    }
}

