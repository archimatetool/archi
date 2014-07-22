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
import com.archimatetool.editor.ui.factory.ElementUIFactory;
import com.archimatetool.editor.ui.factory.IElementUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;



public class OrJunctionFigureTests extends AbstractDiagramModelObjectFigureTests {
    
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
    
    @Test
    public void testGetTextControl() {
        assertNull(abstractFigure.getTextControl());
    }

    @Test
    public void testGetDefaultSize() {
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(abstractFigure.getDiagramModelObject());
        assertEquals(provider.getDefaultSize(), abstractFigure.getDefaultSize());
    }

    @Override
    @Test
    public void testSetFillColor() {
        Color expected = new Color(null, 255, 255, 255);
        assertEquals(expected, abstractFigure.getFillColor());
        expected.dispose();
    }

}