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

import org.eclipse.gef.DragTracker;

/**
 * A handle for bendpoints on a connection.
 */
public class BendpointHandle extends ConnectionHandle implements
        PropertyChangeListener {

    /*
     * @TODO:Pratik No need to implement PropertyChangeListener or override
     * propertyChange()
     */

    private int index;

    /**
     * By default, <code>null</code> is returned for the DragTracker.
     * 
     * @return returns null by default
     */
    @Override
    protected DragTracker createDragTracker() {
        return null;
    }

    /**
     * Returns the index. This could mean different things for different
     * subclasses. It could be the index of the point the handle belongs to. Or
     * it could be the index of the handle itself. For
     * {@link BendpointCreationHandle}s and {@link BendpointMoveHandle}s, this
     * is the index of the handle itself, where these two types of handles are
     * indexed separately. For example, if you have one bendpoint, you will have
     * 2 creation handles, indexed as 0 and 1, and 1 move handle, indexed as 0.
     * 
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Revalidates this handle when the connection's points change.
     * 
     * @param event
     *            the event that caused the points change
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        revalidate();
    }

    /**
     * Sets the index.
     * 
     * @param i
     *            the new index
     * @see #getIndex()
     */
    protected void setIndex(int i) {
        index = i;
    }

}
