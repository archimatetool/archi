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

/**
 * A combination of a {@link PaletteTemplateEntry} and {@link ToolEntry}. The
 * entry will be rendered as a ToolEntry, but it will also be possible to use
 * the entry as a DragSource in the same way as a template.
 * 
 * @author hudsonr
 */
public class CombinedTemplateCreationEntry extends CreationToolEntry {

    private Object template;

    /**
     * Constructs an entry with the given creation factory and template. The
     * creation factory is used by the creation tool when the entry is selected.
     * The template is used with the
     * {@link org.eclipse.gef.dnd.TemplateTransferDragSourceListener}.
     * 
     * @since 3.2
     * @param label
     *            the label
     * @param shortDesc
     *            the descriptoin
     * @param template
     *            the template object
     * @param factory
     *            the CreationFactory
     * @param iconSmall
     *            the small icon
     * @param iconLarge
     *            the large icon
     */
    public CombinedTemplateCreationEntry(String label, String shortDesc,
            Object template, CreationFactory factory,
            ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
        super(label, shortDesc, factory, iconSmall, iconLarge);
        setTemplate(template);
    }

    /**
     * Constructs an entry with the given creation factory. The creation factory
     * is also used as the template object.
     * 
     * @param label
     *            the label
     * @param shortDesc
     *            the description
     * @param factory
     *            the creation factory and template
     * @param iconSmall
     *            the small icon
     * @param iconLarge
     *            the large icon
     * @since 3.2
     */
    public CombinedTemplateCreationEntry(String label, String shortDesc,
            CreationFactory factory, ImageDescriptor iconSmall,
            ImageDescriptor iconLarge) {
        this(label, shortDesc, factory, factory, iconSmall, iconLarge);
    }

    /**
     * Returns the template object.
     * 
     * @return Object the template
     */
    public Object getTemplate() {
        return template;
    }

    /**
     * Sets the template.
     * 
     * @param template
     *            The template
     */
    public void setTemplate(Object template) {
        this.template = template;
    }

}
