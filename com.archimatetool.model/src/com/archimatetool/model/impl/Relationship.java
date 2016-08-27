/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import com.archimatetool.model.IArchimateComponent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IRelationship;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.Relationship#getSource <em>Source</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Relationship#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class Relationship extends ArchimateComponent implements IRelationship {
    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected IArchimateComponent source;

    /**
     * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTarget()
     * @generated
     * @ordered
     */
    protected IArchimateComponent target;

    /**
     * Stored references to Diagram Connections
     */
    private EList<IDiagramModelArchimateConnection> diagramConnections;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Relationship() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.RELATIONSHIP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IArchimateComponent getSource() {
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void setSource(IArchimateComponent newSource) {
        IArchimateComponent oldSource = source;
        source = newSource;
        
        if(oldSource != null) {
            oldSource.getSourceRelationships().remove(this);
        }
        if(newSource != null) {
            newSource.getSourceRelationships().add(this);
        }
        
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.RELATIONSHIP__SOURCE, oldSource, source));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IArchimateComponent getTarget() {
        return target;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void setTarget(IArchimateComponent newTarget) {
        IArchimateComponent oldTarget = target;
        target = newTarget;
        
        if(oldTarget != null) {
            oldTarget.getTargetRelationships().remove(this);
        }
        if(newTarget != null) {
            newTarget.getTargetRelationships().add(this);
        }
        
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.RELATIONSHIP__TARGET, oldTarget, target));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void connect(IArchimateComponent source, IArchimateComponent target) {
        setSource(source);
        setTarget(target);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void reconnect() {
        if(source != null) {
            source.getSourceRelationships().add(this);
        }
        if(target != null) {
            target.getTargetRelationships().add(this);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void disconnect() {
        if(source != null) {
            source.getSourceRelationships().remove(this);
        }
        if(target != null) {
            target.getTargetRelationships().remove(this);
        }
    }
    
    @Override
    public EObject getCopy() {
        IRelationship copy = (IRelationship)super.getCopy();
        
        // Clear source and target. This will also clear connected components' references to this
        copy.setSource(null);
        copy.setTarget(null);
        
        return copy;
    }

    /* (non-Javadoc)
     * @see com.archimatetool.model.IRelationship#getReferencingDiagramConnections()
     */
    @Override
    public EList<IDiagramModelArchimateConnection> getReferencingDiagramConnections() {
        if(diagramConnections == null) {
            diagramConnections = new UniqueEList<IDiagramModelArchimateConnection>();
        }
        return diagramConnections;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.RELATIONSHIP__SOURCE:
                return getSource();
            case IArchimatePackage.RELATIONSHIP__TARGET:
                return getTarget();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IArchimatePackage.RELATIONSHIP__SOURCE:
                setSource((IArchimateComponent)newValue);
                return;
            case IArchimatePackage.RELATIONSHIP__TARGET:
                setTarget((IArchimateComponent)newValue);
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
            case IArchimatePackage.RELATIONSHIP__SOURCE:
                setSource((IArchimateComponent)null);
                return;
            case IArchimatePackage.RELATIONSHIP__TARGET:
                setTarget((IArchimateComponent)null);
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
            case IArchimatePackage.RELATIONSHIP__SOURCE:
                return source != null;
            case IArchimatePackage.RELATIONSHIP__TARGET:
                return target != null;
        }
        return super.eIsSet(featureID);
    }

} //Relationship
