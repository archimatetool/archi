/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelBendpoint;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Bendpoint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelBendpoint#getStartX <em>Start X</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelBendpoint#getStartY <em>Start Y</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelBendpoint#getEndX <em>End X</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelBendpoint#getEndY <em>End Y</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramModelBendpoint extends EObjectImpl implements IDiagramModelBendpoint {
    /**
     * The default value of the '{@link #getStartX() <em>Start X</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartX()
     * @generated
     * @ordered
     */
    protected static final int START_X_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStartX() <em>Start X</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartX()
     * @generated
     * @ordered
     */
    protected int startX = START_X_EDEFAULT;

    /**
     * The default value of the '{@link #getStartY() <em>Start Y</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartY()
     * @generated
     * @ordered
     */
    protected static final int START_Y_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStartY() <em>Start Y</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartY()
     * @generated
     * @ordered
     */
    protected int startY = START_Y_EDEFAULT;

    /**
     * The default value of the '{@link #getEndX() <em>End X</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndX()
     * @generated
     * @ordered
     */
    protected static final int END_X_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getEndX() <em>End X</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndX()
     * @generated
     * @ordered
     */
    protected int endX = END_X_EDEFAULT;

    /**
     * The default value of the '{@link #getEndY() <em>End Y</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndY()
     * @generated
     * @ordered
     */
    protected static final int END_Y_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getEndY() <em>End Y</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndY()
     * @generated
     * @ordered
     */
    protected int endY = END_Y_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelBendpoint() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_BENDPOINT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getStartX() {
        return startX;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStartX(int newStartX) {
        int oldStartX = startX;
        startX = newStartX;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_X, oldStartX, startX));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getStartY() {
        return startY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStartY(int newStartY) {
        int oldStartY = startY;
        startY = newStartY;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_Y, oldStartY, startY));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getEndX() {
        return endX;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEndX(int newEndX) {
        int oldEndX = endX;
        endX = newEndX;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_X, oldEndX, endX));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getEndY() {
        return endY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEndY(int newEndY) {
        int oldEndY = endY;
        endY = newEndY;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_Y, oldEndY, endY));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EObject getCopy() {
        return EcoreUtil.copy(this);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_X:
                return getStartX();
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_Y:
                return getStartY();
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_X:
                return getEndX();
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_Y:
                return getEndY();
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
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_X:
                setStartX((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_Y:
                setStartY((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_X:
                setEndX((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_Y:
                setEndY((Integer)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_X:
                setStartX(START_X_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_Y:
                setStartY(START_Y_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_X:
                setEndX(END_X_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_Y:
                setEndY(END_Y_EDEFAULT);
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
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_X:
                return startX != START_X_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__START_Y:
                return startY != START_Y_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_X:
                return endX != END_X_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT__END_Y:
                return endY != END_Y_EDEFAULT;
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
        result.append(" (startX: "); //$NON-NLS-1$
        result.append(startX);
        result.append(", startY: "); //$NON-NLS-1$
        result.append(startY);
        result.append(", endX: "); //$NON-NLS-1$
        result.append(endX);
        result.append(", endY: "); //$NON-NLS-1$
        result.append(endY);
        result.append(')');
        return result.toString();
    }

} //DiagramModelBendpoint
