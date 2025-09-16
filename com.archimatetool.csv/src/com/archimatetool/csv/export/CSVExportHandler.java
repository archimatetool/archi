package com.archimatetool.csv.export;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Export
 * 
 * @author Phillip Beauvoir
 */
public class CSVExportHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            export(HandlerUtil.getActiveWorkbenchWindow(event), model);
        }

        return null;
    }
    
    private void export(IWorkbenchWindow window, IArchimateModel model) {
        CSVExporter exporter = new CSVExporter(model);
        
        WizardDialog dialog = new ExtendedWizardDialog(window.getShell(),
                new ExportAsCSVWizard(exporter),
                "ExportAsCSVWizard") { //$NON-NLS-1$
            
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.CSVExportHandler_0);
            }
        };
        
        dialog.open();
    }

}
