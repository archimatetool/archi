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
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.diagram.commands.TextPositionCommand;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextPosition;



/**
 * Text Position Action
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionAction extends SelectionAction {
    
    public static final String ACTION_TOP_ID = "com.archimatetool.editor.textPositionTop"; //$NON-NLS-1$
    public static final String ACTION_CENTRE_ID = "com.archimatetool.editor.textPositionCentre"; //$NON-NLS-1$
    public static final String ACTION_BOTTOM_ID = "com.archimatetool.editor.textPositionBottom"; //$NON-NLS-1$
    
    public static final record TextPositionActionDefinition(String id, int position, String label) {}
    
    public static final TextPositionActionDefinition[] getActionDefinitions() {
        return new TextPositionActionDefinition[] {
                new TextPositionActionDefinition(ACTION_TOP_ID, ITextPosition.TEXT_POSITION_TOP, Messages.TextPositionAction_0),
                new TextPositionActionDefinition(ACTION_CENTRE_ID, ITextPosition.TEXT_POSITION_CENTRE, Messages.TextPositionAction_1),
                new TextPositionActionDefinition(ACTION_BOTTOM_ID, ITextPosition.TEXT_POSITION_BOTTOM, Messages.TextPositionAction_2)
        };
    }

    public static List<RetargetAction> createRetargetActions() {
        List<RetargetAction> list = new ArrayList<>();
        
        for(TextPositionActionDefinition a : getActionDefinitions()) {
            RetargetAction action = new RetargetAction(a.id(), a.label(), IAction.AS_RADIO_BUTTON);
            action.setActionDefinitionId(a.id()); // Command key binding support
            list.add(action);
        }
        
        return list;
    }
    
    public static List<TextPositionAction> createActions(IWorkbenchPart part) {
        List<TextPositionAction> list = new ArrayList<>();
        
        for(TextPositionActionDefinition a : getActionDefinitions()) {
            list.add(new TextPositionAction(part, a.position(), a.id(), a.label()));
        }
        
        return list;
    }
    
    private int fPosition;
    
    public TextPositionAction(IWorkbenchPart part, int position, String id, String text) {
        super(part, AS_RADIO_BUTTON);
        fPosition = position;
        setId(id);
        setActionDefinitionId(id); // Command key binding support
        setText(text);
    }

    @Override
    public void run() {
        execute(createCommand());
    }
    
    @Override
    protected boolean calculateEnabled() {
        List<ITextPosition> selected = getValidSelectedObjects();
        
        boolean checked = !selected.isEmpty();
        for(ITextPosition tp : selected) {
            if(tp.getTextPosition() != fPosition) {
                checked = false;
            }
        }
        setChecked(checked);
        
        return !selected.isEmpty();
    }
    
    private Command createCommand() {
        CompoundCommand result = new CompoundCommand(Messages.TextPositionAction_9);
        
        for(ITextPosition object : getValidSelectedObjects()) {
            Command cmd = new TextPositionCommand(object, fPosition);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }

        return result.unwrap();
    }
    
    private List<ITextPosition> getValidSelectedObjects() {
        List<ITextPosition> list = new ArrayList<>();
        
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart editPart && editPart.getModel() instanceof ITextPosition textPositionObject
                    && !(textPositionObject instanceof ILockable lockable && lockable.isLocked())
                    && ObjectUIFactory.INSTANCE.getProvider(textPositionObject) instanceof IGraphicalObjectUIProvider provider
                    && provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION.getName())) {
                list.add(textPositionObject);
            }
        }
        
        return list;
    }

}
