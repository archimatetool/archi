/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelNote;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class NoteFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NoteFigureTests.class);
    }
    
    private NoteFigure figure;
    private IDiagramModelNote dmNote;
    

    @Override
    protected NoteFigure createFigure() {
        // Add a DiagramModelNote
        dmNote = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        dmNote.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmNote.setContent("Note Test");
        dm.getChildren().add(dmNote);
        
        editor.layoutPendingUpdates();
        
        figure = (NoteFigure)editor.findFigure(dmNote);
        return figure;
    }
    
    @Test
    public void testGetDefaultSize() {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(figure.getDiagramModelObject());
        Dimension defaultSize = provider.getDefaultSize();

        assertEquals(defaultSize, figure.getDefaultSize());
    }

    @Test
    public void testGetTextControl() {
        assertNotNull(figure.getTextControl());
    }

    @Override
    @Test
    public void testDidClickTestControl() {
        assertTrue(abstractFigure.didClickTextControl(new Point(10, 10)));
    }
}