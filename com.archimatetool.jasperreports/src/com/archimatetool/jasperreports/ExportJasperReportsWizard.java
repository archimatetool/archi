/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.util.JRProperties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.jasperreports.data.ArchimateModelDataSource;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;



/**
 * Export Model to Jasper Reports Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportJasperReportsWizard extends Wizard {
    
    private boolean DELETE_TEMP_FILES = true;
    
    private IArchimateModel fModel;
    
    private ExportJasperReportsWizardPage1 fPage1;
    private ExportJasperReportsWizardPage2 fPage2;
    
    private File fMainTemplateFile;
    private File fExportFolder;
    private String fExportFileName;
    private String fReportTitle;
    private boolean fIsPDF, fIsHTML, fIsDOCX, fIsPPT, fIsODT, fIsRTF;
    
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
        fPage1.saveSettings();
        
        fMainTemplateFile = fPage2.getMainTemplateFile();
        
        // Check this exists
        if(fMainTemplateFile == null || !fMainTemplateFile.exists()) {
            MessageDialog.openError(getShell(), Messages.ExportJasperReportsWizard_1, Messages.ExportJasperReportsWizard_2);
            return false;
        }
        
        fExportFolder = fPage1.getExportFolder();
        fExportFileName = fPage1.getExportFilename();
        fReportTitle = fPage1.getReportTitle();
        fIsPDF = fPage1.isExportPDF();
        fIsHTML = fPage1.isExportHTML();
        fIsDOCX = fPage1.isExportDOCX();
        fIsPPT = fPage1.isExportPPT();
        fIsODT = fPage1.isExportODT();
        fIsRTF = fPage1.isExportRTF();
        
        // Check valid dir and file name
        try {
            File file = new File(fExportFolder, fExportFileName);
            file.getCanonicalPath();
            fExportFolder.mkdirs();
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
                                export(monitor);
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

    private void export(IProgressMonitor monitor) throws Exception {
        monitor.beginTask(Messages.ExportJasperReportsWizard_6, 11);
        
        // Temp Folder to store assets
        File tmpFolder = new File(fExportFolder, "tmp"); //$NON-NLS-1$
        tmpFolder.mkdirs();
        
        //System.out.println("Exporting: " + fModel.getName());

        monitor.subTask(Messages.ExportJasperReportsWizard_7);
        writeDiagrams(tmpFolder);
        monitor.worked(1);
        
        monitor.subTask(Messages.ExportJasperReportsWizard_8);
        JasperPrint jasperPrint = createJasperPrint(monitor, tmpFolder);
        monitor.worked(1);
        
        if(fIsHTML) {
            monitor.subTask(Messages.ExportJasperReportsWizard_9);
            exportHTML(jasperPrint, new File(fExportFolder, fExportFileName + ".html")); //$NON-NLS-1$
        }
        monitor.worked(1);

        if(fIsPDF) {
            monitor.subTask(Messages.ExportJasperReportsWizard_10);
            exportPDF(jasperPrint, new File(fExportFolder, fExportFileName + ".pdf")); //$NON-NLS-1$
        }
        monitor.worked(1);

        if(fIsDOCX) {
            monitor.subTask(Messages.ExportJasperReportsWizard_11);
            exportDOCX(jasperPrint, new File(fExportFolder, fExportFileName + ".docx")); //$NON-NLS-1$
        }
        monitor.worked(1);
        
        if(fIsPPT) {
            monitor.subTask(Messages.ExportJasperReportsWizard_12);
            exportPPT(jasperPrint, new File(fExportFolder, fExportFileName + ".pptx")); //$NON-NLS-1$
        }
        monitor.worked(1);
        
        if(fIsRTF) {
            monitor.subTask(Messages.ExportJasperReportsWizard_13);
            exportRTF(jasperPrint, new File(fExportFolder, fExportFileName + ".rtf")); //$NON-NLS-1$
        }
        monitor.worked(1);
        
        if(fIsODT) {
            monitor.subTask(Messages.ExportJasperReportsWizard_14);
            exportODT(jasperPrint, new File(fExportFolder, fExportFileName + ".odt")); //$NON-NLS-1$
        }
        monitor.worked(1);
        
        if(DELETE_TEMP_FILES) {
            monitor.subTask(Messages.ExportJasperReportsWizard_15);
            FileUtils.deleteFolder(tmpFolder);
        }
        monitor.worked(1);
        
        //System.out.println("Finished");
    }
    
    /**
     * Write the diagrams to temp files
     */
    private void writeDiagrams(File tmpFolder) {
        for(IDiagramModel dm : fModel.getDiagramModels()) {
            Image image = DiagramUtils.createImage(dm, 1, 10);
            String diagramName = dm.getId() + ".png"; //$NON-NLS-1$
            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                File file = new File(tmpFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
        }
    }
    
    private JasperPrint createJasperPrint(IProgressMonitor monitor, File tmpFolder) throws JRException {
        // Set the location of the default Jasper Properties File
        File propsFile = new File(JasperReportsPlugin.INSTANCE.getPluginFolder(), "jasperreports.properties"); //$NON-NLS-1$
        System.setProperty(JRProperties.PROPERTIES_FILE, propsFile.getAbsolutePath());

        // Set the location of the Images
        System.setProperty("JASPER_IMAGE_PATH", tmpFolder.getPath()); //$NON-NLS-1$

        // Declare Parameters passed to JasperFillManager
        Map<String, Object> params = new HashMap<String, Object>();

        // Parameters referenced in Report
        params.put("REPORT_TITLE", fReportTitle); //$NON-NLS-1$
        //params.put(JRParameter.REPORT_LOCALE, Locale.US);
        
        // Path to main.jrxml
        params.put("REPORT_PATH", fMainTemplateFile.getParent() + File.separator); //$NON-NLS-1$
        
        monitor.worked(1);
        
        // Compile Main Report
        //System.out.println("Compiling Main Report");
        monitor.subTask(Messages.ExportJasperReportsWizard_16);
        JasperReport mainReport = JasperCompileManager.compileReport(fMainTemplateFile.getPath());
        
        // Compile sub-reports
        File folder = fMainTemplateFile.getParentFile();
        for(File file : folder.listFiles()) {
            if(!file.equals(fMainTemplateFile) && file.getName().endsWith(".jrxml")) { //$NON-NLS-1$
                //System.out.println("Compiling Sub-Report: " + file);
                JasperReport jr = JasperCompileManager.compileReport(file.getPath());
                params.put(jr.getName(), jr);
            }
        }
        
        monitor.worked(1);
        
        // Fill Report
        //System.out.println("Filling Report");
        monitor.subTask(Messages.ExportJasperReportsWizard_17);
        return JasperFillManager.fillReport(mainReport, params, new ArchimateModelDataSource(fModel));
    }
    
    private void exportHTML(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to HTML: " + file);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, file.getPath());
    }

    private void exportPDF(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to PDF: " + file);
        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath());
    }
    
    private void exportDOCX(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to DOCX: " + file);
        JRDocxExporter msWordexporter = new JRDocxExporter();
        msWordexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        msWordexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        msWordexporter.exportReport();
    }

    private void exportPPT(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to MS PPT: " + file);
        JRPptxExporter msPPTexporter = new JRPptxExporter();
        msPPTexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        msPPTexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        msPPTexporter.exportReport();
    }

    private void exportODT(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to ODT: " + file);
        JROdtExporter odtExporter = new JROdtExporter();
        odtExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        odtExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        odtExporter.exportReport();
    }
    
    private void exportRTF(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to RTF: " + file);
        JRRtfExporter rtfExporter = new JRRtfExporter();
        rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        rtfExporter.exportReport();
    }
}
