package com.archimatetool.model.relationships;

import org.eclipse.osgi.util.NLS;

public class Messages {
    private static final String BUNDLE_NAME = "com.archimatetool.model.relationships.messages"; //$NON-NLS-1$

    public static String RelationshipManager_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}
