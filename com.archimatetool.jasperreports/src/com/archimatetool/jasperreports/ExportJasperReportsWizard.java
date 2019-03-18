/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.jasperreports.JasperReportsExporter.CancelledException;
import com.archimatetool.model.IArchimateModel;



/**
 * Export Model to Jasper Reports Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportJasperReportsWizard extends Wizard {
    
    private IArchimateModel fModel;
    
    private ExportJasperReportsWizardPage1 fPage1;
    private ExportJasperReportsWizardPage2 fPage2;
    
    private File mainTemplateFile;
    private Locale locale;
    private File exportFolder;
    private String exportFileName;
    private String reportTitle;
    private int exportOptions;

    public ExportJasperReportsWizard(IArchimateModel model) {
        setWindowTitle(Messages.ExportJasperReportsWizard_0);
        fModel = model;
    }
    
    @Override
    public void addPages() {
        fPage1 = new ExportJasperReportsWizardPage1(fModel);
        addPage(fPage1);
        fPage2 = new ExportJasperReportsWizardPage2();
        addPage(fPage2);
    }

    @Override
    public boolean performFinish() {
        fPage1.storePreferences();
        
        mainTemplateFile = fPage2.getMainTemplateFile();
        
        // Check this exists
        if(mainTemplateFile == null || !mainTemplateFile.exists()) {
            MessageDialog.openError(getShell(), Messages.ExportJasperReportsWizard_1, Messages.ExportJasperReportsWizard_2);
            return false;
        }
        
        locale = fPage2.getLocale();
        
        exportFolder = fPage1.getExportFolder();
        exportFileName = fPage1.getExportFilename();
        reportTitle = fPage1.getReportTitle();
        exportOptions = fPage1.getExportOptions();
        
        // Check valid dir and file name
        try {
            File exportFile = new File(exportFolder, exportFileName);
            exportFile.getCanonicalPath();
            exportFolder.mkdirs();
        }
        catch(Exception ex) {
            MessageDialog.openError(getShell(), Messages.ExportJasperReportsWizard_3, Messages.ExportJasperReportsWizard_4);
            return false;
        }
        
        return true;
    }

    // Since this can take a while, show the progress monitor
    public void runWithProgress() throws InvocationTargetException, InterruptedException {
        IRunnableWithProgress runnable = monitor -> {
            try {
                JasperReportsExporter exporter = new JasperReportsExporter(fModel, exportFolder, exportFileName, mainTemplateFile,
                        reportTitle, locale, exportOptions);
                exporter.export(monitor);
            }
            catch(Exception ex) {
                // Async this to close progress monitor
                Display.getCurrent().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        if(ex instanceof CancelledException) {
                            MessageDialog.openInformation(getShell(), Messages.ExportJasperReportsWizard_0, ex.getMessage());
                        }
                        else {
                            ex.printStackTrace();
                            MessageDialog.openError(getShell(), Messages.ExportJasperReportsWizard_5, ex.getMessage());
                        }
                    }
                });
            }
            finally {
                monitor.done();
            }
        };

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        dialog.run(false, true, runnable);
    }
}
