/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.archimatetool.canvas.model.ICanvasFactory;
import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.model.ICanvasModelBlock;
import com.archimatetool.canvas.model.ICanvasModelConnection;
import com.archimatetool.canvas.model.ICanvasModelImage;
import com.archimatetool.canvas.model.ICanvasModelSticky;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.canvas.model.INotesContent;
import com.archimatetool.model.IArchimatePackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CanvasPackage extends EPackageImpl implements ICanvasPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass canvasModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass canvasModelStickyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass canvasModelBlockEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass canvasModelImageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass canvasModelConnectionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass notesContentEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see com.archimatetool.canvas.model.ICanvasPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private CanvasPackage() {
        super(eNS_URI, ICanvasFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>This method is used to initialize {@link ICanvasPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ICanvasPackage init() {
        if (isInited) return (ICanvasPackage)EPackage.Registry.INSTANCE.getEPackage(ICanvasPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredCanvasPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        CanvasPackage theCanvasPackage = registeredCanvasPackage instanceof CanvasPackage ? (CanvasPackage)registeredCanvasPackage : new CanvasPackage();

        isInited = true;

        // Initialize simple dependencies
        IArchimatePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theCanvasPackage.createPackageContents();

        // Initialize created meta-data
        theCanvasPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theCanvasPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ICanvasPackage.eNS_URI, theCanvasPackage);
        return theCanvasPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCanvasModel() {
        return canvasModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCanvasModelSticky() {
        return canvasModelStickyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCanvasModelBlock() {
        return canvasModelBlockEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCanvasModelImage() {
        return canvasModelImageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCanvasModelConnection() {
        return canvasModelConnectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNotesContent() {
        return notesContentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getNotesContent_Notes() {
        return (EAttribute)notesContentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICanvasFactory getCanvasFactory() {
        return (ICanvasFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        notesContentEClass = createEClass(NOTES_CONTENT);
        createEAttribute(notesContentEClass, NOTES_CONTENT__NOTES);

        canvasModelEClass = createEClass(CANVAS_MODEL);

        canvasModelStickyEClass = createEClass(CANVAS_MODEL_STICKY);

        canvasModelBlockEClass = createEClass(CANVAS_MODEL_BLOCK);

        canvasModelImageEClass = createEClass(CANVAS_MODEL_IMAGE);

        canvasModelConnectionEClass = createEClass(CANVAS_MODEL_CONNECTION);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        IArchimatePackage theArchimatePackage = (IArchimatePackage)EPackage.Registry.INSTANCE.getEPackage(IArchimatePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        canvasModelEClass.getESuperTypes().add(theArchimatePackage.getDiagramModel());
        canvasModelEClass.getESuperTypes().add(theArchimatePackage.getHintProvider());
        canvasModelStickyEClass.getESuperTypes().add(theArchimatePackage.getIconic());
        canvasModelStickyEClass.getESuperTypes().add(theArchimatePackage.getTextContent());
        canvasModelStickyEClass.getESuperTypes().add(this.getNotesContent());
        canvasModelStickyEClass.getESuperTypes().add(theArchimatePackage.getProperties());
        canvasModelStickyEClass.getESuperTypes().add(theArchimatePackage.getLockable());
        canvasModelStickyEClass.getESuperTypes().add(theArchimatePackage.getBorderObject());
        canvasModelStickyEClass.getESuperTypes().add(theArchimatePackage.getTextPosition());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getIconic());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getDiagramModelContainer());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getProperties());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getLockable());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getBorderObject());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getTextContent());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getTextPosition());
        canvasModelBlockEClass.getESuperTypes().add(theArchimatePackage.getHintProvider());
        canvasModelImageEClass.getESuperTypes().add(theArchimatePackage.getDiagramModelImage());
        canvasModelImageEClass.getESuperTypes().add(theArchimatePackage.getLockable());
        canvasModelConnectionEClass.getESuperTypes().add(theArchimatePackage.getDiagramModelConnection());
        canvasModelConnectionEClass.getESuperTypes().add(theArchimatePackage.getLockable());

        // Initialize classes and features; add operations and parameters
        initEClass(notesContentEClass, INotesContent.class, "NotesContent", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getNotesContent_Notes(), ecorePackage.getEString(), "notes", "", 0, 1, INotesContent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(canvasModelEClass, ICanvasModel.class, "CanvasModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(canvasModelStickyEClass, ICanvasModelSticky.class, "CanvasModelSticky", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(canvasModelBlockEClass, ICanvasModelBlock.class, "CanvasModelBlock", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(canvasModelImageEClass, ICanvasModelImage.class, "CanvasModelImage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(canvasModelConnectionEClass, ICanvasModelConnection.class, "CanvasModelConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaDataAnnotations() {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
        addAnnotation
          (getNotesContent_Notes(),
           source,
           new String[] {
               "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });
    }

} //CanvasPackage
