/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.ui.PlatformUI;

/**
 * Logging Support
 * 
 * Uses java.util.logging to log to a localised file for a given plug-in.
 * 
 * <p>Example Usage:<p>
   <pre>
   LoggingSupport loggingSupport = new LoggingSupport("com.archimatetool.myplugin",
                                            getBundle().getEntry("logging.properties"),
                                            new File(getLoggingFolder(), "log-%g.txt"));
   loggingSupport.start();
   // Use the logger...
   loggingSupport.stop();
   </pre>
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class LoggingSupport {

    private static final int MAX_BYTES = 1024 * 1024 * 10; // 10 mb files
    private static final int FILE_COUNT = 3; // Max files to use
    
    /**
     * Use our own extension of SimpleFormatter because we don't want to set a global, singleton format
     * with property "java.util.logging.SimpleFormatter.format" as this will be global and will set it for other SimpleFormatter usages.
     */
    private static class ExtSimpleFormatter extends SimpleFormatter {
        
        // %1 time, %2 source, %3 logger name, %4 level, %5 message, %6 thrown
        private static final String DEFAULT_FORMAT = "[%1$tF %1$tH:%1$tM:%1$tS.%1$tL] %4$-7s [%2$s] %5$s%6$s%n";
        
        private String format;
        
        private ExtSimpleFormatter(String format) {
            this.format = format == null ? DEFAULT_FORMAT : format;
        }
        
        /**
         * This is a copy of {@link SimpleFormatter#format(LogRecord)} with our logger format
         */
        @Override
        public String format(LogRecord record) {
            ZonedDateTime zdt = ZonedDateTime.ofInstant(record.getInstant(), ZoneId.systemDefault());
            
            String source;
            
            if(record.getSourceClassName() != null) {
                source = record.getSourceClassName();
                if(record.getSourceMethodName() != null) {
                    source += " " + record.getSourceMethodName();
                }
            }
            else {
                source = record.getLoggerName();
            }
            
            String message = formatMessage(record);
            String throwable = "";
            
            if(record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println();
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            
            return String.format(format,
                                 zdt,
                                 source,
                                 record.getLoggerName(),
                                 record.getLevel().getLocalizedName(),
                                 message,
                                 throwable);
        }
    }
    
    private String rootPackageName;
    private URL propertiesFile;
    private File fileNamePattern;
    
    private Logger rootLogger;
    private FileHandler fileHandler;
    
    /**
     * @param rootPackageName root package name that will be used for the root logger
     * @param propertiesFile bundle location of logging.properties file (can be null)
     * @param fileNamePattern absolute file name pattern of the logging file
     */
    public LoggingSupport(String rootPackageName, URL propertiesFile, File fileNamePattern) {
        this.rootPackageName = rootPackageName;
        this.propertiesFile = propertiesFile;
        this.fileNamePattern = fileNamePattern;
        
        // The root logger is the root class package name of the bundle
        rootLogger = Logger.getLogger(rootPackageName);
        
        // Don't use parent handlers so there's no logging to console
        rootLogger.setUseParentHandlers(false);
    }
    
    /**
     * Get a Logger for its name
     * We use this proxy method rather than the direct call to ensure that the root logger is initialised
     */
    public Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
    
    /**
     * Initialise the file handler for logging to file
     */
    public void start() throws SecurityException, IOException {
        // Don't use file logging if running headless (unit tests)
        if(!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        
        // Read logging properties file, if any
        if(propertiesFile != null) {
            try(InputStream is = propertiesFile.openStream()) {
                LogManager.getLogManager().updateConfiguration(is, null);
            }
        }
        
        // Parent folder must exist for logging file and lock file
        fileNamePattern.getParentFile().mkdirs();
        
        // Set file handler on root logger
        fileHandler = new FileHandler(fileNamePattern.getAbsolutePath(), MAX_BYTES, FILE_COUNT, true); // 10mb
        String format = LogManager.getLogManager().getProperty(rootPackageName + ".format");
        fileHandler.setFormatter(new ExtSimpleFormatter(format));
        fileHandler.setEncoding("UTF-8"); // Important!
        rootLogger.addHandler(fileHandler);
    }
    
    /**
     * Close the file handler
     */
    public void stop() {
        if(fileHandler != null) {
            fileHandler.close();
            fileHandler = null;
        }
    }
}
