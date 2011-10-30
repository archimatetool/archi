/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramConnectionCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramConnectionCommand;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * A policy which allows a component to be connected to another component via a connection
 * 
 * @author Phillip Beauvoir
 */
public class BasicConnectionPolicy extends GraphicalNodeEditPolicy {
    
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        CreateDiagramConnectionCommand cmd = null;
        
        EClass classType = (EClass)request.getNewObjectType();
        IDiagramModelObject source = (IDiagramModelObject)getHost().getModel();
        
        if(isValidConnectionSource(source, classType)) {
            cmd = new CreateLineConnectionCommand(request);
            cmd.setSource(source);
            request.setStartCommand(cmd);
        }
        
        return cmd;
    }

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        // Pick up the command that was created in getConnectionCreateCommand(CreateConnectionRequest request)
        CreateDiagramConnectionCommand cmd = (CreateDiagramConnectionCommand)request.getStartCommand();
        IDiagramModelObject target = (IDiagramModelObject)getHost().getModel();
        cmd.setTarget(target);
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        ReconnectDiagramConnectionCommand cmd = null;
        
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        IDiagramModelObject newSource = (IDiagramModelObject)getHost().getModel();
        
        if(isValidConnectionSource(newSource, connection.eClass())) {
            cmd = new ReconnectDiagramConnectionCommand(connection);
            cmd.setNewSource(newSource);
        }
       
        return cmd;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        ReconnectDiagramConnectionCommand cmd = null;
        
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        IDiagramModelObject newTarget = (IDiagramModelObject)getHost().getModel();
        
        if(isValidConnectionSource(newTarget, connection.eClass())) {
            cmd = new ReconnectDiagramConnectionCommand(connection);
            cmd.setNewTarget(newTarget);
        }
       
        return cmd;
    }
    
    protected boolean isValidConnectionSource(IDiagramModelObject source, EClass relationshipType) {
        return true;
    }
    
    protected boolean isValidConnectionTarget(IDiagramModelObject target, EClass relationshipType) {
        return true;
    }
    
    protected boolean isValidConnection(IDiagramModelObject source, IDiagramModelObject target, EClass relationshipType) {
        return isValidConnectionSource(source, relationshipType) && isValidConnectionTarget(target, relationshipType);
    }
    
    // ==================================================================================================
    // ============================================= Commands ===========================================
    // ==================================================================================================
    
    /*
     * Command to create a line connection
     */
    private class CreateLineConnectionCommand extends CreateDiagramConnectionCommand {
        public CreateLineConnectionCommand(CreateConnectionRequest request) {
            super(request);
        }
        
        @Override
        public boolean canExecute() {
            if(super.canExecute()) {
                EClass classType = (EClass)fRequest.getNewObjectType();
                return isValidConnection(fSource, fTarget, classType);
            }
            return false;
        }  
    }
}
