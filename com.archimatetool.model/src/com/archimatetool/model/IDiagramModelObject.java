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
@SuppressWarnings("nls")
public interface IDiagramModelObject extends IConnectable, IFontAttribute, ILineObject, ITextAlignment {
    
    String FEATURE_LINE_ALPHA = "lineAlpha";
    int FEATURE_LINE_ALPHA_DEFAULT = 255;
    
    String FEATURE_GRADIENT = "gradient";
    int GRADIENT_NONE = -1;
    int FEATURE_GRADIENT_DEFAULT = GRADIENT_NONE;
    
    String FEATURE_ICON_VISIBLE = "iconVisible";
    int ICON_VISIBLE_IF_NO_IMAGE_DEFINED = 0;
    int ICON_VISIBLE_ALWAYS = 1;
    int ICON_VISIBLE_NEVER = 2;
    int FEATURE_ICON_VISIBLE_DEFAULT = ICON_VISIBLE_IF_NO_IMAGE_DEFINED;
    
    String FEATURE_ICON_COLOR = "iconColor";
    String FEATURE_ICON_COLOR_DEFAULT = "";
    
    String FEATURE_DERIVE_ELEMENT_LINE_COLOR = "deriveElementLineColor";
    boolean FEATURE_DERIVE_ELEMENT_LINE_COLOR_DEFAULT = true;
    
    String FEATURE_LINE_STYLE = "lineStyle";
    int LINE_STYLE_DEFAULT = -1;
    int LINE_STYLE_SOLID = 0;
    int LINE_STYLE_DASHED = 1;
    int LINE_STYLE_DOTTED = 2;
    int LINE_STYLE_NONE = 3;
    int FEATURE_LINE_STYLE_DEFAULT = LINE_STYLE_DEFAULT;
    
    /**
     * @return the value of FEATURE_LINE_ALPHA
     */
    int getLineAlpha();
    
    /**
     * Set the value of FEATURE_LINE_ALPHA
     * @param value
     */
    void setLineAlpha(int value);
    
    /**
     * @return the gradient type FEATURE_GRADIENT
     */
    int getGradient();
    
    /**
     * Set the gradient type FEATURE_GRADIENT
     * @param type The type
     */
    void setGradient(int type);
    
    /**
     * @return the icon visible state
     */
    int getIconVisibleState();
    
    /**
     * Set the value of feature FEATURE_ICON_VISIBLE
     * @param visible the value
     */
    void setIconVisibleState(int value);
    
    /**
     * @return The icon color feature FEATURE_ICON_COLOR
     */
    String getIconColor();
    
    /**
     * Set the value of feature FEATURE_ICON_COLOR
     * @param iconColor
     */
    void setIconColor(String iconColor);
    
    /**
     * @return true if this element derives its line color from its fill color
     */
    boolean getDeriveElementLineColor();
    
    /**
     * Set whether this element derives its line color from its fill color
     * @param value value
     */
    void setDeriveElementLineColor(boolean value);
    
    /**
     * @return the value of feature FEATURE_LINE_STYLE
     */
    int getLineStyle();
    
    /**
     * Set the value of feature FEATURE_LINE_STYLE
     * @param lineStyle
     */
    void setLineStyle(int lineStyle);
    
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
