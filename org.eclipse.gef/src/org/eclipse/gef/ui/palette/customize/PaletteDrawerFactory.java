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
package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteDrawer categories}
 * 
 * @author Pratik Shah
 */
public class PaletteDrawerFactory extends PaletteContainerFactory {

    /**
     * Constructor
     */
    public PaletteDrawerFactory() {
        setLabel(PaletteMessages.MODEL_TYPE_DRAWER);
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
     */
    @Override
    protected PaletteEntry createNewEntry(Shell shell) {
        PaletteEntry entry = new PaletteDrawer(PaletteMessages.NEW_DRAWER_LABEL);
        entry.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
        return entry;
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#determineTypeForNewEntry(org.eclipse.gef.palette.PaletteEntry)
     */
    @Override
    protected Object determineTypeForNewEntry(PaletteEntry selected) {
        return PaletteDrawer.PALETTE_TYPE_DRAWER;
    }

}
