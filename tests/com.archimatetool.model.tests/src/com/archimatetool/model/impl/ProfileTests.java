/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProfile;

import junit.framework.JUnit4TestAdapter;



@SuppressWarnings("nls")
public class ProfileTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ProfileTests.class);
    }
    
    IArchimateModel model;
    IProfile profile;
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @Before
    public void runBeforeEachTest() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        profile = IArchimateFactory.eINSTANCE.createProfile();
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void isMember() {
        assertEquals(0, model.getProfiles().size());
        model.getProfiles().add(profile);
        assertSame(profile, model.getProfiles().get(0));
    }

    @Test
    public void getName() {
        CommonTests.testGetName(profile);
    }

    @Test
    public void getID() {
        assertNotNull(profile.getId());
    }

    @Test
    public void getAdapter() {
        CommonTests.testGetAdapter(profile);
    }
        
    @Test
    public void getArchimateModel() {
        assertNull(profile.getArchimateModel());
        model.getProfiles().add(profile);
        assertSame(model, profile.getArchimateModel());
    }

    @Test
    public void isSpecialization() {
        assertTrue(profile.isSpecialization());
        profile.setSpecialization(false);
        assertFalse(profile.isSpecialization());
    }
    
    @Test
    public void getConceptType() {
        assertNull(profile.getConceptType());
        profile.setConceptType(IArchimatePackage.eINSTANCE.getBusinessActor().getName());
        assertEquals("BusinessActor", profile.getConceptType());
    }
    
    @Test
    public void getConceptClass() {
        assertNull(profile.getConceptClass());
        profile.setConceptType(IArchimatePackage.eINSTANCE.getBusinessActor().getName());
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessActor(), profile.getConceptClass());
    }

    @Test
    public void getImagePath() {
        assertNull(profile.getImagePath());
        profile.setImagePath("somePath");
        assertEquals("somePath", profile.getImagePath());
    }
    
    /**
     * Add and remove profiles using ECoreUtil methods to capture usage and delete
     */
    @Test
    public void addAndRemoveProfiles() {
        // Add the profile to the model
        model.getProfiles().add(profile);
        
        // Create 3 concepts
        IArchimateConcept concept1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateConcept concept2 = IArchimateFactory.eINSTANCE.createBusinessEvent();
        IArchimateConcept concept3 = IArchimateFactory.eINSTANCE.createBusinessFunction();
        
        // Add the concepts to the model folder
        IFolder folder = model.getFolder(FolderType.BUSINESS);
        folder.getElements().add(concept1);
        folder.getElements().add(concept2);
        folder.getElements().add(concept3);
        
        // Add the profile to the concepts
        concept1.getProfiles().add(profile);
        concept2.getProfiles().add(profile);
        concept3.getProfiles().add(profile);
        
        // There should be 3 usages of the profile (the concepts)
        Collection<Setting> usages = UsageCrossReferencer.find(profile, model);
        assertEquals(3, usages.size());
        
        // Delete the profile from the model using EcoreUtil
        EcoreUtil.delete(profile);
        
        // Not in the model
        assertEquals(0, model.getProfiles().size());
        
        // And zero usages
        usages = UsageCrossReferencer.find(profile, model);
        assertEquals(0, usages.size());
        
        // And to be sure...
        assertEquals(0, concept1.getProfiles().size());
        assertEquals(0, concept2.getProfiles().size());
        assertEquals(0, concept3.getProfiles().size());
    }
}
