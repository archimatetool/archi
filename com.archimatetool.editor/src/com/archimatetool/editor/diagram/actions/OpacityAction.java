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
        return getFirstValidSelectedModelObject(getSelectedObjects()) != null;
    }

    protected Object getFirstValidSelectedModelObject(List<?> selection) {
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
        
        IDiagramModelObject dmo = (IDiagramModelObject)getFirstValidSelectedModelObject(selection);
        if(dmo == null) {
            return;
        }
        
        // Set default alpha on first selected
        int alpha = dmo.getAlpha();

        OpacityDialog dialog = new OpacityDialog(getWorkbenchPart().getSite().getShell(), alpha);
        if(dialog.open() == Window.OK) {
            execute(createCommand(selection, dialog.getAlpha()));
        }
    }
    
    protected Command createCommand(List<?> selection, int alpha) {
        CompoundCommand result = new CompoundCommand(Messages.OpacityAction_0);
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(shouldEnable(model)) {
                    Command cmd = getCommand((IDiagramModelObject)model, alpha);
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
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(((IDiagramModelObject)model));
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
        private Spinner fSpinner;
        private int fAlpha;

        protected OpacityDialog(Shell parent, int alpha) {
            super(parent);
            fAlpha = alpha;
        }
        
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText(Messages.OpacityAction_0);
        }
        
        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite)super.createDialogArea(parent);
            
            composite.setLayout(new GridLayout(2, true));
            
            Label label = new Label(composite, SWT.NONE);
            label.setText(Messages.OpacityAction_0 + ": "); //$NON-NLS-1$
            
            fSpinner = new Spinner(composite, SWT.BORDER);
            fSpinner.setMinimum(0);
            fSpinner.setMaximum(255);
            fSpinner.setIncrement(5);
            
            fSpinner.setSelection(fAlpha);
            
            return composite;
        }
        
        protected int getAlpha() {
            return fAlpha;
        }
        
        @Override
        protected void okPressed() {
            fAlpha = fSpinner.getSelection();
            super.okPressed();
        }
    }
    
}
