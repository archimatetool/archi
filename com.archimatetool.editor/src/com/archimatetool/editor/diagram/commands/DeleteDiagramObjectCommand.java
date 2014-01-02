/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Command for deleting an Object from its parent container
 * It puts it back at the index position from where it was removed.
 * 
 * @author Phillip Beauvoir
 */
class DeleteDiagramObjectCommand extends Command {

    private IDiagramModelContainer fParent;
    private IDiagramModelObject fObject;
    private int fIndex;
    
    private List<IDiagramModelConnection> fSourceConnections;
    private List<IDiagramModelConnection> fTargetConnections;
    
    public DeleteDiagramObjectCommand(IDiagramModelObject object) {
        fParent = (IDiagramModelContainer)object.eContainer();
        fObject = object;
    }

    @Override
    public boolean canExecute() {
        /*
         * Parent can be null when objects are selected (with marquee tool) and transferred from one container
         * to another and the Diagram Editor updates the enablement state of Actions.
         * Can also be null if already deleted as part of a Compound Command.
         */
        return fParent != null && fParent.getChildren().contains(fObject);
    }
    
    @Override
    public void execute() {
        fSourceConnections = new ArrayList<IDiagramModelConnection>();
        fTargetConnections = new ArrayList<IDiagramModelConnection>();
        
        // Store a copy of incoming & outgoing connections before proceeding 
        fSourceConnections.addAll(fObject.getSourceConnections());
        fTargetConnections.addAll(fObject.getTargetConnections());
        
        // Ensure fIndex is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        fIndex = fParent.getChildren().indexOf(fObject); 
        if(fIndex != -1) { // might have already been deleted by another process
            fParent.getChildren().remove(fObject);
        }
        
        // And then remove the connections ON THE COPY or else we get a conccurrent mod error!
        removeConnections(fSourceConnections);
        removeConnections(fTargetConnections);
    }
    
    @Override
    public void undo() {
        // Add the Child at old index position
        if(fIndex != -1) { // might have already been deleted by another process
            fParent.getChildren().add(fIndex, fObject);
        }
        
        // Reconnect connections
        addConnections(fSourceConnections);
        addConnections(fTargetConnections);
        
        // Unstore connections
        fSourceConnections.clear();
        fTargetConnections.clear();
    }

    /**
     * Reconnects a List of Connections with their previous endpoints.
     * @param connections a non-null List of connections
     */
    private void addConnections(List<IDiagramModelConnection> connections) {
        for(IDiagramModelConnection conn : connections) {
            conn.reconnect();
        }
    }
    
    /**
     * Disconnects a List of Connections from their endpoints.
     * @param connections a non-null List of connections
     */
    private void removeConnections(List<IDiagramModelConnection> connections) {
        for(IDiagramModelConnection conn : connections) {
            conn.disconnect();
        }
    }
    
    @Override
    public void dispose() {
        fParent = null;
        fObject = null;
        fSourceConnections = null;
        fTargetConnections = null;
    }
}