/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getBounds <em>Bounds</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getFillColor <em>Fill Color</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getAlpha <em>Alpha</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject()
 * @model abstract="true"
 * @generated
 */
public interface IDiagramModelObject extends IConnectable, IFontAttribute, ILineObject, ITextAlignment {
    /**
     * Returns the value of the '<em><b>Bounds</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounds</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounds</em>' containment reference.
     * @see #setBounds(IBounds)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject_Bounds()
     * @model containment="true"
     * @generated
     */
    IBounds getBounds();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelObject#getBounds <em>Bounds</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounds</em>' containment reference.
     * @see #getBounds()
     * @generated
     */
    void setBounds(IBounds value);

    /**
     * Returns the value of the '<em><b>Fill Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Fill Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Fill Color</em>' attribute.
     * @see #setFillColor(String)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject_FillColor()
     * @model
     * @generated
     */
    String getFillColor();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelObject#getFillColor <em>Fill Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fill Color</em>' attribute.
     * @see #getFillColor()
     * @generated
     */
    void setFillColor(String value);

    /**
     * Returns the value of the '<em><b>Alpha</b></em>' attribute.
     * The default value is <code>"255"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Alpha</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Alpha</em>' attribute.
     * @see #setAlpha(int)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject_Alpha()
     * @model default="255"
     * @generated
     */
    int getAlpha();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelObject#getAlpha <em>Alpha</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Alpha</em>' attribute.
     * @see #getAlpha()
     * @generated
     */
    void setAlpha(int value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void setBounds(int x, int y, int width, int height);

} // IDiagramModelObject
