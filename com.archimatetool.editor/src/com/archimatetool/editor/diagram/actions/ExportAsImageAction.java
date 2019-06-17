/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.diagram.wizard.ExportAsImageWizard;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;



/**
 * Exmport As Image Action
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageAction extends Action {
    
    public static final String ID = "ExportAsImageAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.ExportAsImageAction_0;

    private GraphicalViewer fGraphicalViewer;
    
    public ExportAsImageAction(GraphicalViewer graphicalViewer) {
        super(TEXT);
        fGraphicalViewer = graphicalViewer;
        setId(ID);
    }

    @Override
    public void run() {
        LayerManager layerManager = (LayerManager)fGraphicalViewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
        
        Object model = fGraphicalViewer.getContents().getModel();
        String name = ArchiLabelProvider.INSTANCE.getLabel(model);
        
        WizardDialog dialog = new ExtendedWizardDialog(fGraphicalViewer.getControl().getShell(),
                new ExportAsImageWizard(rootFigure, name),
                "ExportAsImageWizard") { //$NON-NLS-1$
            
            
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.ExportAsImageAction_1);
            }
        };
        
        dialog.open();
    }
    
}
