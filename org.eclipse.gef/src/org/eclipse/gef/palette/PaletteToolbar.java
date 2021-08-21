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
package org.eclipse.gef.palette;

import java.util.List;

/**
 * A PaletteGroup consists of a group of
 * {@link org.eclipse.gef.palette.PaletteEntry} objects that are uncollapsible .
 * The user modification level is set to
 * {@link PaletteEntry#PERMISSION_NO_MODIFICATION}, meaning that the entries
 * cannot be reordered.
 * 
 * @author crevells
 * @since 3.4
 */
public class PaletteToolbar extends PaletteContainer {

    /**
     * Type Identifier for a palette group that looks like a toolbar and only
     * supports icons mode.
     * 
     * @since 3.4
     */
    public static final String PALETTE_TYPE_TOOLBAR_GROUP = "Palette_Toolbar_Group";//$NON-NLS-1$

    /**
     * Creates a new PaletteGroup with the given label
     * 
     * @param label
     *            the label
     */
    public PaletteToolbar(String label) {
        super(label, null, null, PALETTE_TYPE_TOOLBAR_GROUP);
        setUserModificationPermission(PERMISSION_NO_MODIFICATION);
    }

    /**
     * Creates a new PaletteGroup with the given label and list of
     * {@link PaletteEntry Palette Entries}.
     * 
     * @param label
     *            the label
     * @param children
     *            the list of PaletteEntry children
     */
    @SuppressWarnings("rawtypes")
    public PaletteToolbar(String label, List children) {
        this(label);
        addAll(children);
    }

}
