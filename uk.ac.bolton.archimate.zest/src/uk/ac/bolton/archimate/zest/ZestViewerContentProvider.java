/*******************************************************************************
 * Copyright (c) 2010-12 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.zest;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * Graph Viewer Content Provider
 * 
 * @author Phillip Beauvoir
 */
public class ZestViewerContentProvider implements IGraphContentProvider {

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if(inputElement instanceof IArchimateElement && !(inputElement instanceof IRelationship)) {
            IArchimateElement element = (IArchimateElement)inputElement;
            
            // Check if it was deleted
            if(element.eContainer() == null) {
                return new Object[0];
            }

            List<IRelationship> list = ArchimateModelUtils.getRelationships(element);
            return list.toArray();
        }
        
        return new Object[0];
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
