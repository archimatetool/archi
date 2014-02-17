/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.GraphicalEditPart;
import org.junit.Test;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelNote;



public class NoteFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NoteFigureTests.class);
    }
    
    private NoteFigure figure;
    private IDiagramModelNote dmNote;
    

    @Override
    protected NoteFigure getFigure() {
        // Add a DiagramModelNote
        dmNote = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        dmNote.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dm.getChildren().add(dmNote);
        
        // Get the EditPart and Figure
        GraphicalEditPart editPart = (GraphicalEditPart)editor.getGraphicalViewer().getEditPartRegistry().get(dmNote);
        figure = (NoteFigure)editPart.getFigure();
        
        return figure;
    }
    
    @Test
    public void testGetTextControl() {
        assertNotNull(figure.getTextControl());
    }

}