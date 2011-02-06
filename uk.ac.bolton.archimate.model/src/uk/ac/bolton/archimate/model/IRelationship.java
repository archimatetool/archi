/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IRelationship#getSource <em>Source</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IRelationship#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getRelationship()
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
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getRelationship_Source()
     * @model resolveProxies="false"
     * @generated
     */
    IArchimateElement getSource();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IRelationship#getSource <em>Source</em>}' reference.
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
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getRelationship_Target()
     * @model resolveProxies="false"
     * @generated
     */
    IArchimateElement getTarget();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IRelationship#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(IArchimateElement value);

} // IRelationship
