/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.impl;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFlowRelationship;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Flow Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class FlowRelationship extends Relationship implements IFlowRelationship {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FlowRelationship() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.FLOW_RELATIONSHIP;
    }

} //FlowRelationship
