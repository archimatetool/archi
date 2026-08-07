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
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;



/**
 * Line Width Action
 * 
 * @author Phillip Beauvoir
 */
public class LineWidthAction extends SelectionAction {
    
    public static final String ID = "LineWidthAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.LineWidthAction_0;
    
    public LineWidthAction(IWorkbenchPart part) {
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
        ILineObject lineObject = getFirstValidSelectedModelObject();
        if(lineObject == null) {
            return;
        }
        
        // Set default line width on first selected connection
        int lineWidth = lineObject.getLineWidth();

        LineWidthDialog dialog = new LineWidthDialog(getWorkbenchPart().getSite().getShell(), lineWidth);
        if(dialog.open() == Window.OK) {
            CompoundCommand compoundCommand = new CompoundCommand(Messages.LineWidthAction_1);
            
            for(EditPart editPart : getSelectedEditParts()) {
                Object model = editPart.getModel();
                if(isValidObject(model)) {
                    Command cmd = new LineWidthCommand((ILineObject)model, dialog.getLineWidth());
                    if(cmd.canExecute()) {
                        compoundCommand.add(cmd);
                    }
                }
            }
            
            execute(compoundCommand.unwrap());
        }
    }
    
    private ILineObject getFirstValidSelectedModelObject() {
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                return (ILineObject)model;
            }
        }
        
        return null;
    }

    private boolean isValidObject(Object object) {
        if(object instanceof ILockable lockable && lockable.isLocked()) {
            return false;
        }
        
        if(object instanceof ILineObject lineObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(lineObject);
            return provider != null && provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName());
        }
        
        return false;
    }

    
    private static class LineWidthDialog extends Dialog {
        private Combo combo;
        private int lineWidth;
        
        private static final String[] comboLineWidthItems = {
                Messages.LineWidthAction_2,
                Messages.LineWidthAction_3,
                Messages.LineWidthAction_4
        };

        protected LineWidthDialog(Shell parent, int lineWidth) {
            super(parent);
            this.lineWidth = lineWidth;
        }
        
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText(Messages.LineWidthAction_1);
        }
        
        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite)super.createDialogArea(parent);
            
            combo = new Combo(composite, SWT.READ_ONLY);
            combo.setItems(comboLineWidthItems);
            combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            combo.select(lineWidth - 1);
            
            return composite;
        }
        
        protected int getLineWidth() {
            return lineWidth;
        }
        
        @Override
        protected void okPressed() {
            lineWidth = combo.getSelectionIndex() + 1;
            super.okPressed();
        }
    }
    
}
