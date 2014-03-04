/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.editor.diagram.ImageExportProviderManager.ImageExportProviderInfo;

/**
 * ImageExportProviderManagerTests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ImageExportProviderManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ImageExportProviderManagerTests.class);
    }
    
    @Test
    public void testImageExportProviderInfo() {
        IImageExportProvider provider = mock(IImageExportProvider.class);
        
        String id = "id1";
        String label = "My Image Exporter";
        
        // Can parse spaces in extensions
        ImageExportProviderInfo info = new ImageExportProviderInfo(provider, id, label, "png");
        assertEquals(provider, info.getProvider());
        assertEquals(id, info.getID());
        assertEquals(label, info.getLabel());
        assertEquals(1, info.getExtensions().size());
        assertEquals("png", info.getExtensions().get(0));

        // Can parse trailing comma
        info = new ImageExportProviderInfo(provider, id, label, "png,");
        assertEquals(1, info.getExtensions().size());
        assertEquals("png", info.getExtensions().get(0));

        // Can parse spaces in extensions
        info = new ImageExportProviderInfo(provider, id, label, "  png, svg  ");
        assertEquals(2, info.getExtensions().size());
        assertEquals("png", info.getExtensions().get(0));
        assertEquals("svg", info.getExtensions().get(1));
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
}