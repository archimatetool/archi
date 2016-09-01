/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;

import junit.framework.JUnit4TestAdapter;

@RunWith(Parameterized.class)
public class AllArchimateRelationshipTypeTests extends ArchimateRelationshipTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchimateRelationshipTypeTests.class);
    }
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        List<EClass[]> list = new ArrayList<EClass[]>();
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            list.add(new EClass[] { eClass });
        }
        
        return list;
    }
    
    private EClass eClass;
    
    public AllArchimateRelationshipTypeTests(EClass eClass) {
        this.eClass = eClass;
    }
    
    @Override
    protected IArchimateRelationship getArchimateConcept() {
        return (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(eClass);
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
