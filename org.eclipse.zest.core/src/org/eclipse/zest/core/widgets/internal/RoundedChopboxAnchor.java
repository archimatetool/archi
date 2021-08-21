/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A slight modification to the ChopboxAnchor class to account for the rounded corners.
 * 
 * @author Chris Callendar
 */
public class RoundedChopboxAnchor extends ChopboxAnchor {

    private int arcRadius = 10;
    private int shift = 7;

    public RoundedChopboxAnchor(int arcRadius) {
        super();
        this.arcRadius = arcRadius;
        this.shift = arcRadius - (int) (0.707 * arcRadius);
    }

    public RoundedChopboxAnchor(IFigure owner, int arcRadius) {
        super(owner);
        this.arcRadius = arcRadius;
        this.shift = arcRadius - (int) (0.707 * arcRadius);
    }

    /**
     * Modifies the point slightly for the rounded corners.
     * @return Point
     */
    @Override
    public Point getLocation(Point reference) {
        Point p = super.getLocation(reference);
        Rectangle bounds = getBox();

        boolean done = getTranslatedPoint(bounds.getTopLeft(), p, shift, shift);
        if (!done)
            done = getTranslatedPoint(bounds.getTopRight(), p, -shift, shift);
        if (!done)
            done = getTranslatedPoint(bounds.getBottomLeft(), p, shift, -shift);
        if (!done)
            done = getTranslatedPoint(bounds.getBottomRight(), p, -shift, -shift);
        return p;
    }

    /**
     * Calculates the distance from the corner to the Point p.  
     * If it is less than the minimum then it translates it and returns the new Point.
     * @param corner    The corner Point.
     * @param p            The point to translate if close to the corner.
     * @param dx        The amount to translate in the x direcion.
     * @param dy        The amount to translate in the y direcion.
     * @return boolean     If the translation occured.
     */
    private boolean getTranslatedPoint(Point corner, Point p, int dx, int dy) {
        int diff = (int) corner.getDistance(p);
        if (diff < arcRadius) {
            Point t = corner.getTranslated(dx, dy);
            p.setLocation(t.x, t.y);
            return true;
        }
        return false;
    }

}
