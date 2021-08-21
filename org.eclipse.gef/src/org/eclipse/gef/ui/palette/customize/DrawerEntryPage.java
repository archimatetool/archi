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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * The default entry page for drawers.
 * 
 * @author Pratik Shah
 */
public class DrawerEntryPage extends DefaultEntryPage {

    private Button openDrawerOption, pinDrawerOption;

    private boolean contains(Object[] array, Object obj) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (obj == array[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.EntryPage#createControl(Composite,
     *      PaletteEntry)
     */
    @Override
    public void createControl(Composite parent, PaletteEntry entry) {
        super.createControl(parent, entry);

        openDrawerOption = createOpenDrawerInitiallyOption(getComposite());
        pinDrawerOption = createPinDrawerInitiallyOption(getComposite());

        Control[] oldTablist = getComposite().getTabList();
        if (!contains(oldTablist, openDrawerOption)) {
            // This means that the super class must've set a specific tab order
            // on this
            // composite. We need to add the two newly created controls to this
            // tab order.
            Control[] newTablist = new Control[oldTablist.length + 2];
            System.arraycopy(oldTablist, 0, newTablist, 0, oldTablist.length);
            newTablist[newTablist.length - 2] = openDrawerOption;
            newTablist[newTablist.length - 1] = pinDrawerOption;
            getComposite().setTabList(newTablist);
        }
    }

    /**
     * Creates the button that provides the option to pin a drawer open at
     * start-up.
     * 
     * @param panel
     *            The parent Composite
     * @return The button for the new option
     */
    protected Button createOpenDrawerInitiallyOption(Composite panel) {
        Button b = new Button(panel, SWT.CHECK);
        b.setFont(panel.getFont());
        b.setText(PaletteMessages.EXPAND_DRAWER_AT_STARTUP_LABEL);
        b.setSelection(getDrawer().isInitiallyOpen());
        if (getPermission() >= PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            b.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    handleOpenSelected(((Button) e.getSource()).getSelection());
                }
            });
        } else {
            b.setEnabled(false);
        }

        return b;
    }

    /**
     * Creates the button that provides the option to have a drawer open at
     * start-up.
     * 
     * @param panel
     *            The parent Composite
     * @return The button for the new option
     */
    protected Button createPinDrawerInitiallyOption(Composite panel) {
        Button pinOption = new Button(panel, SWT.CHECK);
        pinOption.setFont(panel.getFont());
        pinOption.setText(PaletteMessages.DRAWER_PIN_AT_STARTUP);
        GridData data = new GridData();
        data.horizontalIndent = 15;
        pinOption.setLayoutData(data);
        pinOption.setEnabled(openDrawerOption.getSelection()
                && openDrawerOption.isEnabled());
        pinOption.setSelection(getDrawer().isInitiallyPinned());
        if (getPermission() >= PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            pinOption.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    handlePinSelected(((Button) e.getSource()).getSelection());
                }
            });
        }
        return pinOption;
    }

    private Composite getComposite() {
        return (Composite) getControl();
    }

    /**
     * Convenience method that provides access to the PaletteDrawer.
     * 
     * @return the entry as a PaletteDrawer
     */
    protected PaletteDrawer getDrawer() {
        return (PaletteDrawer) getEntry();
    }

    /**
     * Returns the checkbox button which controls whether the drawer is
     * initially open.
     * 
     * @return the checkbox button which controls the initially open setting.
     */
    protected Button getOpenDrawerInitiallyButton() {
        return openDrawerOption;
    }

    /**
     * Returns the checkbox button which controls whether the drawer is
     * initially pinned.
     * 
     * @return the checkbox button which controls the initially pinned setting.
     */
    protected Button getPinDrawerInitiallyButton() {
        return pinDrawerOption;
    }

    /**
     * This method is invoked when the selection state of the option to open
     * drawer at start-up is toggled.
     * <p>
     * It sets the initial state of the drawer accordingly.
     * 
     * @param selection
     *            <code>true</code> if that option is now selected
     */
    protected void handleOpenSelected(boolean selection) {
        int status = selection ? PaletteDrawer.INITIAL_STATE_OPEN
                : PaletteDrawer.INITIAL_STATE_CLOSED;
        getDrawer().setInitialState(status);
        pinDrawerOption.setEnabled(selection);
        if (!selection) {
            pinDrawerOption.setSelection(false);
        }
    }

    /**
     * This method is invoked when the selection state of the option to pin a
     * drawer open at start-up is toggled.
     * <p>
     * It sets the initial state of the drawer accordingly.
     * 
     * @param selection
     *            <code>true</code> if that option is now selected
     */
    protected void handlePinSelected(boolean selection) {
        int status = selection ? PaletteDrawer.INITIAL_STATE_PINNED_OPEN
                : PaletteDrawer.INITIAL_STATE_OPEN;
        getDrawer().setInitialState(status);
    }

}
