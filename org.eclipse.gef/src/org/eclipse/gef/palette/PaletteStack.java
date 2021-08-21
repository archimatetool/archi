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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The model object for a PaletteStack - A stack of tools. A stack should
 * contain only tools and should have permissions that are less than or equal to
 * its parent.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
@SuppressWarnings("rawtypes")
public class PaletteStack extends PaletteContainer {

    /**
     * Listens to visibility changes of the children palette entries so that the
     * active entry can be updated if the current active entry is hidden.
     */
    private PropertyChangeListener childListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(PaletteEntry.PROPERTY_VISIBLE)
                    && evt.getNewValue() == Boolean.FALSE
                    && activeEntry == evt.getSource()) {
                checkActiveEntry();
            }
        }
    };

    /** Type identifier **/
    public static final String PALETTE_TYPE_STACK = "$PaletteStack"; //$NON-NLS-1$

    /** Property name for the active entry **/
    public static final String PROPERTY_ACTIVE_ENTRY = "Active Entry"; //$NON-NLS-1$

    private PaletteEntry activeEntry;

    /**
     * Creates a new PaletteStack with the given name, description, and icon.
     * These will be shown only in the customize menu. Any of the given
     * parameter can be <code>null</code>.
     * 
     * @param name
     *            the stack's name
     * @param desc
     *            the stack's description
     * @param icon
     *            an ImageDescriptor for the stack's small icon
     * @see PaletteContainer#PaletteContainer(String, String, ImageDescriptor,
     *      Object)
     */
    public PaletteStack(String name, String desc, ImageDescriptor icon) {
        super(name, desc, icon, PALETTE_TYPE_STACK);
        setUserModificationPermission(PERMISSION_LIMITED_MODIFICATION);
    }

    /**
     * Returns true if this type can be a child of this container Only accepts
     * ToolEntry's.
     * 
     * @param type
     *            the type being requested
     * @return true if this can be a child of this container
     */
    @Override
    public boolean acceptsType(Object type) {
        if (!type.equals(ToolEntry.PALETTE_TYPE_TOOL))
            return false;
        return super.acceptsType(type);
    }

    /**
     * @see org.eclipse.gef.palette.PaletteContainer#add(int,
     *      org.eclipse.gef.palette.PaletteEntry)
     */
    @Override
    public void add(int index, PaletteEntry entry) {
        super.add(index, entry);
        checkActiveEntry();
    }

    /**
     * @see org.eclipse.gef.palette.PaletteContainer#addAll(java.util.List)
     */
    @Override
    public void addAll(List list) {
        super.addAll(list);
        checkActiveEntry();
        updateListeners(list, true);
    }

    /**
     * Checks to make sure the active entry is up-to-date and sets it to the
     * first child if it is <code>null</code>.
     */
    private void checkActiveEntry() {
        PaletteEntry currEntry = activeEntry;
        if (!getChildren().contains(activeEntry))
            activeEntry = null;
        if (activeEntry == null && getChildren().size() > 0)
            activeEntry = (PaletteEntry) getChildren().get(0);
        if (activeEntry != null && !activeEntry.isVisible()) {
            for (Iterator iterator = getChildren().iterator(); iterator
                    .hasNext();) {
                PaletteEntry child = (PaletteEntry) iterator.next();
                if (child.isVisible()) {
                    activeEntry = child;
                    break;
                }
                activeEntry = null;
            }
        }
        listeners.firePropertyChange(PROPERTY_ACTIVE_ENTRY, currEntry,
                activeEntry);
    }

    /**
     * Returns the PaletteEntry referring to the active entry that should be
     * shown in the palette.
     * 
     * @return active entry to be shown in the palette.
     */
    public PaletteEntry getActiveEntry() {
        checkActiveEntry();
        return activeEntry;
    }

    /**
     * @see org.eclipse.gef.palette.PaletteContainer#remove(org.eclipse.gef.palette.PaletteEntry)
     */
    @Override
    public void remove(PaletteEntry entry) {
        super.remove(entry);
        checkActiveEntry();
        updateListeners(Collections.singletonList(entry), false);
    }

    /**
     * Sets the "active" child entry to the given PaletteEntry. This entry will
     * be shown on the palette and will be checked in the menu.
     * 
     * @param entry
     *            the entry to show on the palette.
     */
    public void setActiveEntry(PaletteEntry entry) {
        PaletteEntry oldEntry = activeEntry;
        if (activeEntry != null
                && (activeEntry.equals(entry) || !getChildren().contains(entry)))
            return;
        activeEntry = entry;
        listeners.firePropertyChange(PROPERTY_ACTIVE_ENTRY, oldEntry,
                activeEntry);
    }

    @Override
    public void add(PaletteEntry entry) {
        super.add(entry);
        updateListeners(Collections.singletonList(entry), true);
    }

    @Override
    public void setChildren(List list) {
        updateListeners(getChildren(), false);
        super.setChildren(list);
        updateListeners(getChildren(), true);
        checkActiveEntry();
    }

    /**
     * Either adds or remove the <code>childListener</code> to each palette
     * entry in the collection.
     * 
     * @param entries
     *            a collection of <code>PaletteEntries</code>
     * @param add
     *            true if the lister should be added; false if it should be
     *            removed
     */
    private void updateListeners(Collection entries, boolean add) {
        for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
            PaletteEntry child = (PaletteEntry) iterator.next();
            if (add) {
                child.addPropertyChangeListener(childListener);
            } else {
                child.removePropertyChangeListener(childListener);
            }
        }
    }

}
