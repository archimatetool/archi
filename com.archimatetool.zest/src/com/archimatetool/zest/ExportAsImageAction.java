/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.diagram.wizard.ExportAsImageWizard;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;


/**
 * Export As Image Action
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageAction extends Action {
    
    private ZestGraphViewer fGraphViewer;

    public ExportAsImageAction(ZestGraphViewer graphViewer) {
        super(Messages.ExportAsImageAction_0 + "..."); //$NON-NLS-1$
        setToolTipText(Messages.ExportAsImageAction_0);
        fGraphViewer = graphViewer;
    }
    
    @Override
    public void run() {
        Object model = fGraphViewer.getInput();
        String name = ArchiLabelProvider.INSTANCE.getLabel(model);
        
        WizardDialog dialog = new ExtendedWizardDialog(fGraphViewer.getControl().getShell(),
                new ExportAsImageWizard(fGraphViewer.getGraphControl().getContents(), name),
                "ExportZestViewAsImage") { //$NON-NLS-1$

            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.ExportAsImageAction_3);
            }
        };
        
        dialog.open();
    }
    
}
