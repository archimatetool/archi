/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
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
        ExportToXMLWizard wizard = new ExportToXMLWizard(model.getName());
        
        WizardDialog dialog = new ExtendedWizardDialog(Display.getCurrent().getActiveShell(),
                wizard,
                "ExportToXMLWizard") { //$NON-NLS-1$
            
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.XMLExchangeExportProvider_0);
            }
        };
        
        if(dialog.open() == Window.OK) {
            File file = wizard.getFile();
            
            if(file != null) {
                // Check valid file name
                try {
                    file.getCanonicalPath();
                }
                catch(IOException ex) {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.XMLExchangeExportProvider_1, Messages.XMLExchangeExportProvider_2);
                }
                
                // Make sure the file does not already exist
                if(file.exists()) {
                    boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                            Messages.XMLExchangeExportProvider_3,
                            "'" + file + "' already exists. Are you sure you want to overwrite it?"); //$NON-NLS-1$ //$NON-NLS-2$
                    if(!result) {
                        return;
                    }
                }

                BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            XMLModelExporter xmlModelExporter = new XMLModelExporter();
                            
                            xmlModelExporter.setMetadata(wizard.getMetadata());
                            xmlModelExporter.setSaveOrganisation(wizard.doSaveOrganisation());
                            xmlModelExporter.setIncludeXSD(wizard.doIncludeXSD());
                            xmlModelExporter.setLanguageCode(wizard.getLanguageCode());
                            
                            xmlModelExporter.exportModel(model, file);
                            
                            // Validate file
                            if(wizard.doValidateAfterExport()) {
                                XMLValidator validator = new XMLValidator();
                                validator.validateXML(file);
                            }
                        }
                        catch(Throwable ex) {
                            ex.printStackTrace();
                            MessageDialog.openError(Display.getCurrent().getActiveShell(),
                                    Messages.XMLExchangeExportProvider_3,
                                    Messages.XMLExchangeExportProvider_4
                                    + " " //$NON-NLS-1$
                                    + ex.getMessage());
                        }
                    }
                });
            }
        }
    }
}
