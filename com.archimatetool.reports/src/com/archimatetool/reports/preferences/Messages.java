package com.archimatetool.reports.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.reports.preferences.messages";//$NON-NLS-1$

    public static String ArchiReportsPreferencesPage_0;

    public static String ArchiReportsPreferencesPage_1;

    public static String ArchiReportsPreferencesPage_2;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
