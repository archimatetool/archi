/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.canvas.model.*;
import com.archimatetool.editor.ui.IHelpHintProvider;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextContent;
import com.archimatetool.model.ITextPosition;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.archimatetool.canvas.model.ICanvasPackage
 * @generated
 */
public class CanvasAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ICanvasPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CanvasAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = ICanvasPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CanvasSwitch<Adapter> modelSwitch =
        new CanvasSwitch<Adapter>() {
            @Override
            public Adapter caseHintProvider(IHintProvider object) {
                return createHintProviderAdapter();
            }
            @Override
            public Adapter caseHelpHintProvider(IHelpHintProvider object) {
                return createHelpHintProviderAdapter();
            }
            @Override
            public Adapter caseNotesContent(INotesContent object) {
                return createNotesContentAdapter();
            }
            @Override
            public Adapter caseCanvasModel(ICanvasModel object) {
                return createCanvasModelAdapter();
            }
            @Override
            public Adapter caseCanvasModelSticky(ICanvasModelSticky object) {
                return createCanvasModelStickyAdapter();
            }
            @Override
            public Adapter caseCanvasModelBlock(ICanvasModelBlock object) {
                return createCanvasModelBlockAdapter();
            }
            @Override
            public Adapter caseCanvasModelImage(ICanvasModelImage object) {
                return createCanvasModelImageAdapter();
            }
            @Override
            public Adapter caseCanvasModelConnection(ICanvasModelConnection object) {
                return createCanvasModelConnectionAdapter();
            }
            @Override
            public Adapter caseAdapter(IAdapter object) {
                return createAdapterAdapter();
            }
            @Override
            public Adapter caseNameable(INameable object) {
                return createNameableAdapter();
            }
            @Override
            public Adapter caseIdentifier(IIdentifier object) {
                return createIdentifierAdapter();
            }
            @Override
            public Adapter caseFeatures(IFeatures object) {
                return createFeaturesAdapter();
            }
            @Override
            public Adapter caseArchimateModelObject(IArchimateModelObject object) {
                return createArchimateModelObjectAdapter();
            }
            @Override
            public Adapter caseCloneable(ICloneable object) {
                return createCloneableAdapter();
            }
            @Override
            public Adapter caseDiagramModelComponent(IDiagramModelComponent object) {
                return createDiagramModelComponentAdapter();
            }
            @Override
            public Adapter caseDiagramModelContainer(IDiagramModelContainer object) {
                return createDiagramModelContainerAdapter();
            }
            @Override
            public Adapter caseDocumentable(IDocumentable object) {
                return createDocumentableAdapter();
            }
            @Override
            public Adapter caseProperties(IProperties object) {
                return createPropertiesAdapter();
            }
            @Override
            public Adapter caseDiagramModel(IDiagramModel object) {
                return createDiagramModelAdapter();
            }
            @Override
            public Adapter caseConnectable(IConnectable object) {
                return createConnectableAdapter();
            }
            @Override
            public Adapter caseFontAttribute(IFontAttribute object) {
                return createFontAttributeAdapter();
            }
            @Override
            public Adapter caseLineObject(ILineObject object) {
                return createLineObjectAdapter();
            }
            @Override
            public Adapter caseTextAlignment(ITextAlignment object) {
                return createTextAlignmentAdapter();
            }
            @Override
            public Adapter caseDiagramModelObject(IDiagramModelObject object) {
                return createDiagramModelObjectAdapter();
            }
            @Override
            public Adapter caseDiagramModelImageProvider(IDiagramModelImageProvider object) {
                return createDiagramModelImageProviderAdapter();
            }
            @Override
            public Adapter caseIconic(IIconic object) {
                return createIconicAdapter();
            }
            @Override
            public Adapter caseTextContent(ITextContent object) {
                return createTextContentAdapter();
            }
            @Override
            public Adapter caseLockable(ILockable object) {
                return createLockableAdapter();
            }
            @Override
            public Adapter caseBorderObject(IBorderObject object) {
                return createBorderObjectAdapter();
            }
            @Override
            public Adapter caseTextPosition(ITextPosition object) {
                return createTextPositionAdapter();
            }
            @Override
            public Adapter caseDiagramModelImage(IDiagramModelImage object) {
                return createDiagramModelImageAdapter();
            }
            @Override
            public Adapter caseDiagramModelConnection(IDiagramModelConnection object) {
                return createDiagramModelConnectionAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.ICanvasModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.ICanvasModel
     * @generated
     */
    public Adapter createCanvasModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.ICanvasModelSticky <em>Model Sticky</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.ICanvasModelSticky
     * @generated
     */
    public Adapter createCanvasModelStickyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.ICanvasModelBlock <em>Model Block</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.ICanvasModelBlock
     * @generated
     */
    public Adapter createCanvasModelBlockAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.ICanvasModelImage <em>Model Image</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.ICanvasModelImage
     * @generated
     */
    public Adapter createCanvasModelImageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.ICanvasModelConnection <em>Model Connection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.ICanvasModelConnection
     * @generated
     */
    public Adapter createCanvasModelConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelImageProvider <em>Diagram Model Image Provider</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelImageProvider
     * @generated
     */
    public Adapter createDiagramModelImageProviderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArchimateModelObject <em>Model Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArchimateModelObject
     * @generated
     */
    public Adapter createArchimateModelObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.IHintProvider <em>Hint Provider</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.IHintProvider
     * @generated
     */
    public Adapter createHintProviderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.editor.ui.IHelpHintProvider <em>Help Hint Provider</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.editor.ui.IHelpHintProvider
     * @generated
     */
    public Adapter createHelpHintProviderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.canvas.model.INotesContent <em>Notes Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.canvas.model.INotesContent
     * @generated
     */
    public Adapter createNotesContentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IAdapter <em>Adapter</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IAdapter
     * @generated
     */
    public Adapter createAdapterAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IIdentifier
     * @generated
     */
    public Adapter createIdentifierAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ICloneable <em>Cloneable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ICloneable
     * @generated
     */
    public Adapter createCloneableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.INameable <em>Nameable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.INameable
     * @generated
     */
    public Adapter createNameableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFeatures <em>Features</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFeatures
     * @generated
     */
    public Adapter createFeaturesAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelComponent <em>Diagram Model Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelComponent
     * @generated
     */
    public Adapter createDiagramModelComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IConnectable <em>Connectable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IConnectable
     * @generated
     */
    public Adapter createConnectableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelContainer <em>Diagram Model Container</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelContainer
     * @generated
     */
    public Adapter createDiagramModelContainerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDocumentable <em>Documentable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDocumentable
     * @generated
     */
    public Adapter createDocumentableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IProperties
     * @generated
     */
    public Adapter createPropertiesAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModel
     * @generated
     */
    public Adapter createDiagramModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFontAttribute <em>Font Attribute</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFontAttribute
     * @generated
     */
    public Adapter createFontAttributeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ILineObject <em>Line Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ILineObject
     * @generated
     */
    public Adapter createLineObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITextAlignment <em>Text Alignment</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITextAlignment
     * @generated
     */
    public Adapter createTextAlignmentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelObject <em>Diagram Model Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelObject
     * @generated
     */
    public Adapter createDiagramModelObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITextContent <em>Text Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITextContent
     * @generated
     */
    public Adapter createTextContentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ILockable <em>Lockable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ILockable
     * @generated
     */
    public Adapter createLockableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBorderObject <em>Border Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBorderObject
     * @generated
     */
    public Adapter createBorderObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITextPosition <em>Text Position</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITextPosition
     * @generated
     */
    public Adapter createTextPositionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelImage <em>Diagram Model Image</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelImage
     * @generated
     */
    public Adapter createDiagramModelImageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelConnection <em>Diagram Model Connection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelConnection
     * @generated
     */
    public Adapter createDiagramModelConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IIconic <em>Iconic</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IIconic
     * @generated
     */
    public Adapter createIconicAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //CanvasAdapterFactory
