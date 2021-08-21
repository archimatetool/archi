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

import java.util.List;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog;

/**
 * This action launches the PaletteCustomizerDialog for the given palette.
 * 
 * @author Pratik Shah
 */
@SuppressWarnings("rawtypes")
public class CustomizeAction extends Action {

    private PaletteViewer paletteViewer;

    /**
     * Constructor
     * 
     * @param palette
     *            the palette which has to be customized when this action is run
     */
    public CustomizeAction(PaletteViewer palette) {
        super();
        setText(PaletteMessages.MENU_OPEN_CUSTOMIZE_DIALOG);
        paletteViewer = palette;
    }

    /**
     * Opens the Customizer Dialog for the palette
     * 
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run() {
        PaletteCustomizerDialog dialog = paletteViewer.getCustomizerDialog();
        List list = paletteViewer.getSelectedEditParts();
        if (!list.isEmpty()) {
            PaletteEntry selection = (PaletteEntry) ((EditPart) list.get(0))
                    .getModel();
            if (!(selection instanceof PaletteRoot)) {
                dialog.setDefaultSelection(selection);
            }
        }
        dialog.open();
    }

}
