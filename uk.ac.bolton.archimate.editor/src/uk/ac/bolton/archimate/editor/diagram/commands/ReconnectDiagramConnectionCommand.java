/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Reconnect Diagram Connection Command
 * 
 * @author Phillip Beauvoir
 */
public class ReconnectDiagramConnectionCommand
extends Command {

    protected IDiagramModelConnection fConnection;
    
    protected IDiagramModelObject fNewSource;
    protected IDiagramModelObject fNewTarget;
    protected IDiagramModelObject fOldSource;
    protected IDiagramModelObject fOldTarget;

    public ReconnectDiagramConnectionCommand(IDiagramModelConnection connection) {
        if(connection == null) {
            throw new IllegalArgumentException();
        }
        
        fConnection = connection;
        fOldSource = connection.getSource();
        fOldTarget = connection.getTarget();
    }
    
    public void setNewSource(IDiagramModelObject source) {
        fNewSource = source;
        fNewTarget = null;
        setLabel("Change Source Connection");
    }

    public void setNewTarget(IDiagramModelObject target) {
        fNewTarget = target;
        fNewSource = null;
        setLabel("Change Target Connection");
    }

    @Override
    public boolean canExecute() {
        if(fNewSource != null) {
            return checkSourceConnection();
        }
        else if(fNewTarget != null) {
            return checkTargetConnection();
        }
        return false;
    }

    protected boolean checkSourceConnection() {
        // If it's the same old source then don't allow
        if(fOldSource == fNewSource) {
            return false;
        }
        // Disallow same node connections
        return fNewSource != fOldTarget;
    }
    
    protected boolean checkTargetConnection() {
        // If it's the same old target then don't allow
        if(fOldTarget == fNewTarget) {
            return false;
        }
        
        // Disallow same node connections
        return fNewTarget != fOldSource;
    }


    @Override
    public void execute() {
        if(fNewSource != null) {
            fConnection.connect(fNewSource, fOldTarget);
        }
        else if(fNewTarget != null) {
            fConnection.connect(fOldSource, fNewTarget);
        }
        else {
            throw new IllegalStateException("Should not happen");
        }
    }

    /**
     * Reconnect the connection to its original source and target endpoints.
     */
    @Override
    public void undo() {
        fConnection.connect(fOldSource, fOldTarget);
    }
    
    @Override
    public void dispose() {
        fConnection = null;
        fNewSource = null;
        fOldSource = null;
        fOldSource = null;
        fOldTarget = null;
    }
}
