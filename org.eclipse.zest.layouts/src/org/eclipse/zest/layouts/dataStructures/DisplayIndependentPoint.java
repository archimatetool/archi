/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.dataStructures;

/**
 * This is a point that isn't dependent on awt, swt, or any other library,
 * except layout.
 * 
 * @author Casey Best
 */
public class DisplayIndependentPoint {
    public double x, y;

    @Override
    public boolean equals(Object o) {
        DisplayIndependentPoint that = (DisplayIndependentPoint) o;
        if (this.x == that.x && this.y == that.y)
            return true;
        else
            return false;
    }

    public DisplayIndependentPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DisplayIndependentPoint(DisplayIndependentPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * Create a new point based on the current point but in a new coordinate system
     * @param currentBounds
     * @param targetBounds
     * @return
     */
    public DisplayIndependentPoint convert(DisplayIndependentRectangle currentBounds, DisplayIndependentRectangle targetBounds) {
        double currentWidth = currentBounds.width;
        double currentHeight = currentBounds.height;

        double newX = (currentBounds.width == 0) ? 0 : (x / currentWidth) * targetBounds.width + targetBounds.x;
        double newY = (currentBounds.height == 0) ? 0 : (y / currentHeight) * targetBounds.height + targetBounds.y;
        return new DisplayIndependentPoint(newX, newY);
    }

    /**
     * Converts this point based on the current x, y values to a percentage  
     * of the specified coordinate system
     * @param bounds
     * @return
     */
    public DisplayIndependentPoint convertToPercent(DisplayIndependentRectangle bounds) {
        double newX = (bounds.width == 0) ? 0 : (x - bounds.x) / bounds.width;
        double newY = (bounds.height == 0) ? 0 : (y - bounds.y) / bounds.height;
        return new DisplayIndependentPoint(newX, newY);
    }

    /**
     * Converts this point based on the current x, y values from a percentage  
     * of the specified coordinate system
     * @param bounds
     * @return 
     */
    public DisplayIndependentPoint convertFromPercent(DisplayIndependentRectangle bounds) {
        double newX = bounds.x + x * bounds.width;
        double newY = bounds.y + y * bounds.height;
        return new DisplayIndependentPoint(newX, newY);
    }
}
