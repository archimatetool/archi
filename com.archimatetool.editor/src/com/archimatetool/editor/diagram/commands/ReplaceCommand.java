package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.INameable;

/**
 * Replace command
 * 
 * Uses the super class to change the name of an object in the model.
 * 
 * @see RenameCommandHandler
 * @author Mads Bondo Dydensborg
 * 
 */
public class ReplaceCommand extends EObjectFeatureCommand {
	public ReplaceCommand(INameable object, String newName) {
		super(Messages.ReplaceCommand_0 + " " + object.getName(), object, //$NON-NLS-1$
				IArchimatePackage.Literals.NAMEABLE__NAME, newName);
	}
}
