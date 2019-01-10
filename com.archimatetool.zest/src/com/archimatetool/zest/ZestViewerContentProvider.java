/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.util.ArrayList;
import java.util.List;

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
    private EClass fElementClass;
    private EClass fRelationshipClass;
    private int fDirection = DIR_BOTH;
    
    public void setViewpointFilter(IViewpoint vp) {
        assert(vp != null);
        fViewpoint = vp;
    }
    
    public IViewpoint getViewpointFilter() {
        return fViewpoint;
    }
    
    public void setElementFilter(EClass elementClass) {
        fElementClass = elementClass;
    }
    
    public EClass getElementFilter() {
        return fElementClass;
    }
    
    public void setRelationshipFilter(EClass relationshipClass) {
        fRelationshipClass = relationshipClass;
    }
    
    public EClass getRelationshipFilter() {
        return fRelationshipClass;
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
                List<IArchimateRelationship> mainList = new ArrayList<IArchimateRelationship>();
                getRelations(mainList, new ArrayList<IArchimateConcept>(), archimateConcept, 0);
                return mainList.toArray();
            }
        }
        
        return new Object[0];
    }
    
    /**
     * Get all relations from source and target of concept and add to list, no more than DEPTH
     */
    private void getRelations(List<IArchimateRelationship> mainList, List<IArchimateConcept> checkList, IArchimateConcept concept, int count) {
        if(checkList.contains(concept)) {
            return;
        }
        
        checkList.add(concept);
        
        if(count > fDepth) {
            return;
        }
        
        count++;
        
        List<IArchimateRelationship> allRelationships = ArchimateModelUtils.getAllRelationshipsForConcept(concept);
        
        for(IArchimateRelationship relationship : allRelationships) {
            IArchimateConcept other = relationship.getSource().equals(concept) ? relationship.getTarget() : relationship.getSource();
            int direction = relationship.getSource().equals(concept) ? DIR_OUT : DIR_IN;

            if(!mainList.contains(relationship) && fViewpoint.isAllowedConcept(other.eClass()) && !isFilteredByRelationship(relationship)) {
                if(direction == fDirection || fDirection == DIR_BOTH) {
                    // If the other concept is an element and is filtered
                    if(other instanceof IArchimateElement && !isFilteredByElement((IArchimateElement)other)) {
                        mainList.add(relationship);
                    }
                }
            }

            if(fViewpoint.isAllowedConcept(other.eClass()) && !isFilteredByRelationship(relationship)) {
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
    
    private boolean isFilteredByElement(IArchimateElement element) {
        if(fElementClass == null) {
            return false;
        }
        return fElementClass != element.eClass();
    }

	private boolean isFilteredByRelationship(IArchimateRelationship relation) {
		if(fRelationshipClass == null){
			return false;
		}
		return fRelationshipClass != relation.eClass();
	}

}
