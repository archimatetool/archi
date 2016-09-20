/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = ArchiZestPlugin.INSTANCE.getPreferenceStore();
        
		store.setDefault(VISUALISER_DEPTH, 0);
    }
}
