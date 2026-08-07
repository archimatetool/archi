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

import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.CustomColorDialog;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Fill Color Action
 * 
 * @author Phillip Beauvoir
 */
public class FillColorAction extends SelectionAction {
    
    public static final String ID = "FillColorAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.FillColorAction_0;
    
    public FillColorAction(IWorkbenchPart part) {
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
        IDiagramModelObject dmo = getFirstValidSelectedModelObject();
        if(dmo == null) {
            return;
        }

        CustomColorDialog colorDialog = new CustomColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;

        String s = dmo.getFillColor();
        if(s == null) {
            defaultRGB = ColorFactory.getDefaultFillColor(dmo).getRGB();
        }
        else {
            defaultRGB = ColorFactory.convertStringToRGB(s);
        }

        if(defaultRGB != null) {
            colorDialog.setRGB(defaultRGB);
        }

        RGB newColor = colorDialog.open();
        if(newColor == null) {
            return;
        }
        
        CompoundCommand compoundCommand = new CompoundCommand(Messages.FillColorAction_1);
        
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                Command cmd = new FillColorCommand((IDiagramModelObject)model, ColorFactory.convertRGBToString(newColor));
                if(cmd.canExecute()) {
                    compoundCommand.add(cmd);
                }
            }
        }
        
        execute(compoundCommand.unwrap());
    }
    
    private IDiagramModelObject getFirstValidSelectedModelObject() {
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                return (IDiagramModelObject)model;
            }
        }
        
        return null;
    }
    
    private boolean isValidObject(Object object) {
        if(object instanceof ILockable lockable && lockable.isLocked()) {
            return false;
        }
        
        if(object instanceof IDiagramModelObject dmo) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider((dmo));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName());
        }
        
        return false;
    }
}
