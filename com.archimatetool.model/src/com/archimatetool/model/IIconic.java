/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Iconic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IIconic#getImagePosition <em>Image Position</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getIconic()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IIconic extends IDiagramModelObject, IDiagramModelImageProvider {
    
    int ICON_POSITION_TOP_LEFT = 0;
    int ICON_POSITION_TOP_CENTRE = 1;
    int ICON_POSITION_TOP_RIGHT = 2;
    int ICON_POSITION_MIDDLE_LEFT = 3;
    int ICON_POSITION_MIDDLE_CENTRE = 4;
    int ICON_POSITION_MIDDLE_RIGHT = 5;
    int ICON_POSITION_BOTTOM_LEFT = 6;
    int ICON_POSITION_BOTTOM_CENTRE = 7;
    int ICON_POSITION_BOTTOM_RIGHT = 8;
    
    /**
     * Returns the value of the '<em><b>Image Position</b></em>' attribute.
     * The default value is <code>"2"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image Position</em>' attribute.
     * @see #setImagePosition(int)
     * @see com.archimatetool.model.IArchimatePackage#getIconic_ImagePosition()
     * @model default="2"
     * @generated
     */
    int getImagePosition();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IIconic#getImagePosition <em>Image Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image Position</em>' attribute.
     * @see #getImagePosition()
     * @generated
     */
    void setImagePosition(int value);

} // IIconic
