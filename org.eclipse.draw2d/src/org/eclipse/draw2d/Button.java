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
 * A Button usually has a border and appears to move up and down in response to
 * being pressed. It can contain an image and/or text in it.
 */
public class Button extends Clickable {

    /**
     * Constructs a default Button with no icon or text.
     * 
     * @since 2.0
     */
    public Button() {
        super();
        setStyle(STYLE_BUTTON);
    }

    /**
     * Contructs a Button containing the icon <i>image</i>.
     * 
     * @param image
     *            Image to be used by the Button as its icon.
     * @since 2.0
     */
    public Button(Image image) {
        super(new Label(image), STYLE_BUTTON);
    }

    /**
     * Constructs a Button containing the given text.
     * 
     * @param text
     *            Text for the button.
     * @since 2.0
     */
    public Button(String text) {
        super(new Label(text), STYLE_BUTTON);
    }

    /**
     * Constructs a Button with the given image and text.
     * 
     * @param text
     *            Text for the button.
     * @param image
     *            Image for the button.
     * @since 2.0
     */
    public Button(String text, Image image) {
        super(new Label(text, image), STYLE_BUTTON);
    }

    /**
     * Initializes this button by setting its default border and setting its
     * background color to {@link ColorConstants#button}.
     * 
     * @since 2.0
     */
    @Override
    protected void init() {
        super.init();
        setBackgroundColor(ColorConstants.button);
    }

}
