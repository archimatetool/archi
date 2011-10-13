/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelContainer#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelContainer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IDiagramModelContainer extends IDiagramModelComponent {
    /**
     * Returns the value of the '<em><b>Children</b></em>' containment reference list.
     * The list contents are of type {@link uk.ac.bolton.archimate.model.IDiagramModelObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' containment reference list.
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelContainer_Children()
     * @model containment="true"
     *        extendedMetaData="name='child' kind='element'"
     * @generated
     */
    EList<IDiagramModelObject> getChildren();

} // IDiagramModelContainer
