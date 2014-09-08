/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArtifact;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IRelationship;


@SuppressWarnings("nls")
public class FieldDataFactoryTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FieldDataFactoryTests.class);
    }

    @Test
    public void testGetFieldValue_This() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        Object o = FieldDataFactory.getFieldValue(model, "this");
        assertEquals(model, o);
    }

    @Test
    public void testGetFieldValue_ID() {
        IBusinessActor element = IArchimateFactory.eINSTANCE.createBusinessActor();
        element.setId("1234");
        Object o = FieldDataFactory.getFieldValue(element, "id");
        assertEquals("1234", o);
    }

    @Test
    public void testGetFieldValue_Name() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setName("Pogo");
        Object o = FieldDataFactory.getFieldValue(model, "name");
        assertEquals("Pogo", o);
    }

    @Test
    public void testGetFieldValue_Documentation() {
        IArtifact element = IArchimateFactory.eINSTANCE.createArtifact();
        element.setDocumentation("Documentation");
        Object o = FieldDataFactory.getFieldValue(element, "documentation");
        assertEquals("Documentation", o);
    }
    
    @Test
    public void testGetFieldValue_Purpose() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setPurpose("Purpose");
        Object o = FieldDataFactory.getFieldValue(model, "purpose");
        assertEquals("Purpose", o);
    }

    @Test
    public void testGetFieldValue_RelationSource() {
        IRelationship relation = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        IArtifact source = IArchimateFactory.eINSTANCE.createArtifact();
        source.setName("source");
        relation.setSource(source);
        Object o = FieldDataFactory.getFieldValue(relation, "relation_source");
        assertEquals("source", o);
    }

    @Test
    public void testGetFieldValue_RelationTarget() {
        IRelationship relation = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        IArtifact target = IArchimateFactory.eINSTANCE.createArtifact();
        target.setName("target");
        relation.setTarget(target);
        Object o = FieldDataFactory.getFieldValue(relation, "relation_target");
        assertEquals("target", o);
    }

}
