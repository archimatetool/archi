/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Reconnect Diagram Connection Command
 * 
 * @author Phillip Beauvoir
 */
public class ReconnectDiagramConnectionCommand
extends Command {

    protected IDiagramModelConnection fConnection;
    
    protected IConnectable fNewSource;
    protected IConnectable fNewTarget;
    protected IConnectable fOldSource;
    protected IConnectable fOldTarget;
    
    /**
     * Extra bendpoints were added as a result of a circular connection
     */
    protected Command fBendpointCommand;

    public ReconnectDiagramConnectionCommand(IDiagramModelConnection connection) {
        if(connection == null) {
            throw new IllegalArgumentException();
        }
        
        fConnection = connection;
        fOldSource = connection.getSource();
        fOldTarget = connection.getTarget();
    }
    
    public void setNewSource(IConnectable source) {
        fNewSource = source;
        fNewTarget = null;
        setLabel(Messages.ReconnectDiagramConnectionCommand_0);
    }

    public void setNewTarget(IConnectable target) {
        fNewTarget = target;
        fNewSource = null;
        setLabel(Messages.ReconnectDiagramConnectionCommand_1);
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

        return true;
    }
    
    protected boolean checkTargetConnection() {
        // If it's the same old target then don't allow
        if(fOldTarget == fNewTarget) {
            return false;
        }
        
        return true;
    }

    @Override
    public void execute() {
        doConnection();

        // If it's a circular connection, add some bendpoints if there are none
        if(fConnection.getSource() == fConnection.getTarget() && fConnection.getBendpoints().size() < 1) {
            fBendpointCommand = CreateDiagramConnectionCommand.createBendPointsForCircularConnectionCommand(fConnection);
            if(fBendpointCommand != null) {
                fBendpointCommand.execute();
            }
        }
    }
    
    @Override
    public void redo() {
        doConnection();
        
        if(fBendpointCommand != null) {
            fBendpointCommand.redo();
        }
    }

    /**
     * Reconnect the connection to its original source and target endpoints.
     */
    @Override
    public void undo() {
        if(fBendpointCommand != null) {
            fBendpointCommand.undo();
        }

        fConnection.connect(fOldSource, fOldTarget);
    }
    
    protected void doConnection() {
        if(fNewSource != null) {
            fConnection.connect(fNewSource, fOldTarget);
        }
        else if(fNewTarget != null) {
            fConnection.connect(fOldSource, fNewTarget);
        }
        else {
            throw new IllegalStateException("Should not happen"); //$NON-NLS-1$
        }
    }
    
    @Override
    public void dispose() {
        fConnection = null;
        fNewSource = null;
        fNewTarget = null;
        fOldSource = null;
        fOldTarget = null;
    }
}
