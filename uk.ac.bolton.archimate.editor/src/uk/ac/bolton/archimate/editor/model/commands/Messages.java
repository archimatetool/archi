/*******************************************************************************
 * Copyright (c) 2010-12 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "uk.ac.bolton.archimate.editor.model.commands.messages"; //$NON-NLS-1$

    public static String DeleteDiagramModelCommand_0;

    public static String DeleteElementCommand_0;

    public static String DeleteFolderCommand_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
