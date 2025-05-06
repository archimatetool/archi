/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.CompoundCommand;

/**
 * A CompoundCommand that will always execute/undo/redo even if there are no child commands when calling execute()
 * This ensures we can add child commands dynamically when calling execute()
 * 
 * @author Phillip Beauvoir
 */
public class AlwaysExecutingCompoundCommand extends CompoundCommand {
    
    public AlwaysExecutingCompoundCommand() {
    }
    
    public AlwaysExecutingCompoundCommand(String label) {
        super(label);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
    
    @Override
    public boolean canRedo() {
        return true;
    }
    
    @Override
    public boolean canUndo() {
        return true;
    }
}