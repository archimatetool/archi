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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.gef.ui.palette.customize.PaletteSettingsDialog;

/**
 * An action to launch the Settings dialog for the given palette.
 * 
 * @author Pratik Shah
 */
public class SettingsAction extends Action {

    private PaletteViewer paletteViewer;

    /**
     * Constructor
     * 
     * @param palette
     *            The Palette which has to be customized when this action is run
     */
    public SettingsAction(PaletteViewer palette) {
        super();
        setText(PaletteMessages.MENU_OPEN_SETTINGS_DIALOG);
        paletteViewer = palette;
    }

    /**
     * Opens the Settings dialog
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        Dialog settings = new PaletteSettingsDialog(paletteViewer.getControl()
                .getShell(), paletteViewer.getPaletteViewerPreferences());
        settings.open();
    }

}
