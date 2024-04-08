/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;

import org.eclipse.draw2d.IFigure;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;



public class JunctionFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(createFigure())
        );
    }
    
    static IFigure createFigure() {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createJunction());
        return addDiagramModelObjectToModelAndFindFigure(dmo);
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetTextControl(JunctionFigure figure) {
        assertNull(figure.getTextControl());
    }
}