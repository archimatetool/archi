/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;



public abstract class AbstractTextFlowFigureTests extends AbstractContainerFigureTests {
    
    protected AbstractTextFlowFigure textFlowFigure;
    
    @Before
    public void runBeforeEachTextFlowTest() {
        textFlowFigure = (AbstractTextFlowFigure)abstractFigure;
    }

    
    @Test
    public void testSetEnabled() {
        // @TODO
    }
    
    @Test
    public void testSetText() {
        // @TODO
    }

    @Test
    public void testGetTextControl() {
        assertNotNull(textFlowFigure.getTextControl());
    }

    
    
}
