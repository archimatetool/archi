/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.jasperreports.JasperReportsPlugin;



/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants, IJasperPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = JasperReportsPlugin.INSTANCE.getPreferenceStore();
        
		store.setDefault(JASPER_USER_REPORTS_FOLDER, JasperReportsPlugin.INSTANCE.getDefaultUserTemplatesFolder().getAbsolutePath());
    }
}
