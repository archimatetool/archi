/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.hints;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.help.hints.messages"; //$NON-NLS-1$

    public static String HintsView_0;

    public static String HintsView_1;

    public static String HintsView_2;

    public static String HintsView_3;

    public static String HintsView_4;

    public static String IHintsView_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
