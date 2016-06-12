/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Font Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IFontAttribute#getFont <em>Font</em>}</li>
 *   <li>{@link com.archimatetool.model.IFontAttribute#getFontColor <em>Font Color</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFontAttribute()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IFontAttribute extends EObject {
    /**
     * Returns the value of the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Font</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Font</em>' attribute.
     * @see #setFont(String)
     * @see com.archimatetool.model.IArchimatePackage#getFontAttribute_Font()
     * @model
     * @generated
     */
    String getFont();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFontAttribute#getFont <em>Font</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Font</em>' attribute.
     * @see #getFont()
     * @generated
     */
    void setFont(String value);

    /**
     * Returns the value of the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Font Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Font Color</em>' attribute.
     * @see #setFontColor(String)
     * @see com.archimatetool.model.IArchimatePackage#getFontAttribute_FontColor()
     * @model
     * @generated
     */
    String getFontColor();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFontAttribute#getFontColor <em>Font Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Font Color</em>' attribute.
     * @see #getFontColor()
     * @generated
     */
    void setFontColor(String value);

} // IFontAttribute
