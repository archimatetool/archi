/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Add Connection Command
 * Used when dragging and dropping an Archimate Relationship from the tree to the diagram.
 * Called from {@link uk.ac.bolton.archimate.editor.diagram.policies.ContainerComponentEditPolicy}
 * 
 * This is to be only used as part of a CompoundCommand because it might not execute if the connection
 * has been added already to the diagram from a previous Command in the CompoundCommand
 *
 * @author Phillip Beauvoir
 */
public class AddDiagramArchimateConnectionCommand extends Command {
    
    private IDiagramModel fModel;
    private IRelationship fRelationship;
    private IDiagramModelArchimateConnection fConnection;

    public AddDiagramArchimateConnectionCommand(IDiagramModel model, IRelationship relationship) {
        fModel = model;
        fRelationship = relationship;
        setLabel("Add" + " " + relationship.getName());
    }
    
    @Override
    public void execute() {
        IDiagramModelObject src = (IDiagramModelObject)DiagramModelUtils.findDiagramModelComponentForElement(fModel, fRelationship.getSource());
        IDiagramModelObject tgt = (IDiagramModelObject)DiagramModelUtils.findDiagramModelComponentForElement(fModel, fRelationship.getTarget());
        if(src != null && tgt != null) { // only connect if src and tgt exist
            fConnection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            fConnection.setRelationship(fRelationship);
            fConnection.connect(src, tgt);
        }
    }

    @Override
    public void undo() {
        if(fConnection != null) {
            fConnection.disconnect();
        }
    }

    @Override
    public void redo() {
        if(fConnection != null) {
            fConnection.reconnect();
        }
    }
}