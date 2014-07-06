/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

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
        
        final File mainTemplateFile = fPage2.getMainTemplateFile();
        
        // Check this exists
        if(mainTemplateFile == null || !mainTemplateFile.exists()) {
            MessageDialog.openError(getShell(), Messages.ExportJasperReportsWizard_1, Messages.ExportJasperReportsWizard_2);
            return false;
        }
        
        final File exportFolder = fPage1.getExportFolder();
        final String exportFileName = fPage1.getExportFilename();
        final String reportTitle = fPage1.getReportTitle();
        final int exportOptions = fPage1.getExportOptions();
        
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
        
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
                try {
                    dialog.run(false, true, new IRunnableWithProgress() {
                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            try {
                                JasperReportsExporter exporter = new JasperReportsExporter(fModel, exportFolder, exportFileName, mainTemplateFile,
                                        reportTitle, exportOptions);
                                exporter.export(monitor);
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                                MessageDialog.openError(getShell(), Messages.ExportJasperReportsWizard_5, ex.getMessage());
                            }
                            finally {
                                monitor.done();
                            }
                        }
                    });
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        return true;
    }

}
