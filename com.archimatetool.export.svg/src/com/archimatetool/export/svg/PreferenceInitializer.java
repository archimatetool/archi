/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;



/**
 * Used to initialise Preferences
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = ExportSVGPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(SVG_EXPORT_PREFS_VIEWBOX_ENABLED, true);
        store.setDefault(SVG_EXPORT_PREFS_VIEWBOX, "");
        
        store.setDefault(SVG_EXPORT_PREFS_TEXT_AS_SHAPES, true);
        store.setDefault(PDF_EXPORT_PREFS_TEXT_AS_SHAPES, true);
        
        store.setDefault(SVG_EXPORT_PREFS_EMBED_FONTS, false);
        store.setDefault(PDF_EXPORT_PREFS_EMBED_FONTS, false);
        
        store.setDefault(SVG_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND, false);
        store.setDefault(PDF_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND, false);
    }

}
