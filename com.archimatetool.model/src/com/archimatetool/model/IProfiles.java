/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profiles</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IProfiles#getProfiles <em>Profiles</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getProfiles()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IProfiles extends EObject {
    /**
     * Returns the value of the '<em><b>Profiles</b></em>' reference list.
     * The list contents are of type {@link com.archimatetool.model.IProfile}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Profiles</em>' reference list.
     * @see com.archimatetool.model.IArchimatePackage#getProfiles_Profiles()
     * @model resolveProxies="false"
     * @generated
     */
    EList<IProfile> getProfiles();
    
    /**
     * @return The Primary Profile used for image or null if not set
     */
    IProfile getPrimaryProfile();

} // IProfiles
