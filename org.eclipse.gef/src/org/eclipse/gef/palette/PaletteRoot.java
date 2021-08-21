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
package org.eclipse.gef.palette;

/**
 * Serves as the root {@link org.eclipse.gef.palette.PaletteEntry} for the
 * palette model.
 */
public class PaletteRoot extends PaletteContainer {

    /** Type Identifier **/
    public static String PALETTE_TYPE_ROOT = "Palette_Root";//$NON-NLS-1$
    private ToolEntry defaultEntry;

    /**
     * Creates a new PaletteRoot.
     */
    public PaletteRoot() {
        super(null, null, null, PALETTE_TYPE_ROOT);
    }

    /**
     * @see org.eclipse.gef.palette.PaletteContainer#acceptsType(java.lang.Object)
     */
    @Override
    public boolean acceptsType(Object type) {
        if (type.equals(ToolEntry.PALETTE_TYPE_TOOL)
                || type.equals(PaletteStack.PALETTE_TYPE_STACK))
            return false;
        return super.acceptsType(type);
    }

    /**
     * @return the default ToolEntry
     */
    public ToolEntry getDefaultEntry() {
        return defaultEntry;
    }

    /**
     * Sets the default entry to the passed value. This ToolEntry represents the
     * tool that will be loaded by default.
     * 
     * @param entry
     *            the default entry
     */
    public void setDefaultEntry(ToolEntry entry) {
        this.defaultEntry = entry;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Palette Root"; //$NON-NLS-1$
    }

}
