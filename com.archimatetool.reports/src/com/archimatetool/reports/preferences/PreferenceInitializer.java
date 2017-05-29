/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.reports.ArchiReportsPlugin;



/**
 * Class used to initialize default preference values
 *
 * @author Tun Schlechter
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IArchiReportsPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = ArchiReportsPlugin.INSTANCE.getPreferenceStore();

        store.setDefault(VIEWS_SHOW_DOCUMENTATION, VIEWS_SHOW_DOCUMENTATION_DEFAULT_VALUE);
        store.setDefault(VIEWS_SHOW_PROPERTIES, VIEWS_SHOW_PROPERTIES_DEFAULT_VALUE);
        store.setDefault(VIEWS_SHOW_ELEMENTS, VIEWS_SHOW_ELEMENTS_DEFAULT_VALUE);
        store.setDefault(VIEWS_DEFAULT_TAB, VIEWS_DEFAULT_TAB_DEFAULT_VALUE);

        store.setDefault(ELEMENTS_SHOW_DOCUMENTATION, ELEMENTS_SHOW_DOCUMENTATION_DEFAULT_VALUE);
        store.setDefault(ELEMENTS_SHOW_PROPERTIES, ELEMENTS_SHOW_PROPERTIES_DEFAULT_VALUE);
        store.setDefault(ELEMENTS_SHOW_VIEWS, ELEMENTS_SHOW_VIEWS);
        store.setDefault(ELEMENTS_DEFAULT_TAB, ELEMENTS_DEFAULT_TAB_DEFAULT_VALUE);

    }
}
