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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * Provides for a frame-like border which contains a title bar for holding the
 * title of a Figure.
 */
public class FrameBorder extends CompoundBorder implements LabeledBorder {

    /**
     * The border scheme that determines the border highlight and shadow colors,
     * as well as the border width (3).
     */
    protected static final SchemeBorder.Scheme SCHEME_FRAME = new SchemeBorder.Scheme(
            new Color[] { ColorConstants.button, ColorConstants.buttonLightest,
                    ColorConstants.button }, new Color[] {
                    ColorConstants.buttonDarkest, ColorConstants.buttonDarker,
                    ColorConstants.button });

    {
        createBorders();
    }

    /**
     * Constructs a FrameBorder with its label set to the name of the
     * {@link TitleBarBorder} class.
     * 
     * @since 2.0
     */
    public FrameBorder() {
    }

    /**
     * Constructs a FrameBorder with the title set to the passed String.
     * 
     * @param label
     *            label or title of the frame.
     * @since 2.0
     */
    public FrameBorder(String label) {
        setLabel(label);
    }

    /**
     * Creates the necessary borders for this FrameBorder. The inner border is a
     * {@link TitleBarBorder}. The outer border is a {@link SchemeBorder}.
     * 
     * @since 2.0
     */
    protected void createBorders() {
        inner = new TitleBarBorder();
        outer = new SchemeBorder(SCHEME_FRAME);
    }

    /**
     * Returns the inner border of this FrameBorder, which contains the label
     * for the FrameBorder.
     * 
     * @return the border holding the label.
     * @since 2.0
     */
    protected LabeledBorder getLabeledBorder() {
        return (LabeledBorder) inner;
    }

    /**
     * @return the label for this border
     */
    @Override
    public String getLabel() {
        return getLabeledBorder().getLabel();
    }

    /**
     * Sets the label for this border.
     * 
     * @param label
     *            the label
     */
    @Override
    public void setLabel(String label) {
        getLabeledBorder().setLabel(label);
    }

    /**
     * Sets the font for this border's label.
     * 
     * @param font
     *            the font
     */
    @Override
    public void setFont(Font font) {
        getLabeledBorder().setFont(font);
    }

}
