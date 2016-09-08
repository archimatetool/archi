/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;


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

        // Disallow same node connections if not enabled in Preferences
        boolean allowCircularConnection = Preferences.STORE.getBoolean(IPreferenceConstants.ALLOW_CIRCULAR_CONNECTIONS);
        return allowCircularConnection ? true : fNewSource != fOldTarget;
    }
    
    protected boolean checkTargetConnection() {
        // If it's the same old target then don't allow
        if(fOldTarget == fNewTarget) {
            return false;
        }
        
        // Disallow same node connections if not enabled in Preferences
        boolean allowCircularConnection = Preferences.STORE.getBoolean(IPreferenceConstants.ALLOW_CIRCULAR_CONNECTIONS);
        return allowCircularConnection ? true : fNewTarget != fOldSource;
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
            throw new IllegalStateException("Should not happen"); //$NON-NLS-1$
        }
        
        // If it's a circular connection, add some bendpoints if there are none
        if(fConnection.getSource() == fConnection.getTarget() && fConnection.getBendpoints().size() < 2) {
            if(fBendpointCommand == null) {
                fBendpointCommand = createBendPointsCommand();
            }
            if(fBendpointCommand != null) {
                fBendpointCommand.execute();
            }
        }
    }

    /**
     * Reconnect the connection to its original source and target endpoints.
     */
    @Override
    public void undo() {
        fConnection.connect(fOldSource, fOldTarget);
        
        if(fBendpointCommand != null) {
            fBendpointCommand.undo();
        }
    }
    
    /**
     * Adding a circular connection requires some bendpoints
     */
    protected Command createBendPointsCommand() {
        // Only works for IDiagramModelObject as source and target objects not for connections
        if(!(fConnection.getSource() instanceof IDiagramModelObject) && !(fConnection.getTarget() instanceof IDiagramModelObject)) {
            return null;
        }
        
        IDiagramModelObject source = (IDiagramModelObject)fConnection.getSource();
        IDiagramModelObject target = (IDiagramModelObject)fConnection.getTarget();

        int width = source.getBounds().getWidth();
        if(width == -1) {
            width = 100;
        }
        int height = target.getBounds().getHeight();
        if(height == -1) {
            height = 60;
        }
        
        width = (int)Math.max(100, width * 0.6);
        height = (int)Math.max(60, height * 0.6);
        
        CompoundCommand result = new CompoundCommand();
        
        CreateBendpointCommand cmd = new CreateBendpointCommand();
        cmd.setDiagramModelConnection(fConnection);
        cmd.setRelativeDimensions(new Dimension(width, 0), new Dimension(width, 0));
        result.add(cmd);
        
        cmd = new CreateBendpointCommand();
        cmd.setDiagramModelConnection(fConnection);
        cmd.setRelativeDimensions(new Dimension(width, height), new Dimension(width, height));
        result.add(cmd);
        
        cmd = new CreateBendpointCommand();
        cmd.setDiagramModelConnection(fConnection);
        cmd.setRelativeDimensions(new Dimension(0, height), new Dimension(0, height));
        result.add(cmd);
        
        return result;
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
