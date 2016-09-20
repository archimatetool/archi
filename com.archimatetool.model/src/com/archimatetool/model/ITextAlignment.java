/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text Alignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.ITextAlignment#getTextAlignment <em>Text Alignment</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getTextAlignment()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ITextAlignment extends EObject {
    
    int TEXT_ALIGNMENT_LEFT = 1;
    int TEXT_ALIGNMENT_CENTER = 2;
    int TEXT_ALIGNMENT_RIGHT = 4;
    
    /**
     * Returns the value of the '<em><b>Text Alignment</b></em>' attribute.
     * The default value is <code>"2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Alignment</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Alignment</em>' attribute.
     * @see #setTextAlignment(int)
     * @see com.archimatetool.model.IArchimatePackage#getTextAlignment_TextAlignment()
     * @model default="2"
     * @generated
     */
    int getTextAlignment();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ITextAlignment#getTextAlignment <em>Text Alignment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Alignment</em>' attribute.
     * @see #getTextAlignment()
     * @generated
     */
    void setTextAlignment(int value);

} // ITextAlignment
