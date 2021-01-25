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

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.ITextPosition;
import com.archimatetool.model.util.Logger;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Archimate Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateObject#getChildren <em>Children</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateObject#getTextPosition <em>Text Position</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateObject#getImagePath <em>Image Path</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateObject#getImagePosition <em>Image Position</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateObject#getArchimateElement <em>Archimate Element</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateObject#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramModelArchimateObject extends DiagramModelObject implements IDiagramModelArchimateObject {
    
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
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final int TYPE_EDEFAULT = 0;
    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected int type = TYPE_EDEFAULT;
    /**
     * Wrapped Archimate Element
     */
    private IArchimateElement fArchimateElement;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelArchimateObject() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelObject> getChildren() {
        if (children == null) {
            children = new EObjectContainmentEList<IDiagramModelObject>(IDiagramModelObject.class, this, IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN);
        }
        return children;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION, oldTextPosition, textPosition));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH, oldImagePath, imagePath));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION, oldImagePosition, imagePosition));
    }

    @Override
    public String getName() {
        if(getArchimateElement() != null) {
            return getArchimateElement().getName();
        }
        else {
            return super.getName();
        }
    }
    
    @Override
    public void setName(String newName) {
        if(getArchimateElement() != null) {
            getArchimateElement().setName(newName);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IArchimateElement getArchimateElement() {
        if(fArchimateElement == null) {
            Logger.logError("getArchimateElement() returning null", new Throwable()); //$NON-NLS-1$
        }
        
        return fArchimateElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void setArchimateElement(IArchimateElement archimateElement) {
        if(archimateElement == null) {
            Logger.logError("setArchimateElement() setting null", new Throwable()); //$NON-NLS-1$
        }
        
        // If we already have a concept we *must* remove it from the referenced list first
        if(fArchimateElement != null) {
            ((ArchimateElement)fArchimateElement).diagramObjects.remove(this);
        }
        
        fArchimateElement = archimateElement;
        
        ((ArchimateElement)fArchimateElement).diagramObjects.add(this);
    }
    
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class<?> baseClass, NotificationChain msgs) {
        // Re-Add a reference to this in the Archimate Element
        if(fArchimateElement != null) { // this will be null when a copy of this object is made
            ((ArchimateElement)fArchimateElement).diagramObjects.add(this);
        }
        return super.eInverseAdd(otherEnd, featureID, baseClass, msgs);
    }
    
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class<?> baseClass, NotificationChain msgs) {
        // Remove the reference to this in the Archimate Element
        if(fArchimateElement != null) { // this could be null...possibly?
            ((ArchimateElement)fArchimateElement).diagramObjects.remove(this);
        }
        return super.eInverseRemove(otherEnd, featureID, baseClass, msgs);
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setType(int newType) {
        int oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IArchimateElement getArchimateConcept() {
        return getArchimateElement();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void setArchimateConcept(IArchimateConcept concept) {
        if(!(concept instanceof IArchimateElement)) {
            throw new IllegalArgumentException("Should be of type IArchimateElement"); //$NON-NLS-1$
        }
        setArchimateElement((IArchimateElement)concept);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void addArchimateConceptToModel(IFolder parent) {
        IArchimateElement element = getArchimateElement();

        if(element != null && element.eContainer() != null) {
            throw new IllegalArgumentException("Element already has parent folder"); //$NON-NLS-1$
        }
        
        // If parent is null use default folder
        if(parent == null) {
            parent = getDiagramModel().getArchimateModel().getDefaultFolderForObject(element);
        }

        parent.getElements().add(element);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void removeArchimateConceptFromModel() {
        IArchimateElement element = getArchimateElement();
        if(element != null) {
            IFolder folder = (IFolder)element.eContainer();
            if(folder != null) {
                folder.getElements().remove(element);
            }
        }
    }

    @Override
    public EObject getCopy() {
        IDiagramModelArchimateObject newObject = (IDiagramModelArchimateObject)super.getCopy();
        
        IArchimateElement element = (IArchimateElement)getArchimateElement().getCopy();
        newObject.setArchimateElement(element);
        
        // Clear children
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN:
                return getChildren();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION:
                return getTextPosition();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH:
                return getImagePath();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION:
                return getImagePosition();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT:
                return getArchimateElement();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE:
                return getType();
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends IDiagramModelObject>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION:
                setTextPosition((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH:
                setImagePath((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION:
                setImagePosition((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT:
                setArchimateElement((IArchimateElement)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE:
                setType((Integer)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN:
                getChildren().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION:
                setTextPosition(TEXT_POSITION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH:
                setImagePath(IMAGE_PATH_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION:
                setImagePosition(IMAGE_POSITION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT:
                setArchimateElement((IArchimateElement)null);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE:
                setType(TYPE_EDEFAULT);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN:
                return children != null && !children.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION:
                return textPosition != TEXT_POSITION_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH:
                return IMAGE_PATH_EDEFAULT == null ? imagePath != null : !IMAGE_PATH_EDEFAULT.equals(imagePath);
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION:
                return imagePosition != IMAGE_POSITION_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT:
                return getArchimateElement() != null;
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE:
                return type != TYPE_EDEFAULT;
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
                case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelArchimateComponent.class) {
            switch (derivedFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == ITextPosition.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION: return IArchimatePackage.TEXT_POSITION__TEXT_POSITION;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IIconic.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION: return IArchimatePackage.ICONIC__IMAGE_POSITION;
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
                case IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__CHILDREN;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelArchimateComponent.class) {
            switch (baseFeatureID) {
                default: return -1;
            }
        }
        if (baseClass == ITextPosition.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_POSITION__TEXT_POSITION: return IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TEXT_POSITION;
                default: return -1;
            }
        }
        if (baseClass == IDiagramModelImageProvider.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_PATH;
                default: return -1;
            }
        }
        if (baseClass == IIconic.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.ICONIC__IMAGE_POSITION: return IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT__IMAGE_POSITION;
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
        result.append(", imagePath: "); //$NON-NLS-1$
        result.append(imagePath);
        result.append(", imagePosition: "); //$NON-NLS-1$
        result.append(imagePosition);
        result.append(", type: "); //$NON-NLS-1$
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //DiagramModelArchimateObject
