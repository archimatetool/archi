/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IArchimatePackage;


/**
 * Viewpoint
 * 
 * @author Phillip Beauvoir
 */
class Viewpoint implements IViewpoint {
    
    private Set<EClass> classList = new HashSet<EClass>();

    // Default elements in a Viewpoint are Junction and Grouping
    private static Set<EClass> defaultList = Stream.of(IArchimatePackage.eINSTANCE.getJunction(),
                                                       IArchimatePackage.eINSTANCE.getGrouping())
                                                       .collect(Collectors.toCollection(HashSet::new));
    
    private String id;
    private String name;
    
    Viewpoint(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    Set<EClass> getClassList() {
        return classList;
    }
    
    @Override
    public boolean isAllowedConcept(EClass eClass) {
        // Safety
        if(!IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)) {
            return true;
        }
        
        // All concepts allowed
        if(classList.isEmpty()) {
            return true;
        }
        
        return classList.contains(eClass) || defaultList.contains(eClass);
    }
    
    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}