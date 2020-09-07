/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;

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
        IPreferenceStore store = CSVImportExportPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(CSV_EXPORT_PREFS_SEPARATOR, 0);
        store.setDefault(CSV_EXPORT_PREFS_EXCEL_COMPATIBLE, false);
        store.setDefault(CSV_EXPORT_PREFS_STRIP_NEW_LINES, false);
        store.setDefault(CSV_EXPORT_PREFS_ENCODING, "UTF-8"); //$NON-NLS-1$
    }

}
