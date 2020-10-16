/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateObjectCommand;
import com.archimatetool.model.IArchimatePackage;



/**
 * Policy for Archimate Diagram
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramLayoutPolicy
extends DiagramLayoutPolicy {
    
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if(request.getNewObjectType() instanceof EClass) {
            EClass eClass = (EClass)request.getNewObjectType();
            
            // Archimate type object
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)) {
                Rectangle bounds = (Rectangle)getConstraintFor(request);
                return new CreateDiagramArchimateObjectCommand(getHost(), request, bounds);
            }
        }
        
        return super.getCreateCommand(request);
    }
}

