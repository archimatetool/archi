/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IArchimateModelElement#getArchimateModel <em>Archimate Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getArchimateModelElement()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IArchimateModelElement extends IAdapter {
    /**
     * Returns the value of the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Archimate Model</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Archimate Model</em>' reference.
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getArchimateModelElement_ArchimateModel()
     * @model resolveProxies="false" transient="true" changeable="false" volatile="true"
     * @generated
     */
    IArchimateModel getArchimateModel();

} // IArchimateModelElement
