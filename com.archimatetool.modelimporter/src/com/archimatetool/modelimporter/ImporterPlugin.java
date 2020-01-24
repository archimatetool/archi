/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class ImporterPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.archimatetool.modelimporter"; //$NON-NLS-1$

	// The shared instance
	public static ImporterPlugin INSTANCE;
	
	/**
	 * The constructor
	 */
	public ImporterPlugin() {
	    INSTANCE = this;
	}
}
