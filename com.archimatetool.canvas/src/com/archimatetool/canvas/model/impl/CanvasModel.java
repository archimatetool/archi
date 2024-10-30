/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IHintProvider;
import com.archimatetool.model.impl.DiagramModel;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.canvas.model.impl.CanvasModel#getHintTitle <em>Hint Title</em>}</li>
 *   <li>{@link com.archimatetool.canvas.model.impl.CanvasModel#getHintContent <em>Hint Content</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanvasModel extends DiagramModel implements ICanvasModel {
    /**
     * The default value of the '{@link #getHintTitle() <em>Hint Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHintTitle()
     * @generated
     * @ordered
     */
    protected static final String HINT_TITLE_EDEFAULT = ""; //$NON-NLS-1$
    /**
     * The cached value of the '{@link #getHintTitle() <em>Hint Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHintTitle()
     * @generated
     * @ordered
     */
    protected String hintTitle = HINT_TITLE_EDEFAULT;
    /**
     * The default value of the '{@link #getHintContent() <em>Hint Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHintContent()
     * @generated
     * @ordered
     */
    protected static final String HINT_CONTENT_EDEFAULT = ""; //$NON-NLS-1$
    /**
     * The cached value of the '{@link #getHintContent() <em>Hint Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHintContent()
     * @generated
     * @ordered
     */
    protected String hintContent = HINT_CONTENT_EDEFAULT;
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CanvasModel() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ICanvasPackage.Literals.CANVAS_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getHintTitle() {
        return hintTitle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setHintTitle(String newHintTitle) {
        String oldHintTitle = hintTitle;
        hintTitle = newHintTitle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL__HINT_TITLE, oldHintTitle, hintTitle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getHintContent() {
        return hintContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setHintContent(String newHintContent) {
        String oldHintContent = hintContent;
        hintContent = newHintContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL__HINT_CONTENT, oldHintContent, hintContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ICanvasPackage.CANVAS_MODEL__HINT_TITLE:
                return getHintTitle();
            case ICanvasPackage.CANVAS_MODEL__HINT_CONTENT:
                return getHintContent();
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
            case ICanvasPackage.CANVAS_MODEL__HINT_TITLE:
                setHintTitle((String)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL__HINT_CONTENT:
                setHintContent((String)newValue);
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
            case ICanvasPackage.CANVAS_MODEL__HINT_TITLE:
                setHintTitle(HINT_TITLE_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL__HINT_CONTENT:
                setHintContent(HINT_CONTENT_EDEFAULT);
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
            case ICanvasPackage.CANVAS_MODEL__HINT_TITLE:
                return HINT_TITLE_EDEFAULT == null ? hintTitle != null : !HINT_TITLE_EDEFAULT.equals(hintTitle);
            case ICanvasPackage.CANVAS_MODEL__HINT_CONTENT:
                return HINT_CONTENT_EDEFAULT == null ? hintContent != null : !HINT_CONTENT_EDEFAULT.equals(hintContent);
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
        if (baseClass == IHintProvider.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL__HINT_TITLE: return IArchimatePackage.HINT_PROVIDER__HINT_TITLE;
                case ICanvasPackage.CANVAS_MODEL__HINT_CONTENT: return IArchimatePackage.HINT_PROVIDER__HINT_CONTENT;
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
        if (baseClass == IHintProvider.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.HINT_PROVIDER__HINT_TITLE: return ICanvasPackage.CANVAS_MODEL__HINT_TITLE;
                case IArchimatePackage.HINT_PROVIDER__HINT_CONTENT: return ICanvasPackage.CANVAS_MODEL__HINT_CONTENT;
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
        result.append(" (hintTitle: "); //$NON-NLS-1$
        result.append(hintTitle);
        result.append(", hintContent: "); //$NON-NLS-1$
        result.append(hintContent);
        result.append(')');
        return result.toString();
    }
} //CanvasModel
