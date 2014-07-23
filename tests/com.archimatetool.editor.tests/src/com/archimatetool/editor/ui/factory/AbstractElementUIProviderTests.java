/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.junit.Test;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IArchimateFactory;



public abstract class AbstractElementUIProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractElementUIProviderTests.class);
    }
    
    protected IElementUIProvider provider;
    protected EClass expectedClass;
    
    @Test
    public void testProviderFor() {
        EClass eClass = provider.providerFor();
        assertNotNull(eClass);
        assertEquals(expectedClass, eClass);
    }
    
    @Test
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertNotNull(editPart);
    }
    
    @Test
    public void testGetDefaultColor() {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }

    @Test
    public void testGetDefaultName() {
        String name = provider.getDefaultName();
        assertNotNull(name);
    }
    
    @Test
    public void testGetDefaultShortName() {
        String name = provider.getDefaultShortName();
        assertNotNull(name);
    }
    
    @Test
    public void testGetDefaultLineColor() {
        Color color = provider.getDefaultLineColor();
        assertEquals(ColorFactory.get(92, 92, 92), color);
    }

    @Test
    public void testGetImage() {
        Image image = provider.getImage();
        assertNotNull(image);
    }
    
    @Test
    public void testGetImageDescriptor() {
        ImageDescriptor id = provider.getImageDescriptor();
        assertNotNull(id);
    }

    @Test
    public void testGetImageInstance() {
        EObject instance = IArchimateFactory.eINSTANCE.create(expectedClass);
        Image image = provider.getImage(instance);
        assertNotNull(image);
    }
    
    @Test
    public void testGetDefaultSize() {
        Dimension d = provider.getDefaultSize();
        assertNotNull(d);
    }
    
}
