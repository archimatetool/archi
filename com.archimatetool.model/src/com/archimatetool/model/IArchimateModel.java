/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import java.io.File;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getPurpose <em>Purpose</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getFile <em>File</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getVersion <em>Version</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getArchimateModel()
 * @model extendedMetaData="name='model'"
 * @generated
 */
public interface IArchimateModel extends IFolderContainer, INameable, IIdentifier, IArchimateModelElement, IProperties {
    /**
     * Returns the value of the '<em><b>Purpose</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Purpose</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Purpose</em>' attribute.
     * @see #setPurpose(String)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateModel_Purpose()
     * @model extendedMetaData="kind='element'"
     * @generated
     */
    String getPurpose();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateModel#getPurpose <em>Purpose</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Purpose</em>' attribute.
     * @see #getPurpose()
     * @generated
     */
    void setPurpose(String value);

    /**
     * Returns the value of the '<em><b>File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File</em>' attribute.
     * @see #setFile(File)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateModel_File()
     * @model dataType="com.archimatetool.model.File" transient="true"
     * @generated
     */
    File getFile();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateModel#getFile <em>File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File</em>' attribute.
     * @see #getFile()
     * @generated
     */
    void setFile(File value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateModel_Version()
     * @model default=""
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateModel#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference.
     * @see #setMetadata(IMetadata)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateModel_Metadata()
     * @model containment="true"
     * @generated
     */
    IMetadata getMetadata();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IArchimateModel#getMetadata <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata</em>' containment reference.
     * @see #getMetadata()
     * @generated
     */
    void setMetadata(IMetadata value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void setDefaults();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    IFolder addDerivedRelationsFolder();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void removeDerivedRelationsFolder();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model elementRequired="true"
     * @generated
     */
    IFolder getDefaultFolderForElement(EObject element);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    IDiagramModel getDefaultDiagramModel();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    EList<IDiagramModel> getDiagramModels();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    IFolder getFolder(FolderType type);

} // IArchimateModel
