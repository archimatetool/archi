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
 * A representation of the model object '<em><b>Text Content</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.ITextContent#getContent <em>Content</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getTextContent()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ITextContent extends EObject {
    /**
     * Returns the value of the '<em><b>Content</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Content</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Content</em>' attribute.
     * @see #setContent(String)
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getTextContent_Content()
     * @model default=""
     * @generated
     */
    String getContent();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.ITextContent#getContent <em>Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Content</em>' attribute.
     * @see #getContent()
     * @generated
     */
    void setContent(String value);

} // ITextContent
