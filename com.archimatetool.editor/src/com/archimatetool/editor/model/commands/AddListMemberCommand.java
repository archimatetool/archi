/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;

/**
 * Generic Command to add a member to a List
 * 
 * @author Phillip Beauvoir
 */
public class AddListMemberCommand<E> extends Command {
    private List<E> list;
    private E object;
    
    public AddListMemberCommand(String label, List<E> list, E object) {
        setLabel(label);
        this.list = list;
        this.object = object;
    }
    
    public AddListMemberCommand(List<E> list, E object) {
        this(null, list, object);
    }
    
    @Override
    public void execute() {
        list.add(object);
    }
    
    @Override
    public void undo() {
        list.remove(object);
    }
    
    @Override
    public void dispose() {
        list = null;
        object = null;
    }
}
