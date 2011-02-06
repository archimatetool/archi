/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IArchimateElement#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getArchimateElement()
 * @model abstract="true"
 * @generated
 */
public interface IArchimateElement extends IArchimateModelElement, IIdentifier, ICloneable, INameable, IDocumentable {
    /**
     * Returns the value of the '<em><b>Properties</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Properties</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Properties</em>' map.
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getArchimateElement_Properties()
     * @model mapType="uk.ac.bolton.archimate.model.Property<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
     * @generated
     */
    EMap<String, String> getProperties();

} // IArchimateElement
