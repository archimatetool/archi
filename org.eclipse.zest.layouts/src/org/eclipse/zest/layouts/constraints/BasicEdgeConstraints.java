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
 * @author Ian Bull
 * @author Chris Bennett
 */
public class BasicEdgeConstraints implements LayoutConstraint {

    // These should all be accessed directly.  
    public boolean isBiDirectional = false;
    public int weight = 1;

    /*
     * (non-Javadoc)
     * @see org.eclipse.zest.layouts.constraints.LayoutConstraint#clear()
     */
    @Override
    public void clear() {
        this.isBiDirectional = false;
        this.weight = 1;
    }

}
