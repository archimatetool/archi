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
 * Provides support for anchors which depend on a figure for thier location.
 * 
 * @author hudsonr
 */
public abstract class AbstractConnectionAnchor extends ConnectionAnchorBase
        implements AncestorListener {

    private IFigure owner;

    /**
     * Constructs an AbstractConnectionAnchor with no owner.
     * 
     * @since 2.0
     */
    public AbstractConnectionAnchor() {
    }

    /**
     * Constructs an AbstractConnectionAnchor with the owner supplied as input.
     * 
     * @since 2.0
     * @param owner
     *            Owner of this anchor
     */
    public AbstractConnectionAnchor(IFigure owner) {
        setOwner(owner);
    }

    /**
     * Adds the given listener to the listeners to be notified of anchor
     * location changes.
     * 
     * @since 2.0
     * @param listener
     *            Listener to be added
     * @see #removeAnchorListener(AnchorListener)
     */
    @Override
    public void addAnchorListener(AnchorListener listener) {
        if (listener == null)
            return;
        if (listeners.size() == 0)
            getOwner().addAncestorListener(this);
        super.addAnchorListener(listener);
    }

    /**
     * Notifies all the listeners of this anchor's location change.
     * 
     * @since 2.0
     * @param figure
     *            Anchor-owning Figure which has moved
     * @see org.eclipse.draw2d.AncestorListener#ancestorMoved(IFigure)
     */
    @Override
    public void ancestorMoved(IFigure figure) {
        fireAnchorMoved();
    }

    /**
     * @see org.eclipse.draw2d.AncestorListener#ancestorAdded(IFigure)
     */
    @Override
    public void ancestorAdded(IFigure ancestor) {
    }

    /**
     * @see org.eclipse.draw2d.AncestorListener#ancestorRemoved(IFigure)
     */
    @Override
    public void ancestorRemoved(IFigure ancestor) {
    }

    /**
     * Returns the owner Figure on which this anchor's location is dependent.
     * 
     * @since 2.0
     * @return Owner of this anchor
     * @see #setOwner(IFigure)
     */
    @Override
    public IFigure getOwner() {
        return owner;
    }

    /**
     * Returns the point which is used as the reference by this
     * AbstractConnectionAnchor. It is generally dependent on the Figure which
     * is the owner of this AbstractConnectionAnchor.
     * 
     * @since 2.0
     * @return The reference point of this anchor
     * @see org.eclipse.draw2d.ConnectionAnchor#getReferencePoint()
     */
    @Override
    public Point getReferencePoint() {
        if (getOwner() == null)
            return null;
        else {
            Point ref = getOwner().getBounds().getCenter();
            getOwner().translateToAbsolute(ref);
            return ref;
        }
    }

    /**
     * Removes the given listener from this anchor. If all the listeners are
     * removed, then this anchor removes itself from its owner.
     * 
     * @since 2.0
     * @param listener
     *            Listener to be removed from this anchors listeners list
     * @see #addAnchorListener(AnchorListener)
     */
    @Override
    public void removeAnchorListener(AnchorListener listener) {
        super.removeAnchorListener(listener);
        if (listeners.size() == 0)
            getOwner().removeAncestorListener(this);
    }

    /**
     * Sets the owner of this anchor, on whom this anchors location is
     * dependent.
     * 
     * @since 2.0
     * @param owner
     *            Owner of this anchor
     */
    public void setOwner(IFigure owner) {
        this.owner = owner;
    }

}
