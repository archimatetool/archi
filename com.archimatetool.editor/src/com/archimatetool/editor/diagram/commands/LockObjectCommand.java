/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;



/**
 * Lock Object Command
 *
 * @author Phillip Beauvoir
 */
public class LockObjectCommand extends EObjectFeatureCommand {
    
    public LockObjectCommand(ILockable lockable, boolean lock) {
        super(lock ? Messages.LockObjectCommand_0 : Messages.LockObjectCommand_1, lockable, IArchimatePackage.Literals.LOCKABLE__LOCKED, lock);
    }
}