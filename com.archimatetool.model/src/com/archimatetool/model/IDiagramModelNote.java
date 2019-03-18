/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Note</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelNote#getBorderType <em>Border Type</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelNote()
 * @model extendedMetaData="name='Note'"
 * @generated
 */
public interface IDiagramModelNote extends IDiagramModelObject, ITextContent, ITextPosition, IProperties {

    int BORDER_DOGEAR = 0; // Default
    int BORDER_RECTANGLE = 1;
    int BORDER_NONE = 2;

    /**
     * Returns the value of the '<em><b>Border Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Border Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Border Type</em>' attribute.
     * @see #setBorderType(int)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelNote_BorderType()
     * @model
     * @generated
     */
    int getBorderType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelNote#getBorderType <em>Border Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Border Type</em>' attribute.
     * @see #getBorderType()
     * @generated
     */
    void setBorderType(int value);

} // IDiagramModelNote
