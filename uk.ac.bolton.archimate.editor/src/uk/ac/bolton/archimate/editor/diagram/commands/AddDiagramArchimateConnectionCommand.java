/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Add Connection Command
 * Used when dragging and dropping an Archimate Relationship from the tree to the diagram.
 * Called from {@link uk.ac.bolton.archimate.editor.diagram.policies.ArchimateDNDEditPolicy}
 * 
 * @author Phillip Beauvoir
 */
public class AddDiagramArchimateConnectionCommand extends Command {
    
    private IDiagramModelArchimateConnection fConnection;
    private IDiagramModelObject fSource, fTarget;
    
    public AddDiagramArchimateConnectionCommand(IDiagramModelObject src, IDiagramModelObject tgt, IRelationship relationship) {
        setLabel("Add" + " " + relationship.getName());

        fSource = src;
        fTarget = tgt;
        fConnection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        fConnection.setRelationship(relationship);
    }

    @Override
    public void execute() {
        fConnection.connect(fSource, fTarget);
    }

    @Override
    public void undo() {
        fConnection.disconnect();
    }

    @Override
    public void redo() {
        fConnection.reconnect();
    }
}