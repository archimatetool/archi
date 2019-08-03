/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.canvas.model.ICanvasModelConnection;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.impl.DiagramModelConnection;




/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.canvas.model.impl.CanvasModelConnection#isLocked <em>Locked</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanvasModelConnection extends DiagramModelConnection implements ICanvasModelConnection {
    /**
     * The default value of the '{@link #isLocked() <em>Locked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLocked()
     * @generated
     * @ordered
     */
    protected static final boolean LOCKED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isLocked() <em>Locked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLocked()
     * @generated
     * @ordered
     */
    protected boolean locked = LOCKED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CanvasModelConnection() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ICanvasPackage.Literals.CANVAS_MODEL_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isLocked() {
        return locked;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLocked(boolean newLocked) {
        boolean oldLocked = locked;
        locked = newLocked;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED, oldLocked, locked));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED:
                return isLocked();
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
            case ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED:
                setLocked((Boolean)newValue);
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
            case ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED:
                setLocked(LOCKED_EDEFAULT);
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
            case ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED:
                return locked != LOCKED_EDEFAULT;
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
        if (baseClass == ILockable.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED: return IArchimatePackage.LOCKABLE__LOCKED;
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
        if (baseClass == ILockable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.LOCKABLE__LOCKED: return ICanvasPackage.CANVAS_MODEL_CONNECTION__LOCKED;
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
        result.append(" (locked: "); //$NON-NLS-1$
        result.append(locked);
        result.append(')');
        return result.toString();
    }

} //CanvasModelConnection
