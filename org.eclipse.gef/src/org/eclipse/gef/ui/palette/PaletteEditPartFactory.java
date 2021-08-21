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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.GroupEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.PaletteStackEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.PinnablePaletteStackEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.SeparatorEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.SliderPaletteEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.TemplateEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolbarEditPart;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

/**
 * Factory to create EditParts for different PaletteEntries.
 * 
 * @author Pratik Shah
 */
public class PaletteEditPartFactory implements EditPartFactory {

    /**
     * Create DrawerEditPart - edit part for PaletteDrawer
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteDrawer
     * @return the newly created EditPart
     */
    protected EditPart createDrawerEditPart(EditPart parentEditPart,
            Object model) {
        return new DrawerEditPart((PaletteDrawer) model);
    }

    /**
     * @see org.eclipse.gef.EditPartFactory#createEditPart(EditPart, Object)
     */
    @Override
    public EditPart createEditPart(EditPart parentEditPart, Object model) {
        if (model instanceof PaletteRoot)
            return createMainPaletteEditPart(parentEditPart, model);
        if (model instanceof PaletteStack)
            return createStackEditPart(parentEditPart, model);
        if (model instanceof PaletteContainer) {
            Object type = ((PaletteContainer) model).getType();
            if (PaletteDrawer.PALETTE_TYPE_DRAWER.equals(type))
                return createDrawerEditPart(parentEditPart, model);
            if (PaletteGroup.PALETTE_TYPE_GROUP.equals(type)
                    || PaletteEntry.PALETTE_TYPE_UNKNOWN.equals(type))
                return createGroupEditPart(parentEditPart, model);
            if (PaletteToolbar.PALETTE_TYPE_TOOLBAR_GROUP.equals(type))
                return createToolbarEditPart(parentEditPart, model);
        }
        if (model instanceof PaletteTemplateEntry)
            return createTemplateEditPart(parentEditPart, model);
        if (model instanceof PaletteSeparator)
            return createSeparatorEditPart(parentEditPart, model);
        if (model instanceof PaletteEntry)
            return createEntryEditPart(parentEditPart, model);
        return null;
    }

    /**
     * Create SeparatorEditPart - edit part for PaletteSeparator
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteSeparator
     * @return the newly created EditPart
     */
    protected EditPart createSeparatorEditPart(EditPart parentEditPart,
            Object model) {
        return new SeparatorEditPart((PaletteSeparator) model);
    }

    /**
     * Create PaletteStackEditPart - edit part for PaletteStack
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteStack
     * @return the newly created EditPart
     */
    protected EditPart createStackEditPart(EditPart parentEditPart, Object model) {
        if (parentEditPart instanceof PaletteEditPart
                && ((PaletteEditPart) parentEditPart).isToolbarItem()) {
            return new PaletteStackEditPart((PaletteStack) model);
        }
        return new PinnablePaletteStackEditPart((PaletteStack) model);
    }

    /**
     * Create ToolEntryEditPart - edit part for ToolEntry
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the ToolEntry
     * @return the newly created EditPart
     */
    protected EditPart createEntryEditPart(EditPart parentEditPart, Object model) {
        return new ToolEntryEditPart((PaletteEntry) model);
    }

    /**
     * Create GroupEditPart - edit part for PaletteGroup
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteGroup
     * @return the newly created EditPart
     */
    protected EditPart createGroupEditPart(EditPart parentEditPart, Object model) {
        return new GroupEditPart((PaletteContainer) model);
    }

    /**
     * Create ToolbarEditPart - edit part for PaletteToolbar
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteToolbar
     * @return the newly created EditPart
     */
    protected EditPart createToolbarEditPart(EditPart parentEditPart,
            Object model) {
        return new ToolbarEditPart((PaletteToolbar) model);
    }

    /**
     * Create SliderPaletteEditPart - edit part for PaletteRoot
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteRoot
     * @return the newly created EditPart
     */
    protected EditPart createMainPaletteEditPart(EditPart parentEditPart,
            Object model) {
        return new SliderPaletteEditPart((PaletteRoot) model);
    }

    /**
     * Create TemplateEditPart - edit part for PaletteTemplateEntry
     * 
     * @param parentEditPart
     *            the parent of the new editpart to be created
     * @param model
     *            the PaletteTemplateEntry
     * @return the newly created EditPart
     */
    protected EditPart createTemplateEditPart(EditPart parentEditPart,
            Object model) {
        return new TemplateEditPart((PaletteTemplateEntry) model);
    }

}
