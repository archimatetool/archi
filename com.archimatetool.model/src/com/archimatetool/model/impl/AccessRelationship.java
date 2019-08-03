/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimatePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Access Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.AccessRelationship#getAccessType <em>Access Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AccessRelationship extends ArchimateRelationship implements IAccessRelationship {
    /**
     * The default value of the '{@link #getAccessType() <em>Access Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessType()
     * @generated
     * @ordered
     */
    protected static final int ACCESS_TYPE_EDEFAULT = 0;
    /**
     * The cached value of the '{@link #getAccessType() <em>Access Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessType()
     * @generated
     * @ordered
     */
    protected int accessType = ACCESS_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AccessRelationship() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.ACCESS_RELATIONSHIP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getAccessType() {
        return accessType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAccessType(int newAccessType) {
        int oldAccessType = accessType;
        accessType = newAccessType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ACCESS_RELATIONSHIP__ACCESS_TYPE, oldAccessType, accessType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.ACCESS_RELATIONSHIP__ACCESS_TYPE:
                return getAccessType();
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
            case IArchimatePackage.ACCESS_RELATIONSHIP__ACCESS_TYPE:
                setAccessType((Integer)newValue);
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
            case IArchimatePackage.ACCESS_RELATIONSHIP__ACCESS_TYPE:
                setAccessType(ACCESS_TYPE_EDEFAULT);
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
            case IArchimatePackage.ACCESS_RELATIONSHIP__ACCESS_TYPE:
                return accessType != ACCESS_TYPE_EDEFAULT;
        }
        return super.eIsSet(featureID);
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
        result.append(" (accessType: "); //$NON-NLS-1$
        result.append(accessType);
        result.append(')');
        return result.toString();
    }

} //AccessRelationship
