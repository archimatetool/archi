/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelConnection#getText <em>Text</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelConnection#getSource <em>Source</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelConnection#getTarget <em>Target</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelConnection#getBendpoints <em>Bendpoints</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelConnection#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelConnection()
 * @model
 * @generated
 */
public interface IDiagramModelConnection extends IDiagramModelComponent, IFontAttribute, IProperties, IDocumentable, ILineObject {
    
    /**
     * Text Position at source
     */
    int CONNECTION_TEXT_POSITION_SOURCE = 0;

    /**
     * Text Position at middle
     */
    int CONNECTION_TEXT_POSITION_MIDDLE = 1;
    
    /**
     * Text Position at target
     */
    int CONNECTION_TEXT_POSITION_TARGET = 2;
    
    /**
     * Line Styles
     */
    // Since Archi version 1.7
    int LINE_SOLID = 0;             // default
    int ARROW_FILL_TARGET = 1;      // 1 << 0
    int LINE_DASHED = 2;            // 1 << 1
    int LINE_DOTTED = 4;            // 1 << 2
    
    // Since Archi version 2.1
    int ARROW_NONE = 0;
    int ARROW_FILL_SOURCE = 8;        // 1 << 3
    int ARROW_HOLLOW_TARGET = 16;     // 1 << 4
    int ARROW_HOLLOW_SOURCE = 32;     // 1 << 5
    int ARROW_LINE_TARGET = 64;       // 1 << 6
    int ARROW_LINE_SOURCE = 128;      // 1 << 7

    
    /**
     * Returns the value of the '<em><b>Text</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * @deprecated As of version 2.1.0 the connection text is now the "name" attribute
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text</em>' attribute.
     * @see #setText(String)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelConnection_Text()
     * @model default="" transient="true"
     * @generated
     */
    String getText();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelConnection#getText <em>Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * @deprecated As of version 2.1.0 the connection text is now the "name" attribute
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text</em>' attribute.
     * @see #getText()
     * @generated
     */
    void setText(String value);

    /**
     * Returns the value of the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference.
     * @see #setSource(IDiagramModelObject)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelConnection_Source()
     * @model resolveProxies="false"
     * @generated
     */
    IDiagramModelObject getSource();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelConnection#getSource <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * Don't call this directly. Use connect() instead.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' reference.
     * @see #getSource()
     * @generated
     */
    void setSource(IDiagramModelObject value);

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(IDiagramModelObject)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelConnection_Target()
     * @model resolveProxies="false"
     * @generated
     */
    IDiagramModelObject getTarget();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelConnection#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * Don't call this directly. Use connect() instead.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(IDiagramModelObject value);

    /**
     * Returns the value of the '<em><b>Bendpoints</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IDiagramModelBendpoint}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bendpoints</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bendpoints</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelConnection_Bendpoints()
     * @model containment="true"
     *        extendedMetaData="name='bendpoint' kind='element'"
     * @generated
     */
    EList<IDiagramModelBendpoint> getBendpoints();

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(int)
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelConnection_Type()
     * @model
     * @generated
     */
    int getType();

    /**
     * Sets the value of the '{@link com.archimatetool.model.IDiagramModelConnection#getType <em>Type</em>}' attribute.
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
    void disconnect();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void reconnect();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void connect(IDiagramModelObject source, IDiagramModelObject target);
} // IDiagramModelConnection
