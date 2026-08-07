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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Fill Opacity Action
 * 
 * @author Phillip Beauvoir
 */
public class OpacityAction extends SelectionAction {
    
    public static final String ID = "OpacityAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.OpacityAction_0 + "..."; //$NON-NLS-1$
    
    public OpacityAction(IWorkbenchPart part) {
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
        
        // Set default alpha on first selected
        int alpha = dmo.getAlpha();

        OpacityDialog dialog = new OpacityDialog(getWorkbenchPart().getSite().getShell(), alpha);
        if(dialog.open() == Window.OK) {
            execute(createCommand(dialog.getAlpha()));
        }
    }
    
    protected IDiagramModelObject getFirstValidSelectedModelObject() {
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                return (IDiagramModelObject)model;
            }
        }
        
        return null;
    }
    
    protected Command createCommand(int alpha) {
        CompoundCommand result = new CompoundCommand(Messages.OpacityAction_0);
        
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(isValidObject(model)) {
                Command cmd = getCommand((IDiagramModelObject)model, alpha);
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }
        
        return result.unwrap();
    }
    
    private boolean isValidObject(Object object) {
        if(object instanceof ILockable lockable && lockable.isLocked()) {
            return false;
        }
        
        if(object instanceof IDiagramModelObject dmo) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider((dmo));
            return provider != null && provider.shouldExposeFeature(getFeatureName());
        }
        
        return false;
    }

    protected String getFeatureName() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName();
    }
    
    protected Command getCommand(IDiagramModelObject dmo, int newValue) {
        return new DiagramModelObjectAlphaCommand(dmo, newValue);
    }
    
    protected static class OpacityDialog extends Dialog {
        private Spinner spinner;
        private int alpha;

        protected OpacityDialog(Shell parent, int alpha) {
            super(parent);
            this.alpha = alpha;
        }
        
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText(Messages.OpacityAction_1);
        }
        
        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite)super.createDialogArea(parent);
            
            composite.setLayout(new GridLayout(2, true));
            
            Label label = new Label(composite, SWT.NONE);
            label.setText(Messages.OpacityAction_1 + ": "); //$NON-NLS-1$
            
            spinner = new Spinner(composite, SWT.BORDER);
            spinner.setMinimum(0);
            spinner.setMaximum(255);
            spinner.setIncrement(5);
            
            spinner.setSelection(alpha);
            
            return composite;
        }
        
        protected int getAlpha() {
            return alpha;
        }
        
        @Override
        protected void okPressed() {
            alpha = spinner.getSelection();
            super.okPressed();
        }
    }
    
}
