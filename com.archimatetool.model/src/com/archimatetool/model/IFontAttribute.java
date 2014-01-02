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
 * <ul>
 *   <li>{@link com.archimatetool.model.IFontAttribute#getFont <em>Font</em>}</li>
 *   <li>{@link com.archimatetool.model.IFontAttribute#getFontColor <em>Font Color</em>}</li>
 *   <li>{@link com.archimatetool.model.IFontAttribute#getTextAlignment <em>Text Alignment</em>}</li>
 *   <li>{@link com.archimatetool.model.IFontAttribute#getTextPosition <em>Text Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFontAttribute()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IFontAttribute extends EObject {
    
    int TEXT_ALIGNMENT_NONE = 0; // Legacy support
    int TEXT_ALIGNMENT_LEFT = 1;
    int TEXT_ALIGNMENT_CENTER = 2;
    int TEXT_ALIGNMENT_RIGHT = 4;
    
    int TEXT_POSITION_TOP_LEFT = 0;
    int TEXT_POSITION_TOP_CENTRE = 1;
    int TEXT_POSITION_TOP_RIGHT = 2;
    int TEXT_POSITION_MIDDLE_LEFT = 3;
    int TEXT_POSITION_MIDDLE_CENTRE = 4;
    int TEXT_POSITION_MIDDLE_RIGHT = 5;
    int TEXT_POSITION_BOTTOM_LEFT = 6;
    int TEXT_POSITION_BOTTOM_CENTRE = 7;
    int TEXT_POSITION_BOTTOM_RIGHT = 8;
    
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

    /**
     * Returns the value of the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Alignment</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Alignment</em>' attribute.
     * @see #setTextAlignment(int)
     * @see com.archimatetool.model.IArchimatePackage#getFontAttribute_TextAlignment()
     * @model
     * @generated
     */
    int getTextAlignment();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFontAttribute#getTextAlignment <em>Text Alignment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Alignment</em>' attribute.
     * @see #getTextAlignment()
     * @generated
     */
    void setTextAlignment(int value);

    /**
     * Returns the value of the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Position</em>' attribute.
     * @see #setTextPosition(int)
     * @see com.archimatetool.model.IArchimatePackage#getFontAttribute_TextPosition()
     * @model
     * @generated
     */
    int getTextPosition();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFontAttribute#getTextPosition <em>Text Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Position</em>' attribute.
     * @see #getTextPosition()
     * @generated
     */
    void setTextPosition(int value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    int getDefaultTextAlignment();

} // IFontAttribute
