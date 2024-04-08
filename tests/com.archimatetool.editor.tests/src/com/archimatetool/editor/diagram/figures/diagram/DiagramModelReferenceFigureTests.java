/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import java.util.stream.Stream;

import org.eclipse.draw2d.IFigure;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelReference;



@SuppressWarnings("nls")
public class DiagramModelReferenceFigureTests extends AbstractTextControlContainerFigureTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(createFigure())
        );
    }

    static IFigure createFigure() {
        IDiagramModelReference dmRef = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        dmRef.setReferencedModel(editor.getDiagramModel());
        dmRef.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmRef.setName("Hello World"); // Need to do this for text control tests
        return addDiagramModelObjectToModelAndFindFigure(dmRef);
    }
    
}