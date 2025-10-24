/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;

import org.eclipse.draw2d.IFigure;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
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
    
    @ParamsTest
    public void testGetTextControl(JunctionFigure figure) {
        assertNull(figure.getTextControl());
    }
    
    @Override
    @ParamsTest
    public void testGetIconicDelegate(AbstractDiagramModelObjectFigure figure) {
        assertNull(figure.getIconicDelegate());
    }

}