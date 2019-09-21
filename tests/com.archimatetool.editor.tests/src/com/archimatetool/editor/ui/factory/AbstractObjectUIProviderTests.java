/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;



public abstract class AbstractObjectUIProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractObjectUIProviderTests.class);
    }
    
    protected IObjectUIProvider provider;
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
    public void testGetDefaultName() {
        String name = provider.getDefaultName();
        assertNotNull(name);
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
        EObject instance = createInstanceForExpectedClass();
        ((AbstractObjectUIProvider)provider).setInstance(instance);
        
        Image image = provider.getImage();
        assertNotNull(image);
    }

    @Test
    public void testShouldExposeFeature() {
        assertTrue(provider.shouldExposeFeature((String)null));
    }
    
    
    protected EObject createInstanceForExpectedClass() {
        return expectedClass.getEPackage().getEFactoryInstance().create(expectedClass);
    }
}
