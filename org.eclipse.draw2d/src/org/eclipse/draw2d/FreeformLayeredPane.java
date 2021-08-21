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

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A LayeredPane that contains {@link org.eclipse.draw2d.FreeformLayer
 * FreeformLayers}.
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public class FreeformLayeredPane extends LayeredPane implements FreeformFigure {
    private FreeformHelper helper = new FreeformHelper(this);

    /**
     * Constructs a new FreeformLayeredPane.
     */
    public FreeformLayeredPane() {
        setLayoutManager(null);
    }

    /**
     * @see IFigure#add(IFigure, Object, int)
     */
    @Override
    public void add(IFigure child, Object constraint, int index) {
        super.add(child, constraint, index);
        helper.hookChild(child);
    }

    /**
     * @see FreeformFigure#addFreeformListener(FreeformListener)
     */
    @Override
    public void addFreeformListener(FreeformListener listener) {
        addListener(FreeformListener.class, listener);
    }

    /**
     * @see FreeformFigure#fireExtentChanged()
     */
    @Override
    public void fireExtentChanged() {
        Iterator iter = getListeners(FreeformListener.class);
        while (iter.hasNext())
            ((FreeformListener) iter.next()).notifyFreeformExtentChanged();
    }

    /**
     * Overrides to do nothing.
     * 
     * @see Figure#fireMoved()
     */
    @Override
    protected void fireMoved() {
    }

    /**
     * Returns the FreeformHelper.
     * 
     * @return the FreeformHelper
     */
    protected FreeformHelper getFreeformHelper() {
        return helper;
    }

    /**
     * @see FreeformFigure#getFreeformExtent()
     */
    @Override
    public Rectangle getFreeformExtent() {
        return helper.getFreeformExtent();
    }

    /**
     * @see Figure#primTranslate(int, int)
     */
    @Override
    protected void primTranslate(int dx, int dy) {
        bounds.x += dx;
        bounds.y += dy;
    }

    /**
     * @see IFigure#remove(IFigure)
     */
    @Override
    public void remove(IFigure child) {
        helper.unhookChild(child);
        super.remove(child);
    }

    /**
     * @see FreeformFigure#removeFreeformListener(FreeformListener)
     */
    @Override
    public void removeFreeformListener(FreeformListener listener) {
        removeListener(FreeformListener.class, listener);
    }

    /**
     * @see FreeformFigure#setFreeformBounds(Rectangle)
     */
    @Override
    public void setFreeformBounds(Rectangle bounds) {
        helper.setFreeformBounds(bounds);
    }

    /**
     * Calls {@link Figure#fireMoved() super.fireMoved()}.
     */
    protected void superFireMoved() {
        super.fireMoved();
    }

}
