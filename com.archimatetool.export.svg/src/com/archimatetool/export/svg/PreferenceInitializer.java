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
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = ExportSVGPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(SVG_EXPORT_PREFS_EMBED_FONTS, true);
        store.setDefault(SVG_EXPORT_PREFS_VIEWBOX_ENABLED, true);
        store.setDefault(SVG_EXPORT_PREFS_VIEWBOX, ""); //$NON-NLS-1$
    }

}
