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
    public void testGetFigurePreviewImageForClass() {
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) {
            if(provider instanceof IArchimateElementUIProvider) {
                Image image = FigureImagePreviewFactory.getFigurePreviewImageForClass(provider.providerFor());
                assertNotNull(image);
            }
        }
    }
    
    @Test
    public void testGetAlternateFigurePreviewImageForClass() {
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) {
            if(provider instanceof IArchimateElementUIProvider) {
                Image image = FigureImagePreviewFactory.getAlternateFigurePreviewImageForClass(provider.providerFor());
                
                if(((IArchimateElementUIProvider)provider).hasAlternateFigure()) {
                    assertNotNull(image);
                }
                else {
                    assertNull(image);
                }
            }
        }
    }
}
