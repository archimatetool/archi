/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.jasperreports.data.ArchimateModelDataSource;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;


/**
 * Exporter for Jasper Reports
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsExporter {
    
    /*
     * Export options
     */
    public static int EXPORT_HTML = 1;
    public static int EXPORT_PDF = 1 << 1;
    public static int EXPORT_DOCX = 1 << 2;
    public static int EXPORT_PPT = 1 << 3;
    public static int EXPORT_RTF = 1 << 4;
    public static int EXPORT_ODT = 1 << 5;
    
    private boolean DELETE_TEMP_FILES = true;
    
    private IArchimateModel fModel;
    private File fExportFolder;
    private String fExportFileName;
    private File fMainTemplateFile;
    private String fReportTitle;
    private int fExportOptions;

    /**
     * Export model to one or more Jasper Reports
     * @param model             The ArchiMate model
     * @param exportFolder      The folder to export to
     * @param exportFileName    The file name to use for all reports
     * @param mainTemplateFile  The Jasper main.jrxml template file
     * @param reportTitle       The title of the report
     * @param exportOptions     Export options. XOR of EXPORT_* options
     */
    public JasperReportsExporter(IArchimateModel model, File exportFolder, String exportFileName,
                                        File mainTemplateFile, String reportTitle, int exportOptions) {
        fModel = model;
        fExportFolder = exportFolder;
        fExportFileName = exportFileName;
        fMainTemplateFile = mainTemplateFile;
        fReportTitle = reportTitle;
        fExportOptions = exportOptions;
        
        // Stop logging
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");  //$NON-NLS-1$//$NON-NLS-2$
    }
    
    /** 
     * Export the model to Jasper Reports
     * @param monitor       Progress Monitor. Can be null
     * @throws IOException
     * @throws JRException
     */
    public void export(IProgressMonitor monitor) throws IOException, JRException {
        if(monitor != null) {
            monitor.beginTask(Messages.JasperReportsExporter_0, 11);
        }
        
        // Temp Folder to store assets
        File tmpFolder = new File(fExportFolder, "tmp"); //$NON-NLS-1$
        tmpFolder.mkdirs();
        
        //System.out.println("Exporting: " + fModel.getName());

        if(monitor != null) {
            monitor.subTask(Messages.JasperReportsExporter_1);
        }
        writeDiagrams(tmpFolder);
        if(monitor != null) {
            monitor.worked(1);
        }
        
        if(monitor != null) {
            monitor.subTask(Messages.JasperReportsExporter_2);
        }
        JasperPrint jasperPrint = createJasperPrint(monitor, tmpFolder);
        if(monitor != null) {
            monitor.worked(1);
        }
        
        if((fExportOptions & EXPORT_HTML) != 0) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_3);
            }
            exportHTML(jasperPrint, new File(fExportFolder, fExportFileName + ".html")); //$NON-NLS-1$
        }
        if(monitor != null) {
            monitor.worked(1);
        }

        if((fExportOptions & EXPORT_PDF) != 0) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_4);
            }
            exportPDF(jasperPrint, new File(fExportFolder, fExportFileName + ".pdf")); //$NON-NLS-1$
        }
        if(monitor != null) {
            monitor.worked(1);
        }

        if((fExportOptions & EXPORT_DOCX) != 0) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_5);
            }
            exportDOCX(jasperPrint, new File(fExportFolder, fExportFileName + ".docx")); //$NON-NLS-1$
        }
        if(monitor != null) {
            monitor.worked(1);
        }
        
        if((fExportOptions & EXPORT_PPT) != 0) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_6);
            }
            exportPPT(jasperPrint, new File(fExportFolder, fExportFileName + ".pptx")); //$NON-NLS-1$
        }
        if(monitor != null) {
            monitor.worked(1);
        }
        
        if((fExportOptions & EXPORT_RTF) != 0) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_7);
            }
            exportRTF(jasperPrint, new File(fExportFolder, fExportFileName + ".rtf")); //$NON-NLS-1$
        }
        if(monitor != null) {
            monitor.worked(1);
        }
        
        if((fExportOptions & EXPORT_ODT) != 0) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_8);
            }
            exportODT(jasperPrint, new File(fExportFolder, fExportFileName + ".odt")); //$NON-NLS-1$
        }
        if(monitor != null) {
            monitor.worked(1);
        }
        
        if(DELETE_TEMP_FILES) {
            if(monitor != null) {
                monitor.subTask(Messages.JasperReportsExporter_9);
            }
            FileUtils.deleteFolder(tmpFolder);
        }
        if(monitor != null) {
            monitor.worked(1);
        }
        
        //System.out.println("Finished");
    }
    
    /**
     * Write the diagrams to temp files
     */
    void writeDiagrams(File tmpFolder) {
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
    
    JasperPrint createJasperPrint(IProgressMonitor monitor, File tmpFolder) throws JRException {
        // Set the location of the default Jasper Properties File
        File propsFile = new File(JasperReportsPlugin.INSTANCE.getPluginFolder(), "jasperreports.properties"); //$NON-NLS-1$
        System.setProperty(DefaultJasperReportsContext.PROPERTIES_FILE, propsFile.getAbsolutePath());

        // Set the location of the Images
        System.setProperty("JASPER_IMAGE_PATH", tmpFolder.getPath()); //$NON-NLS-1$
        
        // Declare Parameters passed to JasperFillManager
        Map<String, Object> params = new HashMap<String, Object>();

        // Parameters referenced in Report
        params.put("REPORT_TITLE", fReportTitle); //$NON-NLS-1$
        //params.put(JRParameter.REPORT_LOCALE, Locale.US);
        
        // Path to main.jrxml
        params.put("REPORT_PATH", fMainTemplateFile.getParent() + File.separator); //$NON-NLS-1$
        
        if(monitor != null) {
            monitor.worked(1);
        }
        
        // Compile Main Report
        //System.out.println("Compiling Main Report");
        if(monitor != null) {
            monitor.subTask(Messages.JasperReportsExporter_10);
        }
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
        
        if(monitor != null) {
            monitor.worked(1);
        }
        
        // Fill Report
        //System.out.println("Filling Report");
        if(monitor != null) {
            monitor.subTask(Messages.JasperReportsExporter_11);
        }
        
        return JasperFillManager.fillReport(mainReport, params, new ArchimateModelDataSource(fModel));
    }
    
    void exportHTML(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to HTML: " + file);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, file.getPath());
    }

    void exportPDF(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to PDF: " + file);
        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath());
    }
    
    void exportDOCX(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to DOCX: " + file);
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }

    void exportPPT(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to MS PPT: " + file);
        JRPptxExporter exporter = new JRPptxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }

    void exportODT(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to ODT: " + file);
        JROdtExporter exporter = new JROdtExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }
    
    void exportRTF(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to RTF: " + file);
        JRRtfExporter exporter = new JRRtfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
        exporter.exportReport();
    }

}
