/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ITextAlignment;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getFont <em>Font</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getFontColor <em>Font Color</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getLineWidth <em>Line Width</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getLineColor <em>Line Color</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getTextAlignment <em>Text Alignment</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getBounds <em>Bounds</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getFillColor <em>Fill Color</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelObject#getAlpha <em>Alpha</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DiagramModelObject extends Connectable implements IDiagramModelObject {
    /**
     * The default value of the '{@link #getFont() <em>Font</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFont()
     * @generated
     * @ordered
     */
    protected static final String FONT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFont() <em>Font</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFont()
     * @generated
     * @ordered
     */
    protected String font = FONT_EDEFAULT;

    /**
     * The default value of the '{@link #getFontColor() <em>Font Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFontColor()
     * @generated
     * @ordered
     */
    protected static final String FONT_COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFontColor() <em>Font Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFontColor()
     * @generated
     * @ordered
     */
    protected String fontColor = FONT_COLOR_EDEFAULT;

    /**
     * The default value of the '{@link #getLineWidth() <em>Line Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineWidth()
     * @generated
     * @ordered
     */
    protected static final int LINE_WIDTH_EDEFAULT = 1;

    /**
     * The cached value of the '{@link #getLineWidth() <em>Line Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineWidth()
     * @generated
     * @ordered
     */
    protected int lineWidth = LINE_WIDTH_EDEFAULT;

    /**
     * The default value of the '{@link #getLineColor() <em>Line Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineColor()
     * @generated
     * @ordered
     */
    protected static final String LINE_COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLineColor() <em>Line Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineColor()
     * @generated
     * @ordered
     */
    protected String lineColor = LINE_COLOR_EDEFAULT;

    /**
     * The default value of the '{@link #getTextAlignment() <em>Text Alignment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextAlignment()
     * @generated
     * @ordered
     */
    protected static final int TEXT_ALIGNMENT_EDEFAULT = 2;

    /**
     * The cached value of the '{@link #getTextAlignment() <em>Text Alignment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextAlignment()
     * @generated
     * @ordered
     */
    protected int textAlignment = TEXT_ALIGNMENT_EDEFAULT;

    /**
     * The cached value of the '{@link #getBounds() <em>Bounds</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBounds()
     * @generated
     * @ordered
     */
    protected IBounds bounds;

    /**
     * The default value of the '{@link #getFillColor() <em>Fill Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFillColor()
     * @generated
     * @ordered
     */
    protected static final String FILL_COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFillColor() <em>Fill Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFillColor()
     * @generated
     * @ordered
     */
    protected String fillColor = FILL_COLOR_EDEFAULT;

    /**
     * The default value of the '{@link #getAlpha() <em>Alpha</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlpha()
     * @generated
     * @ordered
     */
    protected static final int ALPHA_EDEFAULT = 255;

    /**
     * The cached value of the '{@link #getAlpha() <em>Alpha</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlpha()
     * @generated
     * @ordered
     */
    protected int alpha = ALPHA_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelObject() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT;
    }

    @Override
    public int getLineAlpha() {
        return getFeatures().getInt(FEATURE_LINE_ALPHA, FEATURE_LINE_ALPHA_DEFAULT);
    }
    
    @Override
    public void setLineAlpha(int value) {
        getFeatures().putInt(FEATURE_LINE_ALPHA, value, FEATURE_LINE_ALPHA_DEFAULT);
    }

    @Override
    public int getGradient() {
        return getFeatures().getInt(FEATURE_GRADIENT, FEATURE_GRADIENT_DEFAULT);
    }
    
    @Override
    public void setGradient(int type) {
        getFeatures().putInt(FEATURE_GRADIENT, type, FEATURE_GRADIENT_DEFAULT);
    }

    @Override
    public boolean isIconVisible() {
        return getFeatures().getBoolean(FEATURE_ICON_VISIBLE, FEATURE_ICON_VISIBLE_DEFAULT);
    }
    
    @Override
    public void setIconVisible(boolean visible) {
        getFeatures().putBoolean(FEATURE_ICON_VISIBLE, visible, FEATURE_ICON_VISIBLE_DEFAULT);
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBounds getBounds() {
        return bounds;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        IBounds bounds = IArchimateFactory.eINSTANCE.createBounds(x, y, width, height);
        setBounds(bounds);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBounds(IBounds newBounds, NotificationChain msgs) {
        IBounds oldBounds = bounds;
        bounds = newBounds;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS, oldBounds, newBounds);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setBounds(IBounds newBounds) {
        if (newBounds != bounds) {
            NotificationChain msgs = null;
            if (bounds != null)
                msgs = ((InternalEObject)bounds).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS, null, msgs);
            if (newBounds != null)
                msgs = ((InternalEObject)newBounds).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS, null, msgs);
            msgs = basicSetBounds(newBounds, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS, newBounds, newBounds));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getFillColor() {
        return fillColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFillColor(String newFillColor) {
        String oldFillColor = fillColor;
        fillColor = newFillColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__FILL_COLOR, oldFillColor, fillColor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getAlpha() {
        return alpha;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAlpha(int newAlpha) {
        int oldAlpha = alpha;
        alpha = newAlpha;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__ALPHA, oldAlpha, alpha));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getFont() {
        return font;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFont(String newFont) {
        String oldFont = font;
        font = newFont;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT, oldFont, font));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getFontColor() {
        return fontColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFontColor(String newFontColor) {
        String oldFontColor = fontColor;
        fontColor = newFontColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR, oldFontColor, fontColor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getTextAlignment() {
        return textAlignment;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTextAlignment(int newTextAlignment) {
        int oldTextAlignment = textAlignment;
        textAlignment = newTextAlignment;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT, oldTextAlignment, textAlignment));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLineWidth(int newLineWidth) {
        int oldLineWidth = lineWidth;
        lineWidth = newLineWidth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH, oldLineWidth, lineWidth));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getLineColor() {
        return lineColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLineColor(String newLineColor) {
        String oldLineColor = lineColor;
        lineColor = newLineColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR, oldLineColor, lineColor));
    }

    @Override
    public EObject getCopy() {
        IDiagramModelObject newObject = (IDiagramModelObject)super.getCopy();
        
        newObject.getSourceConnections().clear();
        newObject.getTargetConnections().clear();
        
        return newObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS:
                return basicSetBounds(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT:
                return getFont();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR:
                return getFontColor();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH:
                return getLineWidth();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR:
                return getLineColor();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT:
                return getTextAlignment();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS:
                return getBounds();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FILL_COLOR:
                return getFillColor();
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__ALPHA:
                return getAlpha();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT:
                setFont((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR:
                setFontColor((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH:
                setLineWidth((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR:
                setLineColor((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT:
                setTextAlignment((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS:
                setBounds((IBounds)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FILL_COLOR:
                setFillColor((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__ALPHA:
                setAlpha((Integer)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT:
                setFont(FONT_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR:
                setFontColor(FONT_COLOR_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH:
                setLineWidth(LINE_WIDTH_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR:
                setLineColor(LINE_COLOR_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT:
                setTextAlignment(TEXT_ALIGNMENT_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS:
                setBounds((IBounds)null);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FILL_COLOR:
                setFillColor(FILL_COLOR_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__ALPHA:
                setAlpha(ALPHA_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT:
                return FONT_EDEFAULT == null ? font != null : !FONT_EDEFAULT.equals(font);
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR:
                return FONT_COLOR_EDEFAULT == null ? fontColor != null : !FONT_COLOR_EDEFAULT.equals(fontColor);
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH:
                return lineWidth != LINE_WIDTH_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR:
                return LINE_COLOR_EDEFAULT == null ? lineColor != null : !LINE_COLOR_EDEFAULT.equals(lineColor);
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT:
                return textAlignment != TEXT_ALIGNMENT_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__BOUNDS:
                return bounds != null;
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FILL_COLOR:
                return FILL_COLOR_EDEFAULT == null ? fillColor != null : !FILL_COLOR_EDEFAULT.equals(fillColor);
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT__ALPHA:
                return alpha != ALPHA_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == IFontAttribute.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT: return IArchimatePackage.FONT_ATTRIBUTE__FONT;
                case IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR: return IArchimatePackage.FONT_ATTRIBUTE__FONT_COLOR;
                default: return -1;
            }
        }
        if (baseClass == ILineObject.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH: return IArchimatePackage.LINE_OBJECT__LINE_WIDTH;
                case IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR: return IArchimatePackage.LINE_OBJECT__LINE_COLOR;
                default: return -1;
            }
        }
        if (baseClass == ITextAlignment.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT: return IArchimatePackage.TEXT_ALIGNMENT__TEXT_ALIGNMENT;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == IFontAttribute.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.FONT_ATTRIBUTE__FONT: return IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT;
                case IArchimatePackage.FONT_ATTRIBUTE__FONT_COLOR: return IArchimatePackage.DIAGRAM_MODEL_OBJECT__FONT_COLOR;
                default: return -1;
            }
        }
        if (baseClass == ILineObject.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.LINE_OBJECT__LINE_WIDTH: return IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_WIDTH;
                case IArchimatePackage.LINE_OBJECT__LINE_COLOR: return IArchimatePackage.DIAGRAM_MODEL_OBJECT__LINE_COLOR;
                default: return -1;
            }
        }
        if (baseClass == ITextAlignment.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.TEXT_ALIGNMENT__TEXT_ALIGNMENT: return IArchimatePackage.DIAGRAM_MODEL_OBJECT__TEXT_ALIGNMENT;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (font: "); //$NON-NLS-1$
        result.append(font);
        result.append(", fontColor: "); //$NON-NLS-1$
        result.append(fontColor);
        result.append(", lineWidth: "); //$NON-NLS-1$
        result.append(lineWidth);
        result.append(", lineColor: "); //$NON-NLS-1$
        result.append(lineColor);
        result.append(", textAlignment: "); //$NON-NLS-1$
        result.append(textAlignment);
        result.append(", fillColor: "); //$NON-NLS-1$
        result.append(fillColor);
        result.append(", alpha: "); //$NON-NLS-1$
        result.append(alpha);
        result.append(')');
        return result.toString();
    }

} //DiagramModelObject
