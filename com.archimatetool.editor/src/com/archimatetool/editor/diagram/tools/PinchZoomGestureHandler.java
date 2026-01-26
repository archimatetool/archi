/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Handles trackpad pinch-to-zoom gestures.
 *
 * Captures the initial zoom level when a gesture begins, then applies
 * magnification proportionally during GESTURE_MAGNIFY events.
 *
 * @author Yassin Chibrani - Derks
 */
public class PinchZoomGestureHandler implements Listener {

    private final ZoomManager zoomManager;
    private double initialZoom;

    public PinchZoomGestureHandler(ZoomManager zoomManager) {
        this.zoomManager = zoomManager;
    }

    @Override
    public void handleEvent(Event event) {
        switch(event.detail) {
            case SWT.GESTURE_BEGIN -> initialZoom = zoomManager.getZoom();
            case SWT.GESTURE_MAGNIFY -> {
                // event.magnification is cumulative from gesture start (1.0 = no change)
                double newZoom = initialZoom * event.magnification;
                newZoom = Math.clamp(newZoom, zoomManager.getMinZoom(), zoomManager.getMaxZoom());
                zoomManager.setZoom(newZoom);
            }
        }
    }
}
