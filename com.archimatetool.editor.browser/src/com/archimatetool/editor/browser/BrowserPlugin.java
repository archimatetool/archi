/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BrowserPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.archimatetool.editor.browser"; //$NON-NLS-1$

	// The shared instance
	private static BrowserPlugin plugin;
	
	/**
	 * The constructor
	 */
	public BrowserPlugin() {
	}

	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BrowserPlugin getDefault() {
		return plugin;
	}
}
