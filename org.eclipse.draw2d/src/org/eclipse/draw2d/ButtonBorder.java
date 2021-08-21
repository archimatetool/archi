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

import org.eclipse.draw2d.geometry.Insets;

/**
 * Creates a border for a clickable type of figure, which works in conjunction
 * with the Figure and its model. This border adjusts itself to the various
 * states the model of the figure could be. This border uses an extended
 * {@link SchemeBorder.Scheme Scheme} called {@link ButtonScheme} which provides
 * more information required by border to handle the the states of the model.
 * 
 * @see SchemeBorder.Scheme
 * @see ButtonScheme
 */
public class ButtonBorder extends SchemeBorder {

    /**
     * Default button border.
     * 
     * @see SCHEMES#BUTTON
     */
    public static final Border BUTTON = new ButtonBorder(SCHEMES.BUTTON);
    /**
     * Inverted hightlight colors from BUTTON.
     * 
     * @see SCHEMES#BUTTON_CONTRAST
     */
    public static final Border BUTTON_CONTRAST = new ButtonBorder(
            SCHEMES.BUTTON_CONTRAST);
    /**
     * Used for scrollbar buttons.
     * 
     * @see SCHEMES#BUTTON_SCROLLBAR
     */
    public static final Border BUTTON_SCROLLBAR = new ButtonBorder(
            SCHEMES.BUTTON_SCROLLBAR);
    /**
     * Used for toolbar buttons.
     * 
     * @see SCHEMES#TOOLBAR
     */
    public static final Border TOOLBAR = new ButtonBorder(SCHEMES.TOOLBAR);

    /**
     * Provides for a scheme to represent the borders of clickable figures like
     * buttons. Though similar to the {@link SchemeBorder.Scheme Scheme} it
     * supports an extra set of borders for the pressed states.
     */
    public static class ButtonScheme extends Scheme {
        private Color highlightPressed[] = null, shadowPressed[] = null;

        /**
         * Constructs a new button scheme where the input colors are the colors
         * for the top-left and bottom-right sides of the border. These colors
         * serve as the colors when the border is in a pressed state too. The
         * width of each side is determined by the number of colors passed in as
         * input.
         * 
         * @param highlight
         *            Colors for the top-left sides of the border
         * @param shadow
         *            Colors for the bottom-right sides of the border
         * @since 2.0
         */
        public ButtonScheme(Color[] highlight, Color[] shadow) {
            highlightPressed = this.highlight = highlight;
            shadowPressed = this.shadow = shadow;
            init();
        }

        /**
         * Constructs a new button scheme where the input colors are the colors
         * for the top-left and bottom-right sides of the border, for the normal
         * and pressed states. The width of each side is determined by the
         * number of colors passed in as input.
         * 
         * @param hl
         *            Colors for the top-left sides of the border
         * @param sh
         *            Colors for the bottom-right sides of the border
         * @param hlp
         *            Colors for the top-left sides of the border when figure is
         *            pressed
         * @param shp
         *            Colors for the bottom-right sides of the border when
         *            figure is pressed
         * @since 2.0
         */
        public ButtonScheme(Color[] hl, Color[] sh, Color[] hlp, Color[] shp) {
            highlight = hl;
            shadow = sh;
            highlightPressed = hlp;
            shadowPressed = shp;
            init();
        }

        /**
         * Calculates and returns the Insets for this border. The calculations
         * are based on the number of normal and pressed, highlight and shadow
         * colors.
         * 
         * @return The insets for this border
         * @since 2.0
         */
        @Override
        protected Insets calculateInsets() {
            int br = 1 + Math.max(getShadow().length,
                    getHighlightPressed().length);
            int tl = Math.max(getHighlight().length, getShadowPressed().length);
            return new Insets(tl, tl, br, br);
        }

