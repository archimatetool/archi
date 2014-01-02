/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.technology;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.ui.factory.technology.messages"; //$NON-NLS-1$

    public static String TechnologyArtifactUIProvider_0;

    public static String TechnologyCommunicationPathUIProvider_0;

    public static String TechnologyDeviceUIProvider_0;

    public static String TechnologyInfrastructureFunctionUIProvider_0;

    public static String TechnologyInfrastructureInterfaceUIProvider_0;

    public static String TechnologyInfrastructureServiceUIProvider_0;

    public static String TechnologyNetworkUIProvider_0;

    public static String TechnologyNodeUIProvider_0;

    public static String TechnologySystemSoftwareUIProvider_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
