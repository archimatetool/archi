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
 *       IRunnable.run(dialog, monitor -> {
 *           // do stuff...
 *           monitor.setTaskName("New task");
 *       }, true);
 *   }
 *   catch(Exception ex) {
 *       ex.printStackTrace();
 *   } 
 * </pre>
 * 
 * @author Phillip Beauvoir
 */
public interface IRunnable {
    
    void run(IProgressMonitor monitor) throws Exception;
    
    /**
     * Run the Runnable, catching any Exceptions and re-throwing them
     * @param fork true if the runnable should be run in a separate thread and false to run in the same thread
     */
    static void run(IRunnableContext context, IRunnable runnable, boolean fork) throws Exception {
        AtomicReference<Exception> exception = new AtomicReference<>();
        
        try {
            context.run(fork, true, monitor -> {
                try {
                    runnable.run(monitor);
                }
                catch(Exception ex) {
                    exception.set(ex);
                }
            });
        }
        catch(InvocationTargetException ex) {
            exception.set(new Exception(ex.getTargetException())); // we want the target exception
        }
        catch(InterruptedException ex) {
            exception.set(ex);
        }
        
        if(exception.get() != null) {
            throw exception.get();
        }
    }
}