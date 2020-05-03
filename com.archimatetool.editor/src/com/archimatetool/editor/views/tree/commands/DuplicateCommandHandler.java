/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeSelectionRequest;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.UUIDFactory;



/**
 * Duplicate Command Handler
 * 
 * @author Phillip Beauvoir
 */
public class DuplicateCommandHandler {

    /*
     * If duplicating elements from more than one model in the tree we need to use the
     * Command Stack allocated to each model. And then allocate one CompoundCommand per Command Stack.
     */
    private Hashtable<CommandStack, CompoundCommand> fCommandMap = new Hashtable<CommandStack, CompoundCommand>();

    // Selected objects in Tree
    private Object[] fSelectedObjects;
    
    // Newly added objects that will be selected in the Models Tree
    private List<Object> fNewObjects = new ArrayList<Object>();

    // If true will open duplicated diagrams when created
    private boolean doOpenDiagrams = true;
    
    /**
     * @param selection
     * @return True if we can duplicate anything in selection
     */
    public static boolean canDuplicate(IStructuredSelection selection) {
        for(Object element : selection.toList()) {
            if(canDuplicate(element)) { // At least one element can be duplicated
                return true;
            }
        }
        
        return false;
    }

    /**
     * @param element
     * @return True if we can duplicate this object
     */
    public static boolean canDuplicate(Object element) {
        // Elements and Diagrams
        return (element instanceof IArchimateElement) || (element instanceof IDiagramModel);
    }
    
    public DuplicateCommandHandler(Object[] objects) {
        fSelectedObjects = objects;
    }

    /**
     * Perform the duplicate command
     */
    public void duplicate() {
        // Create the Commands
        createCommands();
        
        // Execute the Commands on the CommandStack(s) - there could be more than one if more than one model open in the Tree
        for(Entry<CommandStack, CompoundCommand> entry : fCommandMap.entrySet()) {
            entry.getKey().execute(entry.getValue());
        }
        
        // Select new objects in Tree asyncronously
        UIRequestManager.INSTANCE.fireRequestAsync(new TreeSelectionRequest(this, new StructuredSelection(fNewObjects), true));
        
        dispose();
    }

    private void createCommands() {
        for(Object object : fSelectedObjects) {
            if(canDuplicate(object)) {
                CompoundCommand compoundCommand = getCompoundCommand((IAdapter)object);
                if(compoundCommand == null) { // sanity check
                    System.err.println("Could not get CompoundCommand in " + getClass()); //$NON-NLS-1$
                    continue;
                }
                
                if(object instanceof IDiagramModel) {
                    Command cmd = new DuplicateDiagramModelCommand((IDiagramModel)object);
                    compoundCommand.add(cmd);
                }
                else if(object instanceof IArchimateElement) {
                    Command cmd = new DuplicateElementCommand((IArchimateElement)object);
                    compoundCommand.add(cmd);
                }
            }
        }
    }
    
    /**
     * Get, and if need be create, a CompoundCommand to which to add the object to be duplicated command
     */
    private CompoundCommand getCompoundCommand(IAdapter object) {
        // Get the Command Stack registered to the object
        CommandStack stack = (CommandStack)object.getAdapter(CommandStack.class);
        if(stack == null) {
            System.err.println("CommandStack was null in " + getClass()); //$NON-NLS-1$
            return null;
        }
        
        // Now get or create a Compound Command
        CompoundCommand compoundCommand = fCommandMap.get(stack);
        if(compoundCommand == null) {
            compoundCommand = new NonNotifyingCompoundCommand(Messages.DuplicateCommandHandler_1);
            fCommandMap.put(stack, compoundCommand);
        }
        
        return compoundCommand;
    }
    
    private void dispose() {
        fSelectedObjects = null;
        fCommandMap = null;
        fNewObjects = null;
    }


    
    /**
     * Duplicate Diagram Model Command
     */
    private class DuplicateDiagramModelCommand extends Command {
        private IFolder fParent;
        private IDiagramModel fDiagramModelOriginal;
        private IDiagramModel fDiagramModelCopy;
        
        private DuplicateDiagramModelCommand(IDiagramModel dm) {
            fParent = (IFolder)dm.eContainer();
            fDiagramModelOriginal = dm;
            setLabel(Messages.DuplicateCommandHandler_2);
        }
        
        @Override
        public void execute() {
            fDiagramModelCopy = EcoreUtil.copy(fDiagramModelOriginal);
            UUIDFactory.generateNewIDs(fDiagramModelCopy);
            fDiagramModelCopy.setName(fDiagramModelOriginal.getName() + " " + Messages.DuplicateCommandHandler_3); //$NON-NLS-1$
            
            fParent.getElements().add(fDiagramModelCopy);
            
            fNewObjects.add(fDiagramModelCopy);
            
            // Open Editor
            if(doOpenDiagrams) {
                EditorManager.openDiagramEditor(fDiagramModelCopy, false);
            }
        }
        
        @Override
        public void undo() {
            // Close the Editor FIRST!
            EditorManager.closeDiagramEditor(fDiagramModelCopy);
            
            fParent.getElements().remove(fDiagramModelCopy);
        }
        
        @Override
        public void redo() {
            fParent.getElements().add(fDiagramModelCopy);
            
            // Open Editor
            if(doOpenDiagrams) {
                EditorManager.openDiagramEditor(fDiagramModelCopy, false);
            }
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fDiagramModelOriginal = null;
            fDiagramModelCopy = null;
        }
    }
    
    /**
     * Duplicate Element Command
     */
    private class DuplicateElementCommand extends Command {
        private IFolder fParent;
        private IArchimateElement fElementCopy;
        
        private DuplicateElementCommand(IArchimateElement element) {
            setLabel(Messages.DuplicateCommandHandler_4);

            fParent = (IFolder)element.eContainer();
            fElementCopy = (IArchimateElement)element.getCopy();
            fElementCopy.setName(element.getName() + " " + Messages.DuplicateCommandHandler_3); //$NON-NLS-1$

            fNewObjects.add(fElementCopy);
        }
        
        @Override
        public void execute() {
            fParent.getElements().add(fElementCopy);
        }
        
        @Override
        public void undo() {
            fParent.getElements().remove(fElementCopy);
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fElementCopy = null;
        }
    }

}
