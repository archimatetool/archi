/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import junit.framework.TestSuite;

import com.archimatetool.editor.diagram.actions.CopySnapshotTests;
import com.archimatetool.editor.diagram.tools.FormatPainterInfoTests;
import com.archimatetool.editor.diagram.tools.FormatPainterToolTests;
import com.archimatetool.editor.model.DiagramModelUtilsTests;
import com.archimatetool.editor.model.commands.CommandsTests;
import com.archimatetool.editor.model.impl.ByteArrayStorageTests;
import com.archimatetool.editor.model.impl.EditorModelManagerTests;
import com.archimatetool.editor.utils.FileUtilsTests;
import com.archimatetool.editor.utils.HTMLUtilsTests;
import com.archimatetool.editor.utils.StringUtilsTests;
import com.archimatetool.editor.utils.ZipUtilsTests;
import com.archimatetool.editor.views.tree.TreeModelViewerDragDropHandlerTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor");
		
        // diagram.actions
        suite.addTest(CopySnapshotTests.suite());

        // diagram.tools
        suite.addTest(FormatPainterInfoTests.suite());
		suite.addTest(FormatPainterToolTests.suite());

		// model
        suite.addTest(DiagramModelUtilsTests.suite());

        // model.commands
        suite.addTest(CommandsTests.suite());

        // model.impl
        suite.addTest(ByteArrayStorageTests.suite());
		suite.addTest(EditorModelManagerTests.suite());
		
        // utils
        suite.addTest(FileUtilsTests.suite());
        suite.addTest(HTMLUtilsTests.suite());
        suite.addTest(StringUtilsTests.suite());
        suite.addTest(ZipUtilsTests.suite());

        // views
        suite.addTest(TreeModelViewerDragDropHandlerTests.suite());

        return suite;
	}

}