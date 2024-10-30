/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;


@SuppressWarnings("nls")
public abstract class AbstractIssueTypeTests {
    
    protected AbstractIssueType issueType;
    
    @Test
    public void testGetName() {
        assertNotNull(issueType.getName());
        issueType.setName("aname");
        assertEquals("aname", issueType.getName());
    }
    
    @Test
    public void testGetDescription() {
        assertNotNull(issueType.getDescription());
        issueType.setDescription("adesc");
        assertEquals("adesc", issueType.getDescription());
    }
    
    @Test
    public void testGetExplanation() {
        assertNotNull(issueType.getExplanation());
        issueType.setExplanation("exp");
        assertEquals("exp", issueType.getExplanation());
    }

    @Test
    public void testGetObject() {
        assertNull(issueType.getObject());
        Object anObject = new Object();
        issueType.setObject(anObject);
        assertSame(anObject, issueType.getObject());
    }

    @Test
    public void testGetImage() {
        assertNotNull(issueType.getImage());
    }
    
    @Test
    public void testGetHintContent() {
        assertEquals(issueType.getExplanation(), issueType.getHintContent());
    }
    
    @Test
    public void testGetHintTitle() {
        assertEquals(issueType.getName(), issueType.getHintTitle());
    }
    
    @Test
    public void testGetAdapter_ObjectIsNull() {
        assertNull(issueType.getAdapter(Object.class));
    }

    @Test
    public void testGetAdapter_ElementIsInstance() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        issueType.setObject(element);
        assertSame(element, issueType.getAdapter(IArchimateModelObject.class));
    }
    
    @Test
    public void testGetAdapter_DiagramObjectIsInstance() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmc.setArchimateConcept(element);
        issueType.setObject(dmc);
        assertSame(dmc, issueType.getAdapter(IArchimateModelObject.class));
    }
    
    @Test
    public void testGetAdapter_ObjectIsAssignableFrom_Element() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmc.setArchimateConcept(element);
        issueType.setObject(dmc);
        assertSame(element, issueType.getAdapter(IArchimateConcept.class));
        assertSame(element, issueType.getAdapter(IArchimateElement.class));
        assertNull(issueType.getAdapter(IArchimateRelationship.class));
    }

    @Test
    public void testGetAdapter_ObjectIsAssignableFrom_Relation() {
        IAggregationRelationship relation = IArchimateFactory.eINSTANCE.createAggregationRelationship();
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateConcept(relation);
        issueType.setObject(dmc);
        assertSame(relation, issueType.getAdapter(IArchimateConcept.class));
        assertSame(relation, issueType.getAdapter(IArchimateRelationship.class));
        assertNull(issueType.getAdapter(IArchimateElement.class));
    }
}
