/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Image Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelImageProvider#getImagePath <em>Image Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelImageProvider()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IDiagramModelImageProvider extends EObject {
    /**
     * Returns the value of the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image Path</em>' attribute.
     * @see #setImagePath(String)
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelImageProvider_ImagePath()
     * @model
     * @generated
     */
    String getImagePath();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelImageProvider#getImagePath <em>Image Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image Path</em>' attribute.
     * @see #getImagePath()
     * @generated
     */
    void setImagePath(String value);

} // IDiagramModelImageProvider
