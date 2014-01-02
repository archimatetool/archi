/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.archimatetool.model.IRelationship#getSource <em>Source</em>}</li>
 *   <li>{@link com.archimatetool.model.IRelationship#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getRelationship()
 * @model abstract="true"
 * @generated
 */
public interface IRelationship extends IArchimateElement {
    /**
     * Returns the value of the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference.
     * @see #setSource(IArchimateElement)
     * @see com.archimatetool.model.IArchimatePackage#getRelationship_Source()
     * @model resolveProxies="false"
     * @generated
     */
    IArchimateElement getSource();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IRelationship#getSource <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' reference.
     * @see #getSource()
     * @generated
     */
    void setSource(IArchimateElement value);

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(IArchimateElement)
     * @see com.archimatetool.model.IArchimatePackage#getRelationship_Target()
     * @model resolveProxies="false"
     * @generated
     */
    IArchimateElement getTarget();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IRelationship#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(IArchimateElement value);

} // IRelationship
