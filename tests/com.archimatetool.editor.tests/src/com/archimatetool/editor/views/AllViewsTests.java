/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;

import junit.framework.TestSuite;

import com.archimatetool.editor.views.tree.TreeModelViewerDragDropHandlerTests;
import com.archimatetool.editor.views.tree.TreeModelViewerFindReplaceProviderTests;
import com.archimatetool.editor.views.tree.commands.DeleteCommandHandlerTests;

@SuppressWarnings("nls")
public class AllViewsTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.views");
		
        // views.tree
        suite.addTest(TreeModelViewerDragDropHandlerTests.suite());
        suite.addTest(TreeModelViewerFindReplaceProviderTests.suite());

        // views.tree.commands
        suite.addTest(DeleteCommandHandlerTests.suite());

        return suite;
	}

}