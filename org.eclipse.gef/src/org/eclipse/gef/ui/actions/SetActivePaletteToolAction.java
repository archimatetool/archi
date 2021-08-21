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
package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * This action sets a Tool to be the active entry in the PaletteViewer.
 */
public class SetActivePaletteToolAction extends Action {

    private PaletteViewer viewer;
    private ToolEntry entry;

    /**
     * Creates a new SetActivePaletteToolAction with the given entry to set, as
     * well as a label, icon, and isChecked to be used in a menu.
     * 
     * @param viewer
     *            the PaletteViewer
     * @param label
     *            the label to show in the menu for this entry.
     * @param icon
     *            the icon to show in the menu for this entry.
     * @param isChecked
     *            whether or not this is the current active entry.
     * @param entry
     *            the entry to set if this action is invoked.
     */
    public SetActivePaletteToolAction(PaletteViewer viewer, String label,
            ImageDescriptor icon, boolean isChecked, ToolEntry entry) {
        super(label, icon);
        this.viewer = viewer;
        this.entry = entry;
        setChecked(isChecked);
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run() {
        if (viewer != null) {
            viewer.setActiveTool(entry);

            // The PaletteViewerKeyHandler will not pick up key events when the
            // context menu is open for palette stacks so we need to indicate
            // that the focus needs to change to the tool entry that the user
            // has just selected. See GraphicalViewerKeyHandler.procesSelect().
            EditPart part = (EditPart) viewer.getEditPartRegistry().get(entry);
            viewer.appendSelection(part);
            viewer.setFocus(part);
        }
    }

}
