/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.requests.CreateConnectionRequest;

import uk.ac.bolton.archimate.editor.diagram.policies.ConnectionManager;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;

/**
 * A command to create a connection between two Archimate Model diagram objects
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramArchimateConnectionCommand
extends CreateDiagramConnectionCommand {

    /**
     * Instantiate a command that can create a connection between two shapes.
     * @param request the type of connection request
     */
    public CreateDiagramArchimateConnectionCommand(CreateConnectionRequest request) {
        super(request);
    }
    
    @Override
    public boolean canExecute() {
        if(super.canExecute()) {
            // Check rules
            EClass relationshipType = (EClass)fRequest.getNewObjectType();
            return ConnectionManager.isValidConnection(fSource, fTarget, relationshipType);
        }
        
        return false;
    }

    @Override
    public void execute() {
        super.execute();
        
        // Now add the relationship to the model
        ((IDiagramModelArchimateConnection)fConnection).addRelationshipToModel(null);
    }
    
    @Override
    public void redo() {
        super.redo();
        
        // Now add the relationship to the model
        ((IDiagramModelArchimateConnection)fConnection).addRelationshipToModel(null);
    }
    
    @Override
    public void undo() {
        super.undo();
        
        // Now remove the relationship from its folder
        ((IDiagramModelArchimateConnection)fConnection).removeRelationshipFromModel();
    }
}
