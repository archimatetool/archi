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
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModelReference#getReferencedModel <em>Referenced Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramModelReference extends DiagramModelObject implements IDiagramModelReference {
    /**
     * The cached value of the '{@link #getReferencedModel() <em>Referenced Model</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferencedModel()
     * @generated
     * @ordered
     */
    protected IDiagramModel referencedModel;
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelReference() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_REFERENCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IDiagramModel getReferencedModel() {
        return referencedModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferencedModel(IDiagramModel newReferencedModel) {
        IDiagramModel oldReferencedModel = referencedModel;
        referencedModel = newReferencedModel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL, oldReferencedModel, referencedModel));
    }

    @Override
    public String getName() {
        if(getReferencedModel() != null) {
            return getReferencedModel().getName();
        }
        else {
            return "";
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL:
                return getReferencedModel();
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL:
                setReferencedModel((IDiagramModel)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL:
                setReferencedModel((IDiagramModel)null);
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL:
                return referencedModel != null;
        }
        return super.eIsSet(featureID);
    }

} //DiagramModelReference
