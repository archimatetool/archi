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
import com.archimatetool.model.IDiagramModelGroup;



@SuppressWarnings("nls")
public class GroupFigureTests extends AbstractTextControlContainerFigureTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(createFigure())
        );
    }

    static IFigure createFigure() {
        IDiagramModelGroup dmGroup = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmGroup.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmGroup.setName("Group Test");
        return addDiagramModelObjectToModelAndFindFigure(dmGroup);
    }
}