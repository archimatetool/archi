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

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteStack}
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
public class PaletteStackFactory extends PaletteEntryFactory {

    /**
     * Creates a new PaletteStackFactory with label
     * PaletteMessages.MODEL_TYPE_STACK
     */
    public PaletteStackFactory() {
        setLabel(PaletteMessages.MODEL_TYPE_STACK);
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#canCreate(org.eclipse.gef.palette.PaletteEntry)
     */
    @Override
    public boolean canCreate(PaletteEntry selected) {
        if (!(selected instanceof ToolEntry)
                || selected.getParent() instanceof PaletteStack)
            return false;
        return super.canCreate(selected);
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
     */
    @Override
    protected PaletteEntry createNewEntry(Shell shell) {
        return new PaletteStack(PaletteMessages.NEW_STACK_LABEL, null, null);
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(org.eclipse.swt.widgets.Shell,
     *      org.eclipse.gef.palette.PaletteEntry)
     */
    @Override
    public PaletteEntry createNewEntry(Shell shell, PaletteEntry selected) {
        PaletteContainer parent = determineContainerForNewEntry(selected);
        int index = determineIndexForNewEntry(parent, selected);
        PaletteEntry entry = createNewEntry(shell);
        parent.remove(selected);
        parent.add(index - 1, entry);
        ((PaletteStack) entry).add(selected);
        entry.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
        return entry;
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#determineTypeForNewEntry(org.eclipse.gef.palette.PaletteEntry)
     */
    @Override
    protected Object determineTypeForNewEntry(PaletteEntry selected) {
        return PaletteStack.PALETTE_TYPE_STACK;
    }

}
