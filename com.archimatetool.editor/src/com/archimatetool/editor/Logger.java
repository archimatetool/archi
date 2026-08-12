/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.osgi.framework.Bundle;

/**
 * Logger
 * @author Phillip Beauvoir
 * @deprecated Use the {@link org.eclipse.core.runtime.ILog} methods instead
 */
@Deprecated(since = "5.10", forRemoval = true)
public final class Logger {

    private static Bundle bundle = ArchiPlugin.getInstance().getBundle();
    
    /**
     * Log info
     * @param message the message to log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#info(String)}
     */
    @Deprecated
    public static void info(String message) {
        ILog.of(bundle).info(message);
    }

    /**
     * Log info
     * @param message the message to log
     * @param throwable an optional Throwable to associate with this log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#info(String, Throwable)}
     */
    @Deprecated
    public static void info(String message, Throwable throwable) {
        ILog.of(bundle).info(message, throwable);
    }

    /**
     * Log warning
     * @param message Message to log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#warn(String)}
     */
    @Deprecated
    public static void warning(String message) {
        ILog.of(bundle).warn(message);
    }

    /**
     * Log warning
     * @param message the message to log
     * @param throwable an optional Throwable to associate with this log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#warn(String, Throwable)}
     */
    @Deprecated
    public static void warning(String message, Throwable throwable) {
        ILog.of(bundle).warn(message, throwable);
    }

    /**
     * Log an error
     * @param message the message to log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#error(String)}
     */
    @Deprecated
    public static void error(String message) {
        ILog.of(bundle).error(message);
    }

    /**
     * Log an error
     * @param message the message to log
     * @param throwable an optional Throwable to associate with this log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#error(String, throwable)}
     */
    @Deprecated
    public static void error(String message, Throwable throwable) {
        ILog.of(bundle).error(message, throwable);
    }

    /**
     * Log the given status.
     * @param status the status to log
     * @since 5.8.0
     * @deprecated use {@link org.eclipse.core.runtime.ILog#log(IStatus)}
     */
    @Deprecated
    public static void log(IStatus status) {
        ILog.of(bundle).log(status);
    }
    
   
    // ------------------------------------------------------------------ 
    // Old methods before 5.8.0
    // ------------------------------------------------------------------ 
    
    /**
     * @deprecated use {@link org.eclipse.core.runtime.ILog#error(String)}
     */
    @Deprecated
    public static void logError(String message) {
        ILog.of(bundle).error(message);
    }

    /**
     * @deprecated use {@link org.eclipse.core.runtime.ILog#error(String, Throwable)}
     */
    @Deprecated
    public static void logError(String message, Throwable throwable) {
        ILog.of(bundle).error(message, throwable);
    }
    
    /**
     * @deprecated use {@link org.eclipse.core.runtime.ILog#warn(String)}
     */
    @Deprecated
    public static void logWarning(String message) {
        ILog.of(bundle).warn(message);
    }

    /**
     * @deprecated use {@link org.eclipse.core.runtime.ILog#warn(String, Throwable)}
     */
    @Deprecated
    public static void logWarning(String message, Throwable throwable) {
        ILog.of(bundle).warn(message, throwable);
    }
}
