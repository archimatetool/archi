/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Hint Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IHintProvider#getHintTitle <em>Hint Title</em>}</li>
 *   <li>{@link com.archimatetool.model.IHintProvider#getHintContent <em>Hint Content</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getHintProvider()
 * @model interface="true" abstract="true" superTypes="com.archimatetool.model.HelpHintProvider"
 * @generated
 */
public interface IHintProvider extends EObject, IHelpHintProvider {
    /**
     * Returns the value of the '<em><b>Hint Title</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hint Title</em>' attribute.
     * @see #setHintTitle(String)
     * @see com.archimatetool.model.IArchimatePackage#getHintProvider_HintTitle()
     * @model default=""
     * @generated
     */
    @Override
    String getHintTitle();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IHintProvider#getHintTitle <em>Hint Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hint Title</em>' attribute.
     * @see #getHintTitle()
     * @generated
     */
    void setHintTitle(String value);

    /**
     * Returns the value of the '<em><b>Hint Content</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hint Content</em>' attribute.
     * @see #setHintContent(String)
     * @see com.archimatetool.model.IArchimatePackage#getHintProvider_HintContent()
     * @model default=""
     *        extendedMetaData="kind='element'"
     * @generated
     */
    @Override
    String getHintContent();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IHintProvider#getHintContent <em>Hint Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hint Content</em>' attribute.
     * @see #getHintContent()
     * @generated
     */
    void setHintContent(String value);

} // IHintProvider
