/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import uk.ac.bolton.archimate.editor.diagram.commands.TextAlignmentCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IFontAttribute;


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
    
    private int fAlignment;
    
    public TextAlignmentAction(IWorkbenchPart part, int alignment) {
        super(part);
        
        fAlignment = alignment;
        
        switch(alignment) {
            case IFontAttribute.TEXT_ALIGNMENT_LEFT:
                setText(ACTION_LEFT_TEXT);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ALIGN_TEXT_LEFT));
                setId(ACTION_LEFT_ID);
                break;

            case IFontAttribute.TEXT_ALIGNMENT_CENTER:
                setText(ACTION_CENTER_TEXT);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ALIGN_TEXT_CENTER));
                setId(ACTION_CENTER_ID);
                break;

            case IFontAttribute.TEXT_ALIGNMENT_RIGHT:
                setText(ACTION_RIGHT_TEXT);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ALIGN_TEXT_RIGHT));
                setId(ACTION_RIGHT_ID);
                break;

            default:
                break;
        }
    }

    @Override
    protected boolean calculateEnabled() {
        return getFirstSelectedValidEditPart(getSelectedObjects()) != null;
    }

    private ITextAlignedEditPart getFirstSelectedValidEditPart(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(object instanceof ITextAlignedEditPart) {
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
