/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IFeaturesEList;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.UUIDFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Concept</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.ArchimateConcept#getName <em>Name</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateConcept#getId <em>Id</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateConcept#getFeatures <em>Features</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateConcept#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateConcept#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.ArchimateConcept#getProfiles <em>Profiles</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ArchimateConcept extends EObjectImpl implements IArchimateConcept {
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
     * The default value of the '{@link #getDocumentation() <em>Documentation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDocumentation()
     * @generated
     * @ordered
     */
    protected static final String DOCUMENTATION_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getDocumentation() <em>Documentation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDocumentation()
     * @generated
     * @ordered
     */
    protected String documentation = DOCUMENTATION_EDEFAULT;

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
     * The cached value of the '{@link #getProfiles() <em>Profiles</em>}' reference list.
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
     * Stored references to connected relationships
     */
    protected EList<IArchimateRelationship> sourceRelationships, targetRelationships;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    protected ArchimateConcept() {
        super();
        id = UUIDFactory.createID(this);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.ARCHIMATE_CONCEPT;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_CONCEPT__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IFeaturesEList getFeatures() {
        if (features == null) {
            features = new FeaturesEList(IFeature.class, this, IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES);
        }
        return (IFeaturesEList)features;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_CONCEPT__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getDocumentation() {
        return documentation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDocumentation(String newDocumentation) {
        String oldDocumentation = documentation;
        documentation = newDocumentation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION, oldDocumentation, documentation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES);
        }
        return properties;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProfile> getProfiles() {
        if (profiles == null) {
            profiles = new EObjectEList<IProfile>(IProfile.class, this, IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES);
        }
        return profiles;
    }
    
    @Override
    public IProfile getPrimaryProfile() {
        // Return the first one
        return profiles == null || getProfiles().isEmpty() ? null : getProfiles().get(0);
    }

    @Override
    public EList<IArchimateRelationship> getSourceRelationships() {
        if(sourceRelationships == null) {
            sourceRelationships = new UniqueEList<IArchimateRelationship>();
        }
        return sourceRelationships;
    }
    
    @Override
    public EList<IArchimateRelationship> getTargetRelationships() {
        if(targetRelationships == null) {
            targetRelationships = new UniqueEList<IArchimateRelationship>();
        }
        return targetRelationships;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EObject getCopy() {
        IArchimateConcept newObject = EcoreUtil.copy(this);
        newObject.setId(UUIDFactory.createID(newObject)); // need a new ID
        return newObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IArchimateModel getArchimateModel() {
        if(eContainer() == null) {
            return null;
        }
        return ((IArchimateModelObject)eContainer()).getArchimateModel();
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
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES:
                return ((InternalEList<?>)getFeatures()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES:
                return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
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
            case IArchimatePackage.ARCHIMATE_CONCEPT__NAME:
                return getName();
            case IArchimatePackage.ARCHIMATE_CONCEPT__ID:
                return getId();
            case IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES:
                return getFeatures();
            case IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION:
                return getDocumentation();
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES:
                return getProperties();
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES:
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
            case IArchimatePackage.ARCHIMATE_CONCEPT__NAME:
                setName((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__ID:
                setId((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES:
                getFeatures().clear();
                getFeatures().addAll((Collection<? extends IFeature>)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION:
                setDocumentation((String)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES:
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
            case IArchimatePackage.ARCHIMATE_CONCEPT__NAME:
                setName(NAME_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__ID:
                setId(ID_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES:
                getFeatures().clear();
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION:
                setDocumentation(DOCUMENTATION_EDEFAULT);
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES:
                getProperties().clear();
                return;
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES:
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
            case IArchimatePackage.ARCHIMATE_CONCEPT__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case IArchimatePackage.ARCHIMATE_CONCEPT__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES:
                return features != null && !features.isEmpty();
            case IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION:
                return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES:
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
        if (baseClass == INameable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_CONCEPT__NAME: return IArchimatePackage.NAMEABLE__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_CONCEPT__ID: return IArchimatePackage.IDENTIFIER__ID;
                default: return -1;
            }
        }
        if (baseClass == IFeatures.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES: return IArchimatePackage.FEATURES__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == ICloneable.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == IProfiles.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES: return IArchimatePackage.PROFILES__PROFILES;
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
                case IArchimatePackage.NAMEABLE__NAME: return IArchimatePackage.ARCHIMATE_CONCEPT__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.IDENTIFIER__ID: return IArchimatePackage.ARCHIMATE_CONCEPT__ID;
                default: return -1;
            }
        }
        if (baseClass == IFeatures.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.FEATURES__FEATURES: return IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == ICloneable.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.ARCHIMATE_CONCEPT__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.ARCHIMATE_CONCEPT__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == IProfiles.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROFILES__PROFILES: return IArchimatePackage.ARCHIMATE_CONCEPT__PROFILES;
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
        result.append(", documentation: "); //$NON-NLS-1$
        result.append(documentation);
        result.append(')');
        return result.toString();
    }

} //ArchimateConcept
