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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFeaturesEList;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.util.UUIDFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.Profile#getName <em>Name</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Profile#getId <em>Id</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Profile#getFeatures <em>Features</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Profile#getImagePath <em>Image Path</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Profile#isSpecialization <em>Specialization</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Profile#getConceptType <em>Concept Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class Profile extends EObjectImpl implements IProfile {
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
     * Adapter Map for arbitrary objects
     */
    private Map<Object, Object> fAdapterMap = new HashMap<Object, Object>();

    
    /**
     * The default value of the '{@link #getImagePath() <em>Image Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImagePath()
     * @generated
     * @ordered
     */
    protected static final String IMAGE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getImagePath() <em>Image Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImagePath()
     * @generated
     * @ordered
     */
    protected String imagePath = IMAGE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #isSpecialization() <em>Specialization</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSpecialization()
     * @generated
     * @ordered
     */
    protected static final boolean SPECIALIZATION_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isSpecialization() <em>Specialization</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSpecialization()
     * @generated
     * @ordered
     */
    protected boolean specialization = SPECIALIZATION_EDEFAULT;

    /**
     * The default value of the '{@link #getConceptType() <em>Concept Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConceptType()
     * @generated
     * @ordered
     */
    protected static final String CONCEPT_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getConceptType() <em>Concept Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConceptType()
     * @generated
     * @ordered
     */
    protected String conceptType = CONCEPT_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    protected Profile() {
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
        return IArchimatePackage.Literals.PROFILE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.PROFILE__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IFeaturesEList getFeatures() {
        if (features == null) {
            features = new FeaturesEList(IFeature.class, this, IArchimatePackage.PROFILE__FEATURES);
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.PROFILE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isSpecialization() {
        return specialization;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSpecialization(boolean newSpecialization) {
        boolean oldSpecialization = specialization;
        specialization = newSpecialization;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.PROFILE__SPECIALIZATION, oldSpecialization, specialization));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getConceptType() {
        return conceptType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setConceptType(String newConceptType) {
        String oldConceptType = conceptType;
        conceptType = newConceptType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.PROFILE__CONCEPT_TYPE, oldConceptType, conceptType));
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
            case IArchimatePackage.PROFILE__FEATURES:
                return ((InternalEList<?>)getFeatures()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getImagePath() {
        return imagePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setImagePath(String newImagePath) {
        String oldImagePath = imagePath;
        imagePath = newImagePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.PROFILE__IMAGE_PATH, oldImagePath, imagePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.PROFILE__NAME:
                return getName();
            case IArchimatePackage.PROFILE__ID:
                return getId();
            case IArchimatePackage.PROFILE__FEATURES:
                return getFeatures();
            case IArchimatePackage.PROFILE__IMAGE_PATH:
                return getImagePath();
            case IArchimatePackage.PROFILE__SPECIALIZATION:
                return isSpecialization();
            case IArchimatePackage.PROFILE__CONCEPT_TYPE:
                return getConceptType();
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
            case IArchimatePackage.PROFILE__NAME:
                setName((String)newValue);
                return;
            case IArchimatePackage.PROFILE__ID:
                setId((String)newValue);
                return;
            case IArchimatePackage.PROFILE__FEATURES:
                getFeatures().clear();
                getFeatures().addAll((Collection<? extends IFeature>)newValue);
                return;
            case IArchimatePackage.PROFILE__IMAGE_PATH:
                setImagePath((String)newValue);
                return;
            case IArchimatePackage.PROFILE__SPECIALIZATION:
                setSpecialization((Boolean)newValue);
                return;
            case IArchimatePackage.PROFILE__CONCEPT_TYPE:
                setConceptType((String)newValue);
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
            case IArchimatePackage.PROFILE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case IArchimatePackage.PROFILE__ID:
                setId(ID_EDEFAULT);
                return;
            case IArchimatePackage.PROFILE__FEATURES:
                getFeatures().clear();
                return;
            case IArchimatePackage.PROFILE__IMAGE_PATH:
                setImagePath(IMAGE_PATH_EDEFAULT);
                return;
            case IArchimatePackage.PROFILE__SPECIALIZATION:
                setSpecialization(SPECIALIZATION_EDEFAULT);
                return;
            case IArchimatePackage.PROFILE__CONCEPT_TYPE:
                setConceptType(CONCEPT_TYPE_EDEFAULT);
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
            case IArchimatePackage.PROFILE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case IArchimatePackage.PROFILE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case IArchimatePackage.PROFILE__FEATURES:
                return features != null && !features.isEmpty();
            case IArchimatePackage.PROFILE__IMAGE_PATH:
                return IMAGE_PATH_EDEFAULT == null ? imagePath != null : !IMAGE_PATH_EDEFAULT.equals(imagePath);
            case IArchimatePackage.PROFILE__SPECIALIZATION:
                return specialization != SPECIALIZATION_EDEFAULT;
            case IArchimatePackage.PROFILE__CONCEPT_TYPE:
                return CONCEPT_TYPE_EDEFAULT == null ? conceptType != null : !CONCEPT_TYPE_EDEFAULT.equals(conceptType);
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
                case IArchimatePackage.PROFILE__NAME: return IArchimatePackage.NAMEABLE__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.PROFILE__ID: return IArchimatePackage.IDENTIFIER__ID;
                default: return -1;
            }
        }
        if (baseClass == IFeatures.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.PROFILE__FEATURES: return IArchimatePackage.FEATURES__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.PROFILE__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH;
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
                case IArchimatePackage.NAMEABLE__NAME: return IArchimatePackage.PROFILE__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.IDENTIFIER__ID: return IArchimatePackage.PROFILE__ID;
                default: return -1;
            }
        }
        if (baseClass == IFeatures.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.FEATURES__FEATURES: return IArchimatePackage.PROFILE__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH: return IArchimatePackage.PROFILE__IMAGE_PATH;
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
        result.append(", imagePath: "); //$NON-NLS-1$
        result.append(imagePath);
        result.append(", specialization: "); //$NON-NLS-1$
        result.append(specialization);
        result.append(", conceptType: "); //$NON-NLS-1$
        result.append(conceptType);
        result.append(')');
        return result.toString();
    }

    
    /**
     * @return The EClass for this Profile's concept type
     */
    @Override
    public EClass getConceptClass() {
        return (EClass)IArchimatePackage.eINSTANCE.getEClassifier(getConceptType());
    }

} //Profile
