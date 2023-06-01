/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;



public class JunctionFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    @Override
    protected JunctionFigure createFigure() {
        // Add a DiagramModelObject
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createJunction());
        dm.getChildren().add(dmo);
        
        // Layout
        editor.layoutPendingUpdates();
        
        return (JunctionFigure)editor.findFigure(dmo);
    }
    
    @Test
    public void testGetTextControl() {
        assertNull(abstractFigure.getTextControl());
    }
}