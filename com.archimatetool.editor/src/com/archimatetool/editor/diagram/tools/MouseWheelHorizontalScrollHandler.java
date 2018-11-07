/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.widgets.Event;

/**
 * A MouseWheelHandler that handles scrolling left and right when user holds
 * Shift key
 * 
 * Inspired by https://www.eclipse.org/forums/index.php/t/76593/
 * 
 * @author Phillip Beauvoir
 */
public final class MouseWheelHorizontalScrollHandler implements MouseWheelHandler {
    
    final int DELTA = 30;

	/**
	 * The Singleton
	 */
	public static final MouseWheelHandler SINGLETON = new MouseWheelHorizontalScrollHandler();

	private MouseWheelHorizontalScrollHandler() {
	}

	/**
	 * Zooms the given viewer.
	 * 
	 * @see MouseWheelHandler#handleMouseWheel(Event, EditPartViewer)
	 */
	@Override
    public void handleMouseWheel(Event event, EditPartViewer viewer) {
        ZoomManager zoomMgr = (ZoomManager)viewer.getProperty(ZoomManager.class.toString());
        if(zoomMgr != null) {
            Viewport viewport = zoomMgr.getViewport();
            if(viewport != null) {
                viewport.setViewLocation(viewport.getViewLocation().translate(DELTA * event.count, 0));
                event.doit = false;
            }
        }
	}

}
