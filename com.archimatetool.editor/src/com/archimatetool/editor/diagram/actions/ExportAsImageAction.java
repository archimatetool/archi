/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.diagram.wizard.ExportAsImageWizard;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IDiagramModel;



/**
 * Export As Image Action
 * 
 * We create a new GraphicalViewerImpl instance based on the Diagram Model
 * This means we are guaranteed to be at 100% scale
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageAction extends Action {
    
    public static final String ID = "ExportAsImageAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.ExportAsImageAction_0;

    private IDiagramModel diagramModel;
    private Shell parentShell;
    
    public ExportAsImageAction(IDiagramModel dm, Shell parentShell) {
        super(TEXT);
        diagramModel = dm;
        this.parentShell = parentShell;
        setId(ID);
    }

    @Override
    public void run() {
        Shell tempShell = new Shell();
        GraphicalViewerImpl viewer = DiagramUtils.createViewer(diagramModel, tempShell);
        tempShell.dispose();
        
        LayerManager layerManager = (LayerManager)viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
        
        WizardDialog dialog = new ExtendedWizardDialog(parentShell,
                new ExportAsImageWizard(rootFigure, ArchiLabelProvider.INSTANCE.getLabel(diagramModel)),
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
