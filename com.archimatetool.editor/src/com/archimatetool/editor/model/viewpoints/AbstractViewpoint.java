/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Abstract Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractViewpoint implements IViewpoint {
    
    private List<EClass> fClassList;
    
    @Override
    public boolean isElementVisible(EObject object) {
        if(getAllowedList() == null) {
            return true;
        }
        
        EClass eClass = null;
        
        if(object instanceof IDiagramModelArchimateObject) {
            eClass = ((IDiagramModelArchimateObject)object).getArchimateElement().eClass();
        }
        else if(object instanceof IArchimateElement) {
            eClass = object.eClass();
        }
        
        // eClass is IArchimateElement type
        if(eClass != null && !getAllowedList().contains(eClass)) {
            return false;
        }
        
        // It may be that the parent diagram object is hidden, in which case this child object needs to be hidden too
        // even if it does belong in the Viewpoint
        if(object.eContainer() instanceof IDiagramModelObject) {
            return isElementVisible(object.eContainer());
        }
        
        return true;
    }
    
    @Override
    public boolean isAllowedType(EClass type) {
        // Only Archimate types and relations
        if(!IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(type)) {
            return false;
        }
        
        return getAllowedList() == null ? true : getAllowedList().contains(type);
    };
    
    /**
     * @return A list of allowed types or null
     */
    protected List<EClass> getAllowedList() {
        if(getAllowedTypes() != null && fClassList == null) {
            fClassList = Arrays.asList(getAllowedTypes());
        }
        return fClassList;
    }
}