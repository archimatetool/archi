/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.junctions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;



public class JunctionFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JunctionFigureTests.class);
    }
    
    private JunctionFigure figure;
    

    @Override
    protected JunctionFigure createFigure() {
        // Add a DiagramModelObject
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createJunction());
        dm.getChildren().add(dmo);
        
        figure = (JunctionFigure)editorHandler.findFigure(dmo);
        return figure;
    }
    
    @Test
    public void testGetDefaultSize() {
        assertEquals(JunctionFigure.SIZE, figure.getDefaultSize());
    }

    @Test
    public void testGetTextControl() {
        assertNull(figure.getTextControl());
    }

    @Override
    @Test
    public void testSetFillColor() {
        Color expected = new Color(null, 0, 0, 0);
        assertEquals(expected, abstractFigure.getFillColor());
        expected.dispose();
    }

}