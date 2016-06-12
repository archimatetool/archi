/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notes Content</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.canvas.model.INotesContent#getNotes <em>Notes</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.canvas.model.ICanvasPackage#getNotesContent()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface INotesContent extends EObject {
    /**
     * Returns the value of the '<em><b>Notes</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Notes</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Notes</em>' attribute.
     * @see #setNotes(String)
     * @see com.archimatetool.canvas.model.ICanvasPackage#getNotesContent_Notes()
     * @model default=""
     *        extendedMetaData="kind='element'"
     * @generated
     */
    String getNotes();

    /**
     * Sets the value of the '{@link com.archimatetool.canvas.model.INotesContent#getNotes <em>Notes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Notes</em>' attribute.
     * @see #getNotes()
     * @generated
     */
    void setNotes(String value);

} // INotesContent
