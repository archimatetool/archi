/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.archimatetool.canvas.model.ICanvasPackage
 * @generated
 */
public interface ICanvasFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ICanvasFactory eINSTANCE = com.archimatetool.canvas.model.impl.CanvasFactory.init();

    /**
     * Returns a new object of class '<em>Model</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model</em>'.
     * @generated
     */
    ICanvasModel createCanvasModel();

    /**
     * Returns a new object of class '<em>Model Sticky</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Sticky</em>'.
     * @generated
     */
    ICanvasModelSticky createCanvasModelSticky();

    /**
     * Returns a new object of class '<em>Model Block</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Block</em>'.
     * @generated
     */
    ICanvasModelBlock createCanvasModelBlock();

    /**
     * Returns a new object of class '<em>Model Image</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Image</em>'.
     * @generated
     */
    ICanvasModelImage createCanvasModelImage();

    /**
     * Returns a new object of class '<em>Model Connection</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Connection</em>'.
     * @generated
     */
    ICanvasModelConnection createCanvasModelConnection();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ICanvasPackage getCanvasPackage();

} //ICanvasFactory
