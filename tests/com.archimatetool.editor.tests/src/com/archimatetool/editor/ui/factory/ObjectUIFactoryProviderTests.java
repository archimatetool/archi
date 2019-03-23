/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ui.factory.diagram.DiagramConnectionUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessActorUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AccessRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssociationRelationshipUIProvider;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



public class ObjectUIFactoryProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ObjectUIFactoryProviderTests.class);
    }
    
    private ObjectUIFactory factory;
    
    @Before
    public void runOnceBeforeEachTest() {
        factory = new ObjectUIFactory();
    }
    
    @Test
    public void testInstance() {
        ObjectUIFactory factory = ObjectUIFactory.INSTANCE;
        assertNotNull(factory);
    }
    
    @Test
    public void testRegisterProvider() {
        IObjectUIProvider provider = mock(IObjectUIProvider.class);
        EClass eClass = mock(EClass.class);
        when(provider.providerFor()).thenReturn(eClass);
        
        assertNull(factory.getProviderForClass(eClass));
        assertEquals(0, factory.map.size());
        
        factory.registerProvider(provider);
        assertNotNull(factory.getProviderForClass(eClass));
        assertEquals(1, factory.map.size());
    }

    @Test
    public void testGetProvider_EObject_ArchiMateElement() {
        IObjectUIProvider provider = new BusinessActorUIProvider();
        factory.registerProvider(provider);
        
        EObject eObject = IArchimateFactory.eINSTANCE.createBusinessActor();
        assertSame(provider.providerFor(), factory.getProvider(eObject).providerFor());
    }
    
    @Test
    public void testGetProvider_EObject_Relationship() {
        IObjectUIProvider provider = new AssociationRelationshipUIProvider();
        factory.registerProvider(provider);
        
        EObject eObject = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        assertSame(provider.providerFor(), factory.getProvider(eObject).providerFor());
    }

    @Test
    public void testGetProvider_EObject_LineConnection() {
        IObjectUIProvider provider = new DiagramConnectionUIProvider();
        factory.registerProvider(provider);
        
        EObject eObject = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        assertSame(provider.providerFor(), factory.getProvider(eObject).providerFor());
    }

    @Test
    public void testGetProvider_EObject_DiagramModelArchimateObject() {
        IObjectUIProvider provider = new BusinessActorUIProvider();
        factory.registerProvider(provider);
        
        IDiagramModelArchimateObject eObject = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        eObject.setArchimateElement(element);
        
        assertSame(provider.providerFor(), factory.getProvider(eObject).providerFor());
    }

    @Test
    public void testGetProvider_EObject_DiagramModelArchimateConnection() {
        IObjectUIProvider provider = new AccessRelationshipUIProvider();
        factory.registerProvider(provider);
        
        IDiagramModelArchimateConnection eObject = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAccessRelationship();
        eObject.setArchimateRelationship(relation);
        
        assertEquals(provider.providerFor(), factory.getProvider(relation).providerFor());
    }
    
    @Test
    public void testGetProviders() {
        for(IObjectUIProvider provider : factory.getProviders()) {
            assertNotNull(provider);
            assertNotNull(provider.providerFor());
        }
    }

}
