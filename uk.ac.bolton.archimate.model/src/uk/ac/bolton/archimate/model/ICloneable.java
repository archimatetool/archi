/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cloneable</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getCloneable()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ICloneable extends EObject {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    EObject getCopy();

} // ICloneable
