/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.commandline;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.jasperreports.JasperReportsExporter;
import com.archimatetool.jasperreports.JasperReportsPlugin;
import com.archimatetool.model.IArchimateModel;

/**
 * Command Line interface for Jasper Reports
 * 
 * Typical usage - (should be all on one line):
 * 
 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --loadModel "/pathToModel/model.archimate"
   --jasper.createReport "pathToFolder"
   --jasper.filename "filename"
   --jasper.title "title"
   --jasper.format "html,rtf"
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.JasperReportsProvider_0;
    
    static final String OPTION_JASPER_CREATE_REPORT = "jasper.createReport"; //$NON-NLS-1$
    static final String OPTION_JASPER_FILENAME = "jasper.filename"; //$NON-NLS-1$
    static final String OPTION_JASPER_TITLE = "jasper.title"; //$NON-NLS-1$
    static final String OPTION_JASPER_TEMPLATE = "jasper.template"; //$NON-NLS-1$
    static final String OPTION_JASPER_LOCALE = "jasper.locale"; //$NON-NLS-1$
    static final String OPTION_JASPER_FORMAT = "jasper.format"; //$NON-NLS-1$

    
    public JasperReportsProvider() {
    }
    
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(!hasCorrectOptions(commandLine)) {
            return;
        }
        
        IArchimateModel model = CommandLineState.getModel();
        
        if(model == null) {
            throw new IOException(Messages.JasperReportsProvider_1);
        }
        
        // Folder
        String path = commandLine.getOptionValue(OPTION_JASPER_CREATE_REPORT);
        if(!StringUtils.isSet(path)) {
            logError(Messages.JasperReportsProvider_2);
            return;
        }

        File folderOutput = new File(path);
        folderOutput.mkdirs();
        if(!folderOutput.exists()) {
            logError(NLS.bind(Messages.JasperReportsProvider_3, folderOutput.getPath()));
            return;
        }
        
        // Filename
        String filename = commandLine.getOptionValue(OPTION_JASPER_FILENAME);
        if(!StringUtils.isSet(filename)) {
            logError(Messages.JasperReportsProvider_4);
            return;
        }
        
        // Template file
        File templateFile = new File(JasperReportsPlugin.INSTANCE.getJasperReportsFolder(), Messages.JasperReportsProvider_20 + "/main.jrxml"); //$NON-NLS-1$
        String template = commandLine.getOptionValue(OPTION_JASPER_TEMPLATE);
        if(StringUtils.isSet(template)) {
            templateFile = new File(template);
            if(!templateFile.exists()) {
                logError(NLS.bind(Messages.JasperReportsProvider_5, templateFile.getPath()));
                return;
            }
        }
        
        // Title
        String title = commandLine.getOptionValue(OPTION_JASPER_TITLE);
        if(!StringUtils.isSet(title)) {
            logError(Messages.JasperReportsProvider_6);
            return;
        }
        
        // Locale
        Locale locale = null;
        String loc = commandLine.getOptionValue(OPTION_JASPER_LOCALE);
        if(StringUtils.isSet(loc)) {
            locale = Locale.forLanguageTag(loc.replace('_', '-'));
        }
        
        // Export options
        int exportOptions = 0;
        
        String format = commandLine.getOptionValue(OPTION_JASPER_FORMAT);
        if(StringUtils.isSet(format)) {
            format = format.toLowerCase();
            
            exportOptions |= format.contains("pdf") ? JasperReportsExporter.EXPORT_PDF : 0; //$NON-NLS-1$
            exportOptions |= format.contains("html") ? JasperReportsExporter.EXPORT_HTML : 0; //$NON-NLS-1$
            exportOptions |= format.contains("rtf") ? JasperReportsExporter.EXPORT_RTF : 0; //$NON-NLS-1$
            exportOptions |= format.contains("ppt") ? JasperReportsExporter.EXPORT_PPT : 0; //$NON-NLS-1$
            exportOptions |= format.contains("odt") ? JasperReportsExporter.EXPORT_ODT : 0; //$NON-NLS-1$
            exportOptions |= format.contains("docx") ? JasperReportsExporter.EXPORT_DOCX : 0; //$NON-NLS-1$
        }
        
        if(exportOptions == 0) {
            exportOptions = JasperReportsExporter.EXPORT_PDF;
        }
        
        logMessage(NLS.bind(Messages.JasperReportsProvider_7, model.getName(), folderOutput.getPath()));
        
        JasperReportsExporter exporter = new JasperReportsExporter(model, folderOutput, filename, templateFile, title, locale, exportOptions);
        
        exporter.export(new NullProgressMonitor() {
            @Override
            public void subTask(String name) {
                logMessage(name);
            }
        });
        
        logMessage(Messages.JasperReportsProvider_8);
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
    
    @Override
    public Options getOptions() {
        Options options = new Options();
        
        Option option = Option.builder()
                .longOpt(OPTION_JASPER_CREATE_REPORT)
                .hasArg()
                .argName(Messages.JasperReportsProvider_9)
                .desc(Messages.JasperReportsProvider_10)
                .build();
        options.addOption(option);
        
        option = Option.builder()
                .longOpt(OPTION_JASPER_FILENAME)
                .hasArg()
                .argName(Messages.JasperReportsProvider_11)
                .desc(Messages.JasperReportsProvider_12)
                .build();
        options.addOption(option);
        
        option = Option.builder()
                .longOpt(OPTION_JASPER_TITLE)
                .hasArg()
                .argName(Messages.JasperReportsProvider_13)
                .desc(Messages.JasperReportsProvider_14)
                .build();
        options.addOption(option);

        option = Option.builder()
                .longOpt(OPTION_JASPER_TEMPLATE)
                .hasArg()
                .argName("main.jrxml") //$NON-NLS-1$
                .desc(Messages.JasperReportsProvider_15)
                .build();
        options.addOption(option);

        option = Option.builder()
                .longOpt(OPTION_JASPER_LOCALE)
                .hasArg()
                .argName(Messages.JasperReportsProvider_16)
                .desc(Messages.JasperReportsProvider_17)
                .build();
        options.addOption(option);

        option = Option.builder()
                .longOpt(OPTION_JASPER_FORMAT)
                .hasArg()
                .argName(Messages.JasperReportsProvider_18)
                .desc(Messages.JasperReportsProvider_19)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_JASPER_CREATE_REPORT);
    }

}
