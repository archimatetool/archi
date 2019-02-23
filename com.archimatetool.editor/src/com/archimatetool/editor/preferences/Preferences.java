/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.editor.ArchiPlugin;



/**
 * Preferences Manager convenience class
 * 
 * @author Phillip Beauvoir
 */
public class Preferences implements IPreferenceConstants {
    
    public static IPreferenceStore STORE = ArchiPlugin.INSTANCE.getPreferenceStore();

    public static String getUserDataFolder() {
        return STORE.getString(USER_DATA_FOLDER);
    }
    
    public static int getGridSize() {
        return STORE.getInt(GRID_SIZE);
    }
    
    public static boolean isGridVisible() {
        return STORE.getBoolean(GRID_VISIBLE);
    }
    
    public static boolean isGridSnap() {
        return STORE.getBoolean(GRID_SNAP);
    }
    
    public static boolean doShowGuideLines() {
        return STORE.getBoolean(GRID_SHOW_GUIDELINES);
    }
    
    public static boolean doOpenDiagramsOnLoad() {
        return STORE.getBoolean(OPEN_DIAGRAMS_ON_LOAD);
    }
    
    public static boolean useAntiAliasing() {
        return STORE.getBoolean(ANTI_ALIAS);
    }
    
    public static String getDefaultViewFont() {
        return STORE.getString(DEFAULT_VIEW_FONT);
    }
    
    public static void setDefaultViewFont(String val) {
        STORE.setValue(DEFAULT_VIEW_FONT, val);
    }
    
    public static boolean doLinkView() {
        return STORE.getBoolean(LINK_VIEW);
    }
    
    public static void setLinkView(boolean val) {
        STORE.setValue(LINK_VIEW, val);
    }
    
    public static boolean doShowPalette() {
        return STORE.getBoolean(PALETTE_STATE);
    }
    
    public static boolean isMagicConnectorPolarity() {
        return STORE.getBoolean(MAGIC_CONNECTOR_POLARITY);
    }
    
    public static boolean doShowViewTooltips() {
        return STORE.getBoolean(VIEW_TOOLTIPS);
    }
}
