/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * License which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.model.IDiagramModelBendpoint;

/**
 * DeleteBendpointCommand
 * 
 * @author Phillip Beauvoir
 */
public class DeleteBendpointCommand extends BendpointCommand implements IAnimatableCommand {
    
    private IDiagramModelBendpoint fBendpoint;

    public DeleteBendpointCommand() {
        super(Messages.DeleteBendpointCommand_0);
    }

    @Override
    public void execute() {
        fBendpoint = getDiagramModelConnection().getBendpoints().get(getIndex());
        redo();
    }

    @Override
    public void undo() {
        getDiagramModelConnection().getBendpoints().add(getIndex(), fBendpoint);
    }

    @Override
    public void redo() {
        getDiagramModelConnection().getBendpoints().remove(fBendpoint);
    }
}
