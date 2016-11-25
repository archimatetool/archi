/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.EditPart;

import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.viewpoints.ViewpointManager;


/**
 * Viewpoint Filter for EditParts
 * 
 * This will query the current viewpoint (if any) as to whether the child object is
 * to be shown in the parent EditPart.
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointEditPartFilter implements IChildEditPartFilter, IConnectionEditPartFilter {
    
    @Override
    public boolean isChildElementVisible(EditPart parentEditPart, Object childObject) {   	
    	return isConceptVisible(childObject);
    }

    @Override
    public boolean isConnectionVisible(EditPart editPart, IDiagramModelConnection connection) {
    	return isConceptVisible(connection);
    }
    
    private boolean isConceptVisible(Object childObject) {   	
    	if(childObject instanceof IDiagramModelComponent) {
    		return ViewpointManager.INSTANCE.isAllowedDiagramModelComponent((IDiagramModelComponent) childObject);
    	}
        
        return true;
    }
}