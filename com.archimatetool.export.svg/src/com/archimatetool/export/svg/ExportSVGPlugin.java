/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class ExportSVGPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.archimatetool.export.svg"; //$NON-NLS-1$

	// The shared instance
	private static ExportSVGPlugin instance;
	
    /**
     * @return the shared instance
     */
    public static ExportSVGPlugin getInstance() {
        return instance;
    }

	/**
	 * The constructor
	 */
	public ExportSVGPlugin() {
	    instance = this;
	}
}
