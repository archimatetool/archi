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

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Graph Viewer Content Provider
 * 
 * @author Phillip Beauvoir
 */
public class ZestViewerContentProvider implements IGraphContentProvider {
    
    private int fDepth = 0;
    
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
        if(inputElement instanceof IArchimateElement) {
            IArchimateElement element = (IArchimateElement)inputElement;
            
            // Check if it was deleted
            if(element.eContainer() == null) {
                return new Object[0];
            }
            
            // Relationship
            if(element instanceof IRelationship) {
                return new Object[]  { inputElement };
            }

            // Element - Get its relationships
            List<IRelationship> mainList = new ArrayList<IRelationship>();
            getRelations(mainList, new ArrayList<IArchimateElement>(), element, 0);
            
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
        
        List<IRelationship> list = ArchimateModelUtils.getRelationships(element);
        for(IRelationship relationship : list) {
            if(!mainList.contains(relationship)) {
                mainList.add(relationship);
            }
        }
        
        count++;
        if(count > fDepth) {
            return;
        }
        
        checkList.add(element);
        
        for(IRelationship relationship : list) {
            IArchimateElement source = relationship.getSource();
            IArchimateElement target = relationship.getTarget();
            
            getRelations(mainList, checkList, source, count);
            getRelations(mainList, checkList, target, count);
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
