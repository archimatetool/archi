package com.archimatetool.editor.diagram.editparts;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.diagram.editparts.messages"; //$NON-NLS-1$

    public static String DiagramConnectionEditPart_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
