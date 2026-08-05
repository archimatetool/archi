/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    
    private static ArchimateTestModel tm;
    private static IArchimateModel model;
    
    @BeforeAll
    public static void runOnceBeforeAllTests() throws IOException {
        tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_1);
        model = tm.loadModel();
    }

    @Test
    public void testCreateViewer_ArchimateModel() {
        IDiagramModel dm = model.getDiagramModels().get(0);
        assertTrue(dm instanceof IArchimateDiagramModel);
        
        Shell shell = new Shell();
        try {
            GraphicalViewer viewer = DiagramUtils.createViewer(dm, shell);
            
            assertNotNull(viewer);
            assertTrue(viewer.getEditPartFactory() instanceof ArchimateDiagramEditPartFactory);
            assertTrue(viewer.getRootEditPart() instanceof FreeformGraphicalRootEditPart);
            assertSame(dm, viewer.getContents().getModel());
            
            assertSame(shell, viewer.getControl().getShell());
        }
        finally {
            shell.dispose();
        }
    }

    @Test
    public void testCreateViewer_SketchModel() {
        IDiagramModel dm = model.getDiagramModels().get(1);
        assertTrue(dm instanceof ISketchModel);
        
        Shell shell = new Shell();
        try {
            GraphicalViewer viewer = DiagramUtils.createViewer(dm, shell);

            assertNotNull(viewer);
            assertTrue(viewer.getEditPartFactory() instanceof SketchEditPartFactory);
            assertTrue(viewer.getRootEditPart() instanceof FreeformGraphicalRootEditPart);
            assertSame(dm, viewer.getContents().getModel());

            assertSame(shell, viewer.getControl().getShell());
        }
        finally {
            shell.dispose();
        }
    }
    
    @Test
    public void testCreateImage_Model_NoChildren() {
        // This is the blank View
        IDiagramModel dm = model.getDiagramModels().get(0);
        
        // Blank View is minimum 100 x 100
        Image img = DiagramUtils.createImage(dm, 1, 0);
        assertImageBounds(img, new Rectangle(0, 0, 100, 100));
        
        // Margin will not be used for Blank View
        img = DiagramUtils.createImage(dm, 1, 20);
        assertImageBounds(img, new Rectangle(0, 0, 100, 100));

        // Margin & Ratio
        img = DiagramUtils.createImage(dm, 0.2, 0);
        assertImageBounds(img, new Rectangle(0, 0, 20, 20));
        
        // 50% scale
        img = DiagramUtils.createImage(dm, 0.5, 10);
        assertImageBounds(img, new Rectangle(0, 0, 50, 50));
    }
    
    @Test
    public void testCreateImage_Model_NoChildren_Scaled() {
        IDiagramModel dm = model.getDiagramModels().get(0);
        
        // Blank View is minimum 100 x 100
        Image img = DiagramUtils.createImage(dm, 1, 0);
        assertImageBounds(img, new Rectangle(0, 0, 100, 100));
        
        // 200% scale
        img = DiagramUtils.createImage(dm, 2, 0);
        assertImageBounds(img, new Rectangle(0, 0, 200, 200));
    }
    
    @Test
    public void testCreateImage_Model_Scaled() {
        IDiagramModel dm = model.getDiagramModels().get(2);
        
        int width = 720 + 193; // x of furthest object in diagram, and its width
        int height = 468 + 85; // x of furthest object in diagram, and its height
        
        Image img = DiagramUtils.createImage(dm, 1, 0);
        assertImageBounds(img, new Rectangle(0, 0, width, height));
        
        img = DiagramUtils.createImage(dm, 0.5, 0);
        assertImageBounds(img, new Rectangle(0, 0, width / 2, height / 2));
    }

    @Test
    public void testCreateImage_GraphicalViewer() {
        IDiagramModel dm = model.getDiagramModels().get(2);
        
        Shell shell = new Shell();
        try {
            GraphicalViewer viewer = DiagramUtils.createViewer(dm, shell);
            Image img = DiagramUtils.createImage(viewer, 1, 0);
            assertNotNull(img);
            img.dispose();
        }
        finally {
            shell.dispose();
        }
    }
    
    @Test
    public void testCreateImage_FigureWithChildren() {
        IFigure rootFigure = new FreeformLayer();
        org.eclipse.draw2d.geometry.Rectangle rect1 = new org.eclipse.draw2d.geometry.Rectangle(0, 0, 1000, 1000);
        rootFigure.setBounds(rect1);
        
        // Blank View is minimum 100 x 100
        Image img = DiagramUtils.createImage(rootFigure, 1, 0);
        assertImageBounds(img, new Rectangle(0, 0, 100, 100));
        
        IFigure childFigure1 = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect2 = new org.eclipse.draw2d.geometry.Rectangle(90, 90, 230, 190);
        childFigure1.setBounds(rect2);
        rootFigure.add(childFigure1);
        
        IFigure childFigure2 = new Figure();
        org.eclipse.draw2d.geometry.Rectangle rect3 = new org.eclipse.draw2d.geometry.Rectangle(120, 150, 230, 190);
        childFigure2.setBounds(rect3);
        rootFigure.add(childFigure2);
        
        img = DiagramUtils.createImage(rootFigure, 1, 0);
        assertImageBounds(img, new Rectangle(0, 0, 260, 250));
    }
    
    @Test
    public void testCreateImage_SimpleFigure() {
        IFigure figure = new Figure();
        figure.setSize(230, 190);

        Image img = DiagramUtils.createImage(figure, 1, 0);
        assertImageBounds(img, new Rectangle(0, 0, 230, 190));
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
        assertImageBounds(img, new Rectangle(0, 0, 300, 200));
        
        img = DiagramUtils.createImage(rootFigure, 0.25, 0);
        assertImageBounds(img, new Rectangle(0, 0, 75, 50));
        
        img = DiagramUtils.createImage(rootFigure, 5, 0);
        assertImageBounds(img, new Rectangle(0, 0, 1500, 1000));
    }

    @Test
    public void testGetDiagram_IsMinimumSize() {
        Shell shell = new Shell();
        try {
            IDiagramModel dm = model.getDiagramModels().get(0);
            
            GraphicalViewer viewer = DiagramUtils.createViewer(dm, shell);
            org.eclipse.draw2d.geometry.Rectangle rect = DiagramUtils.getDiagramExtents(viewer);
            assertEquals(new org.eclipse.draw2d.geometry.Rectangle(0, 0, 100, 100), rect);
        }
        finally {
            shell.dispose();
        }
    }

    @Test
    public void testGetDiagramExtents() {
        Shell shell = new Shell();
        try {
            IDiagramModel dm = model.getDiagramModels().get(2);
            
            int width = 720 + 193; // x of furthest object in diagram, and its width
            int height = 468 + 85; // x of furthest object in diagram, and its height
            
            GraphicalViewer viewer = DiagramUtils.createViewer(dm, shell);
            org.eclipse.draw2d.geometry.Rectangle rect = DiagramUtils.getDiagramExtents(viewer);
            assertEquals(new org.eclipse.draw2d.geometry.Rectangle(0, 0, width, height), rect);
        }
        finally {
            shell.dispose();
        }
    }
    
    @Test
    public void testGetDiagramExtents_WithConnections() {
        Shell shell = new Shell();
        try {
            IDiagramModel dm = model.getDiagramModels().get(3);
            GraphicalViewer viewer = DiagramUtils.createViewer(dm, shell);
            org.eclipse.draw2d.geometry.Rectangle rect = DiagramUtils.getDiagramExtents(viewer);
            assertEquals(new org.eclipse.draw2d.geometry.Rectangle(12, 24, 587, 323), rect);
        }
        finally {
            shell.dispose();
        }
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

    /**
     * Assert Image is not null and its bounds is equal to rect and dispose the image
     */
    private void assertImageBounds(Image img, Rectangle rect) {
        try {
            assertNotNull(img);
            assertEquals(rect, img.getBounds());
        }
        finally {
            if(img != null) {
                img.dispose();
            }
        }
    }
}