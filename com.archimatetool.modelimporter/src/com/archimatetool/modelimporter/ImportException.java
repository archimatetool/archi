/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;




/**
 * Importer Exception
 * 
 * @author Phillip Beauvoir
 */
public class ImportException extends Exception {
    
    /**
     * Constructs a {@code ImportException} with {@code null}
     * as its error detail message.
     */
    public ImportException() {
        super();
    }

    /**
     * Constructs a {@code ImportException} with the specified detail message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public ImportException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code ImportException} with the specified detail message
     * and cause.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     */
    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@code ImportException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     */
    public ImportException(Throwable cause) {
        super(cause);
    }
}
