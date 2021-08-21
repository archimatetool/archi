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

/**
 * Provides a raised border.
 */
public class SimpleRaisedBorder extends SchemeBorder {

    private static final Scheme DOUBLE = new Scheme(new Color[] {
            ColorConstants.buttonLightest, ColorConstants.button },
            new Color[] { ColorConstants.buttonDarkest,
                    ColorConstants.buttonDarker });

    /**
     * Constructs a SimpleRaisedBorder with the predefined
     * {@link SchemeBorder.SCHEMES#BUTTON_RAISED} Scheme set as default.
     * 
     * @since 2.0
     */
    public SimpleRaisedBorder() {
        super(SCHEMES.BUTTON_RAISED);
    }

    /**
     * Constructs a SimpleRaisedBorder with the width of all sides provided as
     * input. If width == 2, this SimpleRaisedBorder will use the local DOUBLE
     * Scheme, otherwise it will use the
     * {@link SchemeBorder.SCHEMES#BUTTON_RAISED} Scheme.
     * 
     * @param width
     *            the width of all the sides of the border
     * @since 2.0
     */
    public SimpleRaisedBorder(int width) {
        super(width == 2 ? DOUBLE : SCHEMES.BUTTON_RAISED);
    }

}
