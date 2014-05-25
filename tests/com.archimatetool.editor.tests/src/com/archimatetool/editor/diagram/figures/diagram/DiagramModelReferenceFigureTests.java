/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import junit.framework.JUnit4TestAdapter;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigureTests;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelReference;



public class DiagramModelReferenceFigureTests extends AbstractTextFlowFigureTests {
    
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
        dm.getChildren().add(dmRef);
        
        // Layout
        editor.layoutPendingUpdates();
        
        figure = (DiagramModelReferenceFigure)editor.findFigure(dmRef);
        return figure;
    }
    
}