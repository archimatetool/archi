/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text Content</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.ITextContent#getContent <em>Content</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getTextContent()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ITextContent extends EObject {
    /**
     * Returns the value of the '<em><b>Content</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Content</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Content</em>' attribute.
     * @see #setContent(String)
     * @see com.archimatetool.model.IArchimatePackage#getTextContent_Content()
     * @model default=""
     *        extendedMetaData="kind='element'"
     * @generated
     */
    String getContent();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ITextContent#getContent <em>Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Content</em>' attribute.
     * @see #getContent()
     * @generated
     */
    void setContent(String value);

} // ITextContent
