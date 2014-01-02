/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Policy for Archimate Figure Container
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateContainerLayoutPolicy
extends ArchimateDiagramLayoutPolicy {
    
    /*
     * Substitute Command for hooking our own sub-commands
     */
    private static class SubstituteCommand extends CompoundCommand {
        private Command fSubCommand = null;
        private IArchimateElement fParentElement;
        
        SubstituteCommand(IArchimateElement parentElement) {
            fParentElement = parentElement;
        }
        
        @Override
        public void execute() {
            super.execute();
            
            List<IArchimateElement> children = new ArrayList<IArchimateElement>();
            
            for(Object o : getCommands()) {
                if(o instanceof AddObjectCommand) {
                    IDiagramModelObject child = ((AddObjectCommand)o).fChild;
                    if(child instanceof IDiagramModelArchimateObject) {
                        IArchimateElement childElement = ((IDiagramModelArchimateObject)child).getArchimateElement();
                        children.add(childElement);
                    }
                }
            }
            
            fSubCommand = DiagramCommandFactory.createNewNestedRelationCommandWithDialog(fParentElement, 
                    children.toArray(new IArchimateElement[children.size()]));
            
            if(fSubCommand != null) {
                fSubCommand.execute();
            }
        }
        
        @Override
        public void undo() {
            super.undo();
            if(fSubCommand != null) {
                fSubCommand.undo();
            }
        }
        
        @Override
        public void redo() {
            super.redo();
            if(fSubCommand != null) {
                fSubCommand.redo();
            }
        }

    }
    
    /* 
     * Hook into the Command and substitute our own Command so we can hook a sub-Command for adding relations
     */
    @Override
    protected Command getAddCommand(Request generic) {
        Command originalCommand = super.getAddCommand(generic);
        
        if(!ConnectionPreferences.createRelationWhenMovingElement()) {
            return originalCommand;
        }
        
        Object parent = getHost().getModel();
        if(!(parent instanceof IDiagramModelArchimateObject)) {
            return originalCommand;
        }
        
        IArchimateElement parentElement = ((IDiagramModelArchimateObject)parent).getArchimateElement();

        CompoundCommand newCommmand = new SubstituteCommand(parentElement);
        
        if(originalCommand instanceof CompoundCommand) {
            for(Object o : ((CompoundCommand)originalCommand).getCommands()) {
                newCommmand.add((Command)o);
            }
        }
        else {
            newCommmand.add(originalCommand);
        }
        
        return newCommmand;
    }
}


