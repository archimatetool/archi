/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IFeature#getName <em>Name</em>}</li>
 *   <li>{@link com.archimatetool.model.IFeature#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFeature()
 * @model
 * @generated
 */
public interface IFeature extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see com.archimatetool.model.IArchimatePackage#getFeature_Name()
     * @model default=""
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFeature#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see com.archimatetool.model.IArchimatePackage#getFeature_Value()
     * @model default=""
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFeature#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

} // IFeature
