/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.export;

import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;



/**
 * Export Model to CSV Provider
 * 
 * @author Phillip Beauvoir
 */
public class CSVExportProvider implements IModelExporter {
    
    @Override
    public void export(IArchimateModel model) throws IOException {
        CSVExporter exporter = new CSVExporter(model);
        
        WizardDialog dialog = new ExtendedWizardDialog(Display.getCurrent().getActiveShell(),
                new ExportAsCSVWizard(exporter),
                "ExportAsCSVWizard") { //$NON-NLS-1$
            
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.CSVExportProvider_0);
            }
        };
        
        dialog.open();
    }
}
