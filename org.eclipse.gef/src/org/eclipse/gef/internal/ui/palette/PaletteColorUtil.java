/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.internal.ui.palette;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.ui.internal.themes.IThemeRegistry;

/**
 * A class to keep miscellaneous palette color utilities.
 * 
 * @author crevells
 * @since 3.4
 */
@SuppressWarnings({"nls", "restriction"})
public class PaletteColorUtil {

    /**
     * Added by Phillipus - get a theme color from the theme registry
     */
    private static Color getThemeColor(final String id, RGB defaultColor) {
        IThemeRegistry reg = WorkbenchPlugin.getDefault().getThemeRegistry();
        ColorDefinition colorDef = reg.findColor(id);
        if(colorDef != null && colorDef.getValue() != null) {
            defaultColor = colorDef.getValue();
        }
        
        return new Color(defaultColor);
    }
    
    // Added by Phillipus
    public static final Color PALETTE_BACKGROUND = getThemeColor("org.eclipse.gef.PALETTE_BACKGROUND", new RGB(255, 255, 255));
    public static final Color TOOLBAR_BACKGROUND = getThemeColor("org.eclipse.gef.PALETTE_TOOLBAR_BACKGROUND", new RGB(245, 245, 245));
    public static final Color PALETTE_TITLE_LABEL_FOREGROUND = getThemeColor("org.eclipse.gef.PALETTE_TITLE_LABEL_FOREGROUND", ColorConstants.listForeground.getRGB());
    public static final Color PALETTE_TITLE_LABEL_BACKGROUND_START = getThemeColor("org.eclipse.gef.PALETTE_TITLE_LABEL_BACKGROUND_START", ColorConstants.listBackground.getRGB());
    public static final Color PALETTE_TITLE_LABEL_BACKGROUND_END = getThemeColor("org.eclipse.gef.PALETTE_TITLE_LABEL_BACKGROUND_END", ColorConstants.button.getRGB());
    public static final Color PALETTE_ARROW_BUTTON_BACKGROUND = getThemeColor("org.eclipse.gef.PALETTE_ARROW_BUTTON_BACKGROUND", ColorConstants.listBackground.getRGB());
    public static final Color PALETTE_ARROW_BUTTON_FOREGROUND = getThemeColor("org.eclipse.gef.PALETTE_ARROW_BUTTON_FOREGROUND", ColorConstants.buttonDarkest.getRGB());

    public static final Color WIDGET_BACKGROUND = ColorConstants.button;

    public static final Color WIDGET_NORMAL_SHADOW = ColorConstants.buttonDarker;

    public static final Color WIDGET_DARK_SHADOW = ColorConstants.buttonDarkest;

    public static final Color WIDGET_LIST_BACKGROUND = ColorConstants.listBackground;

    public static final Color INFO_FOREGROUND = ColorConstants.tooltipForeground;

    public static final Color ARROW_HOVER = new Color(229, 229, 219);

    private static final Color HOVER_COLOR = new Color(252, 228, 179);

    private static final Color SELECTED_COLOR = new Color(207, 227, 250);

    private static final Color HOVER_COLOR_HICONTRAST = new Color(0, 128,
            0);

    private static final Color SELECTED_COLOR_HICONTRAST = new Color(128,
            0, 128);

    public static final Color WIDGET_BACKGROUND_LIST_BACKGROUND_40 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_LIST_BACKGROUND, 0.4);

    public static final Color WIDGET_BACKGROUND_LIST_BACKGROUND_60 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_LIST_BACKGROUND, 0.6);

    public static final Color WIDGET_BACKGROUND_LIST_BACKGROUND_85 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_LIST_BACKGROUND, 0.85);

    public static final Color WIDGET_BACKGROUND_LIST_BACKGROUND_90 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_LIST_BACKGROUND, 0.9);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_40 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.4);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_45 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.45);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_65 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.65);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_70 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.7);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_80 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.8);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_90 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.9);

    public static final Color WIDGET_BACKGROUND_NORMAL_SHADOW_95 = FigureUtilities
            .mixColors(PaletteColorUtil.WIDGET_BACKGROUND,
                    PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.95);

    /**
     * Gets the color to be used when hovering over palette items. The color
     * differs in high contrast mode.
     * 
     * @return the hover color
     * @since 3.4
     */
    public static Color getHoverColor() {
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        if (display.getHighContrast()) {
            return HOVER_COLOR_HICONTRAST;
        }
        return HOVER_COLOR;
    }

    /**
     * Gets the color to be used when selecting palette items. The color differs
     * in high contrast mode.
     * 
     * @return the selected color
     * @since 3.4
     */
    public static Color getSelectedColor() {
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        if (display.getHighContrast()) {
            return SELECTED_COLOR_HICONTRAST;
        }
        return SELECTED_COLOR;
    }
}
