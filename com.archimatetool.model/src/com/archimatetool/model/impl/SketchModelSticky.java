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
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.ISketchModelSticky;
import com.archimatetool.model.ITextContent;
import com.archimatetool.model.ITextPosition;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sketch Model Sticky</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.SketchModelSticky#getChildren <em>Children</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.SketchModelSticky#getContent <em>Content</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.SketchModelSticky#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.SketchModelSticky#getTextPosition <em>Text Position</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.SketchModelSticky#getImagePath <em>Image Path</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.SketchModelSticky#getImagePosition <em>Image Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SketchModelSticky extends DiagramModelObject implements ISketchModelSticky {
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
     * The default value of the '{@link #getContent() <em>Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContent()
     * @generated
     * @ordered
     */
    protected static final String CONTENT_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getContent() <em>Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContent()
     * @generated
     * @ordered
     */
    protected String content = CONTENT_EDEFAULT;

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
    protected SketchModelSticky() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.SKETCH_MODEL_STICKY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelObject> getChildren() {
        if (children == null) {
            children = new EObjectContainmentEList<IDiagramModelObject>(IDiagramModelObject.class, this, IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getContent() {
        return content;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setContent(String newContent) {
        String oldContent = content;
        content = newContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT, oldContent, content));
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES);
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION, oldTextPosition, textPosition));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH, oldImagePath, imagePath));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION, oldImagePosition, imagePosition));
    }

    @Override
    public EObject getCopy() {
        ISketchModelSticky newObject = (ISketchModelSticky)super.getCopy();
        //newObject.setContent(getContent());
        newObject.getChildren().clear();
        return newObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES:
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
            case IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN:
                return getChildren();
            case IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT:
                return getContent();
            case IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES:
                return getProperties();
            case IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION:
                return getTextPosition();
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH:
                return getImagePath();
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION:
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
            case IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends IDiagramModelObject>)newValue);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT:
                setContent((String)newValue);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION:
                setTextPosition((Integer)newValue);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH:
                setImagePath((String)newValue);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION:
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
            case IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN:
                getChildren().clear();
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT:
                setContent(CONTENT_EDEFAULT);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES:
                getProperties().clear();
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION:
                setTextPosition(TEXT_POSITION_EDEFAULT);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH:
                setImagePath(IMAGE_PATH_EDEFAULT);
                return;
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION:
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
            case IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN:
                return children != null && !children.isEmpty();
            case IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT:
                return CONTENT_EDEFAULT == null ? content != null : !CONTENT_EDEFAULT.equals(content);
            case IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION:
                return textPosition != TEXT_POSITION_EDEFAULT;
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH:
                return IMAGE_PATH_EDEFAULT == null ? imagePath != null : !IMAGE_PATH_EDEFAULT.equals(imagePath);
            case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION:
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
                case IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == ITextContent.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT: return IArchimatePackage.TEXT_CONTENT__CONTENT;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == ITextPosition.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION: return IArchimatePackage.TEXT_POSITION__TEXT_POSITION;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IIconic.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION: return IArchimatePackage.ICONIC__IMAGE_POSITION;
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
                case IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN: return IArchimatePackage.SKETCH_MODEL_STICKY__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == ITextContent.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_CONTENT__CONTENT: return IArchimatePackage.SKETCH_MODEL_STICKY__CONTENT;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.SKETCH_MODEL_STICKY__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == ITextPosition.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_POSITION__TEXT_POSITION: return IArchimatePackage.SKETCH_MODEL_STICKY__TEXT_POSITION;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH: return IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IIconic.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.ICONIC__IMAGE_POSITION: return IArchimatePackage.SKETCH_MODEL_STICKY__IMAGE_POSITION;
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
        result.append(" (content: "); //$NON-NLS-1$
        result.append(content);
        result.append(", textPosition: "); //$NON-NLS-1$
        result.append(textPosition);
        result.append(", imagePath: "); //$NON-NLS-1$
        result.append(imagePath);
        result.append(", imagePosition: "); //$NON-NLS-1$
        result.append(imagePosition);
        result.append(')');
        return result.toString();
    }

} //SketchModelSticky
