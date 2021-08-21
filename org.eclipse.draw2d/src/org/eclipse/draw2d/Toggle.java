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

/**
 * Basic Rule for Toggle: Whoever creates the toggle is reponsible for response
 * changes for it (selection, rollover, etc). Only
 * {@link org.eclipse.draw2d.CheckBox} does its own listening.
 */
public class Toggle extends Clickable {

    /**
     * Constructs a Toggle with no text or icon.
     * 
     * @since 2.0
     */
    public Toggle() {
        super();
        setStyle(STYLE_TOGGLE);
    }

    /**
     * Constructs a Toggle with passed text and icon
     * 
     * @param text
     *            the text
     * @param icon
     *            the icon
     * @since 2.0
     */
    public Toggle(String text, Image icon) {
        super(new Label(text, icon), STYLE_TOGGLE);
    }

    /**
     * Constructs a Toggle with passed IFigure as its contents.
     * 
     * @param contents
     *            the contents
     * @since 2.0
     */
    public Toggle(IFigure contents) {
        super(contents, STYLE_TOGGLE);
    }

    /**
     * Constructs a Toggle with the passed figure as its contents and the given
     * style.
     * 
     * @param contents
     *            the contents
     * @param style
     *            the style
     */
    public Toggle(IFigure contents, int style) {
        super(contents, style);
    }

}
