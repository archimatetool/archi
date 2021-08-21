/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts;

/**
 * Specifies a single bend point in a graph relationship.
 * @author Ian Bull
 * @author Chris Bennett
 */
public interface LayoutBendPoint {
    public double getX();

    public double getY();

    public boolean getIsControlPoint();
}
