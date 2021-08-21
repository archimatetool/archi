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
 * FlowBoxes that are leaf nodes.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public abstract class ContentBox extends FlowBox {

    private int bidiLevel = -1;
    private LineRoot lineRoot;

    /**
     * @see FlowBox#getBaseline()
     */
    @Override
    public int getBaseline() {
        return lineRoot.getBaseline();
    }

    /**
     * @return the Bidi level of this box, if one has been set; -1 otherwise
     * @see #setBidiLevel(int)
     */
    public int getBidiLevel() {
        return bidiLevel;
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#getLineRoot()
     */
    @Override
    LineRoot getLineRoot() {
        return lineRoot;
    }

    /**
     * Returns <code>true</code> if the bidi level for this box is specified,
     * and is not the default level (0).
     * 
     * @see org.eclipse.draw2d.text.FlowBox#requiresBidi()
     */
    @Override
    public boolean requiresBidi() {
        return bidiLevel > 0;
    }

    /**
     * Sets the Bidi level of this fragment. It is used to rearrange fragments
     * as defined by the Unicode Bi-directional algorithm. Valid values are -1
     * (meaning no Bidi level), or any non-negative integer less than 62.
     * 
     * @param newLevel
     *            the new BidiLevel
     * @see #getBidiLevel()
     */
    public void setBidiLevel(int newLevel) {
        bidiLevel = newLevel;
    }

    @Override
    void setLineRoot(LineRoot root) {
        this.lineRoot = root;
    }

}
