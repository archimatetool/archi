/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;


/**
 * Graph Viewer Content Provider
 * 
 * @author Phillip Beauvoir
 * @author Jean-Baptiste Sarrodie
 */
public class ZestViewerContentProvider implements IGraphContentProvider {
	final static int DIR_BOTH = 1;
	final static int DIR_IN = 2;
	final static int DIR_OUT = 3;
    
    private int fDepth = 0;
    private IViewpoint fViewpoint = ViewpointManager.NONE_VIEWPOINT;
    private Set<EClass> fElementClasses = new LinkedHashSet<>();
    private Set<EClass> fRelationshipClasses = new LinkedHashSet<>();
    private int fDirection = DIR_BOTH;
    
    public void setViewpointFilter(IViewpoint vp) {
        assert(vp != null);
        fViewpoint = vp;
    }
    
    public IViewpoint getViewpointFilter() {
        return fViewpoint;
    }
    
    public void addElementFilter(EClass elementClass) {
        if(elementClass == null) {
            fElementClasses.clear();
        }
        else {
            fElementClasses.add(elementClass);
        }
    }
    
    public void removeElementFilter(EClass elementClass) {
        if(elementClass != null) {
            fElementClasses.remove(elementClass);
        }
    }
    
    public Set<EClass> getElementFilters() {
        return fElementClasses;
    }
    
    public void addRelationshipFilter(EClass relationshipClass) {
        if(relationshipClass == null) {
            fRelationshipClasses.clear();
        }
        else {
            fRelationshipClasses.add(relationshipClass);
        }
    }
    
    public void removeRelationshipFilter(EClass relationshipClass) {
        if(relationshipClass != null) {
            fRelationshipClasses.remove(relationshipClass);
        }
    }

    public Set<EClass> getRelationshipFilters() {
        return fRelationshipClasses;
    }
    
    public void setDirection(int direction) {
    	if(direction == DIR_BOTH || direction == DIR_IN || direction == DIR_OUT) {
    	    fDirection = direction;
    	}
    }
    
    public int getDirection() {
        return fDirection;
    }

    public void setDepth(int depth) {
        fDepth = depth;
    }
    
    public int getDepth() {
        return fDepth;
    }
    
    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
    
    @Override
    public Object[] getElements(Object inputElement) {
        if(inputElement instanceof IArchimateConcept) {
            IArchimateConcept archimateConcept = (IArchimateConcept)inputElement;
            
            // Check if it was deleted
            if(archimateConcept.eContainer() == null) {
                return new Object[0];
            }
            
            // Relationship
            if(archimateConcept instanceof IArchimateRelationship) {
                return new Object[]  { inputElement };
            }

            // Element - Get its relationships
            if(archimateConcept instanceof IArchimateElement) {
                Set<IArchimateRelationship> mainList = new HashSet<>();
                getRelations(mainList, new HashSet<IArchimateConcept>(), archimateConcept, 0);
                return mainList.toArray();
            }
        }
        
        return new Object[0];
    }
    
    /**
     * Get all relations from source and target of concept and add to list, no more than DEPTH
     */
    private void getRelations(Set<IArchimateRelationship> mainList, Set<IArchimateConcept> checkList, IArchimateConcept concept, int count) {
        if(checkList.contains(concept)) {
            return;
        }
        
        checkList.add(concept);
        
        if(count > fDepth) {
            return;
        }
        
        count++;
        
        for(IArchimateRelationship relationship : ArchimateModelUtils.getAllRelationshipsForConcept(concept)) {
            IArchimateConcept other = relationship.getSource().equals(concept) ? relationship.getTarget() : relationship.getSource();
            int direction = relationship.getSource().equals(concept) ? DIR_OUT : DIR_IN;

            if(!mainList.contains(relationship) && fViewpoint.isAllowedConcept(other.eClass()) && isVisible(relationship)) {
                if(direction == fDirection || fDirection == DIR_BOTH) {
                    // If the other concept is an element and is selected to beshown
                    if(other instanceof IArchimateElement && isVisible((IArchimateElement)other)) {
                        mainList.add(relationship);
                    }
                }
            }

            if(fViewpoint.isAllowedConcept(other.eClass()) && isVisible(relationship)) {
                if(direction == fDirection || fDirection == DIR_BOTH) {
                    getRelations(mainList, checkList, other, count);
                }
            }
        }
    }
    
    @Override
    public Object getSource(Object rel) {
        if(rel instanceof IArchimateRelationship) {
            return ((IArchimateRelationship)rel).getSource();
        }
        return null;
    }

    @Override
    public Object getDestination(Object rel) {
        if(rel instanceof IArchimateRelationship) {
            return ((IArchimateRelationship)rel).getTarget();
        }
        return null;
    }
    
    private boolean isVisible(IArchimateElement element) {
        if(fElementClasses.isEmpty()) {
            return true;
        }
        
        return fElementClasses.contains(element.eClass());
    }

	private boolean isVisible(IArchimateRelationship relation) {
	    if(fRelationshipClasses.isEmpty()) {
            return true;
        }
        
        return fRelationshipClasses.contains(relation.eClass());
	}
}
