/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;

/**
 * Compound Command to delete nested connections between parent and childObjects in nested objects.
 * This is used when the user drags objects into a parent Archimate object and nested connections to child are removed.
 *    
 * @author Phillip Beauvoir
 */
public class DeleteNestedConnectionsCommand extends CompoundCommand {
    
    IDiagramModelArchimateObject fParentObject;
    List<IDiagramModelArchimateObject> fChildObjects;

    public DeleteNestedConnectionsCommand(IDiagramModelArchimateObject parentObject, IDiagramModelArchimateObject childObject) {
        fParentObject = parentObject;
        fChildObjects = new ArrayList<IDiagramModelArchimateObject>();
        fChildObjects.add(childObject);
    }

    public DeleteNestedConnectionsCommand(IDiagramModelArchimateObject parentObject, List<IDiagramModelArchimateObject> childObjects) {
        fParentObject = parentObject;
        fChildObjects = childObjects;
    }
    
    @Override
    public void execute() {
        createDeleteCommands();

        super.execute();
    }
    
    // These should return true always because sub-commands are only created on execute()
    
    @Override
    public boolean canExecute() {
        return true;
    }
    
    @Override
    public boolean canUndo() {
        return true;
    }
    
    @Override
    public boolean canRedo() {
        return true;
    }
    
    /** 
     * Child Objects that have connections
     */
    void createDeleteCommands() {
        for(IDiagramModelArchimateObject child : fChildObjects) {
            for(IDiagramModelConnection connection : child.getTargetConnections()) {
                if(connection instanceof IDiagramModelArchimateConnection && DiagramModelUtils.shouldBeHiddenConnection((IDiagramModelArchimateConnection)connection)) {
	                for(IDiagramModelConnection subconnection : connection.getTargetConnections()) {
                        Command cmd = DiagramCommandFactory.createDeleteDiagramConnectionCommand(subconnection);
                        add(cmd);
	                }
	                
	                for(IDiagramModelConnection subconnection : connection.getSourceConnections()) {
                        Command cmd = DiagramCommandFactory.createDeleteDiagramConnectionCommand(subconnection);
                        add(cmd);
	                }
                }
            }
        }
    }
}
