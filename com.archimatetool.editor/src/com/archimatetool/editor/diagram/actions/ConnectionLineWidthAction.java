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
import com.archimatetool.editor.propertysections.DiagramConnectionSection;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;



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
        
        // Set default line width on first selected connection
        int lineWidth = model.getLineWidth();

        LineWidthDialog dialog = new LineWidthDialog(getWorkbenchPart().getSite().getShell(), lineWidth);
        if(dialog.open() == Window.OK) {
            execute(createCommand(selection, dialog.getLineWidth()));
        }
    }
    
    private Command createCommand(List<?> selection, int newLineWidth) {
        CompoundCommand result = new CompoundCommand(Messages.ConnectionLineWidthAction_1);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    Command cmd = new LineWidthCommand((ILineObject)model, newLineWidth);
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
        
        if(model instanceof IDiagramModelConnection) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(((IDiagramModelConnection)model));
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName());
        }
        
        return false;
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
