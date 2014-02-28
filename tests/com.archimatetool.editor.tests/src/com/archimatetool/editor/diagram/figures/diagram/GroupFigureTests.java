/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Test;

import com.archimatetool.editor.diagram.figures.AbstractLabelContainerFigureTests;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.tests.AsyncTestRunner;



@SuppressWarnings("nls")
public class GroupFigureTests extends AbstractLabelContainerFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GroupFigureTests.class);
    }
    
    private GroupFigure figure;
    private IDiagramModelGroup dmGroup;
    

    @Override
    protected GroupFigure createFigure() {
        dmGroup = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmGroup.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmGroup.setName("Group Test");
        dm.getChildren().add(dmGroup);
        
        figure = (GroupFigure)editorHandler.findFigure(dmGroup);
        return figure;
    }
    
    @Test
    public void testGetDefaultSize() {
        assertEquals(GroupFigure.DEFAULT_SIZE, figure.getDefaultSize());
    }

    @Override
    @Test
    public void testGetPreferredSize() {
        Dimension d = containerFigure.getPreferredSize();
        assertEquals(containerFigure.getDefaultSize(), d);
        
        IFigure childFigure1 = new Figure();
        childFigure1.setBounds(new Rectangle(0, 0, 200, 200));
        containerFigure.getContentPane().add(childFigure1);
        
        d = containerFigure.getPreferredSize();
        assertEquals(new Dimension(400, 210), d);

        IFigure childFigure2 = new Figure();
        childFigure2.setBounds(new Rectangle(-12, -12, 300, 256));
        containerFigure.getContentPane().add(childFigure2);

        d = containerFigure.getPreferredSize();
        assertEquals(new Dimension(400, 254), d);

        IFigure childFigure3 = new Figure();
        childFigure3.setBounds(new Rectangle(20, 20, 300, 256));
        containerFigure.getContentPane().add(childFigure3);

        d = containerFigure.getPreferredSize();
        assertEquals(new Dimension(400, 286), d);
    }

    @Override
    @Test
    public void testDidClickTestControl() {
        AsyncTestRunner runner = new AsyncTestRunner() {
            @Override
            public void run() {
                super.run();
                assertTrue(abstractFigure.didClickTextControl(new Point(6, 4)));
            }
        };
        runner.start();
    }

}