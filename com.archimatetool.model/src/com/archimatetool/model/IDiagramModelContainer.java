/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelContainer#getChildren <em>Children</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelContainer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IDiagramModelContainer extends IDiagramModelComponent {
    /**
     * Returns the value of the '<em><b>Children</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IDiagramModelObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelContainer_Children()
     * @model containment="true"
     *        extendedMetaData="name='child' kind='element'"
     * @generated
     */
    EList<IDiagramModelObject> getChildren();

} // IDiagramModelContainer
