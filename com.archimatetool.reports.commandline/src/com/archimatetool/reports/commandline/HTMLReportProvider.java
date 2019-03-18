/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.commandline;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.reports.html.HTMLReportExporter;

/**
 * Command Line interface for HTML Reports
 * 
 * Typical usage - (should be all on one line):
 * 
 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --loadModel "/pathToModel/model.archimate"
   --html.createReport "/pathToOutputFolder"
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.HTMLReportProvider_0;
    
    static final String OPTION_CREATE_HTML_REPORT = "html.createReport"; //$NON-NLS-1$
    
    public HTMLReportProvider() {
    }
    
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(!hasCorrectOptions(commandLine)) {
            return;
        }
        
        String sOutput = commandLine.getOptionValue(OPTION_CREATE_HTML_REPORT);
        if(!StringUtils.isSet(sOutput)) {
            logError(Messages.HTMLReportProvider_1);
            return;
        }

        File folderOutput = new File(sOutput);
        folderOutput.mkdirs();
        if(!folderOutput.exists()) {
            logError(NLS.bind(Messages.HTMLReportProvider_2, sOutput));
            return;
        }

        IArchimateModel model = CommandLineState.getModel();
        
        if(model == null) {
            throw new IOException(Messages.HTMLReportProvider_3);
        }
        
        logMessage(NLS.bind(Messages.HTMLReportProvider_4, model.getName(), sOutput));

        HTMLReportExporter ex = new HTMLReportExporter(model);
        ex.createReport(folderOutput, "index.html", new NullProgressMonitor() { //$NON-NLS-1$
            @Override
            public void subTask(String name) {
                logMessage(name);
            }
        });

        logMessage(Messages.HTMLReportProvider_5);
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
    
    @Override
    public Options getOptions() {
        Options options = new Options();
        
        Option option = Option.builder()
                .longOpt(OPTION_CREATE_HTML_REPORT)
                .hasArg().argName(Messages.HTMLReportProvider_6)
                .desc(Messages.HTMLReportProvider_7)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_CREATE_HTML_REPORT);
    }
}
