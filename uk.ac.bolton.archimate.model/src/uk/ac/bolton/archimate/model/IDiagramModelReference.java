/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelReference#getReferencedModel <em>Referenced Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelReference()
 * @model
 * @generated
 */
public interface IDiagramModelReference extends IDiagramModelObject {

    /**
     * Returns the value of the '<em><b>Referenced Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Referenced Model</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Referenced Model</em>' reference.
     * @see #setReferencedModel(IDiagramModel)
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelReference_ReferencedModel()
     * @model resolveProxies="false"
     * @generated
     */
    IDiagramModel getReferencedModel();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelReference#getReferencedModel <em>Referenced Model</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Referenced Model</em>' reference.
     * @see #getReferencedModel()
     * @generated
     */
    void setReferencedModel(IDiagramModel value);
} // IDiagramModelReference
