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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Root class (statically) for the palette model.
 * 
 * @author Pratik Shah
 */
public class PaletteEntry {

    /**
     * Property name for the entry's small icon
     */
    public static final String PROPERTY_SMALL_ICON = "Small Icon"; //$NON-NLS-1$

    /**
     * Property name for the entry's type
     */
    public static final String PROPERTY_TYPE = "Type"; //$NON-NLS-1$

    /**
     * Property name for the entry's large icon
     */
    public static final String PROPERTY_LARGE_ICON = "Large Icon"; //$NON-NLS-1$

    /**
     * Property name for the entry's label (name)
     */
    public static final String PROPERTY_LABEL = "Name"; //$NON-NLS-1$

    /**
     * Property name for the entry's description
     */
    public static final String PROPERTY_DESCRIPTION = "Description"; //$NON-NLS-1$

    /**
     * Property name for the entry's hidden status
     */
    public static final String PROPERTY_VISIBLE = "Visible"; //$NON-NLS-1$

    /**
     * Property name for the entry's default staus
     */
    public static final String PROPERTY_DEFAULT = "Default"; //$NON-NLS-1$

    /**
     * Property name for the entry's parent
     */
    public static final String PROPERTY_PARENT = "Parent"; //$NON-NLS-1$

    /**
     * Type unknown
     */
    public static final String PALETTE_TYPE_UNKNOWN = "Palette_type_Unknown";//$NON-NLS-1$

    /**
     * No changes can be made to a PaletteEntry with this permission level.
     */
    public static final int PERMISSION_NO_MODIFICATION = 1;

    /**
     * Entries with this permission level can only be hidden/shown.
     */
    public static final int PERMISSION_HIDE_ONLY = 3;

    /**
     * Any property of entries with this level of permission can be changed;
     * however, they cannot be deleted from the palette. The children
     * PaletteContainers with this permission level can be reordered within that
     * container (however, cross-container moving is not allowed).
     * 
     */
    public static final int PERMISSION_LIMITED_MODIFICATION = 7;

    /**
     * All modifications allowed.
     */
    public static final int PERMISSION_FULL_MODIFICATION = 15;

    /**
     * PropertyChangeSupport
     */
    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private PaletteContainer parent;
    private String label, id;
    private String shortDescription;
    private ImageDescriptor iconSmall;
    private ImageDescriptor iconLarge;
    private boolean visible = true;
    private int permission = PERMISSION_FULL_MODIFICATION;
    private Object type = PaletteEntry.PALETTE_TYPE_UNKNOWN;

    /**
     * Constructor
     * <p>
     * Any parameter can be <code>null</code>
     * </p>
     * 
     * @param label
     *            The entry's name
     * @param shortDescription
     *            The entry's description
     */
    public PaletteEntry(String label, String shortDescription) {
        this(label, shortDescription, null, null, null);
    }

    /**
     * Constructor
     * <p>
     * Any parameter can be <code>null</code>
     * </p>
     * 
     * @param label
     *            The entry's name
     * @param shortDescription
     *            The entry's description
     * @param type
     *            The entry's type
     */
    public PaletteEntry(String label, String shortDescription, Object type) {
        this(label, shortDescription, null, null, type);
    }

    /**
     * Constructor
     * <p>
     * Any parameter can be <code>null</code>
     * </p>
     * 
     * @param label
     *            The entry's name
     * @param shortDescription
     *            The entry's description
     * @param iconSmall
     *            The small icon to represent this entry
     * @param iconLarge
     *            The large icon to represent this entry
     */
    public PaletteEntry(String label, String shortDescription,
            ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
        this(label, shortDescription, iconSmall, iconLarge, null);
    }

    /**
     * Constructor
     * <p>
     * Any parameter can be <code>null</code>
     * </p>
     * 
     * @param label
     *            The entry's name
     * @param shortDescription
     *            The entry's description
     * @param iconSmall
     *            The small icon to represent this entry
     * @param iconLarge
     *            The large icon to represent this entry
     * @param type
     *            The entry's type
     */
    public PaletteEntry(String label, String shortDescription,
            ImageDescriptor iconSmall, ImageDescriptor iconLarge, Object type) {
        this(label, shortDescription, iconSmall, iconLarge, type, null);
    }

    /**
     * Constructor
     * <p>
     * Any parameter can be <code>null</code>
     * </p>
     * 
     * @param label
     *            The entry's name
     * @param shortDescription
     *            The entry's description
     * @param smallIcon
     *            The small icon to represent this entry
     * @param largeIcon
     *            The large icon to represent this entry
     * @param type
     *            The entry's type
     * @param id
     *            The entry's id (preferrably unique)
     */
    public PaletteEntry(String label, String shortDescription,
            ImageDescriptor smallIcon, ImageDescriptor largeIcon, Object type,
            String id) {
        setLabel(label);
        setDescription(shortDescription);
        setSmallIcon(smallIcon);
        setLargeIcon(largeIcon);
        setType(type);
        setId(id);
    }

    /**
     * A listener can only be added once. Adding it more than once will do
     * nothing.
     * 
     * @param listener
     *            the PropertyChangeListener that is to be notified of changes
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
        listeners.addPropertyChangeListener(listener);
    }

    /**
     * @return a short desecription describing this entry.
     */
    public String getDescription() {
        return shortDescription;
    }

