/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Association Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IAssociationRelationship#isDirected <em>Directed</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getAssociationRelationship()
 * @model
 * @generated
 */
public interface IAssociationRelationship extends IDependendencyRelationship {

    /**
     * Returns the value of the '<em><b>Directed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed</em>' attribute.
     * @see #setDirected(boolean)
     * @see com.archimatetool.model.IArchimatePackage#getAssociationRelationship_Directed()
     * @model
     * @generated
     */
    boolean isDirected();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IAssociationRelationship#isDirected <em>Directed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed</em>' attribute.
     * @see #isDirected()
     * @generated
     */
    void setDirected(boolean value);
} // IAssociationRelationship
