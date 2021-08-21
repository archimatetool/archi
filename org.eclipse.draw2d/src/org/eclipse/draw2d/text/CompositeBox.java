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
package org.eclipse.draw2d.text;

/**
 * A FlowBox that can contain other FlowBoxes. The contained FlowBoxes are
 * called <i>fragments</i>.
 * 
 * @author hudsonr
 * @since 2.1
 */
public abstract class CompositeBox extends FlowBox {

    int recommendedWidth = -1;

    /**
     * Adds the given box and updates properties of this composite box.
     * 
     * @param box
     *            the child being added
     */
    public abstract void add(FlowBox box);

    abstract int getBottomMargin();

    /**
     * Returns the recommended width for this CompositeBox.
     * 
     * @return the recommended width
     */
    public int getRecommendedWidth() {
        return recommendedWidth;
    }

    abstract int getTopMargin();

    /**
     * Sets the recommended width for this CompositeBox.
     * 
     * @param w
     *            the width
     */
    public void setRecommendedWidth(int w) {
        recommendedWidth = w;
    }

    /**
     * Positions the box vertically by setting the y coordinate for the top of
     * the content of the line. For internal use only.
     * 
     * @param top
     *            the y coordinate
     * @since 3.1
     */
    public abstract void setLineTop(int top);

}
