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
 * Reconnect Source Connection Command
 * 
 * @author Phillip Beauvoir
 */
public class ReconnectDiagramConnectionSourceCommand
extends Command {

    protected IDiagramModelConnection fConnection;
    protected IDiagramModelObject fNewSource;
    protected IDiagramModelObject fOldSource;
    protected IDiagramModelObject fTarget;

    public ReconnectDiagramConnectionSourceCommand(IDiagramModelConnection connection, IDiagramModelObject newSource) {
        if(connection == null) {
            throw new IllegalArgumentException();
        }
        
        fConnection = connection;
        fNewSource = newSource;
        fOldSource = connection.getSource();
        fTarget = connection.getTarget();
        
        setLabel("Change Source Connection");
    }

    @Override
    public boolean canExecute() {
        // If it's the same old source then don't allow
        if(fOldSource == fNewSource) {
            return false;
        }
        
        // Disallow same node connections
        return fNewSource != fTarget;
    }

    @Override
    public void execute() {
        if(canExecute()) {
            fConnection.connect(fNewSource, fTarget);
        }
    }

    /**
     * Reconnect the connection to its original source and target endpoints.
     */
    @Override
    public void undo() {
        fConnection.connect(fOldSource, fTarget);
    }
    
    @Override
    public void dispose() {
        fConnection = null;
        fNewSource = null;
        fOldSource = null;
        fTarget = null;
    }
}
