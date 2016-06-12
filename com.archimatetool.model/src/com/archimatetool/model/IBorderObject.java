/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Border Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IBorderObject#getBorderColor <em>Border Color</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getBorderObject()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IBorderObject extends EObject {
    /**
     * Returns the value of the '<em><b>Border Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Border Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Border Color</em>' attribute.
     * @see #setBorderColor(String)
     * @see com.archimatetool.model.IArchimatePackage#getBorderObject_BorderColor()
     * @model
     * @generated
     */
    String getBorderColor();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IBorderObject#getBorderColor <em>Border Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Border Color</em>' attribute.
     * @see #getBorderColor()
     * @generated
     */
    void setBorderColor(String value);

} // IBorderObject
