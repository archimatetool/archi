/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ui.factory.business.BusinessActorUIProvider;
import com.archimatetool.editor.ui.factory.diagram.LineConnectionUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AccessRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssociationRelationshipUIProvider;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IRelationship;



public class ElementUIFactoryProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ElementUIFactoryProviderTests.class);
    }
    
    private ElementUIFactory factory;
    
    @Before
    public void runOnceBeforeEachTest() {
        factory = new ElementUIFactory();
    }
    
    @Test
    public void testInstance() {
        ElementUIFactory factory = ElementUIFactory.INSTANCE;
        assertNotNull(factory);
    }
    
    @Test
    public void testRegisterProvider() {
        IElementUIProvider provider = mock(IElementUIProvider.class);
        EClass eClass = mock(EClass.class);
        when(provider.providerFor()).thenReturn(eClass);
        
        assertNull(factory.getProvider(eClass));
        assertEquals(0, factory.map.size());
        
        factory.registerProvider(provider);
        assertNotNull(factory.getProvider(eClass));
        assertEquals(1, factory.map.size());
    }

    @Test
    public void testGetProvider_EObject_ArchiMateElement() {
        IElementUIProvider provider = new BusinessActorUIProvider();
        factory.registerProvider(provider);
        
        EObject eObject = IArchimateFactory.eINSTANCE.createBusinessActor();
        assertEquals(provider, factory.getProvider(eObject));
    }
    
    @Test
    public void testGetProvider_EObject_Relationship() {
        IElementUIProvider provider = new AssociationRelationshipUIProvider();
        factory.registerProvider(provider);
        
        EObject eObject = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        assertEquals(provider, factory.getProvider(eObject));
    }

    @Test
    public void testGetProvider_EObject_LineConnection() {
        IElementUIProvider provider = new LineConnectionUIProvider();
        factory.registerProvider(provider);
        
        EObject eObject = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        assertEquals(provider, factory.getProvider(eObject));
    }

    @Test
    public void testGetProvider_EObject_DiagramModelArchimateObject() {
        IElementUIProvider provider = new BusinessActorUIProvider();
        factory.registerProvider(provider);
        
        IDiagramModelArchimateObject eObject = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        eObject.setArchimateElement(element);
        
        assertEquals(provider, factory.getProvider(eObject));
    }

    @Test
    public void testGetProvider_EObject_DiagramModelArchimateConnection() {
        IElementUIProvider provider = new AccessRelationshipUIProvider();
        factory.registerProvider(provider);
        
        IDiagramModelArchimateConnection eObject = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IRelationship relation = IArchimateFactory.eINSTANCE.createAccessRelationship();
        eObject.setRelationship(relation);
        
        assertEquals(provider, factory.getProvider(eObject));
    }
}
