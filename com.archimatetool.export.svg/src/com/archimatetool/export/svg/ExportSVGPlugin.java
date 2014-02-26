package com.archimatetool.export.svg;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ExportSVGPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.archimatetool.export.svg"; //$NON-NLS-1$

	// The shared instance
	private static ExportSVGPlugin plugin;
	
	/**
	 * The constructor
	 */
	public ExportSVGPlugin() {
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
	public static ExportSVGPlugin getDefault() {
		return plugin;
	}

}
