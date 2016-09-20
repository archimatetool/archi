/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Logger utility class
 * 
 * @author Phillip Beauvoir
 */
public final class Logger {

    public static boolean enabled = true;
    
    /**
     * Convenience method to log an OK event
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logOK(String message, Throwable t) {
        log(IStatus.OK, message, t);
    }

    /**
     * Convenience method to log an OK event
     * 
     * @param message Message to print
     */
    public static void logOK(String message) {
        log(IStatus.OK, message, null);
    }

    /**
     * Convenience method to log an error
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logError(String message, Throwable t) {
        log(IStatus.ERROR, message, t);
    }

    /**
     * Convenience method to log an error
     * 
     * @param message Message to print
     */
    public static void logError(String message) {
        log(IStatus.ERROR, message, null);
    }

    /**
     * Convenience method to log some info
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logInfo(String message, Throwable t) {
        log(IStatus.INFO, message, t);
    }

    /**
     * Convenience method to log some info
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logInfo(String message) {
        log(IStatus.INFO, message, null);
    }

    /**
     * Convenience method to log a warning
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logWarning(String message, Throwable t) {
        log(IStatus.WARNING, message, t);
    }

    /**
     * Convenience method to log a warning
     * 
     * @param message Message to print
     */
    public static void logWarning(String message) {
        log(IStatus.WARNING, message, null);
    }

    /**
     * Helper logger to log events for this bundle plug-in
     * 
     * @param severity can be
     *          IStatus.WARNING
     *          IStatus.INFO
     *          IStatus.ERROR
     *          IStatus.OK
     *          IStatus.CANCEL
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void log(int severity, String message, Throwable t) {
        if(enabled) {
            ArchiPlugin.INSTANCE.getLog().log(
                    new Status(severity, ArchiPlugin.INSTANCE.getId(), IStatus.OK, message, t));
        }
    }
}
