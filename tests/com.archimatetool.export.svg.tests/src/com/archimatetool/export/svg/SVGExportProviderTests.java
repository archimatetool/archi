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
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class SVGExportProviderTests extends AbstractExportProviderTests {
    
    private SVGExportProvider svgProvider;
    
    @Override
    @BeforeEach
    public void runOnceBeforeEachTest() {
        super.runOnceBeforeEachTest();
        
        // Set prefs to defaults
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        store.setToDefault(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX_ENABLED);
        store.setToDefault(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX);
    }
    
    @Override
    protected SVGExportProvider getProvider() {
        if(svgProvider == null) {
            svgProvider = new SVGExportProvider();
        }
        return svgProvider;
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
        getProvider().export(SVGExportProvider.SVG_IMAGE_EXPORT_PROVIDER, tmp);
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
        // How do you test the integrity of an SVG file? Look at it in a viewer? ;-)
    }
    
    @Test
    public void testExportDiagramModel() throws Exception  {
        File tmp = TestUtils.createTempFile(null);
        
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setBounds(10, 10, 100, 100);
        dm.getChildren().add(note);
        
        getProvider().export(dm, tmp, true);
        
        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 100);
    }

    @Test
    public void testInit() {
        // Add a child figure
        IFigure childFigure = new Figure();
        childFigure.setBounds(new Rectangle(0, 0, 200, 100));
        rootFigure.add(childFigure);
        
        getProvider().init(null, shell, rootFigure);
        assertTrue(shell.getChildren().length > 0);
        
        // Check that two spinners are set to the image width and height
        Rectangle rect = getProvider().getViewportBounds(rootFigure);
        assertEquals(rect.width, getProvider().fSpinner3.getSelection());
        assertEquals(rect.height, getProvider().fSpinner4.getSelection());
    }
    
    @Test
    public void testGetSVGStringDiagramModel() throws Exception {
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setBounds(10, 10, 100, 100);
        dm.getChildren().add(note);
        
        String s = getProvider().getSVGString(dm , true);
        assertTrue(s.startsWith("<?xml version=\"1.0\"?>"));
        assertFalse(s.contains("<!DOCTYPE"));
    }

    @Test
    public void testDefaultPreferences() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        assertTrue(store.getBoolean(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX_ENABLED));
        assertEquals("", store.getString(IPreferenceConstants.SVG_EXPORT_PREFS_VIEWBOX));
    }

    @Test
    public void testSavePreferences() {
        getProvider().init(null, shell, rootFigure);

        getProvider().fSetViewboxButton.setSelection(false);
        getProvider().fTextAsShapesButton.setSelection(false);

        getProvider().fSpinner1.setSelection(1);
        getProvider().fSpinner2.setSelection(2);
        
        getProvider().savePreferences();

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
        
        getProvider().init(null, shell, rootFigure);
        
        assertFalse(getProvider().fSetViewboxButton.getSelection());
        
        assertEquals(5, getProvider().fSpinner1.getSelection());
        assertEquals(6, getProvider().fSpinner2.getSelection());
    }
}
