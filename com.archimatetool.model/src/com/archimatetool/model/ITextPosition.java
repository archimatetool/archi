/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text Position</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.ITextPosition#getTextPosition <em>Text Position</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getTextPosition()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ITextPosition extends EObject {

    int TEXT_POSITION_TOP = 0;
    int TEXT_POSITION_CENTRE = 1;
    int TEXT_POSITION_BOTTOM = 2;
    
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
     * @see com.archimatetool.model.IArchimatePackage#getTextPosition_TextPosition()
     * @model
     * @generated
     */
    int getTextPosition();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ITextPosition#getTextPosition <em>Text Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Position</em>' attribute.
     * @see #getTextPosition()
     * @generated
     */
    void setTextPosition(int value);

} // ITextPosition
