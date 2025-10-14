/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Adapter</b></em>'.
 * 
 * <p>Set and get a key/value pair of arbitrary objects.</p>
 * <p>When calling getAdapter(key) if the value can't be found in this object, getAdapter(key) is called
 *    on the object'a eContainer and up the eContainer chain until it's found or returns null if not found.</p>
 * 
 * <!-- end-user-doc -->
 *
 *
 * @see com.archimatetool.model.IArchimatePackage#getAdapter()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IAdapter extends EObject {
    /**
     * <!-- begin-user-doc -->
     * <p>Returns the value for this key or null. If the value can't be found in this object, getAdapter(key) is called
     *    on the object's eContainer and up the eContainer chain until it's found or returns null if not found.</p>
     * <p>Null keys are not permitted</p>
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    Object getAdapter(Object key);

    /**
     * <!-- begin-user-doc -->
     * <p>Set the value for the given key. If value is null the key is removed.</p>
     * <p>Null keys are not permitted</p>
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void setAdapter(Object key, Object value);

} // IAdapter
