/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.tests.TestUtils;


public abstract class AbstractObjectUIProviderTests {
    
    /**
     * Create an Arguments parameter for provider and expected eClass
     */
    protected static Arguments getParam(IObjectUIProvider provider, EClass eClass) {
        return Arguments.of(Named.of(provider.getClass().getSimpleName(), provider), Named.of(eClass.getName(), eClass));
    }

    @BeforeAll
    public static void runOnce() {
        // Need this in JUnit 5 for getImage() test
        TestUtils.ensureDefaultDisplay();
    }

    @ParamsTest
    public void testProviderFor(IObjectUIProvider provider, EClass expectedClass) {
        EClass eClass = provider.providerFor();
        assertNotNull(eClass);
        assertEquals(expectedClass, eClass);
    }
    
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertNotNull(editPart);
    }
    
    @ParamsTest
    public void testGetDefaultName(IObjectUIProvider provider) {
        String name = provider.getDefaultName();
        assertNotNull(name);
    }
    
    @ParamsTest
    public void testGetImage(IObjectUIProvider provider) {
        Image image = provider.getImage();
        assertNotNull(image);
    }
    
    @ParamsTest
    public void testGetImageDescriptor(IObjectUIProvider provider) {
        ImageDescriptor id = provider.getImageDescriptor();
        assertNotNull(id);
    }

    @ParamsTest
    public void testGetImageInstance(IObjectUIProvider provider, EClass expectedClass) {
        EObject instance = createInstanceForExpectedClass(expectedClass);
        ((AbstractObjectUIProvider)provider).setInstance(instance);
        
        Image image = provider.getImage();
        assertNotNull(image);
    }

    @ParamsTest
    public void testShouldExposeFeature(IObjectUIProvider provider) {
        assertTrue(provider.shouldExposeFeature((String)null));
    }
    
    
    protected EObject createInstanceForExpectedClass(EClass expectedClass) {
        return expectedClass.getEPackage().getEFactoryInstance().create(expectedClass);
    }
}
