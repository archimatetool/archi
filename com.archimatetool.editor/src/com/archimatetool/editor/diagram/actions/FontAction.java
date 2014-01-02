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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.FontCompoundCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
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
        if(object instanceof EditPart && ((EditPart)object).getModel() instanceof IFontAttribute) {
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
        
        // Set default font on first selected object
        FontData fontData = FontFactory.getDefaultUserViewFontData();
        String rgbValue = null;
        
        EditPart firstPart = getFirstSelectedFontEditPart(selection);
        if(firstPart != null) {
            IFontAttribute model = (IFontAttribute)firstPart.getModel();
            rgbValue = model.getFontColor();
            String fontValue = model.getFont();
            if(fontValue != null) {
                try {
                    fontData = new FontData(fontValue);
                }
                catch(Exception ex) {
                    //ex.printStackTrace();
                }
            }
        }
        
        FontDialog dialog = new FontDialog(getWorkbenchPart().getSite().getShell());
        dialog.setText(Messages.FontAction_1);
        dialog.setFontList(new FontData[] { fontData } );
        dialog.setRGB(ColorFactory.convertStringToRGB(rgbValue));
        
        FontData selectedFontData = dialog.open();
        if(selectedFontData != null) {
            execute(createCommand(selection, selectedFontData, dialog.getRGB()));
        }
    }
    
    private Command createCommand(List<?> selection, FontData selectedFontData, RGB newColor) {
        CompoundCommand result = new CompoundCommand(Messages.FontAction_2);
        
        for(Object object : selection) {
            if(isValidEditPart(object)) {
                EditPart editPart = (EditPart)object;
                Command cmd = new FontCompoundCommand((IFontAttribute)editPart.getModel(), selectedFontData.toString(), newColor);
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        return result.unwrap();
    }
}
