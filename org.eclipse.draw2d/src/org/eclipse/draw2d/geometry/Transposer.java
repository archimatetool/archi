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
package org.eclipse.draw2d.geometry;

/**
 * Conditionally transposes geometrical objects based on an "enabled" flag. When
 * enabled, the method t(Object) will transpose the passed geometrical object.
 * Otherwise, the object will be returned without modification
 */
public class Transposer {

    private boolean enabled = false;

    /**
     * Disables transposing of inputs.
     * 
     * @since 2.0
     */
    public void disable() {
        enabled = false;
    }

    /**
     * Enables transposing of inputs.
     * 
     * @since 2.0
     */
    public void enable() {
        enabled = true;
    }

    /**
     * Returns <code>true</code> if this Transposer is enabled.
     * 
     * @return <code>true</code> if this Transposer is enabled
     * @since 2.0
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of this Transposer.
     * 
     * @param e
     *            New enabled value
     * @since 2.0
     */
    public void setEnabled(boolean e) {
        enabled = e;
    }

    /**
     * Returns a new transposed Dimension of the input Dimension.
     * 
     * @param d
     *            Input dimension being transposed.
     * @return The new transposed dimension.
     * @since 2.0
     */
    public Dimension t(Dimension d) {
        if (isEnabled())
            return d.getTransposed();
        return d;
    }

    /**
     * Returns a new transposed Insets of the input Insets.
     * 
     * @param i
     *            Insets to be transposed.
     * @return The new transposed Insets.
     * @since 2.0
     */
    public Insets t(Insets i) {
        if (isEnabled())
            return i.getTransposed();
        return i;
    }

    /**
     * Returns a new transposed Point of the input Point.
     * 
     * @param p
     *            Point to be transposed.
     * @return The new transposed Point.
     * @since 2.0
     */
    public Point t(Point p) {
        if (isEnabled())
            return p.getTransposed();
        return p;
    }

    /**
     * Returns a new transposed Rectangle of the input Rectangle.
     * 
     * @param r
     *            Rectangle to be transposed.
     * @return The new trasnposed Rectangle.
     * @since 2.0
     */
    public Rectangle t(Rectangle r) {
        if (isEnabled())
            return r.getTransposed();
        return r;
    }

}
