/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.ui.palette.editparts;

/**
 * This interface is used to identify and work with a pinnable palette editpart
 * (e.g. drawers, stacks).
 * 
 * @author crevells
 * @since 3.4
 */
public interface IPinnableEditPart {

    /**
     * Returns <code>true</code> if the palette editpart is pinned open.
     * 
     * @return boolean
     */
    public boolean isPinnedOpen();

    /**
     * @return <code>true</code> if the palette editpart can be pinned open.
     */
    public boolean canBePinned();

    /**
     * Sets the palette editpart's pinned state to the specified value.
     * 
     * @param pinned
     *            <code>true</code> if the palette editpart should be pinned
     *            when opened
     */
    public void setPinnedOpen(boolean pinned);

    /**
     * Returns the expansion state of the palette editpart
     * 
     * @return <code>true</code> if the palette editpart is expanded; false
     *         otherwise
     */
    public boolean isExpanded();

}
