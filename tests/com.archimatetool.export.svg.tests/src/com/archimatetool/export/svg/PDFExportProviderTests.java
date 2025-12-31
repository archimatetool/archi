/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.tests.TestUtils;


public class PDFExportProviderTests extends AbstractExportProviderTests {
    
    private PDFExportProvider pdfProvider;
    
    @Override
    protected PDFExportProvider getProvider() {
        if(pdfProvider == null) {
            pdfProvider = new PDFExportProvider();
        }
        return pdfProvider;
    }
    
    @Test
    public void testExport() throws Exception {
        File tmp = TestUtils.createTempFile(null);
        
        // Add a child figure with an image to test image handling
        IFigure childFigure = new Figure() {
            @Override
            public void paintFigure(Graphics graphics) {
                super.paintFigure(graphics);
                Image image = IArchiImages.ImageFactory.getImage(IArchiImages.ICON_LANDSCAPE);
                graphics.drawImage(image, getBounds().x, getBounds().y);
            }
        };
        childFigure.setBounds(new Rectangle(0, 0, 50, 50));
        rootFigure.add(childFigure);
        
        getProvider().init(null, shell, rootFigure);
        getProvider().export(PDFExportProvider.PDF_IMAGE_EXPORT_PROVIDER, tmp);
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
        // How do you test the integrity of a PDF file? Look at it in a viewer? ;-)
    }
    
    @Test
    public void testExportDiagramModel() throws Exception  {
        File tmp = TestUtils.createTempFile(null);
        
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setBounds(10, 10, 100, 100);
        dm.getChildren().add(note);
        
        getProvider().export(dm, tmp);
        
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
    }

    @Test
    public void testInit() {
        getProvider().init(null, shell, rootFigure);
        assertTrue(shell.getChildren().length > 0);
    }
}
