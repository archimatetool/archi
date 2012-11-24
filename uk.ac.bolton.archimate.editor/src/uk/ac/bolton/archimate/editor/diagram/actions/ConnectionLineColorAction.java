/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IWorkbenchPart;

import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineColorCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.IDiagramConnectionEditPart;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Connection Color Action
 * 
 * @author Phillip Beauvoir
 */
public class ConnectionLineColorAction extends SelectionAction {
    
    public static final String ID = "ConnectionLineColorAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.ConnectionLineColorAction_0;
    
    public ConnectionLineColorAction(IWorkbenchPart part) {
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
            if(isValidEditPart(object)) {
                return (EditPart)object;
            }
        }
        
        return null;
    }
    
    private boolean isValidEditPart(Object object) {
        return object instanceof IDiagramConnectionEditPart;
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
            if(model instanceof IDiagramModelConnection) {
                String s = ((IDiagramModelConnection)model).getLineColor();
                if(s != null) {
                    defaultRGB = ColorFactory.convertStringToRGB(s);
                }
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
        CompoundCommand result = new CompoundCommand(Messages.ConnectionLineColorAction_1);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                EditPart editPart = (EditPart)object;
                Object model = editPart.getModel();
                if(model instanceof IDiagramModelConnection) {
                    IDiagramModelConnection connection = (IDiagramModelConnection)model;
                    Command cmd = new ConnectionLineColorCommand(connection, ColorFactory.convertRGBToString(newColor));
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }

        return result.unwrap();
    }
    
}
