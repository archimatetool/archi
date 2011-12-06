/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import uk.ac.bolton.archimate.editor.diagram.commands.TextAlignmentCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Text Alignment Action
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentAction extends SelectionAction {
    
    public static final String ACTION_LEFT_ID = "TextAlignmentActionLeft";
    public static final String ACTION_CENTER_ID = "TextAlignmentActionCenter";
    public static final String ACTION_RIGHT_ID = "TextAlignmentActionRight";
    
    public static final String ACTION_LEFT_TEXT = "Left";
    public static final String ACTION_CENTER_TEXT = "Centre";
    public static final String ACTION_RIGHT_TEXT = "Right";
    
    public static final String ACTION_IDS[] = {
        ACTION_LEFT_ID,
        ACTION_CENTER_ID,
        ACTION_RIGHT_ID
    };
    
    public static final String ACTION_TEXTS[] = {
        ACTION_LEFT_TEXT,
        ACTION_CENTER_TEXT,
        ACTION_RIGHT_TEXT
    };

    public static List<RetargetAction> createRetargetActions() {
        List<RetargetAction> list = new ArrayList<RetargetAction>();
        
        for(int i = 0; i < ACTION_IDS.length; i++) {
            list.add(new RetargetAction(ACTION_IDS[i], ACTION_TEXTS[i], IAction.AS_RADIO_BUTTON));
        }
     
        return list;
    }
    
    public static List<TextAlignmentAction> createActions(IWorkbenchPart part) {
        List<TextAlignmentAction> list = new ArrayList<TextAlignmentAction>();
        
        list.add(new TextAlignmentAction(part, IFontAttribute.TEXT_ALIGNMENT_LEFT, ACTION_LEFT_ID, ACTION_LEFT_TEXT,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ALIGN_TEXT_LEFT)));
        list.add(new TextAlignmentAction(part, IFontAttribute.TEXT_ALIGNMENT_CENTER, ACTION_CENTER_ID, ACTION_CENTER_TEXT,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ALIGN_TEXT_CENTER)));
        list.add(new TextAlignmentAction(part, IFontAttribute.TEXT_ALIGNMENT_RIGHT, ACTION_RIGHT_ID, ACTION_RIGHT_TEXT,
                IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ALIGN_TEXT_RIGHT)));
     
        return list;
    }
    
    private int fAlignment;
    
    public TextAlignmentAction(IWorkbenchPart part, int alignment, String id, String text, ImageDescriptor desc) {
        super(part, AS_RADIO_BUTTON);
        fAlignment = alignment;
        setId(id);
        setText(text);
    }

    @Override
    protected boolean calculateEnabled() {
        setChecked(false);
        
        List<?> selected = getSelectedObjects();
        ITextAlignedEditPart editPart = getFirstSelectedValidEditPart(selected);

        if(editPart != null && selected.size() == 1) {
            Object model = editPart.getModel();
            if(model instanceof IFontAttribute) {
                setChecked(((IFontAttribute)model).getTextAlignment() == fAlignment);
            }
        }
        
        return editPart != null;
    }

    private ITextAlignedEditPart getFirstSelectedValidEditPart(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(object instanceof ITextAlignedEditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                return (ITextAlignedEditPart)object;
            }
        }
        
        return null;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        ITextAlignedEditPart firstPart = getFirstSelectedValidEditPart(selection);
        if(firstPart != null) {
            Object model = firstPart.getModel();
            if(model instanceof IDiagramModelObject) {
                execute(createCommand(selection));
            }
        }
    }
    
    private Command createCommand(List<?> selection) {
        CompoundCommand result = new CompoundCommand("Change text alignment");
        
        for(Object object : selection) {
            if(object instanceof ITextAlignedEditPart) {
                ITextAlignedEditPart editPart = (ITextAlignedEditPart)object;
                Object model = editPart.getModel();
                if(model instanceof IDiagramModelObject) {
                    IDiagramModelObject diagramObject = (IDiagramModelObject)model;
                    Command cmd = new TextAlignmentCommand(diagramObject, fAlignment);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }

        return result.unwrap();
    }
    
}
