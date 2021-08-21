/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.swt.widgets.Event;

import org.eclipse.gef.editparts.ZoomManager;

/**
 * A MouseWheelHandler that zooms the given viewer. Typically, this handler
 * should be registered on a viewer that supports zoom as follows: <br>
 * <code>
 * graphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), 
 *         MouseWheelZoomHandler.SINGLETON);
 * </code>
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public final class MouseWheelZoomHandler implements MouseWheelHandler {

    /**
     * The Singleton
     */
    public static final MouseWheelHandler SINGLETON = new MouseWheelZoomHandler();

    private MouseWheelZoomHandler() {
    }

    /**
     * Zooms the given viewer.
     * 
     * @see MouseWheelHandler#handleMouseWheel(Event, EditPartViewer)
     */
    @Override
    public void handleMouseWheel(Event event, EditPartViewer viewer) {
        ZoomManager zoomMgr = (ZoomManager) viewer
                .getProperty(ZoomManager.class.toString());
        if (zoomMgr != null) {
            if (event.count > 0)
                zoomMgr.zoomIn();
            else
                zoomMgr.zoomOut();
            event.doit = false;
        }
    }

}
