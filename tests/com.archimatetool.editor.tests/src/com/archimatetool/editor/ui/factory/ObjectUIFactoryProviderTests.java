/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.ui.factory.diagram.DiagramConnectionUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessActorUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AccessRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssociationRelationshipUIProvider;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;



public class ObjectUIFactoryProviderTests {

    private ObjectUIFactory factory;
    
    @BeforeEach
    public void runOnceBeforeEachTest() {
        factory = new ObjectUIFactory();
    }
    
    @Test
    public void testGetRegisteredProviders() {
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) { // Use INSTANCE to get registered providers
            assertNotNull(provider);
            assertNotNull(provider.providerFor());
        }
    }
    
    @Test
    public void testSetInstance() {
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            EObject eObject = IArchimateFactory.eINSTANCE.create(eClass);
            AbstractObjectUIProvider provider = (AbstractObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(eObject);
            assertSame(eObject, provider.getInstance());
        }
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
    public void testNoDuplicateProviders() {
        IObjectUIProvider provider1 = mock(IObjectUIProvider.class);
        IObjectUIProvider provider2 = mock(IObjectUIProvider.class);
        
        EClass eClass = mock(EClass.class);
        when(provider1.providerFor()).thenReturn(eClass);
        when(provider2.providerFor()).thenReturn(eClass);
        
        factory.registerProvider(provider1);
        assertSame(provider1, factory.getProviderForClass(eClass));
        
        factory.registerProvider(provider2);
        assertSame(provider1, factory.getProviderForClass(eClass));
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
}
