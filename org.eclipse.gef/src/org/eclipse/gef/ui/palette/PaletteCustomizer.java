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
package org.eclipse.gef.ui.palette;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.customize.DefaultEntryPage;
import org.eclipse.gef.ui.palette.customize.DrawerEntryPage;
import org.eclipse.gef.ui.palette.customize.EntryPage;
import org.eclipse.gef.ui.palette.customize.PaletteDrawerFactory;
import org.eclipse.gef.ui.palette.customize.PaletteSeparatorFactory;
import org.eclipse.gef.ui.palette.customize.PaletteStackFactory;

/**
 * <code>PaletteCustomizer</code> is the <code>PaletteCustomizerDialog</code>'s
 * interface to the model. This class is responsible for propogating to the
 * model changes made in the dialog.
 * 
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class PaletteCustomizer {

    /**
     * Return true if this container can accept this entry as a new child. By
     * default, this method checks to see first if the container has full
     * permissions, then checks to see if this container can accept the type of
     * the entry.
     * 
     * @param container
     *            the container that will be the parent of this entry
     * @param entry
     *            the entry to add to the container
     * @return true if this container can hold this entry
     */
    protected boolean canAdd(PaletteContainer container, PaletteEntry entry) {
        return container.getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION
                && container.acceptsType(entry.getType());
    }

    /**
     * Indicates whether the given entry can be deleted from the model or not.
     * Whether or not an entry can be deleted depends on its permsission (
     * {@link PaletteEntry#getUserModificationPermission()}).
     * <p>
     * This method will be invoked by <code>PaletteCustomizerDialog</code> to
     * determine whether or not to enable the "Delete" action.
     * </p>
     * 
     * @param entry
     *            The selected palette entry. It'll never be <code>null</code>.
     * @return <code>true</code> if the given entry can be deleted
     * 
     * @see #performDelete(PaletteEntry)
     */
    public boolean canDelete(PaletteEntry entry) {
        return entry.getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION;
    }

    /**
     * Indicates whether the given entry can be moved down or not. Whether or
     * not an entry can be moved down or not is determined by its parent's user
     * modification permission (
     * {@link PaletteEntry#getUserModificationPermission()}).
     * <p>
     * Will be called by PaletteCustomizerDialog to determine whether or not to
     * enable the "Move Down" action.
     * </p>
     * 
     * @param entry
     *            The selected palette entry (it'll never be <code>null</code>)
     * @return <code>true</code> if the given entry can be moved down
     * 
     * @see #performMoveDown(PaletteEntry)
     */
    public boolean canMoveDown(PaletteEntry entry) {
        PaletteContainer parent = entry.getParent();
        int parentPermission = parent.getUserModificationPermission();
        if (parentPermission < PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            return false;
        }

        if (parent.getChildren().indexOf(entry) + 1 != parent.getChildren()
                .size()) {
            return true;
        } else {
            // The given entry is the last child in its parent.
            if (parentPermission != PaletteEntry.PERMISSION_FULL_MODIFICATION
                    || parent.getParent() == null)
                return false;

            // try to place in grand parent
            if (canAdd(parent.getParent(), entry))
                return true;

            // walk parent siblings till we find one it can go into.
            List children = parent.getParent().getChildren();
            int parentIndex = children.indexOf(parent);
            PaletteEntry parentSibling = null;

            for (int i = parentIndex + 1; i < children.size(); i++) {
                parentSibling = (PaletteEntry) children.get(i);
                if (parentSibling instanceof PaletteContainer) {
                    if (canAdd((PaletteContainer) parentSibling, entry))
                        return true;
                }
            }

            return false;

        }
    }

    /**
     * Indicates whether the given entry can be moved up or not. Whether or not
     * an entry can be moved up or not is determined by its parent's user
     * modification permission (
     * {@link PaletteEntry#getUserModificationPermission()}).
     * <p>
     * Will be called by PaletteCustomizerDialog to determine whether or not to
     * enable the "Move Up" action.
     * </p>
     * 
     * @param entry
     *            The selected palette entry (it'll never be <code>null</code>)
     * @return <code>true</code> if the given entry can be moved up
     * 
     * @see #performMoveUp(PaletteEntry)
     */
    public boolean canMoveUp(PaletteEntry entry) {
        PaletteContainer parent = entry.getParent();
        int parentPermission = parent.getUserModificationPermission();
        if (parentPermission < PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            return false;
        }

        if (parent.getChildren().indexOf(entry) != 0) {
            return true;
        } else {
            // The given entry is the first child in its parent
            if (parentPermission != PaletteEntry.PERMISSION_FULL_MODIFICATION
                    || parent.getParent() == null)
                return false;

            // try to place in grand parent
            if (canAdd(parent.getParent(), entry))
                return true;

            // walk parent siblings till we find one it can go into.
            List children = parent.getParent().getChildren();
            int parentIndex = children.indexOf(parent);
            PaletteEntry parentSibling = null;

            for (int i = parentIndex - 1; i >= 0; i--) {
                parentSibling = (PaletteEntry) children.get(i);
                if (parentSibling instanceof PaletteContainer) {
                    if (canAdd((PaletteContainer) parentSibling, entry))
                        return true;
                }
            }

            return false;
        }
    }

    /**
     * Returns the list of PaletteEntryFactories that can be used to create new
     * palette entries. The String returned by the getText() method of each
     * PaletteEntryFactory will be used to populate the "New" drop-down.
     * getImageDescriptor() will be used to set the icons on the drop down. This
     * method can return null if there are no PaletteEntryFactories available.
     * 
     * @return The List of PaletteEntryFactories
     */
    public List getNewEntryFactories() {
        List list = new ArrayList(4);
        list.add(new PaletteSeparatorFactory());
        list.add(new PaletteStackFactory());
        list.add(new PaletteDrawerFactory());
        return list;
    }

    /**
     * Returns an EntryPage that will display the custom properties of the given
     * entry. Can return null if there are no custom properties.
     * 
     * @param entry
     *            The PaletteEntry whose properties page needs to be displayed
     *            (it'll never be <code>null</code>)
     * @return The EntryPage to represent the given entry
     */
    public EntryPage getPropertiesPage(PaletteEntry entry) {
        if (entry instanceof PaletteDrawer) {
            return new DrawerEntryPage();
        }
        return new DefaultEntryPage();
    }

    /**
     * Updates the model by deleting the given entry from it. <br>
     * Called when the "Delete" action in the PaletteCustomizerDialog is
     * executed.
     * 
     * @param entry
     *            The selected palette entry (it'll never be <code>null</code>)
     * 
     * @see #canDelete(PaletteEntry)
     */
    public void performDelete(PaletteEntry entry) {
        entry.getParent().remove(entry);
    }

    /**
     * Updates the model by moving the entry down. <br>
     * Called when the "Move Down" action in the PaletteCustomizerDialog is
     * invoked.
     * 
     * @param entry
     *            The selected palette entry (it'll never be <code>null</code>)
     * 
     * @see #canMoveDown(PaletteEntry)
     */
    public void performMoveDown(PaletteEntry entry) {
        PaletteContainer parent = entry.getParent();
        if (!parent.moveDown(entry)) {
            // This is the case of a PaletteEntry that is its parent's last
            // child
            // and will have to move down into the next slot in the grandparent
            PaletteEntry parentSibling = null;
            PaletteContainer newParent = parent.getParent();
            int insertionIndex = 0;

            if (canAdd(newParent, entry))
                insertionIndex = newParent.getChildren().indexOf(parent) + 1;
            else {
                List parents = newParent.getChildren();

                for (int i = parents.indexOf(parent) + 1; i < parents.size(); i++) {
                    parentSibling = (PaletteEntry) parents.get(i);
                    if (parentSibling instanceof PaletteContainer) {
                        newParent = (PaletteContainer) parentSibling;
                        if (canAdd(newParent, entry))
                            break;
                    }
                }
            }

            parent.remove(entry);

            newParent.add(insertionIndex, entry);
        }
    }

    /**
     * Updates the model by moving the entry up. <br>
     * Called when the "Move Up" action in the PaletteCustomizerDialog is
     * invoked.
     * 
     * @param entry
     *            The selected palette entry (it'll never be <code>null</code>)
     * 
     * @see #canMoveUp(PaletteEntry)
     */
    public void performMoveUp(PaletteEntry entry) {
        PaletteContainer parent = entry.getParent();
        if (!parent.moveUp(entry)) {
            // This is the case of a PaletteEntry that is its parent's first
            // child
            // and we should move up in the grand parent.
            PaletteEntry parentSibling = null;
            PaletteContainer newParent = parent.getParent();
            int insertionIndex = 0;

            if (canAdd(newParent, entry))
                insertionIndex = newParent.getChildren().indexOf(parent);
            else {
                List parents = newParent.getChildren();

                for (int i = parents.indexOf(parent) - 1; i >= 0; i--) {
                    parentSibling = (PaletteEntry) parents.get(i);
                    if (parentSibling instanceof PaletteContainer) {
                        newParent = (PaletteContainer) parentSibling;
                        if (canAdd(newParent, entry)) {
                            insertionIndex = newParent.getChildren().size();
                            break;
                        }
                    }
                }
            }

            parent.remove(entry);

            newParent.add(insertionIndex, entry);
        }
    }

    /**
     * Undoes the changes made to the model since the last save.
     * <p>
     * This method is invoked when the "Cancel" is selected in the
     * <code>PaletteCustomizerDialog</code>.
     * </p>
     */
    public abstract void revertToSaved();

    /**
     * Persists the changes made to the model.
     * <p>
     * Called when "OK" or "Apply" are selected in the
     * <code>PaletteCustomizerDialog</code>.
     * </p>
     */
    public abstract void save();

}
