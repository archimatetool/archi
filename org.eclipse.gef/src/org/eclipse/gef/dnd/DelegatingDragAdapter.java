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
package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * A DragSourceListener that maintains and delegates to a set of
 * {@link TransferDragSourceListener}s. Each TransferDragSourceListener can then
 * be implemented as if it were the DragSource's only DragSourceListener.
 * <P>
 * When a native Drag is started, a subset of all
 * <code>TransferDragSourceListeners</code> is generated and stored in a list of
 * <i>active</i> listeners. This subset is calculated by forwarding
 * {@link DragSourceListener#dragStart(DragSourceEvent)} to every listener, and
 * inspecting changes to the {@link DragSourceEvent#doit doit} field. The
 * <code>DragSource</code>'s set of supported Transfer types (
 * {@link DragSource#setTransfer(Transfer[])}) is updated to reflect the
 * Transfer types corresponding to the active listener subset.
 * <P>
 * If and when {@link #dragSetData(DragSourceEvent)} is called, a single
 * <code>TransferDragSourceListener</code> is chosen, and only it is allowed to
 * set the drag data. The chosen listener is the first listener in the subset of
 * active listeners whose Transfer supports (
 * {@link Transfer#isSupportedType(TransferData)}) the dataType on the
 * <code>DragSourceEvent</code>.
 */
public class DelegatingDragAdapter extends
        org.eclipse.jface.util.DelegatingDragAdapter {

    /**
     * Adds the given TransferDragSourceListener. The set of Transfer types is
     * updated to reflect the change.
     * 
     * @param listener
     *            the new listener
     * @deprecated
     */
    public void addDragSourceListener(TransferDragSourceListener listener) {
        super.addDragSourceListener(listener);
    }

    /**
     * Combines the <code>Transfer</code>s from every
     * TransferDragSourceListener.
     * 
     * @return the combined <code>Transfer</code>s
     * @deprecated call getTransfers() instead.
     */
    public Transfer[] getTransferTypes() {
        return super.getTransfers();
    }

    /**
     * Adds the given TransferDragSourceListener. The set of Transfer types is
     * updated to reflect the change.
     * 
     * @param listener
     *            the listener being removed
     * @deprecated
     */
    public void removeDragSourceListener(TransferDragSourceListener listener) {
        super.removeDragSourceListener(listener);
    }

}
