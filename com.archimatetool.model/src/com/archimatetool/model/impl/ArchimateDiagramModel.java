/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.ArchimateDiagramModel#getViewpoint <em>Viewpoint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArchimateDiagramModel extends DiagramModel implements IArchimateDiagramModel {
    /**
     * The default value of the '{@link #getViewpoint() <em>Viewpoint</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getViewpoint()
     * @generated
     * @ordered
     */
    protected static final String VIEWPOINT_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getViewpoint() <em>Viewpoint</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getViewpoint()
     * @generated
     * @ordered
     */
    protected String viewpoint = VIEWPOINT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArchimateDiagramModel() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getViewpoint() {
        return viewpoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setViewpoint(String newViewpoint) {
        String oldViewpoint = viewpoint;
        viewpoint = newViewpoint;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT, oldViewpoint, viewpoint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT:
                return getViewpoint();
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
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT:
                setViewpoint((String)newValue);
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
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT:
                setViewpoint(VIEWPOINT_EDEFAULT);
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
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT:
                return VIEWPOINT_EDEFAULT == null ? viewpoint != null : !VIEWPOINT_EDEFAULT.equals(viewpoint);
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
        result.append(" (viewpoint: "); //$NON-NLS-1$
        result.append(viewpoint);
        result.append(')');
        return result.toString();
    }

} //ArchimateDiagramModel
