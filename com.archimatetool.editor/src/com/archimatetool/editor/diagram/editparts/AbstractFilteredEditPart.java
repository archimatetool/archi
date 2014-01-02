/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Abstract Filtered Edit Part
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractFilteredEditPart extends AbstractGraphicalEditPart {
    
    @Override
    protected List<?> getModelChildren() {
        return getFilteredModelChildren();
    }
    
    protected List<?> getFilteredModelChildren() {
        if(getModel() instanceof IDiagramModelContainer) {
            List<IDiagramModelObject> originalList = ((IDiagramModelContainer)getModel()).getChildren();
            
            IChildEditPartFilter[] filters = getEditPartFilterProvider().getEditPartFilters(IChildEditPartFilter.class);
            if(filters != null) {
                List<IDiagramModelObject> filteredList = new ArrayList<IDiagramModelObject>();
                
                for(IDiagramModelObject object : originalList) {
                    boolean add = true;
                    
                    for(IChildEditPartFilter filter : filters) {
                        add = filter.isChildElementVisible(this, object);
                        if(!add) { // no point in trying the next filter
                            break;
                        }
                    }
                    
                    if(add) {
                        filteredList.add(object);
                    }
                }
                
                return filteredList;
            }
            
            return originalList;
        }
        
        return Collections.EMPTY_LIST;
    }

    protected IEditPartFilterProvider getEditPartFilterProvider() {
        if(this instanceof IEditPartFilterProvider) {
            return (IEditPartFilterProvider)this;
        }
        if(getParent() instanceof AbstractFilteredEditPart) {
            return ((AbstractFilteredEditPart)getParent()).getEditPartFilterProvider();
        }
        return null;
    }
}