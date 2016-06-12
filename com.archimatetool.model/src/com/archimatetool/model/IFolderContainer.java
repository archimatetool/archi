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
 * A representation of the model object '<em><b>Folder Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IFolderContainer#getFolders <em>Folders</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFolderContainer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IFolderContainer extends EObject {
    /**
     * Returns the value of the '<em><b>Folders</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IFolder}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Folders</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Folders</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getFolderContainer_Folders()
     * @model containment="true"
     *        extendedMetaData="name='folder' kind='element'"
     * @generated
     */
    EList<IFolder> getFolders();

} // IFolderContainer
