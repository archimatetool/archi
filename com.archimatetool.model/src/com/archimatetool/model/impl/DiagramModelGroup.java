/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBorderType;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.ITextPosition;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getChildren <em>Children</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getTextPosition <em>Text Position</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getBorderType <em>Border Type</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getImagePath <em>Image Path</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelGroup#getImagePosition <em>Image Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramModelGroup extends DiagramModelObject implements IDiagramModelGroup {
    /**
     * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<IDiagramModelObject> children;

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
     * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProperties()
     * @generated
     * @ordered
     */
    protected EList<IProperty> properties;

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
     * The default value of the '{@link #getBorderType() <em>Border Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBorderType()
     * @generated
     * @ordered
     */
    protected static final int BORDER_TYPE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getBorderType() <em>Border Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBorderType()
     * @generated
     * @ordered
     */
    protected int borderType = BORDER_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getImagePath() <em>Image Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImagePath()
     * @generated
     * @ordered
     */
    protected static final String IMAGE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getImagePath() <em>Image Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImagePath()
     * @generated
     * @ordered
     */
    protected String imagePath = IMAGE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getImagePosition() <em>Image Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImagePosition()
     * @generated
     * @ordered
     */
    protected static final int IMAGE_POSITION_EDEFAULT = 2;

    /**
     * The cached value of the '{@link #getImagePosition() <em>Image Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImagePosition()
     * @generated
     * @ordered
     */
    protected int imagePosition = IMAGE_POSITION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelGroup() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_GROUP;
    }

    @Override
    public EObject getCopy() {
        IDiagramModelGroup newObject = (IDiagramModelGroup)super.getCopy();
        newObject.getChildren().clear();
        return newObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelObject> getChildren() {
        if (children == null) {
            children = new EObjectContainmentEList<IDiagramModelObject>(IDiagramModelObject.class, this, IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN);
        }
        return children;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getDocumentation() {
        return documentation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDocumentation(String newDocumentation) {
        String oldDocumentation = documentation;
        documentation = newDocumentation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION, oldDocumentation, documentation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES);
        }
        return properties;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION, oldTextPosition, textPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getBorderType() {
        return borderType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setBorderType(int newBorderType) {
        int oldBorderType = borderType;
        borderType = newBorderType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE, oldBorderType, borderType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getImagePath() {
        return imagePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setImagePath(String newImagePath) {
        String oldImagePath = imagePath;
        imagePath = newImagePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH, oldImagePath, imagePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getImagePosition() {
        return imagePosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setImagePosition(int newImagePosition) {
        int oldImagePosition = imagePosition;
        imagePosition = newImagePosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION, oldImagePosition, imagePosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES:
                return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN:
                return getChildren();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION:
                return getDocumentation();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES:
                return getProperties();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION:
                return getTextPosition();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE:
                return getBorderType();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH:
                return getImagePath();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION:
                return getImagePosition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends IDiagramModelObject>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION:
                setDocumentation((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION:
                setTextPosition((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE:
                setBorderType((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH:
                setImagePath((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION:
                setImagePosition((Integer)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN:
                getChildren().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION:
                setDocumentation(DOCUMENTATION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES:
                getProperties().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION:
                setTextPosition(TEXT_POSITION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE:
                setBorderType(BORDER_TYPE_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH:
                setImagePath(IMAGE_PATH_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION:
                setImagePosition(IMAGE_POSITION_EDEFAULT);
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
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN:
                return children != null && !children.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION:
                return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION:
                return textPosition != TEXT_POSITION_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE:
                return borderType != BORDER_TYPE_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH:
                return IMAGE_PATH_EDEFAULT == null ? imagePath != null : !IMAGE_PATH_EDEFAULT.equals(imagePath);
            case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION:
                return imagePosition != IMAGE_POSITION_EDEFAULT;
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
        if (baseClass == IDiagramModelContainer.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == ITextPosition.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION: return IArchimatePackage.TEXT_POSITION__TEXT_POSITION;
                default: return -1;
            }
        }
        if (baseClass == IBorderType.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE: return IArchimatePackage.BORDER_TYPE__BORDER_TYPE;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IIconic.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION: return IArchimatePackage.ICONIC__IMAGE_POSITION;
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
        if (baseClass == IDiagramModelContainer.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_GROUP__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.DIAGRAM_MODEL_GROUP__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.DIAGRAM_MODEL_GROUP__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == ITextPosition.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_POSITION__TEXT_POSITION: return IArchimatePackage.DIAGRAM_MODEL_GROUP__TEXT_POSITION;
                default: return -1;
            }
        }
        if (baseClass == IBorderType.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.BORDER_TYPE__BORDER_TYPE: return IArchimatePackage.DIAGRAM_MODEL_GROUP__BORDER_TYPE;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IIconic.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.ICONIC__IMAGE_POSITION: return IArchimatePackage.DIAGRAM_MODEL_GROUP__IMAGE_POSITION;
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
        result.append(" (documentation: "); //$NON-NLS-1$
        result.append(documentation);
        result.append(", textPosition: "); //$NON-NLS-1$
        result.append(textPosition);
        result.append(", borderType: "); //$NON-NLS-1$
        result.append(borderType);
        result.append(", imagePath: "); //$NON-NLS-1$
        result.append(imagePath);
        result.append(", imagePosition: "); //$NON-NLS-1$
        result.append(imagePosition);
        result.append(')');
        return result.toString();
    }

} //DiagramModelGroup
