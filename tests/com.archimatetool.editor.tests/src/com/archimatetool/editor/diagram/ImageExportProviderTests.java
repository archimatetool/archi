/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.tests.TestUtils;

/**
 * ImageExportProviderTests
 * 
 * @author Phillip Beauvoir
 */
public class ImageExportProviderTests {
    
    private ImageExportProvider provider;
    private Shell shell;
    
    private IFigure rootFigure;
    
    @BeforeEach
    public void onceOnceBeforeEachTest() {
        provider = new ImageExportProvider();
        shell = new Shell();
        
        // Set prefs to defaults
        IPreferenceStore store = ArchiPlugin.PREFERENCES;
        store.setValue(ImageExportProvider.PREFS_IMAGE_SCALE, 0);
        
        rootFigure = new FreeformLayer();
        rootFigure.setBounds(new Rectangle(0, 0, 500, 500));
    }
    
    @AfterEach
    public void onceOnceAfterEachTest() {
        shell.dispose();
    }

    @Test
    public void testInit() {
        provider.init(null, shell, rootFigure);
        assertTrue(shell.getChildren().length > 0);
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
        IFigure childFigure = new Figure();
        childFigure.setBounds(new Rectangle(200, 200, 128, 52));
        rootFigure.add(childFigure);
        
        provider.init(null, shell, rootFigure);

        File tmp = TestUtils.createTempFile(null);
        
        provider.export(formatID, tmp);
        
        assertTrue(tmp.exists());

        ImageData imageData = new ImageData(tmp.getPath());
        int scale = ImageFactory.getImageDeviceZoom() / 100; // Image Exporter exports using this
        assertEquals(148 * scale, imageData.width); // 128 width + 10 margins
        assertEquals(72 * scale, imageData.height); // 52 height + 10 margins
        
        // Test it by setting the scale to 200%
        provider.fScaleSpinner.setSelection(200);
        provider.export(formatID, tmp);
        assertTrue(tmp.exists());

        imageData = new ImageData(tmp.getPath());
        assertEquals(276 * scale, imageData.width); // 256 width + 10 margins
        assertEquals(124 * scale, imageData.height); // 104 height + 10 margins
    }
    
    @Test
    public void testSavePreferences() {
        provider.init(null, shell, rootFigure);

        provider.fScaleSpinner.setSelection(345);
        
        provider.savePreferences();

        assertEquals(345, ArchiPlugin.PREFERENCES.getInt(ImageExportProvider.PREFS_IMAGE_SCALE));
    }
    
    @Test
    public void testPreferencesWereLoaded() {
        ArchiPlugin.PREFERENCES.setValue(ImageExportProvider.PREFS_IMAGE_SCALE, 123);
        
        provider.init(null, shell, rootFigure);
        
        assertEquals(123, provider.fScaleSpinner.getSelection());
    }

}