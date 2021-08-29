/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;

/**
 * Generic Command to remove a member from a List
 * 
 * @author Phillip Beauvoir
 */
public class RemoveListMemberCommand<E> extends Command {
    private List<E> list;
    private E object;
    private int index;
    
    public RemoveListMemberCommand(String label, List<E> list, E object) {
        setLabel(label);
        this.list = list;
        this.object = object;
    }
    
    public RemoveListMemberCommand(List<E> list, E object) {
        this(null, list, object);
    }
    
    @Override
    public void execute() {
        // Ensure index position is stored just before execute as this is part of a composite delete action the index position might have changed
        index = list.indexOf(object);
        if(index != -1) {
            list.remove(index);
        }
    }
    
    @Override
    public void undo() {
        if(index != -1) {
            list.add(index, object);
        }
    }
    
    @Override
    public void dispose() {
        list = null;
        object = null;
    }
}