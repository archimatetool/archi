/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import com.archimatetool.editor.diagram.figures.diagram.DiagramImageFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.DiagramModelReferenceFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.GroupFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.NoteFigureTests;
import com.archimatetool.editor.diagram.figures.elements.JunctionFigureTests;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllFiguresTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram.figures");
		
        // figures
        suite.addTest(AllArchimateTextControlContainerFigureTests.suite());
        
        // figures.diagram
        suite.addTest(DiagramImageFigureTests.suite());
        suite.addTest(DiagramModelReferenceFigureTests.suite());
        suite.addTest(GroupFigureTests.suite());
        suite.addTest(NoteFigureTests.suite());

        // figures.elements
        suite.addTest(JunctionFigureTests.suite());


        return suite;
	}

}