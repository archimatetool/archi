/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModel#getConnectionRouterType <em>Connection Router Type</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModel()
 * @model abstract="true"
 * @generated
 */
public interface IDiagramModel extends IArchimateModelObject, IDiagramModelContainer, IDocumentable, IProperties {
    
    /*
     * Connection Router Types
     */
    int CONNECTION_ROUTER_BENDPOINT = 0;
    
    // Doesn't work with connections to connections
    // int CONNECTION_ROUTER_SHORTEST_PATH = 1;
    
    int CONNECTION_ROUTER_MANHATTAN = 2;

    /**
     * Returns the value of the '<em><b>Connection Router Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection Router Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connection Router Type</em>' attribute.
     * @see #setConnectionRouterType(int)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModel_ConnectionRouterType()
     * @model
     * @generated
     */
    int getConnectionRouterType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModel#getConnectionRouterType <em>Connection Router Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection Router Type</em>' attribute.
     * @see #getConnectionRouterType()
     * @generated
     */
    void setConnectionRouterType(int value);

} // IDiagramModel
