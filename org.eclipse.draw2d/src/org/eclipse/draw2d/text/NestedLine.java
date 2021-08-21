/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
 * @since 3.1
 */
public class NestedLine extends LineBox {

    InlineFlow owner;
    private LineRoot root;

    NestedLine(InlineFlow owner) {
        this.owner = owner;
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#containsPoint(int, int)
     */
    @Override
    public boolean containsPoint(int x, int y) {
        // $TODO should contains use LineRoot?
        return x >= getX() && x < getX() + getWidth()
                && y >= getBaseline() - getAscentWithBorder()
                && y < getBaseline() + getDescentWithBorder();
    }

    @Override
    int getAscentWithBorder() {
        return contentAscent + FlowUtilities.getBorderAscent(owner);
    }

    @Override
    int getDescentWithBorder() {
        return contentDescent + FlowUtilities.getBorderDescent(owner);
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#getBaseline()
     */
    @Override
    public int getBaseline() {
        return root.getBaseline();
    }

    @Override
    LineRoot getLineRoot() {
        return root;
    }

    // int getVisibleAscent() {
    // return contentAscent + FlowUtilities.getBorderAscent(owner);
    // }
    //
    // int getVisibleDescent() {
    // return contentDescent + FlowUtilities.getBorderDescent(owner);
    // }

    /**
     * Returns the outer ascent of this box. The outer ascent is the ascent
     * above the baseline including the border size and margin. This is used
     * when adding content into a LineBox. The linebox's own border must be
     * drawn around the children.
     * 
     * @return the outer ascent of this box
     */
    @Override
    public int getOuterAscent() {
        return contentAscent + FlowUtilities.getBorderAscentWithMargin(owner);
    }

    /**
     * Returns the outer descent of this box. The outer descent is the space
     * below the baseline including the border size and margin. This is used
     * when adding content into a LineBox. The linebox's own border must be
     * drawn around the children.
     * 
     * @return the outer descent of this box
     */
    @Override
    public int getOuterDescent() {
        return contentDescent + FlowUtilities.getBorderDescentWithMargin(owner);
    }

    @Override
    void setLineRoot(LineRoot root) {
        this.root = root;
        for (int i = 0; i < fragments.size(); i++)
            ((FlowBox) fragments.get(i)).setLineRoot(root);
    }

    /**
     * @see org.eclipse.draw2d.text.CompositeBox#setLineTop(int)
     */
    @Override
    public void setLineTop(int top) {
        throw new RuntimeException("not supported"); //$NON-NLS-1$
    }

}
