/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Influence Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IInfluenceRelationship#getStrength <em>Strength</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getInfluenceRelationship()
 * @model
 * @generated
 */
public interface IInfluenceRelationship extends IDependendencyRelationship {

    /**
     * Returns the value of the '<em><b>Strength</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Strength</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Strength</em>' attribute.
     * @see #setStrength(String)
     * @see com.archimatetool.model.IArchimatePackage#getInfluenceRelationship_Strength()
     * @model default=""
     * @generated
     */
    String getStrength();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IInfluenceRelationship#getStrength <em>Strength</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Strength</em>' attribute.
     * @see #getStrength()
     * @generated
     */
    void setStrength(String value);
} // IInfluenceRelationship
