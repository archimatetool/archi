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

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.tools.MarqueeSelectionTool;

/**
 * A palette ToolEntry for a {@link org.eclipse.gef.tools.MarqueeSelectionTool}.
 * 
 * @author rhudson
 * @author pshah
 * @author anyssen
 * 
 * @since 2.1
 */
public class MarqueeToolEntry extends ToolEntry {

    /**
     * Creates a new MarqueeToolEntry that can select nodes
     */
    public MarqueeToolEntry() {
        this(null, null);
    }

    /**
     * Constructor for MarqueeToolEntry.
     * 
     * @param label
     *            the label
     */
    public MarqueeToolEntry(String label) {
        this(label, null);
    }

    /**
     * Constructor for MarqueeToolEntry.
     * 
     * @param label
     *            the label; can be <code>null</code>
     * @param description
     *            the description (can be <code>null</code>)
     */
    public MarqueeToolEntry(String label, String description) {
        super(label, description, null, null, MarqueeSelectionTool.class);
        if (label == null || label.length() == 0)
            setLabel(GEFMessages.MarqueeTool_Label);
        setUserModificationPermission(PERMISSION_NO_MODIFICATION);
    }

    /**
     * @see org.eclipse.gef.palette.PaletteEntry#getDescription()
     */
    @Override
    public String getDescription() {
        String description = super.getDescription();
        if (description != null)
            return description;

        int marqueeBehavior = getMarqueeBehavior();
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED) {
            return GEFMessages.MarqueeTool_Connections_Touched_Desc;
        }
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_CONTAINED) {
            return GEFMessages.MarqueeTool_Connections_Contained_Desc;
        }
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED) {
            return GEFMessages.MarqueeTool_Nodes_Touched_Desc;
        }
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED) {
            return GEFMessages.MarqueeTool_Nodes_Contained_Desc;
        }
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED_AND_RELATED_CONNECTIONS) {
            return GEFMessages.MarqueeTool_Nodes_Touched_And_Related_Connections_Desc;
        }
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS) {
            return GEFMessages.MarqueeTool_Nodes_Contained_And_Related_Connections_Desc;
        }
        throw new IllegalArgumentException("Unknown marquee behavior"); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.gef.palette.PaletteEntry#getLargeIcon()
     */
    @Override
    public ImageDescriptor getLargeIcon() {
        ImageDescriptor imageDescriptor = super.getLargeIcon();
        if (imageDescriptor != null) {
            return imageDescriptor;
        }
        // infer icon from behavior mode
        int marqueeBehavior = getMarqueeBehavior();
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_CONTAINED
                || marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED) {
            return SharedImages.DESC_MARQUEE_TOOL_CONNECTIONS_24;
        } else if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED
                || marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED) {
            return SharedImages.DESC_MARQUEE_TOOL_NODES_24;
        } else {
            return SharedImages.DESC_MARQUEE_TOOL_24;
        }
    }

    private int getMarqueeBehavior() {
        // retrieve marquee behavior from tool property
        Object value = getToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR);
        if (value != null && value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        // return default behavior
        return MarqueeSelectionTool.DEFAULT_MARQUEE_BEHAVIOR;
    }

    /**
     * @see org.eclipse.gef.palette.PaletteEntry#getSmallIcon()
     */
    @Override
    public ImageDescriptor getSmallIcon() {
        ImageDescriptor imageDescriptor = super.getSmallIcon();
        if (imageDescriptor != null) {
            return imageDescriptor;
        }
        // infer icon from marquee behavior
        int marqueeBehavior = getMarqueeBehavior();
        if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_CONTAINED
                || marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED) {
            return SharedImages.DESC_MARQUEE_TOOL_CONNECTIONS_16;
        } else if (marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED
                || marqueeBehavior == MarqueeSelectionTool.BEHAVIOR_NODES_TOUCHED) {
            return SharedImages.DESC_MARQUEE_TOOL_NODES_16;
        } else {
            return SharedImages.DESC_MARQUEE_TOOL_16;
        }
    }

}
