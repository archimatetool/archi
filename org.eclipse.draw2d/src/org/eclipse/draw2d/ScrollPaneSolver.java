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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class handles the calculation of solving for the area of a
 * {@link org.eclipse.draw2d.ScrollPane}'s viewport and insets. Also determines
 * if the horizontal and vertical scrollbars should be visible.
 */
public class ScrollPaneSolver {

    /** Scrollbar visibility constants -- never show scrollbars **/
    public static final int NEVER = 0;
    /** Scrollbar visibility constants -- show scrollbars automatically **/
    public static final int AUTOMATIC = 1;
    /** Scrollbar visibility constants -- always show scrollbars **/
    public static final int ALWAYS = 2;

    /**
     * Container class for the results of ScrollPaneSolver's solve method
     */
    public static class Result {
        /** Show horizontal scrollbar boolean **/
        public boolean showH;

        /** Show vertical scrollbar boolean **/
        public boolean showV;

        /** Area of ScrollPane's viewport **/
        public Rectangle viewportArea;

        /** Insets of ScrollPane **/
        public Insets insets;
    }

    /**
     * Solves for the viewport area, insets, and visibility of horizontal and
     * vertical scrollbars of a ScrollPane
     * 
     * @param clientArea
     *            The ScrollPane's client area
     * @param viewport
     *            The ScrollPane's Viewport
     * @param hVis
     *            Horizontal scrollbar visibility
     * @param vVis
     *            Vertical scrollbar visibility
     * @param vBarWidth
     *            Width of vertical scrollbar
     * @param hBarHeight
     *            Height of horizontal scrollbar
     * @return the Result
     */
    @SuppressWarnings("deprecation")
    public static Result solve(Rectangle clientArea, Viewport viewport,
            int hVis, int vVis, int vBarWidth, int hBarHeight) {
        Result result = new Result();
        result.insets = new Insets();
        result.insets.bottom = hBarHeight;
        result.insets.right = vBarWidth;

        Dimension available = clientArea.getSize();
        Dimension guaranteed = new Dimension(available).shrink(
                (vVis == NEVER ? 0 : result.insets.right), (hVis == NEVER ? 0
                        : result.insets.bottom));
        guaranteed.width = Math.max(guaranteed.width, 0);
        guaranteed.height = Math.max(guaranteed.height, 0);
        int wHint = guaranteed.width;
        int hHint = guaranteed.height;

        Dimension preferred = viewport.getPreferredSize(wHint, hHint).getCopy();
        Insets viewportInsets = viewport.getInsets();
        /*
         * This was calling viewport.getMinimumSize(), but viewport's minimum
         * size was really small, and wasn't a function of its contents.
         */
        Dimension viewportMinSize = new Dimension(viewportInsets.getWidth(),
                viewportInsets.getHeight());
        if (viewport.getContents() != null) {
            if (viewport.getContentsTracksHeight() && hHint > -1)
                hHint = Math.max(0, hHint - viewportInsets.getHeight());
            if (viewport.getContentsTracksWidth() && wHint > -1)
                wHint = Math.max(0, wHint - viewportInsets.getWidth());
            viewportMinSize.expand(viewport.getContents().getMinimumSize(wHint,
                    hHint));
        }

        /*
         * Adjust preferred size if tracking flags set. Basically, tracking ==
         * "compress view until its minimum size is reached".
         */
        if (viewport.getContentsTracksHeight())
            preferred.height = viewportMinSize.height;
        if (viewport.getContentsTracksWidth())
            preferred.width = viewportMinSize.width;

        boolean none = available.contains(preferred), both = !none
                && preferred.containsProper(guaranteed), showV = both
                || preferred.height > available.height, showH = both
                || preferred.width > available.width;

        // Adjust for visibility override flags
        result.showV = vVis != NEVER && (showV || vVis == ALWAYS);
        result.showH = hVis != NEVER && (showH || hVis == ALWAYS);

        if (!result.showV)
            result.insets.right = 0;
        if (!result.showH)
            result.insets.bottom = 0;
        result.viewportArea = clientArea.getCropped(result.insets);
        viewport.setBounds(result.viewportArea);
        return result;
    }

}
