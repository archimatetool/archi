/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.archimatetool.model.IArchimateFactory
 * @model kind="package"
 * @generated
 */
public interface IArchimatePackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "model"; //$NON-NLS-1$

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.archimatetool.com/archimate"; //$NON-NLS-1$

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "archimate"; //$NON-NLS-1$

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    IArchimatePackage eINSTANCE = com.archimatetool.model.impl.ArchimatePackage.init();

    /**
     * The meta object id for the '{@link com.archimatetool.model.IAdapter <em>Adapter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IAdapter
     * @see com.archimatetool.model.impl.ArchimatePackage#getAdapter()
     * @generated
     */
    int ADAPTER = 0;

    /**
     * The number of structural features of the '<em>Adapter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADAPTER_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IIdentifier <em>Identifier</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IIdentifier
     * @see com.archimatetool.model.impl.ArchimatePackage#getIdentifier()
     * @generated
     */
    int IDENTIFIER = 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIER__ID = 0;

    /**
     * The number of structural features of the '<em>Identifier</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIER_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IProperties <em>Properties</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IProperties
     * @see com.archimatetool.model.impl.ArchimatePackage#getProperties()
     * @generated
     */
    int PROPERTIES = 3;

    /**
     * The meta object id for the '{@link com.archimatetool.model.INameable <em>Nameable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.INameable
     * @see com.archimatetool.model.impl.ArchimatePackage#getNameable()
     * @generated
     */
    int NAMEABLE = 7;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ITextContent <em>Text Content</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ITextContent
     * @see com.archimatetool.model.impl.ArchimatePackage#getTextContent()
     * @generated
     */
    int TEXT_CONTENT = 8;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IDocumentable <em>Documentable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IDocumentable
     * @see com.archimatetool.model.impl.ArchimatePackage#getDocumentable()
     * @generated
     */
    int DOCUMENTABLE = 9;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ICloneable <em>Cloneable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ICloneable
     * @see com.archimatetool.model.impl.ArchimatePackage#getCloneable()
     * @generated
     */
    int CLONEABLE = 10;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IFolderContainer <em>Folder Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IFolderContainer
     * @see com.archimatetool.model.impl.ArchimatePackage#getFolderContainer()
     * @generated
     */
    int FOLDER_CONTAINER = 13;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ArchimateModel <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ArchimateModel
     * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateModel()
     * @generated
     */
    int ARCHIMATE_MODEL = 37;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Folder <em>Folder</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Folder
     * @see com.archimatetool.model.impl.ArchimatePackage#getFolder()
     * @generated
     */
    int FOLDER = 14;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Property <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Property
     * @see com.archimatetool.model.impl.ArchimatePackage#getProperty()
     * @generated
     */
    int PROPERTY = 2;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__VALUE = 1;

    /**
     * The number of structural features of the '<em>Property</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTIES__PROPERTIES = 0;

    /**
     * The number of structural features of the '<em>Properties</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTIES_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Feature <em>Feature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Feature
     * @see com.archimatetool.model.impl.ArchimatePackage#getFeature()
     * @generated
     */
    int FEATURE = 4;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE__NAME = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE__VALUE = 1;

    /**
     * The number of structural features of the '<em>Feature</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IFeatures <em>Features</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IFeatures
     * @see com.archimatetool.model.impl.ArchimatePackage#getFeatures()
     * @generated
     */
    int FEATURES = 5;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURES__FEATURES = 0;

    /**
     * The number of structural features of the '<em>Features</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURES_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Metadata <em>Metadata</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Metadata
     * @see com.archimatetool.model.impl.ArchimatePackage#getMetadata()
     * @generated
     */
    int METADATA = 6;

    /**
     * The feature id for the '<em><b>Entries</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int METADATA__ENTRIES = 0;

    /**
     * The number of structural features of the '<em>Metadata</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int METADATA_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NAMEABLE__NAME = 0;

    /**
     * The number of structural features of the '<em>Nameable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NAMEABLE_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_CONTENT__CONTENT = 0;

    /**
     * The number of structural features of the '<em>Text Content</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_CONTENT_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTABLE__DOCUMENTATION = 0;

    /**
     * The number of structural features of the '<em>Documentable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTABLE_FEATURE_COUNT = 1;

    /**
     * The number of structural features of the '<em>Cloneable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CLONEABLE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Profile <em>Profile</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Profile
     * @see com.archimatetool.model.impl.ArchimatePackage#getProfile()
     * @generated
     */
    int PROFILE = 11;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IArchimateModelObject <em>Model Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IArchimateModelObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateModelObject()
     * @generated
     */
    int ARCHIMATE_MODEL_OBJECT = 15;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_OBJECT__NAME = ADAPTER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_OBJECT__ID = ADAPTER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_OBJECT__FEATURES = ADAPTER_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Model Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT = ADAPTER_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE__NAME = ARCHIMATE_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE__ID = ARCHIMATE_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE__FEATURES = ARCHIMATE_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE__IMAGE_PATH = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Specialization</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE__SPECIALIZATION = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Concept Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE__CONCEPT_TYPE = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Profile</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILE_FEATURE_COUNT = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IProfiles <em>Profiles</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IProfiles
     * @see com.archimatetool.model.impl.ArchimatePackage#getProfiles()
     * @generated
     */
    int PROFILES = 12;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILES__PROFILES = 0;

    /**
     * The number of structural features of the '<em>Profiles</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROFILES_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Folders</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_CONTAINER__FOLDERS = 0;

    /**
     * The number of structural features of the '<em>Folder Container</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_CONTAINER_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__NAME = ARCHIMATE_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__ID = ARCHIMATE_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__FEATURES = ARCHIMATE_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Folders</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__FOLDERS = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__DOCUMENTATION = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__PROPERTIES = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__ELEMENTS = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__TYPE = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Folder</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_FEATURE_COUNT = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ArchimateConcept <em>Concept</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ArchimateConcept
     * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateConcept()
     * @generated
     */
    int ARCHIMATE_CONCEPT = 16;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT__NAME = ARCHIMATE_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT__ID = ARCHIMATE_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT__FEATURES = ARCHIMATE_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT__DOCUMENTATION = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT__PROPERTIES = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT__PROFILES = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Concept</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_CONCEPT_FEATURE_COUNT = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ArchimateElement <em>Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ArchimateElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateElement()
     * @generated
     */
    int ARCHIMATE_ELEMENT = 17;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__NAME = ARCHIMATE_CONCEPT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__ID = ARCHIMATE_CONCEPT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__FEATURES = ARCHIMATE_CONCEPT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__DOCUMENTATION = ARCHIMATE_CONCEPT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__PROPERTIES = ARCHIMATE_CONCEPT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__PROFILES = ARCHIMATE_CONCEPT__PROFILES;

    /**
     * The number of structural features of the '<em>Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT_FEATURE_COUNT = ARCHIMATE_CONCEPT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IBusinessElement <em>Business Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IBusinessElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessElement()
     * @generated
     */
    int BUSINESS_ELEMENT = 20;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IApplicationElement <em>Application Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IApplicationElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationElement()
     * @generated
     */
    int APPLICATION_ELEMENT = 21;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ITechnologyElement <em>Technology Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ITechnologyElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyElement()
     * @generated
     */
    int TECHNOLOGY_ELEMENT = 22;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IPhysicalElement <em>Physical Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IPhysicalElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getPhysicalElement()
     * @generated
     */
    int PHYSICAL_ELEMENT = 24;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.AccessRelationship <em>Access Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.AccessRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getAccessRelationship()
     * @generated
     */
    int ACCESS_RELATIONSHIP = 99;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.AggregationRelationship <em>Aggregation Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.AggregationRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getAggregationRelationship()
     * @generated
     */
    int AGGREGATION_RELATIONSHIP = 100;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.AssignmentRelationship <em>Assignment Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.AssignmentRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getAssignmentRelationship()
     * @generated
     */
    int ASSIGNMENT_RELATIONSHIP = 101;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.AssociationRelationship <em>Association Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.AssociationRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getAssociationRelationship()
     * @generated
     */
    int ASSOCIATION_RELATIONSHIP = 102;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.CompositionRelationship <em>Composition Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.CompositionRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getCompositionRelationship()
     * @generated
     */
    int COMPOSITION_RELATIONSHIP = 103;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.FlowRelationship <em>Flow Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.FlowRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getFlowRelationship()
     * @generated
     */
    int FLOW_RELATIONSHIP = 104;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TriggeringRelationship <em>Triggering Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TriggeringRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getTriggeringRelationship()
     * @generated
     */
    int TRIGGERING_RELATIONSHIP = 109;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.InfluenceRelationship <em>Influence Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.InfluenceRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getInfluenceRelationship()
     * @generated
     */
    int INFLUENCE_RELATIONSHIP = 105;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessActor <em>Business Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessActor
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessActor()
     * @generated
     */
    int BUSINESS_ACTOR = 49;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessCollaboration <em>Business Collaboration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessCollaboration
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessCollaboration()
     * @generated
     */
    int BUSINESS_COLLABORATION = 50;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Contract <em>Contract</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Contract
     * @see com.archimatetool.model.impl.ArchimatePackage#getContract()
     * @generated
     */
    int CONTRACT = 61;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessEvent <em>Business Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessEvent
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessEvent()
     * @generated
     */
    int BUSINESS_EVENT = 51;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessFunction <em>Business Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessFunction
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessFunction()
     * @generated
     */
    int BUSINESS_FUNCTION = 52;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessInteraction <em>Business Interaction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessInteraction
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessInteraction()
     * @generated
     */
    int BUSINESS_INTERACTION = 53;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessInterface <em>Business Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessInterface
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessInterface()
     * @generated
     */
    int BUSINESS_INTERFACE = 54;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Meaning <em>Meaning</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Meaning
     * @see com.archimatetool.model.impl.ArchimatePackage#getMeaning()
     * @generated
     */
    int MEANING = 77;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessObject <em>Business Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessObject()
     * @generated
     */
    int BUSINESS_OBJECT = 55;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessProcess <em>Business Process</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessProcess
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessProcess()
     * @generated
     */
    int BUSINESS_PROCESS = 56;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Product <em>Product</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Product
     * @see com.archimatetool.model.impl.ArchimatePackage#getProduct()
     * @generated
     */
    int PRODUCT = 83;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Representation <em>Representation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Representation
     * @see com.archimatetool.model.impl.ArchimatePackage#getRepresentation()
     * @generated
     */
    int REPRESENTATION = 84;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessRole <em>Business Role</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessRole
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessRole()
     * @generated
     */
    int BUSINESS_ROLE = 57;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.BusinessService <em>Business Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.BusinessService
     * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessService()
     * @generated
     */
    int BUSINESS_SERVICE = 58;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Value <em>Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Value
     * @see com.archimatetool.model.impl.ArchimatePackage#getValue()
     * @generated
     */
    int VALUE = 96;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Location <em>Location</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Location
     * @see com.archimatetool.model.impl.ArchimatePackage#getLocation()
     * @generated
     */
    int LOCATION = 75;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationCollaboration <em>Application Collaboration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationCollaboration
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationCollaboration()
     * @generated
     */
    int APPLICATION_COLLABORATION = 39;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationComponent <em>Application Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationComponent
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationComponent()
     * @generated
     */
    int APPLICATION_COMPONENT = 40;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationFunction <em>Application Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationFunction
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationFunction()
     * @generated
     */
    int APPLICATION_FUNCTION = 42;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationInteraction <em>Application Interaction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationInteraction
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationInteraction()
     * @generated
     */
    int APPLICATION_INTERACTION = 43;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationInterface <em>Application Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationInterface
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationInterface()
     * @generated
     */
    int APPLICATION_INTERFACE = 44;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DataObject <em>Data Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DataObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getDataObject()
     * @generated
     */
    int DATA_OBJECT = 64;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationService <em>Application Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationService
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationService()
     * @generated
     */
    int APPLICATION_SERVICE = 46;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Artifact <em>Artifact</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Artifact
     * @see com.archimatetool.model.impl.ArchimatePackage#getArtifact()
     * @generated
     */
    int ARTIFACT = 47;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Node <em>Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Node
     * @see com.archimatetool.model.impl.ArchimatePackage#getNode()
     * @generated
     */
    int NODE = 78;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.SystemSoftware <em>System Software</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.SystemSoftware
     * @see com.archimatetool.model.impl.ArchimatePackage#getSystemSoftware()
     * @generated
     */
    int SYSTEM_SOFTWARE = 88;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Device <em>Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Device
     * @see com.archimatetool.model.impl.ArchimatePackage#getDevice()
     * @generated
     */
    int DEVICE = 66;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IMotivationElement <em>Motivation Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IMotivationElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getMotivationElement()
     * @generated
     */
    int MOTIVATION_ELEMENT = 25;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Stakeholder <em>Stakeholder</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Stakeholder
     * @see com.archimatetool.model.impl.ArchimatePackage#getStakeholder()
     * @generated
     */
    int STAKEHOLDER = 87;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Driver <em>Driver</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Driver
     * @see com.archimatetool.model.impl.ArchimatePackage#getDriver()
     * @generated
     */
    int DRIVER = 68;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Assessment <em>Assessment</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Assessment
     * @see com.archimatetool.model.impl.ArchimatePackage#getAssessment()
     * @generated
     */
    int ASSESSMENT = 48;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Goal <em>Goal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Goal
     * @see com.archimatetool.model.impl.ArchimatePackage#getGoal()
     * @generated
     */
    int GOAL = 72;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Requirement <em>Requirement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Requirement
     * @see com.archimatetool.model.impl.ArchimatePackage#getRequirement()
     * @generated
     */
    int REQUIREMENT = 86;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Constraint <em>Constraint</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Constraint
     * @see com.archimatetool.model.impl.ArchimatePackage#getConstraint()
     * @generated
     */
    int CONSTRAINT = 62;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Principle <em>Principle</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Principle
     * @see com.archimatetool.model.impl.ArchimatePackage#getPrinciple()
     * @generated
     */
    int PRINCIPLE = 82;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IImplementationMigrationElement <em>Implementation Migration Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IImplementationMigrationElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getImplementationMigrationElement()
     * @generated
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT = 26;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IStrategyElement <em>Strategy Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IStrategyElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getStrategyElement()
     * @generated
     */
    int STRATEGY_ELEMENT = 19;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.WorkPackage <em>Work Package</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.WorkPackage
     * @see com.archimatetool.model.impl.ArchimatePackage#getWorkPackage()
     * @generated
     */
    int WORK_PACKAGE = 98;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Deliverable <em>Deliverable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Deliverable
     * @see com.archimatetool.model.impl.ArchimatePackage#getDeliverable()
     * @generated
     */
    int DELIVERABLE = 65;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Plateau <em>Plateau</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Plateau
     * @see com.archimatetool.model.impl.ArchimatePackage#getPlateau()
     * @generated
     */
    int PLATEAU = 81;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Gap <em>Gap</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Gap
     * @see com.archimatetool.model.impl.ArchimatePackage#getGap()
     * @generated
     */
    int GAP = 71;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ArchimateRelationship <em>Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ArchimateRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateRelationship()
     * @generated
     */
    int ARCHIMATE_RELATIONSHIP = 18;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__NAME = ARCHIMATE_CONCEPT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__ID = ARCHIMATE_CONCEPT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__FEATURES = ARCHIMATE_CONCEPT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__DOCUMENTATION = ARCHIMATE_CONCEPT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__PROPERTIES = ARCHIMATE_CONCEPT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__PROFILES = ARCHIMATE_CONCEPT__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__SOURCE = ARCHIMATE_CONCEPT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP__TARGET = ARCHIMATE_CONCEPT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_RELATIONSHIP_FEATURE_COUNT = ARCHIMATE_CONCEPT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ICompositeElement <em>Composite Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ICompositeElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getCompositeElement()
     * @generated
     */
    int COMPOSITE_ELEMENT = 27;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Strategy Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IBehaviorElement <em>Behavior Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IBehaviorElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getBehaviorElement()
     * @generated
     */
    int BEHAVIOR_ELEMENT = 28;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IStructureElement <em>Structure Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IStructureElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getStructureElement()
     * @generated
     */
    int STRUCTURE_ELEMENT = 30;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IActiveStructureElement <em>Active Structure Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IActiveStructureElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getActiveStructureElement()
     * @generated
     */
    int ACTIVE_STRUCTURE_ELEMENT = 31;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IPassiveStructureElement <em>Passive Structure Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IPassiveStructureElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getPassiveStructureElement()
     * @generated
     */
    int PASSIVE_STRUCTURE_ELEMENT = 32;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IStructuralRelationship <em>Structural Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IStructuralRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getStructuralRelationship()
     * @generated
     */
    int STRUCTURAL_RELATIONSHIP = 33;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IDependendencyRelationship <em>Dependendency Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IDependendencyRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getDependendencyRelationship()
     * @generated
     */
    int DEPENDENDENCY_RELATIONSHIP = 34;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IDynamicRelationship <em>Dynamic Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IDynamicRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getDynamicRelationship()
     * @generated
     */
    int DYNAMIC_RELATIONSHIP = 35;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IOtherRelationship <em>Other Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IOtherRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getOtherRelationship()
     * @generated
     */
    int OTHER_RELATIONSHIP = 36;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Resource <em>Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Resource
     * @see com.archimatetool.model.impl.ArchimatePackage#getResource()
     * @generated
     */
    int RESOURCE = 85;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Capability <em>Capability</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Capability
     * @see com.archimatetool.model.impl.ArchimatePackage#getCapability()
     * @generated
     */
    int CAPABILITY = 59;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationEvent <em>Application Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationEvent
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationEvent()
     * @generated
     */
    int APPLICATION_EVENT = 41;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ApplicationProcess <em>Application Process</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ApplicationProcess
     * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationProcess()
     * @generated
     */
    int APPLICATION_PROCESS = 45;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Outcome <em>Outcome</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Outcome
     * @see com.archimatetool.model.impl.ArchimatePackage#getOutcome()
     * @generated
     */
    int OUTCOME = 79;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Equipment <em>Equipment</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Equipment
     * @see com.archimatetool.model.impl.ArchimatePackage#getEquipment()
     * @generated
     */
    int EQUIPMENT = 69;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.CommunicationNetwork <em>Communication Network</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.CommunicationNetwork
     * @see com.archimatetool.model.impl.ArchimatePackage#getCommunicationNetwork()
     * @generated
     */
    int COMMUNICATION_NETWORK = 60;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.CourseOfAction <em>Course Of Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.CourseOfAction
     * @see com.archimatetool.model.impl.ArchimatePackage#getCourseOfAction()
     * @generated
     */
    int COURSE_OF_ACTION = 63;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Grouping <em>Grouping</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Grouping
     * @see com.archimatetool.model.impl.ArchimatePackage#getGrouping()
     * @generated
     */
    int GROUPING = 73;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Path <em>Path</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Path
     * @see com.archimatetool.model.impl.ArchimatePackage#getPath()
     * @generated
     */
    int PATH = 80;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyCollaboration <em>Technology Collaboration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyCollaboration
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyCollaboration()
     * @generated
     */
    int TECHNOLOGY_COLLABORATION = 89;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyEvent <em>Technology Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyEvent
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyEvent()
     * @generated
     */
    int TECHNOLOGY_EVENT = 90;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyFunction <em>Technology Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyFunction
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyFunction()
     * @generated
     */
    int TECHNOLOGY_FUNCTION = 91;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyInterface <em>Technology Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyInterface
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyInterface()
     * @generated
     */
    int TECHNOLOGY_INTERFACE = 92;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyInteraction <em>Technology Interaction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyInteraction
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyInteraction()
     * @generated
     */
    int TECHNOLOGY_INTERACTION = 93;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ITechnologyObject <em>Technology Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ITechnologyObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyObject()
     * @generated
     */
    int TECHNOLOGY_OBJECT = 23;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_OBJECT_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Physical Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHYSICAL_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Motivation Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MOTIVATION_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Implementation Migration Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_MIGRATION_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Composite Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Behavior Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOR_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IStrategyBehaviorElement <em>Strategy Behavior Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IStrategyBehaviorElement
     * @see com.archimatetool.model.impl.ArchimatePackage#getStrategyBehaviorElement()
     * @generated
     */
    int STRATEGY_BEHAVIOR_ELEMENT = 29;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT__NAME = BEHAVIOR_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT__ID = BEHAVIOR_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT__FEATURES = BEHAVIOR_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT__DOCUMENTATION = BEHAVIOR_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT__PROPERTIES = BEHAVIOR_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT__PROFILES = BEHAVIOR_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Strategy Behavior Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRATEGY_BEHAVIOR_ELEMENT_FEATURE_COUNT = BEHAVIOR_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Structure Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURE_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT__NAME = STRUCTURE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT__ID = STRUCTURE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT__FEATURES = STRUCTURE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT__DOCUMENTATION = STRUCTURE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT__PROPERTIES = STRUCTURE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT__PROFILES = STRUCTURE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Active Structure Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTIVE_STRUCTURE_ELEMENT_FEATURE_COUNT = STRUCTURE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT__NAME = STRUCTURE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT__ID = STRUCTURE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT__FEATURES = STRUCTURE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT__DOCUMENTATION = STRUCTURE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT__PROPERTIES = STRUCTURE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT__PROFILES = STRUCTURE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Passive Structure Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASSIVE_STRUCTURE_ELEMENT_FEATURE_COUNT = STRUCTURE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__NAME = ARCHIMATE_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__ID = ARCHIMATE_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__FEATURES = ARCHIMATE_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__DOCUMENTATION = ARCHIMATE_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__PROPERTIES = ARCHIMATE_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__PROFILES = ARCHIMATE_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__SOURCE = ARCHIMATE_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP__TARGET = ARCHIMATE_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Structural Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRUCTURAL_RELATIONSHIP_FEATURE_COUNT = ARCHIMATE_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__NAME = ARCHIMATE_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__ID = ARCHIMATE_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__FEATURES = ARCHIMATE_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__DOCUMENTATION = ARCHIMATE_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__PROPERTIES = ARCHIMATE_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__PROFILES = ARCHIMATE_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__SOURCE = ARCHIMATE_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP__TARGET = ARCHIMATE_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Dependendency Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT = ARCHIMATE_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__NAME = ARCHIMATE_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__ID = ARCHIMATE_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__FEATURES = ARCHIMATE_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__DOCUMENTATION = ARCHIMATE_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__PROPERTIES = ARCHIMATE_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__PROFILES = ARCHIMATE_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__SOURCE = ARCHIMATE_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP__TARGET = ARCHIMATE_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Dynamic Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DYNAMIC_RELATIONSHIP_FEATURE_COUNT = ARCHIMATE_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__NAME = ARCHIMATE_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__ID = ARCHIMATE_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__FEATURES = ARCHIMATE_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__DOCUMENTATION = ARCHIMATE_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__PROPERTIES = ARCHIMATE_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__PROFILES = ARCHIMATE_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__SOURCE = ARCHIMATE_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP__TARGET = ARCHIMATE_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Other Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_RELATIONSHIP_FEATURE_COUNT = ARCHIMATE_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Folders</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__FOLDERS = FOLDER_CONTAINER__FOLDERS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__NAME = FOLDER_CONTAINER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__ID = FOLDER_CONTAINER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__FEATURES = FOLDER_CONTAINER_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__PROPERTIES = FOLDER_CONTAINER_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Purpose</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__PURPOSE = FOLDER_CONTAINER_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__FILE = FOLDER_CONTAINER_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__VERSION = FOLDER_CONTAINER_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__METADATA = FOLDER_CONTAINER_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__PROFILES = FOLDER_CONTAINER_FEATURE_COUNT + 8;

    /**
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_FEATURE_COUNT = FOLDER_CONTAINER_FEATURE_COUNT + 9;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Junction <em>Junction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Junction
     * @see com.archimatetool.model.impl.ArchimatePackage#getJunction()
     * @generated
     */
    int JUNCTION = 38;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__FEATURES = ARCHIMATE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__PROFILES = ARCHIMATE_ELEMENT__PROFILES;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__TYPE = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Junction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Collaboration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_EVENT_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Interaction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Process</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_PROCESS_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Application Service</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__NAME = TECHNOLOGY_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__ID = TECHNOLOGY_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__FEATURES = TECHNOLOGY_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__DOCUMENTATION = TECHNOLOGY_OBJECT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__PROPERTIES = TECHNOLOGY_OBJECT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__PROFILES = TECHNOLOGY_OBJECT__PROFILES;

    /**
     * The number of structural features of the '<em>Artifact</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT_FEATURE_COUNT = TECHNOLOGY_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Assessment</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSESSMENT_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Actor</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Collaboration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Interaction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Process</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Role</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Business Service</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY__NAME = STRATEGY_BEHAVIOR_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY__ID = STRATEGY_BEHAVIOR_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY__FEATURES = STRATEGY_BEHAVIOR_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY__DOCUMENTATION = STRATEGY_BEHAVIOR_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY__PROPERTIES = STRATEGY_BEHAVIOR_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY__PROFILES = STRATEGY_BEHAVIOR_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Capability</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITY_FEATURE_COUNT = STRATEGY_BEHAVIOR_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Communication Network</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_NETWORK_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__NAME = BUSINESS_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__ID = BUSINESS_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__FEATURES = BUSINESS_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__DOCUMENTATION = BUSINESS_OBJECT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__PROPERTIES = BUSINESS_OBJECT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__PROFILES = BUSINESS_OBJECT__PROFILES;

    /**
     * The number of structural features of the '<em>Contract</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT_FEATURE_COUNT = BUSINESS_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Constraint</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTRAINT_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION__NAME = STRATEGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION__ID = STRATEGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION__FEATURES = STRATEGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION__DOCUMENTATION = STRATEGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION__PROPERTIES = STRATEGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION__PROFILES = STRATEGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Course Of Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COURSE_OF_ACTION_FEATURE_COUNT = STRATEGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__NAME = APPLICATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__ID = APPLICATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__FEATURES = APPLICATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__DOCUMENTATION = APPLICATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__PROPERTIES = APPLICATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__PROFILES = APPLICATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Data Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT_FEATURE_COUNT = APPLICATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE__NAME = IMPLEMENTATION_MIGRATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE__ID = IMPLEMENTATION_MIGRATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE__FEATURES = IMPLEMENTATION_MIGRATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE__DOCUMENTATION = IMPLEMENTATION_MIGRATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE__PROPERTIES = IMPLEMENTATION_MIGRATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE__PROFILES = IMPLEMENTATION_MIGRATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Deliverable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIVERABLE_FEATURE_COUNT = IMPLEMENTATION_MIGRATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Device</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DistributionNetwork <em>Distribution Network</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DistributionNetwork
     * @see com.archimatetool.model.impl.ArchimatePackage#getDistributionNetwork()
     * @generated
     */
    int DISTRIBUTION_NETWORK = 67;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK__NAME = PHYSICAL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK__ID = PHYSICAL_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK__FEATURES = PHYSICAL_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK__DOCUMENTATION = PHYSICAL_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK__PROPERTIES = PHYSICAL_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK__PROFILES = PHYSICAL_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Distribution Network</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_NETWORK_FEATURE_COUNT = PHYSICAL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Driver</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DRIVER_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT__NAME = PHYSICAL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT__ID = PHYSICAL_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT__FEATURES = PHYSICAL_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT__DOCUMENTATION = PHYSICAL_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT__PROPERTIES = PHYSICAL_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT__PROFILES = PHYSICAL_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Equipment</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUIPMENT_FEATURE_COUNT = PHYSICAL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Facility <em>Facility</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Facility
     * @see com.archimatetool.model.impl.ArchimatePackage#getFacility()
     * @generated
     */
    int FACILITY = 70;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY__NAME = PHYSICAL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY__ID = PHYSICAL_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY__FEATURES = PHYSICAL_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY__DOCUMENTATION = PHYSICAL_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY__PROPERTIES = PHYSICAL_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY__PROFILES = PHYSICAL_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Facility</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FACILITY_FEATURE_COUNT = PHYSICAL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP__NAME = IMPLEMENTATION_MIGRATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP__ID = IMPLEMENTATION_MIGRATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP__FEATURES = IMPLEMENTATION_MIGRATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP__DOCUMENTATION = IMPLEMENTATION_MIGRATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP__PROPERTIES = IMPLEMENTATION_MIGRATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP__PROFILES = IMPLEMENTATION_MIGRATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Gap</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GAP_FEATURE_COUNT = IMPLEMENTATION_MIGRATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Goal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GOAL_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING__NAME = COMPOSITE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING__ID = COMPOSITE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING__FEATURES = COMPOSITE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING__DOCUMENTATION = COMPOSITE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING__PROPERTIES = COMPOSITE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING__PROFILES = COMPOSITE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Grouping</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUPING_FEATURE_COUNT = COMPOSITE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ImplementationEvent <em>Implementation Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ImplementationEvent
     * @see com.archimatetool.model.impl.ArchimatePackage#getImplementationEvent()
     * @generated
     */
    int IMPLEMENTATION_EVENT = 74;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT__NAME = IMPLEMENTATION_MIGRATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT__ID = IMPLEMENTATION_MIGRATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT__FEATURES = IMPLEMENTATION_MIGRATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT__DOCUMENTATION = IMPLEMENTATION_MIGRATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT__PROPERTIES = IMPLEMENTATION_MIGRATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT__PROFILES = IMPLEMENTATION_MIGRATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Implementation Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLEMENTATION_EVENT_FEATURE_COUNT = IMPLEMENTATION_MIGRATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__NAME = COMPOSITE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__ID = COMPOSITE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__FEATURES = COMPOSITE_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__DOCUMENTATION = COMPOSITE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__PROPERTIES = COMPOSITE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__PROFILES = COMPOSITE_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Location</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_FEATURE_COUNT = COMPOSITE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Material <em>Material</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Material
     * @see com.archimatetool.model.impl.ArchimatePackage#getMaterial()
     * @generated
     */
    int MATERIAL = 76;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL__NAME = PHYSICAL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL__ID = PHYSICAL_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL__FEATURES = PHYSICAL_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL__DOCUMENTATION = PHYSICAL_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL__PROPERTIES = PHYSICAL_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL__PROFILES = PHYSICAL_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Material</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATERIAL_FEATURE_COUNT = PHYSICAL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Meaning</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Outcome</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTCOME_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Path</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU__NAME = IMPLEMENTATION_MIGRATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU__ID = IMPLEMENTATION_MIGRATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU__FEATURES = IMPLEMENTATION_MIGRATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU__DOCUMENTATION = IMPLEMENTATION_MIGRATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU__PROPERTIES = IMPLEMENTATION_MIGRATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU__PROFILES = IMPLEMENTATION_MIGRATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Plateau</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLATEAU_FEATURE_COUNT = IMPLEMENTATION_MIGRATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Principle</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINCIPLE_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Product</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__NAME = BUSINESS_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__ID = BUSINESS_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__FEATURES = BUSINESS_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__DOCUMENTATION = BUSINESS_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__PROPERTIES = BUSINESS_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__PROFILES = BUSINESS_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Representation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION_FEATURE_COUNT = BUSINESS_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE__NAME = STRATEGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE__ID = STRATEGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE__FEATURES = STRATEGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE__DOCUMENTATION = STRATEGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE__PROPERTIES = STRATEGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE__PROFILES = STRATEGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Resource</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_FEATURE_COUNT = STRATEGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Requirement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Stakeholder</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STAKEHOLDER_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>System Software</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Collaboration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_COLLABORATION_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_EVENT_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_FUNCTION_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERFACE_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Interaction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_INTERACTION_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyProcess <em>Technology Process</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyProcess
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyProcess()
     * @generated
     */
    int TECHNOLOGY_PROCESS = 94;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Process</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_PROCESS_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.TechnologyService <em>Technology Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.TechnologyService
     * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyService()
     * @generated
     */
    int TECHNOLOGY_SERVICE = 95;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE__NAME = TECHNOLOGY_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE__ID = TECHNOLOGY_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE__FEATURES = TECHNOLOGY_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE__DOCUMENTATION = TECHNOLOGY_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE__PROPERTIES = TECHNOLOGY_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE__PROFILES = TECHNOLOGY_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Technology Service</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_SERVICE_FEATURE_COUNT = TECHNOLOGY_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__NAME = MOTIVATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__ID = MOTIVATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__FEATURES = MOTIVATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__DOCUMENTATION = MOTIVATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__PROPERTIES = MOTIVATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__PROFILES = MOTIVATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Value</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_FEATURE_COUNT = MOTIVATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ValueStream <em>Value Stream</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ValueStream
     * @see com.archimatetool.model.impl.ArchimatePackage#getValueStream()
     * @generated
     */
    int VALUE_STREAM = 97;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM__NAME = STRATEGY_BEHAVIOR_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM__ID = STRATEGY_BEHAVIOR_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM__FEATURES = STRATEGY_BEHAVIOR_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM__DOCUMENTATION = STRATEGY_BEHAVIOR_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM__PROPERTIES = STRATEGY_BEHAVIOR_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM__PROFILES = STRATEGY_BEHAVIOR_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Value Stream</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_STREAM_FEATURE_COUNT = STRATEGY_BEHAVIOR_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE__NAME = IMPLEMENTATION_MIGRATION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE__ID = IMPLEMENTATION_MIGRATION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE__FEATURES = IMPLEMENTATION_MIGRATION_ELEMENT__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE__DOCUMENTATION = IMPLEMENTATION_MIGRATION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE__PROPERTIES = IMPLEMENTATION_MIGRATION_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE__PROFILES = IMPLEMENTATION_MIGRATION_ELEMENT__PROFILES;

    /**
     * The number of structural features of the '<em>Work Package</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WORK_PACKAGE_FEATURE_COUNT = IMPLEMENTATION_MIGRATION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__NAME = DEPENDENDENCY_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__ID = DEPENDENDENCY_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__FEATURES = DEPENDENDENCY_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__DOCUMENTATION = DEPENDENDENCY_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__PROPERTIES = DEPENDENDENCY_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__PROFILES = DEPENDENDENCY_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__SOURCE = DEPENDENDENCY_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__TARGET = DEPENDENDENCY_RELATIONSHIP__TARGET;

    /**
     * The feature id for the '<em><b>Access Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__ACCESS_TYPE = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Access Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP_FEATURE_COUNT = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__NAME = STRUCTURAL_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__ID = STRUCTURAL_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__FEATURES = STRUCTURAL_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__DOCUMENTATION = STRUCTURAL_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__PROPERTIES = STRUCTURAL_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__PROFILES = STRUCTURAL_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__SOURCE = STRUCTURAL_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__TARGET = STRUCTURAL_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Aggregation Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP_FEATURE_COUNT = STRUCTURAL_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__NAME = STRUCTURAL_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__ID = STRUCTURAL_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__FEATURES = STRUCTURAL_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__DOCUMENTATION = STRUCTURAL_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__PROPERTIES = STRUCTURAL_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__PROFILES = STRUCTURAL_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__SOURCE = STRUCTURAL_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__TARGET = STRUCTURAL_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Assignment Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP_FEATURE_COUNT = STRUCTURAL_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__NAME = DEPENDENDENCY_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__ID = DEPENDENDENCY_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__FEATURES = DEPENDENDENCY_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__DOCUMENTATION = DEPENDENDENCY_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__PROPERTIES = DEPENDENDENCY_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__PROFILES = DEPENDENDENCY_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__SOURCE = DEPENDENDENCY_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__TARGET = DEPENDENDENCY_RELATIONSHIP__TARGET;

    /**
     * The feature id for the '<em><b>Directed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__DIRECTED = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Association Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP_FEATURE_COUNT = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__NAME = STRUCTURAL_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__ID = STRUCTURAL_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__FEATURES = STRUCTURAL_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__DOCUMENTATION = STRUCTURAL_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__PROPERTIES = STRUCTURAL_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__PROFILES = STRUCTURAL_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__SOURCE = STRUCTURAL_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__TARGET = STRUCTURAL_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Composition Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP_FEATURE_COUNT = STRUCTURAL_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__NAME = DYNAMIC_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__ID = DYNAMIC_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__FEATURES = DYNAMIC_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__DOCUMENTATION = DYNAMIC_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__PROPERTIES = DYNAMIC_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__PROFILES = DYNAMIC_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__SOURCE = DYNAMIC_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__TARGET = DYNAMIC_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Flow Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP_FEATURE_COUNT = DYNAMIC_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__NAME = DEPENDENDENCY_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__ID = DEPENDENDENCY_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__FEATURES = DEPENDENDENCY_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__DOCUMENTATION = DEPENDENDENCY_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__PROPERTIES = DEPENDENDENCY_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__PROFILES = DEPENDENDENCY_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__SOURCE = DEPENDENDENCY_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__TARGET = DEPENDENDENCY_RELATIONSHIP__TARGET;

    /**
     * The feature id for the '<em><b>Strength</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP__STRENGTH = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Influence Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFLUENCE_RELATIONSHIP_FEATURE_COUNT = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.RealizationRelationship <em>Realization Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.RealizationRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getRealizationRelationship()
     * @generated
     */
    int REALIZATION_RELATIONSHIP = 106;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__NAME = STRUCTURAL_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__ID = STRUCTURAL_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__FEATURES = STRUCTURAL_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__DOCUMENTATION = STRUCTURAL_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__PROPERTIES = STRUCTURAL_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__PROFILES = STRUCTURAL_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__SOURCE = STRUCTURAL_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP__TARGET = STRUCTURAL_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Realization Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALIZATION_RELATIONSHIP_FEATURE_COUNT = STRUCTURAL_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ServingRelationship <em>Serving Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ServingRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getServingRelationship()
     * @generated
     */
    int SERVING_RELATIONSHIP = 107;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__NAME = DEPENDENDENCY_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__ID = DEPENDENDENCY_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__FEATURES = DEPENDENDENCY_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__DOCUMENTATION = DEPENDENDENCY_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__PROPERTIES = DEPENDENDENCY_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__PROFILES = DEPENDENDENCY_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__SOURCE = DEPENDENDENCY_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP__TARGET = DEPENDENDENCY_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Serving Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVING_RELATIONSHIP_FEATURE_COUNT = DEPENDENDENCY_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.SpecializationRelationship <em>Specialization Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.SpecializationRelationship
     * @see com.archimatetool.model.impl.ArchimatePackage#getSpecializationRelationship()
     * @generated
     */
    int SPECIALIZATION_RELATIONSHIP = 108;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__NAME = OTHER_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__ID = OTHER_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__FEATURES = OTHER_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__DOCUMENTATION = OTHER_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__PROPERTIES = OTHER_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__PROFILES = OTHER_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__SOURCE = OTHER_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP__TARGET = OTHER_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Specialization Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALIZATION_RELATIONSHIP_FEATURE_COUNT = OTHER_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__NAME = DYNAMIC_RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__ID = DYNAMIC_RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__FEATURES = DYNAMIC_RELATIONSHIP__FEATURES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__DOCUMENTATION = DYNAMIC_RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__PROPERTIES = DYNAMIC_RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Profiles</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__PROFILES = DYNAMIC_RELATIONSHIP__PROFILES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__SOURCE = DYNAMIC_RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__TARGET = DYNAMIC_RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Triggering Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP_FEATURE_COUNT = DYNAMIC_RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModel <em>Diagram Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModel
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModel()
     * @generated
     */
    int DIAGRAM_MODEL = 113;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelComponent <em>Diagram Model Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelComponent
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelComponent()
     * @generated
     */
    int DIAGRAM_MODEL_COMPONENT = 110;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_COMPONENT__ID = IDENTIFIER__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_COMPONENT__NAME = IDENTIFIER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_COMPONENT__FEATURES = IDENTIFIER_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Diagram Model Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT = IDENTIFIER_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Connectable <em>Connectable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Connectable
     * @see com.archimatetool.model.impl.ArchimatePackage#getConnectable()
     * @generated
     */
    int CONNECTABLE = 111;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTABLE__ID = DIAGRAM_MODEL_COMPONENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTABLE__NAME = DIAGRAM_MODEL_COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTABLE__FEATURES = DIAGRAM_MODEL_COMPONENT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTABLE__SOURCE_CONNECTIONS = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTABLE__TARGET_CONNECTIONS = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Connectable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTABLE_FEATURE_COUNT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelObject <em>Diagram Model Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelObject()
     * @generated
     */
    int DIAGRAM_MODEL_OBJECT = 115;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelArchimateObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelArchimateObject()
     * @generated
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT = 133;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IDiagramModelContainer <em>Diagram Model Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IDiagramModelContainer
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelContainer()
     * @generated
     */
    int DIAGRAM_MODEL_CONTAINER = 112;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONTAINER__ID = DIAGRAM_MODEL_COMPONENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONTAINER__NAME = DIAGRAM_MODEL_COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONTAINER__FEATURES = DIAGRAM_MODEL_COMPONENT__FEATURES;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONTAINER__CHILDREN = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Diagram Model Container</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONTAINER_FEATURE_COUNT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__NAME = ARCHIMATE_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__ID = ARCHIMATE_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__FEATURES = ARCHIMATE_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__CHILDREN = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__DOCUMENTATION = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__PROPERTIES = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Connection Router Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Diagram Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_FEATURE_COUNT = ARCHIMATE_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.ArchimateDiagramModel <em>Diagram Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.ArchimateDiagramModel
     * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateDiagramModel()
     * @generated
     */
    int ARCHIMATE_DIAGRAM_MODEL = 131;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__ID = CONNECTABLE__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__NAME = CONNECTABLE__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FEATURES = CONNECTABLE__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS = CONNECTABLE__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS = CONNECTABLE__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FONT = CONNECTABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FONT_COLOR = CONNECTABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__LINE_WIDTH = CONNECTABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__LINE_COLOR = CONNECTABLE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT = CONNECTABLE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__BOUNDS = CONNECTABLE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FILL_COLOR = CONNECTABLE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__ALPHA = CONNECTABLE_FEATURE_COUNT + 7;

    /**
     * The number of structural features of the '<em>Diagram Model Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT_FEATURE_COUNT = CONNECTABLE_FEATURE_COUNT + 8;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelReference <em>Diagram Model Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelReference
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelReference()
     * @generated
     */
    int DIAGRAM_MODEL_REFERENCE = 114;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__TEXT_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__IMAGE_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Referenced Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Diagram Model Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelGroup <em>Diagram Model Group</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelGroup
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelGroup()
     * @generated
     */
    int DIAGRAM_MODEL_GROUP = 116;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__CHILDREN = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__DOCUMENTATION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__PROPERTIES = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__TEXT_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Border Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__BORDER_TYPE = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__IMAGE_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Diagram Model Group</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelNote <em>Diagram Model Note</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelNote
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelNote()
     * @generated
     */
    int DIAGRAM_MODEL_NOTE = 117;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__CONTENT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__TEXT_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__PROPERTIES = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Border Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__BORDER_TYPE = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__IMAGE_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Diagram Model Note</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelImage <em>Diagram Model Image</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelImage
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelImage()
     * @generated
     */
    int DIAGRAM_MODEL_IMAGE = 118;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__BORDER_COLOR = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__PROPERTIES = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE__DOCUMENTATION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Diagram Model Image</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelConnection <em>Diagram Model Connection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelConnection
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelConnection()
     * @generated
     */
    int DIAGRAM_MODEL_CONNECTION = 119;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__ID = CONNECTABLE__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__NAME = CONNECTABLE__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__FEATURES = CONNECTABLE__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__SOURCE_CONNECTIONS = CONNECTABLE__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TARGET_CONNECTIONS = CONNECTABLE__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__FONT = CONNECTABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__FONT_COLOR = CONNECTABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__PROPERTIES = CONNECTABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__DOCUMENTATION = CONNECTABLE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__LINE_WIDTH = CONNECTABLE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__LINE_COLOR = CONNECTABLE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TEXT = CONNECTABLE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TEXT_POSITION = CONNECTABLE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__SOURCE = CONNECTABLE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TARGET = CONNECTABLE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Bendpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__BENDPOINTS = CONNECTABLE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TYPE = CONNECTABLE_FEATURE_COUNT + 11;

    /**
     * The number of structural features of the '<em>Diagram Model Connection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT = CONNECTABLE_FEATURE_COUNT + 12;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelArchimateConnection
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelArchimateConnection()
     * @generated
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION = 134;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.DiagramModelBendpoint <em>Diagram Model Bendpoint</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.DiagramModelBendpoint
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelBendpoint()
     * @generated
     */
    int DIAGRAM_MODEL_BENDPOINT = 120;

    /**
     * The feature id for the '<em><b>Start X</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_BENDPOINT__START_X = CLONEABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Start Y</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_BENDPOINT__START_Y = CLONEABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>End X</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_BENDPOINT__END_X = CLONEABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>End Y</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_BENDPOINT__END_Y = CLONEABLE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Diagram Model Bendpoint</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_BENDPOINT_FEATURE_COUNT = CLONEABLE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ILineObject <em>Line Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ILineObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getLineObject()
     * @generated
     */
    int LINE_OBJECT = 121;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINE_OBJECT__LINE_WIDTH = 0;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINE_OBJECT__LINE_COLOR = 1;

    /**
     * The number of structural features of the '<em>Line Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINE_OBJECT_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IFontAttribute <em>Font Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IFontAttribute
     * @see com.archimatetool.model.impl.ArchimatePackage#getFontAttribute()
     * @generated
     */
    int FONT_ATTRIBUTE = 122;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FONT_ATTRIBUTE__FONT = 0;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FONT_ATTRIBUTE__FONT_COLOR = 1;

    /**
     * The number of structural features of the '<em>Font Attribute</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FONT_ATTRIBUTE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ITextPosition <em>Text Position</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ITextPosition
     * @see com.archimatetool.model.impl.ArchimatePackage#getTextPosition()
     * @generated
     */
    int TEXT_POSITION = 123;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_POSITION__TEXT_POSITION = 0;

    /**
     * The number of structural features of the '<em>Text Position</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_POSITION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ITextAlignment <em>Text Alignment</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ITextAlignment
     * @see com.archimatetool.model.impl.ArchimatePackage#getTextAlignment()
     * @generated
     */
    int TEXT_ALIGNMENT = 124;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ALIGNMENT__TEXT_ALIGNMENT = 0;

    /**
     * The number of structural features of the '<em>Text Alignment</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ALIGNMENT_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IBorderObject <em>Border Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IBorderObject
     * @see com.archimatetool.model.impl.ArchimatePackage#getBorderObject()
     * @generated
     */
    int BORDER_OBJECT = 125;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BORDER_OBJECT__BORDER_COLOR = 0;

    /**
     * The number of structural features of the '<em>Border Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BORDER_OBJECT_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IBorderType <em>Border Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IBorderType
     * @see com.archimatetool.model.impl.ArchimatePackage#getBorderType()
     * @generated
     */
    int BORDER_TYPE = 126;

    /**
     * The feature id for the '<em><b>Border Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BORDER_TYPE__BORDER_TYPE = 0;

    /**
     * The number of structural features of the '<em>Border Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BORDER_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IDiagramModelImageProvider <em>Diagram Model Image Provider</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IDiagramModelImageProvider
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelImageProvider()
     * @generated
     */
    int DIAGRAM_MODEL_IMAGE_PROVIDER = 127;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH = 0;

    /**
     * The number of structural features of the '<em>Diagram Model Image Provider</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_IMAGE_PROVIDER_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.Bounds <em>Bounds</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.Bounds
     * @see com.archimatetool.model.impl.ArchimatePackage#getBounds()
     * @generated
     */
    int BOUNDS = 128;

    /**
     * The feature id for the '<em><b>X</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDS__X = 0;

    /**
     * The feature id for the '<em><b>Y</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDS__Y = 1;

    /**
     * The feature id for the '<em><b>Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDS__WIDTH = 2;

    /**
     * The feature id for the '<em><b>Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDS__HEIGHT = 3;

    /**
     * The number of structural features of the '<em>Bounds</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDS_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link com.archimatetool.model.ILockable <em>Lockable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.ILockable
     * @see com.archimatetool.model.impl.ArchimatePackage#getLockable()
     * @generated
     */
    int LOCKABLE = 129;

    /**
     * The feature id for the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCKABLE__LOCKED = 0;

    /**
     * The number of structural features of the '<em>Lockable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCKABLE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IIconic <em>Iconic</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IIconic
     * @see com.archimatetool.model.impl.ArchimatePackage#getIconic()
     * @generated
     */
    int ICONIC = 130;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC__IMAGE_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Iconic</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ICONIC_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__NAME = DIAGRAM_MODEL__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__ID = DIAGRAM_MODEL__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__FEATURES = DIAGRAM_MODEL__FEATURES;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__CHILDREN = DIAGRAM_MODEL__CHILDREN;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__DOCUMENTATION = DIAGRAM_MODEL__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__PROPERTIES = DIAGRAM_MODEL__PROPERTIES;

    /**
     * The feature id for the '<em><b>Connection Router Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE = DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE;

    /**
     * The feature id for the '<em><b>Viewpoint</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT = DIAGRAM_MODEL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Diagram Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL_FEATURE_COUNT = DIAGRAM_MODEL_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.IDiagramModelArchimateComponent <em>Diagram Model Archimate Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.IDiagramModelArchimateComponent
     * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelArchimateComponent()
     * @generated
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT = 132;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT__ID = CONNECTABLE__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT__NAME = CONNECTABLE__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT__FEATURES = CONNECTABLE__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT__SOURCE_CONNECTIONS = CONNECTABLE__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT__TARGET_CONNECTIONS = CONNECTABLE__TARGET_CONNECTIONS;

    /**
     * The number of structural features of the '<em>Diagram Model Archimate Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_COMPONENT_FEATURE_COUNT = CONNECTABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Archimate Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Diagram Model Archimate Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ID = DIAGRAM_MODEL_CONNECTION__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__NAME = DIAGRAM_MODEL_CONNECTION__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__FEATURES = DIAGRAM_MODEL_CONNECTION__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__SOURCE_CONNECTIONS = DIAGRAM_MODEL_CONNECTION__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TARGET_CONNECTIONS = DIAGRAM_MODEL_CONNECTION__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__FONT = DIAGRAM_MODEL_CONNECTION__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__FONT_COLOR = DIAGRAM_MODEL_CONNECTION__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__PROPERTIES = DIAGRAM_MODEL_CONNECTION__PROPERTIES;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__DOCUMENTATION = DIAGRAM_MODEL_CONNECTION__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__LINE_WIDTH = DIAGRAM_MODEL_CONNECTION__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__LINE_COLOR = DIAGRAM_MODEL_CONNECTION__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TEXT = DIAGRAM_MODEL_CONNECTION__TEXT;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TEXT_POSITION = DIAGRAM_MODEL_CONNECTION__TEXT_POSITION;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__SOURCE = DIAGRAM_MODEL_CONNECTION__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TARGET = DIAGRAM_MODEL_CONNECTION__TARGET;

    /**
     * The feature id for the '<em><b>Bendpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__BENDPOINTS = DIAGRAM_MODEL_CONNECTION__BENDPOINTS;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TYPE = DIAGRAM_MODEL_CONNECTION__TYPE;

    /**
     * The feature id for the '<em><b>Archimate Relationship</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP = DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Diagram Model Archimate Connection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION_FEATURE_COUNT = DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.SketchModel <em>Sketch Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.SketchModel
     * @see com.archimatetool.model.impl.ArchimatePackage#getSketchModel()
     * @generated
     */
    int SKETCH_MODEL = 135;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__NAME = DIAGRAM_MODEL__NAME;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__ID = DIAGRAM_MODEL__ID;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__FEATURES = DIAGRAM_MODEL__FEATURES;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__CHILDREN = DIAGRAM_MODEL__CHILDREN;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__DOCUMENTATION = DIAGRAM_MODEL__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__PROPERTIES = DIAGRAM_MODEL__PROPERTIES;

    /**
     * The feature id for the '<em><b>Connection Router Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__CONNECTION_ROUTER_TYPE = DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE;

    /**
     * The feature id for the '<em><b>Background</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__BACKGROUND = DIAGRAM_MODEL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Sketch Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_FEATURE_COUNT = DIAGRAM_MODEL_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.SketchModelSticky <em>Sketch Model Sticky</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.SketchModelSticky
     * @see com.archimatetool.model.impl.ArchimatePackage#getSketchModelSticky()
     * @generated
     */
    int SKETCH_MODEL_STICKY = 136;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__CHILDREN = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__CONTENT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__PROPERTIES = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__TEXT_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Image Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__IMAGE_PATH = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Image Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__IMAGE_POSITION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Sketch Model Sticky</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link com.archimatetool.model.impl.SketchModelActor <em>Sketch Model Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.impl.SketchModelActor
     * @see com.archimatetool.model.impl.ArchimatePackage#getSketchModelActor()
     * @generated
     */
    int SKETCH_MODEL_ACTOR = 137;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__ID = DIAGRAM_MODEL_OBJECT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__NAME = DIAGRAM_MODEL_OBJECT__NAME;

    /**
     * The feature id for the '<em><b>Features</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__FEATURES = DIAGRAM_MODEL_OBJECT__FEATURES;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__SOURCE_CONNECTIONS = DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__TARGET_CONNECTIONS = DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__FONT = DIAGRAM_MODEL_OBJECT__FONT;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__FONT_COLOR = DIAGRAM_MODEL_OBJECT__FONT_COLOR;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__LINE_WIDTH = DIAGRAM_MODEL_OBJECT__LINE_WIDTH;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__LINE_COLOR = DIAGRAM_MODEL_OBJECT__LINE_COLOR;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__TEXT_ALIGNMENT = DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__BOUNDS = DIAGRAM_MODEL_OBJECT__BOUNDS;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Alpha</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__ALPHA = DIAGRAM_MODEL_OBJECT__ALPHA;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__DOCUMENTATION = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__PROPERTIES = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Sketch Model Actor</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link com.archimatetool.model.FolderType <em>Folder Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.archimatetool.model.FolderType
     * @see com.archimatetool.model.impl.ArchimatePackage#getFolderType()
     * @generated
     */
    int FOLDER_TYPE = 138;

    /**
     * The meta object id for the '<em>File</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.io.File
     * @see com.archimatetool.model.impl.ArchimatePackage#getFile()
     * @generated
     */
    int FILE = 139;


    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IAdapter <em>Adapter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Adapter</em>'.
     * @see com.archimatetool.model.IAdapter
     * @generated
     */
    EClass getAdapter();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Identifier</em>'.
     * @see com.archimatetool.model.IIdentifier
     * @generated
     */
    EClass getIdentifier();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IIdentifier#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see com.archimatetool.model.IIdentifier#getId()
     * @see #getIdentifier()
     * @generated
     */
    EAttribute getIdentifier_Id();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Properties</em>'.
     * @see com.archimatetool.model.IProperties
     * @generated
     */
    EClass getProperties();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IProperties#getProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Properties</em>'.
     * @see com.archimatetool.model.IProperties#getProperties()
     * @see #getProperties()
     * @generated
     */
    EReference getProperties_Properties();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFeature <em>Feature</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Feature</em>'.
     * @see com.archimatetool.model.IFeature
     * @generated
     */
    EClass getFeature();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IFeature#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see com.archimatetool.model.IFeature#getName()
     * @see #getFeature()
     * @generated
     */
    EAttribute getFeature_Name();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IFeature#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see com.archimatetool.model.IFeature#getValue()
     * @see #getFeature()
     * @generated
     */
    EAttribute getFeature_Value();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFeatures <em>Features</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Features</em>'.
     * @see com.archimatetool.model.IFeatures
     * @generated
     */
    EClass getFeatures();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IFeatures#getFeatures <em>Features</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Features</em>'.
     * @see com.archimatetool.model.IFeatures#getFeatures()
     * @see #getFeatures()
     * @generated
     */
    EReference getFeatures_Features();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Metadata</em>'.
     * @see com.archimatetool.model.IMetadata
     * @generated
     */
    EClass getMetadata();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IMetadata#getEntries <em>Entries</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Entries</em>'.
     * @see com.archimatetool.model.IMetadata#getEntries()
     * @see #getMetadata()
     * @generated
     */
    EReference getMetadata_Entries();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.INameable <em>Nameable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Nameable</em>'.
     * @see com.archimatetool.model.INameable
     * @generated
     */
    EClass getNameable();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.INameable#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see com.archimatetool.model.INameable#getName()
     * @see #getNameable()
     * @generated
     */
    EAttribute getNameable_Name();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITextContent <em>Text Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Content</em>'.
     * @see com.archimatetool.model.ITextContent
     * @generated
     */
    EClass getTextContent();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ITextContent#getContent <em>Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Content</em>'.
     * @see com.archimatetool.model.ITextContent#getContent()
     * @see #getTextContent()
     * @generated
     */
    EAttribute getTextContent_Content();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDocumentable <em>Documentable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Documentable</em>'.
     * @see com.archimatetool.model.IDocumentable
     * @generated
     */
    EClass getDocumentable();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDocumentable#getDocumentation <em>Documentation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Documentation</em>'.
     * @see com.archimatetool.model.IDocumentable#getDocumentation()
     * @see #getDocumentable()
     * @generated
     */
    EAttribute getDocumentable_Documentation();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ICloneable <em>Cloneable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cloneable</em>'.
     * @see com.archimatetool.model.ICloneable
     * @generated
     */
    EClass getCloneable();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IProfile <em>Profile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Profile</em>'.
     * @see com.archimatetool.model.IProfile
     * @generated
     */
    EClass getProfile();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IProfile#isSpecialization <em>Specialization</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Specialization</em>'.
     * @see com.archimatetool.model.IProfile#isSpecialization()
     * @see #getProfile()
     * @generated
     */
    EAttribute getProfile_Specialization();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IProfile#getConceptType <em>Concept Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Concept Type</em>'.
     * @see com.archimatetool.model.IProfile#getConceptType()
     * @see #getProfile()
     * @generated
     */
    EAttribute getProfile_ConceptType();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IProfiles <em>Profiles</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Profiles</em>'.
     * @see com.archimatetool.model.IProfiles
     * @generated
     */
    EClass getProfiles();

    /**
     * Returns the meta object for the reference list '{@link com.archimatetool.model.IProfiles#getProfiles <em>Profiles</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Profiles</em>'.
     * @see com.archimatetool.model.IProfiles#getProfiles()
     * @see #getProfiles()
     * @generated
     */
    EReference getProfiles_Profiles();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFolderContainer <em>Folder Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Folder Container</em>'.
     * @see com.archimatetool.model.IFolderContainer
     * @generated
     */
    EClass getFolderContainer();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IFolderContainer#getFolders <em>Folders</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Folders</em>'.
     * @see com.archimatetool.model.IFolderContainer#getFolders()
     * @see #getFolderContainer()
     * @generated
     */
    EReference getFolderContainer_Folders();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArchimateModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see com.archimatetool.model.IArchimateModel
     * @generated
     */
    EClass getArchimateModel();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IArchimateModel#getPurpose <em>Purpose</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Purpose</em>'.
     * @see com.archimatetool.model.IArchimateModel#getPurpose()
     * @see #getArchimateModel()
     * @generated
     */
    EAttribute getArchimateModel_Purpose();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IArchimateModel#getFile <em>File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File</em>'.
     * @see com.archimatetool.model.IArchimateModel#getFile()
     * @see #getArchimateModel()
     * @generated
     */
    EAttribute getArchimateModel_File();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IArchimateModel#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see com.archimatetool.model.IArchimateModel#getVersion()
     * @see #getArchimateModel()
     * @generated
     */
    EAttribute getArchimateModel_Version();

    /**
     * Returns the meta object for the containment reference '{@link com.archimatetool.model.IArchimateModel#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Metadata</em>'.
     * @see com.archimatetool.model.IArchimateModel#getMetadata()
     * @see #getArchimateModel()
     * @generated
     */
    EReference getArchimateModel_Metadata();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IArchimateModel#getProfiles <em>Profiles</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Profiles</em>'.
     * @see com.archimatetool.model.IArchimateModel#getProfiles()
     * @see #getArchimateModel()
     * @generated
     */
    EReference getArchimateModel_Profiles();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IJunction <em>Junction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Junction</em>'.
     * @see com.archimatetool.model.IJunction
     * @generated
     */
    EClass getJunction();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IJunction#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see com.archimatetool.model.IJunction#getType()
     * @see #getJunction()
     * @generated
     */
    EAttribute getJunction_Type();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IResource <em>Resource</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource</em>'.
     * @see com.archimatetool.model.IResource
     * @generated
     */
    EClass getResource();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFolder <em>Folder</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Folder</em>'.
     * @see com.archimatetool.model.IFolder
     * @generated
     */
    EClass getFolder();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IFolder#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see com.archimatetool.model.IFolder#getElements()
     * @see #getFolder()
     * @generated
     */
    EReference getFolder_Elements();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IFolder#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see com.archimatetool.model.IFolder#getType()
     * @see #getFolder()
     * @generated
     */
    EAttribute getFolder_Type();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArchimateModelObject <em>Model Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Object</em>'.
     * @see com.archimatetool.model.IArchimateModelObject
     * @generated
     */
    EClass getArchimateModelObject();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArchimateConcept <em>Concept</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Concept</em>'.
     * @see com.archimatetool.model.IArchimateConcept
     * @generated
     */
    EClass getArchimateConcept();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IProperty <em>Property</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property</em>'.
     * @see com.archimatetool.model.IProperty
     * @generated
     */
    EClass getProperty();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IProperty#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see com.archimatetool.model.IProperty#getKey()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Key();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IProperty#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see com.archimatetool.model.IProperty#getValue()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Value();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArchimateElement <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Element</em>'.
     * @see com.archimatetool.model.IArchimateElement
     * @generated
     */
    EClass getArchimateElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessElement <em>Business Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Element</em>'.
     * @see com.archimatetool.model.IBusinessElement
     * @generated
     */
    EClass getBusinessElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationElement <em>Application Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Element</em>'.
     * @see com.archimatetool.model.IApplicationElement
     * @generated
     */
    EClass getApplicationElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyElement <em>Technology Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Element</em>'.
     * @see com.archimatetool.model.ITechnologyElement
     * @generated
     */
    EClass getTechnologyElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IPhysicalElement <em>Physical Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Physical Element</em>'.
     * @see com.archimatetool.model.IPhysicalElement
     * @generated
     */
    EClass getPhysicalElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IPath <em>Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Path</em>'.
     * @see com.archimatetool.model.IPath
     * @generated
     */
    EClass getPath();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IAccessRelationship <em>Access Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Access Relationship</em>'.
     * @see com.archimatetool.model.IAccessRelationship
     * @generated
     */
    EClass getAccessRelationship();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IAccessRelationship#getAccessType <em>Access Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Type</em>'.
     * @see com.archimatetool.model.IAccessRelationship#getAccessType()
     * @see #getAccessRelationship()
     * @generated
     */
    EAttribute getAccessRelationship_AccessType();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IAggregationRelationship <em>Aggregation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Aggregation Relationship</em>'.
     * @see com.archimatetool.model.IAggregationRelationship
     * @generated
     */
    EClass getAggregationRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IAssignmentRelationship <em>Assignment Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assignment Relationship</em>'.
     * @see com.archimatetool.model.IAssignmentRelationship
     * @generated
     */
    EClass getAssignmentRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IAssociationRelationship <em>Association Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Association Relationship</em>'.
     * @see com.archimatetool.model.IAssociationRelationship
     * @generated
     */
    EClass getAssociationRelationship();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IAssociationRelationship#isDirected <em>Directed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Directed</em>'.
     * @see com.archimatetool.model.IAssociationRelationship#isDirected()
     * @see #getAssociationRelationship()
     * @generated
     */
    EAttribute getAssociationRelationship_Directed();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ICompositionRelationship <em>Composition Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Composition Relationship</em>'.
     * @see com.archimatetool.model.ICompositionRelationship
     * @generated
     */
    EClass getCompositionRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFlowRelationship <em>Flow Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Flow Relationship</em>'.
     * @see com.archimatetool.model.IFlowRelationship
     * @generated
     */
    EClass getFlowRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITriggeringRelationship <em>Triggering Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Triggering Relationship</em>'.
     * @see com.archimatetool.model.ITriggeringRelationship
     * @generated
     */
    EClass getTriggeringRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IInfluenceRelationship <em>Influence Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Influence Relationship</em>'.
     * @see com.archimatetool.model.IInfluenceRelationship
     * @generated
     */
    EClass getInfluenceRelationship();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IInfluenceRelationship#getStrength <em>Strength</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Strength</em>'.
     * @see com.archimatetool.model.IInfluenceRelationship#getStrength()
     * @see #getInfluenceRelationship()
     * @generated
     */
    EAttribute getInfluenceRelationship_Strength();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IRealizationRelationship <em>Realization Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Realization Relationship</em>'.
     * @see com.archimatetool.model.IRealizationRelationship
     * @generated
     */
    EClass getRealizationRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IServingRelationship <em>Serving Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Serving Relationship</em>'.
     * @see com.archimatetool.model.IServingRelationship
     * @generated
     */
    EClass getServingRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ISpecializationRelationship <em>Specialization Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Specialization Relationship</em>'.
     * @see com.archimatetool.model.ISpecializationRelationship
     * @generated
     */
    EClass getSpecializationRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessActor <em>Business Actor</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Actor</em>'.
     * @see com.archimatetool.model.IBusinessActor
     * @generated
     */
    EClass getBusinessActor();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessCollaboration <em>Business Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Collaboration</em>'.
     * @see com.archimatetool.model.IBusinessCollaboration
     * @generated
     */
    EClass getBusinessCollaboration();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IContract <em>Contract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contract</em>'.
     * @see com.archimatetool.model.IContract
     * @generated
     */
    EClass getContract();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessEvent <em>Business Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Event</em>'.
     * @see com.archimatetool.model.IBusinessEvent
     * @generated
     */
    EClass getBusinessEvent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessFunction <em>Business Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Function</em>'.
     * @see com.archimatetool.model.IBusinessFunction
     * @generated
     */
    EClass getBusinessFunction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessInteraction <em>Business Interaction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Interaction</em>'.
     * @see com.archimatetool.model.IBusinessInteraction
     * @generated
     */
    EClass getBusinessInteraction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessInterface <em>Business Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Interface</em>'.
     * @see com.archimatetool.model.IBusinessInterface
     * @generated
     */
    EClass getBusinessInterface();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Meaning</em>'.
     * @see com.archimatetool.model.IMeaning
     * @generated
     */
    EClass getMeaning();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessObject <em>Business Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Object</em>'.
     * @see com.archimatetool.model.IBusinessObject
     * @generated
     */
    EClass getBusinessObject();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessProcess <em>Business Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Process</em>'.
     * @see com.archimatetool.model.IBusinessProcess
     * @generated
     */
    EClass getBusinessProcess();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IProduct <em>Product</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Product</em>'.
     * @see com.archimatetool.model.IProduct
     * @generated
     */
    EClass getProduct();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IRepresentation <em>Representation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Representation</em>'.
     * @see com.archimatetool.model.IRepresentation
     * @generated
     */
    EClass getRepresentation();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessRole <em>Business Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Role</em>'.
     * @see com.archimatetool.model.IBusinessRole
     * @generated
     */
    EClass getBusinessRole();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBusinessService <em>Business Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Service</em>'.
     * @see com.archimatetool.model.IBusinessService
     * @generated
     */
    EClass getBusinessService();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ICapability <em>Capability</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capability</em>'.
     * @see com.archimatetool.model.ICapability
     * @generated
     */
    EClass getCapability();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ICommunicationNetwork <em>Communication Network</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Communication Network</em>'.
     * @see com.archimatetool.model.ICommunicationNetwork
     * @generated
     */
    EClass getCommunicationNetwork();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Value</em>'.
     * @see com.archimatetool.model.IValue
     * @generated
     */
    EClass getValue();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IValueStream <em>Value Stream</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Value Stream</em>'.
     * @see com.archimatetool.model.IValueStream
     * @generated
     */
    EClass getValueStream();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ILocation <em>Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Location</em>'.
     * @see com.archimatetool.model.ILocation
     * @generated
     */
    EClass getLocation();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IMaterial <em>Material</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Material</em>'.
     * @see com.archimatetool.model.IMaterial
     * @generated
     */
    EClass getMaterial();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationCollaboration <em>Application Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Collaboration</em>'.
     * @see com.archimatetool.model.IApplicationCollaboration
     * @generated
     */
    EClass getApplicationCollaboration();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationComponent <em>Application Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Component</em>'.
     * @see com.archimatetool.model.IApplicationComponent
     * @generated
     */
    EClass getApplicationComponent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationEvent <em>Application Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Event</em>'.
     * @see com.archimatetool.model.IApplicationEvent
     * @generated
     */
    EClass getApplicationEvent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationFunction <em>Application Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Function</em>'.
     * @see com.archimatetool.model.IApplicationFunction
     * @generated
     */
    EClass getApplicationFunction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationInteraction <em>Application Interaction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Interaction</em>'.
     * @see com.archimatetool.model.IApplicationInteraction
     * @generated
     */
    EClass getApplicationInteraction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationInterface <em>Application Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Interface</em>'.
     * @see com.archimatetool.model.IApplicationInterface
     * @generated
     */
    EClass getApplicationInterface();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationProcess <em>Application Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Process</em>'.
     * @see com.archimatetool.model.IApplicationProcess
     * @generated
     */
    EClass getApplicationProcess();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDataObject <em>Data Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Object</em>'.
     * @see com.archimatetool.model.IDataObject
     * @generated
     */
    EClass getDataObject();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IApplicationService <em>Application Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Service</em>'.
     * @see com.archimatetool.model.IApplicationService
     * @generated
     */
    EClass getApplicationService();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArtifact <em>Artifact</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Artifact</em>'.
     * @see com.archimatetool.model.IArtifact
     * @generated
     */
    EClass getArtifact();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.INode <em>Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Node</em>'.
     * @see com.archimatetool.model.INode
     * @generated
     */
    EClass getNode();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IOutcome <em>Outcome</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Outcome</em>'.
     * @see com.archimatetool.model.IOutcome
     * @generated
     */
    EClass getOutcome();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ISystemSoftware <em>System Software</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>System Software</em>'.
     * @see com.archimatetool.model.ISystemSoftware
     * @generated
     */
    EClass getSystemSoftware();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyCollaboration <em>Technology Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Collaboration</em>'.
     * @see com.archimatetool.model.ITechnologyCollaboration
     * @generated
     */
    EClass getTechnologyCollaboration();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyEvent <em>Technology Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Event</em>'.
     * @see com.archimatetool.model.ITechnologyEvent
     * @generated
     */
    EClass getTechnologyEvent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyFunction <em>Technology Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Function</em>'.
     * @see com.archimatetool.model.ITechnologyFunction
     * @generated
     */
    EClass getTechnologyFunction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyInterface <em>Technology Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Interface</em>'.
     * @see com.archimatetool.model.ITechnologyInterface
     * @generated
     */
    EClass getTechnologyInterface();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyInteraction <em>Technology Interaction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Interaction</em>'.
     * @see com.archimatetool.model.ITechnologyInteraction
     * @generated
     */
    EClass getTechnologyInteraction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyObject <em>Technology Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Object</em>'.
     * @see com.archimatetool.model.ITechnologyObject
     * @generated
     */
    EClass getTechnologyObject();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyProcess <em>Technology Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Process</em>'.
     * @see com.archimatetool.model.ITechnologyProcess
     * @generated
     */
    EClass getTechnologyProcess();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITechnologyService <em>Technology Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Service</em>'.
     * @see com.archimatetool.model.ITechnologyService
     * @generated
     */
    EClass getTechnologyService();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDevice <em>Device</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Device</em>'.
     * @see com.archimatetool.model.IDevice
     * @generated
     */
    EClass getDevice();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDistributionNetwork <em>Distribution Network</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Distribution Network</em>'.
     * @see com.archimatetool.model.IDistributionNetwork
     * @generated
     */
    EClass getDistributionNetwork();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IEquipment <em>Equipment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Equipment</em>'.
     * @see com.archimatetool.model.IEquipment
     * @generated
     */
    EClass getEquipment();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFacility <em>Facility</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Facility</em>'.
     * @see com.archimatetool.model.IFacility
     * @generated
     */
    EClass getFacility();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IMotivationElement <em>Motivation Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Motivation Element</em>'.
     * @see com.archimatetool.model.IMotivationElement
     * @generated
     */
    EClass getMotivationElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IStakeholder <em>Stakeholder</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Stakeholder</em>'.
     * @see com.archimatetool.model.IStakeholder
     * @generated
     */
    EClass getStakeholder();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDriver <em>Driver</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Driver</em>'.
     * @see com.archimatetool.model.IDriver
     * @generated
     */
    EClass getDriver();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IAssessment <em>Assessment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assessment</em>'.
     * @see com.archimatetool.model.IAssessment
     * @generated
     */
    EClass getAssessment();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IGoal <em>Goal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Goal</em>'.
     * @see com.archimatetool.model.IGoal
     * @generated
     */
    EClass getGoal();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IGrouping <em>Grouping</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Grouping</em>'.
     * @see com.archimatetool.model.IGrouping
     * @generated
     */
    EClass getGrouping();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IImplementationEvent <em>Implementation Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Implementation Event</em>'.
     * @see com.archimatetool.model.IImplementationEvent
     * @generated
     */
    EClass getImplementationEvent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IRequirement <em>Requirement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Requirement</em>'.
     * @see com.archimatetool.model.IRequirement
     * @generated
     */
    EClass getRequirement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Constraint</em>'.
     * @see com.archimatetool.model.IConstraint
     * @generated
     */
    EClass getConstraint();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ICourseOfAction <em>Course Of Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Course Of Action</em>'.
     * @see com.archimatetool.model.ICourseOfAction
     * @generated
     */
    EClass getCourseOfAction();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IPrinciple <em>Principle</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Principle</em>'.
     * @see com.archimatetool.model.IPrinciple
     * @generated
     */
    EClass getPrinciple();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IImplementationMigrationElement <em>Implementation Migration Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Implementation Migration Element</em>'.
     * @see com.archimatetool.model.IImplementationMigrationElement
     * @generated
     */
    EClass getImplementationMigrationElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ICompositeElement <em>Composite Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Composite Element</em>'.
     * @see com.archimatetool.model.ICompositeElement
     * @generated
     */
    EClass getCompositeElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBehaviorElement <em>Behavior Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Behavior Element</em>'.
     * @see com.archimatetool.model.IBehaviorElement
     * @generated
     */
    EClass getBehaviorElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IStrategyBehaviorElement <em>Strategy Behavior Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Strategy Behavior Element</em>'.
     * @see com.archimatetool.model.IStrategyBehaviorElement
     * @generated
     */
    EClass getStrategyBehaviorElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IStructureElement <em>Structure Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Structure Element</em>'.
     * @see com.archimatetool.model.IStructureElement
     * @generated
     */
    EClass getStructureElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IActiveStructureElement <em>Active Structure Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Active Structure Element</em>'.
     * @see com.archimatetool.model.IActiveStructureElement
     * @generated
     */
    EClass getActiveStructureElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IPassiveStructureElement <em>Passive Structure Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Passive Structure Element</em>'.
     * @see com.archimatetool.model.IPassiveStructureElement
     * @generated
     */
    EClass getPassiveStructureElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IStructuralRelationship <em>Structural Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Structural Relationship</em>'.
     * @see com.archimatetool.model.IStructuralRelationship
     * @generated
     */
    EClass getStructuralRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDependendencyRelationship <em>Dependendency Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dependendency Relationship</em>'.
     * @see com.archimatetool.model.IDependendencyRelationship
     * @generated
     */
    EClass getDependendencyRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDynamicRelationship <em>Dynamic Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dynamic Relationship</em>'.
     * @see com.archimatetool.model.IDynamicRelationship
     * @generated
     */
    EClass getDynamicRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IOtherRelationship <em>Other Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Other Relationship</em>'.
     * @see com.archimatetool.model.IOtherRelationship
     * @generated
     */
    EClass getOtherRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IStrategyElement <em>Strategy Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Strategy Element</em>'.
     * @see com.archimatetool.model.IStrategyElement
     * @generated
     */
    EClass getStrategyElement();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IWorkPackage <em>Work Package</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Work Package</em>'.
     * @see com.archimatetool.model.IWorkPackage
     * @generated
     */
    EClass getWorkPackage();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDeliverable <em>Deliverable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Deliverable</em>'.
     * @see com.archimatetool.model.IDeliverable
     * @generated
     */
    EClass getDeliverable();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IPlateau <em>Plateau</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Plateau</em>'.
     * @see com.archimatetool.model.IPlateau
     * @generated
     */
    EClass getPlateau();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IGap <em>Gap</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Gap</em>'.
     * @see com.archimatetool.model.IGap
     * @generated
     */
    EClass getGap();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArchimateRelationship <em>Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Relationship</em>'.
     * @see com.archimatetool.model.IArchimateRelationship
     * @generated
     */
    EClass getArchimateRelationship();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IArchimateRelationship#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source</em>'.
     * @see com.archimatetool.model.IArchimateRelationship#getSource()
     * @see #getArchimateRelationship()
     * @generated
     */
    EReference getArchimateRelationship_Source();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IArchimateRelationship#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target</em>'.
     * @see com.archimatetool.model.IArchimateRelationship#getTarget()
     * @see #getArchimateRelationship()
     * @generated
     */
    EReference getArchimateRelationship_Target();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model</em>'.
     * @see com.archimatetool.model.IDiagramModel
     * @generated
     */
    EClass getDiagramModel();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModel#getConnectionRouterType <em>Connection Router Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Connection Router Type</em>'.
     * @see com.archimatetool.model.IDiagramModel#getConnectionRouterType()
     * @see #getDiagramModel()
     * @generated
     */
    EAttribute getDiagramModel_ConnectionRouterType();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IArchimateDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model</em>'.
     * @see com.archimatetool.model.IArchimateDiagramModel
     * @generated
     */
    EClass getArchimateDiagramModel();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IArchimateDiagramModel#getViewpoint <em>Viewpoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Viewpoint</em>'.
     * @see com.archimatetool.model.IArchimateDiagramModel#getViewpoint()
     * @see #getArchimateDiagramModel()
     * @generated
     */
    EAttribute getArchimateDiagramModel_Viewpoint();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelArchimateComponent <em>Diagram Model Archimate Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Archimate Component</em>'.
     * @see com.archimatetool.model.IDiagramModelArchimateComponent
     * @generated
     */
    EClass getDiagramModelArchimateComponent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelReference <em>Diagram Model Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Reference</em>'.
     * @see com.archimatetool.model.IDiagramModelReference
     * @generated
     */
    EClass getDiagramModelReference();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IDiagramModelReference#getReferencedModel <em>Referenced Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Referenced Model</em>'.
     * @see com.archimatetool.model.IDiagramModelReference#getReferencedModel()
     * @see #getDiagramModelReference()
     * @generated
     */
    EReference getDiagramModelReference_ReferencedModel();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelComponent <em>Diagram Model Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Component</em>'.
     * @see com.archimatetool.model.IDiagramModelComponent
     * @generated
     */
    EClass getDiagramModelComponent();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IConnectable <em>Connectable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Connectable</em>'.
     * @see com.archimatetool.model.IConnectable
     * @generated
     */
    EClass getConnectable();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IConnectable#getSourceConnections <em>Source Connections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Source Connections</em>'.
     * @see com.archimatetool.model.IConnectable#getSourceConnections()
     * @see #getConnectable()
     * @generated
     */
    EReference getConnectable_SourceConnections();

    /**
     * Returns the meta object for the reference list '{@link com.archimatetool.model.IConnectable#getTargetConnections <em>Target Connections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Connections</em>'.
     * @see com.archimatetool.model.IConnectable#getTargetConnections()
     * @see #getConnectable()
     * @generated
     */
    EReference getConnectable_TargetConnections();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelObject <em>Diagram Model Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Object</em>'.
     * @see com.archimatetool.model.IDiagramModelObject
     * @generated
     */
    EClass getDiagramModelObject();

    /**
     * Returns the meta object for the containment reference '{@link com.archimatetool.model.IDiagramModelObject#getBounds <em>Bounds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounds</em>'.
     * @see com.archimatetool.model.IDiagramModelObject#getBounds()
     * @see #getDiagramModelObject()
     * @generated
     */
    EReference getDiagramModelObject_Bounds();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelObject#getFillColor <em>Fill Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill Color</em>'.
     * @see com.archimatetool.model.IDiagramModelObject#getFillColor()
     * @see #getDiagramModelObject()
     * @generated
     */
    EAttribute getDiagramModelObject_FillColor();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelObject#getAlpha <em>Alpha</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Alpha</em>'.
     * @see com.archimatetool.model.IDiagramModelObject#getAlpha()
     * @see #getDiagramModelObject()
     * @generated
     */
    EAttribute getDiagramModelObject_Alpha();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Archimate Object</em>'.
     * @see com.archimatetool.model.IDiagramModelArchimateObject
     * @generated
     */
    EClass getDiagramModelArchimateObject();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IDiagramModelArchimateObject#getArchimateElement <em>Archimate Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Archimate Element</em>'.
     * @see com.archimatetool.model.IDiagramModelArchimateObject#getArchimateElement()
     * @see #getDiagramModelArchimateObject()
     * @generated
     */
    EReference getDiagramModelArchimateObject_ArchimateElement();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelArchimateObject#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see com.archimatetool.model.IDiagramModelArchimateObject#getType()
     * @see #getDiagramModelArchimateObject()
     * @generated
     */
    EAttribute getDiagramModelArchimateObject_Type();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelContainer <em>Diagram Model Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Container</em>'.
     * @see com.archimatetool.model.IDiagramModelContainer
     * @generated
     */
    EClass getDiagramModelContainer();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IDiagramModelContainer#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Children</em>'.
     * @see com.archimatetool.model.IDiagramModelContainer#getChildren()
     * @see #getDiagramModelContainer()
     * @generated
     */
    EReference getDiagramModelContainer_Children();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelGroup <em>Diagram Model Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Group</em>'.
     * @see com.archimatetool.model.IDiagramModelGroup
     * @generated
     */
    EClass getDiagramModelGroup();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelNote <em>Diagram Model Note</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Note</em>'.
     * @see com.archimatetool.model.IDiagramModelNote
     * @generated
     */
    EClass getDiagramModelNote();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelImage <em>Diagram Model Image</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Image</em>'.
     * @see com.archimatetool.model.IDiagramModelImage
     * @generated
     */
    EClass getDiagramModelImage();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelConnection <em>Diagram Model Connection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Connection</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection
     * @generated
     */
    EClass getDiagramModelConnection();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelConnection#getText <em>Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection#getText()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_Text();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelConnection#getTextPosition <em>Text Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Position</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection#getTextPosition()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_TextPosition();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IDiagramModelConnection#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection#getSource()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EReference getDiagramModelConnection_Source();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IDiagramModelConnection#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection#getTarget()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EReference getDiagramModelConnection_Target();

    /**
     * Returns the meta object for the containment reference list '{@link com.archimatetool.model.IDiagramModelConnection#getBendpoints <em>Bendpoints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bendpoints</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection#getBendpoints()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EReference getDiagramModelConnection_Bendpoints();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelConnection#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see com.archimatetool.model.IDiagramModelConnection#getType()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_Type();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Archimate Connection</em>'.
     * @see com.archimatetool.model.IDiagramModelArchimateConnection
     * @generated
     */
    EClass getDiagramModelArchimateConnection();

    /**
     * Returns the meta object for the reference '{@link com.archimatetool.model.IDiagramModelArchimateConnection#getArchimateRelationship <em>Archimate Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Archimate Relationship</em>'.
     * @see com.archimatetool.model.IDiagramModelArchimateConnection#getArchimateRelationship()
     * @see #getDiagramModelArchimateConnection()
     * @generated
     */
    EReference getDiagramModelArchimateConnection_ArchimateRelationship();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelBendpoint <em>Diagram Model Bendpoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Bendpoint</em>'.
     * @see com.archimatetool.model.IDiagramModelBendpoint
     * @generated
     */
    EClass getDiagramModelBendpoint();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelBendpoint#getStartX <em>Start X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start X</em>'.
     * @see com.archimatetool.model.IDiagramModelBendpoint#getStartX()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_StartX();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelBendpoint#getStartY <em>Start Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Y</em>'.
     * @see com.archimatetool.model.IDiagramModelBendpoint#getStartY()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_StartY();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelBendpoint#getEndX <em>End X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End X</em>'.
     * @see com.archimatetool.model.IDiagramModelBendpoint#getEndX()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_EndX();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelBendpoint#getEndY <em>End Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Y</em>'.
     * @see com.archimatetool.model.IDiagramModelBendpoint#getEndY()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_EndY();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ILineObject <em>Line Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Line Object</em>'.
     * @see com.archimatetool.model.ILineObject
     * @generated
     */
    EClass getLineObject();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ILineObject#getLineWidth <em>Line Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Line Width</em>'.
     * @see com.archimatetool.model.ILineObject#getLineWidth()
     * @see #getLineObject()
     * @generated
     */
    EAttribute getLineObject_LineWidth();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ILineObject#getLineColor <em>Line Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Line Color</em>'.
     * @see com.archimatetool.model.ILineObject#getLineColor()
     * @see #getLineObject()
     * @generated
     */
    EAttribute getLineObject_LineColor();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IFontAttribute <em>Font Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Font Attribute</em>'.
     * @see com.archimatetool.model.IFontAttribute
     * @generated
     */
    EClass getFontAttribute();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IFontAttribute#getFont <em>Font</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Font</em>'.
     * @see com.archimatetool.model.IFontAttribute#getFont()
     * @see #getFontAttribute()
     * @generated
     */
    EAttribute getFontAttribute_Font();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IFontAttribute#getFontColor <em>Font Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Font Color</em>'.
     * @see com.archimatetool.model.IFontAttribute#getFontColor()
     * @see #getFontAttribute()
     * @generated
     */
    EAttribute getFontAttribute_FontColor();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITextPosition <em>Text Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Position</em>'.
     * @see com.archimatetool.model.ITextPosition
     * @generated
     */
    EClass getTextPosition();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ITextPosition#getTextPosition <em>Text Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Position</em>'.
     * @see com.archimatetool.model.ITextPosition#getTextPosition()
     * @see #getTextPosition()
     * @generated
     */
    EAttribute getTextPosition_TextPosition();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ITextAlignment <em>Text Alignment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Alignment</em>'.
     * @see com.archimatetool.model.ITextAlignment
     * @generated
     */
    EClass getTextAlignment();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ITextAlignment#getTextAlignment <em>Text Alignment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Alignment</em>'.
     * @see com.archimatetool.model.ITextAlignment#getTextAlignment()
     * @see #getTextAlignment()
     * @generated
     */
    EAttribute getTextAlignment_TextAlignment();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBorderObject <em>Border Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Border Object</em>'.
     * @see com.archimatetool.model.IBorderObject
     * @generated
     */
    EClass getBorderObject();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IBorderObject#getBorderColor <em>Border Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Border Color</em>'.
     * @see com.archimatetool.model.IBorderObject#getBorderColor()
     * @see #getBorderObject()
     * @generated
     */
    EAttribute getBorderObject_BorderColor();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBorderType <em>Border Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Border Type</em>'.
     * @see com.archimatetool.model.IBorderType
     * @generated
     */
    EClass getBorderType();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IBorderType#getBorderType <em>Border Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Border Type</em>'.
     * @see com.archimatetool.model.IBorderType#getBorderType()
     * @see #getBorderType()
     * @generated
     */
    EAttribute getBorderType_BorderType();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IDiagramModelImageProvider <em>Diagram Model Image Provider</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Image Provider</em>'.
     * @see com.archimatetool.model.IDiagramModelImageProvider
     * @generated
     */
    EClass getDiagramModelImageProvider();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IDiagramModelImageProvider#getImagePath <em>Image Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Image Path</em>'.
     * @see com.archimatetool.model.IDiagramModelImageProvider#getImagePath()
     * @see #getDiagramModelImageProvider()
     * @generated
     */
    EAttribute getDiagramModelImageProvider_ImagePath();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IBounds <em>Bounds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bounds</em>'.
     * @see com.archimatetool.model.IBounds
     * @generated
     */
    EClass getBounds();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IBounds#getX <em>X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>X</em>'.
     * @see com.archimatetool.model.IBounds#getX()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_X();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IBounds#getY <em>Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Y</em>'.
     * @see com.archimatetool.model.IBounds#getY()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_Y();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IBounds#getWidth <em>Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Width</em>'.
     * @see com.archimatetool.model.IBounds#getWidth()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_Width();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IBounds#getHeight <em>Height</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Height</em>'.
     * @see com.archimatetool.model.IBounds#getHeight()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_Height();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ILockable <em>Lockable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Lockable</em>'.
     * @see com.archimatetool.model.ILockable
     * @generated
     */
    EClass getLockable();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ILockable#isLocked <em>Locked</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Locked</em>'.
     * @see com.archimatetool.model.ILockable#isLocked()
     * @see #getLockable()
     * @generated
     */
    EAttribute getLockable_Locked();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.IIconic <em>Iconic</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Iconic</em>'.
     * @see com.archimatetool.model.IIconic
     * @generated
     */
    EClass getIconic();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.IIconic#getImagePosition <em>Image Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Image Position</em>'.
     * @see com.archimatetool.model.IIconic#getImagePosition()
     * @see #getIconic()
     * @generated
     */
    EAttribute getIconic_ImagePosition();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ISketchModel <em>Sketch Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sketch Model</em>'.
     * @see com.archimatetool.model.ISketchModel
     * @generated
     */
    EClass getSketchModel();

    /**
     * Returns the meta object for the attribute '{@link com.archimatetool.model.ISketchModel#getBackground <em>Background</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Background</em>'.
     * @see com.archimatetool.model.ISketchModel#getBackground()
     * @see #getSketchModel()
     * @generated
     */
    EAttribute getSketchModel_Background();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ISketchModelSticky <em>Sketch Model Sticky</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sketch Model Sticky</em>'.
     * @see com.archimatetool.model.ISketchModelSticky
     * @generated
     */
    EClass getSketchModelSticky();

    /**
     * Returns the meta object for class '{@link com.archimatetool.model.ISketchModelActor <em>Sketch Model Actor</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sketch Model Actor</em>'.
     * @see com.archimatetool.model.ISketchModelActor
     * @generated
     */
    EClass getSketchModelActor();

    /**
     * Returns the meta object for enum '{@link com.archimatetool.model.FolderType <em>Folder Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Folder Type</em>'.
     * @see com.archimatetool.model.FolderType
     * @generated
     */
    EEnum getFolderType();

    /**
     * Returns the meta object for data type '{@link java.io.File <em>File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>File</em>'.
     * @see java.io.File
     * @model instanceClass="java.io.File" serializeable="false"
     * @generated
     */
    EDataType getFile();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    IArchimateFactory getArchimateFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link com.archimatetool.model.IAdapter <em>Adapter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IAdapter
         * @see com.archimatetool.model.impl.ArchimatePackage#getAdapter()
         * @generated
         */
        EClass ADAPTER = eINSTANCE.getAdapter();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IIdentifier <em>Identifier</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IIdentifier
         * @see com.archimatetool.model.impl.ArchimatePackage#getIdentifier()
         * @generated
         */
        EClass IDENTIFIER = eINSTANCE.getIdentifier();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFIER__ID = eINSTANCE.getIdentifier_Id();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IProperties <em>Properties</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IProperties
         * @see com.archimatetool.model.impl.ArchimatePackage#getProperties()
         * @generated
         */
        EClass PROPERTIES = eINSTANCE.getProperties();

        /**
         * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTIES__PROPERTIES = eINSTANCE.getProperties_Properties();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Feature <em>Feature</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Feature
         * @see com.archimatetool.model.impl.ArchimatePackage#getFeature()
         * @generated
         */
        EClass FEATURE = eINSTANCE.getFeature();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FEATURE__NAME = eINSTANCE.getFeature_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FEATURE__VALUE = eINSTANCE.getFeature_Value();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IFeatures <em>Features</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IFeatures
         * @see com.archimatetool.model.impl.ArchimatePackage#getFeatures()
         * @generated
         */
        EClass FEATURES = eINSTANCE.getFeatures();

        /**
         * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FEATURES__FEATURES = eINSTANCE.getFeatures_Features();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Metadata <em>Metadata</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Metadata
         * @see com.archimatetool.model.impl.ArchimatePackage#getMetadata()
         * @generated
         */
        EClass METADATA = eINSTANCE.getMetadata();

        /**
         * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference METADATA__ENTRIES = eINSTANCE.getMetadata_Entries();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.INameable <em>Nameable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.INameable
         * @see com.archimatetool.model.impl.ArchimatePackage#getNameable()
         * @generated
         */
        EClass NAMEABLE = eINSTANCE.getNameable();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute NAMEABLE__NAME = eINSTANCE.getNameable_Name();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ITextContent <em>Text Content</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ITextContent
         * @see com.archimatetool.model.impl.ArchimatePackage#getTextContent()
         * @generated
         */
        EClass TEXT_CONTENT = eINSTANCE.getTextContent();

        /**
         * The meta object literal for the '<em><b>Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_CONTENT__CONTENT = eINSTANCE.getTextContent_Content();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IDocumentable <em>Documentable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IDocumentable
         * @see com.archimatetool.model.impl.ArchimatePackage#getDocumentable()
         * @generated
         */
        EClass DOCUMENTABLE = eINSTANCE.getDocumentable();

        /**
         * The meta object literal for the '<em><b>Documentation</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENTABLE__DOCUMENTATION = eINSTANCE.getDocumentable_Documentation();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ICloneable <em>Cloneable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ICloneable
         * @see com.archimatetool.model.impl.ArchimatePackage#getCloneable()
         * @generated
         */
        EClass CLONEABLE = eINSTANCE.getCloneable();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Profile <em>Profile</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Profile
         * @see com.archimatetool.model.impl.ArchimatePackage#getProfile()
         * @generated
         */
        EClass PROFILE = eINSTANCE.getProfile();

        /**
         * The meta object literal for the '<em><b>Specialization</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROFILE__SPECIALIZATION = eINSTANCE.getProfile_Specialization();

        /**
         * The meta object literal for the '<em><b>Concept Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROFILE__CONCEPT_TYPE = eINSTANCE.getProfile_ConceptType();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IProfiles <em>Profiles</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IProfiles
         * @see com.archimatetool.model.impl.ArchimatePackage#getProfiles()
         * @generated
         */
        EClass PROFILES = eINSTANCE.getProfiles();

        /**
         * The meta object literal for the '<em><b>Profiles</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROFILES__PROFILES = eINSTANCE.getProfiles_Profiles();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IFolderContainer <em>Folder Container</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IFolderContainer
         * @see com.archimatetool.model.impl.ArchimatePackage#getFolderContainer()
         * @generated
         */
        EClass FOLDER_CONTAINER = eINSTANCE.getFolderContainer();

        /**
         * The meta object literal for the '<em><b>Folders</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOLDER_CONTAINER__FOLDERS = eINSTANCE.getFolderContainer_Folders();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ArchimateModel <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ArchimateModel
         * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateModel()
         * @generated
         */
        EClass ARCHIMATE_MODEL = eINSTANCE.getArchimateModel();

        /**
         * The meta object literal for the '<em><b>Purpose</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARCHIMATE_MODEL__PURPOSE = eINSTANCE.getArchimateModel_Purpose();

        /**
         * The meta object literal for the '<em><b>File</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARCHIMATE_MODEL__FILE = eINSTANCE.getArchimateModel_File();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARCHIMATE_MODEL__VERSION = eINSTANCE.getArchimateModel_Version();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIMATE_MODEL__METADATA = eINSTANCE.getArchimateModel_Metadata();

        /**
         * The meta object literal for the '<em><b>Profiles</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIMATE_MODEL__PROFILES = eINSTANCE.getArchimateModel_Profiles();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Junction <em>Junction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Junction
         * @see com.archimatetool.model.impl.ArchimatePackage#getJunction()
         * @generated
         */
        EClass JUNCTION = eINSTANCE.getJunction();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JUNCTION__TYPE = eINSTANCE.getJunction_Type();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Resource <em>Resource</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Resource
         * @see com.archimatetool.model.impl.ArchimatePackage#getResource()
         * @generated
         */
        EClass RESOURCE = eINSTANCE.getResource();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Folder <em>Folder</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Folder
         * @see com.archimatetool.model.impl.ArchimatePackage#getFolder()
         * @generated
         */
        EClass FOLDER = eINSTANCE.getFolder();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOLDER__ELEMENTS = eINSTANCE.getFolder_Elements();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FOLDER__TYPE = eINSTANCE.getFolder_Type();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IArchimateModelObject <em>Model Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IArchimateModelObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateModelObject()
         * @generated
         */
        EClass ARCHIMATE_MODEL_OBJECT = eINSTANCE.getArchimateModelObject();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ArchimateConcept <em>Concept</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ArchimateConcept
         * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateConcept()
         * @generated
         */
        EClass ARCHIMATE_CONCEPT = eINSTANCE.getArchimateConcept();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Property <em>Property</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Property
         * @see com.archimatetool.model.impl.ArchimatePackage#getProperty()
         * @generated
         */
        EClass PROPERTY = eINSTANCE.getProperty();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__KEY = eINSTANCE.getProperty_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ArchimateElement <em>Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ArchimateElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateElement()
         * @generated
         */
        EClass ARCHIMATE_ELEMENT = eINSTANCE.getArchimateElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IBusinessElement <em>Business Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IBusinessElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessElement()
         * @generated
         */
        EClass BUSINESS_ELEMENT = eINSTANCE.getBusinessElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IApplicationElement <em>Application Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IApplicationElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationElement()
         * @generated
         */
        EClass APPLICATION_ELEMENT = eINSTANCE.getApplicationElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ITechnologyElement <em>Technology Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ITechnologyElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyElement()
         * @generated
         */
        EClass TECHNOLOGY_ELEMENT = eINSTANCE.getTechnologyElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IPhysicalElement <em>Physical Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IPhysicalElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getPhysicalElement()
         * @generated
         */
        EClass PHYSICAL_ELEMENT = eINSTANCE.getPhysicalElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Path <em>Path</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Path
         * @see com.archimatetool.model.impl.ArchimatePackage#getPath()
         * @generated
         */
        EClass PATH = eINSTANCE.getPath();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.AccessRelationship <em>Access Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.AccessRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getAccessRelationship()
         * @generated
         */
        EClass ACCESS_RELATIONSHIP = eINSTANCE.getAccessRelationship();

        /**
         * The meta object literal for the '<em><b>Access Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACCESS_RELATIONSHIP__ACCESS_TYPE = eINSTANCE.getAccessRelationship_AccessType();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.AggregationRelationship <em>Aggregation Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.AggregationRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getAggregationRelationship()
         * @generated
         */
        EClass AGGREGATION_RELATIONSHIP = eINSTANCE.getAggregationRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.AssignmentRelationship <em>Assignment Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.AssignmentRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getAssignmentRelationship()
         * @generated
         */
        EClass ASSIGNMENT_RELATIONSHIP = eINSTANCE.getAssignmentRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.AssociationRelationship <em>Association Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.AssociationRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getAssociationRelationship()
         * @generated
         */
        EClass ASSOCIATION_RELATIONSHIP = eINSTANCE.getAssociationRelationship();

        /**
         * The meta object literal for the '<em><b>Directed</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ASSOCIATION_RELATIONSHIP__DIRECTED = eINSTANCE.getAssociationRelationship_Directed();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.CompositionRelationship <em>Composition Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.CompositionRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getCompositionRelationship()
         * @generated
         */
        EClass COMPOSITION_RELATIONSHIP = eINSTANCE.getCompositionRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.FlowRelationship <em>Flow Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.FlowRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getFlowRelationship()
         * @generated
         */
        EClass FLOW_RELATIONSHIP = eINSTANCE.getFlowRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TriggeringRelationship <em>Triggering Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TriggeringRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getTriggeringRelationship()
         * @generated
         */
        EClass TRIGGERING_RELATIONSHIP = eINSTANCE.getTriggeringRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.InfluenceRelationship <em>Influence Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.InfluenceRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getInfluenceRelationship()
         * @generated
         */
        EClass INFLUENCE_RELATIONSHIP = eINSTANCE.getInfluenceRelationship();

        /**
         * The meta object literal for the '<em><b>Strength</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INFLUENCE_RELATIONSHIP__STRENGTH = eINSTANCE.getInfluenceRelationship_Strength();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.RealizationRelationship <em>Realization Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.RealizationRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getRealizationRelationship()
         * @generated
         */
        EClass REALIZATION_RELATIONSHIP = eINSTANCE.getRealizationRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ServingRelationship <em>Serving Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ServingRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getServingRelationship()
         * @generated
         */
        EClass SERVING_RELATIONSHIP = eINSTANCE.getServingRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.SpecializationRelationship <em>Specialization Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.SpecializationRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getSpecializationRelationship()
         * @generated
         */
        EClass SPECIALIZATION_RELATIONSHIP = eINSTANCE.getSpecializationRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessActor <em>Business Actor</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessActor
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessActor()
         * @generated
         */
        EClass BUSINESS_ACTOR = eINSTANCE.getBusinessActor();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessCollaboration <em>Business Collaboration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessCollaboration
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessCollaboration()
         * @generated
         */
        EClass BUSINESS_COLLABORATION = eINSTANCE.getBusinessCollaboration();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Contract <em>Contract</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Contract
         * @see com.archimatetool.model.impl.ArchimatePackage#getContract()
         * @generated
         */
        EClass CONTRACT = eINSTANCE.getContract();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessEvent <em>Business Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessEvent
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessEvent()
         * @generated
         */
        EClass BUSINESS_EVENT = eINSTANCE.getBusinessEvent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessFunction <em>Business Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessFunction
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessFunction()
         * @generated
         */
        EClass BUSINESS_FUNCTION = eINSTANCE.getBusinessFunction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessInteraction <em>Business Interaction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessInteraction
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessInteraction()
         * @generated
         */
        EClass BUSINESS_INTERACTION = eINSTANCE.getBusinessInteraction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessInterface <em>Business Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessInterface
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessInterface()
         * @generated
         */
        EClass BUSINESS_INTERFACE = eINSTANCE.getBusinessInterface();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Meaning <em>Meaning</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Meaning
         * @see com.archimatetool.model.impl.ArchimatePackage#getMeaning()
         * @generated
         */
        EClass MEANING = eINSTANCE.getMeaning();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessObject <em>Business Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessObject()
         * @generated
         */
        EClass BUSINESS_OBJECT = eINSTANCE.getBusinessObject();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessProcess <em>Business Process</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessProcess
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessProcess()
         * @generated
         */
        EClass BUSINESS_PROCESS = eINSTANCE.getBusinessProcess();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Product <em>Product</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Product
         * @see com.archimatetool.model.impl.ArchimatePackage#getProduct()
         * @generated
         */
        EClass PRODUCT = eINSTANCE.getProduct();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Representation <em>Representation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Representation
         * @see com.archimatetool.model.impl.ArchimatePackage#getRepresentation()
         * @generated
         */
        EClass REPRESENTATION = eINSTANCE.getRepresentation();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessRole <em>Business Role</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessRole
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessRole()
         * @generated
         */
        EClass BUSINESS_ROLE = eINSTANCE.getBusinessRole();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.BusinessService <em>Business Service</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.BusinessService
         * @see com.archimatetool.model.impl.ArchimatePackage#getBusinessService()
         * @generated
         */
        EClass BUSINESS_SERVICE = eINSTANCE.getBusinessService();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Capability <em>Capability</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Capability
         * @see com.archimatetool.model.impl.ArchimatePackage#getCapability()
         * @generated
         */
        EClass CAPABILITY = eINSTANCE.getCapability();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.CommunicationNetwork <em>Communication Network</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.CommunicationNetwork
         * @see com.archimatetool.model.impl.ArchimatePackage#getCommunicationNetwork()
         * @generated
         */
        EClass COMMUNICATION_NETWORK = eINSTANCE.getCommunicationNetwork();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Value <em>Value</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Value
         * @see com.archimatetool.model.impl.ArchimatePackage#getValue()
         * @generated
         */
        EClass VALUE = eINSTANCE.getValue();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ValueStream <em>Value Stream</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ValueStream
         * @see com.archimatetool.model.impl.ArchimatePackage#getValueStream()
         * @generated
         */
        EClass VALUE_STREAM = eINSTANCE.getValueStream();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Location <em>Location</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Location
         * @see com.archimatetool.model.impl.ArchimatePackage#getLocation()
         * @generated
         */
        EClass LOCATION = eINSTANCE.getLocation();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Material <em>Material</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Material
         * @see com.archimatetool.model.impl.ArchimatePackage#getMaterial()
         * @generated
         */
        EClass MATERIAL = eINSTANCE.getMaterial();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationCollaboration <em>Application Collaboration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationCollaboration
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationCollaboration()
         * @generated
         */
        EClass APPLICATION_COLLABORATION = eINSTANCE.getApplicationCollaboration();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationComponent <em>Application Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationComponent
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationComponent()
         * @generated
         */
        EClass APPLICATION_COMPONENT = eINSTANCE.getApplicationComponent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationEvent <em>Application Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationEvent
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationEvent()
         * @generated
         */
        EClass APPLICATION_EVENT = eINSTANCE.getApplicationEvent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationFunction <em>Application Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationFunction
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationFunction()
         * @generated
         */
        EClass APPLICATION_FUNCTION = eINSTANCE.getApplicationFunction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationInteraction <em>Application Interaction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationInteraction
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationInteraction()
         * @generated
         */
        EClass APPLICATION_INTERACTION = eINSTANCE.getApplicationInteraction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationInterface <em>Application Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationInterface
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationInterface()
         * @generated
         */
        EClass APPLICATION_INTERFACE = eINSTANCE.getApplicationInterface();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationProcess <em>Application Process</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationProcess
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationProcess()
         * @generated
         */
        EClass APPLICATION_PROCESS = eINSTANCE.getApplicationProcess();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DataObject <em>Data Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DataObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getDataObject()
         * @generated
         */
        EClass DATA_OBJECT = eINSTANCE.getDataObject();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ApplicationService <em>Application Service</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ApplicationService
         * @see com.archimatetool.model.impl.ArchimatePackage#getApplicationService()
         * @generated
         */
        EClass APPLICATION_SERVICE = eINSTANCE.getApplicationService();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Artifact <em>Artifact</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Artifact
         * @see com.archimatetool.model.impl.ArchimatePackage#getArtifact()
         * @generated
         */
        EClass ARTIFACT = eINSTANCE.getArtifact();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Node <em>Node</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Node
         * @see com.archimatetool.model.impl.ArchimatePackage#getNode()
         * @generated
         */
        EClass NODE = eINSTANCE.getNode();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Outcome <em>Outcome</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Outcome
         * @see com.archimatetool.model.impl.ArchimatePackage#getOutcome()
         * @generated
         */
        EClass OUTCOME = eINSTANCE.getOutcome();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.SystemSoftware <em>System Software</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.SystemSoftware
         * @see com.archimatetool.model.impl.ArchimatePackage#getSystemSoftware()
         * @generated
         */
        EClass SYSTEM_SOFTWARE = eINSTANCE.getSystemSoftware();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyCollaboration <em>Technology Collaboration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyCollaboration
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyCollaboration()
         * @generated
         */
        EClass TECHNOLOGY_COLLABORATION = eINSTANCE.getTechnologyCollaboration();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyEvent <em>Technology Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyEvent
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyEvent()
         * @generated
         */
        EClass TECHNOLOGY_EVENT = eINSTANCE.getTechnologyEvent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyFunction <em>Technology Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyFunction
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyFunction()
         * @generated
         */
        EClass TECHNOLOGY_FUNCTION = eINSTANCE.getTechnologyFunction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyInterface <em>Technology Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyInterface
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyInterface()
         * @generated
         */
        EClass TECHNOLOGY_INTERFACE = eINSTANCE.getTechnologyInterface();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyInteraction <em>Technology Interaction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyInteraction
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyInteraction()
         * @generated
         */
        EClass TECHNOLOGY_INTERACTION = eINSTANCE.getTechnologyInteraction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ITechnologyObject <em>Technology Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ITechnologyObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyObject()
         * @generated
         */
        EClass TECHNOLOGY_OBJECT = eINSTANCE.getTechnologyObject();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyProcess <em>Technology Process</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyProcess
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyProcess()
         * @generated
         */
        EClass TECHNOLOGY_PROCESS = eINSTANCE.getTechnologyProcess();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.TechnologyService <em>Technology Service</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.TechnologyService
         * @see com.archimatetool.model.impl.ArchimatePackage#getTechnologyService()
         * @generated
         */
        EClass TECHNOLOGY_SERVICE = eINSTANCE.getTechnologyService();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Device <em>Device</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Device
         * @see com.archimatetool.model.impl.ArchimatePackage#getDevice()
         * @generated
         */
        EClass DEVICE = eINSTANCE.getDevice();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DistributionNetwork <em>Distribution Network</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DistributionNetwork
         * @see com.archimatetool.model.impl.ArchimatePackage#getDistributionNetwork()
         * @generated
         */
        EClass DISTRIBUTION_NETWORK = eINSTANCE.getDistributionNetwork();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Equipment <em>Equipment</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Equipment
         * @see com.archimatetool.model.impl.ArchimatePackage#getEquipment()
         * @generated
         */
        EClass EQUIPMENT = eINSTANCE.getEquipment();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Facility <em>Facility</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Facility
         * @see com.archimatetool.model.impl.ArchimatePackage#getFacility()
         * @generated
         */
        EClass FACILITY = eINSTANCE.getFacility();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IMotivationElement <em>Motivation Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IMotivationElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getMotivationElement()
         * @generated
         */
        EClass MOTIVATION_ELEMENT = eINSTANCE.getMotivationElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Stakeholder <em>Stakeholder</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Stakeholder
         * @see com.archimatetool.model.impl.ArchimatePackage#getStakeholder()
         * @generated
         */
        EClass STAKEHOLDER = eINSTANCE.getStakeholder();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Driver <em>Driver</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Driver
         * @see com.archimatetool.model.impl.ArchimatePackage#getDriver()
         * @generated
         */
        EClass DRIVER = eINSTANCE.getDriver();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Assessment <em>Assessment</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Assessment
         * @see com.archimatetool.model.impl.ArchimatePackage#getAssessment()
         * @generated
         */
        EClass ASSESSMENT = eINSTANCE.getAssessment();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Goal <em>Goal</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Goal
         * @see com.archimatetool.model.impl.ArchimatePackage#getGoal()
         * @generated
         */
        EClass GOAL = eINSTANCE.getGoal();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Grouping <em>Grouping</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Grouping
         * @see com.archimatetool.model.impl.ArchimatePackage#getGrouping()
         * @generated
         */
        EClass GROUPING = eINSTANCE.getGrouping();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ImplementationEvent <em>Implementation Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ImplementationEvent
         * @see com.archimatetool.model.impl.ArchimatePackage#getImplementationEvent()
         * @generated
         */
        EClass IMPLEMENTATION_EVENT = eINSTANCE.getImplementationEvent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Requirement <em>Requirement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Requirement
         * @see com.archimatetool.model.impl.ArchimatePackage#getRequirement()
         * @generated
         */
        EClass REQUIREMENT = eINSTANCE.getRequirement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Constraint <em>Constraint</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Constraint
         * @see com.archimatetool.model.impl.ArchimatePackage#getConstraint()
         * @generated
         */
        EClass CONSTRAINT = eINSTANCE.getConstraint();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.CourseOfAction <em>Course Of Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.CourseOfAction
         * @see com.archimatetool.model.impl.ArchimatePackage#getCourseOfAction()
         * @generated
         */
        EClass COURSE_OF_ACTION = eINSTANCE.getCourseOfAction();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Principle <em>Principle</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Principle
         * @see com.archimatetool.model.impl.ArchimatePackage#getPrinciple()
         * @generated
         */
        EClass PRINCIPLE = eINSTANCE.getPrinciple();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IImplementationMigrationElement <em>Implementation Migration Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IImplementationMigrationElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getImplementationMigrationElement()
         * @generated
         */
        EClass IMPLEMENTATION_MIGRATION_ELEMENT = eINSTANCE.getImplementationMigrationElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ICompositeElement <em>Composite Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ICompositeElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getCompositeElement()
         * @generated
         */
        EClass COMPOSITE_ELEMENT = eINSTANCE.getCompositeElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IBehaviorElement <em>Behavior Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IBehaviorElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getBehaviorElement()
         * @generated
         */
        EClass BEHAVIOR_ELEMENT = eINSTANCE.getBehaviorElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IStrategyBehaviorElement <em>Strategy Behavior Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IStrategyBehaviorElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getStrategyBehaviorElement()
         * @generated
         */
        EClass STRATEGY_BEHAVIOR_ELEMENT = eINSTANCE.getStrategyBehaviorElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IStructureElement <em>Structure Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IStructureElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getStructureElement()
         * @generated
         */
        EClass STRUCTURE_ELEMENT = eINSTANCE.getStructureElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IActiveStructureElement <em>Active Structure Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IActiveStructureElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getActiveStructureElement()
         * @generated
         */
        EClass ACTIVE_STRUCTURE_ELEMENT = eINSTANCE.getActiveStructureElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IPassiveStructureElement <em>Passive Structure Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IPassiveStructureElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getPassiveStructureElement()
         * @generated
         */
        EClass PASSIVE_STRUCTURE_ELEMENT = eINSTANCE.getPassiveStructureElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IStructuralRelationship <em>Structural Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IStructuralRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getStructuralRelationship()
         * @generated
         */
        EClass STRUCTURAL_RELATIONSHIP = eINSTANCE.getStructuralRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IDependendencyRelationship <em>Dependendency Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IDependendencyRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getDependendencyRelationship()
         * @generated
         */
        EClass DEPENDENDENCY_RELATIONSHIP = eINSTANCE.getDependendencyRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IDynamicRelationship <em>Dynamic Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IDynamicRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getDynamicRelationship()
         * @generated
         */
        EClass DYNAMIC_RELATIONSHIP = eINSTANCE.getDynamicRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IOtherRelationship <em>Other Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IOtherRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getOtherRelationship()
         * @generated
         */
        EClass OTHER_RELATIONSHIP = eINSTANCE.getOtherRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IStrategyElement <em>Strategy Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IStrategyElement
         * @see com.archimatetool.model.impl.ArchimatePackage#getStrategyElement()
         * @generated
         */
        EClass STRATEGY_ELEMENT = eINSTANCE.getStrategyElement();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.WorkPackage <em>Work Package</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.WorkPackage
         * @see com.archimatetool.model.impl.ArchimatePackage#getWorkPackage()
         * @generated
         */
        EClass WORK_PACKAGE = eINSTANCE.getWorkPackage();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Deliverable <em>Deliverable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Deliverable
         * @see com.archimatetool.model.impl.ArchimatePackage#getDeliverable()
         * @generated
         */
        EClass DELIVERABLE = eINSTANCE.getDeliverable();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Plateau <em>Plateau</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Plateau
         * @see com.archimatetool.model.impl.ArchimatePackage#getPlateau()
         * @generated
         */
        EClass PLATEAU = eINSTANCE.getPlateau();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Gap <em>Gap</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Gap
         * @see com.archimatetool.model.impl.ArchimatePackage#getGap()
         * @generated
         */
        EClass GAP = eINSTANCE.getGap();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ArchimateRelationship <em>Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ArchimateRelationship
         * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateRelationship()
         * @generated
         */
        EClass ARCHIMATE_RELATIONSHIP = eINSTANCE.getArchimateRelationship();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIMATE_RELATIONSHIP__SOURCE = eINSTANCE.getArchimateRelationship_Source();

        /**
         * The meta object literal for the '<em><b>Target</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIMATE_RELATIONSHIP__TARGET = eINSTANCE.getArchimateRelationship_Target();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModel <em>Diagram Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModel
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModel()
         * @generated
         */
        EClass DIAGRAM_MODEL = eINSTANCE.getDiagramModel();

        /**
         * The meta object literal for the '<em><b>Connection Router Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE = eINSTANCE.getDiagramModel_ConnectionRouterType();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.ArchimateDiagramModel <em>Diagram Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.ArchimateDiagramModel
         * @see com.archimatetool.model.impl.ArchimatePackage#getArchimateDiagramModel()
         * @generated
         */
        EClass ARCHIMATE_DIAGRAM_MODEL = eINSTANCE.getArchimateDiagramModel();

        /**
         * The meta object literal for the '<em><b>Viewpoint</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT = eINSTANCE.getArchimateDiagramModel_Viewpoint();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IDiagramModelArchimateComponent <em>Diagram Model Archimate Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IDiagramModelArchimateComponent
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelArchimateComponent()
         * @generated
         */
        EClass DIAGRAM_MODEL_ARCHIMATE_COMPONENT = eINSTANCE.getDiagramModelArchimateComponent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelReference <em>Diagram Model Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelReference
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelReference()
         * @generated
         */
        EClass DIAGRAM_MODEL_REFERENCE = eINSTANCE.getDiagramModelReference();

        /**
         * The meta object literal for the '<em><b>Referenced Model</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL = eINSTANCE.getDiagramModelReference_ReferencedModel();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelComponent <em>Diagram Model Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelComponent
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelComponent()
         * @generated
         */
        EClass DIAGRAM_MODEL_COMPONENT = eINSTANCE.getDiagramModelComponent();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Connectable <em>Connectable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Connectable
         * @see com.archimatetool.model.impl.ArchimatePackage#getConnectable()
         * @generated
         */
        EClass CONNECTABLE = eINSTANCE.getConnectable();

        /**
         * The meta object literal for the '<em><b>Source Connections</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONNECTABLE__SOURCE_CONNECTIONS = eINSTANCE.getConnectable_SourceConnections();

        /**
         * The meta object literal for the '<em><b>Target Connections</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONNECTABLE__TARGET_CONNECTIONS = eINSTANCE.getConnectable_TargetConnections();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelObject <em>Diagram Model Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelObject()
         * @generated
         */
        EClass DIAGRAM_MODEL_OBJECT = eINSTANCE.getDiagramModelObject();

        /**
         * The meta object literal for the '<em><b>Bounds</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_OBJECT__BOUNDS = eINSTANCE.getDiagramModelObject_Bounds();

        /**
         * The meta object literal for the '<em><b>Fill Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_OBJECT__FILL_COLOR = eINSTANCE.getDiagramModelObject_FillColor();

        /**
         * The meta object literal for the '<em><b>Alpha</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_OBJECT__ALPHA = eINSTANCE.getDiagramModelObject_Alpha();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelArchimateObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelArchimateObject()
         * @generated
         */
        EClass DIAGRAM_MODEL_ARCHIMATE_OBJECT = eINSTANCE.getDiagramModelArchimateObject();

        /**
         * The meta object literal for the '<em><b>Archimate Element</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT = eINSTANCE.getDiagramModelArchimateObject_ArchimateElement();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE = eINSTANCE.getDiagramModelArchimateObject_Type();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IDiagramModelContainer <em>Diagram Model Container</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IDiagramModelContainer
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelContainer()
         * @generated
         */
        EClass DIAGRAM_MODEL_CONTAINER = eINSTANCE.getDiagramModelContainer();

        /**
         * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_CONTAINER__CHILDREN = eINSTANCE.getDiagramModelContainer_Children();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelGroup <em>Diagram Model Group</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelGroup
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelGroup()
         * @generated
         */
        EClass DIAGRAM_MODEL_GROUP = eINSTANCE.getDiagramModelGroup();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelNote <em>Diagram Model Note</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelNote
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelNote()
         * @generated
         */
        EClass DIAGRAM_MODEL_NOTE = eINSTANCE.getDiagramModelNote();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelImage <em>Diagram Model Image</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelImage
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelImage()
         * @generated
         */
        EClass DIAGRAM_MODEL_IMAGE = eINSTANCE.getDiagramModelImage();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelConnection <em>Diagram Model Connection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelConnection
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelConnection()
         * @generated
         */
        EClass DIAGRAM_MODEL_CONNECTION = eINSTANCE.getDiagramModelConnection();

        /**
         * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_CONNECTION__TEXT = eINSTANCE.getDiagramModelConnection_Text();

        /**
         * The meta object literal for the '<em><b>Text Position</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_CONNECTION__TEXT_POSITION = eINSTANCE.getDiagramModelConnection_TextPosition();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_CONNECTION__SOURCE = eINSTANCE.getDiagramModelConnection_Source();

        /**
         * The meta object literal for the '<em><b>Target</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_CONNECTION__TARGET = eINSTANCE.getDiagramModelConnection_Target();

        /**
         * The meta object literal for the '<em><b>Bendpoints</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_CONNECTION__BENDPOINTS = eINSTANCE.getDiagramModelConnection_Bendpoints();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_CONNECTION__TYPE = eINSTANCE.getDiagramModelConnection_Type();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelArchimateConnection
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelArchimateConnection()
         * @generated
         */
        EClass DIAGRAM_MODEL_ARCHIMATE_CONNECTION = eINSTANCE.getDiagramModelArchimateConnection();

        /**
         * The meta object literal for the '<em><b>Archimate Relationship</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP = eINSTANCE.getDiagramModelArchimateConnection_ArchimateRelationship();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.DiagramModelBendpoint <em>Diagram Model Bendpoint</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.DiagramModelBendpoint
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelBendpoint()
         * @generated
         */
        EClass DIAGRAM_MODEL_BENDPOINT = eINSTANCE.getDiagramModelBendpoint();

        /**
         * The meta object literal for the '<em><b>Start X</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_BENDPOINT__START_X = eINSTANCE.getDiagramModelBendpoint_StartX();

        /**
         * The meta object literal for the '<em><b>Start Y</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_BENDPOINT__START_Y = eINSTANCE.getDiagramModelBendpoint_StartY();

        /**
         * The meta object literal for the '<em><b>End X</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_BENDPOINT__END_X = eINSTANCE.getDiagramModelBendpoint_EndX();

        /**
         * The meta object literal for the '<em><b>End Y</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_BENDPOINT__END_Y = eINSTANCE.getDiagramModelBendpoint_EndY();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ILineObject <em>Line Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ILineObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getLineObject()
         * @generated
         */
        EClass LINE_OBJECT = eINSTANCE.getLineObject();

        /**
         * The meta object literal for the '<em><b>Line Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LINE_OBJECT__LINE_WIDTH = eINSTANCE.getLineObject_LineWidth();

        /**
         * The meta object literal for the '<em><b>Line Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LINE_OBJECT__LINE_COLOR = eINSTANCE.getLineObject_LineColor();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IFontAttribute <em>Font Attribute</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IFontAttribute
         * @see com.archimatetool.model.impl.ArchimatePackage#getFontAttribute()
         * @generated
         */
        EClass FONT_ATTRIBUTE = eINSTANCE.getFontAttribute();

        /**
         * The meta object literal for the '<em><b>Font</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FONT_ATTRIBUTE__FONT = eINSTANCE.getFontAttribute_Font();

        /**
         * The meta object literal for the '<em><b>Font Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FONT_ATTRIBUTE__FONT_COLOR = eINSTANCE.getFontAttribute_FontColor();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ITextPosition <em>Text Position</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ITextPosition
         * @see com.archimatetool.model.impl.ArchimatePackage#getTextPosition()
         * @generated
         */
        EClass TEXT_POSITION = eINSTANCE.getTextPosition();

        /**
         * The meta object literal for the '<em><b>Text Position</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_POSITION__TEXT_POSITION = eINSTANCE.getTextPosition_TextPosition();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ITextAlignment <em>Text Alignment</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ITextAlignment
         * @see com.archimatetool.model.impl.ArchimatePackage#getTextAlignment()
         * @generated
         */
        EClass TEXT_ALIGNMENT = eINSTANCE.getTextAlignment();

        /**
         * The meta object literal for the '<em><b>Text Alignment</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_ALIGNMENT__TEXT_ALIGNMENT = eINSTANCE.getTextAlignment_TextAlignment();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IBorderObject <em>Border Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IBorderObject
         * @see com.archimatetool.model.impl.ArchimatePackage#getBorderObject()
         * @generated
         */
        EClass BORDER_OBJECT = eINSTANCE.getBorderObject();

        /**
         * The meta object literal for the '<em><b>Border Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BORDER_OBJECT__BORDER_COLOR = eINSTANCE.getBorderObject_BorderColor();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IBorderType <em>Border Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IBorderType
         * @see com.archimatetool.model.impl.ArchimatePackage#getBorderType()
         * @generated
         */
        EClass BORDER_TYPE = eINSTANCE.getBorderType();

        /**
         * The meta object literal for the '<em><b>Border Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BORDER_TYPE__BORDER_TYPE = eINSTANCE.getBorderType_BorderType();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IDiagramModelImageProvider <em>Diagram Model Image Provider</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IDiagramModelImageProvider
         * @see com.archimatetool.model.impl.ArchimatePackage#getDiagramModelImageProvider()
         * @generated
         */
        EClass DIAGRAM_MODEL_IMAGE_PROVIDER = eINSTANCE.getDiagramModelImageProvider();

        /**
         * The meta object literal for the '<em><b>Image Path</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH = eINSTANCE.getDiagramModelImageProvider_ImagePath();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.Bounds <em>Bounds</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.Bounds
         * @see com.archimatetool.model.impl.ArchimatePackage#getBounds()
         * @generated
         */
        EClass BOUNDS = eINSTANCE.getBounds();

        /**
         * The meta object literal for the '<em><b>X</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDS__X = eINSTANCE.getBounds_X();

        /**
         * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDS__Y = eINSTANCE.getBounds_Y();

        /**
         * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDS__WIDTH = eINSTANCE.getBounds_Width();

        /**
         * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDS__HEIGHT = eINSTANCE.getBounds_Height();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.ILockable <em>Lockable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.ILockable
         * @see com.archimatetool.model.impl.ArchimatePackage#getLockable()
         * @generated
         */
        EClass LOCKABLE = eINSTANCE.getLockable();

        /**
         * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LOCKABLE__LOCKED = eINSTANCE.getLockable_Locked();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.IIconic <em>Iconic</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.IIconic
         * @see com.archimatetool.model.impl.ArchimatePackage#getIconic()
         * @generated
         */
        EClass ICONIC = eINSTANCE.getIconic();

        /**
         * The meta object literal for the '<em><b>Image Position</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ICONIC__IMAGE_POSITION = eINSTANCE.getIconic_ImagePosition();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.SketchModel <em>Sketch Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.SketchModel
         * @see com.archimatetool.model.impl.ArchimatePackage#getSketchModel()
         * @generated
         */
        EClass SKETCH_MODEL = eINSTANCE.getSketchModel();

        /**
         * The meta object literal for the '<em><b>Background</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SKETCH_MODEL__BACKGROUND = eINSTANCE.getSketchModel_Background();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.SketchModelSticky <em>Sketch Model Sticky</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.SketchModelSticky
         * @see com.archimatetool.model.impl.ArchimatePackage#getSketchModelSticky()
         * @generated
         */
        EClass SKETCH_MODEL_STICKY = eINSTANCE.getSketchModelSticky();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.impl.SketchModelActor <em>Sketch Model Actor</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.impl.SketchModelActor
         * @see com.archimatetool.model.impl.ArchimatePackage#getSketchModelActor()
         * @generated
         */
        EClass SKETCH_MODEL_ACTOR = eINSTANCE.getSketchModelActor();

        /**
         * The meta object literal for the '{@link com.archimatetool.model.FolderType <em>Folder Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see com.archimatetool.model.FolderType
         * @see com.archimatetool.model.impl.ArchimatePackage#getFolderType()
         * @generated
         */
        EEnum FOLDER_TYPE = eINSTANCE.getFolderType();

        /**
         * The meta object literal for the '<em>File</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.io.File
         * @see com.archimatetool.model.impl.ArchimatePackage#getFile()
         * @generated
         */
        EDataType FILE = eINSTANCE.getFile();

    }

} //IArchimatePackage
