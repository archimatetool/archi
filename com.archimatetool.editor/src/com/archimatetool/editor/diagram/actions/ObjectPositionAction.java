/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Object Position Action
 * 
 * @author Phillip Beauvoir
 */
public class ObjectPositionAction extends SelectionAction {
    
    public static final String ACTION_BRINGTOFRONT_ID = "com.archimatetool.editor.bringToFront"; //$NON-NLS-1$
    public static final String ACTION_BRINGFORWARD_ID = "com.archimatetool.editor.bringForward"; //$NON-NLS-1$
    public static final String ACTION_SENDTOBACK_ID = "com.archimatetool.editor.sendToBack"; //$NON-NLS-1$
    public static final String ACTION_SENDBACKWARD_ID = "com.archimatetool.editor.sendBackward"; //$NON-NLS-1$
    
    public static final record ObjectPositionActionDefinition(String id, String label) {}
    
    public static final ObjectPositionActionDefinition[] getActionDefinitions() {
        return new ObjectPositionActionDefinition[] {
                new ObjectPositionActionDefinition(ACTION_BRINGTOFRONT_ID, Messages.BringToFrontAction_0),
                new ObjectPositionActionDefinition(ACTION_BRINGFORWARD_ID, Messages.BringForwardAction_0),
                new ObjectPositionActionDefinition(ACTION_SENDTOBACK_ID, Messages.SendToBackAction_0),
                new ObjectPositionActionDefinition(ACTION_SENDBACKWARD_ID, Messages.SendBackwardAction_0)
        };
    }
    
    public static List<RetargetAction> createRetargetActions() {
        List<RetargetAction> list = new ArrayList<>();
        
        for(ObjectPositionActionDefinition a : getActionDefinitions()) {
            RetargetAction action = new RetargetAction(a.id(), a.label());
            action.setActionDefinitionId(a.id()); // Command key binding support
            list.add(action);
        }
        
        return list;
    }
    
    public static List<ObjectPositionAction> createActions(IWorkbenchPart part) {
        List<ObjectPositionAction> list = new ArrayList<>();
        
        for(ObjectPositionActionDefinition a : getActionDefinitions()) {
            list.add(new ObjectPositionAction(part, a.id(), a.label()));
        }
        
        return list;
    }
    
    
    public ObjectPositionAction(IWorkbenchPart part, String id, String text) {
        super(part);
        setText(text);
        setId(id);
        setActionDefinitionId(id); // Register for key binding
    }

    @Override
    public void run() {
        execute(createCommand());
    }
    
    @Override
    protected boolean calculateEnabled() {
        return createCommand().canExecute();
    }

    private Command createCommand() {
        CompoundCommand result = new CompoundCommand(getText());
        
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart editPart && editPart.getModel() instanceof IDiagramModelObject dmo
                    // Parent can be null under some circumstances when dragging from one container to another
                    && dmo.eContainer() instanceof IDiagramModelContainer
                    && !(dmo instanceof ILockable lockable && lockable.isLocked())) {
                Command cmd = new PositionCommand(dmo, getText(), getId());
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }

        return result.unwrap();
    }
    
    /**
     * Position Commmand
     */
    private static class PositionCommand extends Command {
        private IDiagramModelObject dmo;
        private EList<IDiagramModelObject> children;
        private int oldPos;
        private String actionId;
        
        PositionCommand(IDiagramModelObject dmo, String label, String actionId) {
            setLabel(label);
            this.dmo = dmo;
            this.actionId = actionId;
            children = ((IDiagramModelContainer)dmo.eContainer()).getChildren();
            oldPos = children.indexOf(dmo);
        }
        
        @Override
        public boolean canExecute() {
            return switch(actionId) {
                case ACTION_BRINGTOFRONT_ID, ACTION_BRINGFORWARD_ID -> {
                    yield oldPos < children.size() - 1;
                }
                case ACTION_SENDTOBACK_ID, ACTION_SENDBACKWARD_ID  -> {
                    yield oldPos > 0;
                }
                default -> {
                    yield false;
                }
            };
        }

        @Override
        public void execute() {
            oldPos = children.indexOf(dmo);
            
            int newPosition = switch(actionId) {
                case ACTION_BRINGTOFRONT_ID -> {
                    yield children.size() - 1;
                }
                case ACTION_BRINGFORWARD_ID -> {
                    yield oldPos + 1;
                }
                case ACTION_SENDTOBACK_ID -> {
                    yield 0;
                }
                case ACTION_SENDBACKWARD_ID -> {
                    yield oldPos - 1;
                }
                default -> {
                    yield oldPos;
                }
            };
            
            children.move(newPosition, oldPos);
        }
        
        @Override
        public void undo() {
            children.move(oldPos, children.indexOf(dmo));
        }
        
        @Override
        public void dispose() {
            dmo = null;
            children = null;
        }
    }
    
}
