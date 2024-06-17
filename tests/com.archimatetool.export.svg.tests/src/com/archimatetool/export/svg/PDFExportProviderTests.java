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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.tests.TestUtils;


public class PDFExportProviderTests extends AbstractExportProviderTests {
    
    @Override
    @BeforeEach
    public void runOnceBeforeEachTest() {
        super.runOnceBeforeEachTest();
        provider = new PDFExportProvider();
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
                graphics.drawImage(image, bounds.x, bounds.y);
            }
        };
        childFigure.setBounds(new Rectangle(0, 0, 50, 50));
        rootFigure.add(childFigure);
        
        provider.init(null, shell, rootFigure);
        provider.export(PDFExportProvider.PDF_IMAGE_EXPORT_PROVIDER, tmp);
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
        // How do you test the integrity of a PDF file? Look at it in a viewer? ;-)
    }

    @Test
    public void testInit() {
        provider.init(null, shell, rootFigure);
        assertTrue(shell.getChildren().length > 0);
    }
}
