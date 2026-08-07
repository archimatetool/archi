/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

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
        return getFirstValidSelectedBorderObject() != null;
    }

    @Override
    public void run() {
        IBorderObject borderObject = getFirstValidSelectedBorderObject();
        if(borderObject == null) {
            return;
        }
        
        CustomColorDialog colorDialog = new CustomColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;
        
        String s = borderObject.getBorderColor();
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
        if(newColor == null) {
            return;
        }
        
        CompoundCommand compoundCommand = new CompoundCommand(Messages.BorderColorAction_1);
        
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                Command cmd = new BorderColorCommand((IBorderObject)model, ColorFactory.convertRGBToString(newColor));
                if(cmd.canExecute()) {
                    compoundCommand.add(cmd);
                }
            }
        }
        
        execute(compoundCommand.unwrap());
    }
    
    private IBorderObject getFirstValidSelectedBorderObject() {
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                return (IBorderObject)model;
            }
        }
        
        return null;
    }
    
    private boolean isValidObject(Object object) {
        if(object instanceof ILockable lockable && lockable.isLocked()) {
            return false;
        }
        
        if(object instanceof IBorderObject borderObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider((borderObject));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR.getName());
        }
        
        return false;
    }
}
