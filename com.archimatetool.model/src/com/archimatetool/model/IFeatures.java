/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Features</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IFeatures#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFeatures()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IFeatures extends EObject {
    /**
     * Returns the value of the '<em><b>Features</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IFeature}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Features</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getFeatures_Features()
     * @model containment="true"
     *        extendedMetaData="name='feature' kind='element'"
     * @generated NOT
     */
    IFeaturesEList getFeatures();

} // IFeatures
