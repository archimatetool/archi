/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Object</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.archimatetool.model.IArchimatePackage#getArchimateModelObject()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IArchimateModelObject extends IAdapter, INameable, IIdentifier, IFeatures {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    IArchimateModel getArchimateModel();

} // IArchimateModelObject
