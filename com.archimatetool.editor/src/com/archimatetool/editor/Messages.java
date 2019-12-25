package com.archimatetool.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.messages"; //$NON-NLS-1$

    public static String PluginInstaller_0;

    public static String PluginInstaller_1;

    public static String SplashHandler_0;

    public static String WorkbenchCleaner_0;

    public static String WorkbenchCleaner_1;

    public static String WorkbenchCleaner_2;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
