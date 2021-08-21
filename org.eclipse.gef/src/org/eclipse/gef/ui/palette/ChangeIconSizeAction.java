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

/**
 * This action toggles the "Use Large Icons" option for the current layout mode
 * of the palette.
 * 
 * @author Pratik Shah
 */
public class ChangeIconSizeAction extends Action {

    private PaletteViewerPreferences prefs;

    /**
     * Constructor
     * 
     * @param prefs
     *            The <code>PaletteViewerPreferences</code> object that this
     *            action is manipulating
     */
    public ChangeIconSizeAction(PaletteViewerPreferences prefs) {
        super(PaletteMessages.SETTINGS_USE_LARGE_ICONS_LABEL_CAPS);
        this.prefs = prefs;
        setChecked(prefs.useLargeIcons());
    }

    /**
     * Toggles the "Use Large Icons" option for the current layout mode.
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        prefs.setCurrentUseLargeIcons(!prefs.useLargeIcons());
    }

}
