/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * Logger for Archi
 * Don't use this in third-party plug-ins outside of Archi and its core plug-ins
 * 
 * @author Phillip Beauvoir
 */
public final class Logger {

    private static boolean enabled = true;
    
    private static Bundle bundle = ArchiPlugin.getInstance().getBundle();
    
    /**
     * Set logging enabled
     * @since 5.8.0
     */
    public static void setEnabled(boolean enable) {
        enabled = enable;
    }
    
    /**
     * Log info
     * @param message the message to log
     * @since 5.8.0
     */
    public static void info(String message) {
        log(new Status(IStatus.INFO, bundle.getSymbolicName(), message));
    }

    /**
     * Log info
     * @param message the message to log
     * @param throwable an optional Throwable to associate with this log
     * @since 5.8.0
     */
    public static void info(String message, Throwable throwable) {
        log(new Status(IStatus.INFO, bundle.getSymbolicName(), message, throwable));
    }

    /**
     * Log warning
     * @param message Message to log
     * @since 5.8.0
     */
    public static void warning(String message) {
        log(new Status(IStatus.WARNING, bundle.getSymbolicName(), message));
    }

    /**
     * Log warning
     * @param message the message to log
     * @param throwable an optional Throwable to associate with this log
     * @since 5.8.0
     */
    public static void warning(String message, Throwable throwable) {
        log(new Status(IStatus.WARNING, bundle.getSymbolicName(), message, throwable));
    }

    /**
     * Log an error
     * @param message the message to log
     * @since 5.8.0
     */
    public static void error(String message) {
        log(new Status(IStatus.ERROR, bundle.getSymbolicName(), message));
    }

    /**
     * Log an error
     * @param message the message to log
     * @param throwable an optional Throwable to associate with this log
     * @since 5.8.0
     */
    public static void error(String message, Throwable throwable) {
        log(new Status(IStatus.ERROR, bundle.getSymbolicName(), message, throwable));
    }

    /**
     * Log the given status.
     * @param status the status to log
     * @since 5.8.0
     */
    public static void log(IStatus status) {
        if(enabled) {
            ILog.of(bundle).log(status);
        }
    }
    
   
    // ------------------------------------------------------------------ 
    // Deprecated methods
    // ------------------------------------------------------------------ 
    
    /**
     * @deprecated use {@link #error(String)}
     */
    @Deprecated
    public static void logError(String message) {
        error(message);
    }

    /**
     * @deprecated use {@link #error(String, Throwable)}
     */
    @Deprecated
    public static void logError(String message, Throwable throwable) {
        error(message, throwable);
    }
    
    /**
     * @deprecated use {@link #warning(String)}
     */
    @Deprecated
    public static void logWarning(String message) {
        warning(message);
    }

    /**
     * @deprecated use {@link #warning(String, Throwable)}
     */
    @Deprecated
    public static void logWarning(String message, Throwable throwable) {
        warning(message, throwable);
    }
}
