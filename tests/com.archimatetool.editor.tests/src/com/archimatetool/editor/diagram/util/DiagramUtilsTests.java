/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.editparts.ArchimateDiagramEditPartFactory;
import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactory;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.testingtools.ArchimateTestModel;

/**
 * DiagramUtilsTests
 * 
 * @author Phillip Beauvoir
 */
public class DiagramUtilsTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramUtilsTests.class);
    }
    
    private static ArchimateTestModel tm;
    private static IArchimateModel model;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_1);
        model = tm.loadModel();
    }

    @Test
    public void testCreateViewer_ArchimateModel() {
        IDiagramModel dm = model.getDiagramModels().get(0);
        assertTrue(dm instanceof IArchimateDiagramModel);
        
        Shell shell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(dm, shell);
        
        assertNotNull(viewer);
        assertTrue(viewer.getEditPartFactory() instanceof ArchimateDiagramEditPartFactory);
        assertTrue(viewer.getRootEditPart() instanceof FreeformGraphicalRootEditPart);
        assertSame(dm, viewer.getContents().getModel());
        
        assertSame(shell, viewer.getControl().getShell());
        
        shell.dispose();
    }

    @Test
    public void testCreateViewer_SketchModel() {
        IDiagramModel dm = model.getDiagramModels().get(1);
        assertTrue(dm instanceof ISketchModel);
        
        Shell shell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(dm, shell);
        
        assertNotNull(viewer);
        assertTrue(viewer.getEditPartFactory() instanceof SketchEditPartFactory);
        assertTrue(viewer.getRootEditPart() instanceof FreeformGraphicalRootEditPart);
        assertSame(dm, viewer.getContents().getModel());
        
        assertSame(shell, viewer.getControl().getShell());
        
        shell.dispose();
    }
    
    @Test
    public void testCreateImage_Model_NoChildren() {
        // This is the blank View
        IDiagramModel dm = model.getDiagramModels().get(0);
        Image img = DiagramUtils.createImage(dm, 1, 0);
        
        // Blank View is minimum 100 x 100
        assertEquals(new Rectangle(0, 0, 100, 100), img.getBounds());
        img.dispose();
        
        // Margin will not be used for Blank View
        img = DiagramUtils.createImage(dm, 1, 20);
        assertEquals(new Rectangle(0, 0, 100, 100), img.getBounds());
        img.dispose();

        // Margin & Ratio
        img = DiagramUtils.createImage(dm, 0.2, 0);
        assertEquals(new Rectangle(0, 0, 20, 20), img.getBounds());
        img.dispose();
        
        img = DiagramUtils.createImage(dm, 0.5, 10);
        assertEquals(new Rectangle(0, 0, 50, 50), img.getBounds());
        img.dispose();
    }
   
    @Test
    public void testCreateImage_Model_NoChildren_Scaled() {
        IDiagramModel dm = model.getDiagramModels().get(0);
        
        // Blank View is minimum 100 x 100
        Image img = DiagramUtils.createImage(dm, 1, 0);
        assertEquals(new Rectangle(0, 0, 100, 100), img.getBounds());
        img.dispose();
        
        img = DiagramUtils.createImage(dm, 2, 0);
        assertEquals(new Rectangle(0, 0, 200, 200), img.getBounds());
        img.dispose();
    }
    
    @Test
    public void testCreateImage_Model_Scaled() {
        IDiagramModel dm = model.getDiagramModels().get(2);
        
        int width = 720 + 193; // x of furthest object in diagram, and its width
        int height = 468 + 85; // x of furthest object in diagram, and its height
        
        Image img = DiagramUtils.createImage(dm, 1, 0);
        assertEquals(new Rectangle(0, 0, width, height), img.getBounds());
        img.dispose();
        
        img = DiagramUtils.createImage(dm, 0.5, 0);
        assertEquals(new Rectangle(0, 0, width / 2, height / 2), img.getBounds());
        img.dispose();
    }

    @Test
    public void testCreateImage_GraphicalViewer() {
        IDiagramModel dm = model.getDiagramModels().get(2);
        
        Shell shell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(dm, shell);
        
        Image img = DiagramUtils.createImage(viewer, 1, 0);
        assertNotNull(img);

        img.dispose();
        shell.dispose();
    }
    
    @Test
    public void testCreateImage_FigureWithChildren() {
        IFigure rootFigure = new FreeformLayer();
        org.eclipse.draw2d.geometry.Rectangle rect1 = new org.eclipse.draw2d.geometry.Rectangle(0, 0, 1000, 1000);
        rootFigure.setBounds(rect1);
        
        // Blank View is minimum 100 x 100
        Image img = DiagramUtils.createImage(rootFigure, 1, 0);
        assertEquals(new Rectangle(0, 0, 100, 100), img.getBounds());
        img.dispose();
        
        IFigure childFigure1 = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect2 = new org.eclipse.draw2d.geometry.Rectangle(90, 90, 230, 190);
        childFigure1.setBounds(rect2);
        rootFigure.add(childFigure1);
        
        IFigure childFigure2 = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect3 = new org.eclipse.draw2d.geometry.Rectangle(120, 150, 230, 190);
        childFigure2.setBounds(rect3);
        rootFigure.add(childFigure2);
        
        img = DiagramUtils.createImage(rootFigure, 1, 0);
        assertEquals(new Rectangle(0, 0, 260, 250), img.getBounds());
        img.dispose();
    }
    
    @Test
    public void testCreateImage_SimpleFigure() {
        IFigure figure = new Figure();
        figure.setSize(230, 190);
        Image img = DiagramUtils.createImage(figure, 1, 0);
        assertEquals(new Rectangle(0, 0, 230, 190), img.getBounds());
        img.dispose();
    }

    @Test
    public void testCreateImage_Is_Scaled() {
        IFigure rootFigure = new FreeformLayer();
        org.eclipse.draw2d.geometry.Rectangle rect1 = new org.eclipse.draw2d.geometry.Rectangle(0, 0, 1000, 1000);
        rootFigure.setBounds(rect1);
        
        IFigure childFigure1 = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect2 = new org.eclipse.draw2d.geometry.Rectangle(100, 100, 300, 200);
        childFigure1.setBounds(rect2);
        rootFigure.add(childFigure1);
        
        Image img = DiagramUtils.createImage(rootFigure, 1, 0);
        assertEquals(new Rectangle(0, 0, 300, 200), img.getBounds());
        img.dispose();
        
        img = DiagramUtils.createImage(rootFigure, 0.25, 0);
        assertEquals(new Rectangle(0, 0, 75, 50), img.getBounds());
        img.dispose();
        
        img = DiagramUtils.createImage(rootFigure, 5, 0);
        assertEquals(new Rectangle(0, 0, 1500, 1000), img.getBounds());
        img.dispose();
    }

    @Test
    public void testGetDiagram_IsMinimumSize() {
        IDiagramModel dm = model.getDiagramModels().get(0);
        
        Shell shell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(dm, shell);
        shell.dispose();
        
        org.eclipse.draw2d.geometry.Rectangle rect = DiagramUtils.getDiagramExtents(viewer);
        assertEquals(new org.eclipse.draw2d.geometry.Rectangle(0, 0, 100, 100), rect);
    }

    @Test
    public void testGetDiagramExtents() {
        IDiagramModel dm = model.getDiagramModels().get(2);
        
        int width = 720 + 193; // x of furthest object in diagram, and its width
        int height = 468 + 85; // x of furthest object in diagram, and its height
        
        Shell shell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(dm, shell);
        shell.dispose();
        
        org.eclipse.draw2d.geometry.Rectangle rect = DiagramUtils.getDiagramExtents(viewer);
        assertEquals(new org.eclipse.draw2d.geometry.Rectangle(0, 0, width, height), rect);
    }
    
    @Test
    public void testGetDiagramExtents_WithConnections() {
        IDiagramModel dm = model.getDiagramModels().get(3);
        
        Shell shell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(dm, shell);
        shell.dispose();
        
        org.eclipse.draw2d.geometry.Rectangle rect = DiagramUtils.getDiagramExtents(viewer);
        assertEquals(new org.eclipse.draw2d.geometry.Rectangle(12, 24, 587, 323), rect);
    }

    @Test
    public void testGetMinimumBounds_FreeformLayer() {
        IFigure rootFigure = new FreeformLayer();
        org.eclipse.draw2d.geometry.Rectangle rect1 = new org.eclipse.draw2d.geometry.Rectangle(0, 0, 1000, 1000);
        rootFigure.setBounds(rect1);
        assertNull(DiagramUtils.getMinimumBounds(rootFigure));
        
        IFigure childFigure = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect2 = new org.eclipse.draw2d.geometry.Rectangle(10, 10, 50, 50);
        childFigure.setBounds(rect2);
        rootFigure.add(childFigure);
        
        childFigure = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect3 = new org.eclipse.draw2d.geometry.Rectangle(200, 220, 100, 100);
        childFigure.setBounds(rect3);
        rootFigure.add(childFigure);
        
        assertEquals(new org.eclipse.draw2d.geometry.Rectangle(10, 10, 290, 310), DiagramUtils.getMinimumBounds(rootFigure));
    }
    
    @Test
    public void testGetMinimumBounds_SimpleFigure() {
        IFigure figure = new Figure();
        figure.setSize(50, 50);
        assertEquals(new org.eclipse.draw2d.geometry.Rectangle(0, 0, 50, 50), DiagramUtils.getMinimumBounds(figure));
    }

}