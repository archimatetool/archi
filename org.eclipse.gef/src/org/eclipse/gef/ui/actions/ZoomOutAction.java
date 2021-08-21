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

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * @author danlee
 */
public class ZoomOutAction extends ZoomAction {

    /**
     * Constructor for ZoomOutAction.
     * 
     * @param zoomManager
     *            the zoom manager
     */
    public ZoomOutAction(ZoomManager zoomManager) {
        super(GEFMessages.ZoomOut_Label, InternalImages.DESC_ZOOM_OUT,
                zoomManager);
        setId(GEFActionConstants.ZOOM_OUT);
        setToolTipText(GEFMessages.ZoomOut_Tooltip);
        setActionDefinitionId(GEFActionConstants.ZOOM_OUT);
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run() {
        zoomManager.zoomOut();
    }

    /**
     * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
     */
    @Override
    public void zoomChanged(double zoom) {
        setEnabled(zoomManager.canZoomOut());
    }

}
