/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class SVGExportProviderTests extends AbstractExportProviderTests {
    
    private SVGExportProvider svgProvider;
    
    @Override
    @BeforeEach
    public void runOnceBeforeEachTest() {
        svgProvider = new SVGExportProvider();
        provider = svgProvider;
        
        // Set prefs to defaults
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        store.setToDefault(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX_ENABLED);
        store.setToDefault(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX);
        
        super.runOnceBeforeEachTest();
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
        provider.export(SVGExportProvider.SVG_IMAGE_EXPORT_PROVIDER, tmp);
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
        // How do you test the integrity of an SVG file? Look at it in a viewer? ;-)
    }

    @Test
    public void testInit() {
        // Add a child figure
        IFigure childFigure = new Figure();
        childFigure.setBounds(new Rectangle(0, 0, 200, 100));
        rootFigure.add(childFigure);
        
        provider.init(null, shell, rootFigure);
        assertTrue(shell.getChildren().length > 0);
        
        // Check that two spinners are set to the image width and height
        Rectangle rect = provider.getViewportBounds(rootFigure);
        assertEquals(rect.width, svgProvider.fSpinner3.getSelection());
        assertEquals(rect.height, svgProvider.fSpinner4.getSelection());
    }

    @Test
    public void testDefaultPreferences() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        assertTrue(store.getBoolean(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX_ENABLED));
        assertEquals("", store.getString(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX));
    }

    @Test
    public void testSavePreferences() {
        provider.init(null, shell, rootFigure);

        svgProvider.fSetViewboxButton.setSelection(false);
        svgProvider.fTextAsShapesButton.setSelection(false);

        svgProvider.fSpinner1.setSelection(1);
        svgProvider.fSpinner2.setSelection(2);
        
        svgProvider.savePreferences();

        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        
        assertFalse(store.getBoolean(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX_ENABLED));
        assertFalse(store.getBoolean(IPreferenceConstants.SVG_EXPORT_PREFS_TEXT_AS_SHAPES));
        assertEquals("1 2", store.getString(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX));
    }
    
    @Test
    public void testPreferencesWereLoaded() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();

        store.setValue(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX_ENABLED, false);
        store.setValue(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX, "5 6");
        
        provider.init(null, shell, rootFigure);
        
        assertFalse(svgProvider.fSetViewboxButton.getSelection());
        
        assertEquals(5, svgProvider.fSpinner1.getSelection());
        assertEquals(6, svgProvider.fSpinner2.getSelection());
    }
}
