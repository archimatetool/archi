/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.BorderColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.ILockable;



/**
 * Border Color Action
 * 
 * @author Phillip Beauvoir
 */
public class BorderColorAction extends SelectionAction {
    
    public static final String ID = "BorderColorAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.BorderColorAction_0;
    
    public BorderColorAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return getFirstSelectedFontEditPart(getSelectedObjects()) != null;
    }

    private EditPart getFirstSelectedFontEditPart(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(isValidEditPart(object)) {
                return (EditPart)object;
            }
        }
        
        return null;
    }
    
    private boolean isValidEditPart(Object object) {
        if(object instanceof EditPart && ((EditPart)object).getModel() instanceof IBorderObject) {
            Object model = ((EditPart)object).getModel();
            if(model instanceof ILockable) {
                return !((ILockable)model).isLocked();
            }
            return true;
        }
        
        return false;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        ColorDialog colorDialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;
        EditPart firstPart = getFirstSelectedFontEditPart(selection);
        if(firstPart != null) {
            IBorderObject model = (IBorderObject)firstPart.getModel();
            String s = model.getBorderColor();
            if(s != null) {
                defaultRGB = ColorFactory.convertStringToRGB(s);
            }
        }
        
        if(defaultRGB != null) {
            colorDialog.setRGB(defaultRGB);
        }
        else {
            colorDialog.setRGB(new RGB(0, 0, 0));
        }
        
        RGB newColor = colorDialog.open();
        if(newColor != null) {
            execute(createCommand(selection, newColor));
        }
    }
    
    private Command createCommand(List<?> selection, RGB newColor) {
        CompoundCommand result = new CompoundCommand(Messages.BorderColorAction_1);
        
        for(Object object : selection) {
            if(isValidEditPart(object)) {
                EditPart editPart = (EditPart)object;
                Command cmd = new BorderColorCommand((IBorderObject)editPart.getModel(), ColorFactory.convertRGBToString(newColor));
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }

        return result.unwrap();
    }
    
}
