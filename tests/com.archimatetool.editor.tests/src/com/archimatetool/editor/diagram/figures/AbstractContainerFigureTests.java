/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.ParamsTest;


public abstract class AbstractContainerFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    @ParamsTest
    public void testGetMainFigure(AbstractContainerFigure figure) {
        assertNotNull(figure.getMainFigure());
    }
    
    @ParamsTest
    public void testGetContentPane(AbstractContainerFigure figure) {
        assertNotNull(figure.getContentPane());
    }
    
    @ParamsTest
    public void testTranslateMousePointToRelative(AbstractContainerFigure figure) {
        figure.setLocation(new Point(100, 100));
        
        Point pt = new Point(300, 400);
        figure.translateMousePointToRelative(pt);
        assertEquals(new Point(200, 300), pt);
    }
    
    @ParamsTest
    public void testUseLocalCoordinates(AbstractContainerFigure figure) {
        assertTrue(figure.useLocalCoordinates());
    }

    @ParamsTest
    public void testGetPreferredSize(AbstractContainerFigure figure) {
        assertEquals(figure.getDefaultSize(), figure.getPreferredSize());
        
        IFigure childFigure1 = new Figure();
        childFigure1.setBounds(new Rectangle(400, 400, 200, 200));
        figure.getContentPane().add(childFigure1);
        
        assertEquals(new Dimension(610, 610), figure.getPreferredSize());

        IFigure childFigure2 = new Figure();
        childFigure2.setBounds(new Rectangle(0, 600, 200, 200));
        figure.getContentPane().add(childFigure2);

        assertEquals(new Dimension(610, 810), figure.getPreferredSize());

        IFigure childFigure3 = new Figure();
        childFigure3.setBounds(new Rectangle(600, 0, 200, 200));
        figure.getContentPane().add(childFigure3);

        assertEquals(new Dimension(810, 810), figure.getPreferredSize());
    }
}
