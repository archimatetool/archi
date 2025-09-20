package com.archimatetool.editor.diagram.policies.snaptogrid;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

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

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.BendpointCreationHandle;
// snap-to-grid patch
// Use alternate ConnectionBendpointTracker
//import org.eclipse.gef.tools.ConnectionBendpointTracker;
import org.eclipse.swt.graphics.Color;

/**
 * A BendpointHandle that is used to create a new bendpoint.
 */
public class ExtendedBendpointCreationHandle extends BendpointCreationHandle {
    
    {
        setPreferredSize(new Dimension(6, 6));
    }
    
	public ExtendedBendpointCreationHandle(ConnectionEditPart owner, int index, int locatorIndex) {
	    super(owner, index, locatorIndex);
    }

    /**
	 * Creates and returns a new {@link ExtendedConnectionBendpointTracker}.
	 * 
	 * @return the new ConnectionBendpointTracker
	 */
	@Override
    protected DragTracker createDragTracker() {
		ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker((ConnectionEditPart) getOwner(), getIndex());
		tracker.setType(RequestConstants.REQ_CREATE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}
	
    /**
     * Over-ride for custom colors
     */
    @Override
    protected Color getBorderColor() {
        return isPrimary() ? ColorConstants.black : ColorConstants.gray;
    }
    
    /**
     * Over-ride for custom colors
     */
    @Override
    protected Color getFillColor() {
        return ColorConstants.white;
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

