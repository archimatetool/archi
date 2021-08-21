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
package org.eclipse.gef;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.draw2d.Cursors;

import org.eclipse.gef.internal.Internal;

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
        CURSOR_PLUG = createCursor("icons/plugmask.gif", //$NON-NLS-1$
                "icons/plug.bmp"); //$NON-NLS-1$
        CURSOR_PLUG_NOT = createCursor("icons/plugmasknot.gif", //$NON-NLS-1$
                "icons/plugnot.bmp"); //$NON-NLS-1$
        CURSOR_TREE_ADD = createCursor("icons/Tree_Add_Mask.gif", //$NON-NLS-1$
                "icons/Tree_Add.gif"); //$NON-NLS-1$
        CURSOR_TREE_MOVE = createCursor("icons/Tree_Move_Mask.gif", //$NON-NLS-1$
                "icons/Tree_Move.gif"); //$NON-NLS-1$
    }

    @SuppressWarnings("deprecation")
    private static Cursor createCursor(String sourceName, String maskName) {
        ImageDescriptor src = ImageDescriptor.createFromFile(Internal.class,
                sourceName);
        ImageDescriptor mask = ImageDescriptor.createFromFile(Internal.class,
                maskName);
        return new Cursor(null, src.getImageData(), mask.getImageData(), 0, 0);
    }

}
