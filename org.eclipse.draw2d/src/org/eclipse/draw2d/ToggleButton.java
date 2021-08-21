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

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Toggle that appears like a 3-dimensional button.
 */
public class ToggleButton extends Toggle {

    /** This ToggleButton's Label **/
    protected Label label = null;

    /**
     * Constructs a new ToggleButton with no initial contents.
     */
    public ToggleButton() {
        setStyle(STYLE_BUTTON | STYLE_TOGGLE);
    }

    /**
     * Constructs a ToggleButton with the passed IFigure as its contents.
     * 
     * @param contents
     *            the contents of the toggle button
     * @since 2.0
     */
    public ToggleButton(IFigure contents) {
        super(contents, STYLE_BUTTON | STYLE_TOGGLE);
    }

    /**
     * Constructs a ToggleButton with the passed string as its text.
     * 
     * @param text
     *            the text to be displayed on the button
     * @since 2.0
     */
    public ToggleButton(String text) {
        this(text, null);
    }

    /**
     * Constructs a ToggleButton with a Label containing the passed text and
     * icon.
     * 
     * @param text
     *            the text
     * @param normalIcon
     *            the icon
     * @since 2.0
     */
    public ToggleButton(String text, Image normalIcon) {
        super(new Label(text, normalIcon), STYLE_BUTTON | STYLE_TOGGLE);
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        if (isSelected() && isOpaque()) {
            fillCheckeredRectangle(graphics);
        } else {
            super.paintFigure(graphics);
        }
    }

    /**
     * Draws a checkered pattern to emulate a toggle button that is in the
     * selected state.
     * 
     * @param graphics
     *            The Graphics object used to paint
     */
    @SuppressWarnings("deprecation")
    protected void fillCheckeredRectangle(Graphics graphics) {
        graphics.setBackgroundColor(ColorConstants.button);
        graphics.setForegroundColor(ColorConstants.buttonLightest);
        Rectangle rect = getClientArea(Rectangle.SINGLETON).crop(
                new Insets(1, 1, 0, 0));
        graphics.fillRectangle(rect.x, rect.y, rect.width, rect.height);

        graphics.clipRect(rect);
        graphics.translate(rect.x, rect.y);
        int n = rect.width + rect.height;
        for (int i = 1; i < n; i += 2) {
            graphics.drawLine(0, i, i, 0);
        }
        graphics.restoreState();
    }

}
