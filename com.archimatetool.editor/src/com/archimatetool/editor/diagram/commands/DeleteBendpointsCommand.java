/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Delete all bendpoints from a connection
 * 
 * @author Phillip Beauvoir
 */
public class DeleteBendpointsCommand extends Command implements IAnimatableCommand {
    
    private IDiagramModelConnection connection;
    private List<IDiagramModelBendpoint> bendpoints;

    public DeleteBendpointsCommand(IDiagramModelConnection connection) {
        this.connection = connection;
        bendpoints = new ArrayList<>(connection.getBendpoints());
        setLabel(Messages.DeleteBendpointsCommand_0);
    }
    
    @Override
    public boolean canExecute() {
        return !connection.getBendpoints().isEmpty();
    }

    @Override
    public void execute() {
        connection.getBendpoints().clear();
    }

    @Override
    public void undo() {
        connection.getBendpoints().addAll(bendpoints);
    }

    @Override
    public void dispose() {
        connection = null;
        bendpoints = null;
    }
}
