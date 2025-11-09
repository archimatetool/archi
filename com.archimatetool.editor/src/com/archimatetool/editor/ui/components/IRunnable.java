package com.archimatetool.editor.ui.components;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * An IRunnableContext and IRunnableWithProgress wrapper that catches and re-throws any Exceptions. Example:
 * 
 * <pre>
 *   ProgressMonitorDialog dialog = ...;
 *   try {
 *       IRunnable.run(dialog, true, monitor -> {
 *           // do stuff...
 *           monitor.setTaskName("New task");
 *       });
 *   }
 *   catch(Exception ex) {
 *       ex.printStackTrace();
 *   } 
 * </pre>
 * 
 * @author Phillip Beauvoir
 * @since 5.2.0
 */
public interface IRunnable {
    
    void run(IProgressMonitor monitor) throws Exception;
    
    /**
     * Run the Runnable, catching any Exceptions and re-throwing them
     * @param context The context which is typically a ProgressMonitorDialog or Job, or WizardDialog
     * @param fork if true the runnable should be run in a separate thread and false to run in the same thread
     * @param cancelable <code>true</code> to enable the cancelation, and <code>false</code> to make the operation uncancellable
     * @param runnable The IRunnable to run
     * @throws Exception
     * @since 5.8.0
     */
    static void run(IRunnableContext context, boolean fork, boolean cancelable, IRunnable runnable) throws Exception {
        // The possible exception thrown by runnable.run(monitor)
        AtomicReference<Exception> exception = new AtomicReference<>();
        
        try {
            context.run(fork, cancelable, monitor -> {
                try {
                    runnable.run(monitor);
                }
                catch(Exception ex) {
                    exception.set(ex); // store this...
                }
            });
        }
        catch(InvocationTargetException ex) {
            throw new Exception(ex.getTargetException()); // we want the target exception
        }
        
        if(exception.get() != null) { // ...and throw it
            throw exception.get();
        }
    }
    
    /**
     * This is deprecated since 5.7.0 because the order of parameters is not ideal and doesn't have a cancelable option.
     * So use run(IRunnableContext context, boolean fork, boolean cancelable, IRunnable runnable) above.
     * @since 5.2.0
     */
    static void run(IRunnableContext context, IRunnable runnable, boolean fork) throws Exception {
        run(context, fork, true, runnable);
    }
}