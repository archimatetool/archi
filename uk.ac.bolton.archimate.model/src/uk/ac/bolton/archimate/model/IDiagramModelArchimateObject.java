/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Archimate Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getArchimateElement <em>Archimate Element</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelArchimateObject()
 * @model
 * @generated
 */
public interface IDiagramModelArchimateObject extends IDiagramModelObject, IDiagramModelContainer {
    /**
     * Returns the value of the '<em><b>Archimate Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Archimate Element</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Archimate Element</em>' reference.
     * @see #setArchimateElement(IArchimateElement)
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelArchimateObject_ArchimateElement()
     * @model resolveProxies="false" volatile="true"
     * @generated
     */
    IArchimateElement getArchimateElement();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getArchimateElement <em>Archimate Element</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Archimate Element</em>' reference.
     * @see #getArchimateElement()
     * @generated
     */
    void setArchimateElement(IArchimateElement value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(int)
     * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelArchimateObject_Type()
     * @model default="0"
     * @generated
     */
    int getType();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(int value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void addArchimateElementToModel(IFolder parent);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void removeArchimateElementFromModel();

} // IDiagramModelArchimateObject
