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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class LineBox extends CompositeBox {

    /**
     * The maximum ascent of all contained fragments.
     */
    int contentAscent;

    /**
     * The maximum descent of all contained fragments.
     */
    int contentDescent;

    List fragments = new ArrayList();

    /**
     * @see org.eclipse.draw2d.text.CompositeBox#add(org.eclipse.draw2d.text.FlowBox)
     */
    @Override
    public void add(FlowBox child) {
        fragments.add(child);
        width += child.getWidth();
        contentAscent = Math.max(contentAscent, child.getOuterAscent());
        contentDescent = Math.max(contentDescent, child.getOuterDescent());
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#getAscent()
     */
    @Override
    public int getAscent() {
        int ascent = 0;
        for (int i = 0; i < fragments.size(); i++)
            ascent = Math.max(ascent, ((FlowBox) fragments.get(i)).getAscent());
        return ascent;
    }

    /**
     * Returns the remaining width available for line content.
     * 
     * @return the available width in pixels
     */
    int getAvailableWidth() {
        if (recommendedWidth < 0)
            return Integer.MAX_VALUE;
        return recommendedWidth - getWidth();
    }

    @Override
    int getBottomMargin() {
        return 0;
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#getDescent()
     */
    @Override
    public int getDescent() {
        int descent = 0;
        for (int i = 0; i < fragments.size(); i++)
            descent = Math.max(descent,
                    ((FlowBox) fragments.get(i)).getDescent());
        return descent;
    }

    /**
     * @return Returns the fragments.
     */
    List getFragments() {
        return fragments;
    }

    @Override
    int getTopMargin() {
        return 0;
    }

    /**
     * @return <code>true</code> if this box contains any fragments
     */
    public boolean isOccupied() {
        return !fragments.isEmpty();
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#requiresBidi()
     */
    @Override
    public boolean requiresBidi() {
        for (Iterator iter = getFragments().iterator(); iter.hasNext();) {
            FlowBox box = (FlowBox) iter.next();
            if (box.requiresBidi())
                return true;
        }
        return false;
    }

}
