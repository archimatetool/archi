package com.archimatetool.editor.ui.factory.model;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.ui.factory.model.messages"; //$NON-NLS-1$

    public static String ArchimateModelUIProvider_0;

    public static String FolderUIProvider_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
