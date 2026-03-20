/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.IFigure;
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
    
    private ZestGraphViewer graphViewer;

    public ExportAsImageAction(ZestGraphViewer graphViewer) {
        super(Messages.ExportAsImageAction_0 + "..."); //$NON-NLS-1$
        setToolTipText(Messages.ExportAsImageAction_0);
        this.graphViewer = graphViewer;
    }
    
    @Override
    public void run() {
        IFigure figure = (IFigure)graphViewer.getGraphControl().getRootLayer().getChildren().get(0);
        String name = ArchiLabelProvider.INSTANCE.getLabel(graphViewer.getInput());
        
        WizardDialog dialog = new ExtendedWizardDialog(graphViewer.getControl().getShell(),
                new ExportAsImageWizard(figure, name),
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
