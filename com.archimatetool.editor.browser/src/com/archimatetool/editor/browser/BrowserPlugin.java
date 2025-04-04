/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class BrowserPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.archimatetool.editor.browser"; //$NON-NLS-1$

	// The shared instance
	private static BrowserPlugin instance;
	
    /**
     * @return the shared instance
     */
    public static BrowserPlugin getInstance() {
        return instance;
    }
	/**
	 * The constructor
	 */
	public BrowserPlugin() {
	    instance = this;
	}
}
