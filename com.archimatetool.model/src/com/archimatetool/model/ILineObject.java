/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Line Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.ILineObject#getLineWidth <em>Line Width</em>}</li>
 *   <li>{@link com.archimatetool.model.ILineObject#getLineColor <em>Line Color</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getLineObject()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ILineObject extends EObject {
    /**
     * Returns the value of the '<em><b>Line Width</b></em>' attribute.
     * The default value is <code>"1"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Width</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line Width</em>' attribute.
     * @see #setLineWidth(int)
     * @see com.archimatetool.model.IArchimatePackage#getLineObject_LineWidth()
     * @model default="1"
     * @generated
     */
    int getLineWidth();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ILineObject#getLineWidth <em>Line Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line Width</em>' attribute.
     * @see #getLineWidth()
     * @generated
     */
    void setLineWidth(int value);

    /**
     * Returns the value of the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line Color</em>' attribute.
     * @see #setLineColor(String)
     * @see com.archimatetool.model.IArchimatePackage#getLineObject_LineColor()
     * @model
     * @generated
     */
    String getLineColor();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ILineObject#getLineColor <em>Line Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line Color</em>' attribute.
     * @see #getLineColor()
     * @generated
     */
    void setLineColor(String value);

} // ILineObject
