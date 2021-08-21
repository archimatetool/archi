/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.zest.core.viewers.internal.ZoomListener;
import org.eclipse.zest.core.viewers.internal.ZoomManager;

/**
 * A contribution item that adds a combo to a toolbar or coolbar, or a list of
 * zooms to a menu. Can only be used for one toolbar, coolbar, or menu.
 * 
 * In order to use this item, let your workbench part implement
 * IZoomableWorkbenchPart. If the workbench part then supplies a viewer that is
 * zoomable, the combo or menu created by this item will be enabled.
 * 
 * @author Del Myers
 * 
 */
//@tag zest.bug.156286-Zooming.fix : create a contribution item that can set zooming on Zest views.
public class ZoomContributionViewItem extends ContributionItem implements ZoomListener {
    /**
     * Zooms to fit the width.
     */
    public static final String FIT_WIDTH = ZoomManager.FIT_WIDTH;
    /**
     * Zooms to fit the height.
     */
    public static final String FIT_HEIGHT = ZoomManager.FIT_HEIGHT;
    /**
     * Zooms to fit entirely within the viewport.
     */
    public static final String FIT_ALL = ZoomManager.FIT_ALL;

    private String[] zoomLevels;
    private ZoomManager zoomManager;
    private Combo combo;
    private Menu fMenu;
    private MenuAdapter menuAdapter = new MenuAdapter() {
        @Override
        public void menuShown(MenuEvent e) {
            refresh(true);
        }
    };

    /**
     * Creates a new contribution item that will work on the given part
     * service.initialZooms will be used to populate the combo or the menu.
     * Valid values for initialZooms are percentage numbers (e.g., "100%"), or
     * FIT_WIDTH, FIT_HEIGHT, FIT_ALL.
     * 
     * @param partService
     *            service used to see whether the view is zoomable.
     */
    public ZoomContributionViewItem(IZoomableWorkbenchPart part) {
        zoomManager = part.getZoomableViewer().getZoomManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu,
     *      int)
     */
    @Override
    public void fill(Menu menu, int index) {
        if (this.fMenu == null || this.fMenu != menu) {
            if (this.fMenu != null) {
                this.fMenu.removeMenuListener(menuAdapter);
                this.fMenu = null;
            }
            this.fMenu = menu;
            menu.addMenuListener(menuAdapter);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.CoolBar,
     *      int)
     */
    @Override
    public void fill(CoolBar parent, int index) {
        CoolItem item = new CoolItem(parent, SWT.DROP_DOWN);
        Combo combo = createCombo(parent);
        item.setControl(combo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.ToolBar,
     *      int)
     */
    @Override
    public void fill(ToolBar parent, int index) {
        ToolItem item = new ToolItem(parent, SWT.SEPARATOR, index);
        Combo combo = createCombo(parent);
        item.setControl(combo);
        item.setWidth(combo.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
    }

    private Combo createCombo(Composite parent) {
        this.combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        if (zoomLevels == null) {
            zoomLevels = zoomManager.getZoomLevelsAsText();
        }
        this.combo.setItems(zoomLevels);
        this.combo.addSelectionListener(new SelectionAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                int selection = combo.getSelectionIndex();
                if (selection > 0) {
                    doZoom(combo.getItem(selection));
                } else {
                    doZoom(combo.getItem(0));
                }
            }
        });
        this.combo.pack();
        return this.combo;
    }

    private void doZoom(String zoom) {
        if (zoomManager != null) {
            zoomManager.setZoomAsText(zoom);
        }
    }

    private void refresh(boolean rebuild) {
        //
        if (combo != null && !combo.isDisposed()) {
            refreshCombo(rebuild);
        } else if (fMenu != null && !fMenu.isDisposed()) {
            refreshMenu(rebuild);
        }
    }

    /**
     * @param rebuild
     */
    private void refreshMenu(boolean rebuild) {
        fMenu.setEnabled(false);
        if (zoomManager == null) {
            return;
        }
        if (rebuild) {
            zoomLevels = zoomManager.getZoomLevelsAsText();
            MenuItem[] oldItems = fMenu.getItems();
            for (int i = 0; i < oldItems.length; i++) {
                if (oldItems[i].getData() == this) {
                    oldItems[i].dispose();
                }
            }
            for (int i = 0; i < zoomLevels.length; i++) {
                MenuItem item = new MenuItem(fMenu, SWT.RADIO);
                item.setText(zoomLevels[i]);
                item.setData(this);
                item.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        MenuItem source = (MenuItem) e.getSource();
                        doZoom(source.getText());
                    }
                });
            }
        }
        String zoom = zoomManager.getZoomAsText();
        MenuItem[] items = fMenu.getItems();
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item.getData() == this) {
                item.setSelection(false);
                if (zoom.equalsIgnoreCase(item.getText())) {
                    item.setSelection(true);
                }
            }
        }
        fMenu.setEnabled(true);
    }

    /**
     * @param rebuild
     */
    private void refreshCombo(boolean rebuild) {
        combo.setEnabled(false);
        if (zoomManager == null) {
            return;
        }
        if (rebuild) {
            combo.setItems(zoomManager.getZoomLevelsAsText());
        }
        String zoom = zoomManager.getZoomAsText();
        int index = combo.indexOf(zoom);
        if (index > 0) {
            combo.select(index);
        }
        combo.setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
     */
    @Override
    public void zoomChanged(double z) {
        refresh(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.ContributionItem#dispose()
     */

    @Override
    public void dispose() {
        if (combo != null) {
            combo = null;
        }
        if (fMenu != null) {
            fMenu = null;
        }
        //        @tag zest.bug.159667-ZoomDispose : make sure that we no longer listen to the part service.
        super.dispose();
    }
}
