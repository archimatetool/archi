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
package org.eclipse.draw2d.geometry;

/**
 * Provides support for transformations of scaling, translation and rotation.
 */
public class Transform {

    private double scaleX = 1.0, scaleY = 1.0, dx, dy, cos = 1.0, sin;

    /**
     * Sets the value for the amount of scaling to be done along both axes.
     * 
     * @param scale
     *            Scale factor
     * @since 2.0
     */
    public void setScale(double scale) {
        scaleX = scaleY = scale;
    }

    /**
     * Sets the value for the amount of scaling to be done along X and Y axes
     * individually.
     * 
     * @param x
     *            Amount of scaling on X axis
     * @param y
     *            Amount of scaling on Y axis
     * @since 2.0
     */
    public void setScale(double x, double y) {
        scaleX = x;
        scaleY = y;
    }

    /**
     * Sets the rotation angle.
     * 
     * @param angle
     *            Angle of rotation
     * @since 2.0
     */
    public void setRotation(double angle) {
        cos = Math.cos(angle);
        sin = Math.sin(angle);
    }

    /**
     * Sets the translation amounts for both axes.
     * 
     * @param x
     *            Amount of shift on X axis
     * @param y
     *            Amount of shift on Y axis
     * @since 2.0
     */
    public void setTranslation(double x, double y) {
        dx = x;
        dy = y;
    }

    /**
     * Returns a new transformed Point of the input Point based on the
     * transformation values set.
     * 
     * @param p
     *            Point being transformed
     * @return The transformed Point
     * @since 2.0
     */
    @SuppressWarnings("deprecation")
    public Point getTransformed(Point p) {
        double x = p.x;
        double y = p.y;
        double temp;
        x *= scaleX;
        y *= scaleY;

        temp = x * cos - y * sin;
        y = x * sin + y * cos;
        x = temp;
        return new Point(Math.round(x + dx), Math.round(y + dy));
    }

}
