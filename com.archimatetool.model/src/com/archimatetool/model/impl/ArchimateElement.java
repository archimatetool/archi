/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public abstract class ArchimateElement extends ArchimateConcept implements IArchimateElement {
    
    /**
     * Stored references to Diagram Objects
     * Some of these may be orphaned so this is not an accurate list of live diagram objects
     */
    Set<IDiagramModelArchimateObject> diagramObjects = new HashSet<>();

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
    
    /*
     * It's not simply a case of returning the list of references.
     * If an *ancestor* of a dmo is deleted, or the diagram model itself, but not the direct parent,
     * the dmo will not be removed from the element's dmo reference list,
     * so we check if there is a top model ancestor on the referenced dmo.
     * If there is a top model ancestor, it's used in a diagram model.
     */
    @Override
    public List<IDiagramModelArchimateObject> getReferencingDiagramObjects() {
        List<IDiagramModelArchimateObject> list = new ArrayList<>();
        
        for(IDiagramModelArchimateObject dmo : diagramObjects) {
            if(dmo.getArchimateModel() != null) {
                list.add(dmo);
            }
        }
        
        return list;
    }
    
    @Override
    public List<IDiagramModelArchimateObject> getReferencingDiagramComponents() {
        return getReferencingDiagramObjects();
    }
    
} //ArchimateElement
