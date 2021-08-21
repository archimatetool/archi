/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

import org.eclipse.zest.layouts.LayoutStyles;

/**
 * @version  2.0
 * @author   Casey Best and Rob Lintern (version 1.0 by Rob Lintern)
 */
public class VerticalLayoutAlgorithm extends GridLayoutAlgorithm {

    /**
     * Veertical Layout Algorithm constructor with no styles.
     *
     */
    public VerticalLayoutAlgorithm() {
        this(LayoutStyles.NONE);
    }

    public VerticalLayoutAlgorithm(int styles) {
        super(styles);
    }

    /**
     * Calculates and returns an array containing the number of columns, followed by the number of rows
     */
    @Override
    protected int[] calculateNumberOfRowsAndCols(int numChildren, double boundX, double boundY, double boundWidth, double boundHeight) {
        int cols = 1;
        int rows = numChildren;
        int[] result = { cols, rows };
        return result;
    }

    @Override
    protected boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
        if (asynchronous && continueous)
            return false;
        else if (asynchronous && !continueous)
            return true;
        else if (!asynchronous && continueous)
            return false;
        else if (!asynchronous && !continueous)
            return true;

        return false;
    }
}
