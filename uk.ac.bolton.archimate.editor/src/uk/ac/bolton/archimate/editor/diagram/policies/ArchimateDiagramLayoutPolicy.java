/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramArchimateObjectCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;


/**
 * Policy for General Diagram
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramLayoutPolicy
extends DiagramLayoutPolicy {
    
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Rectangle bounds = getConstraintFor(request);
        
        if(request.getNewObjectType() instanceof EClass) {
            EClass eClass = (EClass)request.getNewObjectType();
            
            // Archimate type object
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)) {
                return new CreateDiagramArchimateObjectCommand((IDiagramModelContainer)getHost().getModel(), request, bounds);
            }
        }
        
        return super.getCreateCommand(request);
    }
    
    @Override
    protected Dimension getMaximumSizeFor(Object object) {
        // Junctions should not be bigger than their default size
        if(object instanceof EClass && IArchimatePackage.eINSTANCE.getJunctionElement().isSuperTypeOf((EClass)object)) {
            return new Dimension(-1, -1);
        }
        
        return super.getMaximumSizeFor(object);
    }
}

