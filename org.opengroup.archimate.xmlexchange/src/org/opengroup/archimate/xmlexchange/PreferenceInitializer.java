package org.opengroup.archimate.xmlexchange;

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
        IPreferenceStore store = XMLExchangePlugin.INSTANCE.getPreferenceStore();
        
        store.setDefault(XMLEXCHANGE_PREFS_ORGANISATION, true);
        store.setDefault(XMLEXCHANGE_PREFS_INCLUDE_XSD, false);
        store.setDefault(XMLEXCHANGE_PREFS_VALIDATE_AFTER_EXPORT, true);
    }
}
