/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.opengroup.archimate.xmlexchange.XMLModelExporter;
import org.opengroup.archimate.xmlexchange.XMLValidator;

import com.archimatetool.model.IArchimateModel;



/**
 * Export to XML Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportToXMLWizard extends Wizard {
    
    private IArchimateModel fModel;
    
    private ExportToXMLPage fPage;
    private ExportToXMLPageMetadata fPageMetadata;
    
    public ExportToXMLWizard(IArchimateModel model) {
        fModel = model;
        setWindowTitle(Messages.ExportToXMLWizard_0);
    }
    
    @Override
    public void addPages() {
        fPage = new ExportToXMLPage(fModel);
        addPage(fPage);
        
        fPageMetadata = new ExportToXMLPageMetadata();
        addPage(fPageMetadata);
    }
    
    @Override
    public boolean performFinish() {
        final File file = new File(fPage.getFileName());
        
        // Check valid file name
        try {
            file.getCanonicalPath();
        }
        catch(IOException ex) {
            MessageDialog.openError(getShell(), Messages.ExportToXMLWizard_1, Messages.ExportToXMLWizard_2);
            return false;
        }
        
        // Make sure the file does not already exist
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.ExportToXMLWizard_3,
                    "'" + file + "' already exists. Are you sure you want to overwrite it?"); //$NON-NLS-1$ //$NON-NLS-2$
            if(!result) {
                return false;
            }
        }

        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                try {
                    XMLModelExporter xmlModelExporter = new XMLModelExporter();
                    
                    xmlModelExporter.setMetadata(fPageMetadata.getMetadata());
                    xmlModelExporter.setSaveOrganisation(fPage.doSaveOrganisation());
                    xmlModelExporter.setIncludeXSD(fPage.doIncludeXSD());
                    xmlModelExporter.setLanguageCode(fPage.getLanguageCode());
                    
                    xmlModelExporter.exportModel(fModel, file);

                    fPage.storePreferences();
                    fPageMetadata.storePreferences();
                    
                    // Validate file
                    XMLValidator validator = new XMLValidator();
                    validator.validateXML(file);
                }
                catch(Throwable ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.ExportToXMLWizard_3,
                            Messages.ExportToXMLWizard_4
                            + " " //$NON-NLS-1$
                            + ex.getMessage());
                }
            }
        });
        
        return true;
    }

}
