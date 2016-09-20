/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Access Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IAccessRelationship#getAccessType <em>Access Type</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getAccessRelationship()
 * @model
 * @generated
 */
public interface IAccessRelationship extends IDependendencyRelationship {

    int WRITE_ACCESS = 0; // Default
    int READ_ACCESS = 1;
    int UNSPECIFIED_ACCESS = 2;
    int READ_WRITE_ACCESS = 3;

    /**
     * Returns the value of the '<em><b>Access Type</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Access Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Access Type</em>' attribute.
     * @see #setAccessType(int)
     * @see com.archimatetool.model.IArchimatePackage#getAccessRelationship_AccessType()
     * @model default="0"
     * @generated
     */
    int getAccessType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IAccessRelationship#getAccessType <em>Access Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Access Type</em>' attribute.
     * @see #getAccessType()
     * @generated
     */
    void setAccessType(int value);
} // IAccessRelationship
