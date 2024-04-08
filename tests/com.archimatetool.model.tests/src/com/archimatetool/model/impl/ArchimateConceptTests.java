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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProfile;


@SuppressWarnings("nls")
public abstract class ArchimateConceptTests {
    
    static final String PARAMS_METHOD = "getParams";
    
    /**
     * Create an Arguments parameter for an EClass instance
     */
    protected static Arguments getParam(EClass eClass) {
        return Arguments.of(Named.of(eClass.getName(), IArchimateFactory.eINSTANCE.create(eClass)));
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetAdapter(IArchimateConcept concept) {
        CommonTests.testGetAdapter(concept);
    }
        
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetArchimateModel(IArchimateConcept concept) {
        assertNull(concept.getArchimateModel());
        
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        model.getDefaultFolderForObject(concept).getElements().add(concept);
        assertSame(model, concept.getArchimateModel());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
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

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDocumentation(IArchimateConcept concept) {
        CommonTests.testGetDocumentation(concept);
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetID(IArchimateConcept concept) {
        assertNotNull(concept.getId());
    }
        
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetName(IArchimateConcept concept) {
        CommonTests.testGetName(concept);
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetProperties(IArchimateConcept concept) {
        CommonTests.testProperties(concept);
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetSourceRelationships(IArchimateConcept concept) {
        assertEquals(0, concept.getSourceRelationships().size());
        concept.getSourceRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, concept.getSourceRelationships().size());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetTargetRelationships(IArchimateConcept concept) {
        assertEquals(0, concept.getTargetRelationships().size());
        concept.getTargetRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, concept.getTargetRelationships().size());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetProfiles(IArchimateConcept concept) {
        CommonTests.testList(concept.getProfiles(), IArchimatePackage.eINSTANCE.getProfile());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetPrimaryProfile(IArchimateConcept concept) {
        assertNull(concept.getPrimaryProfile());
        
        IProfile profile1 = IArchimateFactory.eINSTANCE.createProfile();
        IProfile profile2 = IArchimateFactory.eINSTANCE.createProfile();
        
        concept.getProfiles().add(profile1);
        concept.getProfiles().add(profile2);
        assertSame(profile1, concept.getPrimaryProfile());
    }
}
