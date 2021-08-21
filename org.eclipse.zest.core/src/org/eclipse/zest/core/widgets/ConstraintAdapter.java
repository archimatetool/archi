/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.widgets;

import org.eclipse.zest.layouts.constraints.LayoutConstraint;

/**
 * 
 * This interface is used to populate layout constraints on Zest nodes.
 * Constraint will be a instance of LayoutConstraint (look at the heirarchy for an up-to-date list).
 * 
 * @author Ian Bull
 */
public interface ConstraintAdapter {

    /**
     * 
     * @param object
     * @param constraint
     */
    public void populateConstraint(Object object, LayoutConstraint constraint);

}
