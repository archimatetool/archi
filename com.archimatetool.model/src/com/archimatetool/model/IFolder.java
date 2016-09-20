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
 * A representation of the model object '<em><b>Folder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IFolder#getElements <em>Elements</em>}</li>
 *   <li>{@link com.archimatetool.model.IFolder#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFolder()
 * @model
 * @generated
 */
public interface IFolder extends IArchimateModelObject, IFolderContainer, IDocumentable, IProperties {
    /**
     * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Elements</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getFolder_Elements()
     * @model containment="true"
     *        extendedMetaData="name='element' kind='element'"
     * @generated
     */
    EList<EObject> getElements();

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The literals are from the enumeration {@link com.archimatetool.model.FolderType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see com.archimatetool.model.FolderType
     * @see #setType(FolderType)
     * @see com.archimatetool.model.IArchimatePackage#getFolder_Type()
     * @model
     * @generated
     */
    FolderType getType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IFolder#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see com.archimatetool.model.FolderType
     * @see #getType()
     * @generated
     */
    void setType(FolderType value);

} // IFolder
