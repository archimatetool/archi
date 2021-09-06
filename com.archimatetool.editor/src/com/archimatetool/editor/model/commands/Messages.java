/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.model.commands.messages"; //$NON-NLS-1$

    public static String DeleteDiagramModelCommand_0;

    public static String DeleteElementCommand_0;

    public static String DeleteFolderCommand_0;

    public static String SetProfileCommand_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
