/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Add Diagram Object Command
 * 
 * @author Phillip Beauvoir
 */
public class AddDiagramObjectCommand extends Command {
    
    private IDiagramModelContainer fParent;
    private IDiagramModelObject fChild;

    public AddDiagramObjectCommand(IDiagramModelContainer parent, IDiagramModelObject object) {
        fParent = parent;
        fChild = object;
        setLabel("Add" + " " + object.getName());
    }

    @Override
    public void execute() {
        fParent.getChildren().add(fChild);
    }

    @Override
    public void undo() {
        fParent.getChildren().remove(fChild);
    }
    
    @Override
    public void dispose() {
        fParent = null;
        fChild = null;
    }
}