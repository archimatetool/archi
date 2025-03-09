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

import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;



/**
 * Text Alignment Action
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentAction extends SelectionAction {
    
    public static final String ACTION_LEFT_ID = "com.archimatetool.editor.textAlignmentLeft"; //$NON-NLS-1$
    public static final String ACTION_CENTRE_ID = "com.archimatetool.editor.textAlignmentCentre"; //$NON-NLS-1$
    public static final String ACTION_RIGHT_ID = "com.archimatetool.editor.textAlignmentRight"; //$NON-NLS-1$
    
    public static final record TextAlignmentActionDefinition(String id, int alignment, String label) {}
    
    public static final TextAlignmentActionDefinition[] getActionDefinitions() {
        return new TextAlignmentActionDefinition[] {
                new TextAlignmentActionDefinition(ACTION_LEFT_ID, ITextAlignment.TEXT_ALIGNMENT_LEFT, Messages.TextAlignmentAction_0),
                new TextAlignmentActionDefinition(ACTION_CENTRE_ID, ITextAlignment.TEXT_ALIGNMENT_CENTER, Messages.TextAlignmentAction_1),
                new TextAlignmentActionDefinition(ACTION_RIGHT_ID, ITextAlignment.TEXT_ALIGNMENT_RIGHT, Messages.TextAlignmentAction_2)
        };
    }
    
    public static List<RetargetAction> createRetargetActions() {
        List<RetargetAction> list = new ArrayList<>();
        
        for(TextAlignmentActionDefinition a : getActionDefinitions()) {
            RetargetAction action = new RetargetAction(a.id(), a.label(), IAction.AS_RADIO_BUTTON);
            action.setActionDefinitionId(a.id()); // Command key binding support
            list.add(action);
        }
        
        return list;
    }
    
    public static List<TextAlignmentAction> createActions(IWorkbenchPart part) {
        List<TextAlignmentAction> list = new ArrayList<>();
        
        for(TextAlignmentActionDefinition a : getActionDefinitions()) {
            list.add(new TextAlignmentAction(part, a.alignment(), a.id(), a.label()));
        }
        
        return list;
    }
    
    private int fAlignment;
    
    public TextAlignmentAction(IWorkbenchPart part, int alignment, String id, String text) {
        super(part, AS_RADIO_BUTTON);
        fAlignment = alignment;
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
        List<ITextAlignment> selected = getValidSelectedObjects();
        
        boolean checked = !selected.isEmpty();
        for(ITextAlignment ta : selected) {
            if(ta.getTextAlignment() != fAlignment) {
                checked = false;
            }
        }
        setChecked(checked);
        
        return !selected.isEmpty();
    }

    private Command createCommand() {
        CompoundCommand result = new CompoundCommand(Messages.TextAlignmentAction_3);
        
        for(ITextAlignment object : getValidSelectedObjects()) {
            Command cmd = new TextAlignmentCommand(object, fAlignment);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }

        return result.unwrap();
    }
    
    private List<ITextAlignment> getValidSelectedObjects() {
        List<ITextAlignment> list = new ArrayList<>();
        
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart editPart && editPart.getModel() instanceof ITextAlignment textAlignmentObject
                    && !(textAlignmentObject instanceof ILockable lockable && lockable.isLocked())
                    && ObjectUIFactory.INSTANCE.getProvider(textAlignmentObject) instanceof IGraphicalObjectUIProvider provider
                    && provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName())) {
                list.add(textAlignmentObject);
            }
        }
        
        return list;
    }

}
