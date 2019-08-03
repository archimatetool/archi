/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInfluenceRelationship;
import org.eclipse.emf.common.notify.Notification;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Influence Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.InfluenceRelationship#getStrength <em>Strength</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InfluenceRelationship extends ArchimateRelationship implements IInfluenceRelationship {
    /**
     * The default value of the '{@link #getStrength() <em>Strength</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStrength()
     * @generated
     * @ordered
     */
    protected static final String STRENGTH_EDEFAULT = ""; //$NON-NLS-1$
    /**
     * The cached value of the '{@link #getStrength() <em>Strength</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStrength()
     * @generated
     * @ordered
     */
    protected String strength = STRENGTH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InfluenceRelationship() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getStrength() {
        return strength;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStrength(String newStrength) {
        String oldStrength = strength;
        strength = newStrength;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.INFLUENCE_RELATIONSHIP__STRENGTH, oldStrength, strength));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.INFLUENCE_RELATIONSHIP__STRENGTH:
                return getStrength();
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
            case IArchimatePackage.INFLUENCE_RELATIONSHIP__STRENGTH:
                setStrength((String)newValue);
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
            case IArchimatePackage.INFLUENCE_RELATIONSHIP__STRENGTH:
                setStrength(STRENGTH_EDEFAULT);
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
            case IArchimatePackage.INFLUENCE_RELATIONSHIP__STRENGTH:
                return STRENGTH_EDEFAULT == null ? strength != null : !STRENGTH_EDEFAULT.equals(strength);
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
        result.append(" (strength: "); //$NON-NLS-1$
        result.append(strength);
        result.append(')');
        return result.toString();
    }

} //InfluenceRelationship
