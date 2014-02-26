/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.ImageData;
import org.junit.Test;

import com.archimatetool.editor.diagram.ImageExportProviderManager.ImageExportProviderInfo;
import com.archimatetool.tests.TestUtils;

@SuppressWarnings("nls")
public class ImageExportProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ImageExportProviderTests.class);
    }
    
    @Test
    public void testGetImageExportProviders() {
        List<ImageExportProviderInfo> providers = ImageExportProviderManager.getImageExportProviders();
        assertNotNull(providers);
        for(ImageExportProviderInfo info : providers) {
            assertNotNull(info.getProvider());
            assertNotNull(info.getLabel());
            assertNotNull(info.getID());
            assertNotNull(info.getExtensions());
        }
    }

    @Test
    public void testCanExport() throws Exception {
        IFigure rootFigure = new FreeformLayer();
        rootFigure.setBounds(new Rectangle(0, 0, 500, 500));
        IFigure childFigure = new Figure();
        childFigure.setBounds(new Rectangle(200, 200, 128, 52));
        rootFigure.add(childFigure);
        
        List<ImageExportProviderInfo> providers = ImageExportProviderManager.getImageExportProviders();
        for(ImageExportProviderInfo info : providers) {
            IImageExportProvider provider = info.getProvider();
            
            // Only test editor providers
            if(info.getID().startsWith("com.archimatetool.editor")) {
                File tmp = TestUtils.createTempFile(null);
                provider.export(info.getID(), tmp, rootFigure);
                assertTrue(tmp.exists());
                
                ImageData id = new ImageData(tmp.getPath());
                assertEquals(148, id.width); // width + 10 margins
                assertEquals(72, id.height); // height + 10 margins
            }
            
        }
    }
    
}