/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("nls")
public abstract class AbstractTextControlContainerFigureTests extends AbstractContainerFigureTests {
    
    protected AbstractTextControlContainerFigure figure;
    
    @Before
    public void runBeforeEachTextFlowTest() {
        figure = (AbstractTextControlContainerFigure)abstractFigure;
    }

    @Test
    public void testSetEnabled() {
        assertTrue(figure.isEnabled());
        assertTrue(figure.getTextControl().isEnabled());
        
        figure.setEnabled(false);
        
        assertFalse(figure.isEnabled());
        assertFalse(figure.getTextControl().isEnabled());
    }
    
    @Test
    public void testGetText() {
        assertEquals(diagramModelObject.getName(), figure.getText());
        diagramModelObject.setName("Fido");
        assertEquals("Fido", figure.getText());
    }

    @Test
    public void testGetTextControl() {
        assertNotNull(figure.getTextControl());
    }
    
    @Override
    @Test
    public void testDidClickTextControl() {
        Rectangle bounds = figure.getTextControl().getBounds().getCopy();
        figure.getTextControl().translateToAbsolute(bounds);
        assertTrue(figure.didClickTextControl(new Point(bounds.x + 10, bounds.y + 5)));
    }
    
}
