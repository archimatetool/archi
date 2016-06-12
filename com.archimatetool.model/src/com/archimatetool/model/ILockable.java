/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lockable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.ILockable#isLocked <em>Locked</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getLockable()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ILockable extends EObject {
    /**
     * Returns the value of the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Locked</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Locked</em>' attribute.
     * @see #setLocked(boolean)
     * @see com.archimatetool.model.IArchimatePackage#getLockable_Locked()
     * @model
     * @generated
     */
    boolean isLocked();

    /**
     * Sets the value of the '{@link com.archimatetool.model.ILockable#isLocked <em>Locked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Locked</em>' attribute.
     * @see #isLocked()
     * @generated
     */
    void setLocked(boolean value);

} // ILockable
