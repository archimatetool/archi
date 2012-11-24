/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.zest;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateZestPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "uk.ac.bolton.archimate.zest"; //$NON-NLS-1$

    /**
     * The shared instance
     */
    public static ArchimateZestPlugin INSTANCE;
    

    public ArchimateZestPlugin() {
        INSTANCE = this;
    }
}
