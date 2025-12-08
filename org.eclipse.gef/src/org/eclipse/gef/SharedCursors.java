/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.internal.Internal;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;

/**
 * A shared collection of Cursors.
 *
 * @since 2.0
 */
public class SharedCursors extends Cursors {

    /**
     * Cursor for valid connection
     */
    public static final Cursor CURSOR_PLUG;
    /**
     * Cursor for invalid connection
     */
    public static final Cursor CURSOR_PLUG_NOT;
    /**
     * Cursor for adding to a tree
     */
    public static final Cursor CURSOR_TREE_ADD;
    /**
     * Cursor for dragging in a tree
     */
    public static final Cursor CURSOR_TREE_MOVE;

    static {
        CURSOR_PLUG = createCursor("icons/plug-cursor.png"); //$NON-NLS-1$
        CURSOR_PLUG_NOT = createCursor("icons/plugnot-cursor.png"); //$NON-NLS-1$
        CURSOR_TREE_ADD = createCursor("icons/tree_add-cursor.png"); //$NON-NLS-1$
        CURSOR_TREE_MOVE = createCursor("icons/tree_move-cursor.png"); //$NON-NLS-1$
    }

    private static Cursor createCursor(String sourceName) {
        // Loading via an ImageDescriptor first will load the @2x image on hi-dpi
        ImageDescriptor src = ImageDescriptor.createFromFile(Internal.class, sourceName);
        return new Cursor(null,
                zoom -> src.getImageData(zoom),
                0, 0);
    }
}
