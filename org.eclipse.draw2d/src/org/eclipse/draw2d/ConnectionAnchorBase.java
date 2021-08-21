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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides support for a ConnectionAnchor. A ConnectionAnchor is one of the end
 * points of a {@link Connection}. It holds listeners and notifies them if the
 * anchor is moved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ConnectionAnchorBase implements ConnectionAnchor {

    /**
     * The list of listeners
     */
    protected List listeners = new ArrayList(1);

    /**
     * @see org.eclipse.draw2d.ConnectionAnchor#addAnchorListener(AnchorListener)
     */
    @Override
    public void addAnchorListener(AnchorListener listener) {
        listeners.add(listener);
    }

    /**
     * @see org.eclipse.draw2d.ConnectionAnchor#removeAnchorListener(AnchorListener)
     */
    @Override
    public void removeAnchorListener(AnchorListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all the listeners in the list of a change in position of this
     * anchor. This is called from one of the implementing anchors when its
     * location is changed.
     * 
     * @since 2.0
     */
    protected void fireAnchorMoved() {
        Iterator iter = listeners.iterator();
        while (iter.hasNext())
            ((AnchorListener) iter.next()).anchorMoved(this);
    }

}
