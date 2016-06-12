/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sketch Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.ISketchModel#getBackground <em>Background</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getSketchModel()
 * @model
 * @generated
 */
public interface ISketchModel extends IDiagramModel {

    /**
     * Returns the value of the '<em><b>Background</b></em>' attribute.
     * The default value is <code>"1"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Background</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Background</em>' attribute.
     * @see #setBackground(int)
     * @see com.archimatetool.model.IArchimatePackage#getSketchModel_Background()
     * @model default="1"
     * @generated
     */
    int getBackground();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ISketchModel#getBackground <em>Background</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Background</em>' attribute.
     * @see #getBackground()
     * @generated
     */
    void setBackground(int value);
} // ISketchModel
