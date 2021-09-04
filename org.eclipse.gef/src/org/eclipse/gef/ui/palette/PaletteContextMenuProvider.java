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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.editparts.IPinnableEditPart;

/**
 * Provides the context menu for a palette.
 * 
 * @author Pratik Shah
 */
public class PaletteContextMenuProvider extends ContextMenuProvider {

    /**
     * Constructor
     * 
     * @param palette
     *            the palette viewer for which the context menu has to be
     *            created
     */
    public PaletteContextMenuProvider(PaletteViewer palette) {
        super(palette);
    }

    /**
     * @return the palette viewer
     */
    protected PaletteViewer getPaletteViewer() {
        return (PaletteViewer) getViewer();
    }

    /**
     * This is the method that builds the context menu.
     * 
     * @param menu
     *            The IMenuManager to which actions for the palette's context
     *            menu can be added.
     * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void buildContextMenu(IMenuManager menu) {
        GEFActionConstants.addStandardActionGroups(menu);

        EditPart selectedPart = (EditPart) getPaletteViewer()
                .getSelectedEditParts().get(0);
        IPinnableEditPart pinnablePart = selectedPart
                .getAdapter(IPinnableEditPart.class);
        if (pinnablePart != null && pinnablePart.canBePinned()) {
            menu.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS,
                    new PinDrawerAction(pinnablePart));
        }
        menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new LayoutAction(
                getPaletteViewer().getPaletteViewerPreferences()));
        
        // Added by Phillipus - we don't use large icons
        //menu.appendToGroup(GEFActionConstants.GROUP_VIEW,
        //        new ChangeIconSizeAction(getPaletteViewer()
        //                .getPaletteViewerPreferences()));
        
        if (getPaletteViewer().getCustomizer() != null) {
            menu.appendToGroup(GEFActionConstants.GROUP_REST,
                    new CustomizeAction(getPaletteViewer()));
        }
        menu.appendToGroup(GEFActionConstants.GROUP_REST, new SettingsAction(
                getPaletteViewer()));
    }

}
