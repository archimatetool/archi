/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.export;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.csv.CSVConstants;






/**
 * Export As CSV Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsCSVWizard extends Wizard implements CSVConstants {

    private ExportAsCSVPage fPage;

    private CSVExporter fExporter;

    public ExportAsCSVWizard(CSVExporter exporter) {
        fExporter = exporter;
        setWindowTitle(Messages.ExportAsCSVWizard_0);
    }

    @Override
    public void addPages() {
        fPage = new ExportAsCSVPage();
        addPage(fPage);
    }

    @Override
    public boolean performFinish() {
        String folderPath = fPage.getExportFolderPath();
        
        if(folderPath == null) {
            return false;
        }
        
        // Set file prefix
        fExporter.setFilePrefix(fPage.getFilenamePrefix());
        
        // Set delimiter
        fExporter.setDelimiter(DELIMITERS[fPage.getDelimiterIndex()]);
        
        // Strip newlines
        fExporter.setStripNewLines(fPage.getStripNewlines());
        
        // Excel compatible
        fExporter.setExcelCompatible(fPage.getExcelCompatible());
        
        // Encoding
        fExporter.setEncoding(fPage.getEncoding());
        
        File folder = new File(folderPath);
        
        // Make folder
        folder.mkdirs();
        
        // Make sure the elements file does not already exist
        File elementsFile = new File(folder, fExporter.createElementsFileName());
        if(elementsFile.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.ExportAsCSVWizard_1,
                    Messages.ExportAsCSVWizard_2);
            if(!result) {
                return false;
            }
        }
        
        // Store Preferences
        fPage.storePreferences();
        
        // Export
        try {
            fExporter.export(folder);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            MessageDialog.openError(Display.getCurrent().getActiveShell(),
                    Messages.ExportAsCSVWizard_3,
                    Messages.ExportAsCSVWizard_4 + " " + ex.getMessage()); //$NON-NLS-1$
            return false;
        }

        return true;
    }

}
