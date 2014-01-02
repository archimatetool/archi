/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.editor.diagram.editparts.connections.IDiagramConnectionEditPart;
import com.archimatetool.editor.propertysections.DiagramConnectionSection;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Fill Color Action
 * 
 * @author Phillip Beauvoir
 */
public class ConnectionLineWidthAction extends SelectionAction {
    
    public static final String ID = "ConnectionLineWidthAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.ConnectionLineWidthAction_0;
    
    public ConnectionLineWidthAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return getFirstSelectedConnectionEditPart(getSelectedObjects()) != null;
    }

    private IDiagramConnectionEditPart getFirstSelectedConnectionEditPart(List<?> selection) {
        for(Object object : getSelectedObjects()) {
            if(object instanceof IDiagramConnectionEditPart) {
                return (IDiagramConnectionEditPart)object;
            }
        }
        
        return null;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        int lineWidth = 1;
        
        // Set line width on first selected connection
        IDiagramConnectionEditPart firstPart = getFirstSelectedConnectionEditPart(selection);
        if(firstPart != null) {
            Object model = firstPart.getModel();
            if(model instanceof IDiagramModelConnection) {
                lineWidth = ((IDiagramModelConnection)model).getLineWidth();
            }
        }
        
        LineWidthDialog dialog = new LineWidthDialog(getWorkbenchPart().getSite().getShell(), lineWidth);
        if(dialog.open() == Window.OK) {
            execute(createCommand(selection, dialog.getLineWidth()));
        }
    }
    
    private Command createCommand(List<?> selection, int newLineWidth) {
        CompoundCommand result = new CompoundCommand(Messages.ConnectionLineWidthAction_1);
        
        for(Object object : selection) {
            if(object instanceof IDiagramConnectionEditPart) {
                IDiagramConnectionEditPart editPart = (IDiagramConnectionEditPart)object;
                Object model = editPart.getModel();
                if(model instanceof IDiagramModelConnection) {
                    IDiagramModelConnection diagramConnection = (IDiagramModelConnection)model;
                    Command cmd = new LineWidthCommand(diagramConnection, newLineWidth);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        
        return result.unwrap();
    }
    
    private static class LineWidthDialog extends Dialog {
        private Combo fCombo;
        private int fLineWidth;

        protected LineWidthDialog(Shell parent, int lineWidth) {
            super(parent);
            fLineWidth = lineWidth;
        }
        
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText(Messages.ConnectionLineWidthAction_1);
        }
        
        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite)super.createDialogArea(parent);
            
            fCombo = new Combo(composite, SWT.READ_ONLY);
            fCombo.setItems(DiagramConnectionSection.comboLineWidthItems);
            fCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fCombo.select(fLineWidth - 1);
            
            return composite;
        }
        
        protected int getLineWidth() {
            return fLineWidth;
        }
        
        @Override
        protected void okPressed() {
            fLineWidth = fCombo.getSelectionIndex() + 1;
            super.okPressed();
        }
    }
    
}
