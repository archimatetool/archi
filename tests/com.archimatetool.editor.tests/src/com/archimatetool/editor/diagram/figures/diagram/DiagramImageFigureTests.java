/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigureTests;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelImage;



@SuppressWarnings("nls")
public class DiagramImageFigureTests extends AbstractDiagramModelObjectFigureTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(createFigure())
        );
    }

    static IFigure createFigure() {
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dmImage.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        return addDiagramModelObjectToModelAndFindFigure(dmImage);
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultSize(AbstractDiagramModelObjectFigure figure) {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(figure.getDiagramModelObject());
        Dimension defaultSize = provider.getDefaultSize();
        
        assertEquals(defaultSize, figure.getDefaultSize());
        
        // Add image
        try {
            File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img1.png");
            addImage(file, (DiagramImageFigure)figure);
            Image image = ((DiagramImageFigure)figure).getImage();
            assertEquals(new Dimension(image), figure.getDefaultSize());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Reset
        ((IDiagramModelImage)figure.getDiagramModelObject()).setImagePath(null);
        assertEquals(defaultSize, figure.getDefaultSize());
    }

    @ParamsTest
    public void testBorderColor(DiagramImageFigure figure) {
        assertNull(figure.getBorderColor());
        
        figure.getDiagramModelObject().setBorderColor("#010203");
        Color expected = new Color(1, 2, 3);
        assertEquals(expected, figure.getBorderColor());
    }
    
    @ParamsTest
    public void testDiagramImageSize(DiagramImageFigure figure) throws Exception {
        // No image
        Image image = figure.getImage();
        assertNull(image);
        
        // Add image
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img1.png");
        addImage(file, figure);
        
        // Get image again
        image = figure.getImage();

        // Image size
        Dimension imageSize = new Dimension(image);
        assertEquals(new Rectangle(0, 0, imageSize.width, imageSize.height), image.getBounds());
        
        // Check correct default and preferred size of figure is image size
        assertEquals(imageSize, figure.getDefaultSize());
        assertEquals(imageSize, figure.getPreferredSize(-1, -1));
        
        // Change size of DiagramModelImage and the figure
        IBounds bounds = IArchimateFactory.eINSTANCE.createBounds(0, 0, 512, 512);
        figure.getDiagramModelObject().setBounds(bounds);
        
        // Layout
        editor.layoutPendingUpdates();
        
        // Force a mock repaint since we are not using a GUI
        // We don't really need to do this here but keep it in case of future tests
        figure.paint(mock(Graphics.class));
        
        // Figure size is same as bounds
        assertEquals(new org.eclipse.draw2d.geometry.Rectangle(0, 0, 512, 512), figure.getBounds());
        
        // Check correct default and preferred size of figure is still image size
        assertEquals(imageSize, figure.getDefaultSize());
        assertEquals(imageSize, figure.getPreferredSize(-1, -1));

        // Image is still the same size
        image = figure.getImage();
        assertEquals(new Rectangle(0, 0, imageSize.width, imageSize.height), image.getBounds());
    }
    
    @ParamsTest
    public void testSettingNewImagePath(DiagramImageFigure figure) throws Exception {
        Image image = figure.getImage();
        assertNull(image);
        
        // Add image
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img1.png");
        addImage(file, figure);
        
        // Check initial Image size
        image = figure.getImage();
        assertEquals(new Rectangle(0, 0, 1024, 1024), image.getBounds());
        
        // Check correct default size of image
        assertEquals(new Dimension(1024, 1024), figure.getDefaultSize());
        assertEquals(new Dimension(1024, 1024), figure.getPreferredSize(-1, -1));

        // Add new image
        file = new File(TestSupport.getTestDataFolder().getPath(), "img/img2.png");
        addImage(file, figure);

        // Check initial Image size
        image = figure.getImage();
        assertEquals(new Rectangle(0, 0, 268, 268), image.getBounds());
        
        // Check correct default size of image
        assertEquals(new Dimension(268, 268), figure.getDefaultSize());
        assertEquals(new Dimension(268, 268), figure.getPreferredSize(-1, -1));
    }
    
    @ParamsTest
    public void testCreateImage(DiagramImageFigure figure) throws Exception {
        // No image path set so null image
        assertNull(figure.createImage());
        
        // Add image
        File file = new File(TestSupport.getTestDataFolder().getPath(), "img/img2.png");
        addImage(file, figure);
        
        // Not null
        Image image1 = figure.getImage();
        assertNotNull(image1);
        
        // Create another instance of the image
        Image image2 = figure.createImage();
        assertNotNull(image2);
        assertEquals(new Rectangle(0, 0, 268, 268), image2.getBounds());
        
        // Previous image should be disposed
        assertTrue(image1.isDisposed());
        
        // Clean up
        image2.dispose();
    }
    
    @Override
    @ParamsTest
    public void testGetIconicDelegate(AbstractDiagramModelObjectFigure figure) {
        assertNull(figure.getIconicDelegate());
    }
    
   
    private void addImage(File file, DiagramImageFigure figure) throws IOException {
        IArchiveManager archiveManager = (IArchiveManager)figure.getDiagramModelObject().getAdapter(IArchiveManager.class);
        String path = archiveManager.addImageFromFile(file);
        figure.getDiagramModelObject().setImagePath(path);
    }
}