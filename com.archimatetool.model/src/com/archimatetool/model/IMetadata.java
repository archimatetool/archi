/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.archimatetool.model.IMetadata#getEntries <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getMetadata()
 * @model
 * @generated
 */
public interface IMetadata extends EObject {
    /**
     * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IProperty}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Entries</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getMetadata_Entries()
     * @model containment="true"
     *        extendedMetaData="name='entry' kind='element'"
     * @generated
     */
    EList<IProperty> getEntries();

} // IMetadata
