/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.stream.Stream;

import org.eclipse.draw2d.IFigure;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelNote;


public class LegendFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(createFigure())
        );
    }

    static IFigure createFigure() {
        IDiagramModelNote dmNote = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        dmNote.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_DEFAULTS, 12, IDiagramModelNote.LEGEND_OFFSET_DEFAULT);
        dmNote.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        return addDiagramModelObjectToModelAndFindFigure(dmNote);
    }
    
    @ParamsTest
    public void testGetTextControl(LegendFigure figure) {
        assertSame(figure, figure.getTextControl());
    }

    @Override
    @ParamsTest
    public void testDidClickTextControl(AbstractDiagramModelObjectFigure figure) {
        // Not needed
        //assertTrue(figure.didClickTextControl(new Point(10, 10)));
    }
    
    @Override
    @ParamsTest
    public void testGetIconicDelegate(AbstractDiagramModelObjectFigure figure) {
        assertNull(figure.getIconicDelegate());
    }

}