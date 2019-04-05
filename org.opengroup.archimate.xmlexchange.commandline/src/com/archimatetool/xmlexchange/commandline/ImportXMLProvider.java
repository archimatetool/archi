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
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.osgi.util.NLS;
import org.opengroup.archimate.xmlexchange.XMLModelImporter;
import org.opengroup.archimate.xmlexchange.XMLValidator;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;

/**
 * Command Line interface for XML Import
 * 
 * Typical usage - (should be all on one line):
 * 
 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --xmlexchange.import "path/model.xml"
 * 
 * @author Phillip Beauvoir
 */
public class ImportXMLProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.ImportXMLProvider_0;
    
    static final String OPTION_IMPORT_XML = "xmlexchange.import"; //$NON-NLS-1$
    
    public ImportXMLProvider() {
    }
    
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(!hasCorrectOptions(commandLine)) {
            return;
        }
        
        // File
        String value = commandLine.getOptionValue(OPTION_IMPORT_XML);
        if(!StringUtils.isSet(value)) {
            logError(Messages.ImportXMLProvider_1);
            return;
        }
        File importFile = new File(value);
        if(!importFile.exists()) {
            logError(NLS.bind(Messages.ImportXMLProvider_2, value));
            return;
        }
        
        // Validate file
        logMessage(Messages.ImportXMLProvider_3);
        
        XMLValidator validator = new XMLValidator();
        validator.validateXML(importFile);

        logMessage(Messages.ImportXMLProvider_4);
        
        logMessage(NLS.bind(Messages.ImportXMLProvider_5, importFile.getPath()));
        
        XMLModelImporter importer = new XMLModelImporter();
        IArchimateModel model = importer.createArchiMateModel(importFile);

        if(model == null) {
            throw new IOException(Messages.ImportXMLProvider_6);
        }
        
        // Add an Archive Manager
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        
        // Add a Command Stack
        CommandStack cmdStack = new CommandStack();
        model.setAdapter(CommandStack.class, cmdStack);
        
        CommandLineState.setModel(model);

        logMessage(Messages.ImportXMLProvider_7);
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
                .longOpt(OPTION_IMPORT_XML)
                .hasArg().argName(Messages.ImportXMLProvider_8)
                .desc(Messages.ImportXMLProvider_9)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_IMPORT_XML);
    }
}
