/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.editor.views.tree.messages"; //$NON-NLS-1$

    public static String ITreeModelView_0;

    public static String TreeModelView_0;

    public static String TreeModelView_1;

    public static String TreeModelView_2;

    public static String TreeModelViewerDragDropHandler_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
