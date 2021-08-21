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
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.internal.Internal;

/**
 * This action allows to switch between the various supported layout modes for
 * the given palette.
 * 
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LayoutAction extends Action implements IMenuCreator {

    private PaletteViewerPreferences prefs;
    private List actions;

    /**
     * Constructor
     * 
     * @param prefs
     *            PaletteViewerPreferences object where the settings can be
     *            saved
     */
    public LayoutAction(PaletteViewerPreferences prefs) {
        this(prefs, false);
    }

    /**
     * Constructor
     * 
     * @param hasIcon
     *            True if this action should associate an icon with itself
     * @param prefs
     *            PaletteViewerPreferences object where the settings can be
     *            saved
     */
    public LayoutAction(PaletteViewerPreferences prefs, boolean hasIcon) {
        super(PaletteMessages.LAYOUT_MENU_LABEL);
        this.prefs = prefs;
        actions = createActions();
        setMenuCreator(this);

        if (hasIcon)
            setImageDescriptor(ImageDescriptor.createFromFile(Internal.class,
                    "icons/palette_layout.gif")); //$NON-NLS-1$

        setToolTipText(PaletteMessages.LAYOUT_MENU_LABEL);
    }

    /**
     * Helper method that wraps the given action in an ActionContributionItem
     * and then adds it to the given menu.
     * 
     * @param parent
     *            The menu to which the given action is to be added
     * @param action
     *            The action that is to be added to the given menu
     */
    protected void addActionToMenu(Menu parent, IAction action) {
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

    /**
     * @return A list of actions that can switch to one of the supported layout
     *         modes
     */
    protected List createActions() {
        ArrayList list = new ArrayList();
        int[] modes = prefs.getSupportedLayoutModes();

        Action action;
        for (int i = 0; i < modes.length; i++) {
            switch (modes[i]) {
            case PaletteViewerPreferences.LAYOUT_COLUMNS:
                action = new LayoutChangeAction(
                        PaletteViewerPreferences.LAYOUT_COLUMNS);
                action.setText(PaletteMessages.SETTINGS_COLUMNS_VIEW_LABEL);
                list.add(action);
                break;
            case PaletteViewerPreferences.LAYOUT_LIST:
                action = new LayoutChangeAction(
                        PaletteViewerPreferences.LAYOUT_LIST);
                action.setText(PaletteMessages.SETTINGS_LIST_VIEW_LABEL);
                list.add(action);
                break;
            case PaletteViewerPreferences.LAYOUT_ICONS:
                action = new LayoutChangeAction(
                        PaletteViewerPreferences.LAYOUT_ICONS);
                action.setText(PaletteMessages.SETTINGS_ICONS_VIEW_LABEL_CAPS);
                list.add(action);
                break;
            case PaletteViewerPreferences.LAYOUT_DETAILS:
                action = new LayoutChangeAction(
                        PaletteViewerPreferences.LAYOUT_DETAILS);
                action.setText(PaletteMessages.SETTINGS_DETAILS_VIEW_LABEL);
                list.add(action);
                break;
            }
        }
        return list;
    }

    /**
     * Empty method
     * 
     * @see org.eclipse.jface.action.IMenuCreator#dispose()
     */
    @Override
    public void dispose() {
    }

    private Menu fillMenu(Menu menu) {
        for (Iterator iter = actions.iterator(); iter.hasNext();) {
            LayoutChangeAction action = (LayoutChangeAction) iter.next();
            action.setChecked(prefs.getLayoutSetting() == action
                    .getLayoutSetting());
            addActionToMenu(menu, action);
        }

        setEnabled(!actions.isEmpty());

        return menu;
    }

    /**
     * @see org.eclipse.jface.action.IMenuCreator#getMenu(Control)
     */
    @Override
    public Menu getMenu(Control parent) {
        return fillMenu(new Menu(parent));
    }

    /**
     * @see org.eclipse.jface.action.IMenuCreator#getMenu(Menu)
     */
    @Override
    public Menu getMenu(Menu parent) {
        return fillMenu(new Menu(parent));
    }

    private class LayoutChangeAction extends Action {
        private int value;

        public LayoutChangeAction(int layoutSetting) {
            value = layoutSetting;
        }

        public int getLayoutSetting() {
            return value;
        }

        @Override
        public void run() {
            prefs.setLayoutSetting(value);
        }
    }

}
