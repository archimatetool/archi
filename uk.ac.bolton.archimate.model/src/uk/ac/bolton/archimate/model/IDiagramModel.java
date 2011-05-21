/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModel#getConnectionRouterType <em>Connection Router Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModel()
 * @model abstract="true"
 * @generated
 */
public interface IDiagramModel extends IArchimateModelElement, IDiagramModelContainer, IDocumentable, IProperties {
    
    /*
     * Connection Router Types
     */
    int CONNECTION_ROUTER_BENDPOINT = 0;
    
    int CONNECTION_ROUTER_SHORTEST_PATH = 1;
    
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
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModel_ConnectionRouterType()
     * @model
     * @generated
     */
    int getConnectionRouterType();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModel#getConnectionRouterType <em>Connection Router Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection Router Type</em>' attribute.
     * @see #getConnectionRouterType()
     * @generated
     */
    void setConnectionRouterType(int value);

} // IDiagramModel
