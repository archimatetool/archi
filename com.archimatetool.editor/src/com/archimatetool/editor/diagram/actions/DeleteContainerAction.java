/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.commands.AddListMemberCommand;
import com.archimatetool.editor.model.commands.AlwaysExecutingChainedCompoundCommand;
import com.archimatetool.editor.model.commands.RemoveListMemberCommand;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
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
    public static final String TEXT = Messages.DeleteContainerAction_0;
    public static final String TOOLTIP_TEXT = Messages.DeleteContainerAction_1;
    
    public DeleteContainerAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setToolTipText(TOOLTIP_TEXT);
        setId(ID);
        setActionDefinitionId(ID); // register key binding
    }

    @Override
    protected boolean calculateEnabled() {
        return !getObjectsToBeDeleted().isEmpty();
    }
    
    @Override
    public void run() {
        // Execute each command before proceeding to the next in case of multi-selection of embedded containers
        // Use a AlwaysExecutingChainedCompoundCommand to ensure redo/undo always executes and chains sub-commands
        CompoundCommand compoundCommand = new AlwaysExecutingChainedCompoundCommand(TEXT) {
            @Override
            public void execute() {
                for(IDiagramModelObject dmo : getObjectsToBeDeleted()) {
                    IDiagramModelContainer parent = (IDiagramModelContainer)dmo.eContainer();
                    List<IDiagramModelObject> children = ((IDiagramModelContainer)dmo).getChildren();
                    
                    // Delete main object
                    add(DiagramCommandFactory.createDeleteDiagramObjectCommand(dmo, false));

                    // Iterate thru child objects to move them to new parent
                    for(IDiagramModelObject child : new ArrayList<>(children)) {
                        // Remove child from main object
                        add(new RemoveListMemberCommand<>(children, child));
                        
                        // Adjust x,y position to new parent
                        add(new ChangePositionCommand(child, dmo.getBounds()));
                        
                        // Add child to new parent
                        add(new AddListMemberCommand<>(parent.getChildren(), child));
                    }
                }
            }
        };
        
        execute(compoundCommand);
    }
    
    private List<IDiagramModelObject> getObjectsToBeDeleted() {
        List<IDiagramModelObject> list = new ArrayList<>();
        
        for(Object object : getSelectedObjects()) {
            if(isValidObject(object)) {
                list.add((IDiagramModelObject)((EditPart)object).getModel());
            }
        }
        
        return list;
    }
    
    private boolean isValidObject(Object object) {
        return object instanceof EditPart editPart &&                             // Is an EditPart
                editPart.getModel() instanceof IDiagramModelObject dmo &&         // Diagram Model Object
                dmo instanceof IDiagramModelContainer container &&                // And a container
                dmo.eContainer() instanceof IDiagramModelContainer &&             // And parent is a container
                !(dmo instanceof ILockable lockable && lockable.isLocked()) &&    // And not locked
                !container.getChildren().isEmpty();                               // And has child objects
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
