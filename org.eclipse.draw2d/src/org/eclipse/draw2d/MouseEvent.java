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
 * An event caused by the user interacting with the mouse.
 */
public class MouseEvent extends InputEvent {

    /** The X coordinate of the mouse event. */
    public int x;
    /** The Y coordinate of the mouse event. */
    public int y;

    /** The button that was pressed or released: {1, 2, 3}. */
    public int button;

    /**
     * Constructs a new MouseEvent.
     * 
     * @param dispatcher
     *            the event dispatcher
     * @param source
     *            the source of the event
     * @param me
     *            an SWT mouse event used to supply the state mask, button and
     *            position
     * @since 3.7
     */
    public MouseEvent(EventDispatcher dispatcher, IFigure source,
            org.eclipse.swt.events.MouseEvent me) {
        super(dispatcher, source, me.stateMask);
        this.button = me.button;
        Point pt = Point.SINGLETON;
        pt.setLocation(me.x, me.y);
        source.translateToRelative(pt);
        this.x = pt.x;
        this.y = pt.y;
    }

    /**
     * Constructor for MouseEvent.
     * 
     * @param x
     * @param y
     * @param dispatcher
     * @param f
     * @param button
     * @param stateMask
     * @since 3.6
     * @deprecated Use
     *             {@link #MouseEvent(EventDispatcher, IFigure, org.eclipse.swt.events.MouseEvent)}
     *             instead.
     */
    public MouseEvent(int x, int y, EventDispatcher dispatcher, IFigure f,
            int button, int stateMask) {
        super(dispatcher, f, stateMask);
        Point pt = Point.SINGLETON;
        pt.setLocation(x, y);
        f.translateToRelative(pt);
        this.button = button;
        this.x = pt.x;
        this.y = pt.y;
    }

    /**
     * @return the location of this mouse event
     */
    public Point getLocation() {
        return new Point(x, y);
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "MouseEvent(" + x + ',' + y + ") to Figure: " + source;//$NON-NLS-2$//$NON-NLS-1$
    }

}
