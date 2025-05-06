/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

/**
 * A CompoundCommand that will always execute/undo/redo even if there are no child commands when calling execute()
 * Sub-commands that are added are executed immediately. This means that we effectively chain one command after another
 * so that the state of each sub-command can depend on the state when the previous commmands are executed.
 * 
 * @author Phillip Beauvoir
 */
public class AlwaysExecutingChainedCompoundCommand extends CompoundCommand {
    
    public AlwaysExecutingChainedCompoundCommand() {
    }
    
    public AlwaysExecutingChainedCompoundCommand(String label) {
        super(label);
    }

    @Override
    public void add(Command command) {
        if(command != null) {
            super.add(command);
            command.execute();
        }
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