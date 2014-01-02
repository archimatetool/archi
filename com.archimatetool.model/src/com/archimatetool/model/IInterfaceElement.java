/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interface Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.archimatetool.model.IInterfaceElement#getInterfaceType <em>Interface Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getInterfaceElement()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IInterfaceElement extends IArchimateElement {
    
    int PROVIDED = 0;
    int REQUIRED = 1;
    
    /**
     * Returns the value of the '<em><b>Interface Type</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Interface Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Interface Type</em>' attribute.
     * @see #setInterfaceType(int)
     * @see com.archimatetool.model.IArchimatePackage#getInterfaceElement_InterfaceType()
     * @model default="0"
     * @generated
     */
    int getInterfaceType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IInterfaceElement#getInterfaceType <em>Interface Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interface Type</em>' attribute.
     * @see #getInterfaceType()
     * @generated
     */
    void setInterfaceType(int value);

} // IInterfaceElement
