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

import com.archimatetool.editor.diagram.commands.FontColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.CustomColorDialog;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILockable;



/**
 * Font Color Action
 * 
 * @author Phillip Beauvoir
 */
public class FontColorAction extends SelectionAction {
    
    public static final String ID = "FontColorAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.FontColorAction_0;
    
    public FontColorAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return getFirstValidSelectedModelObject() != null;
    }
    
    @Override
    public void run() {
        IFontAttribute fontObject = getFirstValidSelectedModelObject();
        if(fontObject == null) {
            return;
        }

        CustomColorDialog colorDialog = new CustomColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;
        
        String s = fontObject.getFontColor();
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
        
        CompoundCommand compoundCommand = new CompoundCommand(Messages.FontColorAction_1);
        
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                Command cmd = new FontColorCommand((IFontAttribute)model, ColorFactory.convertRGBToString(newColor));
                if(cmd.canExecute()) {
                    compoundCommand.add(cmd);
                }
            }
        }
        
        
        execute(compoundCommand.unwrap());
    }
    
    private IFontAttribute getFirstValidSelectedModelObject() {
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                return (IFontAttribute)model;
            }
        }
        
        return null;
    }
    
    private boolean isValidObject(Object object) {
        if(object instanceof ILockable lockable && lockable.isLocked()) {
            return false;
        }
        
        if(object instanceof IFontAttribute fontObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider((fontObject));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR.getName());
        }
        
        return false;
    }
}
