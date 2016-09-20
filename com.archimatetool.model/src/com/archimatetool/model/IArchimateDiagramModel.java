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
 *   <li>{@link com.archimatetool.model.IArchimateDiagramModel#getViewpoint <em>Viewpoint</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getArchimateDiagramModel()
 * @model
 * @generated
 */
public interface IArchimateDiagramModel extends IDiagramModel {
    /**
     * Returns the value of the '<em><b>Viewpoint</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Viewpoint</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Viewpoint</em>' attribute.
     * @see #setViewpoint(String)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateDiagramModel_Viewpoint()
     * @model default=""
     * @generated
     */
    String getViewpoint();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateDiagramModel#getViewpoint <em>Viewpoint</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Viewpoint</em>' attribute.
     * @see #getViewpoint()
     * @generated
     */
    void setViewpoint(String value);

} // IArchimateDiagramModel
