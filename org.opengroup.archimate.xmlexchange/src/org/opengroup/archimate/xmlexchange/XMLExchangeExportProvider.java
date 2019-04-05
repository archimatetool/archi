/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.opengroup.archimate.xmlexchange.wizard.ExportToXMLWizard;

import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;



/**
 * Export Archi Model to Open Exchange XML Format
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangeExportProvider implements IModelExporter, IXMLExchangeGlobals {
    
    @Override
    public void export(IArchimateModel model) throws IOException {
        WizardDialog dialog = new ExtendedWizardDialog(Display.getCurrent().getActiveShell(),
                new ExportToXMLWizard(model),
                "ExportToXMLWizard") { //$NON-NLS-1$
            
            
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.XMLExchangeExportProvider_0);
            }
        };
        
        dialog.open();
    }
}
