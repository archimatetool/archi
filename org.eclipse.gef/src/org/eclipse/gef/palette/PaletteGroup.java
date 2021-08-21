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

import java.util.List;

/**
 * A PaletteGroup consists of a group of
 * {@link org.eclipse.gef.palette.PaletteEntry} objects that are uncollapsible .
 * The user modification level is set to
 * {@link PaletteEntry#PERMISSION_NO_MODIFICATION}, meaning that the entries
 * cannot be reordered.
 */
public class PaletteGroup extends PaletteContainer {

    /** Type Identifier **/
    public static final String PALETTE_TYPE_GROUP = "Palette_Group";//$NON-NLS-1$

    /**
     * Creates a new PaletteGroup with the given label
     * 
     * @param label
     *            the label
     */
    public PaletteGroup(String label) {
        super(label, null, null, PALETTE_TYPE_GROUP);
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
    public PaletteGroup(String label, List children) {
        this(label);
        addAll(children);
    }

}
