/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.archimatetool.editor.diagram.IImageExportProvider.IExportDialogAdapter;
import com.archimatetool.tests.TestUtils;

/**
 * ImageExportProviderTests
 * 
 * @author Phillip Beauvoir
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageExportProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ImageExportProviderTests.class);
    }
    
    private ImageExportProvider provider;
    
    @Mock
    private IExportDialogAdapter adapter;
    
    private Shell shell;
    
    @Before
    public void onceOnceBeforeEachTest() {
        provider = new ImageExportProvider();
        shell = new Shell();
    }
    
    @After
    public void onceOnceAfterEachTest() {
        shell.dispose();
    }

    @Test
    public void testInit() {
        provider.init(adapter, shell, new FreeformLayer());
        assertTrue(shell.getChildren().length == 0);
    }

    @Test
    public void testBMPExport() throws Exception {
        testExport(ImageExportProvider.BMP_IMAGE_EXPORT_PROVIDER);
    }
    
    @Test
    public void testJPEGExport() throws Exception {
        testExport(ImageExportProvider.JPEG_IMAGE_EXPORT_PROVIDER);
    }

    @Test
    public void testPNGExport() throws Exception {
        testExport(ImageExportProvider.PNG_IMAGE_EXPORT_PROVIDER);
    }

    private void testExport(String formatID) throws Exception {
        IFigure rootFigure = new FreeformLayer();
        rootFigure.setBounds(new Rectangle(0, 0, 500, 500));
        IFigure childFigure = new Figure();
        childFigure.setBounds(new Rectangle(200, 200, 128, 52));
        rootFigure.add(childFigure);
        
        provider.init(adapter, shell, rootFigure);

        File tmp = TestUtils.createTempFile(null);
        provider.export(formatID, tmp);
        assertTrue(tmp.exists());

        ImageData imageData = new ImageData(tmp.getPath());
        assertEquals(148, imageData.width); // width + 10 margins
        assertEquals(72, imageData.height); // height + 10 margins
    }
}