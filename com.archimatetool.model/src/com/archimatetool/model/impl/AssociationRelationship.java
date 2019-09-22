/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IAssociationRelationship;
import org.eclipse.emf.common.notify.Notification;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Association Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.AssociationRelationship#isDirected <em>Directed</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssociationRelationship extends ArchimateRelationship implements IAssociationRelationship {
    /**
     * The default value of the '{@link #isDirected() <em>Directed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDirected()
     * @generated
     * @ordered
     */
    protected static final boolean DIRECTED_EDEFAULT = false;
    /**
     * The cached value of the '{@link #isDirected() <em>Directed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDirected()
     * @generated
     * @ordered
     */
    protected boolean directed = DIRECTED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssociationRelationship() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.ASSOCIATION_RELATIONSHIP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isDirected() {
        return directed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDirected(boolean newDirected) {
        boolean oldDirected = directed;
        directed = newDirected;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ASSOCIATION_RELATIONSHIP__DIRECTED, oldDirected, directed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP__DIRECTED:
                return isDirected();
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
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP__DIRECTED:
                setDirected((Boolean)newValue);
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
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP__DIRECTED:
                setDirected(DIRECTED_EDEFAULT);
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
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP__DIRECTED:
                return directed != DIRECTED_EDEFAULT;
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
        result.append(" (directed: "); //$NON-NLS-1$
        result.append(directed);
        result.append(')');
        return result.toString();
    }

} //AssociationRelationship
