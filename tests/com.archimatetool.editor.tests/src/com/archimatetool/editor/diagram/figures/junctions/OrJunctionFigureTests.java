/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.junctions;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



public class OrJunctionFigureTests extends AndJunctionFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OrJunctionFigureTests.class);
    }
    
    @Override
    protected OrJunctionFigure createFigure() {
        // Add a DiagramModelObject
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createOrJunction());
        dm.getChildren().add(dmo);
        
        // Layout
        editor.layoutPendingUpdates();
        
        return (OrJunctionFigure)editor.findFigure(dmo);
    }
    
    @Override
    @Test
    public void testSetFillColor() {
        Color expected = new Color(null, 0, 0, 0);
        assertEquals(expected, abstractFigure.getFillColor());
        expected.dispose();
    }

}