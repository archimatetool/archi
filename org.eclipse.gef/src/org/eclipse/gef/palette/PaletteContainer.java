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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Default implementation of PaletteContainer
 * 
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PaletteContainer extends PaletteEntry {

    /**
     * Property name indicating that this PaletteContainer's children have
     * changed
     */
    public static final String PROPERTY_CHILDREN = "Children Changed"; //$NON-NLS-1$

    /**
     * This container's contents
     */
    protected List children = new ArrayList();

    /**
     * Constructor
     * <p>
     * Any parameter can be <code>null</code>.
     * </p>
     * 
     * @param label
     *            The container's name
     * @param desc
     *            The container's description
     * @param icon
     *            The small icon to represent this container
     * @param type
     *            The container's type
     */
    protected PaletteContainer(String label, String desc, ImageDescriptor icon,
            Object type) {
        super(label, desc, icon, null, type);
    }

    /**
     * Returns true if this type can be a child of this container.
     * 
     * @param type
     *            the type being requested
     * @return true if this can be a child of this container
     */
    public boolean acceptsType(Object type) {
        return true;
    }

    /**
     * Adds the given entry to the end of this PaletteContainer
     * 
     * @param entry
     *            the PaletteEntry to add
     */
    public void add(PaletteEntry entry) {
        add(-1, entry);
    }

    /**
     * Adds the given PaletteEntry at position <code>index</code>.
     * 
     * @param index
     *            position to add the PaletteEntry
     * @param entry
     *            the PaletteEntry to add
     */
    public void add(int index, PaletteEntry entry) {
        if (!acceptsType(entry.getType()))
            throw new IllegalArgumentException(
                    "This container can not contain this type of child: " //$NON-NLS-1$
                            + entry.getType());

        List oldChildren = new ArrayList(getChildren());

        int actualIndex = index < 0 ? getChildren().size() : index;
        getChildren().add(actualIndex, entry);
        entry.setParent(this);
        listeners.firePropertyChange(PROPERTY_CHILDREN, oldChildren,
                getChildren());
    }

    /**
     * Adds the list of {@link PaletteEntry} objects to this PaletteContainer.
     * 
     * @param list
     *            a list of PaletteEntry objects to add to this PaletteContainer
     */
    public void addAll(List list) {
        ArrayList oldChildren = new ArrayList(getChildren());
        for (int i = 0; i < list.size(); i++) {
            PaletteEntry child = (PaletteEntry) list.get(i);
            if (!acceptsType(child.getType()))
                throw new IllegalArgumentException(
                        "This container can not contain this type of child: " //$NON-NLS-1$
                                + child.getType());
            getChildren().add(child);
            child.setParent(this);
        }
        listeners.firePropertyChange(PROPERTY_CHILDREN, oldChildren,
                getChildren());
    }

    /**
     * Appends the given entry after the entry with the given id, but before the
     * next separator.
     * 
     * @param id
     *            the id of the entry to append after
     * @param entry
     *            the entry to add
     */
    public void appendToSection(String id, PaletteEntry entry) {
        // find the entry with the given id
        boolean found = false;
        for (int i = 0; i < getChildren().size(); i++) {
            PaletteEntry currEntry = (PaletteEntry) getChildren().get(i);
            if (currEntry.getId().equals(id))
                found = true;
            else if (found && currEntry instanceof PaletteSeparator) {
                add(i, entry);
                return;
            }
        }
        if (found)
            add(entry);
        else
            throw new IllegalArgumentException("Section not found: " + id); //$NON-NLS-1$
    }

    /**
     * @return the children of this container
     */
    public List getChildren() {
        return children;
    }

    private boolean move(PaletteEntry entry, boolean up) {
        int index = getChildren().indexOf(entry);
        if (index < 0) {
            // This container does not contain the given palette entry
            return false;
        }
        index = up ? index - 1 : index + 1;
        if (index < 0 || index >= getChildren().size()) {
            // Performing the move operation will give the child an invalid
            // index
            return false;
        }
        if (getChildren().get(index) instanceof PaletteContainer
                && getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION) {
            // move it into a container if we have full permission
            PaletteContainer container = (PaletteContainer) getChildren().get(
                    index);
            if (container.acceptsType(entry.getType())
                    && container.getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION) {
                remove(entry);
                if (up)
                    container.add(entry);
                else
                    container.add(0, entry);
                return true;
            }
        }
        List oldChildren = new ArrayList(getChildren());
        getChildren().remove(entry);
        getChildren().add(index, entry);
        listeners.firePropertyChange(PROPERTY_CHILDREN, oldChildren,
                getChildren());
        return true;
    }

    /**
     * Moves the given entry down, if possible. This method only handles moving
     * the child within this container.
     * 
     * @param entry
     *            The entry to be moved
     * @return <code>true</code> if the given entry was successfully moved down
     */
    public boolean moveDown(PaletteEntry entry) {
        return move(entry, false);
    }

    /**
     * Moves the given entry up, if possible. This method only handles moving
     * the child within this container.
     * 
     * @param entry
     *            The entry to be moved
     * @return <code>true</code> if the given entry was successfully moved up
     */
    public boolean moveUp(PaletteEntry entry) {
        return move(entry, true);
    }

    /**
     * Removes the given PaletteEntry from this PaletteContainer
     * 
     * @param entry
     *            the PaletteEntry to remove
     */
    public void remove(PaletteEntry entry) {
        List oldChildren = new ArrayList(getChildren());
        if (getChildren().remove(entry)) {
            entry.setParent(null);
            listeners.firePropertyChange(PROPERTY_CHILDREN, oldChildren,
                    getChildren());
        }
    }

    /**
     * Sets the children of this PaletteContainer to the given list of
     * {@link PaletteEntry} objects.
     * 
     * @param list
     *            the list of children
     */
    public void setChildren(List list) {
        List oldChildren = children;
        for (int i = 0; i < oldChildren.size(); i++) {
            PaletteEntry entry = (PaletteEntry) oldChildren.get(i);
            entry.setParent(null);
        }
        children = list;
        for (int i = 0; i < children.size(); i++) {
            PaletteEntry entry = (PaletteEntry) children.get(i);
            entry.setParent(this);
        }
        listeners.firePropertyChange(PROPERTY_CHILDREN, oldChildren,
                getChildren());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Palette Container (" //$NON-NLS-1$
                + (getLabel() != null ? getLabel() : "") //$NON-NLS-1$
                + ")"; //$NON-NLS-1$
    }

}
