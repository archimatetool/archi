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

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

/**
 * A palette ToolEntry for a {@link CreationTool}.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class CreationToolEntry extends ToolEntry {

    /**
     * The creation factory used with the returned creation tool.
     * 
     * @deprecated in 3.1. This field will be removed in future releases. The
     *             factory is being provided to the tool via the
     *             {@link ToolEntry#setToolProperty(Object, Object)} method.
     */
    protected final CreationFactory factory;

    /**
     * Constructor for CreationToolEntry.
     * 
     * @param label
     *            the label
     * @param shortDesc
     *            the description
     * @param factory
     *            the CreationFactory
     * @param iconSmall
     *            the small icon
     * @param iconLarge
     *            the large icon
     */
    public CreationToolEntry(String label, String shortDesc,
            CreationFactory factory, ImageDescriptor iconSmall,
            ImageDescriptor iconLarge) {
        super(label, shortDesc, iconSmall, iconLarge, CreationTool.class);
        this.factory = factory;
        setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, factory);
    }

}
