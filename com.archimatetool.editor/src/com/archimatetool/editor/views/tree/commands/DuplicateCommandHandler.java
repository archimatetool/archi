/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
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
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.impl.ArchimateModel;



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
    
    // Newly added objects
    private List<Object> fNewObjects = new ArrayList<Object>();

    // Elements to duplicate
    private List<Object> fElementsToDuplicate = new ArrayList<Object>();
    
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
        // Elements, Diagrams and Folders in the Views Model Folder
        return (element instanceof IArchimateElement) || (element instanceof IDiagramModel) 
        		|| ((element instanceof IFolder) 
        				&& checkFolderInViewFolder((IFolder)element)
        				// We can't allow to duplicate the Views folder
        				&& !((IFolder)element).getName().equals("Views"));
        				// TODO: Must Use the messages.properties original name : com.archimatetool.model.FolderType_7
    }
    
    // TODO: this function must be in Folder interface
    private static boolean checkFolderInViewFolder(IFolder folder) {
    	// if we are at the top
    	if(folder.eContainer() instanceof ArchimateModel) {
    		if(folder.getName().equals("Views")) // TODO: Must Use the messages.properties original name : com.archimatetool.model.FolderType_7
    			return true;
    		return false;
    	}
    	if(folder.eContainer() instanceof IFolder)
    		return checkFolderInViewFolder((IFolder)folder.eContainer());
    	return false; // This is not normal : the folder is not in another folder nor in the ArchimateModel
    }
    
    
    public DuplicateCommandHandler(Object[] objects) {
        fSelectedObjects = objects;
    }

    /**
     * Perform the duplicate command
     */
    public void duplicate() {
        // Gather the elements to duplicate
        getElementsToDuplicate();
        
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

    private void getElementsToDuplicate() {
        for(Object object : fSelectedObjects) {
            if(canDuplicate(object)) {
                addToList(object, fElementsToDuplicate);
            }
        }
    }
    
    private void createCommands() {
        for(Object object : fElementsToDuplicate) {
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
            else if(object instanceof IFolder) {
            	// All actions have to be orchestrated here and cannot be handled in a sub command class
            	IFolder toCopy = (IFolder)object;
            	// We have to drill down inside and copy subdiagrams and subfolders
            	// the reason it has to be here is because it otherwise cannot be add in the compoundCommand
            	recurseDuplicateVewFolder(toCopy, compoundCommand);
            }
        }
    }
    
    private DuplicateDiagramFolderCommand recurseDuplicateVewFolder(IFolder originalFolder, CompoundCommand compoundCommand) {
    	// The raw folder is copied first
    	DuplicateDiagramFolderCommand duplicationFolderCmd = new DuplicateDiagramFolderCommand(originalFolder);
    	compoundCommand.add(duplicationFolderCmd);
    	
    	// we drill in this folder
    	for(EObject object : originalFolder.getFolders()) {
    		if(object instanceof IFolder) {
    			// we recursively drill down
    			// We duplicate this object 
    			DuplicateDiagramFolderCommand newFolderCmd = recurseDuplicateVewFolder((IFolder)object, compoundCommand);
    			// We move this new object in the new folder 
    			Command moveCmd = new DuplicatedFolderMoveCommand(duplicationFolderCmd, newFolderCmd);
    			compoundCommand.add(moveCmd);
    		}
    	}
    	
    	for(EObject object : originalFolder.getElements()) {
    		if(object instanceof IDiagramModel) {
    			DuplicateDiagramModelCommand duplicationDiagramCmd = new DuplicateDiagramModelCommand((IDiagramModel)object);
                compoundCommand.add(duplicationDiagramCmd);
                
                // move the copied diagram from its original folder to the new one
                Command moveCmd = new DuplicatedDiagramMoveCommand(duplicationFolderCmd, duplicationDiagramCmd);
                compoundCommand.add(moveCmd);
  
    		}
    	}
    	return duplicationFolderCmd;
    
    }
    
    
    /**
     * Add object to list if not already in list
     */
    private void addToList(Object object, List<Object> list) {
        if(object != null && !list.contains(object)) {
            list.add(object);
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
        fElementsToDuplicate = null;
        fCommandMap = null;
        fNewObjects = null;
    }
    
    /**
     * Duplicate Diagram Model ViewFolder Command
     */
    private class DuplicateDiagramFolderCommand extends Command {
        private IFolder fParent;
        private IFolder fFolderlOriginal;
        private IFolder fFolderCopy;
        
        public IFolder getCopiedFolder() { return fFolderCopy; }
        
    	public DuplicateDiagramFolderCommand(IFolder viewFolder) {
    		fParent = (IFolder)viewFolder.eContainer();
    		fFolderlOriginal = viewFolder;
    		setLabel(Messages.DuplicateCommandHandler_1);
    	}

    	@Override
    	public void execute() {
    		
    		fFolderCopy = (IFolder)fFolderlOriginal.getCopy();
    		
    		fFolderCopy.setName(fFolderlOriginal.getName() + " " + Messages.DuplicateCommandHandler_3); //$NON-NLS-1$
    		
            fParent.getFolders().add(fFolderCopy);
            
            // Execute Command
            fNewObjects.add(fFolderCopy);

    	}

    	/**
    	 * Close all sub Diagram in the folder in argument
    	 * Use recursion
    	 */
    	private void closeAllDiagram(IFolder folder) {
    		folder.getElements().forEach(element -> {
    			if(element instanceof IDiagramModel) 
    				EditorManager.closeDiagramEditor((IDiagramModel)element);
    			else if(element instanceof IFolder)
    				closeAllDiagram(folder);
				});
    	}
    	
    	@Override
    	public void undo() {
            // Get all copied view, have to be closed in the Editor FIRST!
    		closeAllDiagram(fFolderCopy);
           
            fParent.getElements().remove(fFolderCopy);
    		
    	}
    	@Override
    	public void redo() {
            fParent.getElements().add(fFolderCopy);
    		
    	}
    	@Override
    	public void dispose() {
    		fParent = null;
    		fFolderlOriginal = null;
    		fFolderCopy = null;
    	}
    }
    
    /**
     * Duplicate Diagram Model Command
     */
    private class DuplicateDiagramModelCommand extends Command {
        private IFolder fParent;
        private IDiagramModel fDiagramModelOriginal;
        private IDiagramModel fDiagramModelCopy;
        
        public IDiagramModel getCopiedDiagram() { return fDiagramModelCopy; }
        
        /**
         * Mapping of original objects to new copied objects
         */
        private Hashtable<IConnectable, IConnectable> fMapping;
        
        public DuplicateDiagramModelCommand(IDiagramModel dm) {
            fParent = (IFolder)dm.eContainer();
            fDiagramModelOriginal = dm;
            setLabel(Messages.DuplicateCommandHandler_2);
        }
        
        @Override
        public void execute() {
            // We have to add the diagram model to the model first so that child objects can be allocated IDs.
            // See com.archimatetool.model.util.IDAdapter
            fDiagramModelCopy = (IDiagramModel)fDiagramModelOriginal.getCopy();
            fDiagramModelCopy.setName(fDiagramModelOriginal.getName() + " " + Messages.DuplicateCommandHandler_3); //$NON-NLS-1$
            fParent.getElements().add(fDiagramModelCopy);
            
            fNewObjects.add(fDiagramModelCopy);
            
            fMapping = new Hashtable<IConnectable, IConnectable>();
            
            // Add child objects first
            copyChildDiagramObjects(fDiagramModelOriginal, fDiagramModelCopy);
            
            // Then add connections
            copyConnections();

            // Open Editor
            EditorManager.openDiagramEditor(fDiagramModelCopy, false);
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
            EditorManager.openDiagramEditor(fDiagramModelCopy, false);
        }
        
        /*
         * This is done first and recursively so that the object's parent container can be determined and also to maintain z-order of objects
         */
        private void copyChildDiagramObjects(IDiagramModelContainer container, IDiagramModelContainer containerCopy) {
            for(IDiagramModelObject dmo : container.getChildren()) {
                IDiagramModelObject dmoCopy = (IDiagramModelObject)createCopy(dmo);
                containerCopy.getChildren().add(dmoCopy);
                
                if(dmo instanceof IDiagramModelContainer) {
                    copyChildDiagramObjects((IDiagramModelContainer)dmo, (IDiagramModelContainer)dmoCopy);
                }
            }
        }
        
        /*
         * Copy Connections
         */
        private void copyConnections() {
            // Iterate through all connections in original
            for(Iterator<EObject> iter = fDiagramModelOriginal.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                
                // Only Connect ;-)
                if(eObject instanceof IDiagramModelConnection) {
                    IDiagramModelConnection conn = (IDiagramModelConnection)eObject;
                    
                    IConnectable srcCopy = fMapping.get(conn.getSource());
                    IConnectable tgtCopy = fMapping.get(conn.getTarget());
                    
                    // Source/Target copy does not exist yet - it will therefore be a connection that connects to another connection
                    if(srcCopy == null) {
                        srcCopy = createCopy(conn.getSource());
                    }
                    if(tgtCopy == null) {
                        tgtCopy = createCopy(conn.getTarget());
                    }
                    
                    // Make/get a copy and connect
                    IDiagramModelConnection connCopy = (IDiagramModelConnection)createCopy(conn);
                    connCopy.connect(srcCopy, tgtCopy);
                }
            }
        }
        
        /*
         * Create a copy and map it
         */
        private IConnectable createCopy(IConnectable object) {
            if(fMapping.containsKey(object)) {
                return fMapping.get(object);
            }

            IConnectable copy = (IConnectable)object.getCopy();
            
            if(object instanceof IDiagramModelArchimateComponent) {
                ((IDiagramModelArchimateComponent)copy).setArchimateConcept(((IDiagramModelArchimateComponent)object).getArchimateConcept());
            }
            
            fMapping.put(object, copy);
            
            return copy;
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fDiagramModelOriginal = null;
            fDiagramModelCopy = null;
            fMapping =  null;
        }
    }
    
    /**
     * Duplicate Element Command
     */
    private class DuplicateElementCommand extends Command {
        private IFolder fParent;
        private IArchimateElement fElementCopy;
        
        public DuplicateElementCommand(IArchimateElement element) {
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

    /**
     * Move an object after its duplication.
     */
    private class DuplicatedDiagramMoveCommand extends Command {
    	private MoveObjectCommand innerCommand;
    	private DuplicateDiagramFolderCommand futureNewTarget;
    	private DuplicateDiagramModelCommand futureNewDiagram;
    	
    	public DuplicatedDiagramMoveCommand(DuplicateDiagramFolderCommand folderDuplication, DuplicateDiagramModelCommand diagramDuplication) {
			futureNewDiagram = diagramDuplication;
			futureNewTarget = folderDuplication;
		}
    	@Override
    	public void execute() {
    		innerCommand = new MoveObjectCommand(futureNewTarget.getCopiedFolder(), futureNewDiagram.getCopiedDiagram());
    		innerCommand.execute();
    	}
    	@Override
    	public void redo() {
    		innerCommand.redo();
    	}
    	@Override
    	public void undo() {
    		innerCommand.undo();
    	}
    	@Override
    	public void dispose() {
    		innerCommand.dispose();
    		innerCommand = null;
    		futureNewDiagram = null;
    		futureNewTarget = null;
    	}
    }
    /**
     * Move an object after its duplication.
     */
    private class DuplicatedFolderMoveCommand extends Command {
    	private MoveFolderCommand innerCommand;
    	private DuplicateDiagramFolderCommand futureNewDestination;
    	private DuplicateDiagramFolderCommand futureNewFolder;
    	
    	public DuplicatedFolderMoveCommand(DuplicateDiagramFolderCommand duplicatedDestination, DuplicateDiagramFolderCommand duplicatedFolder) {
    		futureNewFolder = duplicatedFolder;
    		futureNewDestination = duplicatedDestination;
		}
    	@Override
    	public void execute() {
    		innerCommand = new MoveFolderCommand(futureNewDestination.getCopiedFolder(), futureNewFolder.getCopiedFolder());
    		innerCommand.execute();
    	}
    	@Override
    	public void redo() {
    		innerCommand.redo();
    	}
    	@Override
    	public void undo() {
    		innerCommand.undo();
    	}
    	@Override
    	public void dispose() {
    		innerCommand.dispose();
    		innerCommand = null;
    		futureNewFolder = null;
    		futureNewDestination = null;
    	}
    }
}
