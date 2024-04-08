/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ITextAlignment;


@SuppressWarnings("nls")
public class NoteFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(createFigure())
        );
    }

    static IFigure createFigure() {
        IDiagramModelNote dmNote = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        dmNote.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmNote.setContent("Note Test");
        dmNote.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        return addDiagramModelObjectToModelAndFindFigure(dmNote);
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetTextControl(NoteFigure figure) {
        assertNotNull(figure.getTextControl());
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testDidClickTextControl(AbstractDiagramModelObjectFigure figure) {
        assertTrue(figure.didClickTextControl(new Point(10, 10)));
    }
}