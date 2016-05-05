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

import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


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
    private IViewpoint fViewpoint;
    private int fOrientation = DIR_BOTH;
    
    public void setViewpointFilter(IViewpoint vp) {
        fViewpoint = vp;
    }
    
    public void setOrientation(int orientation) {
    	if(orientation == DIR_BOTH || orientation == DIR_IN || orientation == DIR_OUT) {
    	    fOrientation = orientation;
    	}
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
        if(inputElement instanceof IArchimateComponent) {
            IArchimateComponent archimateComponent = (IArchimateComponent)inputElement;
            
            // Check if it was deleted
            if(archimateComponent.eContainer() == null) {
                return new Object[0];
            }
            
            // Relationship
            if(archimateComponent instanceof IRelationship) {
                return new Object[]  { inputElement };
            }

            // Element - Get its relationships
            List<IRelationship> mainList = new ArrayList<IRelationship>();
            getRelations(mainList, new ArrayList<IArchimateElement>(), (IArchimateElement)archimateComponent, 0);
            
            return mainList.toArray();
        }
        
        return new Object[0];
    }
    
    /**
     * Get all relations from source and target of element and add to list, no more than DEPTH
     */
    private void getRelations(List<IRelationship> mainList, List<IArchimateElement> checkList, IArchimateElement element, int count) {
        if(checkList.contains(element)) {
            return;
        }
        
        checkList.add(element);
        
        if(count > fDepth) {
            return;
        }
        count++;
        
        List<IRelationship> list = ArchimateModelUtils.getRelationships(element);
        
        for(IRelationship relationship : list) {
        	if(fViewpoint.isAllowedType(relationship.eClass())) {
	            //IArchimateElement source = relationship.getSource();
	            //IArchimateElement target = relationship.getTarget();
	            IArchimateElement other = relationship.getSource().equals(element) ? relationship.getTarget() : relationship.getSource();
	            int direction = relationship.getSource().equals(element) ? DIR_OUT : DIR_IN;
	            
	            if(!mainList.contains(relationship) && fViewpoint.isAllowedType(other.eClass())) {
	            	if(direction == fOrientation || fOrientation == DIR_BOTH) {
	            		mainList.add(relationship);
	            	}
	            }
	            
	            if(fViewpoint.isAllowedType(other.eClass())) {
	            	if(direction == fOrientation || fOrientation == DIR_BOTH) {
	            		getRelations(mainList, checkList, other, count);
	            	}
	            }
	            /*
	            if(fViewpoint.isAllowedType(((IArchimateElement)target).eClass())) {
	            	getRelations(mainList, checkList, target, count);	
	            } */
        	}
        }
    }
    
    @Override
    public Object getSource(Object rel) {
        if(rel instanceof IRelationship) {
            return ((IRelationship)rel).getSource();
        }
        return null;
    }

    @Override
    public Object getDestination(Object rel) {
        if(rel instanceof IRelationship) {
            return ((IRelationship)rel).getTarget();
        }
        return null;
    }
}
