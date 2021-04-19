/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProfile;


@SuppressWarnings("nls")
public abstract class ArchimateConceptTests {
    
    protected abstract IArchimateConcept getArchimateConcept();
    
    protected IArchimateModel model;
    protected IArchimateConcept concept;
    
    @Before
    public void runBeforeEachArchimateConceptTest() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        concept = getArchimateConcept();
    }

    
    @Test
    public void testGetAdapter() {
        CommonTests.testGetAdapter(concept);
    }
        
    @Test
    public void testGetArchimateModel() {
        assertNull(concept.getArchimateModel());
        
        model.getDefaultFolderForObject(concept).getElements().add(concept);
        assertSame(model, concept.getArchimateModel());
    }
    
    @Test
    public void testGetCopy() {
        concept.setName("name");
        concept.setDocumentation("doc");
        concept.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        concept.getFeatures().add(IArchimateFactory.eINSTANCE.createFeature());
        
        IArchimateConcept copy = (IArchimateConcept)concept.getCopy();
        
        assertNotSame(concept, copy);
        assertNotNull(copy.getId());
        assertNotEquals(concept.getId(), copy.getId());
        assertEquals(concept.getName(), copy.getName());
        assertEquals(concept.getDocumentation(), copy.getDocumentation());
        
        assertNotSame(concept.getProperties(), copy.getProperties());
        assertEquals(concept.getProperties().size(), copy.getProperties().size());
        
        assertNotSame(concept.getFeatures(), copy.getFeatures());
        assertEquals(concept.getFeatures().size(), copy.getFeatures().size());

        assertNotSame(concept.getSourceRelationships(), copy.getSourceRelationships());
        assertNotSame(concept.getTargetRelationships(), copy.getTargetRelationships());
    }

    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(concept);
    }

    @Test
    public void testGetID() {
        assertNotNull(concept.getId());
    }
        
    @Test
    public void testGetName() {
        CommonTests.testGetName(concept);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(concept);
    }
    
    @Test
    public void testGetSourceRelationships() {
        assertEquals(0, concept.getSourceRelationships().size());
        concept.getSourceRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, concept.getSourceRelationships().size());
    }
    
    @Test
    public void testGetTargetRelationships() {
        assertEquals(0, concept.getTargetRelationships().size());
        concept.getTargetRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, concept.getTargetRelationships().size());
    }
    
    @Test
    public void testGetProfiles() {
        CommonTests.testList(concept.getProfiles(), IArchimatePackage.eINSTANCE.getProfile());
    }
    
    @Test
    public void testGetPrimaryProfile() {
        assertNull(concept.getPrimaryProfile());
        
        IProfile profile1 = IArchimateFactory.eINSTANCE.createProfile();
        IProfile profile2 = IArchimateFactory.eINSTANCE.createProfile();
        
        concept.getProfiles().add(profile1);
        concept.getProfiles().add(profile2);
        assertSame(profile1, concept.getPrimaryProfile());
    }
}
