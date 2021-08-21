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

import org.eclipse.gef.tools.PanningSelectionTool;

/**
 * A ToolEntry for a {@link PanningSelectionTool}.
 * 
 * @author msorens
 * @since 3.0
 */
public class PanningSelectionToolEntry extends SelectionToolEntry {

    /**
     * Creates a new PanningSelectionToolEntry.
     */
    public PanningSelectionToolEntry() {
        this(null);
    }

    /**
     * Constructor for PanningSelectionToolEntry.
     * 
     * @param label
     *            the label
     */
    public PanningSelectionToolEntry(String label) {
        this(label, null);
    }

    /**
     * Constructor for PanningSelectionToolEntry.
     * 
     * @param label
     *            the label
     * @param shortDesc
     *            the description
     */
    public PanningSelectionToolEntry(String label, String shortDesc) {
        super(label, shortDesc);
        setToolClass(PanningSelectionTool.class);
    }

}
