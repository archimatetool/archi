/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.archimatetool.editor.diagram.commands.CreateDiagramConnectionCommand;
import com.archimatetool.editor.diagram.commands.ReconnectDiagramConnectionCommand;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;


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
            cmd = new CreateDiagramConnectionCommand(request);
            cmd.setSource(source);
            request.setStartCommand(cmd);
        }
        
        return cmd;
    }

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        IConnectable source = (IConnectable)request.getSourceEditPart().getModel();
        IConnectable target = (IConnectable)getHost().getModel();
        EClass relationshipType = (EClass)request.getNewObjectType();
        
        CreateDiagramConnectionCommand cmd = null;
        
        if(isValidConnection(source, target, relationshipType)) {
            // Pick up the command that was created in getConnectionCreateCommand(CreateConnectionRequest request)
            cmd = (CreateDiagramConnectionCommand)request.getStartCommand();
            cmd.setTarget(target);
        }
        
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
        
        if(isValidConnectionTarget(newTarget, connection.eClass())) {
            cmd = new ReconnectDiagramConnectionCommand(connection);
            cmd.setNewTarget(newTarget);
        }
       
        return cmd;
    }
    
    protected boolean isValidConnectionSource(IConnectable source, EClass relationshipType) {
        return true;
    }
    
    protected boolean isValidConnectionTarget(IConnectable target, EClass relationshipType) {
        return true;
    }
    
    protected boolean isValidConnection(IConnectable source, IConnectable target, EClass relationshipType) {
        return isValidConnectionSource(source, relationshipType) && isValidConnectionTarget(target, relationshipType);
    }
    
}
