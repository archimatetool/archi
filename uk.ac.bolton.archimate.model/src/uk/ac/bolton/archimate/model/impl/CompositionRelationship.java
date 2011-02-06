/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.impl;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ICompositionRelationship;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composition Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class CompositionRelationship extends Relationship implements ICompositionRelationship {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CompositionRelationship() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.COMPOSITION_RELATIONSHIP;
    }

} //CompositionRelationship
