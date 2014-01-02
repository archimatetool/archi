/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;

/**
 * Extended ViewportAutoexposeHelper allows auto expose if mouse goies outside the Viewport
 * 
 * @author Phillip Beauvoir
 */
public class ExtendedViewportAutoexposeHelper extends ViewportAutoexposeHelper {
    
    private Insets threshold;
    
    /** the last time an auto expose was performed */
    private long lastStepTime = 0;

    private boolean continueIfOutside;
    
	public ExtendedViewportAutoexposeHelper(GraphicalEditPart owner, Insets threshold, boolean continueIfOutside) {
        super(owner, threshold);
        this.threshold = threshold;
        this.continueIfOutside = continueIfOutside;
    }

	@Override
    public boolean step(Point where) {
		Viewport port = findViewport(owner);

		Rectangle rect = Rectangle.SINGLETON;
		port.getClientArea(rect);
		port.translateToParent(rect);
		port.translateToAbsolute(rect);
		if (!(continueIfOutside || rect.contains(where) ) || rect.shrink(threshold).contains(where))
			return false;
		
		// set scroll offset (speed factor)
        int scrollOffset = 0;

        // calculate time based scroll offset
        if (lastStepTime == 0)
            lastStepTime = System.currentTimeMillis();

        long difference = System.currentTimeMillis() - lastStepTime;

        if (difference > 0) {
            scrollOffset = ((int) difference / 3);
            lastStepTime = System.currentTimeMillis();
        }

        if (scrollOffset == 0)
            return true;

        rect.shrink(threshold);

        int region = rect.getPosition(where);
        Point loc = port.getViewLocation();

        if ((region & PositionConstants.SOUTH) != 0)
            loc.y += scrollOffset;
        else if ((region & PositionConstants.NORTH) != 0)
            loc.y -= scrollOffset;

        if ((region & PositionConstants.EAST) != 0)
            loc.x += scrollOffset;
        else if ((region & PositionConstants.WEST) != 0)
            loc.x -= scrollOffset;

        port.setViewLocation(loc);
        return true;

	}
}
