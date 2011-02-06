/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramConnectionCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramConnectionSourceCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramConnectionTargetCommand;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * A policy which allows a component to be connected to another component via a connection
 * 
 * @author Phillip Beauvoir
 */
public class SketchConnectionPolicy extends GraphicalNodeEditPolicy {
    
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        CreateDiagramConnectionCommand cmd = new CreateDiagramConnectionCommand(request);
        cmd.setSource((IDiagramModelObject)getHost().getModel());
        request.setStartCommand(cmd);
        return cmd;
    }

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        // Pick up the command that was created in getConnectionCreateCommand(CreateConnectionRequest request)
        CreateDiagramConnectionCommand cmd = (CreateDiagramConnectionCommand)request.getStartCommand();
        cmd.setTarget((IDiagramModelObject)getHost().getModel());
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        IDiagramModelObject newSource = (IDiagramModelObject)getHost().getModel();
        return new ReconnectDiagramConnectionSourceCommand(connection, newSource);
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        IDiagramModelObject newTarget = (IDiagramModelObject)getHost().getModel();
        return new ReconnectDiagramConnectionTargetCommand(connection, newTarget);
    }
}
