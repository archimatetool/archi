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
import com.archimatetool.editor.diagram.editparts.ITextPositionedEditPart;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILockable;



/**
 * Text Position Action
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionAction extends SelectionAction {
    
    public static final String ACTION_TOP_LEFT_ID = "TextPositionActionTopLeft"; //$NON-NLS-1$
    public static final String ACTION_TOP_CENTRE_ID = "TextPositionActionTopCentre"; //$NON-NLS-1$
    public static final String ACTION_TOP_RIGHT_ID = "TextPositionActionTopRight"; //$NON-NLS-1$
    public static final String ACTION_MIDDLE_LEFT_ID = "TextPositionActionMiddleLeft"; //$NON-NLS-1$
    public static final String ACTION_MIDDLE_CENTRE_ID = "TextPositionActionMiddleCentre"; //$NON-NLS-1$
    public static final String ACTION_MIDDLE_RIGHT_ID = "TextPositionActionMiddleRight"; //$NON-NLS-1$
    public static final String ACTION_BOTTOM_LEFT_ID = "TextPositionActionBottomLeft"; //$NON-NLS-1$
    public static final String ACTION_BOTTOM_CENTRE_ID = "TextPositionActionBottomCentre"; //$NON-NLS-1$
    public static final String ACTION_BOTTOM_RIGHT_ID = "TextPositionActionBottomRight"; //$NON-NLS-1$
    
    public static final String ACTION_TOP_LEFT_TEXT = Messages.TextPositionAction_0;
    public static final String ACTION_TOP_CENTRE_TEXT = Messages.TextPositionAction_1;
    public static final String ACTION_TOP_RIGHT_TEXT = Messages.TextPositionAction_2;
    public static final String ACTION_MIDDLE_LEFT_TEXT = Messages.TextPositionAction_3;
    public static final String ACTION_MIDDLE_CENTRE_TEXT = Messages.TextPositionAction_4;
    public static final String ACTION_MIDDLE_RIGHT_TEXT = Messages.TextPositionAction_5;
    public static final String ACTION_BOTTOM_LEFT_TEXT = Messages.TextPositionAction_6;
    public static final String ACTION_BOTTOM_CENTRE_TEXT = Messages.TextPositionAction_7;
    public static final String ACTION_BOTTOM_RIGHT_TEXT = Messages.TextPositionAction_8;
    
    public static final String ACTION_IDS[] = {
        ACTION_TOP_LEFT_ID,
        ACTION_TOP_CENTRE_ID,
        ACTION_TOP_RIGHT_ID,
        ACTION_MIDDLE_LEFT_ID,
        ACTION_MIDDLE_CENTRE_ID,
        ACTION_MIDDLE_RIGHT_ID,
        ACTION_BOTTOM_LEFT_ID,
        ACTION_BOTTOM_CENTRE_ID,
        ACTION_BOTTOM_RIGHT_ID
    };
    
    public static final String ACTION_TEXTS[] = {
        ACTION_TOP_LEFT_TEXT,
        ACTION_TOP_CENTRE_TEXT,
        ACTION_TOP_RIGHT_TEXT,
        ACTION_MIDDLE_LEFT_TEXT,
        ACTION_MIDDLE_CENTRE_TEXT,
        ACTION_MIDDLE_RIGHT_TEXT,
        ACTION_BOTTOM_LEFT_TEXT,
        ACTION_BOTTOM_CENTRE_TEXT,
        ACTION_BOTTOM_RIGHT_TEXT
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
        
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_TOP_LEFT, ACTION_TOP_LEFT_ID, ACTION_TOP_LEFT_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_TOP_CENTRE, ACTION_TOP_CENTRE_ID, ACTION_TOP_CENTRE_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_TOP_RIGHT, ACTION_TOP_RIGHT_ID, ACTION_TOP_RIGHT_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_MIDDLE_LEFT, ACTION_MIDDLE_LEFT_ID, ACTION_MIDDLE_LEFT_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_MIDDLE_CENTRE, ACTION_MIDDLE_CENTRE_ID, ACTION_MIDDLE_CENTRE_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_MIDDLE_RIGHT, ACTION_MIDDLE_RIGHT_ID, ACTION_MIDDLE_RIGHT_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_BOTTOM_LEFT, ACTION_BOTTOM_LEFT_ID, ACTION_BOTTOM_LEFT_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_BOTTOM_CENTRE, ACTION_BOTTOM_CENTRE_ID, ACTION_BOTTOM_CENTRE_TEXT));
        list.add(new TextPositionAction(part, IFontAttribute.TEXT_POSITION_BOTTOM_RIGHT, ACTION_BOTTOM_RIGHT_ID, ACTION_BOTTOM_RIGHT_TEXT));
     
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
        ITextPositionedEditPart editPart = getFirstSelectedValidEditPart(selected);

        if(editPart != null && selected.size() == 1) {
            Object model = editPart.getModel();
            if(model instanceof IFontAttribute) {
                setChecked(((IFontAttribute)model).getTextPosition() == fPosition);
            }
        }
        
        return editPart != null;
    }
    
    private ITextPositionedEditPart getFirstSelectedValidEditPart(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(object instanceof ITextPositionedEditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                return (ITextPositionedEditPart)object;
            }
        }
        
        return null;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        ITextPositionedEditPart firstPart = getFirstSelectedValidEditPart(selection);
        if(firstPart != null) {
            execute(createCommand(selection));
        }
    }
    
    private Command createCommand(List<?> selection) {
        CompoundCommand result = new CompoundCommand(Messages.TextPositionAction_9);
        
        for(Object object : selection) {
            if(object instanceof ITextPositionedEditPart) {
                ITextPositionedEditPart editPart = (ITextPositionedEditPart)object;
                Object model = editPart.getModel();
                if(model instanceof IFontAttribute) {
                    IFontAttribute fontObject = (IFontAttribute)model;
                    Command cmd = new TextPositionCommand(fontObject, fPosition);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }

        return result.unwrap();
    }
    
}
