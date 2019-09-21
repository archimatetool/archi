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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;



/**
 * Text Alignment Action
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentAction extends SelectionAction {
    
    public static final String ACTION_LEFT_ID = "TextAlignmentActionLeft"; //$NON-NLS-1$
    public static final String ACTION_CENTER_ID = "TextAlignmentActionCenter"; //$NON-NLS-1$
    public static final String ACTION_RIGHT_ID = "TextAlignmentActionRight"; //$NON-NLS-1$
    
    public static final String ACTION_LEFT_TEXT = Messages.TextAlignmentAction_0;
    public static final String ACTION_CENTER_TEXT = Messages.TextAlignmentAction_1;
    public static final String ACTION_RIGHT_TEXT = Messages.TextAlignmentAction_2;
    
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
        
        list.add(new TextAlignmentAction(part, ITextAlignment.TEXT_ALIGNMENT_LEFT, ACTION_LEFT_ID, ACTION_LEFT_TEXT,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ALIGN_TEXT_LEFT)));
        list.add(new TextAlignmentAction(part, ITextAlignment.TEXT_ALIGNMENT_CENTER, ACTION_CENTER_ID, ACTION_CENTER_TEXT,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ALIGN_TEXT_CENTER)));
        list.add(new TextAlignmentAction(part, ITextAlignment.TEXT_ALIGNMENT_RIGHT, ACTION_RIGHT_ID, ACTION_RIGHT_TEXT,
                IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ALIGN_TEXT_RIGHT)));
     
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
        
        ITextAlignment model = (ITextAlignment)getFirstValidSelectedModelObject(selected);

        if(model != null && selected.size() == 1) {
            setChecked(model.getTextAlignment() == fAlignment);
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
            if(shouldEnable(model)) {
                execute(createCommand(selection));
            }
        }
    }
    
    private Command createCommand(List<?> selection) {
        CompoundCommand result = new CompoundCommand(Messages.TextAlignmentAction_3);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    Command cmd = new TextAlignmentCommand((ITextAlignment)model, fAlignment);
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
        
        if(model instanceof ITextAlignment) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(((ITextAlignment)model));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName());
        }
        
        return false;
    }

}
