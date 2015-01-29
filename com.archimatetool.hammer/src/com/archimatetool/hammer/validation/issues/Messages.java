package com.archimatetool.hammer.validation.issues;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.hammer.validation.issues.messages"; //$NON-NLS-1$

    public static String AdviceCategory_0;

    public static String ErrorsCategory_0;

    public static String OKType_0;

    public static String OKType_1;

    public static String OKType_2;

    public static String WarningsCategory_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
