/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import junit.framework.TestSuite;

import com.archimatetool.editor.diagram.actions.CopySnapshotTests;
import com.archimatetool.editor.diagram.figures.AllFiguresTests;
import com.archimatetool.editor.diagram.policies.snaptogrid.ExtendedConnectionBendpointTrackerTests;
import com.archimatetool.editor.diagram.tools.FormatPainterInfoTests;
import com.archimatetool.editor.diagram.tools.FormatPainterToolTests;
import com.archimatetool.editor.diagram.util.DiagramUtilsTests;

@SuppressWarnings("nls")
public class AllDiagramTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram");
		
        // diagram
        suite.addTest(ImageExportProviderTests.suite());
        suite.addTest(ImageExportProviderManagerTests.suite());

        // diagram.actions
        suite.addTest(CopySnapshotTests.suite());
        
        // diagram.figures
        suite.addTest(AllFiguresTests.suite());

        // diagram.policies.snaptogrid
        suite.addTest(ExtendedConnectionBendpointTrackerTests.suite());
        
        // diagram.tools
        suite.addTest(FormatPainterInfoTests.suite());
		suite.addTest(FormatPainterToolTests.suite());

        // diagram.util
        suite.addTest(DiagramUtilsTests.suite());

        return suite;
	}

}