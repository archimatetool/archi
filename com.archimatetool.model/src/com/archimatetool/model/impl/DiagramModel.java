/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *
 */
package com.archimatetool.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IFeaturesEList;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.UUIDFactory;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getName <em>Name</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getId <em>Id</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getFeatures <em>Features</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getChildren <em>Children</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModel#getConnectionRouterType <em>Connection Router Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DiagramModel extends EObjectImpl implements IDiagramModel {
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
     * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<IDiagramModelObject> children;

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
     * The default value of the '{@link #getConnectionRouterType() <em>Connection Router Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConnectionRouterType()
     * @generated
     * @ordered
     */
    protected static final int CONNECTION_ROUTER_TYPE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getConnectionRouterType() <em>Connection Router Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConnectionRouterType()
     * @generated
     * @ordered
     */
    protected int connectionRouterType = CONNECTION_ROUTER_TYPE_EDEFAULT;

    /**
     * Adapter Map for arbitrary objects
     */
    private Map<Object, Object> fAdapterMap = new HashMap<Object, Object>();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    protected DiagramModel() {
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
        return IArchimatePackage.Literals.DIAGRAM_MODEL;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IDiagramModel getDiagramModel() {
        return this;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IFeaturesEList getFeatures() {
        if (features == null) {
            features = new FeaturesEList(IFeature.class, this, IArchimatePackage.DIAGRAM_MODEL__FEATURES);
        }
        return (IFeaturesEList)features;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelObject> getChildren() {
        if (children == null) {
            children = new EObjectContainmentEList<IDiagramModelObject>(IDiagramModelObject.class, this, IArchimatePackage.DIAGRAM_MODEL__CHILDREN);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getConnectionRouterType() {
        return connectionRouterType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setConnectionRouterType(int newConnectionRouterType) {
        int oldConnectionRouterType = connectionRouterType;
        connectionRouterType = newConnectionRouterType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE, oldConnectionRouterType, connectionRouterType));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION, oldDocumentation, documentation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.DIAGRAM_MODEL__PROPERTIES);
        }
        return properties;
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
     * @generated NOT
     */
    @Override
    public EObject getCopy() {
        IDiagramModel newDiagramModel = EcoreUtil.copy(this);
        newDiagramModel.setId(UUIDFactory.createID(newDiagramModel)); // need a new ID
        newDiagramModel.getChildren().clear(); // need to do this!
        return newDiagramModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL__FEATURES:
                return ((InternalEList<?>)getFeatures()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.DIAGRAM_MODEL__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES:
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
            case IArchimatePackage.DIAGRAM_MODEL__NAME:
                return getName();
            case IArchimatePackage.DIAGRAM_MODEL__ID:
                return getId();
            case IArchimatePackage.DIAGRAM_MODEL__FEATURES:
                return getFeatures();
            case IArchimatePackage.DIAGRAM_MODEL__CHILDREN:
                return getChildren();
            case IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION:
                return getDocumentation();
            case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES:
                return getProperties();
            case IArchimatePackage.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE:
                return getConnectionRouterType();
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
            case IArchimatePackage.DIAGRAM_MODEL__NAME:
                setName((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__ID:
                setId((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__FEATURES:
                getFeatures().clear();
                getFeatures().addAll((Collection<? extends IFeature>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends IDiagramModelObject>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION:
                setDocumentation((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE:
                setConnectionRouterType((Integer)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL__NAME:
                setName(NAME_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__ID:
                setId(ID_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__FEATURES:
                getFeatures().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL__CHILDREN:
                getChildren().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION:
                setDocumentation(DOCUMENTATION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES:
                getProperties().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE:
                setConnectionRouterType(CONNECTION_ROUTER_TYPE_EDEFAULT);
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
            case IArchimatePackage.DIAGRAM_MODEL__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case IArchimatePackage.DIAGRAM_MODEL__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case IArchimatePackage.DIAGRAM_MODEL__FEATURES:
                return features != null && !features.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL__CHILDREN:
                return children != null && !children.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION:
                return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
            case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE:
                return connectionRouterType != CONNECTION_ROUTER_TYPE_EDEFAULT;
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
                case IArchimatePackage.DIAGRAM_MODEL__NAME: return IArchimatePackage.NAMEABLE__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL__ID: return IArchimatePackage.IDENTIFIER__ID;
                default: return -1;
            }
        }
        if (baseClass == IFeatures.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL__FEATURES: return IArchimatePackage.FEATURES__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == ICloneable.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelComponent.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelContainer.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
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
                case IArchimatePackage.NAMEABLE__NAME: return IArchimatePackage.DIAGRAM_MODEL__NAME;
                default: return -1;
            }
        }
        if (baseClass == IIdentifier.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.IDENTIFIER__ID: return IArchimatePackage.DIAGRAM_MODEL__ID;
                default: return -1;
            }
        }
        if (baseClass == IFeatures.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.FEATURES__FEATURES: return IArchimatePackage.DIAGRAM_MODEL__FEATURES;
                default: return -1;
            }
        }
        if (baseClass == ICloneable.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelComponent.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelContainer.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.DIAGRAM_MODEL__PROPERTIES;
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
        result.append(", connectionRouterType: "); //$NON-NLS-1$
        result.append(connectionRouterType);
        result.append(')');
        return result.toString();
    }

} //DiagramModel
