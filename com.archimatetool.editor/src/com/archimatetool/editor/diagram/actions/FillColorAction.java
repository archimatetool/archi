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

import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
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
        return getFirstSelectedEditPart(getSelectedObjects()) != null;
    }

    private EditPart getFirstSelectedEditPart(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                
                if(shouldFillColor(model)) {
                    return (EditPart)object;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        ColorDialog colorDialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;
        EditPart firstPart = getFirstSelectedEditPart(selection);
        if(firstPart != null) {
            Object model = firstPart.getModel();
            if(model instanceof IDiagramModelObject) {
                String s = ((IDiagramModelObject)model).getFillColor();
                if(s == null) {
                    defaultRGB = ColorFactory.getDefaultFillColor(model).getRGB();
                }
                else {
                    defaultRGB = ColorFactory.convertStringToRGB(s);
                }
            }
        }
        
        if(defaultRGB != null) {
            colorDialog.setRGB(defaultRGB);
        }
        
        RGB newColor = colorDialog.open();
        if(newColor != null) {
            execute(createCommand(selection, newColor));
        }
    }
    
    private Command createCommand(List<?> selection, RGB newColor) {
        CompoundCommand result = new CompoundCommand(Messages.FillColorAction_1);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                EditPart editPart = (EditPart)object;
                Object model = editPart.getModel();
                
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                
                if(shouldFillColor(model)) {
                    IDiagramModelObject diagramObject = (IDiagramModelObject)model;
                    Command cmd = new FillColorCommand(diagramObject, ColorFactory.convertRGBToString(newColor));
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }

        return result.unwrap();
    }
    
    
    private boolean shouldFillColor(Object model) {
        return (model instanceof IDiagramModelObject) &&
                (((IDiagramModelObject)model).shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR));
    }
}
