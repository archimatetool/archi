/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.hammer.ArchiHammerPlugin;



/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = ArchiHammerPlugin.INSTANCE.getPreferenceStore();
        
		store.setDefault(PREFS_HAMMER_CHECK_EMPTY_VIEWS, true);
		store.setDefault(PREFS_HAMMER_CHECK_INVALID_RELATIONS, true);
		store.setDefault(PREFS_HAMMER_CHECK_NESTING, true);
		store.setDefault(PREFS_HAMMER_CHECK_UNUSED_ELEMENTS, true);
		store.setDefault(PREFS_HAMMER_CHECK_UNUSED_RELATIONS, true);
		store.setDefault(PREFS_HAMMER_CHECK_VIEWPOINT, true);
        store.setDefault(PREFS_HAMMER_CHECK_DUPLICATE_ELEMENTS, true);
        store.setDefault(PREFS_HAMMER_CHECK_JUNCTIONS, true);
    }
}
