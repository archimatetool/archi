/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;

/**
 * Move Object Command
 * 
 * @author Phillip Beauvoir
 */
public class MoveObjectCommand extends Command {
    private IFolder fOldParent;
    private IFolder fNewParent;
    private INameable fElement;
    private int fOldPos;
    
    public MoveObjectCommand(IFolder newParent, INameable element) {
        super(Messages.MoveObjectCommand_0 + " " + element.getName()); //$NON-NLS-1$
        fOldParent = (IFolder)element.eContainer();
        fNewParent = newParent;
        fElement = element;
    }
    
    @Override
    public void execute() {
        fOldPos = fOldParent.getElements().indexOf(fElement); // do this here as its part of a compound command
        redo();
    }
    
    @Override
    public void undo() {
        fNewParent.getElements().remove(fElement);
        fOldParent.getElements().add(fOldPos, fElement);
    }
    
    @Override
    public void redo() {
        fOldParent.getElements().remove(fElement);
        fNewParent.getElements().add(fElement);
    }
    
    @Override
    public void dispose() {
        fOldParent = null;
        fNewParent = null;
        fElement = null;
    }
}