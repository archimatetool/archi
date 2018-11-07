/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.archimatetool.canvas.model.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CanvasFactory extends EFactoryImpl implements ICanvasFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ICanvasFactory init() {
        try {
            ICanvasFactory theCanvasFactory = (ICanvasFactory)EPackage.Registry.INSTANCE.getEFactory(ICanvasPackage.eNS_URI);
            if (theCanvasFactory != null) {
                return theCanvasFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new CanvasFactory();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CanvasFactory() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case ICanvasPackage.CANVAS_MODEL: return createCanvasModel();
            case ICanvasPackage.CANVAS_MODEL_STICKY: return createCanvasModelSticky();
            case ICanvasPackage.CANVAS_MODEL_BLOCK: return createCanvasModelBlock();
            case ICanvasPackage.CANVAS_MODEL_IMAGE: return createCanvasModelImage();
            case ICanvasPackage.CANVAS_MODEL_CONNECTION: return createCanvasModelConnection();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasModel createCanvasModel() {
        CanvasModel canvasModel = new CanvasModel();
        return canvasModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasModelSticky createCanvasModelSticky() {
        CanvasModelSticky canvasModelSticky = new CanvasModelSticky();
        return canvasModelSticky;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasModelBlock createCanvasModelBlock() {
        CanvasModelBlock canvasModelBlock = new CanvasModelBlock();
        return canvasModelBlock;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasModelImage createCanvasModelImage() {
        CanvasModelImage canvasModelImage = new CanvasModelImage();
        return canvasModelImage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasModelConnection createCanvasModelConnection() {
        CanvasModelConnection canvasModelConnection = new CanvasModelConnection();
        return canvasModelConnection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasPackage getCanvasPackage() {
        return (ICanvasPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ICanvasPackage getPackage() {
        return ICanvasPackage.eINSTANCE;
    }

} //CanvasFactory
