/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import junit.framework.TestSuite;

import com.archimatetool.editor.diagram.actions.CopySnapshotTests;
import com.archimatetool.editor.diagram.actions.SelectAllActionTests;
import com.archimatetool.editor.diagram.commands.AllCommandsTests;
import com.archimatetool.editor.diagram.editparts.AllEditPartsTests;
import com.archimatetool.editor.diagram.figures.AllFiguresTests;
import com.archimatetool.editor.diagram.policies.AllPoliciesTests;
import com.archimatetool.editor.diagram.sketch.AllSketchTests;
import com.archimatetool.editor.diagram.tools.FormatPainterInfoTests;
import com.archimatetool.editor.diagram.tools.FormatPainterToolTests;
import com.archimatetool.editor.diagram.util.DiagramUtilsTests;

@SuppressWarnings("nls")
public class AllDiagramTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram");
		
        // diagram
		suite.addTest(ArchimateDiagramModelFactoryTests.suite());
        suite.addTest(DiagramEditorFindReplaceProviderTests.suite());
        suite.addTest(ImageExportProviderTests.suite());
        suite.addTest(ImageExportProviderManagerTests.suite());

        // diagram.actions
        suite.addTest(CopySnapshotTests.suite());
        suite.addTest(SelectAllActionTests.suite());
        
        // diagram.commands
        suite.addTest(AllCommandsTests.suite());

        // diagram.editparts
        suite.addTest(AllEditPartsTests.suite());
        
        // diagram.figures
        suite.addTest(AllFiguresTests.suite());

        // diagram.policies
        suite.addTest(AllPoliciesTests.suite());

        // diagram.sketch
        suite.addTest(AllSketchTests.suite());

        // diagram.tools
        suite.addTest(FormatPainterInfoTests.suite());
		suite.addTest(FormatPainterToolTests.suite());

        // diagram.util
        suite.addTest(DiagramUtilsTests.suite());

        return suite;
	}

}