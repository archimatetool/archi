/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Component</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelComponent()
 * @model abstract="true"
 * @generated
 */
public interface IDiagramModelComponent extends IIdentifier, ICloneable, IAdapter, INameable, IArchimateModelObject {
    /**
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Diagram Model</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    IDiagramModel getDiagramModel();
} // IDiagramModelComponent
