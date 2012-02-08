/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.editor.ui.services.EditorManager;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;


/**
 * Delete Diagram Model Command
 * 
 * @author Phillip Beauvoir
 */
public class DeleteDiagramModelCommand extends Command {
    
    private IDiagramModel fDiagramModel;
    private int fIndex;
    private IFolder fFolder;

    public DeleteDiagramModelCommand(IDiagramModel model) {
        fFolder = (IFolder)model.eContainer();
        fDiagramModel = model;
        setLabel(Messages.DeleteDiagramModelCommand_0 + " " + model.getName()); //$NON-NLS-1$
    }
    
    @Override
    public void execute() {
        // Ensure fIndex is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        fIndex = fFolder.getElements().indexOf(fDiagramModel); 
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            // Close Editor first - we do it here in case user performs an Undo.
            EditorManager.closeDiagramEditor(fDiagramModel);

            fFolder.getElements().remove(fDiagramModel);
        }
    }
    
    @Override
    public void undo() {
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().add(fIndex, fDiagramModel);
        }
    }
    
    @Override
    public void dispose() {
        fDiagramModel = null;
        fFolder = null;
    }
}
