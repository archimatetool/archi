/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


@SuppressWarnings("nls")
public abstract class AbstractTextControlContainerFigureTests extends AbstractContainerFigureTests {
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testSetEnabled(AbstractTextControlContainerFigure figure) {
        assertTrue(figure.isEnabled());
        assertTrue(figure.getTextControl().isEnabled());
        
        figure.setEnabled(false);
        
        assertFalse(figure.isEnabled());
        assertFalse(figure.getTextControl().isEnabled());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetText(AbstractTextControlContainerFigure figure) {
        assertEquals(figure.getDiagramModelObject().getName(), figure.getText());
        figure.getDiagramModelObject().setName("Fido");
        assertEquals("Fido", figure.getText());
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetTextControl(AbstractTextControlContainerFigure figure) {
        assertNotNull(figure.getTextControl());
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testDidClickTextControl(AbstractDiagramModelObjectFigure figure) {
        Rectangle bounds = figure.getTextControl().getBounds().getCopy();
        figure.getTextControl().translateToAbsolute(bounds);
        assertTrue(figure.didClickTextControl(new Point(bounds.x + 10, bounds.y + 5)));
    }
    
}
