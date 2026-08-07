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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.FontCompoundCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILockable;



/**
 * Font Action
 * 
 * @author Phillip Beauvoir
 */
public class FontAction extends SelectionAction {
    
    public static final String ID = "FontAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.FontAction_0;
    
    public FontAction(IWorkbenchPart part) {
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

        // Set default font on first selected object
        FontData fontData = FontFactory.getDefaultUserViewFontData();
        String rgbValue = null;
        
        rgbValue = fontObject.getFontColor();
        String fontValue = fontObject.getFont();
        if(fontValue != null) {
            try {
                fontData = new FontData(fontValue);
            }
            catch(Exception ex) {
                //ex.printStackTrace();
            }
        }

        FontDialog dialog = new FontDialog(getWorkbenchPart().getSite().getShell());
        dialog.setText(Messages.FontAction_1);
        dialog.setEffectsVisible(false); // Don't allow underline/strikeout on Windows. See https://github.com/archimatetool/archi/issues/851
        dialog.setFontList(new FontData[] { fontData } );
        dialog.setRGB(ColorFactory.convertStringToRGB(rgbValue));

        FontData selectedFontData = dialog.open();
        if(selectedFontData == null) {
            return;
        }
        
        CompoundCommand compoundCommand = new CompoundCommand(Messages.FontAction_2);
        
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                Command cmd = new FontCompoundCommand((IFontAttribute)model, selectedFontData.toString(), dialog.getRGB());
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
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT.getName());
        }
        
        return false;
    }

}
