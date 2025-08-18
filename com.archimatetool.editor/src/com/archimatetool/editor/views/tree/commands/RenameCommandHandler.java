/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;



/**
 * Handles Rename Commands for the Tree Model View
 * 
 * @author Phillip Beauvoir
 */
public class RenameCommandHandler {

    /**
     * @param element
     * @return True if element can be renamed
     */
    public static boolean canRename(Object element) {
        if(element instanceof IFolder) {
            return ((IFolder)element).getType() == FolderType.USER;
        }
        
        return (element instanceof INameable) && (element instanceof IAdapter);
    }
    
    /**
     * Rename element to newText by issuing a Command on the CommandStack
     * @param element
     * @param newText
     */
    public static void doRenameCommand(INameable element, String newText) {
        CommandStack stack = (CommandStack)((IAdapter)element).getAdapter(CommandStack.class);
        if(stack != null) {
            stack.execute(new EObjectFeatureCommand(Messages.RenameCommandHandler_0, element,
                    IArchimatePackage.Literals.NAMEABLE__NAME, newText));
        }
    }
    
    /**
     * Rename elements to matching newNames by issuing a CompundCommand(s) on the CommandStack(s)
     * @param elements
     * @param newNames
     */
    public static void doRenameCommands(List<INameable> elements, List<String> newNames) {
        // Must match sizes
        if(elements.size() != newNames.size() || elements.isEmpty()) {
            return;
        }
        
        /*
         * If renaming elements from more than one model in the tree we need to use the
         * Command Stack allocated to each model. And then allocate one CompoundCommand per Command Stack.
         */
        Hashtable<CommandStack, CompoundCommand> commandMap = new Hashtable<CommandStack, CompoundCommand>();
        
        for(int i = 0; i < elements.size(); i++) {
            INameable element = elements.get(i);
            String newName = newNames.get(i);
            
            CompoundCommand compoundCommand = getCompoundCommand((IAdapter)element, commandMap);
            if(compoundCommand != null) {
                Command cmd = new EObjectFeatureCommand(Messages.RenameCommandHandler_0, element,
                        IArchimatePackage.Literals.NAMEABLE__NAME, newName);
                compoundCommand.add(cmd);
            }
            else {
                System.err.println("Could not get CompoundCommand in doRenameCommands"); //$NON-NLS-1$
            }
        }
        
        // Execute the Commands on the CommandStack(s) - there could be more than one if more than one model open in the Tree
        for(Entry<CommandStack, CompoundCommand> entry : commandMap.entrySet()) {
            entry.getKey().execute(entry.getValue().unwrap());
        }
    }
    
    /**
     * Get, and if need be create, a CompoundCommand to which to add the object to be renamed command
     */
    private static CompoundCommand getCompoundCommand(IAdapter object, Hashtable<CommandStack, CompoundCommand> commandMap) {
        // Get the Command Stack registered to the object
        CommandStack stack = (CommandStack)object.getAdapter(CommandStack.class);
        if(stack == null) {
            System.err.println("CommandStack was null in getCompoundCommand"); //$NON-NLS-1$
            return null;
        }
        
        // Now get or create a Compound Command
        CompoundCommand compoundCommand = commandMap.get(stack);
        if(compoundCommand == null) {
            compoundCommand = new NonNotifyingCompoundCommand(Messages.RenameCommandHandler_0);
            commandMap.put(stack, compoundCommand);
        }
        
        return compoundCommand;
    }

}
