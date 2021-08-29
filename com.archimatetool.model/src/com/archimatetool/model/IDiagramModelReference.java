/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelReference#getReferencedModel <em>Referenced Model</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelReference()
 * @model
 * @generated
 */
public interface IDiagramModelReference extends IDiagramModelObject, ITextPosition, IIconic {

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
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelReference_ReferencedModel()
     * @model resolveProxies="false"
     *        extendedMetaData="name='model' kind='attribute'"
     * @generated
     */
    IDiagramModel getReferencedModel();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelReference#getReferencedModel <em>Referenced Model</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Referenced Model</em>' reference.
     * @see #getReferencedModel()
     * @generated
     */
    void setReferencedModel(IDiagramModel value);
} // IDiagramModelReference
