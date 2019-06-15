/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IArchimateRelationship#getSource <em>Source</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateRelationship#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getArchimateRelationship()
 * @model abstract="true"
 * @generated
 */
public interface IArchimateRelationship extends IArchimateConcept {
    /**
     * Returns the value of the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference.
     * @see #setSource(IArchimateConcept)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateRelationship_Source()
     * @model resolveProxies="false"
     * @generated
     */
    IArchimateConcept getSource();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateRelationship#getSource <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' reference.
     * @see #getSource()
     * @generated
     */
    void setSource(IArchimateConcept value);

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(IArchimateConcept)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateRelationship_Target()
     * @model resolveProxies="false"
     * @generated
     */
    IArchimateConcept getTarget();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateRelationship#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(IArchimateConcept value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void connect(IArchimateConcept source, IArchimateConcept target);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void reconnect();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void disconnect();
    
    /**
     * @return A list of diagram connections that reference this Relationship
     */
    List<IDiagramModelArchimateConnection> getReferencingDiagramConnections();

} // IArchimateRelationship
