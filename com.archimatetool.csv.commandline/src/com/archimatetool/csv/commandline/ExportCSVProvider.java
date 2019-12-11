/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.commandline;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;

/**
 * Command Line interface for CSV Export
 * 
 * Typical usage - (should be all on one line):
 * 
 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --loadModel "/pathToModel/model.archimate"
   --csv.export "/pathToOutputFolder"
 * 
 * @author Phillip Beauvoir
 */
public class ExportCSVProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.ExportCSVProvider_0;
    
    static final String OPTION_EXPORT_CSV = "csv.export"; //$NON-NLS-1$
    static final String OPTION_DELIMITER = "csv.exportDelimiter"; //$NON-NLS-1$
    static final String OPTION_FILE_PREFIX = "csv.exportFilenamePrefix"; //$NON-NLS-1$
    static final String OPTION_STRIP_NEW_LINES = "csv.exportStripNewLines"; //$NON-NLS-1$
    static final String OPTION_LEADING_CHARS_HACK = "csv.exportLeadingZeros"; //$NON-NLS-1$
    static final String OPTION_ENCODING = "csv.exportEncoding"; //$NON-NLS-1$

    
    public ExportCSVProvider() {
    }
    
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(!hasCorrectOptions(commandLine)) {
            return;
        }
        
        IArchimateModel model = CommandLineState.getModel();
        
        if(model == null) {
            throw new IOException(Messages.ExportCSVProvider_1);
        }
        
        // Folder
        String value = commandLine.getOptionValue(OPTION_EXPORT_CSV);
        if(!StringUtils.isSet(value)) {
            logError(Messages.ExportCSVProvider_2);
            return;
        }

        File folderOutput = new File(value);
        folderOutput.mkdirs();
        if(!folderOutput.exists()) {
            logError(NLS.bind(Messages.ExportCSVProvider_3, value));
            return;
        }
        
        CSVExporter exporter = new CSVExporter(model);

        // Delimiter
        value = commandLine.getOptionValue(OPTION_DELIMITER);
        if(StringUtils.isSet(value)) {
            switch(value) {
                case "\\t": //$NON-NLS-1$
                    exporter.setDelimiter('\t');
                    break;

                case ";": //$NON-NLS-1$
                    exporter.setDelimiter(';');
                    break;

                default:
                    exporter.setDelimiter(',');
                    break;
            }
        }
        
        // File prefix
        value = commandLine.getOptionValue(OPTION_FILE_PREFIX);
        if(StringUtils.isSet(value)) {
            exporter.setFilePrefix(value);
        }
        
        // Encoding
        value = commandLine.getOptionValue(OPTION_ENCODING);
        if(StringUtils.isSet(value)) {
            exporter.setEncoding(value);
        }
        
        // Use leading
        exporter.setUseLeadingCharsHack(commandLine.hasOption(OPTION_LEADING_CHARS_HACK));

        // Strip newlines
        exporter.setStripNewLines(commandLine.hasOption(OPTION_STRIP_NEW_LINES));

        logMessage(NLS.bind(Messages.ExportCSVProvider_4, model.getName(), folderOutput.getPath()));
        exporter.export(folderOutput);
        logMessage(Messages.ExportCSVProvider_5);
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
    
    @Override
    public Options getOptions() {
        Options options = new Options();
        
        Option option = Option.builder()
                .longOpt(OPTION_EXPORT_CSV)
                .hasArg().argName(Messages.ExportCSVProvider_6)
                .desc(Messages.ExportCSVProvider_7)
                .build();
        options.addOption(option);
        
        option = Option.builder()
                .longOpt(OPTION_DELIMITER)
                .hasArg()
                .argName(Messages.ExportCSVProvider_8)
                .desc(Messages.ExportCSVProvider_9)
                .build();
        options.addOption(option);
        
        option = Option.builder()
                .longOpt(OPTION_ENCODING)
                .hasArg()
                .argName(Messages.ExportCSVProvider_10)
                .desc(Messages.ExportCSVProvider_11)
                .build();
        options.addOption(option);

        option = Option.builder()
                .longOpt(OPTION_FILE_PREFIX)
                .hasArg()
                .argName(Messages.ExportCSVProvider_12)
                .desc(Messages.ExportCSVProvider_13)
                .build();
        options.addOption(option);

        option = Option.builder()
                .longOpt(OPTION_LEADING_CHARS_HACK)
                .desc(Messages.ExportCSVProvider_14)
                .build();
        options.addOption(option);

        option = Option.builder()
                .longOpt(OPTION_STRIP_NEW_LINES)
                .desc(Messages.ExportCSVProvider_15)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_EXPORT_CSV);
    }
}
