/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opengroup.archimate.xmlexchange.wizard.ExportToXMLWizard;

import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Export
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangeExportHandler extends AbstractHandler implements IXMLExchangeGlobals {
    
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
        ExportToXMLWizard wizard = new ExportToXMLWizard(model.getName());
        
        WizardDialog dialog = new ExtendedWizardDialog(window.getShell(),
                wizard,
                "ExportToXMLWizard") { //$NON-NLS-1$
            
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Save"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.XMLExchangeExportHandler_0);
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
                    MessageDialog.openError(window.getShell(),
                            Messages.XMLExchangeExportHandler_1, Messages.XMLExchangeExportHandler_2);
                }
                
                // Make sure the file does not already exist
                if(file.exists()) {
                    boolean result = MessageDialog.openQuestion(window.getShell(),
                            Messages.XMLExchangeExportHandler_3,
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
                            MessageDialog.openError(window.getShell(),
                                    Messages.XMLExchangeExportHandler_3,
                                    Messages.XMLExchangeExportHandler_4
                                    + " " //$NON-NLS-1$
                                    + ex.getMessage());
                        }
                    }
                });
            }
        }
    }
}
