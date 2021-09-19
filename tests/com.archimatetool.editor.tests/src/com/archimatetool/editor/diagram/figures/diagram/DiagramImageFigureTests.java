/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;



@SuppressWarnings("nls")
public class DiagramImageFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramImageFigureTests.class);
    }
    
    private DiagramImageFigure figure;
    private IDiagramModelImage dmImage;
    

    @Override
    protected DiagramImageFigure createFigure() {
        // Add a DiagramModelImage
        dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dmImage.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dm = (IArchimateDiagramModel)model.getDefaultDiagramModel();
        dm.getChildren().add(dmImage);
        
        // Layout
        editor.layoutPendingUpdates();

        figure = (DiagramImageFigure)editor.findFigure(dmImage);
        return figure;
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(figure.getDiagramModelObject());
        Dimension defaultSize = provider.getDefaultSize();
        
        assertEquals(defaultSize, figure.getDefaultSize());
        
        // Add image
        try {
            File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img1.png");
            addImage(file);
            Image image = getPrivateImageField();
            assertEquals(new Dimension(image), figure.getDefaultSize());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Reset
        dmImage.setImagePath(null);
        assertEquals(defaultSize, figure.getDefaultSize());
    }

    @Test
    public void testBorderColor() {
        assertNull(figure.getBorderColor());
        
        dmImage.setBorderColor("#010203");
        Color expected = new Color(1, 2, 3);
        assertEquals(expected, figure.getBorderColor());
    }
    
    @Test
    public void testDiagramImageScaled() throws Exception {
        // Have to set this to true to use scaling
        figure.useScaledImage = true;
        
        Image image = getPrivateImageField();
        assertNull(image);
        
        // Add image
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img1.png");
        addImage(file);
        
        // Check initial Image size
        image = getPrivateImageField();
        assertEquals(new Rectangle(0, 0, 1024, 1024), image.getBounds());
        
        // Check correct default size of image
        assertEquals(new Dimension(1024, 1024), figure.getDefaultSize());
        assertEquals(new Dimension(1024, 1024), figure.getPreferredSize(-1, -1));
        
        // Change size of DiagramModelImage and check it was rescaled
        IBounds bounds = IArchimateFactory.eINSTANCE.createBounds(0, 0, 512, 512);
        dmImage.setBounds(bounds);
        
        // Layout
        editor.layoutPendingUpdates();
        
        // Force a mock repaint since we are not using a GUI
        figure.paint(mock(Graphics.class));

        // Test image was rescaled
        image = getPrivateImageField();
        assertEquals(new Rectangle(0, 0, 512, 512), image.getBounds());
    }
    
    @Test
    public void testSettingNewImagePath() throws Exception {
        Image image = getPrivateImageField();
        assertNull(image);
        
        // Add image
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img1.png");
        addImage(file);
        
        // Check initial Image size
        image = getPrivateImageField();
        assertEquals(new Rectangle(0, 0, 1024, 1024), image.getBounds());
        
        // Check correct default size of image
        assertEquals(new Dimension(1024, 1024), figure.getDefaultSize());
        assertEquals(new Dimension(1024, 1024), figure.getPreferredSize(-1, -1));

        // Add new image
        file = new File(TestSupport.getTestDataFolder().getPath(), "img/img3.png");
        addImage(file);

        // Check initial Image size
        image = getPrivateImageField();
        assertEquals(new Rectangle(0, 0, 268, 268), image.getBounds());
        
        // Check correct default size of image
        assertEquals(new Dimension(268, 268), figure.getDefaultSize());
        assertEquals(new Dimension(268, 268), figure.getPreferredSize(-1, -1));
    }
    
    @Test
    public void testRescaleImage() throws Exception {
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img3.png");
        addImage(file);
        Image image = getPrivateImageField();
        assertEquals(new Rectangle(0, 0, 268, 268), image.getBounds());
        
        figure.setBounds(new org.eclipse.draw2d.geometry.Rectangle(0, 0, 10, 10));
        
        figure.rescaleImage();
        
        image = getPrivateImageField();
        assertEquals(new Rectangle(0, 0, 10, 10), image.getBounds());
        
        image.dispose();
    }
    
    @Test
    public void testGetOriginalImage() throws Exception {
        Image image = figure.getOriginalImage();
        assertNull(image);
        
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img3.png");
        addImage(file);
        
        image = figure.getOriginalImage();
        assertEquals(new Rectangle(0, 0, 268, 268), image.getBounds());
        
        image.dispose();
    }
   
    private void addImage(File file) throws IOException {
        IArchiveManager archiveManager = (IArchiveManager)dmImage.getAdapter(IArchiveManager.class);
        String path = archiveManager.addImageFromFile(file);
        dmImage.setImagePath(path);
    }

    private Image getPrivateImageField() throws Exception {
        return (Image)TestUtils.getPrivateField(figure, "fImage");
    }
}