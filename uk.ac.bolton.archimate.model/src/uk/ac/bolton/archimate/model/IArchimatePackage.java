/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

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
 * @see uk.ac.bolton.archimate.model.IArchimateFactory
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
    String eNS_URI = "http://www.bolton.ac.uk/archimate"; //$NON-NLS-1$

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
    IArchimatePackage eINSTANCE = uk.ac.bolton.archimate.model.impl.ArchimatePackage.init();

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IAdapter <em>Adapter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IAdapter
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAdapter()
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
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IIdentifier <em>Identifier</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IIdentifier
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getIdentifier()
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
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IProperties <em>Properties</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IProperties
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getProperties()
     * @generated
     */
    int PROPERTIES = 3;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.INameable <em>Nameable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.INameable
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getNameable()
     * @generated
     */
    int NAMEABLE = 4;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.ITextContent <em>Text Content</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.ITextContent
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getTextContent()
     * @generated
     */
    int TEXT_CONTENT = 5;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IDocumentable <em>Documentable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IDocumentable
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDocumentable()
     * @generated
     */
    int DOCUMENTABLE = 6;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.ICloneable <em>Cloneable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.ICloneable
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getCloneable()
     * @generated
     */
    int CLONEABLE = 7;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IFolderContainer <em>Folder Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IFolderContainer
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFolderContainer()
     * @generated
     */
    int FOLDER_CONTAINER = 8;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ArchimateModel <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ArchimateModel
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateModel()
     * @generated
     */
    int ARCHIMATE_MODEL = 9;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IArchimateModelElement <em>Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IArchimateModelElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateModelElement()
     * @generated
     */
    int ARCHIMATE_MODEL_ELEMENT = 10;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Folder <em>Folder</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Folder
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFolder()
     * @generated
     */
    int FOLDER = 11;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Property <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Property
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getProperty()
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
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL__ARCHIMATE_MODEL = FOLDER_CONTAINER_FEATURE_COUNT + 2;

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
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_FEATURE_COUNT = FOLDER_CONTAINER_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL = ADAPTER_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Model Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT = ADAPTER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__ARCHIMATE_MODEL = ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Folders</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__FOLDERS = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__NAME = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__ID = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__DOCUMENTATION = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__PROPERTIES = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__ELEMENTS = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER__TYPE = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Folder</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_FEATURE_COUNT = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ArchimateElement <em>Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ArchimateElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateElement()
     * @generated
     */
    int ARCHIMATE_ELEMENT = 12;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__ARCHIMATE_MODEL = ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__ID = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__NAME = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__DOCUMENTATION = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT__PROPERTIES = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_ELEMENT_FEATURE_COUNT = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IJunctionElement <em>Junction Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IJunctionElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getJunctionElement()
     * @generated
     */
    int JUNCTION_ELEMENT = 13;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_ELEMENT__ARCHIMATE_MODEL = ARCHIMATE_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Junction Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IInterfaceElement <em>Interface Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IInterfaceElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getInterfaceElement()
     * @generated
     */
    int INTERFACE_ELEMENT = 14;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT__ARCHIMATE_MODEL = ARCHIMATE_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Interface Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT__INTERFACE_TYPE = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Interface Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERFACE_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Junction <em>Junction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Junction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getJunction()
     * @generated
     */
    int JUNCTION = 15;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__ARCHIMATE_MODEL = JUNCTION_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__ID = JUNCTION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__NAME = JUNCTION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__DOCUMENTATION = JUNCTION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION__PROPERTIES = JUNCTION_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Junction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JUNCTION_FEATURE_COUNT = JUNCTION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.AndJunction <em>And Junction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.AndJunction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAndJunction()
     * @generated
     */
    int AND_JUNCTION = 16;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JUNCTION__ARCHIMATE_MODEL = JUNCTION_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JUNCTION__ID = JUNCTION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JUNCTION__NAME = JUNCTION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JUNCTION__DOCUMENTATION = JUNCTION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JUNCTION__PROPERTIES = JUNCTION_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>And Junction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JUNCTION_FEATURE_COUNT = JUNCTION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.OrJunction <em>Or Junction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.OrJunction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getOrJunction()
     * @generated
     */
    int OR_JUNCTION = 17;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JUNCTION__ARCHIMATE_MODEL = JUNCTION_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JUNCTION__ID = JUNCTION_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JUNCTION__NAME = JUNCTION_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JUNCTION__DOCUMENTATION = JUNCTION_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JUNCTION__PROPERTIES = JUNCTION_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Or Junction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JUNCTION_FEATURE_COUNT = JUNCTION_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Relationship <em>Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Relationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getRelationship()
     * @generated
     */
    int RELATIONSHIP = 18;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__ARCHIMATE_MODEL = ARCHIMATE_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__SOURCE = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP__TARGET = ARCHIMATE_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RELATIONSHIP_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.AccessRelationship <em>Access Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.AccessRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAccessRelationship()
     * @generated
     */
    int ACCESS_RELATIONSHIP = 19;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The feature id for the '<em><b>Access Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP__ACCESS_TYPE = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Access Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCESS_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.AggregationRelationship <em>Aggregation Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.AggregationRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAggregationRelationship()
     * @generated
     */
    int AGGREGATION_RELATIONSHIP = 20;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Aggregation Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AGGREGATION_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.AssignmentRelationship <em>Assignment Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.AssignmentRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAssignmentRelationship()
     * @generated
     */
    int ASSIGNMENT_RELATIONSHIP = 21;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Assignment Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.AssociationRelationship <em>Association Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.AssociationRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAssociationRelationship()
     * @generated
     */
    int ASSOCIATION_RELATIONSHIP = 22;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Association Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSOCIATION_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.CompositionRelationship <em>Composition Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.CompositionRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getCompositionRelationship()
     * @generated
     */
    int COMPOSITION_RELATIONSHIP = 23;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Composition Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITION_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.FlowRelationship <em>Flow Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.FlowRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFlowRelationship()
     * @generated
     */
    int FLOW_RELATIONSHIP = 24;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Flow Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FLOW_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.RealisationRelationship <em>Realisation Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.RealisationRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getRealisationRelationship()
     * @generated
     */
    int REALISATION_RELATIONSHIP = 25;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Realisation Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REALISATION_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.SpecialisationRelationship <em>Specialisation Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.SpecialisationRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSpecialisationRelationship()
     * @generated
     */
    int SPECIALISATION_RELATIONSHIP = 26;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Specialisation Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIALISATION_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.TriggeringRelationship <em>Triggering Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.TriggeringRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getTriggeringRelationship()
     * @generated
     */
    int TRIGGERING_RELATIONSHIP = 27;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Triggering Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRIGGERING_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.UsedByRelationship <em>Used By Relationship</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.UsedByRelationship
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getUsedByRelationship()
     * @generated
     */
    int USED_BY_RELATIONSHIP = 28;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__ARCHIMATE_MODEL = RELATIONSHIP__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__ID = RELATIONSHIP__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__NAME = RELATIONSHIP__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__DOCUMENTATION = RELATIONSHIP__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__PROPERTIES = RELATIONSHIP__PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__SOURCE = RELATIONSHIP__SOURCE;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP__TARGET = RELATIONSHIP__TARGET;

    /**
     * The number of structural features of the '<em>Used By Relationship</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USED_BY_RELATIONSHIP_FEATURE_COUNT = RELATIONSHIP_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IBusinessLayerElement <em>Business Layer Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IBusinessLayerElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessLayerElement()
     * @generated
     */
    int BUSINESS_LAYER_ELEMENT = 29;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL = ARCHIMATE_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_LAYER_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_LAYER_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_LAYER_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_LAYER_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Layer Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_LAYER_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessActivity <em>Business Activity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessActivity
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessActivity()
     * @generated
     */
    int BUSINESS_ACTIVITY = 30;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTIVITY__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTIVITY__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTIVITY__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTIVITY__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTIVITY__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Activity</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTIVITY_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessActor <em>Business Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessActor
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessActor()
     * @generated
     */
    int BUSINESS_ACTOR = 31;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Actor</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ACTOR_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessCollaboration <em>Business Collaboration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessCollaboration
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessCollaboration()
     * @generated
     */
    int BUSINESS_COLLABORATION = 32;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Collaboration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_COLLABORATION_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Contract <em>Contract</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Contract
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getContract()
     * @generated
     */
    int CONTRACT = 33;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Contract</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTRACT_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessEvent <em>Business Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessEvent
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessEvent()
     * @generated
     */
    int BUSINESS_EVENT = 34;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_EVENT_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessFunction <em>Business Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessFunction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessFunction()
     * @generated
     */
    int BUSINESS_FUNCTION = 35;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_FUNCTION_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessInteraction <em>Business Interaction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessInteraction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessInteraction()
     * @generated
     */
    int BUSINESS_INTERACTION = 36;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Interaction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERACTION_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessInterface <em>Business Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessInterface
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessInterface()
     * @generated
     */
    int BUSINESS_INTERFACE = 37;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Interface Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE__INTERFACE_TYPE = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Business Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_INTERFACE_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Meaning <em>Meaning</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Meaning
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getMeaning()
     * @generated
     */
    int MEANING = 38;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Meaning</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEANING_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessObject <em>Business Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessObject
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessObject()
     * @generated
     */
    int BUSINESS_OBJECT = 39;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_OBJECT_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessProcess <em>Business Process</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessProcess
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessProcess()
     * @generated
     */
    int BUSINESS_PROCESS = 40;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Process</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Product <em>Product</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Product
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getProduct()
     * @generated
     */
    int PRODUCT = 41;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Product</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRODUCT_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Representation <em>Representation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Representation
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getRepresentation()
     * @generated
     */
    int REPRESENTATION = 42;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Representation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPRESENTATION_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessRole <em>Business Role</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessRole
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessRole()
     * @generated
     */
    int BUSINESS_ROLE = 43;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Role</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_ROLE_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.BusinessService <em>Business Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.BusinessService
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessService()
     * @generated
     */
    int BUSINESS_SERVICE = 44;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Business Service</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_SERVICE_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Value <em>Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Value
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getValue()
     * @generated
     */
    int VALUE = 45;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__ARCHIMATE_MODEL = BUSINESS_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__ID = BUSINESS_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__NAME = BUSINESS_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__DOCUMENTATION = BUSINESS_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE__PROPERTIES = BUSINESS_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Value</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_FEATURE_COUNT = BUSINESS_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IApplicationLayerElement <em>Application Layer Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IApplicationLayerElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationLayerElement()
     * @generated
     */
    int APPLICATION_LAYER_ELEMENT = 46;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL = ARCHIMATE_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_LAYER_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_LAYER_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_LAYER_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_LAYER_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Application Layer Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_LAYER_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationCollaboration <em>Application Collaboration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ApplicationCollaboration
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationCollaboration()
     * @generated
     */
    int APPLICATION_COLLABORATION = 47;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Application Collaboration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COLLABORATION_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationComponent <em>Application Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ApplicationComponent
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationComponent()
     * @generated
     */
    int APPLICATION_COMPONENT = 48;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Application Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_COMPONENT_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationFunction <em>Application Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ApplicationFunction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationFunction()
     * @generated
     */
    int APPLICATION_FUNCTION = 49;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Application Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FUNCTION_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationInteraction <em>Application Interaction</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ApplicationInteraction
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationInteraction()
     * @generated
     */
    int APPLICATION_INTERACTION = 50;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Application Interaction</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERACTION_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationInterface <em>Application Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ApplicationInterface
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationInterface()
     * @generated
     */
    int APPLICATION_INTERFACE = 51;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Interface Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE__INTERFACE_TYPE = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Application Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_INTERFACE_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DataObject <em>Data Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DataObject
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDataObject()
     * @generated
     */
    int DATA_OBJECT = 52;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Data Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_OBJECT_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationService <em>Application Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ApplicationService
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationService()
     * @generated
     */
    int APPLICATION_SERVICE = 53;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__ARCHIMATE_MODEL = APPLICATION_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__ID = APPLICATION_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__NAME = APPLICATION_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__DOCUMENTATION = APPLICATION_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE__PROPERTIES = APPLICATION_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Application Service</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_SERVICE_FEATURE_COUNT = APPLICATION_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.ITechnologyLayerElement <em>Technology Layer Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.ITechnologyLayerElement
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getTechnologyLayerElement()
     * @generated
     */
    int TECHNOLOGY_LAYER_ELEMENT = 54;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL = ARCHIMATE_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_LAYER_ELEMENT__ID = ARCHIMATE_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_LAYER_ELEMENT__NAME = ARCHIMATE_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION = ARCHIMATE_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_LAYER_ELEMENT__PROPERTIES = ARCHIMATE_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Technology Layer Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT = ARCHIMATE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Artifact <em>Artifact</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Artifact
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArtifact()
     * @generated
     */
    int ARTIFACT = 55;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Artifact</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.CommunicationPath <em>Communication Path</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.CommunicationPath
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getCommunicationPath()
     * @generated
     */
    int COMMUNICATION_PATH = 56;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_PATH__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_PATH__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_PATH__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_PATH__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_PATH__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Communication Path</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_PATH_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Network <em>Network</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Network
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getNetwork()
     * @generated
     */
    int NETWORK = 57;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NETWORK__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NETWORK__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NETWORK__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NETWORK__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NETWORK__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Network</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NETWORK_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.InfrastructureInterface <em>Infrastructure Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.InfrastructureInterface
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getInfrastructureInterface()
     * @generated
     */
    int INFRASTRUCTURE_INTERFACE = 58;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The feature id for the '<em><b>Interface Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE__INTERFACE_TYPE = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Infrastructure Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_INTERFACE_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.InfrastructureService <em>Infrastructure Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.InfrastructureService
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getInfrastructureService()
     * @generated
     */
    int INFRASTRUCTURE_SERVICE = 59;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_SERVICE__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_SERVICE__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_SERVICE__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_SERVICE__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_SERVICE__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Infrastructure Service</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_SERVICE_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Node <em>Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Node
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getNode()
     * @generated
     */
    int NODE = 60;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.SystemSoftware <em>System Software</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.SystemSoftware
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSystemSoftware()
     * @generated
     */
    int SYSTEM_SOFTWARE = 61;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>System Software</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SYSTEM_SOFTWARE_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Device <em>Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Device
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDevice()
     * @generated
     */
    int DEVICE = 62;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__ARCHIMATE_MODEL = TECHNOLOGY_LAYER_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__ID = TECHNOLOGY_LAYER_ELEMENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__NAME = TECHNOLOGY_LAYER_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__DOCUMENTATION = TECHNOLOGY_LAYER_ELEMENT__DOCUMENTATION;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE__PROPERTIES = TECHNOLOGY_LAYER_ELEMENT__PROPERTIES;

    /**
     * The number of structural features of the '<em>Device</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEVICE_FEATURE_COUNT = TECHNOLOGY_LAYER_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModel <em>Diagram Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModel
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModel()
     * @generated
     */
    int DIAGRAM_MODEL = 65;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelComponent <em>Diagram Model Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelComponent
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelComponent()
     * @generated
     */
    int DIAGRAM_MODEL_COMPONENT = 63;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL = IDENTIFIER_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Diagram Model Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT = IDENTIFIER_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelObject <em>Diagram Model Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelObject
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelObject()
     * @generated
     */
    int DIAGRAM_MODEL_OBJECT = 67;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelArchimateObject
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelArchimateObject()
     * @generated
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT = 75;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IDiagramModelContainer <em>Diagram Model Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IDiagramModelContainer
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelContainer()
     * @generated
     */
    int DIAGRAM_MODEL_CONTAINER = 64;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONTAINER__DIAGRAM_MODEL = DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__ARCHIMATE_MODEL = ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__ID = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__NAME = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__DIAGRAM_MODEL = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__CHILDREN = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__DOCUMENTATION = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__PROPERTIES = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Connection Router Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Diagram Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_FEATURE_COUNT = ARCHIMATE_MODEL_ELEMENT_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.ArchimateDiagramModel <em>Diagram Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.ArchimateDiagramModel
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateDiagramModel()
     * @generated
     */
    int ARCHIMATE_DIAGRAM_MODEL = 74;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__ID = DIAGRAM_MODEL_COMPONENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__NAME = DIAGRAM_MODEL_COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL = DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FONT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FONT_COLOR = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__BOUNDS = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Source Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Target Connections</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT__FILL_COLOR = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Diagram Model Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_OBJECT_FEATURE_COUNT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelReference <em>Diagram Model Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelReference
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelReference()
     * @generated
     */
    int DIAGRAM_MODEL_REFERENCE = 66;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__DIAGRAM_MODEL = DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Referenced Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Diagram Model Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_REFERENCE_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelGroup <em>Diagram Model Group</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelGroup
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelGroup()
     * @generated
     */
    int DIAGRAM_MODEL_GROUP = 68;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__DIAGRAM_MODEL = DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

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
     * The number of structural features of the '<em>Diagram Model Group</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_GROUP_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelNote <em>Diagram Model Note</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelNote
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelNote()
     * @generated
     */
    int DIAGRAM_MODEL_NOTE = 69;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__DIAGRAM_MODEL = DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE__CONTENT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Diagram Model Note</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_NOTE_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelConnection <em>Diagram Model Connection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelConnection
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelConnection()
     * @generated
     */
    int DIAGRAM_MODEL_CONNECTION = 70;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__ID = DIAGRAM_MODEL_COMPONENT__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__NAME = DIAGRAM_MODEL_COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__DIAGRAM_MODEL = DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL;

    /**
     * The feature id for the '<em><b>Font</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__FONT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Font Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__FONT_COLOR = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TEXT_ALIGNMENT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Properties</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__PROPERTIES = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Documentation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__DOCUMENTATION = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TEXT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Text Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TEXT_POSITION = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__SOURCE = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TARGET = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Bendpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__BENDPOINTS = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Line Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__LINE_WIDTH = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Line Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__LINE_COLOR = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION__TYPE = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 12;

    /**
     * The number of structural features of the '<em>Diagram Model Connection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT = DIAGRAM_MODEL_COMPONENT_FEATURE_COUNT + 13;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelArchimateConnection
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelArchimateConnection()
     * @generated
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION = 76;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelBendpoint <em>Diagram Model Bendpoint</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.DiagramModelBendpoint
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelBendpoint()
     * @generated
     */
    int DIAGRAM_MODEL_BENDPOINT = 71;

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
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.IFontAttribute <em>Font Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.IFontAttribute
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFontAttribute()
     * @generated
     */
    int FONT_ATTRIBUTE = 72;

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
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FONT_ATTRIBUTE__TEXT_ALIGNMENT = 2;

    /**
     * The number of structural features of the '<em>Font Attribute</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FONT_ATTRIBUTE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.Bounds <em>Bounds</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.Bounds
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBounds()
     * @generated
     */
    int BOUNDS = 73;

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
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__ARCHIMATE_MODEL = DIAGRAM_MODEL__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__ID = DIAGRAM_MODEL__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__NAME = DIAGRAM_MODEL__NAME;

    /**
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARCHIMATE_DIAGRAM_MODEL__DIAGRAM_MODEL = DIAGRAM_MODEL__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__DIAGRAM_MODEL = DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Archimate Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Diagram Model Archimate Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_OBJECT_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__DIAGRAM_MODEL = DIAGRAM_MODEL_CONNECTION__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Text Alignment</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TEXT_ALIGNMENT = DIAGRAM_MODEL_CONNECTION__TEXT_ALIGNMENT;

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
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__TYPE = DIAGRAM_MODEL_CONNECTION__TYPE;

    /**
     * The feature id for the '<em><b>Relationship</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP = DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Diagram Model Archimate Connection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIAGRAM_MODEL_ARCHIMATE_CONNECTION_FEATURE_COUNT = DIAGRAM_MODEL_CONNECTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.SketchModel <em>Sketch Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.SketchModel
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSketchModel()
     * @generated
     */
    int SKETCH_MODEL = 77;

    /**
     * The feature id for the '<em><b>Archimate Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__ARCHIMATE_MODEL = DIAGRAM_MODEL__ARCHIMATE_MODEL;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__ID = DIAGRAM_MODEL__ID;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__NAME = DIAGRAM_MODEL__NAME;

    /**
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL__DIAGRAM_MODEL = DIAGRAM_MODEL__DIAGRAM_MODEL;

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
     * The number of structural features of the '<em>Sketch Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_FEATURE_COUNT = DIAGRAM_MODEL_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.SketchModelSticky <em>Sketch Model Sticky</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.SketchModelSticky
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSketchModelSticky()
     * @generated
     */
    int SKETCH_MODEL_STICKY = 78;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__DIAGRAM_MODEL = DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

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
     * The number of structural features of the '<em>Sketch Model Sticky</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_STICKY_FEATURE_COUNT = DIAGRAM_MODEL_OBJECT_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.impl.SketchModelActor <em>Sketch Model Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.impl.SketchModelActor
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSketchModelActor()
     * @generated
     */
    int SKETCH_MODEL_ACTOR = 79;

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
     * The feature id for the '<em><b>Diagram Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__DIAGRAM_MODEL = DIAGRAM_MODEL_OBJECT__DIAGRAM_MODEL;

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
     * The feature id for the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SKETCH_MODEL_ACTOR__FILL_COLOR = DIAGRAM_MODEL_OBJECT__FILL_COLOR;

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
     * The meta object id for the '{@link uk.ac.bolton.archimate.model.FolderType <em>Folder Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see uk.ac.bolton.archimate.model.FolderType
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFolderType()
     * @generated
     */
    int FOLDER_TYPE = 80;

    /**
     * The meta object id for the '<em>File</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.io.File
     * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFile()
     * @generated
     */
    int FILE = 81;


    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IAdapter <em>Adapter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Adapter</em>'.
     * @see uk.ac.bolton.archimate.model.IAdapter
     * @generated
     */
    EClass getAdapter();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Identifier</em>'.
     * @see uk.ac.bolton.archimate.model.IIdentifier
     * @generated
     */
    EClass getIdentifier();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IIdentifier#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see uk.ac.bolton.archimate.model.IIdentifier#getId()
     * @see #getIdentifier()
     * @generated
     */
    EAttribute getIdentifier_Id();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Properties</em>'.
     * @see uk.ac.bolton.archimate.model.IProperties
     * @generated
     */
    EClass getProperties();

    /**
     * Returns the meta object for the containment reference list '{@link uk.ac.bolton.archimate.model.IProperties#getProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Properties</em>'.
     * @see uk.ac.bolton.archimate.model.IProperties#getProperties()
     * @see #getProperties()
     * @generated
     */
    EReference getProperties_Properties();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.INameable <em>Nameable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Nameable</em>'.
     * @see uk.ac.bolton.archimate.model.INameable
     * @generated
     */
    EClass getNameable();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.INameable#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see uk.ac.bolton.archimate.model.INameable#getName()
     * @see #getNameable()
     * @generated
     */
    EAttribute getNameable_Name();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ITextContent <em>Text Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Content</em>'.
     * @see uk.ac.bolton.archimate.model.ITextContent
     * @generated
     */
    EClass getTextContent();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.ITextContent#getContent <em>Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Content</em>'.
     * @see uk.ac.bolton.archimate.model.ITextContent#getContent()
     * @see #getTextContent()
     * @generated
     */
    EAttribute getTextContent_Content();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDocumentable <em>Documentable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Documentable</em>'.
     * @see uk.ac.bolton.archimate.model.IDocumentable
     * @generated
     */
    EClass getDocumentable();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDocumentable#getDocumentation <em>Documentation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Documentation</em>'.
     * @see uk.ac.bolton.archimate.model.IDocumentable#getDocumentation()
     * @see #getDocumentable()
     * @generated
     */
    EAttribute getDocumentable_Documentation();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ICloneable <em>Cloneable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cloneable</em>'.
     * @see uk.ac.bolton.archimate.model.ICloneable
     * @generated
     */
    EClass getCloneable();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IFolderContainer <em>Folder Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Folder Container</em>'.
     * @see uk.ac.bolton.archimate.model.IFolderContainer
     * @generated
     */
    EClass getFolderContainer();

    /**
     * Returns the meta object for the containment reference list '{@link uk.ac.bolton.archimate.model.IFolderContainer#getFolders <em>Folders</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Folders</em>'.
     * @see uk.ac.bolton.archimate.model.IFolderContainer#getFolders()
     * @see #getFolderContainer()
     * @generated
     */
    EReference getFolderContainer_Folders();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IArchimateModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateModel
     * @generated
     */
    EClass getArchimateModel();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IArchimateModel#getPurpose <em>Purpose</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Purpose</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateModel#getPurpose()
     * @see #getArchimateModel()
     * @generated
     */
    EAttribute getArchimateModel_Purpose();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IArchimateModel#getFile <em>File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateModel#getFile()
     * @see #getArchimateModel()
     * @generated
     */
    EAttribute getArchimateModel_File();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IArchimateModel#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateModel#getVersion()
     * @see #getArchimateModel()
     * @generated
     */
    EAttribute getArchimateModel_Version();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IArchimateModelElement <em>Model Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Element</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateModelElement
     * @generated
     */
    EClass getArchimateModelElement();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IArchimateModelElement#getArchimateModel <em>Archimate Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Archimate Model</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateModelElement#getArchimateModel()
     * @see #getArchimateModelElement()
     * @generated
     */
    EReference getArchimateModelElement_ArchimateModel();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IFolder <em>Folder</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Folder</em>'.
     * @see uk.ac.bolton.archimate.model.IFolder
     * @generated
     */
    EClass getFolder();

    /**
     * Returns the meta object for the containment reference list '{@link uk.ac.bolton.archimate.model.IFolder#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see uk.ac.bolton.archimate.model.IFolder#getElements()
     * @see #getFolder()
     * @generated
     */
    EReference getFolder_Elements();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IFolder#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see uk.ac.bolton.archimate.model.IFolder#getType()
     * @see #getFolder()
     * @generated
     */
    EAttribute getFolder_Type();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IProperty <em>Property</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property</em>'.
     * @see uk.ac.bolton.archimate.model.IProperty
     * @generated
     */
    EClass getProperty();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IProperty#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see uk.ac.bolton.archimate.model.IProperty#getKey()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Key();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IProperty#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see uk.ac.bolton.archimate.model.IProperty#getValue()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Value();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IArchimateElement <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Element</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateElement
     * @generated
     */
    EClass getArchimateElement();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IJunctionElement <em>Junction Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Junction Element</em>'.
     * @see uk.ac.bolton.archimate.model.IJunctionElement
     * @generated
     */
    EClass getJunctionElement();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IInterfaceElement <em>Interface Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Interface Element</em>'.
     * @see uk.ac.bolton.archimate.model.IInterfaceElement
     * @generated
     */
    EClass getInterfaceElement();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IInterfaceElement#getInterfaceType <em>Interface Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Interface Type</em>'.
     * @see uk.ac.bolton.archimate.model.IInterfaceElement#getInterfaceType()
     * @see #getInterfaceElement()
     * @generated
     */
    EAttribute getInterfaceElement_InterfaceType();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IJunction <em>Junction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Junction</em>'.
     * @see uk.ac.bolton.archimate.model.IJunction
     * @generated
     */
    EClass getJunction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IAndJunction <em>And Junction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>And Junction</em>'.
     * @see uk.ac.bolton.archimate.model.IAndJunction
     * @generated
     */
    EClass getAndJunction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IOrJunction <em>Or Junction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Or Junction</em>'.
     * @see uk.ac.bolton.archimate.model.IOrJunction
     * @generated
     */
    EClass getOrJunction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IRelationship <em>Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IRelationship
     * @generated
     */
    EClass getRelationship();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IRelationship#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source</em>'.
     * @see uk.ac.bolton.archimate.model.IRelationship#getSource()
     * @see #getRelationship()
     * @generated
     */
    EReference getRelationship_Source();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IRelationship#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target</em>'.
     * @see uk.ac.bolton.archimate.model.IRelationship#getTarget()
     * @see #getRelationship()
     * @generated
     */
    EReference getRelationship_Target();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IAccessRelationship <em>Access Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Access Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IAccessRelationship
     * @generated
     */
    EClass getAccessRelationship();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IAccessRelationship#getAccessType <em>Access Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Type</em>'.
     * @see uk.ac.bolton.archimate.model.IAccessRelationship#getAccessType()
     * @see #getAccessRelationship()
     * @generated
     */
    EAttribute getAccessRelationship_AccessType();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IAggregationRelationship <em>Aggregation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Aggregation Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IAggregationRelationship
     * @generated
     */
    EClass getAggregationRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IAssignmentRelationship <em>Assignment Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assignment Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IAssignmentRelationship
     * @generated
     */
    EClass getAssignmentRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IAssociationRelationship <em>Association Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Association Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IAssociationRelationship
     * @generated
     */
    EClass getAssociationRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ICompositionRelationship <em>Composition Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Composition Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.ICompositionRelationship
     * @generated
     */
    EClass getCompositionRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IFlowRelationship <em>Flow Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Flow Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IFlowRelationship
     * @generated
     */
    EClass getFlowRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IRealisationRelationship <em>Realisation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Realisation Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IRealisationRelationship
     * @generated
     */
    EClass getRealisationRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ISpecialisationRelationship <em>Specialisation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Specialisation Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.ISpecialisationRelationship
     * @generated
     */
    EClass getSpecialisationRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ITriggeringRelationship <em>Triggering Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Triggering Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.ITriggeringRelationship
     * @generated
     */
    EClass getTriggeringRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IUsedByRelationship <em>Used By Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Used By Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IUsedByRelationship
     * @generated
     */
    EClass getUsedByRelationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessLayerElement <em>Business Layer Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Layer Element</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessLayerElement
     * @generated
     */
    EClass getBusinessLayerElement();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessActivity <em>Business Activity</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Activity</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessActivity
     * @generated
     */
    EClass getBusinessActivity();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessActor <em>Business Actor</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Actor</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessActor
     * @generated
     */
    EClass getBusinessActor();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessCollaboration <em>Business Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Collaboration</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessCollaboration
     * @generated
     */
    EClass getBusinessCollaboration();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IContract <em>Contract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contract</em>'.
     * @see uk.ac.bolton.archimate.model.IContract
     * @generated
     */
    EClass getContract();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessEvent <em>Business Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Event</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessEvent
     * @generated
     */
    EClass getBusinessEvent();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessFunction <em>Business Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Function</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessFunction
     * @generated
     */
    EClass getBusinessFunction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessInteraction <em>Business Interaction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Interaction</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessInteraction
     * @generated
     */
    EClass getBusinessInteraction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessInterface <em>Business Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Interface</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessInterface
     * @generated
     */
    EClass getBusinessInterface();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Meaning</em>'.
     * @see uk.ac.bolton.archimate.model.IMeaning
     * @generated
     */
    EClass getMeaning();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessObject <em>Business Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Object</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessObject
     * @generated
     */
    EClass getBusinessObject();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessProcess <em>Business Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Process</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessProcess
     * @generated
     */
    EClass getBusinessProcess();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IProduct <em>Product</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Product</em>'.
     * @see uk.ac.bolton.archimate.model.IProduct
     * @generated
     */
    EClass getProduct();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IRepresentation <em>Representation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Representation</em>'.
     * @see uk.ac.bolton.archimate.model.IRepresentation
     * @generated
     */
    EClass getRepresentation();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessRole <em>Business Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Role</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessRole
     * @generated
     */
    EClass getBusinessRole();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBusinessService <em>Business Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Service</em>'.
     * @see uk.ac.bolton.archimate.model.IBusinessService
     * @generated
     */
    EClass getBusinessService();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Value</em>'.
     * @see uk.ac.bolton.archimate.model.IValue
     * @generated
     */
    EClass getValue();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationLayerElement <em>Application Layer Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Layer Element</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationLayerElement
     * @generated
     */
    EClass getApplicationLayerElement();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationCollaboration <em>Application Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Collaboration</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationCollaboration
     * @generated
     */
    EClass getApplicationCollaboration();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationComponent <em>Application Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Component</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationComponent
     * @generated
     */
    EClass getApplicationComponent();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationFunction <em>Application Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Function</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationFunction
     * @generated
     */
    EClass getApplicationFunction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationInteraction <em>Application Interaction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Interaction</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationInteraction
     * @generated
     */
    EClass getApplicationInteraction();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationInterface <em>Application Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Interface</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationInterface
     * @generated
     */
    EClass getApplicationInterface();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDataObject <em>Data Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Object</em>'.
     * @see uk.ac.bolton.archimate.model.IDataObject
     * @generated
     */
    EClass getDataObject();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IApplicationService <em>Application Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application Service</em>'.
     * @see uk.ac.bolton.archimate.model.IApplicationService
     * @generated
     */
    EClass getApplicationService();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ITechnologyLayerElement <em>Technology Layer Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technology Layer Element</em>'.
     * @see uk.ac.bolton.archimate.model.ITechnologyLayerElement
     * @generated
     */
    EClass getTechnologyLayerElement();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IArtifact <em>Artifact</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Artifact</em>'.
     * @see uk.ac.bolton.archimate.model.IArtifact
     * @generated
     */
    EClass getArtifact();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ICommunicationPath <em>Communication Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Communication Path</em>'.
     * @see uk.ac.bolton.archimate.model.ICommunicationPath
     * @generated
     */
    EClass getCommunicationPath();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.INetwork <em>Network</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Network</em>'.
     * @see uk.ac.bolton.archimate.model.INetwork
     * @generated
     */
    EClass getNetwork();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IInfrastructureInterface <em>Infrastructure Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Infrastructure Interface</em>'.
     * @see uk.ac.bolton.archimate.model.IInfrastructureInterface
     * @generated
     */
    EClass getInfrastructureInterface();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IInfrastructureService <em>Infrastructure Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Infrastructure Service</em>'.
     * @see uk.ac.bolton.archimate.model.IInfrastructureService
     * @generated
     */
    EClass getInfrastructureService();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.INode <em>Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Node</em>'.
     * @see uk.ac.bolton.archimate.model.INode
     * @generated
     */
    EClass getNode();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ISystemSoftware <em>System Software</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>System Software</em>'.
     * @see uk.ac.bolton.archimate.model.ISystemSoftware
     * @generated
     */
    EClass getSystemSoftware();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDevice <em>Device</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Device</em>'.
     * @see uk.ac.bolton.archimate.model.IDevice
     * @generated
     */
    EClass getDevice();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModel
     * @generated
     */
    EClass getDiagramModel();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModel#getConnectionRouterType <em>Connection Router Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Connection Router Type</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModel#getConnectionRouterType()
     * @see #getDiagramModel()
     * @generated
     */
    EAttribute getDiagramModel_ConnectionRouterType();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IArchimateDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateDiagramModel
     * @generated
     */
    EClass getArchimateDiagramModel();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IArchimateDiagramModel#getViewpoint <em>Viewpoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Viewpoint</em>'.
     * @see uk.ac.bolton.archimate.model.IArchimateDiagramModel#getViewpoint()
     * @see #getArchimateDiagramModel()
     * @generated
     */
    EAttribute getArchimateDiagramModel_Viewpoint();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelReference <em>Diagram Model Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Reference</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelReference
     * @generated
     */
    EClass getDiagramModelReference();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IDiagramModelReference#getReferencedModel <em>Referenced Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Referenced Model</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelReference#getReferencedModel()
     * @see #getDiagramModelReference()
     * @generated
     */
    EReference getDiagramModelReference_ReferencedModel();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelComponent <em>Diagram Model Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Component</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelComponent
     * @generated
     */
    EClass getDiagramModelComponent();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IDiagramModelComponent#getDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Diagram Model</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelComponent#getDiagramModel()
     * @see #getDiagramModelComponent()
     * @generated
     */
    EReference getDiagramModelComponent_DiagramModel();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelObject <em>Diagram Model Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Object</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelObject
     * @generated
     */
    EClass getDiagramModelObject();

    /**
     * Returns the meta object for the containment reference '{@link uk.ac.bolton.archimate.model.IDiagramModelObject#getBounds <em>Bounds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounds</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelObject#getBounds()
     * @see #getDiagramModelObject()
     * @generated
     */
    EReference getDiagramModelObject_Bounds();

    /**
     * Returns the meta object for the containment reference list '{@link uk.ac.bolton.archimate.model.IDiagramModelObject#getSourceConnections <em>Source Connections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Source Connections</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelObject#getSourceConnections()
     * @see #getDiagramModelObject()
     * @generated
     */
    EReference getDiagramModelObject_SourceConnections();

    /**
     * Returns the meta object for the reference list '{@link uk.ac.bolton.archimate.model.IDiagramModelObject#getTargetConnections <em>Target Connections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Connections</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelObject#getTargetConnections()
     * @see #getDiagramModelObject()
     * @generated
     */
    EReference getDiagramModelObject_TargetConnections();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelObject#getFillColor <em>Fill Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill Color</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelObject#getFillColor()
     * @see #getDiagramModelObject()
     * @generated
     */
    EAttribute getDiagramModelObject_FillColor();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Archimate Object</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateObject
     * @generated
     */
    EClass getDiagramModelArchimateObject();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getArchimateElement <em>Archimate Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Archimate Element</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getArchimateElement()
     * @see #getDiagramModelArchimateObject()
     * @generated
     */
    EReference getDiagramModelArchimateObject_ArchimateElement();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getType()
     * @see #getDiagramModelArchimateObject()
     * @generated
     */
    EAttribute getDiagramModelArchimateObject_Type();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelContainer <em>Diagram Model Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Container</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelContainer
     * @generated
     */
    EClass getDiagramModelContainer();

    /**
     * Returns the meta object for the containment reference list '{@link uk.ac.bolton.archimate.model.IDiagramModelContainer#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Children</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelContainer#getChildren()
     * @see #getDiagramModelContainer()
     * @generated
     */
    EReference getDiagramModelContainer_Children();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelGroup <em>Diagram Model Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Group</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelGroup
     * @generated
     */
    EClass getDiagramModelGroup();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelNote <em>Diagram Model Note</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Note</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelNote
     * @generated
     */
    EClass getDiagramModelNote();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection <em>Diagram Model Connection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Connection</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection
     * @generated
     */
    EClass getDiagramModelConnection();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getText <em>Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getText()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_Text();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getTextPosition <em>Text Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Position</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getTextPosition()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_TextPosition();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getSource()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EReference getDiagramModelConnection_Source();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getTarget()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EReference getDiagramModelConnection_Target();

    /**
     * Returns the meta object for the containment reference list '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getBendpoints <em>Bendpoints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bendpoints</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getBendpoints()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EReference getDiagramModelConnection_Bendpoints();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineWidth <em>Line Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Line Width</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineWidth()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_LineWidth();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineColor <em>Line Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Line Color</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineColor()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_LineColor();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection#getType()
     * @see #getDiagramModelConnection()
     * @generated
     */
    EAttribute getDiagramModelConnection_Type();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Archimate Connection</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection
     * @generated
     */
    EClass getDiagramModelArchimateConnection();

    /**
     * Returns the meta object for the reference '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection#getRelationship <em>Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Relationship</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection#getRelationship()
     * @see #getDiagramModelArchimateConnection()
     * @generated
     */
    EReference getDiagramModelArchimateConnection_Relationship();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint <em>Diagram Model Bendpoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Diagram Model Bendpoint</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelBendpoint
     * @generated
     */
    EClass getDiagramModelBendpoint();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getStartX <em>Start X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start X</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getStartX()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_StartX();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getStartY <em>Start Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Y</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getStartY()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_StartY();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getEndX <em>End X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End X</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getEndX()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_EndX();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getEndY <em>End Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Y</em>'.
     * @see uk.ac.bolton.archimate.model.IDiagramModelBendpoint#getEndY()
     * @see #getDiagramModelBendpoint()
     * @generated
     */
    EAttribute getDiagramModelBendpoint_EndY();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IFontAttribute <em>Font Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Font Attribute</em>'.
     * @see uk.ac.bolton.archimate.model.IFontAttribute
     * @generated
     */
    EClass getFontAttribute();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IFontAttribute#getFont <em>Font</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Font</em>'.
     * @see uk.ac.bolton.archimate.model.IFontAttribute#getFont()
     * @see #getFontAttribute()
     * @generated
     */
    EAttribute getFontAttribute_Font();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IFontAttribute#getFontColor <em>Font Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Font Color</em>'.
     * @see uk.ac.bolton.archimate.model.IFontAttribute#getFontColor()
     * @see #getFontAttribute()
     * @generated
     */
    EAttribute getFontAttribute_FontColor();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IFontAttribute#getTextAlignment <em>Text Alignment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Alignment</em>'.
     * @see uk.ac.bolton.archimate.model.IFontAttribute#getTextAlignment()
     * @see #getFontAttribute()
     * @generated
     */
    EAttribute getFontAttribute_TextAlignment();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.IBounds <em>Bounds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bounds</em>'.
     * @see uk.ac.bolton.archimate.model.IBounds
     * @generated
     */
    EClass getBounds();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IBounds#getX <em>X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>X</em>'.
     * @see uk.ac.bolton.archimate.model.IBounds#getX()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_X();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IBounds#getY <em>Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Y</em>'.
     * @see uk.ac.bolton.archimate.model.IBounds#getY()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_Y();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IBounds#getWidth <em>Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Width</em>'.
     * @see uk.ac.bolton.archimate.model.IBounds#getWidth()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_Width();

    /**
     * Returns the meta object for the attribute '{@link uk.ac.bolton.archimate.model.IBounds#getHeight <em>Height</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Height</em>'.
     * @see uk.ac.bolton.archimate.model.IBounds#getHeight()
     * @see #getBounds()
     * @generated
     */
    EAttribute getBounds_Height();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ISketchModel <em>Sketch Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sketch Model</em>'.
     * @see uk.ac.bolton.archimate.model.ISketchModel
     * @generated
     */
    EClass getSketchModel();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ISketchModelSticky <em>Sketch Model Sticky</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sketch Model Sticky</em>'.
     * @see uk.ac.bolton.archimate.model.ISketchModelSticky
     * @generated
     */
    EClass getSketchModelSticky();

    /**
     * Returns the meta object for class '{@link uk.ac.bolton.archimate.model.ISketchModelActor <em>Sketch Model Actor</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sketch Model Actor</em>'.
     * @see uk.ac.bolton.archimate.model.ISketchModelActor
     * @generated
     */
    EClass getSketchModelActor();

    /**
     * Returns the meta object for enum '{@link uk.ac.bolton.archimate.model.FolderType <em>Folder Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Folder Type</em>'.
     * @see uk.ac.bolton.archimate.model.FolderType
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IAdapter <em>Adapter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IAdapter
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAdapter()
         * @generated
         */
        EClass ADAPTER = eINSTANCE.getAdapter();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IIdentifier <em>Identifier</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IIdentifier
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getIdentifier()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IProperties <em>Properties</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IProperties
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getProperties()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.INameable <em>Nameable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.INameable
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getNameable()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.ITextContent <em>Text Content</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.ITextContent
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getTextContent()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IDocumentable <em>Documentable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IDocumentable
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDocumentable()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.ICloneable <em>Cloneable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.ICloneable
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getCloneable()
         * @generated
         */
        EClass CLONEABLE = eINSTANCE.getCloneable();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IFolderContainer <em>Folder Container</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IFolderContainer
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFolderContainer()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ArchimateModel <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ArchimateModel
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateModel()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IArchimateModelElement <em>Model Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IArchimateModelElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateModelElement()
         * @generated
         */
        EClass ARCHIMATE_MODEL_ELEMENT = eINSTANCE.getArchimateModelElement();

        /**
         * The meta object literal for the '<em><b>Archimate Model</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL = eINSTANCE.getArchimateModelElement_ArchimateModel();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Folder <em>Folder</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Folder
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFolder()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Property <em>Property</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Property
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getProperty()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ArchimateElement <em>Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ArchimateElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateElement()
         * @generated
         */
        EClass ARCHIMATE_ELEMENT = eINSTANCE.getArchimateElement();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IJunctionElement <em>Junction Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IJunctionElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getJunctionElement()
         * @generated
         */
        EClass JUNCTION_ELEMENT = eINSTANCE.getJunctionElement();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IInterfaceElement <em>Interface Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IInterfaceElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getInterfaceElement()
         * @generated
         */
        EClass INTERFACE_ELEMENT = eINSTANCE.getInterfaceElement();

        /**
         * The meta object literal for the '<em><b>Interface Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INTERFACE_ELEMENT__INTERFACE_TYPE = eINSTANCE.getInterfaceElement_InterfaceType();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Junction <em>Junction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Junction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getJunction()
         * @generated
         */
        EClass JUNCTION = eINSTANCE.getJunction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.AndJunction <em>And Junction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.AndJunction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAndJunction()
         * @generated
         */
        EClass AND_JUNCTION = eINSTANCE.getAndJunction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.OrJunction <em>Or Junction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.OrJunction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getOrJunction()
         * @generated
         */
        EClass OR_JUNCTION = eINSTANCE.getOrJunction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Relationship <em>Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Relationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getRelationship()
         * @generated
         */
        EClass RELATIONSHIP = eINSTANCE.getRelationship();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RELATIONSHIP__SOURCE = eINSTANCE.getRelationship_Source();

        /**
         * The meta object literal for the '<em><b>Target</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RELATIONSHIP__TARGET = eINSTANCE.getRelationship_Target();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.AccessRelationship <em>Access Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.AccessRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAccessRelationship()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.AggregationRelationship <em>Aggregation Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.AggregationRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAggregationRelationship()
         * @generated
         */
        EClass AGGREGATION_RELATIONSHIP = eINSTANCE.getAggregationRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.AssignmentRelationship <em>Assignment Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.AssignmentRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAssignmentRelationship()
         * @generated
         */
        EClass ASSIGNMENT_RELATIONSHIP = eINSTANCE.getAssignmentRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.AssociationRelationship <em>Association Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.AssociationRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getAssociationRelationship()
         * @generated
         */
        EClass ASSOCIATION_RELATIONSHIP = eINSTANCE.getAssociationRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.CompositionRelationship <em>Composition Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.CompositionRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getCompositionRelationship()
         * @generated
         */
        EClass COMPOSITION_RELATIONSHIP = eINSTANCE.getCompositionRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.FlowRelationship <em>Flow Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.FlowRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFlowRelationship()
         * @generated
         */
        EClass FLOW_RELATIONSHIP = eINSTANCE.getFlowRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.RealisationRelationship <em>Realisation Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.RealisationRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getRealisationRelationship()
         * @generated
         */
        EClass REALISATION_RELATIONSHIP = eINSTANCE.getRealisationRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.SpecialisationRelationship <em>Specialisation Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.SpecialisationRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSpecialisationRelationship()
         * @generated
         */
        EClass SPECIALISATION_RELATIONSHIP = eINSTANCE.getSpecialisationRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.TriggeringRelationship <em>Triggering Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.TriggeringRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getTriggeringRelationship()
         * @generated
         */
        EClass TRIGGERING_RELATIONSHIP = eINSTANCE.getTriggeringRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.UsedByRelationship <em>Used By Relationship</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.UsedByRelationship
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getUsedByRelationship()
         * @generated
         */
        EClass USED_BY_RELATIONSHIP = eINSTANCE.getUsedByRelationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IBusinessLayerElement <em>Business Layer Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IBusinessLayerElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessLayerElement()
         * @generated
         */
        EClass BUSINESS_LAYER_ELEMENT = eINSTANCE.getBusinessLayerElement();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessActivity <em>Business Activity</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessActivity
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessActivity()
         * @generated
         */
        EClass BUSINESS_ACTIVITY = eINSTANCE.getBusinessActivity();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessActor <em>Business Actor</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessActor
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessActor()
         * @generated
         */
        EClass BUSINESS_ACTOR = eINSTANCE.getBusinessActor();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessCollaboration <em>Business Collaboration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessCollaboration
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessCollaboration()
         * @generated
         */
        EClass BUSINESS_COLLABORATION = eINSTANCE.getBusinessCollaboration();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Contract <em>Contract</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Contract
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getContract()
         * @generated
         */
        EClass CONTRACT = eINSTANCE.getContract();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessEvent <em>Business Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessEvent
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessEvent()
         * @generated
         */
        EClass BUSINESS_EVENT = eINSTANCE.getBusinessEvent();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessFunction <em>Business Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessFunction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessFunction()
         * @generated
         */
        EClass BUSINESS_FUNCTION = eINSTANCE.getBusinessFunction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessInteraction <em>Business Interaction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessInteraction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessInteraction()
         * @generated
         */
        EClass BUSINESS_INTERACTION = eINSTANCE.getBusinessInteraction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessInterface <em>Business Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessInterface
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessInterface()
         * @generated
         */
        EClass BUSINESS_INTERFACE = eINSTANCE.getBusinessInterface();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Meaning <em>Meaning</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Meaning
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getMeaning()
         * @generated
         */
        EClass MEANING = eINSTANCE.getMeaning();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessObject <em>Business Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessObject
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessObject()
         * @generated
         */
        EClass BUSINESS_OBJECT = eINSTANCE.getBusinessObject();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessProcess <em>Business Process</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessProcess
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessProcess()
         * @generated
         */
        EClass BUSINESS_PROCESS = eINSTANCE.getBusinessProcess();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Product <em>Product</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Product
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getProduct()
         * @generated
         */
        EClass PRODUCT = eINSTANCE.getProduct();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Representation <em>Representation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Representation
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getRepresentation()
         * @generated
         */
        EClass REPRESENTATION = eINSTANCE.getRepresentation();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessRole <em>Business Role</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessRole
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessRole()
         * @generated
         */
        EClass BUSINESS_ROLE = eINSTANCE.getBusinessRole();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.BusinessService <em>Business Service</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.BusinessService
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBusinessService()
         * @generated
         */
        EClass BUSINESS_SERVICE = eINSTANCE.getBusinessService();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Value <em>Value</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Value
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getValue()
         * @generated
         */
        EClass VALUE = eINSTANCE.getValue();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IApplicationLayerElement <em>Application Layer Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IApplicationLayerElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationLayerElement()
         * @generated
         */
        EClass APPLICATION_LAYER_ELEMENT = eINSTANCE.getApplicationLayerElement();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationCollaboration <em>Application Collaboration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ApplicationCollaboration
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationCollaboration()
         * @generated
         */
        EClass APPLICATION_COLLABORATION = eINSTANCE.getApplicationCollaboration();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationComponent <em>Application Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ApplicationComponent
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationComponent()
         * @generated
         */
        EClass APPLICATION_COMPONENT = eINSTANCE.getApplicationComponent();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationFunction <em>Application Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ApplicationFunction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationFunction()
         * @generated
         */
        EClass APPLICATION_FUNCTION = eINSTANCE.getApplicationFunction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationInteraction <em>Application Interaction</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ApplicationInteraction
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationInteraction()
         * @generated
         */
        EClass APPLICATION_INTERACTION = eINSTANCE.getApplicationInteraction();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationInterface <em>Application Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ApplicationInterface
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationInterface()
         * @generated
         */
        EClass APPLICATION_INTERFACE = eINSTANCE.getApplicationInterface();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DataObject <em>Data Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DataObject
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDataObject()
         * @generated
         */
        EClass DATA_OBJECT = eINSTANCE.getDataObject();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ApplicationService <em>Application Service</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ApplicationService
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getApplicationService()
         * @generated
         */
        EClass APPLICATION_SERVICE = eINSTANCE.getApplicationService();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.ITechnologyLayerElement <em>Technology Layer Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.ITechnologyLayerElement
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getTechnologyLayerElement()
         * @generated
         */
        EClass TECHNOLOGY_LAYER_ELEMENT = eINSTANCE.getTechnologyLayerElement();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Artifact <em>Artifact</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Artifact
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArtifact()
         * @generated
         */
        EClass ARTIFACT = eINSTANCE.getArtifact();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.CommunicationPath <em>Communication Path</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.CommunicationPath
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getCommunicationPath()
         * @generated
         */
        EClass COMMUNICATION_PATH = eINSTANCE.getCommunicationPath();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Network <em>Network</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Network
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getNetwork()
         * @generated
         */
        EClass NETWORK = eINSTANCE.getNetwork();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.InfrastructureInterface <em>Infrastructure Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.InfrastructureInterface
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getInfrastructureInterface()
         * @generated
         */
        EClass INFRASTRUCTURE_INTERFACE = eINSTANCE.getInfrastructureInterface();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.InfrastructureService <em>Infrastructure Service</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.InfrastructureService
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getInfrastructureService()
         * @generated
         */
        EClass INFRASTRUCTURE_SERVICE = eINSTANCE.getInfrastructureService();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Node <em>Node</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Node
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getNode()
         * @generated
         */
        EClass NODE = eINSTANCE.getNode();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.SystemSoftware <em>System Software</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.SystemSoftware
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSystemSoftware()
         * @generated
         */
        EClass SYSTEM_SOFTWARE = eINSTANCE.getSystemSoftware();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Device <em>Device</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Device
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDevice()
         * @generated
         */
        EClass DEVICE = eINSTANCE.getDevice();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModel <em>Diagram Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModel
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModel()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.ArchimateDiagramModel <em>Diagram Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.ArchimateDiagramModel
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getArchimateDiagramModel()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelReference <em>Diagram Model Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelReference
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelReference()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelComponent <em>Diagram Model Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelComponent
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelComponent()
         * @generated
         */
        EClass DIAGRAM_MODEL_COMPONENT = eINSTANCE.getDiagramModelComponent();

        /**
         * The meta object literal for the '<em><b>Diagram Model</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL = eINSTANCE.getDiagramModelComponent_DiagramModel();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelObject <em>Diagram Model Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelObject
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelObject()
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
         * The meta object literal for the '<em><b>Source Connections</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS = eINSTANCE.getDiagramModelObject_SourceConnections();

        /**
         * The meta object literal for the '<em><b>Target Connections</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS = eINSTANCE.getDiagramModelObject_TargetConnections();

        /**
         * The meta object literal for the '<em><b>Fill Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_OBJECT__FILL_COLOR = eINSTANCE.getDiagramModelObject_FillColor();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelArchimateObject
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelArchimateObject()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IDiagramModelContainer <em>Diagram Model Container</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IDiagramModelContainer
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelContainer()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelGroup <em>Diagram Model Group</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelGroup
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelGroup()
         * @generated
         */
        EClass DIAGRAM_MODEL_GROUP = eINSTANCE.getDiagramModelGroup();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelNote <em>Diagram Model Note</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelNote
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelNote()
         * @generated
         */
        EClass DIAGRAM_MODEL_NOTE = eINSTANCE.getDiagramModelNote();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelConnection <em>Diagram Model Connection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelConnection
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelConnection()
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
         * The meta object literal for the '<em><b>Line Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_CONNECTION__LINE_WIDTH = eINSTANCE.getDiagramModelConnection_LineWidth();

        /**
         * The meta object literal for the '<em><b>Line Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_CONNECTION__LINE_COLOR = eINSTANCE.getDiagramModelConnection_LineColor();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIAGRAM_MODEL_CONNECTION__TYPE = eINSTANCE.getDiagramModelConnection_Type();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelArchimateConnection
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelArchimateConnection()
         * @generated
         */
        EClass DIAGRAM_MODEL_ARCHIMATE_CONNECTION = eINSTANCE.getDiagramModelArchimateConnection();

        /**
         * The meta object literal for the '<em><b>Relationship</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP = eINSTANCE.getDiagramModelArchimateConnection_Relationship();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.DiagramModelBendpoint <em>Diagram Model Bendpoint</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.DiagramModelBendpoint
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getDiagramModelBendpoint()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.IFontAttribute <em>Font Attribute</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.IFontAttribute
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFontAttribute()
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
         * The meta object literal for the '<em><b>Text Alignment</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FONT_ATTRIBUTE__TEXT_ALIGNMENT = eINSTANCE.getFontAttribute_TextAlignment();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.Bounds <em>Bounds</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.Bounds
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getBounds()
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
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.SketchModel <em>Sketch Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.SketchModel
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSketchModel()
         * @generated
         */
        EClass SKETCH_MODEL = eINSTANCE.getSketchModel();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.SketchModelSticky <em>Sketch Model Sticky</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.SketchModelSticky
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSketchModelSticky()
         * @generated
         */
        EClass SKETCH_MODEL_STICKY = eINSTANCE.getSketchModelSticky();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.impl.SketchModelActor <em>Sketch Model Actor</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.impl.SketchModelActor
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getSketchModelActor()
         * @generated
         */
        EClass SKETCH_MODEL_ACTOR = eINSTANCE.getSketchModelActor();

        /**
         * The meta object literal for the '{@link uk.ac.bolton.archimate.model.FolderType <em>Folder Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see uk.ac.bolton.archimate.model.FolderType
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFolderType()
         * @generated
         */
        EEnum FOLDER_TYPE = eINSTANCE.getFolderType();

        /**
         * The meta object literal for the '<em>File</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.io.File
         * @see uk.ac.bolton.archimate.model.impl.ArchimatePackage#getFile()
         * @generated
         */
        EDataType FILE = eINSTANCE.getFile();

    }

} //IArchimatePackage
