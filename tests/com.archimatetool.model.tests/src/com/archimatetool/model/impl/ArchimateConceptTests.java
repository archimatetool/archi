/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EClass;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.ParamsTest;


@SuppressWarnings("nls")
public abstract class ArchimateConceptTests {
    
    /**
     * Create an Arguments parameter for an EClass instance
     */
    protected static Arguments getParam(EClass eClass) {
        return Arguments.of(Named.of(eClass.getName(), IArchimateFactory.eINSTANCE.create(eClass)));
    }

    @ParamsTest
    public void testGetAdapter(IArchimateConcept concept) {
        CommonTests.testGetAdapter(concept);
    }
        
    @ParamsTest
    public void testGetArchimateModel(IArchimateConcept concept) {
        assertNull(concept.getArchimateModel());
        
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        model.getDefaultFolderForObject(concept).getElements().add(concept);
        assertSame(model, concept.getArchimateModel());
    }
    
    @ParamsTest
    public void testGetCopy(IArchimateConcept concept) {
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

    @ParamsTest
    public void testGetDocumentation(IArchimateConcept concept) {
        CommonTests.testGetDocumentation(concept);
    }

    @ParamsTest
    public void testGetID(IArchimateConcept concept) {
        CommonTests.testGetID(concept);
    }
        
    @ParamsTest
    public void testGetName(IArchimateConcept concept) {
        CommonTests.testGetName(concept);
    }

    @ParamsTest
    public void testGetProperties(IArchimateConcept concept) {
        CommonTests.testProperties(concept);
    }
    
    @ParamsTest
    public void testGetSourceRelationships(IArchimateConcept concept) {
        assertEquals(0, concept.getSourceRelationships().size());
        concept.getSourceRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, concept.getSourceRelationships().size());
    }
    
    @ParamsTest
    public void testGetTargetRelationships(IArchimateConcept concept) {
        assertEquals(0, concept.getTargetRelationships().size());
        concept.getTargetRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, concept.getTargetRelationships().size());
    }
    
    @ParamsTest
    public void testGetProfiles(IArchimateConcept concept) {
        CommonTests.testList(concept.getProfiles(), IArchimatePackage.eINSTANCE.getProfile());
    }
    
    @ParamsTest
    public void testGetPrimaryProfile(IArchimateConcept concept) {
        assertNull(concept.getPrimaryProfile());
        
        IProfile profile1 = IArchimateFactory.eINSTANCE.createProfile();
        IProfile profile2 = IArchimateFactory.eINSTANCE.createProfile();
        
        concept.getProfiles().add(profile1);
        concept.getProfiles().add(profile2);
        assertSame(profile1, concept.getPrimaryProfile());
    }
}
