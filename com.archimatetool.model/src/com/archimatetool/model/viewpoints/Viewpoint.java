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
    
    private Set<EClass> elementsClassList = new HashSet<EClass>();
    private Set<EClass> relationsClassList = new HashSet<EClass>();

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
    
    void addEClass(EClass eClass) {
        if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)) {
            elementsClassList.add(eClass);
        }
        else if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass)) {
            relationsClassList.add(eClass);
        }
    }
    
    @Override
    public boolean isAllowedConcept(EClass eClass) {
        // Safety
        if(!IArchimatePackage.eINSTANCE.getArchimateConcept().isSuperTypeOf(eClass)) {
            return true;
        }
        
        // If elements list is empty all are allowed
        if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass) && elementsClassList.isEmpty()) {
            return true;
        }
        
        // If relations list is empty all are allowed
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass) && relationsClassList.isEmpty()) {
            return true;
        }
        
        return elementsClassList.contains(eClass) || relationsClassList.contains(eClass) || defaultList.contains(eClass);
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