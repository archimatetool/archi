package com.archimatetool.export.svg;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.export.svg.messages"; //$NON-NLS-1$

    public static String SVGExportProvider_0;

    public static String SVGExportProvider_1;

    public static String SVGExportProvider_2;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
