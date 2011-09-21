/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Font Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IFontAttribute#getFont <em>Font</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IFontAttribute#getFontColor <em>Font Color</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IFontAttribute#getTextAlignment <em>Text Alignment</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFontAttribute()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IFontAttribute extends EObject {
    
    int TEXT_ALIGNMENT_NONE = 0; // Legacy support
    int TEXT_ALIGNMENT_LEFT = 1;
    int TEXT_ALIGNMENT_CENTER = 2;
    int TEXT_ALIGNMENT_RIGHT = 4;
    
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
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFontAttribute_Font()
     * @model
     * @generated
     */
    String getFont();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IFontAttribute#getFont <em>Font</em>}' attribute.
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
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFontAttribute_FontColor()
     * @model
     * @generated
     */
    String getFontColor();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IFontAttribute#getFontColor <em>Font Color</em>}' attribute.
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
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFontAttribute_TextAlignment()
     * @model
     * @generated
     */
    int getTextAlignment();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IFontAttribute#getTextAlignment <em>Text Alignment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Alignment</em>' attribute.
     * @see #getTextAlignment()
     * @generated
     */
    void setTextAlignment(int value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    int getDefaultTextAlignment();

} // IFontAttribute
