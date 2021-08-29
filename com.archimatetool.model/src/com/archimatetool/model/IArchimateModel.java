/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import java.io.File;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.util.IModelContentListener;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getPurpose <em>Purpose</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getFile <em>File</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getVersion <em>Version</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.archimatetool.model.IArchimateModel#getProfiles <em>Profiles</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getArchimateModel()
 * @model extendedMetaData="name='model'"
 * @generated
 */
public interface IArchimateModel extends IFolderContainer, IArchimateModelObject, IProperties {
    /**
     * Returns the value of the '<em><b>Purpose</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Purpose</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Purpose</em>' attribute.
     * @see #setPurpose(String)
     * @see com.archimatetool.model.IArchimatePackage#getArchimateModel_Purpose()
     * @model default=""
     *        extendedMetaData="kind='element'"
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
     * @deprecated Use getFeatures() instead
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
     * @deprecated Use getFeatures().setFeature() instead
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata</em>' containment reference.
     * @see #getMetadata()
     * @generated
     */
    void setMetadata(IMetadata value);

    /**
     * Returns the value of the '<em><b>Profiles</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IProfile}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Profiles</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getArchimateModel_Profiles()
     * @model containment="true"
     *        extendedMetaData="name='profile' kind='element'"
     * @generated
     */
    EList<IProfile> getProfiles();

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
     * @model objectRequired="true"
     * @generated
     */
    IFolder getDefaultFolderForObject(EObject object);

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
    
    /**
     * Add ecore listener for notifications in the whole model
     * @param listener
     */
    boolean addModelContentListener(IModelContentListener listener);
    
    /**
     * Remove ecore listener for whole model
     * @param listener
     */
    boolean removeModelContentListener(IModelContentListener listener);
    
    /**
     * Dispose of this model to free memory
     */
    void dispose();

} // IArchimateModel
