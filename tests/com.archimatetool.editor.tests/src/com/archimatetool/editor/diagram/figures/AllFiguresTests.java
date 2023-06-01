/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.diagram.figures.diagram.DiagramImageFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.DiagramModelReferenceFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.GroupFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.NoteFigureTests;
import com.archimatetool.editor.diagram.figures.elements.JunctionFigureTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    // figures
    AllArchimateTextControlContainerFigureTests.class,
    // figures.diagram
    DiagramImageFigureTests.class,
    DiagramModelReferenceFigureTests.class,
    GroupFigureTests.class,
    NoteFigureTests.class,
    // figures.elements
    JunctionFigureTests.class
})

public class AllFiguresTests {
}