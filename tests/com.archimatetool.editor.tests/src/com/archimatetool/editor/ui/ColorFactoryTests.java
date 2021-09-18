/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IBusinessActor;

import junit.framework.JUnit4TestAdapter;



@SuppressWarnings("nls")
public class ColorFactoryTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ColorFactoryTests.class);
    }
    
    @Test
    public void testGetInt_Int_Int() {
        Color color = ColorFactory.get(1, 2, 3);
        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen());
        assertEquals(3, color.getBlue());
    }
    
    @Test
    public void testGetRGB() {
        Color color = ColorFactory.get(new RGB(1, 2, 3));
        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen());
        assertEquals(3, color.getBlue());
    }
    
    @Test
    public void testGetString() {
        Color color = ColorFactory.get("#010203");
        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen());
        assertEquals(3, color.getBlue());
    }
    
    @Test
    public void testColorIsCached() {
        Color color1 = ColorFactory.get(11, 22, 33);
        Color color2 = ColorFactory.get(11, 22, 33);
        assertSame(color1, color2);
    }
    
    @Test
    public void testGetUserDefaultFillColor() {
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        Color color = ColorFactory.getUserDefaultFillColor(actor);
        assertNull(color);
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX + actor.eClass().getName(), "#010203");
        color = ColorFactory.getUserDefaultFillColor(actor);
        
        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen());
        assertEquals(3, color.getBlue());
        
        ArchiPlugin.PREFERENCES.setToDefault(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX + actor.eClass().getName());
    }

    @Test
    public void testGetInbuiltDefaultFillColor() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        Color color = ColorFactory.getInbuiltDefaultFillColor(element);
        assertEquals(((IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(element)).getDefaultColor(), color);
        
        element = IArchimateFactory.eINSTANCE.createApplicationComponent();
        color = ColorFactory.getInbuiltDefaultFillColor(element);
        assertEquals(((IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(element)).getDefaultColor(), color);
        
        element = IArchimateFactory.eINSTANCE.createCommunicationNetwork();
        color = ColorFactory.getInbuiltDefaultFillColor(element);
        assertEquals(((IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(element)).getDefaultColor(), color);
    }

    @Test
    public void testGetUserDefaultLineColor_Element() {
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        Color color = ColorFactory.getUserDefaultLineColor(actor);
        assertNull(color);
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR, "#020304");
        color = ColorFactory.getUserDefaultLineColor(actor);
        
        assertEquals(2, color.getRed());
        assertEquals(3, color.getGreen());
        assertEquals(4, color.getBlue());
        
        ArchiPlugin.PREFERENCES.setToDefault(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR);
    }

    @Test
    public void testGetUserDefaultLineColor_Connection() {
        IAssignmentRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        Color color = ColorFactory.getUserDefaultLineColor(relation);
        assertNull(color);
        
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR, "#010203");
        color = ColorFactory.getUserDefaultLineColor(relation);
        
        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen());
        assertEquals(3, color.getBlue());
        
        ArchiPlugin.PREFERENCES.setToDefault(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR);
    }

    @Test
    public void testGetInbuiltDefaultLineColor() {
        Color color = ColorFactory.getInbuiltDefaultLineColor(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertEquals(92, color.getRed());
        assertEquals(92, color.getGreen());
        assertEquals(92, color.getBlue());
    }
    
    @Test
    public void testGetPixelValue() {
        RGB rgb = new RGB(10, 10, 10);
        int value = ColorFactory.getPixelValue(rgb);
        assertEquals(657930, value);
    }

    @Test
    public void testConvertColorToString() {
        // Test for null
        String s = ColorFactory.convertColorToString(null);
        assertEquals("", s);
        
        Color color = ColorFactory.get(100, 212, 4);
        s = ColorFactory.convertColorToString(color);
        assertEquals("#64d404", s);
    }

    @Test
    public void testConvertRGBToString() {
        // Test for null
        String s = ColorFactory.convertRGBToString(null);
        assertEquals("", s);
        
        RGB rgb = new RGB(100, 212, 4);
        s = ColorFactory.convertRGBToString(rgb);
        assertEquals("#64d404", s);
    }

    @Test
    public void testConvertStringToRGB() {
        // Test is null
        RGB rgb = ColorFactory.convertStringToRGB("");
        assertNull(rgb);
        
        rgb = ColorFactory.convertStringToRGB("#010aef");
        assertEquals(new RGB(1, 10, 239), rgb);
    }
    
    @Test
    public void testGetDarkerColor() {
        Color color1 = ColorFactory.get(10, 100, 200);
        Color color2 = ColorFactory.getDarkerColor(color1, 0.9f);
        assertEquals(9, color2.getRed());
        assertEquals(90, color2.getGreen());
        assertEquals(180, color2.getBlue());
    }
    
    @Test
    public void testGetLighterColor() {
        Color color1 = ColorFactory.get(10, 100, 200);
        Color color2 = ColorFactory.getLighterColor(color1, 0.9f);
        assertEquals(11, color2.getRed());
        assertEquals(111, color2.getGreen());
        assertEquals(222, color2.getBlue());
    }
}
