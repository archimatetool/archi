/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.util.ArrayList;
import java.util.List;

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
    private int fDirection = DIR_BOTH;
    
    public void setViewpointFilter(IViewpoint vp) {
        assert(vp != null);
        fViewpoint = vp;
    }
    
    public IViewpoint getViewpointFilter() {
        return fViewpoint;
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
    
    // TODO: A3 get relations->relations

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
                getRelations(mainList, new ArrayList<IArchimateElement>(), (IArchimateElement)archimateConcept, 0);
                return mainList.toArray();
            }
        }
        
        return new Object[0];
    }
    
    /**
     * Get all relations from source and target of element and add to list, no more than DEPTH
     */
    private void getRelations(List<IArchimateRelationship> mainList, List<IArchimateElement> checkList, IArchimateElement element, int count) {
        if(checkList.contains(element)) {
            return;
        }
        
        checkList.add(element);
        
        if(count > fDepth) {
            return;
        }
        count++;
        
        List<IArchimateRelationship> list = ArchimateModelUtils.getAllRelationshipsForConcept(element);
        
        for(IArchimateRelationship relationship : list) {
            IArchimateConcept other = relationship.getSource().equals(element) ? relationship.getTarget() : relationship.getSource();
            int direction = relationship.getSource().equals(element) ? DIR_OUT : DIR_IN;

            if(!mainList.contains(relationship) && fViewpoint.isAllowedConcept(other.eClass())) {
                if(direction == fDirection || fDirection == DIR_BOTH) {
                    mainList.add(relationship);
                }
            }

            if(fViewpoint.isAllowedConcept(other.eClass()) && other instanceof IArchimateElement) {
                if(direction == fDirection || fDirection == DIR_BOTH) {
                    getRelations(mainList, checkList, (IArchimateElement)other, count);
                }
            }
            /*
	            if(fViewpoint.isAllowedConcept(((IArchimateElement)target).eClass())) {
	            	getRelations(mainList, checkList, target, count);	
	            } */
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
}
