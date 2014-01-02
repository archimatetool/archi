/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.views.tree.commands.messages"; //$NON-NLS-1$

    public static String DeleteElementsCompoundCommand_0;

    public static String DuplicateCommandHandler_1;

    public static String DuplicateCommandHandler_2;

    public static String DuplicateCommandHandler_3;

    public static String DuplicateCommandHandler_4;

    public static String MoveFolderCommand_0;

    public static String MoveObjectCommand_0;

    public static String NewElementCommand_0;

    public static String NewFolderCommand_0;

    public static String RenameCommandHandler_0;

    public static String SortFolderCommand_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
