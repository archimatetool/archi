/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;



/**
 * ArchimateModelUtils Tests
 *
 * @author Phillip Beauvoir
 */
public class DerivedRelationsUtilsTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DerivedRelationsUtilsTests.class);
    }
    
    @Test
    public void getDerivedRelationships() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(element2).getElements().add(element2);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation1.setSource(element1);
        relation1.setTarget(element2);
        IFolder folder = model.addDerivedRelationsFolder();
        folder.getElements().add(relation1);
        
        assertEquals(1, ArchimateModelUtils.getRelationships(element1).size());
        assertEquals(1, ArchimateModelUtils.getRelationships(element2).size());

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
        model.getFolder(FolderType.DERIVED).getElements().add(relation2);
       
        assertEquals(2, ArchimateModelUtils.getRelationships(element1).size());
        assertEquals(2, ArchimateModelUtils.getRelationships(element2).size());
    }
    
    @Test
    public void isInDerivedChain1_TestForCircular() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement businessProcess = IArchimateFactory.eINSTANCE.createBusinessProcess();
        model.getDefaultFolderForElement(businessProcess).getElements().add(businessProcess);
        
        IArchimateElement applicationService = IArchimateFactory.eINSTANCE.createApplicationService();
        model.getDefaultFolderForElement(applicationService).getElements().add(applicationService);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        model.getDefaultFolderForElement(relation1).getElements().add(relation1);

        relation1.setSource(businessProcess);
        relation1.setTarget(applicationService);
        
        IRelationship relation2 = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);

        relation2.setSource(applicationService);
        relation2.setTarget(businessProcess);
        
        assertFalse(DerivedRelationsUtils.isInDerivedChain(relation1));
        assertFalse(DerivedRelationsUtils.isInDerivedChain(relation2));
    }
    
    @Test
    public void isInDerivedChain2_TestForBidirectional1() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement applicationComponent = IArchimateFactory.eINSTANCE.createApplicationComponent();
        model.getDefaultFolderForElement(applicationComponent).getElements().add(applicationComponent);
        
        IArchimateElement device = IArchimateFactory.eINSTANCE.createDevice();
        model.getDefaultFolderForElement(device).getElements().add(device);
        
        IArchimateElement network = IArchimateFactory.eINSTANCE.createNetwork();
        model.getDefaultFolderForElement(network).getElements().add(network);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        model.getDefaultFolderForElement(relation1).getElements().add(relation1);

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);

        relation1.setSource(device);
        relation1.setTarget(applicationComponent);
        relation2.setSource(device);
        relation2.setTarget(network);
        
        assertFalse(DerivedRelationsUtils.isInDerivedChain(relation1));
        assertFalse(DerivedRelationsUtils.isInDerivedChain(relation2));
        
        relation1.setSource(applicationComponent);
        relation1.setTarget(device);
        relation2.setSource(network);
        relation2.setTarget(device);
        
        assertFalse(DerivedRelationsUtils.isInDerivedChain(relation1));
        assertFalse(DerivedRelationsUtils.isInDerivedChain(relation2));
    }

    @Test
    public void isInDerivedChain2_TestForBidirectional2() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(actor).getElements().add(actor);
        
        IArchimateElement role = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(role).getElements().add(role);
        
        IArchimateElement service = IArchimateFactory.eINSTANCE.createBusinessService();
        model.getDefaultFolderForElement(service).getElements().add(service);
        
        IArchimateElement process = IArchimateFactory.eINSTANCE.createBusinessProcess();
        model.getDefaultFolderForElement(process).getElements().add(process);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        model.getDefaultFolderForElement(relation1).getElements().add(relation1);

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);
        
        IRelationship relation3 = IArchimateFactory.eINSTANCE.createRealisationRelationship();
        model.getDefaultFolderForElement(relation2).getElements().add(relation3);

        relation1.setSource(actor);
        relation1.setTarget(role);
        relation2.setSource(service);
        relation2.setTarget(role);
        relation3.setSource(process);
        relation3.setTarget(service);
        
        assertTrue(DerivedRelationsUtils.isInDerivedChain(relation1));
        assertTrue(DerivedRelationsUtils.isInDerivedChain(relation2));
        assertTrue(DerivedRelationsUtils.isInDerivedChain(relation3));
    }
} 
