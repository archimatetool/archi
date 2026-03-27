/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.gef.ui.palette.PaletteColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.themes.IThemeManager;

import com.archimatetool.editor.ui.ThemeUtils;

/**
 * CustomPaletteColorProvider
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class CustomPaletteColorProvider extends PaletteColorProvider {
    
    private static CustomPaletteColorProvider instance = new CustomPaletteColorProvider();

    public static CustomPaletteColorProvider getInstance() {
        return instance;
    }
    
    private boolean themesEnabled = ThemeUtils.getThemeEngine() != null;
    private IThemeManager themeManager = ThemeUtils.getThemeManager();

    private CustomPaletteColorProvider() {}
    
    private Color getColorFromRegistry(String id, Color defaultColor) {
        Color color = (themesEnabled && themeManager != null) ? themeManager.getCurrentTheme().getColorRegistry().get(id) : null;
        return color != null ? color : defaultColor;
    }
    
    @Override
    public Color getTitleBackground() {
        return getColorFromRegistry("org.eclipse.gef.PALETTE_TITLE_BACKGROUND", super.getTitleBackground());
    }
    
    @Override
    public Color getToolbarEditPartBackground() {
        return getColorFromRegistry("org.eclipse.gef.PALETTE_TOOLBAR_BACKGROUND", super.getToolbarEditPartBackground());
    }
    
    @Override
    public Color getGroupEditPartBackground() {
        return getPaletteBackground(super.getGroupEditPartBackground());
    }
    
    @Override
    public Color getSliderPaletteEditPartBackground() {
        return getPaletteBackground(super.getSliderPaletteEditPartBackground());
    }
    
    @Override
    public Color getDrawerEditPartBackground() {
        return getPaletteBackground(super.getDrawerEditPartBackground());
    }
    
    public Color getPaletteBackground(Color defaultColor) {
        return getColorFromRegistry("org.eclipse.gef.PALETTE_BACKGROUND", defaultColor);
    }
}
