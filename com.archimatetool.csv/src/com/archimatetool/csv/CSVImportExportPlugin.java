/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class CSVImportExportPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.archimatetool.csv"; //$NON-NLS-1$

	// The shared instance
	private static CSVImportExportPlugin instance;
	
    /**
     * @return the shared instance
     */
    public static CSVImportExportPlugin getInstance() {
        return instance;
    }

	public CSVImportExportPlugin() {
	    instance = this;
	}
}
