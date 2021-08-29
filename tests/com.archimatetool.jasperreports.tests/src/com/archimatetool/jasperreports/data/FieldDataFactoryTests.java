/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IArtifact;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IProfile;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class FieldDataFactoryTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FieldDataFactoryTests.class);
    }

    @Test
    public void testGetFieldValue_This() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        Object o = FieldDataFactory.getFieldValue(model, FieldDataFactory.THIS);
        assertEquals(model, o);
    }

    @Test
    public void testGetFieldValue_ID() {
        IBusinessActor element = IArchimateFactory.eINSTANCE.createBusinessActor();
        element.setId("1234");
        Object o = FieldDataFactory.getFieldValue(element, FieldDataFactory.ID);
        assertEquals("1234", o);
    }

    @Test
    public void testGetFieldValue_Name() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setName("Pogo");
        Object o = FieldDataFactory.getFieldValue(model, FieldDataFactory.NAME);
        assertEquals("Pogo", o);
    }

    @Test
    public void testGetFieldValue_Type() {
        IBusinessActor ba = IArchimateFactory.eINSTANCE.createBusinessActor();
        Object o = FieldDataFactory.getFieldValue(ba, FieldDataFactory.TYPE);
        assertEquals("Business Actor", o);
    }

    @Test
    public void testGetFieldValue_TypeWithProfileName() {
        IBusinessActor ba = IArchimateFactory.eINSTANCE.createBusinessActor();
        IProfile profile = IArchimateFactory.eINSTANCE.createProfile();
        profile.setName("Oscar");
        ba.getProfiles().add(profile);
        Object o = FieldDataFactory.getFieldValue(ba, FieldDataFactory.TYPE);
        assertEquals("Business Actor (Oscar)", o);
    }

    @Test
    public void testGetFieldValue_Documentation() {
        IArtifact element = IArchimateFactory.eINSTANCE.createArtifact();
        element.setDocumentation("Documentation");
        Object o = FieldDataFactory.getFieldValue(element, FieldDataFactory.DOCUMENTATION);
        assertEquals("Documentation", o);
    }
    
    @Test
    public void testGetFieldValue_Purpose() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setPurpose("Purpose");
        Object o = FieldDataFactory.getFieldValue(model, FieldDataFactory.PURPOSE);
        assertEquals("Purpose", o);
    }

    @Test
    public void testGetFieldValue_RelationSource() {
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createServingRelationship();
        IArtifact source = IArchimateFactory.eINSTANCE.createArtifact();
        source.setName("source");
        relation.setSource(source);
        Object o = FieldDataFactory.getFieldValue(relation, FieldDataFactory.RELATION_SOURCE);
        assertEquals("source", o);
    }

    @Test
    public void testGetFieldValue_RelationTarget() {
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createServingRelationship();
        IArtifact target = IArchimateFactory.eINSTANCE.createArtifact();
        target.setName("target");
        relation.setTarget(target);
        Object o = FieldDataFactory.getFieldValue(relation, FieldDataFactory.RELATION_TARGET);
        assertEquals("target", o);
    }

    @Test
    public void testGetFieldValue_InfluenceStrength() {
        IInfluenceRelationship relation = IArchimateFactory.eINSTANCE.createInfluenceRelationship();
        relation.setStrength("++");
        Object o = FieldDataFactory.getFieldValue(relation, FieldDataFactory.INFLUENCE_STRENGTH);
        assertEquals("++", o);
    }
    
    @Test
    public void testGetFieldValue_AccessType() {
        IAccessRelationship relation = IArchimateFactory.eINSTANCE.createAccessRelationship();
        relation.setAccessType(1);
        Object o = FieldDataFactory.getFieldValue(relation, FieldDataFactory.ACCESS_TYPE);
        assertEquals(1, o);
    }

}
