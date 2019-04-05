/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.xmlexchange.commandline;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.osgi.util.NLS;
import org.opengroup.archimate.xmlexchange.XMLModelExporter;
import org.opengroup.archimate.xmlexchange.XMLValidator;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;

/**
 * Command Line interface for XML Export
 * 
 * Typical usage - (should be all on one line):
 * 
 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --loadModel "/pathToModel/model.archimate"
   --xmlexchange.export "/path/model.xml"
 * 
 * @author Phillip Beauvoir
 */
public class ExportXMLProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.ExportXMLProvider_0;
    
    static final String OPTION_EXPORT_XML = "xmlexchange.export"; //$NON-NLS-1$
    static final String OPTION_EXPORT_XML_FOLDERS = "xmlexchange.exportFolders"; //$NON-NLS-1$
    static final String OPTION_EXPORT_XML_LANGUAGE = "xmlexchange.exportLang"; //$NON-NLS-1$

    
    public ExportXMLProvider() {
    }
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(!hasCorrectOptions(commandLine)) {
            return;
        }
        
        IArchimateModel model = CommandLineState.getModel();
        
        if(model == null) {
            throw new IOException(Messages.ExportXMLProvider_1);
        }
        
        // File
        String value = commandLine.getOptionValue(OPTION_EXPORT_XML);
        if(!StringUtils.isSet(value)) {
            logError(Messages.ExportXMLProvider_2);
            return;
        }
        File outputFile = new File(value);
        
        XMLModelExporter exporter = new XMLModelExporter();

        // Folders
        exporter.setSaveOrganisation(commandLine.hasOption(OPTION_EXPORT_XML_FOLDERS));
        
        // Language
        exporter.setLanguageCode(commandLine.getOptionValue(OPTION_EXPORT_XML_LANGUAGE));
        
        logMessage(NLS.bind(Messages.ExportXMLProvider_3, model.getName(), outputFile.getPath()));

        exporter.exportModel(model, outputFile);
        
        logMessage(Messages.ExportXMLProvider_4);
        
        // Validate file
        logMessage(Messages.ExportXMLProvider_5);
        
        XMLValidator validator = new XMLValidator();
        validator.validateXML(outputFile);

        logMessage(Messages.ExportXMLProvider_6);
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
    
    @Override
    public Options getOptions() {
        Options options = new Options();
        
        Option option = Option.builder()
                .longOpt(OPTION_EXPORT_XML)
                .hasArg()
                .argName(Messages.ExportXMLProvider_7)
                .desc(Messages.ExportXMLProvider_8)
                .build();
        options.addOption(option);
        
        option = Option.builder()
                .longOpt(OPTION_EXPORT_XML_FOLDERS)
                .desc(Messages.ExportXMLProvider_9)
                .build();
        options.addOption(option);
        
        option = Option.builder()
                .longOpt(OPTION_EXPORT_XML_LANGUAGE)
                .hasArg()
                .argName(Messages.ExportXMLProvider_10)
                .desc(Messages.ExportXMLProvider_11)
                .build();
        options.addOption(option);

        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_EXPORT_XML);
    }
}
