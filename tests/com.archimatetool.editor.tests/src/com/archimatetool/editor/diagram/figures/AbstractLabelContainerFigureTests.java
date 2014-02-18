/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.Label;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("nls")
public abstract class AbstractLabelContainerFigureTests extends AbstractContainerFigureTests {
    
    protected AbstractLabelContainerFigure labelContainerFigure;
    
    @Before
    public void runBeforeEachTextFlowTest() {
        labelContainerFigure = (AbstractLabelContainerFigure)abstractFigure;
    }

    
    @Test
    public void testSetText() {
        assertEquals(diagramModelObject.getName(), labelContainerFigure.getLabel().getText());
        diagramModelObject.setName("Fido");
        assertEquals("Fido", diagramModelObject.getName());
        assertEquals(diagramModelObject.getName(), labelContainerFigure.getLabel().getText());
    }

    @Test
    public void testGetLabel() {
        assertNotNull(labelContainerFigure.getLabel());
    }

    @Test
    public void testGetTextControl() {
        assertTrue(labelContainerFigure.getTextControl() instanceof Label);
    }
    
}
