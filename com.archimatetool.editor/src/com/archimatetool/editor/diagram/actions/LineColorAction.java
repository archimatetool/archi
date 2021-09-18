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

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.CustomColorDialog;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;



/**
 * Line Color Action
 * 
 * @author Phillip Beauvoir
 */
public class LineColorAction extends SelectionAction {
    
    public static final String ID = "LineColorAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.LineColorAction_0;
    
    public LineColorAction(IWorkbenchPart part) {
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
        
        ILineObject model = (ILineObject)getFirstValidSelectedModelObject(selection);
        if(model == null) {
            return;
        }

        CustomColorDialog colorDialog = new CustomColorDialog(getWorkbenchPart().getSite().getShell());
        
        // Set default RGB on first selected object
        RGB defaultRGB = null;

        String s = model.getLineColor();
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
        CompoundCommand result = new CompoundCommand(Messages.LineColorAction_1);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    Command cmd = new LineColorCommand((ILineObject)model, ColorFactory.convertRGBToString(newColor));
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
        
        if(model instanceof IDiagramModelObject) {
            // Disable if diagram model object line colours are derived from fill colours as set in Prefs
            if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR)) {
                return false;
            }
        }
        
        if(model instanceof ILineObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(((ILineObject)model));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName());
        }
        
        return false;
    }

}