    /**
     * Returns the id. If no ID has been set (or it is <code>null</code>), an
     * empty String will be returned.
     * 
     * @return String id
     */
    public String getId() {
        if (id == null)
            return ""; //$NON-NLS-1$
        return id;
    }

    /**
     * @return the label for this entry.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return a large icon representing this entry.
     */
    public ImageDescriptor getLargeIcon() {
        return iconLarge;
    }

    /**
     * @return the parent container of this entry
     */
    public PaletteContainer getParent() {
        return parent;
    }

    /**
     * @return a small icon representing the entry.
     */
    public ImageDescriptor getSmallIcon() {
        return iconSmall;
    }

    /**
     * @return the type of this entry. Useful for different interpretations of
     *         the palette model.
     */
    public Object getType() {
        return type;
    }

    /**
     * Returned values are from amongst the following:
     * <UL>
     * <LI>PERMISSION_NO_MODIFICATION</LI>
     * <LI>PERMISSION_HIDE_ONLY</LI>
     * <LI>PERMISSION_LIMITED_MODIFICATION</LI>
     * <LI>PERMISSION_FULL_MODIFICATION</LI>
     * </UL>
     * 
     * @return the permission level for this entry.
     * @see #setUserModificationPermission(int)
     */
    public int getUserModificationPermission() {
        return permission;
    }

    /**
     * @return whether or not this entry is visible. An entry that is not
     *         visible is not shown on the palette.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param listener
     *            the PropertyChangeListener that is not to be notified anymore
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    /**
     * Mutator method for description
     * 
     * @param s
     *            The new description
     */
    public void setDescription(String s) {
        if (s == null && shortDescription == null) {
            return;
        }

        if (s == null || !s.equals(shortDescription)) {
            String oldDescrption = shortDescription;
            shortDescription = s;
            listeners.firePropertyChange(PROPERTY_DESCRIPTION, oldDescrption,
                    shortDescription);
        }
    }

    /**
     * Sets the id. Can be <code>null</code>.
     * 
     * @param id
     *            The new id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Mutator method for label
     * 
     * @param s
     *            The new name
     */
    public void setLabel(String s) {
        if (s == null && label == null) {
            return;
        }

        if (s == null || !s.equals(label)) {
            String oldLabel = label;
            label = s;
            listeners.firePropertyChange(PROPERTY_LABEL, oldLabel, label);
        }
    }

    /**
     * Mutator method for large icon
     * 
     * @param icon
     *            The large icon to represent this entry
     */
    public void setLargeIcon(ImageDescriptor icon) {
        if (icon != iconLarge) {
            ImageDescriptor oldIcon = iconLarge;
            iconLarge = icon;
            listeners.firePropertyChange(PROPERTY_LARGE_ICON, oldIcon,
                    iconLarge);
        }
    }

    /**
     * Sets the parent of this entry
     * 
     * @param newParent
     *            The parent PaletteContainer
     */
    public void setParent(PaletteContainer newParent) {
        if (parent != newParent) {
            PaletteContainer oldParent = parent;
            parent = newParent;
            listeners.firePropertyChange(PROPERTY_PARENT, oldParent, parent);
        }
    }

    /**
     * Mutator method for small icon
     * 
     * @param icon
     *            The new small icon to represent this entry
     */
    public void setSmallIcon(ImageDescriptor icon) {
        if (icon != iconSmall) {
            ImageDescriptor oldIcon = iconSmall;
            iconSmall = icon;
            listeners.firePropertyChange(PROPERTY_SMALL_ICON, oldIcon, icon);
        }
    }

    /**
     * Mutator method for type
     * 
     * @param newType
     *            The new type
     */
    public void setType(Object newType) {
        if (newType == null && type == null) {
            return;
        }

        if (type == null || !type.equals(newType)) {
            Object oldType = type;
            type = newType;
            listeners.firePropertyChange(PROPERTY_TYPE, oldType, type);
        }
    }

    /**
     * Permissions are not checked before making modifications. Clients should
     * check the permission before invoking a modification. Sub-classes may
     * extend the set of permissions. Current set has:
     * <UL>
     * <LI>PERMISSION_NO_MODIFICATION</LI>
     * <LI>PERMISSION_HIDE_ONLY</LI>
     * <LI>PERMISSION_LIMITED_MODIFICATION</LI>
     * <LI>PERMISSION_FULL_MODIFICATION</LI>
     * </UL>
     * Default is <code>PERMISSION_FULL_MODIFICATION</code>
     * 
     * @param permission
     *            One of the above-specified permission levels
     */
    public void setUserModificationPermission(int permission) {
        this.permission = permission;
    }

    /**
     * Makes this entry visible or invisible. An invisible entry does not show
     * up on the palette.
     * 
     * @param newVal
     *            The new boolean indicating whether the entry is visible or not
     */
    public void setVisible(boolean newVal) {
        if (newVal != visible) {
            visible = newVal;
            listeners.firePropertyChange(PROPERTY_VISIBLE, !visible, visible);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Palette Entry (" + (label != null ? label : "") //$NON-NLS-2$//$NON-NLS-1$
                + ")"; //$NON-NLS-1$
    }

}