        /**
         * Calculates and returns the opaque state of this border.
         * <p>
         * Returns false in the following conditions:
         * <ul>
         * <li>The number of highlight colors is different than the the number
         * of shadow colors.
         * <li>The number of pressed highlight colors is different than the
         * number of pressed shadow colors.
         * <li>Any of the highlight and shadow colors are set to
         * <code>null</code>
         * <li>Any of the pressed highlight and shadow colors are set to
         * <code>null</code>
         * </ul>
         * This is done so that the entire region under the figure is properly
         * covered.
         * 
         * @return The opaque state of this border
         * @since 2.0
         */
        @Override
        protected boolean calculateOpaque() {
            if (!super.calculateOpaque())
                return false;
            if (getHighlight().length != getShadowPressed().length)
                return false;
            if (getShadow().length != getHighlightPressed().length)
                return false;
            Color[] colors = getHighlightPressed();
            for (int i = 0; i < colors.length; i++)
                if (colors[i] == null)
                    return false;
            colors = getShadowPressed();
            for (int i = 0; i < colors.length; i++)
                if (colors[i] == null)
                    return false;
            return true;
        }

        /**
         * Returns the pressed highlight colors of this border.
         * 
         * @return Colors as an array of Colors
         * @since 2.0
         */
        protected Color[] getHighlightPressed() {
            return highlightPressed;
        }

        /**
         * Returns the pressed shadow colors of this border.
         * 
         * @return Colors as an array of Colors
         * @since 2.0
         */
        protected Color[] getShadowPressed() {
            return shadowPressed;
        }
    }

    /**
     * Interface defining commonly used schemes for the ButtonBorder.
     */
    public static interface SCHEMES {

        /**
         * Contrast button scheme
         */
        ButtonScheme BUTTON_CONTRAST = new ButtonScheme(new Color[] { button,
                buttonLightest }, DARKEST_DARKER);
        /**
         * Regular button scheme
         */
        ButtonScheme BUTTON = new ButtonScheme(new Color[] { buttonLightest },
                DARKEST_DARKER);
        /**
         * Toolbar button scheme
         */
        ButtonScheme TOOLBAR = new ButtonScheme(new Color[] { buttonLightest },
                new Color[] { buttonDarker });
        /**
         * Scrollbar button scheme
         */
        ButtonScheme BUTTON_SCROLLBAR = new ButtonScheme(new Color[] { button,
                buttonLightest }, DARKEST_DARKER, new Color[] { buttonDarker },
                new Color[] { buttonDarker });
    }

    /**
     * Constructs a ButtonBorder with a predefined button scheme set as its
     * default.
     * 
     * @since 2.0
     */
    public ButtonBorder() {
        setScheme(SCHEMES.BUTTON);
    }

    /**
     * Constructs a ButtonBorder with the input ButtonScheme set as its Scheme.
     * 
     * @param scheme
     *            ButtonScheme for this ButtonBorder.
     * @since 2.0
     */
    public ButtonBorder(ButtonScheme scheme) {
        setScheme(scheme);
    }

    /**
     * Paints this border with the help of the set scheme, the model of the
     * clickable figure, and other inputs. The scheme is used in conjunction
     * with the state of the model to get the appropriate colors for the border.
     * 
     * @param figure
     *            The Clickable that this border belongs to
     * @param graphics
     *            The graphics used for painting
     * @param insets
     *            The insets
     */
    @Override
    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        Clickable clickable = (Clickable) figure;
        ButtonModel model = clickable.getModel();
        ButtonScheme colorScheme = (ButtonScheme) getScheme();

        if (clickable.isRolloverEnabled() && !model.isMouseOver()
                && !model.isSelected())
            return;

        Color tl[], br[];
        if (model.isSelected() || model.isArmed()) {
            tl = colorScheme.getShadowPressed();
            br = colorScheme.getHighlightPressed();
        } else {
            tl = colorScheme.getHighlight();
            br = colorScheme.getShadow();
        }

        paint(graphics, figure, insets, tl, br);
    }

}
