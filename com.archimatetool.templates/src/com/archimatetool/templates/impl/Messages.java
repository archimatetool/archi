package com.archimatetool.templates.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.templates.impl.messages"; //$NON-NLS-1$

    public static String NewArchimateModelFromTemplateHandler_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
