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

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("nls")
public abstract class AbstractTextFlowFigureTests extends AbstractContainerFigureTests {
    
    protected AbstractTextFlowFigure textFlowFigure;
    
    @Before
    public void runBeforeEachTextFlowTest() {
        textFlowFigure = (AbstractTextFlowFigure)abstractFigure;
    }

    
    @Test
    public void testSetEnabled() {
        assertTrue(textFlowFigure.isEnabled());
        assertTrue(textFlowFigure.getTextControl().isEnabled());
        
        textFlowFigure.setEnabled(false);
        
        assertFalse(textFlowFigure.isEnabled());
        assertFalse(textFlowFigure.getTextControl().isEnabled());
    }
    
    @Test
    public void testSetText() {
        assertEquals(diagramModelObject.getName(), textFlowFigure.getTextControl().getText());
        diagramModelObject.setName("Fido");
        assertEquals("Fido", diagramModelObject.getName());
        assertEquals(diagramModelObject.getName(), textFlowFigure.getTextControl().getText());
    }

    @Test
    public void testGetTextControl() {
        assertNotNull(textFlowFigure.getTextControl());
    }

    
    
}
