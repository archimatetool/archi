/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IRelationship;

@RunWith(Parameterized.class)
public class AllRelationshipTypeTests extends RelationshipTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllRelationshipTypeTests.class);
    }
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        return Arrays.asList(new EClass[][] {
                { IArchimatePackage.eINSTANCE.getSpecialisationRelationship() },
                { IArchimatePackage.eINSTANCE.getCompositionRelationship() },
                { IArchimatePackage.eINSTANCE.getAggregationRelationship() },
                { IArchimatePackage.eINSTANCE.getAssignmentRelationship() },
                { IArchimatePackage.eINSTANCE.getRealisationRelationship() },
                { IArchimatePackage.eINSTANCE.getTriggeringRelationship() },
                { IArchimatePackage.eINSTANCE.getFlowRelationship() },
                { IArchimatePackage.eINSTANCE.getUsedByRelationship() },
                { IArchimatePackage.eINSTANCE.getAccessRelationship() },
                { IArchimatePackage.eINSTANCE.getAssociationRelationship() },
                { IArchimatePackage.eINSTANCE.getInfluenceRelationship() }
        });
    }
    
    private EClass eClass;
    
    public AllRelationshipTypeTests(EClass eClass) {
        this.eClass = eClass;
    }
    
    @Override
    protected IRelationship getArchimateElement() {
        return (IRelationship)IArchimateFactory.eINSTANCE.create(eClass);
    }

    @Test
    public void testGetInterfaceType() {
        // Only IAccessRelationship type
        Assume.assumeTrue(relationship instanceof IAccessRelationship);

        IAccessRelationship aRelationship = (IAccessRelationship)relationship;
        assertEquals(IAccessRelationship.WRITE_ACCESS, aRelationship.getAccessType());
        aRelationship.setAccessType(IAccessRelationship.READ_ACCESS);
        assertEquals(IAccessRelationship.READ_ACCESS, aRelationship.getAccessType());
    }
}
