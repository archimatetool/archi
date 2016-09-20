/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IDiagramModelConnection;



/**
 * Delete Diagram Connection Command
 * 
 * @author Phillip Beauvoir
 */
class DeleteDiagramConnectionCommand extends Command {
	
	private IDiagramModelConnection fConnection;
	
	/** 
	 * Create a command that will disconnect a connection from its endpoints.
	 * @param connection the connection instance to disconnect (non-null)
	 */
	public DeleteDiagramConnectionCommand(IDiagramModelConnection connection){
		fConnection = connection;
	}
	
	@Override
    public void execute() {
        fConnection.disconnect();
	}
	
	@Override
    public void undo() {
		fConnection.reconnect();
	}


    @Override
	public void dispose() {
	    fConnection = null;
	}
}
