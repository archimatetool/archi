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

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.util.ArchimateModelUtils;

@RunWith(Parameterized.class)
public class AllArchimateElementTypeTests extends ArchimateConceptTests {
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        List<EClass[]> list = new ArrayList<>();
        
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            list.add(new EClass[] { eClass });
        }
        
        list.add(new EClass[] { IArchimatePackage.eINSTANCE.getJunction() });
        
        return list;
    }
    
    public AllArchimateElementTypeTests(EClass eClass) {
        super(eClass);
    }
    
    @Test
    public void testGetJuntion_Type() {
        Assume.assumeTrue(getConcept() instanceof IJunction);

        IJunction junction = (IJunction)getConcept();
        assertEquals(IJunction.AND_JUNCTION_TYPE, junction.getType());
        junction.setType(IJunction.OR_JUNCTION_TYPE);
        assertEquals(IJunction.OR_JUNCTION_TYPE, junction.getType());
    }

}
