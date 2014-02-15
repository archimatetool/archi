/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.Logger;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelArchimateConnection#getRelationship <em>Relationship</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramModelArchimateConnection extends DiagramModelConnection implements IDiagramModelArchimateConnection {
    /**
     * Wrapped Archimate relationship
     */
    private IRelationship fRelationship;

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
        if(getRelationship() != null) {
            return getRelationship().getName();
        }
        else {
            return super.getName();
        }
    }

    @Override
    public void setName(String name) {
        if(getRelationship() != null) {
            getRelationship().setName(name);
        }
    }
    
    @Override
    public void connect(IDiagramModelObject source, IDiagramModelObject target) {
        if(!(source instanceof IDiagramModelArchimateObject) && !(target instanceof IDiagramModelArchimateObject)) {
            throw new IllegalArgumentException("Should be Archimate objects for source and target!"); //$NON-NLS-1$
        }
        super.connect(source, target);
    }

    @Override
    public void reconnect() {
        if(source != null && target != null) {
            super.reconnect();

            // Set the source/target in the Archimate model if need be
            IRelationship relationship = getRelationship();
            IArchimateElement src = ((IDiagramModelArchimateObject)source).getArchimateElement();
            IArchimateElement tgt = ((IDiagramModelArchimateObject)target).getArchimateElement();
            if(relationship.getSource() != src) { //optimised
                relationship.setSource(src); 
            }
            if(relationship.getTarget() != tgt) { //optimised
                relationship.setTarget(tgt);
            }
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public IRelationship getRelationship() {
        if(fRelationship == null) {
            Logger.logError("getRelationship() returning null", new Throwable()); //$NON-NLS-1$
        }
        
        return fRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void setRelationship(IRelationship relationship) {
        if(relationship == null) {
            Logger.logError("setRelationship() setting null", new Throwable()); //$NON-NLS-1$
        }
        
        fRelationship = relationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void addRelationshipToModel(IFolder parent) {
        IRelationship relationship = getRelationship();

        if(relationship != null && relationship.eContainer() != null) {
            throw new IllegalArgumentException("Relationship already has parent folder"); //$NON-NLS-1$
        }
        
        // If parent is null use default folder
        if(parent == null) {
            parent = getDiagramModel().getArchimateModel().getDefaultFolderForElement(relationship);
        }
        
        parent.getElements().add(relationship);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void removeRelationshipFromModel() {
        IRelationship relationship = getRelationship();
        if(relationship != null) {
            IFolder folder = (IFolder)relationship.eContainer();
            if(folder != null) {
                folder.getElements().remove(relationship);
            }
        }
    }

    @Override
    public EObject getCopy() {
        IDiagramModelArchimateConnection newConnection = (IDiagramModelArchimateConnection)super.getCopy();
        IRelationship relationship = (IRelationship)getRelationship().getCopy();
        newConnection.setRelationship(relationship);
        return newConnection;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP:
                return getRelationship();
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP:
                setRelationship((IRelationship)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP:
                setRelationship((IRelationship)null);
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
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP:
                return getRelationship() != null;
        }
        return super.eIsSet(featureID);
    }

} //DiagramModelConnection
