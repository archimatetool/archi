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

/**
 * Classes which implement this interface provide methods to respond to changes
 * in the ancestor properties of a Figure.
 * <P>
 * Instances of this class can be added as listeners to a figure using the
 * <code>addAncestorListener</code> method and removed using the
 * <code>removeAncestoreListener</code> method. When the parent chain of the
 * figure being observed changes or moves, the listener will be notified
 * appropriately.
 */
public interface AncestorListener {

    /**
     * Called when an ancestor has been added into the listening figure's
     * hierarchy.
     * 
     * @param ancestor
     *            The ancestor that was added
     */
    void ancestorAdded(IFigure ancestor);

    /**
     * Called when an ancestor has moved to a new location.
     * 
     * @param ancestor
     *            The ancestor that has moved
     */
    void ancestorMoved(IFigure ancestor);

    /**
     * Called when an ancestor has been removed from the listening figure's
     * hierarchy.
     * 
     * @param ancestor
     *            The ancestor that has been removed
     */
    void ancestorRemoved(IFigure ancestor);

    /**
     * An empty implementation of AncestorListener for convenience.
     */
    class Stub implements AncestorListener {
        @Override
        public void ancestorMoved(IFigure ancestor) {
        }

        @Override
        public void ancestorAdded(IFigure ancestor) {
        }

        @Override
        public void ancestorRemoved(IFigure ancestor) {
        }
    }

}
