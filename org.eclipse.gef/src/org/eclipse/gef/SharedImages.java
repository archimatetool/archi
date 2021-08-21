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

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.internal.Internal;

/**
 * A class containing shared Images and ImageDescriptors for use by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class SharedImages {

    /**
     * A 16x16 icon representing the Selection Tool
     */
    public static final ImageDescriptor DESC_SELECTION_TOOL_16;

    /**
     * A 24x24 icon representing the Selection Tool
     */
    public static final ImageDescriptor DESC_SELECTION_TOOL_24;

    /**
     * A 16x16 icon representing the Marquee Tool (nodes and connections)
     */
    public static final ImageDescriptor DESC_MARQUEE_TOOL_16;

    /**
     * A 24x24 icon representing the Marquee Tool (nodes and connections)
     */
    public static final ImageDescriptor DESC_MARQUEE_TOOL_24;

    /**
     * A 16x16 icon representing the Marquee Tool (nodes only)
     * 
     * @since 3.7
     */
    public static final ImageDescriptor DESC_MARQUEE_TOOL_NODES_16;

    /**
     * A 24x24 icon representing the Marquee Tool (nodes only).
     * 
     * @since 3.7
     */
    public static final ImageDescriptor DESC_MARQUEE_TOOL_NODES_24;

    /**
     * A 16x16 icon representing the Marquee Tool (connections only)
     * 
     * @since 3.7
     */
    public static final ImageDescriptor DESC_MARQUEE_TOOL_CONNECTIONS_16;

    /**
     * A 24x24 icon representing the Marquee Tool (connections only).
     * 
     * @since 3.7
     */
    public static final ImageDescriptor DESC_MARQUEE_TOOL_CONNECTIONS_24;

    static {
        DESC_SELECTION_TOOL_16 = createDescriptor("icons/arrow16.gif"); //$NON-NLS-1$
        DESC_SELECTION_TOOL_24 = createDescriptor("icons/arrow24.gif"); //$NON-NLS-1$
        DESC_MARQUEE_TOOL_16 = createDescriptor("icons/marquee16.gif"); //$NON-NLS-1$
        DESC_MARQUEE_TOOL_24 = createDescriptor("icons/marquee24.gif"); //$NON-NLS-1$
        DESC_MARQUEE_TOOL_NODES_16 = createDescriptor("icons/marquee_nodes16.gif"); //$NON-NLS-1$
        DESC_MARQUEE_TOOL_NODES_24 = createDescriptor("icons/marquee_nodes24.gif"); //$NON-NLS-1$
        DESC_MARQUEE_TOOL_CONNECTIONS_16 = createDescriptor("icons/marquee_wires16.gif"); //$NON-NLS-1$
        DESC_MARQUEE_TOOL_CONNECTIONS_24 = createDescriptor("icons/marquee_wires24.gif"); //$NON-NLS-1$
    }

    private static ImageDescriptor createDescriptor(String filename) {
        return ImageDescriptor.createFromFile(Internal.class, filename);
    }

}
