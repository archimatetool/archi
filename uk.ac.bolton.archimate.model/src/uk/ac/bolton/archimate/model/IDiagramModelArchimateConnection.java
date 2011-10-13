/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Archimate Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection#getRelationship <em>Relationship</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelArchimateConnection()
 * @model extendedMetaData="name='Connection'"
 * @generated
 */
public interface IDiagramModelArchimateConnection extends IDiagramModelConnection {
    /**
     * Returns the value of the '<em><b>Relationship</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relationship</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relationship</em>' reference.
     * @see #setRelationship(IRelationship)
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelArchimateConnection_Relationship()
     * @model resolveProxies="false" volatile="true"
     * @generated
     */
    IRelationship getRelationship();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection#getRelationship <em>Relationship</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relationship</em>' reference.
     * @see #getRelationship()
     * @generated
     */
    void setRelationship(IRelationship value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void addRelationshipToModel(IFolder parent);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void removeRelationshipFromModel();

} // IDiagramModelArchimateConnection
