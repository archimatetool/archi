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

import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * A separator for the palette <br>
 * <br>
 * Separators can also be used as markers. Palettes that expect external code to
 * add entries to it can use such markers to indicate where those new entries
 * should be added. For this to happen, a separator must be uniquely identified.
 * Unless a separator is not a marker, it is recommended that it be given a
 * unique ID.
 * 
 * @author Pratik Shah
 */
public class PaletteSeparator extends PaletteEntry {

    /** Type identifier **/
    public static final Object PALETTE_TYPE_SEPARATOR = "$Palette Separator";//$NON-NLS-1$

    /**
     * Creates a new PaletteSeparator with an empty string as its identifier.
     */
    public PaletteSeparator() {
        this(""); //$NON-NLS-1$
    }

    /**
     * Constructor
     * 
     * @param id
     *            This Separator's unique ID
     */
    public PaletteSeparator(String id) {
        super(PaletteMessages.NEW_SEPARATOR_LABEL, "", PALETTE_TYPE_SEPARATOR);//$NON-NLS-1$
        setId(id);
    }

}
