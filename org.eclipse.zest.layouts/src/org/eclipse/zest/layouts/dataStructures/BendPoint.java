/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.dataStructures;

import org.eclipse.zest.layouts.LayoutBendPoint;

/**
 * Implements a single bend point in a graph relationship.
 * 
 * @author Ian Bull
 * @author Chris Bennett
 */
public class BendPoint extends DisplayIndependentPoint implements LayoutBendPoint {

    private boolean isControlPoint = false; // is this a control point (for use in curves)

    public BendPoint(double x, double y) {
        super(x, y);
    }

    public BendPoint(double x, double y, boolean isControlPoint) {
        this(x, y);
        this.isControlPoint = isControlPoint;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean getIsControlPoint() {
        return isControlPoint;
    }

}
