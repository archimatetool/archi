package com.archimatetool.editor.ui.dialog;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.ui.dialog.messages"; //$NON-NLS-1$

    public static String RelationshipsMatrixDialog_0;

    public static String RelationshipsMatrixDialog_1;

    public static String RelationshipsMatrixDialog_2;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
