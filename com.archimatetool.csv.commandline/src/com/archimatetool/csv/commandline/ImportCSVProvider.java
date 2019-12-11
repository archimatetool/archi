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
import com.archimatetool.csv.importer.CSVImporter;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;

/**
 * Command Line interface for CSV Import
 * 
 * Typical usage - (should be all on one line):
 * 
 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --createEmptyModel
   --csv.import "elements.csv"
 * 
 * @author Phillip Beauvoir
 */
public class ImportCSVProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.ImportCSVProvider_0;
    
    static final String OPTION_IMPORT_CSV = "csv.import"; //$NON-NLS-1$
    
    public ImportCSVProvider() {
    }
    
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(!hasCorrectOptions(commandLine)) {
            return;
        }
        
        // Get the current model that should be loaded
        IArchimateModel model = CommandLineState.getModel();
        
        if(model == null) {
            throw new IOException(Messages.ImportCSVProvider_1);
        }
        
        // CSV File
        String value = commandLine.getOptionValue(OPTION_IMPORT_CSV);
        if(!StringUtils.isSet(value)) {
            logError(Messages.ImportCSVProvider_2);
            return;
        }
        File csvFile = new File(value);
        if(!csvFile.exists()) {
            logError(NLS.bind(Messages.ImportCSVProvider_3, value));
            return;
        }
        
        logMessage(NLS.bind(Messages.ImportCSVProvider_4, csvFile.getPath(), model.getName()));
        
        CSVImporter importer = new CSVImporter(model);
        importer.doImport(csvFile);

        logMessage(Messages.ImportCSVProvider_5);
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
    
    @Override
    public int getPriority() {
        return PRIORITY_IMPORT;
    }
    
    @Override
    public Options getOptions() {
        Options options = new Options();
        
        Option option = Option.builder()
                .longOpt(OPTION_IMPORT_CSV)
                .hasArg().argName(Messages.ImportCSVProvider_6)
                .desc(Messages.ImportCSVProvider_7)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_IMPORT_CSV);
    }

}
