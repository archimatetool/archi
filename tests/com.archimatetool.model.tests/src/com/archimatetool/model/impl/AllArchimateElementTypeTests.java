/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.util.ArchimateModelUtils;

public class AllArchimateElementTypeTests extends ArchimateConceptTests {
    
    static Stream<Arguments> getParams() {
        List<Arguments> list = new ArrayList<>();
        
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            list.add(getParam(eClass));
        }
        
        // Junction
        list.add(getParam(IArchimatePackage.eINSTANCE.getJunction()));
        
        return list.stream();
    }

    @Test
    public void testGetJuntion_Type() {
        IJunction junction = IArchimateFactory.eINSTANCE.createJunction();
        assertEquals(IJunction.AND_JUNCTION_TYPE, junction.getType());
        junction.setType(IJunction.OR_JUNCTION_TYPE);
        assertEquals(IJunction.OR_JUNCTION_TYPE, junction.getType());
    }

}
