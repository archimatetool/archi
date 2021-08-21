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

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;

/**
 * Base implementation for handles. This class keeps track of the typical data
 * needed by a handle, such as a drag tracker, a locator to place the handle, a
 * cursor, and the editpart to which the handle belongs. AbstractHandle will add
 * an {@link AncestorListener} to the owner's figure, and will automatically
 * revalidate this handle whenever the owner's figure moves.
 */
public abstract class AbstractHandle extends Figure implements Handle,
        AncestorListener {

    private GraphicalEditPart editpart;
    private DragTracker dragTracker;
    private Locator locator;

    /**
     * Null constructor
     */
    public AbstractHandle() {
    }

    /**
     * Creates a handle for the given <code>GraphicalEditPart</code> using the
     * given <code>Locator</code>.
     * 
     * @param owner
     *            The editpart which provided this handle
     * @param loc
     *            The locator to position the handle
     */
    public AbstractHandle(GraphicalEditPart owner, Locator loc) {
        setOwner(owner);
        setLocator(loc);
    }

    /**
     * Creates a handle for the given <code>GraphicalEditPart</code> using the
     * given <code>Locator</code> and <code>Cursor</code>.
     * 
     * @param owner
     *            The editpart which provided this handle
     * @param loc
     *            The locator to position the handle
     * @param c
     *            The cursor to display when the mouse is over the handle
     */
    public AbstractHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
        this(owner, loc);
        setCursor(c);
    }

    /**
     * Adds this as an {@link AncestorListener} to the owner's {@link Figure}.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        // Listen to the owner figure so the handle moves when the
        // figure moves.
        getOwnerFigure().addAncestorListener(this);
    }

    /**
     * @see org.eclipse.draw2d.AncestorListener#ancestorMoved(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void ancestorMoved(IFigure ancestor) {
        revalidate();
    }

    /**
     * @see org.eclipse.draw2d.AncestorListener#ancestorAdded(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void ancestorAdded(IFigure ancestor) {
    }

    /**
     * @see org.eclipse.draw2d.AncestorListener#ancestorRemoved(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void ancestorRemoved(IFigure ancestor) {
    }

    /**
     * Creates a new drag tracker to be returned by getDragTracker().
     * 
     * @return a new drag tracker
     */
    protected abstract DragTracker createDragTracker();

    /**
     * By default, the center of the handle is returned.
     * 
     * @see org.eclipse.gef.Handle#getAccessibleLocation()
     */
    @Override
    public Point getAccessibleLocation() {
        Point p = getBounds().getCenter();
        translateToAbsolute(p);
        return p;
    }

    /**
     * Returns the cursor. The cursor is displayed whenever the mouse is over
     * the handle.
     * 
     * @deprecated use getCursor()
     * @return the cursor
     */
    public Cursor getDragCursor() {
        return getCursor();
    }

    /**
     * Returns the drag tracker to use when the user clicks on this handle. If
     * the drag tracker has not been set, it will be lazily created by calling
     * {@link #createDragTracker()}.
     * 
     * @return the drag tracker
     */
    @Override
    public DragTracker getDragTracker() {
        if (dragTracker == null)
            dragTracker = createDragTracker();
        return dragTracker;
    }

    /**
     * Returns the <code>Locator</code> used to position this handle.
     * 
     * @return the locator
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * Returns the <code>GraphicalEditPart</code> associated with this handle.
     * 
     * @return the owner editpart
     */
    protected GraphicalEditPart getOwner() {
        return editpart;
    }

    /**
     * Convenience method to return the owner's figure.
     * 
     * @return the owner editpart's figure
     */
    protected IFigure getOwnerFigure() {
        return getOwner().getFigure();
    }

    /**
     * @see org.eclipse.draw2d.IFigure#removeNotify()
     */
    @Override
    public void removeNotify() {
        getOwnerFigure().removeAncestorListener(this);
        super.removeNotify();
    }

    /**
     * Sets the Cursor for the handle.
     * 
     * @param c
     *            the cursor
     * @throws Exception
     *             a bogus excpetion declaration
     * @deprecated use setCursor()
     */
    public void setDragCursor(Cursor c) throws Exception {
        setCursor(c);
    }

    /**
     * Sets the drag tracker for this handle.
     * 
     * @param t
     *            the drag tracker
     */
    public void setDragTracker(DragTracker t) {
        dragTracker = t;
    }

    /**
     * Sets the locator which position this handle.
     * 
     * @param locator
     *            the new locator
     */
    protected void setLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * Sets the owner editpart associated with this handle.
     * 
     * @param editpart
     *            the owner
     */
    protected void setOwner(GraphicalEditPart editpart) {
        this.editpart = editpart;
    }

    /**
     * Extends validate() to place the handle using its locator.
     * 
     * @see org.eclipse.draw2d.IFigure#validate()
     */
    @Override
    public void validate() {
        if (isValid())
            return;
        getLocator().relocate(this);
        super.validate();
    }

}
