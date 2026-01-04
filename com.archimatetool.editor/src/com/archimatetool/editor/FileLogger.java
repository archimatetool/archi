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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.ui.PlatformUI;

/**
 * File Logging Support
 * 
 * Uses java.util.logging to log to a localised file for a given plug-in.
 * 
 * <p>Example Usage:<p>
   <pre>
   FileLogger.create("com.archimatetool.myplugin",
                      getBundle().getEntry("logging.properties"),
                      new File(getLoggingFolder(), "log-%g.txt"));
   </pre>
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FileLogger {

    private static final int DEFAULT_MAX_BYTES = 1024 * 1024 * 10; // 10 mb files
    private static final int DEFAULT_MAX_FILE_COUNT = 3; // Max files to use
    
    /**
     * Registered loggers
     */
    private static Map<String, FileLogger> loggers = new HashMap<>();
    
    /**
     * Create and start Java Logging to file using default file size of 10 mb and 3 log files
     * 
     * @param rootPackageName root package name that will be used for the root logger
     * @param propertiesFile bundle location of logging.properties file (can be null)
     * @param fileNamePattern absolute file name pattern of the logging file to write to
     */
    public static FileLogger create(String rootPackageName, URL propertiesFile, File fileNamePattern) throws IOException {
        return create(rootPackageName, propertiesFile, fileNamePattern, DEFAULT_MAX_BYTES, DEFAULT_MAX_FILE_COUNT);
    }
    
    /**
     * Create and start Java Logging to file
     * 
     * @param rootPackageName root package name that will be used for the root logger
     * @param propertiesFile bundle location of logging.properties file (can be null)
     * @param fileNamePattern absolute file name pattern of the logging file to write to
     * @param limit the maximum number of bytes to write to any one file
     * @param count the number of files to use
     */
    public static FileLogger create(String rootPackageName, URL propertiesFile, File fileNamePattern, int limit, int count) throws IOException {
        FileLogger logger = loggers.get(rootPackageName);
        
        if(logger == null) {
            logger = new FileLogger(rootPackageName, propertiesFile, fileNamePattern, limit, count);
            loggers.put(rootPackageName, logger);
        }
        
        return logger;
    }
    
    /**
     * Return a previsously created FileLogger, or null if it hasn't been created yet or has been closed
     */
    public static FileLogger get(String rootPackageName) {
        return loggers.get(rootPackageName);
    }
    
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
                try(PrintWriter pw = new PrintWriter(sw)) {
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                }
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
    private Logger rootLogger;
    private FileHandler fileHandler;
    
    private FileLogger() {
    }

    private FileLogger(String rootPackageName, URL propertiesFile, File fileNamePattern, int limit, int count) throws IOException {
        this.rootPackageName = rootPackageName;
        
        // The root logger is the root class package name of the plug-in
        rootLogger = Logger.getLogger(rootPackageName);
        
        // Don't use parent handlers so there's no logging to console
        rootLogger.setUseParentHandlers(false);
        
        // Read logging properties file, if any
        if(propertiesFile != null) {
            try(InputStream is = propertiesFile.openStream()) {
                LogManager.getLogManager().updateConfiguration(is, null);
            }
        }

        // Don't use file logging if running headless (unit tests)
        if(!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        
        // Parent folder must exist for logging file and lock file
        if(fileNamePattern.getParentFile() != null) {
            fileNamePattern.getParentFile().mkdirs();
        }
        
        // Set file handler and format on the root logger
        fileHandler = new FileHandler(fileNamePattern.getAbsolutePath(), limit, count, true);
        String format = LogManager.getLogManager().getProperty(rootPackageName + ".format");
        fileHandler.setFormatter(new ExtSimpleFormatter(format));
        fileHandler.setEncoding("UTF-8"); // Important!
        rootLogger.addHandler(fileHandler);
    }
    
    /**
     * Set the root logger to level
     */
    public void setLevel(Level newLevel) {
        if(rootLogger != null) {
            rootLogger.setLevel(newLevel);
        }
    }
    
    /**
     * Close the logger and stop logging. The logger will be unregistered and calls to get(packagename) will return null.
     */
    public void close() {
        if(fileHandler != null) {
            fileHandler.close();
        }

        if(rootLogger != null) {
            rootLogger.removeHandler(fileHandler);
        }
        
        fileHandler = null;
        rootLogger = null;
        loggers.remove(rootPackageName);
    }
}
