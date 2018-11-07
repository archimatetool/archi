/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IArchimatePackage;


/**
 * Viewpoint
 * 
 * @author Phillip Beauvoir
 */
class Viewpoint implements IViewpoint {
    
    private List<EClass> classList = new ArrayList<EClass>();

    private static List<EClass> defaultList = new ArrayList<EClass>();
    static {
        defaultList.add(IArchimatePackage.eINSTANCE.getJunction());
        defaultList.add(IArchimatePackage.eINSTANCE.getGrouping());
    }
    
    private String id;
    private String name;
    
    Viewpoint(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    List<EClass> getClassList() {
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