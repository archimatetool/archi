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

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A CompositeBox suitable for containing multiple LineBox fragments.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class BlockBox extends CompositeBox {

    int height;
    private int y;
    BlockFlow owner;

    BlockBox(BlockFlow owner) {
        this.owner = owner;
    }

    /**
     * @see CompositeBox#add(FlowBox)
     */
    @Override
    public void add(FlowBox box) {
        width = Math.max(width, box.getWidth());
        height = Math.max(height, box.getBaseline() + box.getDescent());
    }

    /**
     * @see FlowBox#containsPoint(int, int)
     */
    @Override
    public boolean containsPoint(int x, int y) {
        return true;
    }

    /**
     * @see FlowBox#getAscent()
     */
    @Override
    public int getAscent() {
        return 0;
    }

    /**
     * @see FlowBox#getBaseline()
     */
    @Override
    public int getBaseline() {
        return y;
    }

    @Override
    int getBottomMargin() {
        return owner.getBottomMargin();
    }

    /**
     * @see FlowBox#getDescent()
     */
    @Override
    public int getDescent() {
        return height;
    }

    /**
     * @return Returns the height.
     */
    public int getHeight() {
        return height;
    }

    @Override
    LineRoot getLineRoot() {
        return null;
    }

    @Override
    int getTopMargin() {
        return owner.getTopMargin();
    }

    /**
     * Sets the height.
     * 
     * @param h
     *            The height
     */
    public void setHeight(int h) {
        height = h;
    }

    /**
     * @see CompositeBox#setLineTop(int)
     */
    @Override
    public void setLineTop(int y) {
        this.y = y;
    }

    Rectangle toRectangle() {
        return new Rectangle(getX(), y, Math.max(getWidth(), recommendedWidth),
                height);
    }

}
