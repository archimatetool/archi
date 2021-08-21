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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Tool;

/**
 * A factory for returning Tools.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ToolEntry extends PaletteEntry {

    /** Type Identifier **/
    public static final Object PALETTE_TYPE_TOOL = "$Palette Tool";//$NON-NLS-1$

    private Map map;
    private Class toolClass;

    /**
     * Creates a new ToolEntry. Any parameter can be <code>null</code>.
     * 
     * @param label
     *            the entry's name
     * @param shortDesc
     *            the entry's description
     * @param iconSmall
     *            the entry's small icon
     * @param iconLarge
     *            the entry's large icon
     */
    public ToolEntry(String label, String shortDesc, ImageDescriptor iconSmall,
            ImageDescriptor iconLarge) {
        this(label, shortDesc, iconSmall, iconLarge, null);
    }

    /**
     * Constructor to create a new ToolEntry. Any parameter can be
     * <code>null</code>.
     * 
     * @param label
     *            the entry's name
     * @param description
     *            the entry's description
     * @param iconSmall
     *            the entry's small icon
     * @param iconLarge
     *            the entry's large icon
     * @param tool
     *            the type of tool that this entry uses
     * @since 3.1
     */
    public ToolEntry(String label, String description,
            ImageDescriptor iconSmall, ImageDescriptor iconLarge, Class tool) {
        super(label, description, iconSmall, iconLarge, PALETTE_TYPE_TOOL);
        setToolClass(tool);
    }

    /**
     * Creates the tool of the type specified by {@link #setToolClass(Class)}
     * for this ToolEntry. The tool is also configured with the properties set
     * in {@link #setToolProperty(Object, Object)}. Sub-classes overriding this
     * method should ensure that their tools are also configured with those
     * properties.
     * 
     * @return the tool for this entry
     */
    @SuppressWarnings("deprecation")
    public Tool createTool() {
        if (toolClass == null)
            return null;
        Tool tool;
        try {
            tool = (Tool) toolClass.newInstance();
        } catch (IllegalAccessException iae) {
            return null;
        } catch (InstantiationException ie) {
            return null;
        }
        tool.setProperties(getToolProperties());
        return tool;
    }

    /**
     * @return the properties set in {@link #setToolProperty(Object, Object)}
     * @since 3.1
     */
    protected Map getToolProperties() {
        return map;
    }

    /**
     * Returns the property value for the specified property key.
     * 
     * @param key
     *            the property key
     * @return the value for the requested property
     * @since 3.1
     */
    public Object getToolProperty(Object key) {
        if (map != null)
            return map.get(key);
        return null;
    }

    /**
     * Sets the type of tool to be created. This provides clients with a method
     * of specifying a different type of tool to be created without having to
     * sub-class. The provided class should have a default constructor for this
     * to work successfully.
     * 
     * @param toolClass
     *            the type of tool to be created by this entry
     * @since 3.1
     */
    public void setToolClass(Class toolClass) {
        if (toolClass != null)
            Assert.isTrue(Tool.class.isAssignableFrom(toolClass));
        this.toolClass = toolClass;
    }

    /**
     * Clients can use this method to configure the associated tool without
     * having to sub-class.
     * 
     * @param key
     *            the property name
     * @param value
     *            a value of type associated with the given property
     * @since 3.1
     * @see Tool#setProperties(Map)
     */
    public void setToolProperty(Object key, Object value) {
        if (map == null)
            map = new HashMap();
        map.put(key, value);
    }

}
