/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Documentable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDocumentable#getDocumentation <em>Documentation</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDocumentable()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IDocumentable extends EObject {
    /**
     * Returns the value of the '<em><b>Documentation</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Documentation</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Documentation</em>' attribute.
     * @see #setDocumentation(String)
     * @see com.archimatetool.model.IArchimatePackage#getDocumentable_Documentation()
     * @model default=""
     *        extendedMetaData="kind='element'"
     * @generated
     */
    String getDocumentation();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDocumentable#getDocumentation <em>Documentation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Documentation</em>' attribute.
     * @see #getDocumentation()
     * @generated
     */
    void setDocumentation(String value);

} // IDocumentable
