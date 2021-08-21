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
 * A layout constraint that uses priorities
 * @author Ian Bull
 */
public class EntityPriorityConstraint implements LayoutConstraint {

    // A priority that can be set for nodes.  This could be used
    // for a treemap layout
    public double priority = 1.0;

    /*
     * (non-Javadoc)
     * @see org.eclipse.zest.layouts.constraints.LayoutConstraint#clear()
     */
    @Override
    public void clear() {
        this.priority = 1.0;
    }

}
