/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.canvas.model.impl;

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

import uk.ac.bolton.archimate.canvas.model.ICanvasModelBlock;
import uk.ac.bolton.archimate.canvas.model.ICanvasPackage;
import uk.ac.bolton.archimate.canvas.model.IHintProvider;
import uk.ac.bolton.archimate.help.hints.IHelpHintProvider;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IBorderObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelImageProvider;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.ILockable;
import uk.ac.bolton.archimate.model.IProperties;
import uk.ac.bolton.archimate.model.IProperty;
import uk.ac.bolton.archimate.model.ITextContent;
import uk.ac.bolton.archimate.model.impl.DiagramModelObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getImagePath <em>Image Path</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getImagePosition <em>Image Position</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getChildren <em>Children</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getProperties <em>Properties</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#isLocked <em>Locked</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getBorderColor <em>Border Color</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getHintTitle <em>Hint Title</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getHintContent <em>Hint Content</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.impl.CanvasModelBlock#getContent <em>Content</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CanvasModelBlock extends DiagramModelObject implements ICanvasModelBlock {
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
     * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<IDiagramModelObject> children;

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
     * The default value of the '{@link #isLocked() <em>Locked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLocked()
     * @generated
     * @ordered
     */
    protected static final boolean LOCKED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isLocked() <em>Locked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLocked()
     * @generated
     * @ordered
     */
    protected boolean locked = LOCKED_EDEFAULT;

    /**
     * The default value of the '{@link #getBorderColor() <em>Border Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBorderColor()
     * @generated
     * @ordered
     */
    protected static final String BORDER_COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBorderColor() <em>Border Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBorderColor()
     * @generated
     * @ordered
     */
    protected String borderColor = BORDER_COLOR_EDEFAULT;

    /**
     * The default value of the '{@link #getHintTitle() <em>Hint Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHintTitle()
     * @generated
     * @ordered
     */
    protected static final String HINT_TITLE_EDEFAULT = null;

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
    protected static final String HINT_CONTENT_EDEFAULT = null;

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
     * The default value of the '{@link #getContent() <em>Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContent()
     * @generated
     * @ordered
     */
    protected static final String CONTENT_EDEFAULT = "";

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CanvasModelBlock() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ICanvasPackage.Literals.CANVAS_MODEL_BLOCK;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IDiagramModelObject> getChildren() {
        if (children == null) {
            children = new EObjectContainmentEList<IDiagramModelObject>(IDiagramModelObject.class, this, ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getContent() {
        return content;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContent(String newContent) {
        String oldContent = content;
        content = newContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT, oldContent, content));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES);
        }
        return properties;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLocked(boolean newLocked) {
        boolean oldLocked = locked;
        locked = newLocked;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED, oldLocked, locked));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImagePath(String newImagePath) {
        String oldImagePath = imagePath;
        imagePath = newImagePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH, oldImagePath, imagePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getImagePosition() {
        return imagePosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImagePosition(int newImagePosition) {
        int oldImagePosition = imagePosition;
        imagePosition = newImagePosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_POSITION, oldImagePosition, imagePosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHintTitle() {
        return hintTitle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHintTitle(String newHintTitle) {
        String oldHintTitle = hintTitle;
        hintTitle = newHintTitle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE, oldHintTitle, hintTitle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHintContent() {
        return hintContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHintContent(String newHintContent) {
        String oldHintContent = hintContent;
        hintContent = newHintContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT, oldHintContent, hintContent));
    }

    @Override
    public String getHelpHintTitle() {
        return getHintTitle();
    }

    @Override
    public String getHelpHintContent() {
        return getHintContent();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBorderColor(String newBorderColor) {
        String oldBorderColor = borderColor;
        borderColor = newBorderColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR, oldBorderColor, borderColor));
    }

    @Override
    public int getDefaultTextAlignment() {
        return TEXT_ALIGNMENT_LEFT;
    }
    
    @Override
    public EObject getCopy() {
        ICanvasModelBlock newObject = (ICanvasModelBlock)super.getCopy();
        newObject.getChildren().clear(); // need to do this!
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
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
            case ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES:
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
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH:
                return getImagePath();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_POSITION:
                return getImagePosition();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN:
                return getChildren();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES:
                return getProperties();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED:
                return isLocked();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR:
                return getBorderColor();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE:
                return getHintTitle();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT:
                return getHintContent();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT:
                return getContent();
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
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH:
                setImagePath((String)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_POSITION:
                setImagePosition((Integer)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends IDiagramModelObject>)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED:
                setLocked((Boolean)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR:
                setBorderColor((String)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE:
                setHintTitle((String)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT:
                setHintContent((String)newValue);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT:
                setContent((String)newValue);
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
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH:
                setImagePath(IMAGE_PATH_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_POSITION:
                setImagePosition(IMAGE_POSITION_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN:
                getChildren().clear();
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES:
                getProperties().clear();
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED:
                setLocked(LOCKED_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR:
                setBorderColor(BORDER_COLOR_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE:
                setHintTitle(HINT_TITLE_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT:
                setHintContent(HINT_CONTENT_EDEFAULT);
                return;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT:
                setContent(CONTENT_EDEFAULT);
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
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH:
                return IMAGE_PATH_EDEFAULT == null ? imagePath != null : !IMAGE_PATH_EDEFAULT.equals(imagePath);
            case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_POSITION:
                return imagePosition != IMAGE_POSITION_EDEFAULT;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN:
                return children != null && !children.isEmpty();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED:
                return locked != LOCKED_EDEFAULT;
            case ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR:
                return BORDER_COLOR_EDEFAULT == null ? borderColor != null : !BORDER_COLOR_EDEFAULT.equals(borderColor);
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE:
                return HINT_TITLE_EDEFAULT == null ? hintTitle != null : !HINT_TITLE_EDEFAULT.equals(hintTitle);
            case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT:
                return HINT_CONTENT_EDEFAULT == null ? hintContent != null : !HINT_CONTENT_EDEFAULT.equals(hintContent);
            case ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT:
                return CONTENT_EDEFAULT == null ? content != null : !CONTENT_EDEFAULT.equals(content);
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
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelContainer.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == ILockable.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED: return IArchimatePackage.LOCKABLE__LOCKED;
                default: return -1;
            }
        }
        if (baseClass == IBorderObject.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR: return IArchimatePackage.BORDER_OBJECT__BORDER_COLOR;
                default: return -1;
            }
        }
        if (baseClass == IHelpHintProvider.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IHintProvider.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE: return ICanvasPackage.HINT_PROVIDER__HINT_TITLE;
                case ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT: return ICanvasPackage.HINT_PROVIDER__HINT_CONTENT;
                default: return -1;
            }
        }
        if (baseClass == ITextContent.class) {
            switch (derivedFeatureID) {
                case ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT: return IArchimatePackage.TEXT_CONTENT__CONTENT;
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
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH: return ICanvasPackage.CANVAS_MODEL_BLOCK__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelContainer.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN: return ICanvasPackage.CANVAS_MODEL_BLOCK__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return ICanvasPackage.CANVAS_MODEL_BLOCK__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == ILockable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.LOCKABLE__LOCKED: return ICanvasPackage.CANVAS_MODEL_BLOCK__LOCKED;
                default: return -1;
            }
        }
        if (baseClass == IBorderObject.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.BORDER_OBJECT__BORDER_COLOR: return ICanvasPackage.CANVAS_MODEL_BLOCK__BORDER_COLOR;
                default: return -1;
            }
        }
        if (baseClass == IHelpHintProvider.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == IHintProvider.class) {
            switch (baseFeatureID) {
                case ICanvasPackage.HINT_PROVIDER__HINT_TITLE: return ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_TITLE;
                case ICanvasPackage.HINT_PROVIDER__HINT_CONTENT: return ICanvasPackage.CANVAS_MODEL_BLOCK__HINT_CONTENT;
                default: return -1;
            }
        }
        if (baseClass == ITextContent.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_CONTENT__CONTENT: return ICanvasPackage.CANVAS_MODEL_BLOCK__CONTENT;
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
        result.append(" (imagePath: "); //$NON-NLS-1$
        result.append(imagePath);
        result.append(", imagePosition: "); //$NON-NLS-1$
        result.append(imagePosition);
        result.append(", locked: "); //$NON-NLS-1$
        result.append(locked);
        result.append(", borderColor: "); //$NON-NLS-1$
        result.append(borderColor);
        result.append(", hintTitle: "); //$NON-NLS-1$
        result.append(hintTitle);
        result.append(", hintContent: "); //$NON-NLS-1$
        result.append(hintContent);
        result.append(", content: "); //$NON-NLS-1$
        result.append(content);
        result.append(')');
        return result.toString();
    }

} //CanvasModelBlock
