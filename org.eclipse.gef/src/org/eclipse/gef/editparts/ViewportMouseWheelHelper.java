/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.MouseWheelHelper;

/**
 * ViewportMouseWheelHelper is the default MouseWheelHelper that should be used
 * to scroll edit parts.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class ViewportMouseWheelHelper extends ViewportHelper implements
        MouseWheelHelper {

    private static final int LINE_HEIGHT = 12;

    private int lineHeight, pageHeight;

    /**
     * Constructor
     * 
     * @param part
     *            the EditPArt that has to be scrolled
     */
    public ViewportMouseWheelHelper(GraphicalEditPart part) {
        this(part, -1, -1);
    }

    /**
     * 
     * Constructor
     * 
     * @param part
     *            the EditPart that has to be scrolled
     * @param lineHeight
     *            the new line height
     * @param pageHeight
     *            the new page height
     * @see #setLineHeight(int)
     * @see #setPageHeight(int)
     */
    public ViewportMouseWheelHelper(GraphicalEditPart part, int lineHeight,
            int pageHeight) {
        super(part);
        setLineHeight(lineHeight);
        setPageHeight(pageHeight);
    }

    /**
     * Finds the viewport of the given EditPart and scrolls it as requested. If
     * it can't be scrolled then leaves doit to be true so that the given
     * EditPart's ancestors might have a chance to scroll.
     * 
     * @see org.eclipse.gef.MouseWheelHelper#handleMouseWheelScrolled(org.eclipse.swt.widgets.Event)
     */
    @Override
    public void handleMouseWheelScrolled(Event event) {
        Viewport viewport = findViewport(owner);
        if (viewport == null || !viewport.isShowing())
            return;
        RangeModel rModel = viewport.getVerticalRangeModel();
        if (rModel.getExtent() < (rModel.getMaximum() - rModel.getMinimum())) {
            int currentPos = rModel.getValue();
            int scrollHeight = lineHeight;
            if (event.detail == SWT.SCROLL_PAGE)
                scrollHeight = pageHeight > lineHeight ? pageHeight
                        : Math.max(
                                lineHeight,
                                viewport.getClientArea(Rectangle.SINGLETON).height - 26);
            scrollHeight *= event.count;
            viewport.setVerticalLocation(currentPos - scrollHeight);
            if (rModel.getValue() != currentPos)
                event.doit = false;
        }
    }

    /**
     * Sets the height (in pixels) that will be scrolled when
     * {@link SWT#SCROLL_LINE line scrolling} is requested. If the line height
     * is set to 0 or less, or not set at all, a default height will be used.
     * 
     * @param height
     *            the new line height
     */
    public void setLineHeight(int height) {
        if (height > 0)
            lineHeight = height;
        else
            lineHeight = LINE_HEIGHT;
    }

    /**
     * Sets the height (in pixels) that will be scrolled when
     * {@link SWT#SCROLL_PAGE page scrolling} is requested. A page height that
     * is less than the line height will not be respected. If the page height is
     * set to -1, or one is not set at all, the default height will be used. The
     * default height is calculated based on the given EditPart's figure's
     * viewport's size. It will not be less than the line height.
     * 
     * @param height
     *            the new page height
     */
    public void setPageHeight(int height) {
        pageHeight = height;
    }

}
