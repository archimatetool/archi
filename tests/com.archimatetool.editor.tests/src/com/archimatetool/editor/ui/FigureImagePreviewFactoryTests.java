/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.Image;
import org.junit.Test;

import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;

import junit.framework.JUnit4TestAdapter;



public class FigureImagePreviewFactoryTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FigureImagePreviewFactoryTests.class);
    }

    @Test
    public void testGetPreviewImageType1() {
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) {
            if(provider instanceof IArchimateElementUIProvider) {
                Image image = FigureImagePreviewFactory.getPreviewImage(provider.providerFor(), 0);
                assertNotNull(image);
            }
        }
    }
    
    @Test
    public void testGetPreviewImageType2() {
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) {
            if(provider instanceof IArchimateElementUIProvider) {
                Image image = FigureImagePreviewFactory.getPreviewImage(provider.providerFor(), 1);
                
                if(((IArchimateElementUIProvider)provider).hasAlternateFigure()) {
                    assertNotNull(image);
                }
                else {
                    assertNull(image);
                }
            }
        }
    }
    
    @Test
    public void testGetPreviewImageTypeWrong() {
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) {
            if(provider instanceof IArchimateElementUIProvider) {
                Image image = FigureImagePreviewFactory.getPreviewImage(provider.providerFor(), 3);
                assertNull(image);
            }
        }
    }

}
