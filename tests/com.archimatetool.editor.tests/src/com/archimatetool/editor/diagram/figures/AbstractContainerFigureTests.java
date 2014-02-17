/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public abstract class AbstractContainerFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    protected AbstractContainerFigure containerFigure;
    
    @Before
    public void runBeforeEachTest() {
        containerFigure = (AbstractContainerFigure)abstractFigure;
    }

    
    @Test
    public void testGetContentPane() {
        assertNotNull(containerFigure.getContentPane());
    }
    
    @Test
    public void testTranslateMousePointToRelative() {
        // @TODO
    }
    
    @Test
    public void testGetPreferredSize() {
        // @TODO
    }
    
    @Test
    public void testUseLocalCoordinates() {
        assertTrue(containerFigure.useLocalCoordinates());
    }

}
