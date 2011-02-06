/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.ISketchModelActor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sketch Model Actor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.SketchModelActor#getDocumentation <em>Documentation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SketchModelActor extends DiagramModelObject implements ISketchModelActor {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SketchModelActor() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.SKETCH_MODEL_ACTOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDocumentation(String newDocumentation) {
        String oldDocumentation = documentation;
        documentation = newDocumentation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION, oldDocumentation, documentation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION:
                return getDocumentation();
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
            case IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION:
                setDocumentation((String)newValue);
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
            case IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION:
                setDocumentation(DOCUMENTATION_EDEFAULT);
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
            case IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION:
                return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
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
        if (baseClass == IDocumentable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
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
        if (baseClass == IDocumentable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.SKETCH_MODEL_ACTOR__DOCUMENTATION;
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

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (documentation: "); //$NON-NLS-1$
        result.append(documentation);
        result.append(')');
        return result.toString();
    }

} //SketchModelActor
