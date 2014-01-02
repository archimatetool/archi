/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.model.IDiagramModelBendpoint;

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
