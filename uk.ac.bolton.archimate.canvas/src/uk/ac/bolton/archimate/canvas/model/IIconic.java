/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.canvas.model;

import uk.ac.bolton.archimate.model.IDiagramModelImageProvider;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Iconic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.canvas.model.IIconic#getImagePosition <em>Image Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.canvas.model.ICanvasPackage#getIconic()
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
    
    int MAX_IMAGE_SIZE = 100;

    /**
     * Returns the value of the '<em><b>Image Position</b></em>' attribute.
     * The default value is <code>"2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image Position</em>' attribute.
     * @see #setImagePosition(int)
     * @see uk.ac.bolton.archimate.canvas.model.ICanvasPackage#getIconic_ImagePosition()
     * @model default="2"
     * @generated
     */
    int getImagePosition();

    /**
     * Sets the value of the '{@link uk.ac.bolton.archimate.canvas.model.IIconic#getImagePosition <em>Image Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image Position</em>' attribute.
     * @see #getImagePosition()
     * @generated
     */
    void setImagePosition(int value);
} // IIconic
