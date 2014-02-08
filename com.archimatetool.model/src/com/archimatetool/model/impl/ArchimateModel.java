/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IApplicationLayerElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBusinessLayerElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IMetadata;
import com.archimatetool.model.IImplementationMigrationElement;
import com.archimatetool.model.IJunctionElement;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.ITechnologyLayerElement;
import com.archimatetool.model.util.IDAdapter;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getFolders <em>Folders</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getName <em>Name</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getId <em>Id</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getArchimateModel <em>Archimate Model</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getPurpose <em>Purpose</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getFile <em>File</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getVersion <em>Version</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArchimateModel extends EObjectImpl implements IArchimateModel {
    /**
     * The cached value of the '{@link #getFolders() <em>Folders</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFolders()
     * @generated
     * @ordered
     */
    protected EList<IFolder> folders;
    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = ""; //$NON-NLS-1$
    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;
    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final String ID_EDEFAULT = null;
    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected String id = ID_EDEFAULT;
    /**
     * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProperties()
     * @generated
     * @ordered
     */
    protected EList<IProperty> properties;
    /**
     * The default value of the '{@link #getPurpose() <em>Purpose</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPurpose()
     * @generated
     * @ordered
     */
    protected static final String PURPOSE_EDEFAULT = null;
    /**
     * The cached value of the '{@link #getPurpose() <em>Purpose</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPurpose()
     * @generated
     * @ordered
     */
    protected String purpose = PURPOSE_EDEFAULT;
    
    /**
     * The default value of the '{@link #getFile() <em>File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFile()
     * @generated
     * @ordered
     */
    protected static final File FILE_EDEFAULT = null;
    /**
     * The cached value of the '{@link #getFile() <em>File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFile()
     * @generated
     * @ordered
     */
    protected File file = FILE_EDEFAULT;
    
    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = ""; //$NON-NLS-1$
    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;
    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected IMetadata metadata;
    /**
     * Adapter Map for arbitrary objects
     */
    private Map<Object, Object> fAdapterMap = new HashMap<Object, Object>();
    
    /**
     * ID Adapter
     */
    private IDAdapter fIDAdapter = new IDAdapter();


    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    protected ArchimateModel() {
        super();
        eAdapters().add(fIDAdapter);
    }
    
    /**
     * Add any default folders
     */
    protected void addDefaultFolders() {
        if(getFolder(FolderType.BUSINESS) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_0);
            folder.setType(FolderType.BUSINESS);
            getFolders().add(0, folder);
        }

        if(getFolder(FolderType.APPLICATION) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_1);
            folder.setType(FolderType.APPLICATION);
            getFolders().add(1, folder);
        }

        if(getFolder(FolderType.TECHNOLOGY) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_2);
            folder.setType(FolderType.TECHNOLOGY);
            getFolders().add(2, folder);
        }

        if(getFolder(FolderType.MOTIVATION) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_3);
            folder.setType(FolderType.MOTIVATION);
            getFolders().add(3, folder);
        }

        if(getFolder(FolderType.IMPLEMENTATION_MIGRATION) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_4);
            folder.setType(FolderType.IMPLEMENTATION_MIGRATION);
            getFolders().add(4, folder);
        }
        
        if(getFolder(FolderType.CONNECTORS) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_5);
            folder.setType(FolderType.CONNECTORS);
            getFolders().add(5, folder);
        }

        if(getFolder(FolderType.RELATIONS) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_6);
            folder.setType(FolderType.RELATIONS);
            getFolders().add(6, folder);
        }

        if(getFolder(FolderType.DIAGRAMS) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName(Messages.ArchimateModel_7);
            folder.setType(FolderType.DIAGRAMS);
            getFolders().add(getFolders().size(), folder); // Make sure this is last
        }
    }
    
    /**
     * <!-- begin-user-doc -->
     * This folder is optional so we add it as needed
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IFolder addDerivedRelationsFolder() {
        addDefaultFolders(); // Check they haven't been deleted
        
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        folder.setName(Messages.ArchimateModel_8);
        folder.setType(FolderType.DERIVED);
        int index = getFolders().indexOf(getFolder(FolderType.RELATIONS)) + 1;
        getFolders().add(index, folder);
        
        return folder;
    }
    
    /**
     * <!-- begin-user-doc -->
     * This folder is optional so we add it as needed
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void removeDerivedRelationsFolder() {
        IFolder folder = getFolder(FolderType.DERIVED);
        if(folder != null) {
            getFolders().remove(folder);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IFolder getDefaultFolderForElement(EObject element) {
        addDefaultFolders(); // Check they haven't been deleted
        
        if(element instanceof IBusinessLayerElement) {
            return getFolder(FolderType.BUSINESS);
        }
        if(element instanceof IApplicationLayerElement) {
            return getFolder(FolderType.APPLICATION);
        }
        if(element instanceof ITechnologyLayerElement) {
            return getFolder(FolderType.TECHNOLOGY);
        }
        if(element instanceof IMotivationElement) {
            return getFolder(FolderType.MOTIVATION);
        }
        if(element instanceof IImplementationMigrationElement) {
            return getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        }
        if(element instanceof IJunctionElement) {
            return getFolder(FolderType.CONNECTORS);
        }
        if(element instanceof IRelationship) {
            return getFolder(FolderType.RELATIONS);
        }
        if(element instanceof IDiagramModel) {
            return getFolder(FolderType.DIAGRAMS);
        }
        
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IFolder getFolder(FolderType type) {
        for(IFolder folder : getFolders()) {
            if(folder.getType().equals(type)) {
                return folder;
            }
        }
        
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public Object getAdapter(Object adapter) {
        if(!fAdapterMap.containsKey(adapter) && eContainer() instanceof IAdapter) {
            return ((IAdapter)eContainer()).getAdapter(adapter);
        }
        
        return fAdapterMap.get(adapter);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void setAdapter(Object adapter, Object object) {
        fAdapterMap.put(adapter, object);
    }

    /**
     * <!-- begin-user-doc -->
     * Return the first diagram model or null
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IDiagramModel getDefaultDiagramModel() {
        EList<IDiagramModel> list = getDiagramModels();
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * <!-- begin-user-doc -->
     * Return the Diagram Models - could be empty list
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public EList<IDiagramModel> getDiagramModels() {
        EList<IDiagramModel> list = new BasicEList<IDiagramModel>();
        
        IFolder folder = getFolder(FolderType.DIAGRAMS);
        if(folder != null) {
            _getDiagramModels(folder, list);
        }
        
        return list;
    }
    
    private void _getDiagramModels(IFolder folder, EList<IDiagramModel> list) {
        for(EObject object : folder.getElements()) {
            if(object instanceof IDiagramModel) {
                list.add((IDiagramModel)object);
            }
        }
        for(IFolder f : folder.getFolders()) {
            _getDiagramModels(f, list);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.ARCHIMATE_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IArchimateModel getArchimateModel() {
        return this;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES);
        }
        return properties;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IMetadata getMetadata() {
        if(metadata == null) {
            metadata = IArchimateFactory.eINSTANCE.createMetadata();
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetadata(IMetadata newMetadata, NotificationChain msgs) {
        IMetadata oldMetadata = metadata;
        metadata = newMetadata;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__METADATA, oldMetadata, newMetadata);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMetadata(IMetadata newMetadata) {
        if (newMetadata != metadata) {
            NotificationChain msgs = null;
            if (metadata != null)
                msgs = ((InternalEObject)metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IArchimatePackage.ARCHIMATE_MODEL__METADATA, null, msgs);
            if (newMetadata != null)
                msgs = ((InternalEObject)newMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IArchimatePackage.ARCHIMATE_MODEL__METADATA, null, msgs);
            msgs = basicSetMetadata(newMetadata, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__METADATA, newMetadata, newMetadata));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPurpose(String newPurpose) {
        String oldPurpose = purpose;
        purpose = newPurpose;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__PURPOSE, oldPurpose, purpose));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IFolder> getFolders() {
        if (folders == null) {
            folders = new EObjectContainmentEList<IFolder>(IFolder.class, this, IArchimatePackage.ARCHIMATE_MODEL__FOLDERS);
        }
        return folders;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public File getFile() {
        return file;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFile(File newFile) {
        File oldFile = file;
        file = newFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__FILE, oldFile, file));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void setDefaults() {
        // Element has no ID so allocate one
        if(getId() == null) {
            setId(fIDAdapter.getNewID());
        }
        else {
            fIDAdapter.registerID(getId());
        }

        addDefaultFolders();
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_MODEL__FOLDERS:
                return ((InternalEList<?>)getFolders()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES:
                return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.ARCHIMATE_MODEL__METADATA:
                return basicSetMetadata(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_MODEL__FOLDERS:
                return getFolders();
            case IArchimatePackage.ARCHIMATE_MODEL__NAME:
                return getName();
            case IArchimatePackage.ARCHIMATE_MODEL__ID:
                return getId();
            case IArchimatePackage.ARCHIMATE_MODEL__ARCHIMATE_MODEL:
                return getArchimateModel();
            case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES:
                return getProperties();
            case IArchimatePackage.ARCHIMATE_MODEL__PURPOSE:
                return getPurpose();
            case IArchimatePackage.ARCHIMATE_MODEL__FILE:
                return getFile();
            case IArchimatePackage.ARCHIMATE_MODEL__VERSION:
                return getVersion();
            case IArchimatePackage.ARCHIMATE_MODEL__METADATA:
                return getMetadata();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_MODEL__FOLDERS:
                getFolders().clear();
                getFolders().addAll((Collection<? extends IFolder>)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__NAME:
                setName((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__ID:
                setId((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__PURPOSE:
                setPurpose((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__FILE:
                setFile((File)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__VERSION:
                setVersion((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__METADATA:
                setMetadata((IMetadata)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_MODEL__FOLDERS:
                getFolders().clear();
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__NAME:
                setName(NAME_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__ID:
                setId(ID_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES:
                getProperties().clear();
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__PURPOSE:
                setPurpose(PURPOSE_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__FILE:
                setFile(FILE_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_MODEL__METADATA:
                setMetadata((IMetadata)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_MODEL__FOLDERS:
                return folders != null && !folders.isEmpty();
            case IArchimatePackage.ARCHIMATE_MODEL__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case IArchimatePackage.ARCHIMATE_MODEL__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case IArchimatePackage.ARCHIMATE_MODEL__ARCHIMATE_MODEL:
                return getArchimateModel() != null;
            case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case IArchimatePackage.ARCHIMATE_MODEL__PURPOSE:
                return PURPOSE_EDEFAULT == null ? purpose != null : !PURPOSE_EDEFAULT.equals(purpose);
            case IArchimatePackage.ARCHIMATE_MODEL__FILE:
                return FILE_EDEFAULT == null ? file != null : !FILE_EDEFAULT.equals(file);
            case IArchimatePackage.ARCHIMATE_MODEL__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
            case IArchimatePackage.ARCHIMATE_MODEL__METADATA:
                return metadata != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == INameable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_MODEL__NAME: return IArchimatePackage.NAMEABLE__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_MODEL__ID: return IArchimatePackage.IDENTIFIER__ID;
                default: return -1;
            }
        }
        if (baseClass == IAdapter.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IArchimateModelElement.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_MODEL__ARCHIMATE_MODEL: return IArchimatePackage.ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == INameable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.NAMEABLE__NAME: return IArchimatePackage.ARCHIMATE_MODEL__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.IDENTIFIER__ID: return IArchimatePackage.ARCHIMATE_MODEL__ID;
                default: return -1;
            }
        }
        if (baseClass == IAdapter.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IArchimateModelElement.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL: return IArchimatePackage.ARCHIMATE_MODEL__ARCHIMATE_MODEL;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (name: "); //$NON-NLS-1$
        result.append(name);
        result.append(", id: "); //$NON-NLS-1$
        result.append(id);
        result.append(", purpose: "); //$NON-NLS-1$
        result.append(purpose);
        result.append(", file: "); //$NON-NLS-1$
        result.append(file);
        result.append(", version: "); //$NON-NLS-1$
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //ArchimateModel
