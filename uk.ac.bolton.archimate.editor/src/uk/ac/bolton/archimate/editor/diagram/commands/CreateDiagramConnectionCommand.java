/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * A command to create a connection between two diagram objects
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramConnectionCommand
extends Command {

    protected CreateConnectionRequest fRequest;
    
    protected IDiagramModelConnection fConnection;
    protected IDiagramModelObject fSource;
    protected IDiagramModelObject fTarget;

    /**
     * Instantiate a command that can create a connection between two shapes.
     * @param request the type of connection request
     */
    public CreateDiagramConnectionCommand(CreateConnectionRequest request) {
        fRequest = request;
        setLabel("Create Connection");
    }
    
    /**
     * Set the source endpoint for the connection.
     * @param source that source endpoint
     * @throws IllegalArgumentException if source is null
     */
    public void setSource(IDiagramModelObject source) {
        if(source == null) {
            throw new IllegalArgumentException("Source connected model object cannot be null");
        }
        fSource = source;
    }

    /**
     * Set the target endpoint for the connection.
     * @param target that target endpoint
     * @throws IllegalArgumentException if target is null
     */
    public void setTarget(IDiagramModelObject target) {
        if(target == null) {
            throw new IllegalArgumentException("Target connected model object cannot be null");
        }
        fTarget = target;
    }

    @Override
    public boolean canExecute() {
        if(fTarget == null || fSource == null) {
            return false;
        }
        // Disallow same node connections
        return !fSource.equals(fTarget);
    }

    @Override
    public void execute() {
        fConnection = (IDiagramModelConnection)fRequest.getNewObject();
        fConnection.connect(fSource, fTarget);
    }

    @Override
    public void redo() {
        fConnection.reconnect();
    }
    
    @Override
    public void undo() {
        fConnection.disconnect();
    }
    
    @Override
    public void dispose() {
        fRequest = null;
        fConnection = null;
        fSource = null;
        fTarget = null;
    }
}
