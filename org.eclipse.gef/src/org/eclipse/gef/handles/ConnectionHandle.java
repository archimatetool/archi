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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Cursors;

/**
 * The base implementation for handles used with editparts whose figure is a
 * {@link org.eclipse.draw2d.Connection}. This class adds an additional listener
 * to the owner's connection figure to receive notification whenever the owner's
 * connection's points are changed. Changing the points of a connection does not
 * fire "figure moved", it only fires "points" property as changing.
 */
public abstract class ConnectionHandle extends SquareHandle implements
        PropertyChangeListener {

    private boolean fixed = false;

    /**
     * Creates a new ConnectionHandle.
     */
    public ConnectionHandle() {
        setCursor(Cursors.CROSS);
    }

    /**
     * Creates a new handle with the given fixed setting. If the handle is
     * fixed, it cannot be dragged.
     * 
     * @param fixed
     *            <code>true</code> if the handle cannot be dragged.
     */
    public ConnectionHandle(boolean fixed) {
        setFixed(fixed);
        if (fixed)
            setCursor(Cursors.NO);
        else
            setCursor(Cursors.CROSS);
    }

    /**
     * Adds this as a {@link org.eclipse.draw2d.FigureListener} to the owner's
     * {@link org.eclipse.draw2d.Figure}.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        getConnection().addPropertyChangeListener(Connection.PROPERTY_POINTS,
                this);
    }

    /**
     * Convenience method to return the owner's figure typed as
     * <code>Connection</code>.
     * 
     * @return the owner's connection
     */
    public Connection getConnection() {
        return (Connection) getOwnerFigure();
    }

    /**
     * Returns true if the handle cannot be dragged.
     * 
     * @return <code>true</code> if the handle cannot be dragged
     */
    protected boolean isFixed() {
        return fixed;
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Connection.PROPERTY_POINTS))
            revalidate();
    }

    /**
     * Extended to remove a listener.
     * 
     * @see org.eclipse.draw2d.IFigure#removeNotify()
     */
    @Override
    public void removeNotify() {
        getConnection().removePropertyChangeListener(
                Connection.PROPERTY_POINTS, this);
        super.removeNotify();
    }

    /**
     * Sets whether the handle is fixed and cannot be moved
     * 
     * @param fixed
     *            <code>true</code> if the handle should be unmovable
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
        if (fixed)
            setCursor(Cursors.NO);
        else
            setCursor(Cursors.CROSS);
    }

}
