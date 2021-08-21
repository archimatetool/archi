/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.constraints;

/**
 * 
 * @author Chris Bennett
 *
 */
public class BasicEntityConstraint implements LayoutConstraint {

    public boolean hasPreferredLocation = false;

    public double preferredX;
    public double preferredY;

    public boolean hasPreferredSize = false;
    public double preferredWidth;
    public double preferredHeight;

    public BasicEntityConstraint() {
        clear();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.zest.layouts.constraints.LayoutConstraint#clear()
     */
    @Override
    public void clear() {
        this.hasPreferredLocation = false;
        this.hasPreferredSize = false;
        this.preferredX = 0.0;
        this.preferredY = 0.0;
        this.preferredWidth = 0.0;
        this.preferredHeight = 0.0;
    }
}
