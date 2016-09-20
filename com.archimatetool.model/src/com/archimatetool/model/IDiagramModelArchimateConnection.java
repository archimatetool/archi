/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Archimate Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelArchimateConnection#getArchimateRelationship <em>Archimate Relationship</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelArchimateConnection()
 * @model extendedMetaData="name='Connection'"
 * @generated
 */
public interface IDiagramModelArchimateConnection extends IDiagramModelConnection, IDiagramModelArchimateComponent {
    /**
     * Returns the value of the '<em><b>Archimate Relationship</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Archimate Relationship</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Archimate Relationship</em>' reference.
     * @see #setArchimateRelationship(IArchimateRelationship)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelArchimateConnection_ArchimateRelationship()
     * @model resolveProxies="false" volatile="true"
     * @generated
     */
    IArchimateRelationship getArchimateRelationship();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelArchimateConnection#getArchimateRelationship <em>Archimate Relationship</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Archimate Relationship</em>' reference.
     * @see #getArchimateRelationship()
     * @generated
     */
    void setArchimateRelationship(IArchimateRelationship value);

    /**
     * Over-ride to return correct type
     */
    @Override
    IArchimateRelationship getArchimateConcept();

} // IDiagramModelArchimateConnection
