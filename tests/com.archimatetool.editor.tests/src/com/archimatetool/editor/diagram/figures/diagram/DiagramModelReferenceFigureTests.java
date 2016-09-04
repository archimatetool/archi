/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import junit.framework.JUnit4TestAdapter;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigureTests;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelReference;



@SuppressWarnings("nls")
public class DiagramModelReferenceFigureTests extends AbstractTextControlContainerFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelReferenceFigureTests.class);
    }
    
    private DiagramModelReferenceFigure figure;
    private IDiagramModelReference dmRef;
    

    @Override
    protected DiagramModelReferenceFigure createFigure() {
        dmRef = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        dm = (IArchimateDiagramModel)model.getDefaultDiagramModel();
        dmRef.setReferencedModel(dm);
        dmRef.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmRef.setName("Hello World"); // Need to do this for text control tests
        dm.getChildren().add(dmRef);
        
        // Layout
        editor.layoutPendingUpdates();
        
        figure = (DiagramModelReferenceFigure)editor.findFigure(dmRef);
        return figure;
    }
    
}