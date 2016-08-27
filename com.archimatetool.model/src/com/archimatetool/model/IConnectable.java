/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connectable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IConnectable#getSourceConnections <em>Source Connections</em>}</li>
 *   <li>{@link com.archimatetool.model.IConnectable#getTargetConnections <em>Target Connections</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getConnectable()
 * @model abstract="true"
 * @generated
 */
public interface IConnectable extends IDiagramModelComponent {
    /**
     * Returns the value of the '<em><b>Source Connections</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IDiagramModelConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Connections</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Connections</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getConnectable_SourceConnections()
     * @model containment="true"
     *        extendedMetaData="name='sourceConnection' kind='element'"
     * @generated
     */
    EList<IDiagramModelConnection> getSourceConnections();

    /**
     * Returns the value of the '<em><b>Target Connections</b></em>' reference list.
     * The list contents are of type {@link com.archimatetool.model.IDiagramModelConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Connections</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Connections</em>' reference list.
     * @see com.archimatetool.model.IArchimatePackage#getConnectable_TargetConnections()
     * @model resolveProxies="false"
     * @generated
     */
    EList<IDiagramModelConnection> getTargetConnections();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void addConnection(IDiagramModelConnection connection);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void removeConnection(IDiagramModelConnection connection);

} // IConnectable
