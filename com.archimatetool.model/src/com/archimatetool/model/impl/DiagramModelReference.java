/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.ITextPosition;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelReference#getTextPosition <em>Text Position</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelReference#getReferencedModel <em>Referenced Model</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramModelReference extends DiagramModelObject implements IDiagramModelReference {
    /**
     * The default value of the '{@link #getTextPosition() <em>Text Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPosition()
     * @generated
     * @ordered
     */
    protected static final int TEXT_POSITION_EDEFAULT = 0;
    /**
     * The cached value of the '{@link #getTextPosition() <em>Text Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPosition()
     * @generated
     * @ordered
     */
    protected int textPosition = TEXT_POSITION_EDEFAULT;
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
    @Override
    public int getTextPosition() {
        return textPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTextPosition(int newTextPosition) {
        int oldTextPosition = textPosition;
        textPosition = newTextPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION, oldTextPosition, textPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModel getReferencedModel() {
        return referencedModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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
            return ""; //$NON-NLS-1$
        }
    }
    
    @Override
    public void setName(String newName) {
        if(getReferencedModel() != null) {
            getReferencedModel().setName(newName);
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION:
                return getTextPosition();
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION:
                setTextPosition((Integer)newValue);
                return;
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION:
                setTextPosition(TEXT_POSITION_EDEFAULT);
                return;
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
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION:
                return textPosition != TEXT_POSITION_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL:
                return referencedModel != null;
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
        if (baseClass == ITextPosition.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION: return IArchimatePackage.TEXT_POSITION__TEXT_POSITION;
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
        if (baseClass == ITextPosition.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_POSITION__TEXT_POSITION: return IArchimatePackage.DIAGRAM_MODEL_REFERENCE__TEXT_POSITION;
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
        result.append(" (textPosition: "); //$NON-NLS-1$
        result.append(textPosition);
        result.append(')');
        return result.toString();
    }

} //DiagramModelReference
