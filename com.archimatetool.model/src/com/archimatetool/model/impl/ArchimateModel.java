/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IApplicationElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IBusinessElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFeaturesEList;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IGrouping;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IImplementationMigrationElement;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.ILocation;
import com.archimatetool.model.IMetadata;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IPhysicalElement;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IStrategyElement;
import com.archimatetool.model.ITechnologyElement;
import com.archimatetool.model.util.IModelContentListener;
import com.archimatetool.model.util.UUIDFactory;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getFolders <em>Folders</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getName <em>Name</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getId <em>Id</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getFeatures <em>Features</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getPurpose <em>Purpose</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getFile <em>File</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getVersion <em>Version</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateModel#getProfiles <em>Profiles</em>}</li>
 * </ul>
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
     * The cached value of the '{@link #getFeatures() <em>Features</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatures()
     * @generated
     * @ordered
     */
    protected EList<IFeature> features;
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
    protected static final String PURPOSE_EDEFAULT = ""; //$NON-NLS-1$
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
     * The cached value of the '{@link #getProfiles() <em>Profiles</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProfiles()
     * @generated
     * @ordered
     */
    protected EList<IProfile> profiles;
    
    /**
     * Adapter Map for arbitrary objects
     */
    private Map<Object, Object> fAdapterMap = new HashMap<Object, Object>();
    
    /**
     * Ecore listeners
     */
    private List<IModelContentListener> fContentListeners = new ArrayList<IModelContentListener>();
    
    /**
     * One central EContentAdapter to listen to all model changes and forward on to listeners
     */
    private EContentAdapter eContentAdapter = new EContentAdapter() {
        @Override
        public void notifyChanged(Notification notification) {
            super.notifyChanged(notification);
            
            // Not interested in this type
            if(notification.getEventType() == Notification.REMOVING_ADAPTER) {
                return;
            }
            
            // Notify model listeners
            if(fContentListeners != null) {
                for(IModelContentListener listener : new ArrayList<>(fContentListeners)) {
                    listener.notifyChanged(notification);
                }
            }
        }
    };

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    protected ArchimateModel() {
        super();
        id = UUIDFactory.createID(this);
    }
    
    
    @Override
    public boolean addModelContentListener(IModelContentListener listener) {
        if(listener == null || fContentListeners == null) { // we might be disposed
            return false;
        }
        
        if(!eAdapters().contains(eContentAdapter)) {
            eAdapters().add(eContentAdapter);
        }
        
        return fContentListeners.contains(listener) ? false: fContentListeners.add(listener);
    }
    
    @Override
    public boolean removeModelContentListener(IModelContentListener listener) {
        if(listener == null || fContentListeners == null) { // we might be disposed
            return false;
        }
        
        return fContentListeners.remove(listener);
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void setDefaults() {
        addDefaultFolders();
    }
    
    /**
     * Add any default folders
     */
    protected void addDefaultFolders() {
        int index = 0;
        
        createDefaultFolder(FolderType.STRATEGY, index++);
        createDefaultFolder(FolderType.BUSINESS, index++);
        createDefaultFolder(FolderType.APPLICATION, index++);
        createDefaultFolder(FolderType.TECHNOLOGY, index++);
        createDefaultFolder(FolderType.MOTIVATION, index++);
        createDefaultFolder(FolderType.IMPLEMENTATION_MIGRATION, index++);
        createDefaultFolder(FolderType.OTHER, index++);
        createDefaultFolder(FolderType.RELATIONS, index++);
        createDefaultFolder(FolderType.DIAGRAMS, index++);
    }
    
    private void createDefaultFolder(FolderType folderType, int index) {
        if(getFolder(folderType) == null) {
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setType(folderType);
            folder.setName(folderType.getLabel());
            getFolders().add(index, folder);
        }
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IFolder getDefaultFolderForObject(EObject object) {
        addDefaultFolders(); // Check they haven't been deleted
        
        if(object instanceof IStrategyElement) {
            return getFolder(FolderType.STRATEGY);
        }
        if(object instanceof IBusinessElement) {
            return getFolder(FolderType.BUSINESS);
        }
        if(object instanceof IApplicationElement) {
            return getFolder(FolderType.APPLICATION);
        }
        if(object instanceof ITechnologyElement || object instanceof IPhysicalElement) {
            return getFolder(FolderType.TECHNOLOGY);
        }
        if(object instanceof IMotivationElement) {
            return getFolder(FolderType.MOTIVATION);
        }
        if(object instanceof IImplementationMigrationElement) {
            return getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        }
        if(object instanceof IJunction || object instanceof ILocation || object instanceof IGrouping) {
            return getFolder(FolderType.OTHER);
        }
        if(object instanceof IArchimateRelationship) {
            return getFolder(FolderType.RELATIONS);
        }
        if(object instanceof IDiagramModel) {
            return getFolder(FolderType.DIAGRAMS);
        }
        
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
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
    @Override
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
    @Override
    public void setAdapter(Object adapter, Object object) {
        fAdapterMap.put(adapter, object);
    }

    /**
     * <!-- begin-user-doc -->
     * Return the first diagram model or null
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
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
    @Override
    public EList<IDiagramModel> getDiagramModels() {
        EList<IDiagramModel> list = new BasicEList<IDiagramModel>();
        
        IFolder folder = getFolder(FolderType.DIAGRAMS);
        if(folder != null) {
            for(Iterator<EObject> iter = folder.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                if(eObject instanceof IDiagramModel) {
                    list.add((IDiagramModel)eObject);
                }
            }

        }
        
        return list;
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
    @Override
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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
    @Override
    public String getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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
    @Override
    public IFeaturesEList getFeatures() {
        if (features == null) {
            features = new FeaturesEList(IFeature.class, this, IArchimatePackage.ARCHIMATE_MODEL__FEATURES);
        }
        return (IFeaturesEList)features;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IArchimateModel getArchimateModel() {
        return this;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES);
        }
        return properties;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IMetadata getMetadata() {
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
    @Override
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
    @Override
    public EList<IProfile> getProfiles() {
        if (profiles == null) {
            profiles = new EObjectContainmentEList<IProfile>(IProfile.class, this, IArchimatePackage.ARCHIMATE_MODEL__PROFILES);
        }
        return profiles;
    }


    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getPurpose() {
        return purpose;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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
    @Override
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
    @Override
    public File getFile() {
        return file;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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
    @Override
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_MODEL__VERSION, oldVersion, version));
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
            case IArchimatePackage.ARCHIMATE_MODEL__FEATURES:
                return ((InternalEList<?>)getFeatures()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES:
                return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.ARCHIMATE_MODEL__METADATA:
                return basicSetMetadata(null, msgs);
            case IArchimatePackage.ARCHIMATE_MODEL__PROFILES:
                return ((InternalEList<?>)getProfiles()).basicRemove(otherEnd, msgs);
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
            case IArchimatePackage.ARCHIMATE_MODEL__FEATURES:
                return getFeatures();
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
            case IArchimatePackage.ARCHIMATE_MODEL__PROFILES:
                return getProfiles();
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
            case IArchimatePackage.ARCHIMATE_MODEL__FEATURES:
                getFeatures().clear();
                getFeatures().addAll((Collection<? extends IFeature>)newValue);
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
            case IArchimatePackage.ARCHIMATE_MODEL__PROFILES:
                getProfiles().clear();
                getProfiles().addAll((Collection<? extends IProfile>)newValue);
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
            case IArchimatePackage.ARCHIMATE_MODEL__FEATURES:
                getFeatures().clear();
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
            case IArchimatePackage.ARCHIMATE_MODEL__PROFILES:
                getProfiles().clear();
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
            case IArchimatePackage.ARCHIMATE_MODEL__FEATURES:
                return features != null && !features.isEmpty();
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
            case IArchimatePackage.ARCHIMATE_MODEL__PROFILES:
                return profiles != null && !profiles.isEmpty();
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
        if (baseClass == IAdapter.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
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
        if (baseClass == IFeatures.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_MODEL__FEATURES: return IArchimatePackage.FEATURES__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == IArchimateModelObject.class) {
            switch (derivedFeatureID) {
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
        if (baseClass == IAdapter.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
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
        if (baseClass == IFeatures.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.FEATURES__FEATURES: return IArchimatePackage.ARCHIMATE_MODEL__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == IArchimateModelObject.class) {
            switch (baseFeatureID) {
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

        StringBuilder result = new StringBuilder(super.toString());
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

    @Override
    public void dispose() {
        id = null;
        name = null;
        purpose = null;
        file = null;
        
        if(eAdapters != null) {
            //eAdapters.clear(); // This can be slow
            eAdapters = null;
        }
        
        fAdapterMap.clear();
        fAdapterMap = null;
        
        fContentListeners.clear();
        fContentListeners = null;
        
        // Dispose of these in case they are referenced in an editor or similar
        for(IDiagramModel dm : getDiagramModels()) {
            dm.getProperties().clear();
            dm.getChildren().clear();
        }
        
        if(folders != null) {
            folders.clear();
            folders = null;
        }
        
        if(properties != null) {
            properties.clear();
            properties = null;
        }
        
        if(metadata != null) {
            metadata.getEntries().clear();
            metadata = null;
        }
        
        // Could be storing images
        if(features != null) {
            features.clear();
            features = null;
        }
    }

} //ArchimateModel
