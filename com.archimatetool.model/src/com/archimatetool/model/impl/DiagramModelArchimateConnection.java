/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IFolder;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateConnection#getArchimateRelationship <em>Archimate Relationship</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramModelArchimateConnection extends DiagramModelConnection implements IDiagramModelArchimateConnection {

    /**
     * Wrapped Archimate relationship
     */
    private IArchimateRelationship fRelationship;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelArchimateConnection() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_CONNECTION;
    }

    @Override
    public String getName() {
        if(getArchimateRelationship() != null) {
            return getArchimateRelationship().getName();
        }
        else {
            return super.getName();
        }
    }

    @Override
    public void setName(String name) {
        if(getArchimateRelationship() != null) {
            getArchimateRelationship().setName(name);
        }
    }
    
    @Override
    public void connect(IConnectable source, IConnectable target) {
        // Source and Target has to be IDiagramModelArchimateObject or IDiagramModelArchimateConnection
        if((source instanceof IDiagramModelArchimateComponent) && (target instanceof IDiagramModelArchimateComponent)) {
            super.connect(source, target);
        }
        else {
            throw new IllegalArgumentException("Should be Archimate objects/connections for source and target!"); //$NON-NLS-1$
        }
    }

    @Override
    public void reconnect() {
        super.reconnect();
        
        // Set the source/target in the Relationship
        if(source instanceof IDiagramModelArchimateComponent && target instanceof IDiagramModelArchimateComponent) {
            IArchimateConcept srcConcept = ((IDiagramModelArchimateComponent)source).getArchimateConcept();
            IArchimateConcept tgtConcept = ((IDiagramModelArchimateComponent)target).getArchimateConcept();
            
            getArchimateRelationship().setSource(srcConcept); 
            getArchimateRelationship().setTarget(tgtConcept);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IArchimateRelationship getArchimateRelationship() {
        return fRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void setArchimateRelationship(IArchimateRelationship relationship) {
        // If we already have a relationship we *must* remove it from the referenced list first
        if(fRelationship != null) {
            ((ArchimateRelationship)fRelationship).diagramConnections.remove(this);
        }
        
        if(relationship != null) {
            ((ArchimateRelationship)relationship).diagramConnections.add(this);
        }

        fRelationship = relationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IArchimateRelationship getArchimateConcept() {
        return getArchimateRelationship();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void setArchimateConcept(IArchimateConcept concept) {
        if(!(concept instanceof IArchimateRelationship)) {
            throw new IllegalArgumentException("Should be of type IArchimateRelationship"); //$NON-NLS-1$
        }
        setArchimateRelationship((IArchimateRelationship)concept);
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void addArchimateConceptToModel(IFolder parent) {
        IArchimateRelationship relationship = getArchimateRelationship();

        if(relationship != null && relationship.eContainer() != null) {
            throw new IllegalArgumentException("Relationship already has parent folder"); //$NON-NLS-1$
        }
        
        // If parent is null use default folder
        if(parent == null) {
            parent = getDiagramModel().getArchimateModel().getDefaultFolderForObject(relationship);
        }
        
        if(relationship != null) {
            parent.getElements().add(relationship);
            relationship.reconnect();
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void removeArchimateConceptFromModel() {
        IArchimateRelationship relationship = getArchimateRelationship();
        if(relationship != null) {
            IFolder folder = (IFolder)relationship.eContainer();
            if(folder != null) {
                relationship.disconnect(); // Do this first so a notification is sent before removing from model
                folder.getElements().remove(relationship);
            }
        }
    }

    @Override
    public EObject getCopy() {
        IDiagramModelArchimateConnection newConnection = (IDiagramModelArchimateConnection)super.getCopy();
        IArchimateRelationship relationship = (IArchimateRelationship)getArchimateRelationship().getCopy();
        newConnection.setArchimateRelationship(relationship);
        return newConnection;
    }
    
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class<?> baseClass, NotificationChain msgs) {
        // Add a reference to this in the Archimate Relationship
        if(fRelationship != null) { // this will be null when a copy of this object is made
            ((ArchimateRelationship)fRelationship).diagramConnections.add(this);
        }
        return super.eInverseAdd(otherEnd, featureID, baseClass, msgs);
    }
    
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class<?> baseClass, NotificationChain msgs) {
        // Remove the reference to this in the Archimate Relationship
        if(fRelationship != null) { // this may be null...possibly?
            ((ArchimateRelationship)fRelationship).diagramConnections.remove(this);
        }
        return super.eInverseRemove(otherEnd, featureID, baseClass, msgs);
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP:
                return getArchimateRelationship();
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP:
                setArchimateRelationship((IArchimateRelationship)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP:
                setArchimateRelationship((IArchimateRelationship)null);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP:
                return getArchimateRelationship() != null;
        }
        return super.eIsSet(featureID);
    }

} //DiagramModelConnection
