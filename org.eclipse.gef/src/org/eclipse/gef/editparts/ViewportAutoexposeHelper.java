/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Intershop, Gunnar Wagenknecht - fix for 44288
 *******************************************************************************/
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.GraphicalEditPart;

/**
 * An implementation of {@link org.eclipse.gef.AutoexposeHelper} that performs
 * autoscrolling of a <code>Viewport</code> figure. This helper is for use with
 * graphical editparts that contain a viewport figure. This helper will search
 * the editpart and find the viewport. Autoscroll will occur when the detect
 * location is inside the viewport's bounds, but near its edge. It will continue
 * for as long as the location continues to meet these criteria. The autoscroll
 * direction is approximated to the nearest orthogonal or diagonal direction
 * (north, northeast, east, etc.).
 * 
 * @author hudsonr
 */
public class ViewportAutoexposeHelper extends ViewportHelper implements
        AutoexposeHelper {

    /** defines the range where autoscroll is active inside a viewer */
    private static final Insets DEFAULT_EXPOSE_THRESHOLD = new Insets(36);

    /** the last time an auto expose was performed */
    private long lastStepTime = 0;

    /** The insets for this helper. */
    private Insets threshold;

    private boolean continueOutside = false;
    
    /**
     * Constructs a new helper on the given GraphicalEditPart. The editpart must
     * have a <code>Viewport</code> somewhere between its <i>contentsPane</i>
     * and its <i>figure</i> inclusively.
     * 
     * @param owner
     *            the GraphicalEditPart that owns the Viewport
     */
    public ViewportAutoexposeHelper(GraphicalEditPart owner) {
        super(owner);
        threshold = DEFAULT_EXPOSE_THRESHOLD;
    }

    /**
     * Constructs a new helper on the given GraphicalEditPart. The editpart must
     * have a <code>Viewport</code> somewhere between its <i>contentsPane</i>
     * and its <i>figure</i> inclusively.
     * 
     * @param owner
     *            the GraphicalEditPart that owns the Viewport
     * @param threshold
     *            the Expose Threshold to use when determing whether or not a
     *            scroll should occur.
     */
    public ViewportAutoexposeHelper(GraphicalEditPart owner, Insets threshold) {
        super(owner);
        this.threshold = threshold;
    }

    /**
     * Constructs a new helper on the given GraphicalEditPart. The editpart must
     * have a <code>Viewport</code> somewhere between its <i>contentsPane</i>
     * and its <i>figure</i> inclusively.
     * 
     * @param owner
     *            the GraphicalEditPart that owns the Viewport
     * @param threshold
     *            the Expose Threshold to use when determing whether or not a
     *            scroll should occur.
     * @param continueOutside
     *            if true sceolling will continue outside the viewport
     */
    public ViewportAutoexposeHelper(GraphicalEditPart owner, Insets threshold, boolean continueOutside) {
        this(owner, threshold);
        this.continueOutside = continueOutside;
    }

    /**
     * Returns <code>true</code> if the given point is inside the viewport, but
     * near its edge.
     * 
     * @see org.eclipse.gef.AutoexposeHelper#detect(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public boolean detect(Point where) {
        lastStepTime = 0;
        Viewport port = findViewport(owner);
        Rectangle rect = Rectangle.SINGLETON;
        port.getClientArea(rect);
        port.translateToParent(rect);
        port.translateToAbsolute(rect);
        return rect.contains(where) && !rect.shrink(threshold).contains(where);
    }

    /**
     * Returns <code>true</code> if the given point is outside the viewport or
     * near its edge. Scrolls the viewport by a calculated (time based) amount
     * in the current direction.
     * 
     * todo: investigate if we should allow auto expose when the pointer is
     * outside the viewport
     * 
     * Phillipus added option to allow auto expose when the pointer is outside!
     * 
     * @see org.eclipse.gef.AutoexposeHelper#step(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public boolean step(Point where) {
        Viewport port = findViewport(owner);

        Rectangle rect = Rectangle.SINGLETON;
        port.getClientArea(rect);
        port.translateToParent(rect);
        port.translateToAbsolute(rect);
        if (!(continueOutside || rect.contains(where) ) || rect.shrink(threshold).contains(where))
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ViewportAutoexposeHelper for: " + owner; //$NON-NLS-1$
    }

}
