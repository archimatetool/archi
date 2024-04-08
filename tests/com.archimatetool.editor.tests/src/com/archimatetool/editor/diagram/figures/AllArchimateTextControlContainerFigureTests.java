/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EClass;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;

@SuppressWarnings("nls")
public class AllArchimateTextControlContainerFigureTests extends AbstractTextControlContainerFigureTests {
    
    static Stream<Arguments> getParams() {
        List<Arguments> list = new ArrayList<>();
        
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            list.add(getParam(createFigure(eClass)));
        }
        
        return list.stream();
    }
    
    static IFigure createFigure(EClass eClass) {
        IDiagramModelArchimateObject dmo =
                ArchimateTestModel.createDiagramModelArchimateObject((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setName("Hello World!");
        return addDiagramModelObjectToModelAndFindFigure(dmo);
    }
    
}
