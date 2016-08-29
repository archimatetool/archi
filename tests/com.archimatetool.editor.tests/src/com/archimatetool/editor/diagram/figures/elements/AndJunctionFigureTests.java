/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



public class AndJunctionFigureTests extends AbstractDiagramModelObjectFigureTests {
    
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
        
        // Layout
        editor.layoutPendingUpdates();
        
        return (AndJunctionFigure)editor.findFigure(dmo);
    }
    
    @Test
    public void testGetTextControl() {
        assertNull(abstractFigure.getTextControl());
    }

    @Test
    public void testGetDefaultSize() {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(abstractFigure.getDiagramModelObject());
        assertEquals(provider.getDefaultSize(), abstractFigure.getDefaultSize());
    }

    @Override
    @Test
    public void testSetFillColor() {
        Color expected = new Color(null, 0, 0, 0);
        assertEquals(expected, abstractFigure.getFillColor());
        expected.dispose();
    }

}