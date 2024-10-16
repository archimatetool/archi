/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.commands.AddListMemberCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.model.commands.RemoveListMemberCommand;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Delete Container object and move children to parent
 * 
 * @author Phillip Beauvoir
 */
public class DeleteContainerAction extends SelectionAction {
    
    public static final String ID = "com.archimatetool.editor.action.deleteContainer"; //$NON-NLS-1$
    public static final String TEXT = "Delete from View keeping children";
    
    public DeleteContainerAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(ID); // register key binding
    }

    @Override
    protected boolean calculateEnabled() {
        for(Object object : getSelectedObjects()) {
            if(isValidObject(object)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void run() {
        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand(TEXT);
        
        for(IDiagramModelObject dmo : getValidObjects()) {
            IDiagramModelContainer parent = (IDiagramModelContainer)dmo.eContainer();
            
            // Iterate thru child objects to move them to new parent
            for(IDiagramModelObject child : ((IDiagramModelContainer)dmo).getChildren()) {
                // Remove child from parent
                compoundCommand.add(new RemoveListMemberCommand<>(((IDiagramModelContainer)dmo).getChildren(), child));
                
                // Add child to new parent
                compoundCommand.add(new AddListMemberCommand<>(parent.getChildren(), child));
                
                // Adjust x,y position to new parent
                compoundCommand.add(new ChangePositionCommand(child, dmo.getBounds()));
            }
            
            // Delete target container object
            compoundCommand.add(DiagramCommandFactory.createDeleteDiagramObjectNoChildrenCommand(dmo));
        }
        
        execute(compoundCommand);
    }
    
    private boolean isValidObject(Object object) {
        return object instanceof EditPart editPart &&                             // Is an EditPart
                editPart.getModel() instanceof IDiagramModelObject dmo &&         // Diagram Model Object
                dmo instanceof IDiagramModelContainer container &&                // And a container
                dmo.eContainer() instanceof IDiagramModelContainer &&             // And parent is a container
                !(dmo instanceof ILockable lockable && lockable.isLocked()) &&    // And not locked
                !container.getChildren().isEmpty();                               // And has child objects
    }
    
    private Set<IDiagramModelObject> getValidObjects() {
        Set<IDiagramModelObject> list = new HashSet<>();
        
        for(Object object : getSelectedObjects()) {
            if(isValidObject(object)) {
                list.add((IDiagramModelObject)((EditPart)object).getModel());
            }
        }
        
        // Remove child objects that have an ancestor that will be deleted
        for(IDiagramModelObject dmo : new ArrayList<>(list)) {
            EObject parent = dmo.eContainer();
            while(parent != null && !(parent instanceof IDiagramModel)) {
                if(list.contains(parent)) {
                    list.remove(dmo);
                }
                parent = parent.eContainer();
            }
        }
        
        return list;
    }
    
    private static class ChangePositionCommand extends Command {
        private IDiagramModelObject dmo;
        private IBounds oldBounds, newBounds;
        
        public ChangePositionCommand(IDiagramModelObject dmo, IBounds parentBounds) {
            this.dmo = dmo;
            oldBounds = dmo.getBounds();
            newBounds = IArchimateFactory.eINSTANCE.createBounds(oldBounds.getX() + parentBounds.getX(),
                    oldBounds.getY() + parentBounds.getY(), oldBounds.getWidth(), oldBounds.getHeight());
        }

        @Override
        public void execute() {
            dmo.setBounds(newBounds);
        }
        
        @Override
        public void undo() {
            dmo.setBounds(oldBounds);
        }
        
        @Override
        public void dispose() {
            dmo = null;
            oldBounds = null;
            newBounds = null;
        }
    }
}
