/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.jasperreports.data.ArchimateModelDataSource;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
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


/**
 * Exporter for Jasper Reports
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsExporter {
    
    static class CancelledException extends IOException {
        public CancelledException(String message) {
            super(message);
        }
    }
    
    public static final int EXPORT_HTML = 1;
    public static final int EXPORT_PDF = 1 << 1;
    public static final int EXPORT_DOCX = 1 << 2;
    public static final int EXPORT_PPT = 1 << 3;
    public static final int EXPORT_RTF = 1 << 4;
    public static final int EXPORT_ODT = 1 << 5;
    
    private boolean DELETE_TEMP_FILES = true;
    
    private IArchimateModel fModel;
    private File fExportFolder;
    private String fExportFileName;
    private File fMainTemplateFile;
    private String fReportTitle;
    private int fExportOptions;
    
    private Locale fLocale;
    
    private IProgressMonitor progressMonitor;

    /**
     * Export model to one or more Jasper Reports
     * @param model             The ArchiMate model
     * @param exportFolder      The folder to export to
     * @param exportFileName    The file name to use for all reports
     * @param mainTemplateFile  The Jasper main.jrxml template file
     * @param reportTitle       The title of the report
     * @param locale            The target Locale, or null for default locale
     * @param exportOptions     Export options. XOR of EXPORT_* options
     */
    public JasperReportsExporter(IArchimateModel model, File exportFolder, String exportFileName,
                                        File mainTemplateFile, String reportTitle, Locale locale, int exportOptions) {
        fModel = model;
        fExportFolder = exportFolder;
        fExportFileName = exportFileName;
        fMainTemplateFile = mainTemplateFile;
        fReportTitle = reportTitle;
        fExportOptions = exportOptions;
        fLocale = locale == null ? Locale.getDefault() : locale;
        
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
        progressMonitor = monitor;
        
        if(progressMonitor != null) {
            progressMonitor.beginTask(Messages.JasperReportsExporter_0, -1);
        }
        
        // Temp Folder to store assets
        File tmpFolder = new File(fExportFolder, "tmp"); //$NON-NLS-1$
        tmpFolder.mkdirs();
        
        try {
            writeDiagrams(tmpFolder);
            
            JasperPrint jasperPrint = createJasperPrint(tmpFolder);
            
            if((fExportOptions & EXPORT_HTML) != 0) {
                setProgressSubTask(Messages.JasperReportsExporter_3);
                exportHTML(jasperPrint, new File(fExportFolder, fExportFileName + ".html")); //$NON-NLS-1$
            }

            if((fExportOptions & EXPORT_PDF) != 0) {
                setProgressSubTask(Messages.JasperReportsExporter_4);
                exportPDF(jasperPrint, new File(fExportFolder, fExportFileName + ".pdf")); //$NON-NLS-1$
            }

            if((fExportOptions & EXPORT_DOCX) != 0) {
                setProgressSubTask(Messages.JasperReportsExporter_5);
                exportDOCX(jasperPrint, new File(fExportFolder, fExportFileName + ".docx")); //$NON-NLS-1$
            }
            
            if((fExportOptions & EXPORT_PPT) != 0) {
                setProgressSubTask(Messages.JasperReportsExporter_6);
                exportPPT(jasperPrint, new File(fExportFolder, fExportFileName + ".pptx")); //$NON-NLS-1$
            }
            
            if((fExportOptions & EXPORT_RTF) != 0) {
                setProgressSubTask(Messages.JasperReportsExporter_7);
                exportRTF(jasperPrint, new File(fExportFolder, fExportFileName + ".rtf")); //$NON-NLS-1$
            }
            
            if((fExportOptions & EXPORT_ODT) != 0) {
                setProgressSubTask(Messages.JasperReportsExporter_8);
                exportODT(jasperPrint, new File(fExportFolder, fExportFileName + ".odt")); //$NON-NLS-1$
            }
        }
        finally {
            if(DELETE_TEMP_FILES) {
                setProgressSubTask(Messages.JasperReportsExporter_9);
                FileUtils.deleteFolder(tmpFolder);
            }
        }
    }
    
    /**
     * Write the diagrams to temp files
     */
    void writeDiagrams(File tmpFolder) throws IOException {
        List<IDiagramModel> diagramModels = fModel.getDiagramModels();
        int total = diagramModels.size();
        int i = 1;

        for(IDiagramModel dm : diagramModels) {
            setProgressSubTask(NLS.bind(Messages.JasperReportsExporter_1, i++, total));
            
            Image image = DiagramUtils.createImage(dm, 1, 10);
            String diagramName = dm.getId() + ".png"; //$NON-NLS-1$
            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData(ImageFactory.getImageDeviceZoom()) };
                File file = new File(tmpFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
        }
    }
    
    JasperPrint createJasperPrint(File tmpFolder) throws JRException, IOException {
        setProgressSubTask(Messages.JasperReportsExporter_2);
        
        // Set the location of the default Jasper Properties File
        File propsFile = new File(JasperReportsPlugin.INSTANCE.getPluginFolder(), "jasperreports.properties"); //$NON-NLS-1$
        System.setProperty(DefaultJasperReportsContext.PROPERTIES_FILE, propsFile.getAbsolutePath());

        // Set the location of the Images
        System.setProperty("JASPER_IMAGE_PATH", tmpFolder.getPath()); //$NON-NLS-1$
        
        // Declare Parameters passed to JasperFillManager
        Map<String, Object> params = new HashMap<String, Object>();

        // Parameters referenced in Report
        params.put("REPORT_TITLE", fReportTitle); //$NON-NLS-1$
        
        // Report folder
        File reportFolder = fMainTemplateFile.getParentFile();
        
        // Load local strings properties as a resource bundle - location is report folder. If not present, ignore
        File bundleFile = new File(reportFolder, "strings.properties"); //$NON-NLS-1$
        if(bundleFile.canRead()) {
            ClassLoader loader = new URLClassLoader(new URL[] {reportFolder.toURI().toURL()});
            ResourceBundle resourceBundle = ResourceBundle.getBundle("strings", fLocale, loader); //$NON-NLS-1$
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
        }
        
        // Set locale
        params.put(JRParameter.REPORT_LOCALE, fLocale);
        
        // Path to report
        params.put("REPORT_PATH", reportFolder.toString() + File.separator); //$NON-NLS-1$
        
        // Path to Model
        if(fModel.getFile() != null) {
            // Model File path
            params.put("MODEL_FILE", fModel.getFile().getAbsolutePath()); //$NON-NLS-1$
            // Model Directory
            params.put("MODEL_DIRECTORY", fModel.getFile().getParent() + File.separator); //$NON-NLS-1$
        }

        // Compile Main Report
        setProgressSubTask(Messages.JasperReportsExporter_10);
        
        JasperReport mainReport = JasperCompileManager.compileReport(fMainTemplateFile.getPath());
        
        // Compile sub-reports
        for(File file : reportFolder.listFiles()) {
            if(!file.equals(fMainTemplateFile) && file.getName().endsWith(".jrxml")) { //$NON-NLS-1$
                //System.out.println("Compiling Sub-Report: " + file);
                JasperReport jr = JasperCompileManager.compileReport(file.getPath());
                params.put(jr.getName(), jr);
            }
        }
        
        // Fill Report
        setProgressSubTask(Messages.JasperReportsExporter_11);
        
        return JasperFillManager.fillReport(mainReport, params, new ArchimateModelDataSource(fModel));
    }
    
    void exportHTML(JasperPrint jasperPrint, File file) throws JRException {
        JasperExportManager.exportReportToHtmlFile(jasperPrint, file.getPath());
    }

    void exportPDF(JasperPrint jasperPrint, File file) throws JRException {
        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath());
    }
    
    void exportDOCX(JasperPrint jasperPrint, File file) throws JRException {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }

    void exportPPT(JasperPrint jasperPrint, File file) throws JRException {
        JRPptxExporter exporter = new JRPptxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }

    void exportODT(JasperPrint jasperPrint, File file) throws JRException {
        JROdtExporter exporter = new JROdtExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }
    
    void exportRTF(JasperPrint jasperPrint, File file) throws JRException {
        JRRtfExporter exporter = new JRRtfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
        exporter.exportReport();
    }

    private void setProgressSubTask(String task) throws IOException {
        if(progressMonitor != null) {
            progressMonitor.subTask(task);
            updateProgress();
        }
    }
    
    private void updateProgress() throws IOException {
        if(progressMonitor != null && PlatformUI.isWorkbenchRunning() && Display.getCurrent() != null) {
            while(Display.getCurrent().readAndDispatch());
            
            if(progressMonitor.isCanceled()) {
                throw new CancelledException(Messages.JasperReportsExporter_12);
            }
        }
    }
}
