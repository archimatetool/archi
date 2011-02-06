/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Reconnect Target Connection Command
 * 
 * @author Phillip Beauvoir
 */
public class ReconnectDiagramConnectionTargetCommand
extends Command {

    protected IDiagramModelConnection fConnection;
    protected IDiagramModelObject fSource;
    protected IDiagramModelObject fNewTarget;
    protected IDiagramModelObject fOldTarget;

    public ReconnectDiagramConnectionTargetCommand(IDiagramModelConnection connection, IDiagramModelObject newTarget) {
        if(connection == null) {
            throw new IllegalArgumentException();
        }
        
        fConnection = connection;
        fSource = connection.getSource();
        fOldTarget = connection.getTarget();
        fNewTarget = newTarget;
        
        setLabel("Change Target Connection");
    }

    @Override
    public boolean canExecute() {
        // If it's the same old target then don't allow
        if(fOldTarget == fNewTarget) {
            return false;
        }
        
        // Disallow same node connections
        return fNewTarget != fSource;
    }

    /**
     * Reconnect the connection to newSource (if setNewSource(...) was invoked
     * before) or newTarget (if setNewTarget(...) was invoked before).
     */
    @Override
    public void execute() {
        if(canExecute()) {
            fConnection.connect(fSource, fNewTarget);
        }
    }

    /**
     * Reconnect the connection to its original source and target endpoints.
     */
    @Override
    public void undo() {
        fConnection.connect(fSource, fOldTarget);
    }
    
    @Override
    public void dispose() {
        fConnection = null;
        fSource = null;
        fNewTarget = null;
        fOldTarget = null;
    }
}
