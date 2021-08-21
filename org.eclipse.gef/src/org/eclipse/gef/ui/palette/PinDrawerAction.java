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
package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.ui.palette.editparts.IPinnableEditPart;

/**
 * An action that can be used to pin the given pinnable palette editpart (drawer
 * or stack) open.
 * 
 * @author Pratik Shah
 */
public class PinDrawerAction extends Action {

    private IPinnableEditPart pinnableEditPart;

    /**
     * Constructor
     * 
     * @param drawer
     *            The EditPart for the drawer that this action pins/unpins
     */
    public PinDrawerAction(DrawerEditPart drawer) {
        this.pinnableEditPart = drawer;
        setChecked(drawer.isPinnedOpen());
        setEnabled(drawer.isExpanded());
        setText(PaletteMessages.PINNED);
    }

    /**
     * Constructor
     * 
     * @param pinnableEditPart
     *            the pinnable palette editpart
     * @since 3.4
     */
    public PinDrawerAction(IPinnableEditPart pinnableEditPart) {
        this.pinnableEditPart = pinnableEditPart;
        setChecked(pinnableEditPart.isPinnedOpen());
        setEnabled(pinnableEditPart.isExpanded());
        setText(PaletteMessages.PINNED);
    }

    /**
     * Toggles the pinned open status of the pinnable palette editpart.
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        pinnableEditPart.setPinnedOpen(!pinnableEditPart.isPinnedOpen());
    }

}
