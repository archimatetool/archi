/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Junction</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IJunction#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getJunction()
 * @model
 * @generated
 */
public interface IJunction extends IArchimateElement {
    
    String AND_JUNCTION_TYPE = ""; //Default   //$NON-NLS-1$
    String OR_JUNCTION_TYPE = "or";  //$NON-NLS-1$
    //String XOR_JUNCTION_TYPE = "xor";  //$NON-NLS-1$
    
    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see com.archimatetool.model.IArchimatePackage#getJunction_Type()
     * @model default=""
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IJunction#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

} // IJunction
