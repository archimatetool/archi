/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IDiagramModelConnection;



/**
 * Delete Diagram Connection Command
 * 
 * @author Phillip Beauvoir
 */
class DeleteDiagramConnectionCommand extends Command {
	
	private IDiagramModelConnection connection;
	
    // Store the positions of the connection in their lists
	private int oldSourcePosition;
	private int oldTargetPosition;
	
	/** 
	 * Create a command that will disconnect a connection from its endpoints.
	 * @param connection the connection instance to disconnect (non-null)
	 */
	public DeleteDiagramConnectionCommand(IDiagramModelConnection connection){
	    this.connection = connection;
	}
	
	@Override
    public void execute() {
	    // store these here
	    oldSourcePosition = connection.getSource().getSourceConnections().indexOf(connection);
        oldTargetPosition = connection.getTarget().getTargetConnections().indexOf(connection);
	    
        connection.disconnect();
	}
	
	@Override
    public void undo() {
	    connection.reconnect();
	    
	    // restore these
	    EList<IDiagramModelConnection> sources = connection.getSource().getSourceConnections();
        if(oldSourcePosition >= 0 && oldSourcePosition < sources.size() && sources.contains(connection)) {
            sources.move(oldSourcePosition, connection);
        }
        
        EList<IDiagramModelConnection> targets = connection.getTarget().getTargetConnections();
        if(oldTargetPosition >= 0 && oldTargetPosition < targets.size() && targets.contains(connection)) {
            targets.move(oldTargetPosition, connection);
        }
	}

    @Override
	public void dispose() {
        connection = null;
	}
}
