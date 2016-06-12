/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class ArchimateElement extends ArchimateComponent implements IArchimateElement {
    
    /**
     * Stored references to Diagram Objects
     */
    private EList<IDiagramModelArchimateObject> diagramObjects;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArchimateElement() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.ARCHIMATE_ELEMENT;
    }
    
    /* (non-Javadoc)
     * @see com.archimatetool.model.IArchimateElement#getReferencingDiagramObjects()
     */
    @Override
    public EList<IDiagramModelArchimateObject> getReferencingDiagramObjects() {
        if(diagramObjects == null) {
            diagramObjects = new UniqueEList<IDiagramModelArchimateObject>();
        }
        return diagramObjects;
    }
    
} //ArchimateElement
