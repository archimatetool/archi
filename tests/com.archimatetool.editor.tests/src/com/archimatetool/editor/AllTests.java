/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import junit.framework.TestSuite;

import com.archimatetool.editor.model.impl.EditorModelManagerTests;
import com.archimatetool.editor.utils.FileUtilsTests;
import com.archimatetool.editor.utils.HTMLUtilsTests;
import com.archimatetool.editor.utils.StringUtilsTests;
import com.archimatetool.editor.views.tree.TreeModelViewerDragDropHandlerTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor");

		// model
		suite.addTest(EditorModelManagerTests.suite());
		
        // utils
        suite.addTest(FileUtilsTests.suite());
        suite.addTest(HTMLUtilsTests.suite());
        suite.addTest(StringUtilsTests.suite());

        // views
        suite.addTest(TreeModelViewerDragDropHandlerTests.suite());

        return suite;
	}

}