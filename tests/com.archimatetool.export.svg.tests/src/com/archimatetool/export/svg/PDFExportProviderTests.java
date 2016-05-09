/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.IImageExportProvider.IExportDialogAdapter;
import com.archimatetool.tests.TestUtils;


public class PDFExportProviderTests extends AbstractExportProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PDFExportProviderTests.class);
    }
    
    @Override
    @Before
    public void runOnceBeforeEachTest() {
        super.runOnceBeforeEachTest();
        provider = new PDFExportProvider();
    }
    
    @Test
    public void testExport() throws Exception {
        File tmp = TestUtils.createTempFile(null);
        provider.init(mock(IExportDialogAdapter.class), shell, rootFigure);
        provider.export(PDFExportProvider.PDF_IMAGE_EXPORT_PROVIDER, tmp);
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
        // How do you test the integrity of a PDF file? Look at it in a viewer? ;-)
    }

    @Test
    public void testInit() {
        provider.init(mock(IExportDialogAdapter.class), shell, rootFigure);
        assertTrue(shell.getChildren().length == 0);
    }
}
