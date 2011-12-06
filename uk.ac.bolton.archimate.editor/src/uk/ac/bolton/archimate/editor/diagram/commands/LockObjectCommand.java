/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Lock Object Command
 *
 * @author Phillip Beauvoir
 */
public class LockObjectCommand extends EObjectFeatureCommand {
    
    public LockObjectCommand(ILockable lockable, boolean lock) {
        super(lock ? "Lock" : "Unlock", lockable, IArchimatePackage.Literals.LOCKABLE__LOCKED, lock);
    }
}