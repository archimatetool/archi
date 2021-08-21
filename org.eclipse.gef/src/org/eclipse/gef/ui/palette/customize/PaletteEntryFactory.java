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
package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;

/**
 * A PaletteEntryFactory creates certain type of
 * {@link org.eclipse.gef.palette.PaletteEntry PaletteEntries}.
 * 
 * <p>
 * This class only creates {@link org.eclipse.gef.palette.PaletteEntry
 * PaletteEntries} in {@link org.eclipse.gef.palette.PaletteContainer
 * PaletteContainers} and not directly in the
 * {@link org.eclipse.gef.palette.PaletteRoot root}. It is recommended that
 * sub-classes not do that either.
 * </p>
 * 
 * <p>
 * To keep palette customization consistent across different types of editors,
 * it is recommended that a new entry be created after the currently selected
 * entry. If the new entry needs to be created inside the currently selected
 * entry (i.e., if the currently selected entry is a
 * <code>PaletteContainer</code>), it should be added as the last child. Look at
 * {@link #determineContainerForNewEntry(PaletteEntry)} and
 * {@link #determineIndexForNewEntry(PaletteContainer, PaletteEntry)} in this
 * class and
 * {@link org.eclipse.gef.ui.palette.customize.PaletteContainerFactory} to see
 * what the general guidelines are for creating containers and leaf entries.
 * </p>
 * 
 * @author Pratik Shah
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer
 * @see org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog
 */
public abstract class PaletteEntryFactory {

    private String label;
    private ImageDescriptor imageDescriptor;

    /**
     * This method is called when a new palette entry of the type supported by
     * this <code>PaletteEntryFactory</code> is to be created.
     * 
     * @param shell
     *            The <code>Shell</code> of the
     *            <code>PaletteCustomizerDialog</code>
     * @param selected
     *            The <code>PaletteEntry</code> that was selected in the outline
     *            when this action was launched. Will never be <code>null</code>
     *            .
     * @return PaletteEntry The newly created <code>PaletteEntry</code>
     */
    public PaletteEntry createNewEntry(Shell shell, PaletteEntry selected) {
        PaletteContainer parent = determineContainerForNewEntry(selected);
        int index = determineIndexForNewEntry(parent, selected);
        PaletteEntry entry = createNewEntry(shell);
        parent.add(index, entry);
        return entry;
    }

    /**
     * Create the PaletteEntry
     * 
     * @param shell
     *            The <code>Shell</code> of the
     *            <code>PaletteCustomizerDialog</code>; it can be used to create
     *            another warning or information dialog.
     * @return The newly created entry
     */
    protected abstract PaletteEntry createNewEntry(Shell shell);

    /**
     * This method is called by the <code>PaletteCustomizerDialog</code> to
     * determine whether to enable or disable this action on the toolbar and the
     * context menu.
     * <P>
     * This default implementation allows the creation of a new entry only in
     * <code>PaletteContainer</code>s with the following user permission:
     * <code>PERMISSION_FULL_MODIFICATION</code>
     * 
     * @param selected
     *            The selected <code>PaletteEntry</code> (Will never be
     *            <code>null</code>)
     * @return <code>true</code> if, given the current selection, this
     *         <code>PaletteEntryFactory</code> can create a new
     *         <code>PaletteEntry</code>
     */
    public boolean canCreate(PaletteEntry selected) {
        if (selected instanceof PaletteRoot)
            return false;

        PaletteContainer parent;
        if (selected instanceof PaletteContainer)
            parent = (PaletteContainer) selected;
        else
            parent = selected.getParent();

        return parent.getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION
                && parent.acceptsType(determineTypeForNewEntry(selected));
    }

    /**
     * Given the current selection, this method determines the parent for the
     * new entry to be created.
     * 
     * <p>
     * Sub-classes may override this method.
     * </p>
     * 
     * @param selected
     *            The selected entry
     * @return The parent of the new entry to be created
     */
    protected PaletteContainer determineContainerForNewEntry(
            PaletteEntry selected) {
        if (selected instanceof PaletteContainer)
            return (PaletteContainer) selected;
        return selected.getParent();
    }

    /**
     * Given the current selection, this method determines the type of the new
     * entry to be created.
     * 
     * <p>
     * Sub-classes may override this method.
     * </p>
     * 
     * @param selected
     *            The selected entry
     * @return The type of the new entry to be created
     */
    protected Object determineTypeForNewEntry(PaletteEntry selected) {
        return PaletteEntry.PALETTE_TYPE_UNKNOWN;
    }

    /**
     * Calculates the index at which the new entry is to be created, given the
     * current selection.
     * 
     * <p>
     * Sub-classes may override this method.
     * </p>
     * 
     * @param c
     *            The parent container
     * @param selected
     *            The selected entry
     * @return the index at which the new entry should be added in the given
     *         container (-1 indicates add at the end)
     */
    protected int determineIndexForNewEntry(PaletteContainer c,
            PaletteEntry selected) {
        return c.getChildren().indexOf(selected) + 1;
    }

    /**
     * @return <code>ImageDescriptor</code> used to create the image to
     *         represent this factory
     */
    public ImageDescriptor getImageDescriptor() {
        return imageDescriptor;
    }

    /**
     * @return This factory's name
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the <code>ImageDescriptor</code> used to create the image to
     * represent this factory
     * 
     * @param imgDesc
     *            The new ImageDescriptor
     */
    public void setImageDescriptor(ImageDescriptor imgDesc) {
        imageDescriptor = imgDesc;
    }

    /**
     * Sets this factory's name. It will be used to list this factory in the
     * toolbar, context menu, etc.
     * 
     * @param newLabel
     *            The new name for this factory
     */
    public void setLabel(String newLabel) {
        label = newLabel;
    }

}
