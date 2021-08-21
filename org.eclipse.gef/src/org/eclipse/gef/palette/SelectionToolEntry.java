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

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.tools.SelectionTool;

/**
 * A ToolEntry for a {@link SelectionTool}.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class SelectionToolEntry extends ToolEntry {

    /**
     * Creates a new SelectionToolEntry.
     */
    public SelectionToolEntry() {
        this(null);
    }

    /**
     * Constructor for SelectionToolEntry.
     * 
     * @param label
     *            the label
     */
    public SelectionToolEntry(String label) {
        this(label, null);
    }

    /**
     * Constructor for SelectionToolEntry.
     * 
     * @param label
     *            the label
     * @param shortDesc
     *            the description
     */
    public SelectionToolEntry(String label, String shortDesc) {
        super(label, shortDesc, SharedImages.DESC_SELECTION_TOOL_16,
                SharedImages.DESC_SELECTION_TOOL_24, SelectionTool.class);
        if (label == null || label.length() == 0)
            setLabel(GEFMessages.SelectionTool_Label);
        setUserModificationPermission(PERMISSION_NO_MODIFICATION);
    }

}
