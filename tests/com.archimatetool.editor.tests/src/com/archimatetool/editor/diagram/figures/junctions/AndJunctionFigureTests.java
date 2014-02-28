/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.junctions;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;



public class AndJunctionFigureTests extends OrJunctionFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AndJunctionFigureTests.class);
    }
    

    @Override
    protected AndJunctionFigure createFigure() {
        // Add a DiagramModelObject
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createAndJunction());
        dm.getChildren().add(dmo);
        
        figure = (AndJunctionFigure)editorHandler.findFigure(dmo);
        
        return (AndJunctionFigure)figure;
    }
    
    @Override
    @Test
    public void testSetFillColor() {
        Color expected = new Color(null, 0, 0, 0);
        assertEquals(expected, figure.getFillColor());
        expected.dispose();
    }

}