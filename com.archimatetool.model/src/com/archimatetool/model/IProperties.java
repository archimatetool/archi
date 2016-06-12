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
 * A representation of the model object '<em><b>Properties</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IProperties#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getProperties()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IProperties extends EObject {
    /**
     * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IProperty}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Properties</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Properties</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getProperties_Properties()
     * @model containment="true"
     *        extendedMetaData="name='property' kind='element'"
     * @generated
     */
    EList<IProperty> getProperties();

} // IProperties
