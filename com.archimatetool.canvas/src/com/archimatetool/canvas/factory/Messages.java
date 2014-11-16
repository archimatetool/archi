package com.archimatetool.canvas.factory;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.canvas.factory.messages"; //$NON-NLS-1$

    public static String CanvasBlockUIProvider_0;

    public static String CanvasLineConnectionUIProvider_0;

    public static String CanvasModelUIProvider_0;

    public static String CanvasStickyUIProvider_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
