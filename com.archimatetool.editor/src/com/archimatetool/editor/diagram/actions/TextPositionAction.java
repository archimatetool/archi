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
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextPosition;



/**
 * Text Position Action
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionAction extends SelectionAction {
    
    public static final String ACTION_TOP_ID = "TextPositionActionTop"; //$NON-NLS-1$
    public static final String ACTION_CENTRE_ID = "TextPositionActionCentre"; //$NON-NLS-1$
    public static final String ACTION_BOTTOM_ID = "TextPositionActionBottom"; //$NON-NLS-1$
    
    public static final String ACTION_TOP_TEXT = Messages.TextPositionAction_0;
    public static final String ACTION_CENTRE_TEXT = Messages.TextPositionAction_1;
    public static final String ACTION_BOTTOM_TEXT = Messages.TextPositionAction_2;
    
    public static final String ACTION_IDS[] = {
            ACTION_TOP_ID,
            ACTION_CENTRE_ID,
            ACTION_BOTTOM_ID
    };
    
    public static final String ACTION_TEXTS[] = {
            ACTION_TOP_TEXT,
            ACTION_CENTRE_TEXT,
            ACTION_BOTTOM_TEXT
    };
    
    public static List<RetargetAction> createRetargetActions() {
        List<RetargetAction> list = new ArrayList<RetargetAction>();
        
        for(int i = 0; i < ACTION_IDS.length; i++) {
            list.add(new RetargetAction(ACTION_IDS[i], ACTION_TEXTS[i], IAction.AS_RADIO_BUTTON));
        }
     
        return list;
    }
    
    public static List<TextPositionAction> createActions(IWorkbenchPart part) {
        List<TextPositionAction> list = new ArrayList<TextPositionAction>();
        
        list.add(new TextPositionAction(part, ITextPosition.TEXT_POSITION_TOP, ACTION_TOP_ID, ACTION_TOP_TEXT));
        list.add(new TextPositionAction(part, ITextPosition.TEXT_POSITION_CENTRE, ACTION_CENTRE_ID, ACTION_CENTRE_TEXT));
        list.add(new TextPositionAction(part, ITextPosition.TEXT_POSITION_BOTTOM, ACTION_BOTTOM_ID, ACTION_BOTTOM_TEXT));
     
        return list;
    }
    
    private int fPosition;
    
    public TextPositionAction(IWorkbenchPart part, int position, String id, String text) {
        super(part, AS_RADIO_BUTTON);
        fPosition = position;
        setId(id);
        setText(text);
    }

    @Override
    protected boolean calculateEnabled() {
        setChecked(false);
        
        List<?> selected = getSelectedObjects();
        
        ITextPosition model = (ITextPosition)getFirstValidSelectedModelObject(selected);

        if(model != null && selected.size() == 1) {
            setChecked(model.getTextPosition() == fPosition);
        }
        
        return model != null;
    }
    
    private Object getFirstValidSelectedModelObject(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    return model;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        Object model = getFirstValidSelectedModelObject(selection);
        if(model != null) {
            execute(createCommand(selection));
        }
    }
    
    private Command createCommand(List<?> selection) {
        CompoundCommand result = new CompoundCommand(Messages.TextPositionAction_9);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    Command cmd = new TextPositionCommand((ITextPosition)model, fPosition);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }

        return result.unwrap();
    }
    
    private boolean shouldEnable(Object model) {
        if(model instanceof ILockable && ((ILockable)model).isLocked()) {
            return false;
        }
        
        if(model instanceof ITextPosition) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(((ITextPosition)model));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION.getName());
        }
        
        return false;
    }

}
