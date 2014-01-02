/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getBounds <em>Bounds</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getSourceConnections <em>Source Connections</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getTargetConnections <em>Target Connections</em>}</li>
 *   <li>{@link com.archimatetool.model.IDiagramModelObject#getFillColor <em>Fill Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject()
 * @model abstract="true"
 * @generated
 */
public interface IDiagramModelObject extends IDiagramModelComponent, IFontAttribute, ILineObject {
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
     * Returns the value of the '<em><b>Source Connections</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IDiagramModelConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Connections</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Connections</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject_SourceConnections()
     * @model containment="true"
     *        extendedMetaData="name='sourceConnection' kind='element'"
     * @generated
     */
    EList<IDiagramModelConnection> getSourceConnections();

    /**
     * Returns the value of the '<em><b>Target Connections</b></em>' reference list.
     * The list contents are of type {@link com.archimatetool.model.IDiagramModelConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Connections</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Connections</em>' reference list.
     * @see com.archimatetool.model.IArchimatePackage#getDiagramModelObject_TargetConnections()
     * @model resolveProxies="false"
     * @generated
     */
    EList<IDiagramModelConnection> getTargetConnections();

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void addConnection(IDiagramModelConnection connection);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void removeConnection(IDiagramModelConnection connection);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void setBounds(int x, int y, int width, int height);

} // IDiagramModelObject
