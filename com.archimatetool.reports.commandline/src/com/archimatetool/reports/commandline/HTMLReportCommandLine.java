/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.commandline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.archimatetool.commandline.CommandLineUtils;
import com.archimatetool.commandline.ICommandLineProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.reports.html.HTMLReportExporter;

/**
 * Command Line interface for HTML Reports
 * 
 * Usage:
 * 
 * Archi -consoleLog -console -nosplash -application com.archimatetool.commandline.app -HTMLReport_InputFile "/pathToModel/model.archimate" -HTMLReport_OutputFolder "/pathToOutputFolder"
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class HTMLReportCommandLine implements ICommandLineProvider {

    public static final String HTMLREPORT = "[HTMLReport]"; //$NON-NLS-1$
    
    public static final String ARGS_HTMLREPORT_PREFIX = "-HTMLReport_"; //$NON-NLS-1$
    public static final String ARGS_HTMLREPORT_INPUTFILE = ARGS_HTMLREPORT_PREFIX + "InputFile"; //$NON-NLS-1$
    public static final String ARGS_HTMLREPORT_OUTPUTFOLDER = ARGS_HTMLREPORT_PREFIX + "OutputFolder"; //$NON-NLS-1$

    public HTMLReportCommandLine() {
    }
    
    @Override
    public void run(String[] args) throws Exception {
        Map<String, String> argsMap = parseArgs(args);
        
        if(!hasCorrectArgs(argsMap)) {
            return;
        }
        
        File fileInput = null;
        File folderOutput = null;

        String sInput = argsMap.get(ARGS_HTMLREPORT_INPUTFILE);
        if(!StringUtils.isSet(sInput)) {
            logError("No file input.");
            return;
        }
        else {
            fileInput = new File(sInput);
            if(!fileInput.exists()) {
                logError(sInput + " is not a file or does not exist.");
                return;
            }
        }
        
        String sOutput = argsMap.get(ARGS_HTMLREPORT_OUTPUTFOLDER);
        if(!StringUtils.isSet(sOutput)) {
            logError("No output folder.");
            return;
        }
        else {
            folderOutput = new File(sOutput);
            folderOutput.mkdirs();
            if(!folderOutput.exists()) {
                logError(sOutput + " is not a folder or does not exist.");
                return;
            }
        }

        logMessage("Exporting file '" + sInput + "' to '" + sOutput + "'");

        IArchimateModel model = CommandLineUtils.loadModel(fileInput);
        HTMLReportExporter ex = new HTMLReportExporter(model);
        ex.createReport(folderOutput, "index.html"); //$NON-NLS-1$

        logMessage("Export done!");
    }
    
    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<String, String>();
        
        for(int i = 0; i < args.length; i++) {
            String s = args[i];
            switch(s) {
                case ARGS_HTMLREPORT_INPUTFILE:
                case ARGS_HTMLREPORT_OUTPUTFOLDER:
                    if(i < args.length - 1) {
                        argsMap.put(s, args[i + 1]);
                    }
                    break;

                default:
                    break;
            }
        }
        
        return argsMap;
    }
    
    private boolean hasCorrectArgs(Map<String, String> args) {
        return args.containsKey(ARGS_HTMLREPORT_INPUTFILE) && args.containsKey(ARGS_HTMLREPORT_OUTPUTFOLDER);
    }
    
    private void logMessage(String message) {
        System.out.println(HTMLREPORT + " " + message); //$NON-NLS-1$
    }
    
    private void logError(String message) {
        System.err.println(HTMLREPORT + " " + message); //$NON-NLS-1$
    }
}
