/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelConnection;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connectable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.Connectable#getSourceConnections <em>Source Connections</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.Connectable#getTargetConnections <em>Target Connections</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class Connectable extends DiagramModelComponent implements IConnectable {
    /**
     * The cached value of the '{@link #getSourceConnections() <em>Source Connections</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceConnections()
     * @generated
     * @ordered
     */
    protected EList<IDiagramModelConnection> sourceConnections;

    /**
     * The cached value of the '{@link #getTargetConnections() <em>Target Connections</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetConnections()
     * @generated
     * @ordered
     */
    protected EList<IDiagramModelConnection> targetConnections;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Connectable() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.CONNECTABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelConnection> getSourceConnections() {
        if (sourceConnections == null) {
            sourceConnections = new EObjectContainmentEList<IDiagramModelConnection>(IDiagramModelConnection.class, this, IArchimatePackage.CONNECTABLE__SOURCE_CONNECTIONS);
        }
        return sourceConnections;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelConnection> getTargetConnections() {
        if (targetConnections == null) {
            targetConnections = new EObjectEList<IDiagramModelConnection>(IDiagramModelConnection.class, this, IArchimatePackage.CONNECTABLE__TARGET_CONNECTIONS);
        }
        return targetConnections;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void addConnection(IDiagramModelConnection connection) {
        if(connection == null) {
            throw new IllegalArgumentException("Connection was null"); //$NON-NLS-1$
        }
        
        // This used to be "if/else if". This way it is possible for source == target (recursive)
        if(connection.getSource() == this) {
            getSourceConnections().add(connection);
        }
        
        if(connection.getTarget() == this) {
            getTargetConnections().add(connection);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void removeConnection(IDiagramModelConnection connection) {
        if(connection == null) {
            throw new IllegalArgumentException();
        }
        
        if(connection.getSource() == this) {
            getSourceConnections().remove(connection);
        } 
        
        if(connection.getTarget() == this) {
            getTargetConnections().remove(connection);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.CONNECTABLE__SOURCE_CONNECTIONS:
                return ((InternalEList<?>)getSourceConnections()).basicRemove(otherEnd, msgs);
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
            case IArchimatePackage.CONNECTABLE__SOURCE_CONNECTIONS:
                return getSourceConnections();
            case IArchimatePackage.CONNECTABLE__TARGET_CONNECTIONS:
                return getTargetConnections();
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
            case IArchimatePackage.CONNECTABLE__SOURCE_CONNECTIONS:
                getSourceConnections().clear();
                getSourceConnections().addAll((Collection<? extends IDiagramModelConnection>)newValue);
                return;
            case IArchimatePackage.CONNECTABLE__TARGET_CONNECTIONS:
                getTargetConnections().clear();
                getTargetConnections().addAll((Collection<? extends IDiagramModelConnection>)newValue);
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
            case IArchimatePackage.CONNECTABLE__SOURCE_CONNECTIONS:
                getSourceConnections().clear();
                return;
            case IArchimatePackage.CONNECTABLE__TARGET_CONNECTIONS:
                getTargetConnections().clear();
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
            case IArchimatePackage.CONNECTABLE__SOURCE_CONNECTIONS:
                return sourceConnections != null && !sourceConnections.isEmpty();
            case IArchimatePackage.CONNECTABLE__TARGET_CONNECTIONS:
                return targetConnections != null && !targetConnections.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //Connectable
