/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IProfile#isSpecialization <em>Specialization</em>}</li>
 *   <li>{@link com.archimatetool.model.IProfile#getConceptType <em>Concept Type</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getProfile()
 * @model
 * @generated
 */
public interface IProfile extends IArchimateModelObject, IDiagramModelImageProvider, ICloneable {
    /**
     * Returns the value of the '<em><b>Specialization</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Specialization</em>' attribute.
     * @see #setSpecialization(boolean)
     * @see com.archimatetool.model.IArchimatePackage#getProfile_Specialization()
     * @model default="true" required="true"
     * @generated
     */
    boolean isSpecialization();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IProfile#isSpecialization <em>Specialization</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Specialization</em>' attribute.
     * @see #isSpecialization()
     * @generated
     */
    void setSpecialization(boolean value);

    /**
     * Returns the value of the '<em><b>Concept Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Concept Type</em>' attribute.
     * @see #setConceptType(String)
     * @see com.archimatetool.model.IArchimatePackage#getProfile_ConceptType()
     * @model
     * @generated
     */
    String getConceptType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IProfile#getConceptType <em>Concept Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Concept Type</em>' attribute.
     * @see #getConceptType()
     * @generated
     */
    void setConceptType(String value);
    
    /**
     * @return The EClass for this Profile's concept type
     */
    EClass getConceptClass();

} // IProfile
