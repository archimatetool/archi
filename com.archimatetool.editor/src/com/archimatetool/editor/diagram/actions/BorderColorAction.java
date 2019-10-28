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
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.BorderColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.CustomColorDialog;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
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
        return getFirstValidSelectedModelObject(getSelectedObjects()) != null;
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
        
        IBorderObject model = (IBorderObject)getFirstValidSelectedModelObject(selection);
        if(model == null) {
            return;
        }
        
        CustomColorDialog colorDialog = new CustomColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;
        
        String s = model.getBorderColor();
        if(s != null) {
            defaultRGB = ColorFactory.convertStringToRGB(s);
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
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    Command cmd = new BorderColorCommand((IBorderObject)model, ColorFactory.convertRGBToString(newColor));
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
        
        if(model instanceof IBorderObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(((IBorderObject)model));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR.getName());
        }
        
        return false;
    }
}
