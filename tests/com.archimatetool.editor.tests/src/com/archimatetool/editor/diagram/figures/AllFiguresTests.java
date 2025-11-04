/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.archimatetool.editor.diagram.figures.diagram.DiagramImageFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.DiagramModelReferenceFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.GroupFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.LegendFigureTests;
import com.archimatetool.editor.diagram.figures.diagram.NoteFigureTests;
import com.archimatetool.editor.diagram.figures.elements.JunctionFigureTests;

@Suite
@SelectClasses({
    // figures
    AllArchimateTextControlContainerFigureTests.class,
    // figures.diagram
    DiagramImageFigureTests.class,
    DiagramModelReferenceFigureTests.class,
    GroupFigureTests.class,
    LegendFigureTests.class,
    NoteFigureTests.class,
    // figures.elements
    JunctionFigureTests.class
})
@DisplayName("All Figures Tests")
public class AllFiguresTests {
}