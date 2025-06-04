package com.archimatetool.csv;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.csv.messages"; //$NON-NLS-1$

    public static String CSVConstants_0;
    public static String CSVConstants_1;
    public static String CSVConstants_2;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
