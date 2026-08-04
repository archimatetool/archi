/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;

/**
 * Manages the two bundled default colour schemes ("ArchiMate Standard" and "Saturated") that back the
 * Colours preference page's per-element default colours.
 * <p>
 * Applying a scheme replaces the preference store's *default* colour values (not any colours a user has
 * explicitly customized) and clears any explicit override, so the scheme's colours become the new baseline.
 */
public final class ColourSchemeManager implements IPreferenceConstants {

    private static final String STANDARD_SCHEME_FILE = "colourschemes/ArchiMateStandard.prefs"; //$NON-NLS-1$
    private static final String SATURATED_SCHEME_FILE = "colourschemes/Saturated.prefs"; //$NON-NLS-1$

    private ColourSchemeManager() {
    }

    /**
     * @param shapeStyle A SHAPE_STYLE preference value
     * @return The colour scheme that is the default match for a given Shape Style
     */
    public static String getSchemeForShapeStyle(String shapeStyle) {
        return SHAPE_STYLE_OUTLINE.equals(shapeStyle) ? COLOUR_SCHEME_SATURATED : COLOUR_SCHEME_STANDARD;
    }

    /**
     * @param schemeId A COLOUR_SCHEME preference value
     * @return The bundled scheme's key/value pairs
     * @throws IOException if the bundled resource can't be read
     */
    public static PreferenceStore loadScheme(String schemeId) throws IOException {
        String file = COLOUR_SCHEME_SATURATED.equals(schemeId) ? SATURATED_SCHEME_FILE : STANDARD_SCHEME_FILE;

        URL url = ArchiPlugin.getInstance().getBundle().getEntry(file);
        if(url == null) {
            throw new IOException("Colour scheme resource not found: " + file); //$NON-NLS-1$
        }

        PreferenceStore store = new PreferenceStore();

        try(InputStream in = url.openStream()) {
            store.load(in);
        }

        return store;
    }

    /**
     * @return true if none of the colours governed by a colour scheme have been explicitly customized by the user
     */
    public static boolean isColourSchemeUnmodified() {
        IPreferenceStore store = ArchiPlugin.getInstance().getPreferenceStore();

        try {
            for(String key : loadScheme(COLOUR_SCHEME_STANDARD).preferenceNames()) {
                if(!store.isDefault(key)) {
                    return false;
                }
            }
        }
        catch(IOException ex) {
            Logger.error("Error loading colour scheme", ex); //$NON-NLS-1$
            return false;
        }

        return true;
    }

    /**
     * Apply a bundled colour scheme as the new baseline default, clearing any explicit overrides so the
     * scheme's colours take effect immediately.
     * @param schemeId A COLOUR_SCHEME preference value
     */
    public static void applyScheme(String schemeId) {
        IPreferenceStore store = ArchiPlugin.getInstance().getPreferenceStore();

        try {
            PreferenceStore scheme = loadScheme(schemeId);
            for(String key : scheme.preferenceNames()) {
                store.setDefault(key, scheme.getString(key));
                store.setToDefault(key);
            }
            store.setValue(COLOUR_SCHEME, schemeId);
        }
        catch(IOException ex) {
            Logger.error("Error applying colour scheme", ex); //$NON-NLS-1$
        }
    }

    /**
     * Called when the Shape Style preference changes to Classic or Outline.
     * Automatically switches the default colour scheme to match, but only if the user hasn't customized
     * any colours, so as not to discard their edits.
     * @param shapeStyle The new SHAPE_STYLE preference value
     */
    public static void applySchemeForShapeStyleIfUnmodified(String shapeStyle) {
        if(isColourSchemeUnmodified()) {
            applyScheme(getSchemeForShapeStyle(shapeStyle));
        }
    }
}
