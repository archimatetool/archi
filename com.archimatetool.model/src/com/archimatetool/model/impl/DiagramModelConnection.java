/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getFont <em>Font</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getFontColor <em>Font Color</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getProperties <em>Properties</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getLineWidth <em>Line Width</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getLineColor <em>Line Color</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getText <em>Text</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getTextPosition <em>Text Position</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getSource <em>Source</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getTarget <em>Target</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getBendpoints <em>Bendpoints</em>}</li>
 *   <li>{@link com.archimatetool.model.impl.DiagramModelConnection#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramModelConnection extends Connectable implements IDiagramModelConnection {
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
     * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProperties()
     * @generated
     * @ordered
     */
    protected EList<IProperty> properties;

    /**
     * The default value of the '{@link #getDocumentation() <em>Documentation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDocumentation()
     * @generated
     * @ordered
     */
    protected static final String DOCUMENTATION_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getDocumentation() <em>Documentation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDocumentation()
     * @generated
     * @ordered
     */
    protected String documentation = DOCUMENTATION_EDEFAULT;

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
     * The default value of the '{@link #getText() <em>Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getText()
     * @generated
     * @ordered
     */
    protected static final String TEXT_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getText()
     * @generated
     * @ordered
     */
    protected String text = TEXT_EDEFAULT;

    /**
     * The default value of the '{@link #getTextPosition() <em>Text Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPosition()
     * @generated NOT
     * @ordered
     */
    protected static final int TEXT_POSITION_EDEFAULT = CONNECTION_TEXT_POSITION_MIDDLE;

    /**
     * The cached value of the '{@link #getTextPosition() <em>Text Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTextPosition()
     * @generated
     * @ordered
     */
    protected int textPosition = TEXT_POSITION_EDEFAULT;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected IConnectable source;

    /**
     * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTarget()
     * @generated
     * @ordered
     */
    protected IConnectable target;

    /**
     * The cached value of the '{@link #getBendpoints() <em>Bendpoints</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBendpoints()
     * @generated
     * @ordered
     */
    protected EList<IDiagramModelBendpoint> bendpoints;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final int TYPE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected int type = TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiagramModelConnection() {
        super();
    }

    @Override
    public boolean isNameVisible() {
        return getFeatures().getBoolean(FEATURE_NAME_VISIBLE, FEATURE_NAME_VISIBLE_DEFAULT);
    }
    
    @Override
    public void setNameVisible(boolean value) {
        getFeatures().putBoolean(FEATURE_NAME_VISIBLE, value, FEATURE_NAME_VISIBLE_DEFAULT);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT, oldFont, font));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR, oldFontColor, fontColor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectContainmentEList<IProperty>(IProperty.class, this, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES);
        }
        return properties;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getDocumentation() {
        return documentation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDocumentation(String newDocumentation) {
        String oldDocumentation = documentation;
        documentation = newDocumentation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION, oldDocumentation, documentation));
    }

    /**
     * <!-- begin-user-doc -->
     * @deprecated As of version 2.1.0 the connection text is now the "name" attribute
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * <!-- begin-user-doc -->
     * @deprecated As of version 2.1.0 the connection text is now the "name" attribute
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setText(String newText) {
        String oldText = text;
        text = newText;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT, oldText, text));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IConnectable getSource() {
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSource(IConnectable newSource) {
        IConnectable oldSource = source;
        source = newSource;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE, oldSource, source));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IConnectable getTarget() {
        return target;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTarget(IConnectable newTarget) {
        IConnectable oldTarget = target;
        target = newTarget;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET, oldTarget, target));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IDiagramModelBendpoint> getBendpoints() {
        if (bendpoints == null) {
            bendpoints = new EObjectContainmentEList<IDiagramModelBendpoint>(IDiagramModelBendpoint.class, this, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS);
        }
        return bendpoints;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH, oldLineWidth, lineWidth));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR, oldLineColor, lineColor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getTextPosition() {
        return textPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTextPosition(int newTextPosition) {
        int oldTextPosition = textPosition;
        textPosition = newTextPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION, oldTextPosition, textPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setType(int newType) {
        int oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void connect(IConnectable source, IConnectable target) {
        if(source == null || target == null) {
            throw new IllegalArgumentException("Source or Target cannot be null"); //$NON-NLS-1$
        }

        // Same as before, don't bother
        if(this.source == source && this.target == target) {
            return;
        }
        
        disconnect();
        
        // TODO: Why isn't this setSource() and setTarget()? Surely that would fire the correct eNotification?
        this.source = source;
        this.target = target;  
        
        reconnect();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void disconnect() {
        if(source != null && target != null) {
            source.removeConnection(this);
            target.removeConnection(this);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public void reconnect() {
        if(source != null && target != null) {
            source.addConnection(this);
            target.addConnection(this);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES:
                return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS:
                return ((InternalEList<?>)getBendpoints()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    @Override
    public EObject getCopy() {
        IDiagramModelConnection newConnection = (IDiagramModelConnection)super.getCopy();

        newConnection.setSource(null);
        newConnection.setTarget(null);
        
        newConnection.getSourceConnections().clear();
        newConnection.getTargetConnections().clear();

        return newConnection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT:
                return getFont();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR:
                return getFontColor();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES:
                return getProperties();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION:
                return getDocumentation();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH:
                return getLineWidth();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR:
                return getLineColor();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT:
                return getText();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION:
                return getTextPosition();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE:
                return getSource();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET:
                return getTarget();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS:
                return getBendpoints();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TYPE:
                return getType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT:
                setFont((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR:
                setFontColor((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES:
                getProperties().clear();
                getProperties().addAll((Collection<? extends IProperty>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION:
                setDocumentation((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH:
                setLineWidth((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR:
                setLineColor((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT:
                setText((String)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION:
                setTextPosition((Integer)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE:
                setSource((IConnectable)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET:
                setTarget((IConnectable)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS:
                getBendpoints().clear();
                getBendpoints().addAll((Collection<? extends IDiagramModelBendpoint>)newValue);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TYPE:
                setType((Integer)newValue);
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
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT:
                setFont(FONT_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR:
                setFontColor(FONT_COLOR_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES:
                getProperties().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION:
                setDocumentation(DOCUMENTATION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH:
                setLineWidth(LINE_WIDTH_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR:
                setLineColor(LINE_COLOR_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT:
                setText(TEXT_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION:
                setTextPosition(TEXT_POSITION_EDEFAULT);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE:
                setSource((IConnectable)null);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET:
                setTarget((IConnectable)null);
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS:
                getBendpoints().clear();
                return;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TYPE:
                setType(TYPE_EDEFAULT);
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
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT:
                return FONT_EDEFAULT == null ? font != null : !FONT_EDEFAULT.equals(font);
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR:
                return FONT_COLOR_EDEFAULT == null ? fontColor != null : !FONT_COLOR_EDEFAULT.equals(fontColor);
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION:
                return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH:
                return lineWidth != LINE_WIDTH_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR:
                return LINE_COLOR_EDEFAULT == null ? lineColor != null : !LINE_COLOR_EDEFAULT.equals(lineColor);
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT:
                return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION:
                return textPosition != TEXT_POSITION_EDEFAULT;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__SOURCE:
                return source != null;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TARGET:
                return target != null;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__BENDPOINTS:
                return bendpoints != null && !bendpoints.isEmpty();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__TYPE:
                return type != TYPE_EDEFAULT;
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
                case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT: return IArchimatePackage.FONT_ATTRIBUTE__FONT;
                case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR: return IArchimatePackage.FONT_ATTRIBUTE__FONT_COLOR;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == ILineObject.class) {
            switch (derivedFeatureID) {
                case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH: return IArchimatePackage.LINE_OBJECT__LINE_WIDTH;
                case IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR: return IArchimatePackage.LINE_OBJECT__LINE_COLOR;
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
                case IArchimatePackage.FONT_ATTRIBUTE__FONT: return IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT;
                case IArchimatePackage.FONT_ATTRIBUTE__FONT_COLOR: return IArchimatePackage.DIAGRAM_MODEL_CONNECTION__FONT_COLOR;
                default: return -1;
            }
        }
        if (baseClass == IProperties.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.DIAGRAM_MODEL_CONNECTION__PROPERTIES;
                default: return -1;
            }
        }
        if (baseClass == IDocumentable.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.DIAGRAM_MODEL_CONNECTION__DOCUMENTATION;
                default: return -1;
            }
        }
        if (baseClass == ILineObject.class) {
            switch (baseFeatureID) {
                case IArchimatePackage.LINE_OBJECT__LINE_WIDTH: return IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_WIDTH;
                case IArchimatePackage.LINE_OBJECT__LINE_COLOR: return IArchimatePackage.DIAGRAM_MODEL_CONNECTION__LINE_COLOR;
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
        result.append(", documentation: "); //$NON-NLS-1$
        result.append(documentation);
        result.append(", lineWidth: "); //$NON-NLS-1$
        result.append(lineWidth);
        result.append(", lineColor: "); //$NON-NLS-1$
        result.append(lineColor);
        result.append(", text: "); //$NON-NLS-1$
        result.append(text);
        result.append(", textPosition: "); //$NON-NLS-1$
        result.append(textPosition);
        result.append(", type: "); //$NON-NLS-1$
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //DiagramModelConnection
