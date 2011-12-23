/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.canvas.model;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Hint Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.IHintProvider#getHintTitle <em>Hint Title</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.IHintProvider#getHintContent <em>Hint Content</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.canvas.model.ICanvasPackage#getHintProvider()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IHintProvider extends EObject {
    /**
     * Returns the value of the '<em><b>Hint Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Hint Title</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hint Title</em>' attribute.
     * @see #setHintTitle(String)
     * @see uk.ac.bolton.archimate.canvas.model.ICanvasPackage#getHintProvider_HintTitle()
     * @model
     * @generated
     */
    String getHintTitle();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.canvas.model.IHintProvider#getHintTitle <em>Hint Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hint Title</em>' attribute.
     * @see #getHintTitle()
     * @generated
     */
    void setHintTitle(String value);

    /**
     * Returns the value of the '<em><b>Hint Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Hint Content</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hint Content</em>' attribute.
     * @see #setHintContent(String)
     * @see uk.ac.bolton.archimate.canvas.model.ICanvasPackage#getHintProvider_HintContent()
     * @model extendedMetaData="kind='element'"
     * @generated
     */
    String getHintContent();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.canvas.model.IHintProvider#getHintContent <em>Hint Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hint Content</em>' attribute.
     * @see #getHintContent()
     * @generated
     */
    void setHintContent(String value);

} // IHintProvider
