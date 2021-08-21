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

/**
 * @author Eric Bordeau
 */
public class PaletteTemplateEntry extends PaletteEntry {

    private Object template;

    /** Type identifier **/
    public static final String PALETTE_TYPE_TEMPLATE = "$Palette Template"; //$NON-NLS-1$

    /**
     * Creates a new PaletteTemplateEntry with the given template.
     * 
     * @param label
     *            the entry's name
     * @param shortDesc
     *            the entry's description
     * @param template
     *            the template for this entry
     * @param iconSmall
     *            an ImageDescriptor for the entry's small icon
     * @param iconLarge
     *            an ImageDescriptor for the entry's large icon
     * @see PaletteEntry#PaletteEntry(String, String, ImageDescriptor,
     *      ImageDescriptor, Object)
     */
    public PaletteTemplateEntry(String label, String shortDesc,
            Object template, ImageDescriptor iconSmall,
            ImageDescriptor iconLarge) {
        super(label, shortDesc, iconSmall, iconLarge, PALETTE_TYPE_TEMPLATE);
        setTemplate(template);
    }

    /**
     * @return the user-defined template object
     */
    public Object getTemplate() {
        return template;
    }

    /**
     * Sets the template object to the given value
     * 
     * @param template
     *            the template object
     */
    public void setTemplate(Object template) {
        this.template = template;
    }

}
