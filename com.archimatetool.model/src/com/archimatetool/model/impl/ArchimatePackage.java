/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IAndJunction;
import com.archimatetool.model.IApplicationCollaboration;
import com.archimatetool.model.IApplicationComponent;
import com.archimatetool.model.IApplicationFunction;
import com.archimatetool.model.IApplicationInteraction;
import com.archimatetool.model.IApplicationInterface;
import com.archimatetool.model.IApplicationLayerElement;
import com.archimatetool.model.IApplicationService;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArtifact;
import com.archimatetool.model.IAssessment;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IBusinessActivity;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessCollaboration;
import com.archimatetool.model.IBusinessEvent;
import com.archimatetool.model.IBusinessFunction;
import com.archimatetool.model.IBusinessInteraction;
import com.archimatetool.model.IBusinessInterface;
import com.archimatetool.model.IBusinessLayerElement;
import com.archimatetool.model.IBusinessObject;
import com.archimatetool.model.IBusinessProcess;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IBusinessService;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.ICommunicationPath;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IConstraint;
import com.archimatetool.model.IContract;
import com.archimatetool.model.IDataObject;
import com.archimatetool.model.IDeliverable;
import com.archimatetool.model.IDevice;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IDriver;
import com.archimatetool.model.IFlowRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IGap;
import com.archimatetool.model.IGoal;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IImplementationMigrationElement;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IInfrastructureFunction;
import com.archimatetool.model.IInfrastructureInterface;
import com.archimatetool.model.IInfrastructureService;
import com.archimatetool.model.IInterfaceElement;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.IJunctionElement;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILocation;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.IMeaning;
import com.archimatetool.model.IMetadata;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.INameable;
import com.archimatetool.model.INetwork;
import com.archimatetool.model.INode;
import com.archimatetool.model.IOrJunction;
import com.archimatetool.model.IPlateau;
import com.archimatetool.model.IPrinciple;
import com.archimatetool.model.IProduct;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRealisationRelationship;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.IRepresentation;
import com.archimatetool.model.IRequirement;
import com.archimatetool.model.IServiceElement;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;
import com.archimatetool.model.ISpecialisationRelationship;
import com.archimatetool.model.IStakeholder;
import com.archimatetool.model.ISystemSoftware;
import com.archimatetool.model.ITechnologyLayerElement;
import com.archimatetool.model.ITextContent;
import com.archimatetool.model.ITriggeringRelationship;
import com.archimatetool.model.IUsedByRelationship;
import com.archimatetool.model.IValue;
import com.archimatetool.model.IWorkPackage;
import java.io.File;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ArchimatePackage extends EPackageImpl implements IArchimatePackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass adapterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass identifierEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertiesEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nameableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass textContentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass documentableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cloneableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass folderContainerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass archimateModelElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass archimateModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass folderEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass archimateElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessActivityEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessLayerElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationLayerElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyLayerElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass artifactEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass communicationPathEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass networkEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass infrastructureInterfaceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass infrastructureServiceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass infrastructureFunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass systemSoftwareEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass deviceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass motivationElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stakeholderEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass driverEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assessmentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass goalEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass constraintEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass principleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass implementationMigrationElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass workPackageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass deliverableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass plateauEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gapEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass archimateDiagramModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelComponentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelArchimateObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelContainerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelGroupEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelNoteEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelImageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelConnectionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelArchimateConnectionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelBendpointEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lineObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fontAttributeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass borderObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass diagramModelImageProviderEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boundsEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lockableEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sketchModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sketchModelStickyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sketchModelActorEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum folderTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType fileEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessActorEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessCollaborationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass contractEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessEventEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessFunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessInteractionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessInterfaceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass meaningEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessProcessEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass productEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass representationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessRoleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessServiceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass locationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationComponentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationFunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationInteractionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationInterfaceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationServiceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationCollaborationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass relationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass accessRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass aggregationRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assignmentRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass associationRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositionRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass flowRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass realisationRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specialisationRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass triggeringRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass usedByRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass influenceRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass junctionElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interfaceElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass serviceElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass junctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass andJunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass orJunctionEClass = null;

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
     * @see com.archimatetool.model.IArchimatePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ArchimatePackage() {
        super(eNS_URI, IArchimateFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link IArchimatePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static IArchimatePackage init() {
        if (isInited) return (IArchimatePackage)EPackage.Registry.INSTANCE.getEPackage(IArchimatePackage.eNS_URI);

        // Obtain or create and register package
        ArchimatePackage theArchimatePackage = (ArchimatePackage)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ArchimatePackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ArchimatePackage());

        isInited = true;

        // Create package meta-data objects
        theArchimatePackage.createPackageContents();

        // Initialize created meta-data
        theArchimatePackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theArchimatePackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(IArchimatePackage.eNS_URI, theArchimatePackage);
        return theArchimatePackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdapter() {
        return adapterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIdentifier() {
        return identifierEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentifier_Id() {
        return (EAttribute)identifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProperties() {
        return propertiesEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProperties_Properties() {
        return (EReference)propertiesEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetadata() {
        return metadataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMetadata_Entries() {
        return (EReference)metadataEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNameable() {
        return nameableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNameable_Name() {
        return (EAttribute)nameableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTextContent() {
        return textContentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTextContent_Content() {
        return (EAttribute)textContentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentable() {
        return documentableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentable_Documentation() {
        return (EAttribute)documentableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCloneable() {
        return cloneableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFolderContainer() {
        return folderContainerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFolderContainer_Folders() {
        return (EReference)folderContainerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArchimateModelElement() {
        return archimateModelElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArchimateModelElement_ArchimateModel() {
        return (EReference)archimateModelElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProperty() {
        return propertyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProperty_Key() {
        return (EAttribute)propertyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProperty_Value() {
        return (EAttribute)propertyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArchimateModel() {
        return archimateModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArchimateModel_Purpose() {
        return (EAttribute)archimateModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArchimateModel_File() {
        return (EAttribute)archimateModelEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArchimateModel_Version() {
        return (EAttribute)archimateModelEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArchimateModel_Metadata() {
        return (EReference)archimateModelEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFolder() {
        return folderEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFolder_Elements() {
        return (EReference)folderEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFolder_Type() {
        return (EAttribute)folderEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArchimateElement() {
        return archimateElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessActivity() {
        return businessActivityEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessLayerElement() {
        return businessLayerElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationLayerElement() {
        return applicationLayerElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTechnologyLayerElement() {
        return technologyLayerElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArtifact() {
        return artifactEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCommunicationPath() {
        return communicationPathEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNetwork() {
        return networkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInfrastructureInterface() {
        return infrastructureInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInfrastructureService() {
        return infrastructureServiceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInfrastructureFunction() {
        return infrastructureFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNode() {
        return nodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSystemSoftware() {
        return systemSoftwareEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDevice() {
        return deviceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMotivationElement() {
        return motivationElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStakeholder() {
        return stakeholderEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDriver() {
        return driverEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAssessment() {
        return assessmentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGoal() {
        return goalEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequirement() {
        return requirementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConstraint() {
        return constraintEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPrinciple() {
        return principleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getImplementationMigrationElement() {
        return implementationMigrationElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWorkPackage() {
        return workPackageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDeliverable() {
        return deliverableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPlateau() {
        return plateauEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGap() {
        return gapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModel() {
        return diagramModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModel_ConnectionRouterType() {
        return (EAttribute)diagramModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArchimateDiagramModel() {
        return archimateDiagramModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArchimateDiagramModel_Viewpoint() {
        return (EAttribute)archimateDiagramModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelReference() {
        return diagramModelReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelReference_ReferencedModel() {
        return (EReference)diagramModelReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelComponent() {
        return diagramModelComponentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelComponent_DiagramModel() {
        return (EReference)diagramModelComponentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelObject() {
        return diagramModelObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelObject_Bounds() {
        return (EReference)diagramModelObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelObject_SourceConnections() {
        return (EReference)diagramModelObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelObject_TargetConnections() {
        return (EReference)diagramModelObjectEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelObject_FillColor() {
        return (EAttribute)diagramModelObjectEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelArchimateObject() {
        return diagramModelArchimateObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelArchimateObject_ArchimateElement() {
        return (EReference)diagramModelArchimateObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelArchimateObject_Type() {
        return (EAttribute)diagramModelArchimateObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelContainer() {
        return diagramModelContainerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelContainer_Children() {
        return (EReference)diagramModelContainerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelGroup() {
        return diagramModelGroupEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelNote() {
        return diagramModelNoteEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelImage() {
        return diagramModelImageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelConnection() {
        return diagramModelConnectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelConnection_Text() {
        return (EAttribute)diagramModelConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelConnection_Source() {
        return (EReference)diagramModelConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelConnection_Target() {
        return (EReference)diagramModelConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelConnection_Bendpoints() {
        return (EReference)diagramModelConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelConnection_Type() {
        return (EAttribute)diagramModelConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelArchimateConnection() {
        return diagramModelArchimateConnectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDiagramModelArchimateConnection_Relationship() {
        return (EReference)diagramModelArchimateConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelBendpoint() {
        return diagramModelBendpointEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelBendpoint_StartX() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelBendpoint_StartY() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelBendpoint_EndX() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelBendpoint_EndY() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLineObject() {
        return lineObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineObject_LineWidth() {
        return (EAttribute)lineObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineObject_LineColor() {
        return (EAttribute)lineObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFontAttribute() {
        return fontAttributeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFontAttribute_Font() {
        return (EAttribute)fontAttributeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFontAttribute_FontColor() {
        return (EAttribute)fontAttributeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFontAttribute_TextAlignment() {
        return (EAttribute)fontAttributeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFontAttribute_TextPosition() {
        return (EAttribute)fontAttributeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBorderObject() {
        return borderObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBorderObject_BorderColor() {
        return (EAttribute)borderObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDiagramModelImageProvider() {
        return diagramModelImageProviderEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDiagramModelImageProvider_ImagePath() {
        return (EAttribute)diagramModelImageProviderEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBounds() {
        return boundsEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBounds_X() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBounds_Y() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBounds_Width() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBounds_Height() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLockable() {
        return lockableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLockable_Locked() {
        return (EAttribute)lockableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSketchModel() {
        return sketchModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSketchModel_Background() {
        return (EAttribute)sketchModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSketchModelSticky() {
        return sketchModelStickyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSketchModelActor() {
        return sketchModelActorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFolderType() {
        return folderTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFile() {
        return fileEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessActor() {
        return businessActorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessCollaboration() {
        return businessCollaborationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getContract() {
        return contractEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessEvent() {
        return businessEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessFunction() {
        return businessFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessInteraction() {
        return businessInteractionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessInterface() {
        return businessInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMeaning() {
        return meaningEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessObject() {
        return businessObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessProcess() {
        return businessProcessEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProduct() {
        return productEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRepresentation() {
        return representationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessRole() {
        return businessRoleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBusinessService() {
        return businessServiceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValue() {
        return valueEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLocation() {
        return locationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationComponent() {
        return applicationComponentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationFunction() {
        return applicationFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationInteraction() {
        return applicationInteractionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationInterface() {
        return applicationInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDataObject() {
        return dataObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationService() {
        return applicationServiceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getApplicationCollaboration() {
        return applicationCollaborationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRelationship() {
        return relationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRelationship_Source() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRelationship_Target() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAccessRelationship() {
        return accessRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAccessRelationship_AccessType() {
        return (EAttribute)accessRelationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAggregationRelationship() {
        return aggregationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAssignmentRelationship() {
        return assignmentRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAssociationRelationship() {
        return associationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositionRelationship() {
        return compositionRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFlowRelationship() {
        return flowRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRealisationRelationship() {
        return realisationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpecialisationRelationship() {
        return specialisationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTriggeringRelationship() {
        return triggeringRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUsedByRelationship() {
        return usedByRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInfluenceRelationship() {
        return influenceRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getJunctionElement() {
        return junctionElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInterfaceElement() {
        return interfaceElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInterfaceElement_InterfaceType() {
        return (EAttribute)interfaceElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getServiceElement() {
        return serviceElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getJunction() {
        return junctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAndJunction() {
        return andJunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOrJunction() {
        return orJunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IArchimateFactory getArchimateFactory() {
        return (IArchimateFactory)getEFactoryInstance();
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
        adapterEClass = createEClass(ADAPTER);

        identifierEClass = createEClass(IDENTIFIER);
        createEAttribute(identifierEClass, IDENTIFIER__ID);

        propertyEClass = createEClass(PROPERTY);
        createEAttribute(propertyEClass, PROPERTY__KEY);
        createEAttribute(propertyEClass, PROPERTY__VALUE);

        propertiesEClass = createEClass(PROPERTIES);
        createEReference(propertiesEClass, PROPERTIES__PROPERTIES);

        metadataEClass = createEClass(METADATA);
        createEReference(metadataEClass, METADATA__ENTRIES);

        nameableEClass = createEClass(NAMEABLE);
        createEAttribute(nameableEClass, NAMEABLE__NAME);

        textContentEClass = createEClass(TEXT_CONTENT);
        createEAttribute(textContentEClass, TEXT_CONTENT__CONTENT);

        documentableEClass = createEClass(DOCUMENTABLE);
        createEAttribute(documentableEClass, DOCUMENTABLE__DOCUMENTATION);

        cloneableEClass = createEClass(CLONEABLE);

        folderContainerEClass = createEClass(FOLDER_CONTAINER);
        createEReference(folderContainerEClass, FOLDER_CONTAINER__FOLDERS);

        archimateModelEClass = createEClass(ARCHIMATE_MODEL);
        createEAttribute(archimateModelEClass, ARCHIMATE_MODEL__PURPOSE);
        createEAttribute(archimateModelEClass, ARCHIMATE_MODEL__FILE);
        createEAttribute(archimateModelEClass, ARCHIMATE_MODEL__VERSION);
        createEReference(archimateModelEClass, ARCHIMATE_MODEL__METADATA);

        archimateModelElementEClass = createEClass(ARCHIMATE_MODEL_ELEMENT);
        createEReference(archimateModelElementEClass, ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL);

        folderEClass = createEClass(FOLDER);
        createEReference(folderEClass, FOLDER__ELEMENTS);
        createEAttribute(folderEClass, FOLDER__TYPE);

        archimateElementEClass = createEClass(ARCHIMATE_ELEMENT);

        junctionElementEClass = createEClass(JUNCTION_ELEMENT);

        interfaceElementEClass = createEClass(INTERFACE_ELEMENT);
        createEAttribute(interfaceElementEClass, INTERFACE_ELEMENT__INTERFACE_TYPE);

        serviceElementEClass = createEClass(SERVICE_ELEMENT);

        junctionEClass = createEClass(JUNCTION);

        andJunctionEClass = createEClass(AND_JUNCTION);

        orJunctionEClass = createEClass(OR_JUNCTION);

        relationshipEClass = createEClass(RELATIONSHIP);
        createEReference(relationshipEClass, RELATIONSHIP__SOURCE);
        createEReference(relationshipEClass, RELATIONSHIP__TARGET);

        accessRelationshipEClass = createEClass(ACCESS_RELATIONSHIP);
        createEAttribute(accessRelationshipEClass, ACCESS_RELATIONSHIP__ACCESS_TYPE);

        aggregationRelationshipEClass = createEClass(AGGREGATION_RELATIONSHIP);

        assignmentRelationshipEClass = createEClass(ASSIGNMENT_RELATIONSHIP);

        associationRelationshipEClass = createEClass(ASSOCIATION_RELATIONSHIP);

        compositionRelationshipEClass = createEClass(COMPOSITION_RELATIONSHIP);

        flowRelationshipEClass = createEClass(FLOW_RELATIONSHIP);

        realisationRelationshipEClass = createEClass(REALISATION_RELATIONSHIP);

        specialisationRelationshipEClass = createEClass(SPECIALISATION_RELATIONSHIP);

        triggeringRelationshipEClass = createEClass(TRIGGERING_RELATIONSHIP);

        usedByRelationshipEClass = createEClass(USED_BY_RELATIONSHIP);

        influenceRelationshipEClass = createEClass(INFLUENCE_RELATIONSHIP);

        businessLayerElementEClass = createEClass(BUSINESS_LAYER_ELEMENT);

        businessActivityEClass = createEClass(BUSINESS_ACTIVITY);

        businessActorEClass = createEClass(BUSINESS_ACTOR);

        businessCollaborationEClass = createEClass(BUSINESS_COLLABORATION);

        contractEClass = createEClass(CONTRACT);

        businessEventEClass = createEClass(BUSINESS_EVENT);

        businessFunctionEClass = createEClass(BUSINESS_FUNCTION);

        businessInteractionEClass = createEClass(BUSINESS_INTERACTION);

        businessInterfaceEClass = createEClass(BUSINESS_INTERFACE);

        meaningEClass = createEClass(MEANING);

        businessObjectEClass = createEClass(BUSINESS_OBJECT);

        businessProcessEClass = createEClass(BUSINESS_PROCESS);

        productEClass = createEClass(PRODUCT);

        representationEClass = createEClass(REPRESENTATION);

        businessRoleEClass = createEClass(BUSINESS_ROLE);

        businessServiceEClass = createEClass(BUSINESS_SERVICE);

        valueEClass = createEClass(VALUE);

        locationEClass = createEClass(LOCATION);

        applicationLayerElementEClass = createEClass(APPLICATION_LAYER_ELEMENT);

        applicationCollaborationEClass = createEClass(APPLICATION_COLLABORATION);

        applicationComponentEClass = createEClass(APPLICATION_COMPONENT);

        applicationFunctionEClass = createEClass(APPLICATION_FUNCTION);

        applicationInteractionEClass = createEClass(APPLICATION_INTERACTION);

        applicationInterfaceEClass = createEClass(APPLICATION_INTERFACE);

        dataObjectEClass = createEClass(DATA_OBJECT);

        applicationServiceEClass = createEClass(APPLICATION_SERVICE);

        technologyLayerElementEClass = createEClass(TECHNOLOGY_LAYER_ELEMENT);

        artifactEClass = createEClass(ARTIFACT);

        communicationPathEClass = createEClass(COMMUNICATION_PATH);

        networkEClass = createEClass(NETWORK);

        infrastructureInterfaceEClass = createEClass(INFRASTRUCTURE_INTERFACE);

        infrastructureServiceEClass = createEClass(INFRASTRUCTURE_SERVICE);

        infrastructureFunctionEClass = createEClass(INFRASTRUCTURE_FUNCTION);

        nodeEClass = createEClass(NODE);

        systemSoftwareEClass = createEClass(SYSTEM_SOFTWARE);

        deviceEClass = createEClass(DEVICE);

        motivationElementEClass = createEClass(MOTIVATION_ELEMENT);

        stakeholderEClass = createEClass(STAKEHOLDER);

        driverEClass = createEClass(DRIVER);

        assessmentEClass = createEClass(ASSESSMENT);

        goalEClass = createEClass(GOAL);

        requirementEClass = createEClass(REQUIREMENT);

        constraintEClass = createEClass(CONSTRAINT);

        principleEClass = createEClass(PRINCIPLE);

        implementationMigrationElementEClass = createEClass(IMPLEMENTATION_MIGRATION_ELEMENT);

        workPackageEClass = createEClass(WORK_PACKAGE);

        deliverableEClass = createEClass(DELIVERABLE);

        plateauEClass = createEClass(PLATEAU);

        gapEClass = createEClass(GAP);

        diagramModelComponentEClass = createEClass(DIAGRAM_MODEL_COMPONENT);
        createEReference(diagramModelComponentEClass, DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL);

        diagramModelContainerEClass = createEClass(DIAGRAM_MODEL_CONTAINER);
        createEReference(diagramModelContainerEClass, DIAGRAM_MODEL_CONTAINER__CHILDREN);

        diagramModelEClass = createEClass(DIAGRAM_MODEL);
        createEAttribute(diagramModelEClass, DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE);

        diagramModelReferenceEClass = createEClass(DIAGRAM_MODEL_REFERENCE);
        createEReference(diagramModelReferenceEClass, DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL);

        diagramModelObjectEClass = createEClass(DIAGRAM_MODEL_OBJECT);
        createEReference(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__BOUNDS);
        createEReference(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS);
        createEReference(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS);
        createEAttribute(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__FILL_COLOR);

        diagramModelGroupEClass = createEClass(DIAGRAM_MODEL_GROUP);

        diagramModelNoteEClass = createEClass(DIAGRAM_MODEL_NOTE);

        diagramModelImageEClass = createEClass(DIAGRAM_MODEL_IMAGE);

        diagramModelConnectionEClass = createEClass(DIAGRAM_MODEL_CONNECTION);
        createEAttribute(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__TEXT);
        createEReference(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__SOURCE);
        createEReference(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__TARGET);
        createEReference(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__BENDPOINTS);
        createEAttribute(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__TYPE);

        diagramModelBendpointEClass = createEClass(DIAGRAM_MODEL_BENDPOINT);
        createEAttribute(diagramModelBendpointEClass, DIAGRAM_MODEL_BENDPOINT__START_X);
        createEAttribute(diagramModelBendpointEClass, DIAGRAM_MODEL_BENDPOINT__START_Y);
        createEAttribute(diagramModelBendpointEClass, DIAGRAM_MODEL_BENDPOINT__END_X);
        createEAttribute(diagramModelBendpointEClass, DIAGRAM_MODEL_BENDPOINT__END_Y);

        lineObjectEClass = createEClass(LINE_OBJECT);
        createEAttribute(lineObjectEClass, LINE_OBJECT__LINE_WIDTH);
        createEAttribute(lineObjectEClass, LINE_OBJECT__LINE_COLOR);

        fontAttributeEClass = createEClass(FONT_ATTRIBUTE);
        createEAttribute(fontAttributeEClass, FONT_ATTRIBUTE__FONT);
        createEAttribute(fontAttributeEClass, FONT_ATTRIBUTE__FONT_COLOR);
        createEAttribute(fontAttributeEClass, FONT_ATTRIBUTE__TEXT_ALIGNMENT);
        createEAttribute(fontAttributeEClass, FONT_ATTRIBUTE__TEXT_POSITION);

        borderObjectEClass = createEClass(BORDER_OBJECT);
        createEAttribute(borderObjectEClass, BORDER_OBJECT__BORDER_COLOR);

        diagramModelImageProviderEClass = createEClass(DIAGRAM_MODEL_IMAGE_PROVIDER);
        createEAttribute(diagramModelImageProviderEClass, DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH);

        boundsEClass = createEClass(BOUNDS);
        createEAttribute(boundsEClass, BOUNDS__X);
        createEAttribute(boundsEClass, BOUNDS__Y);
        createEAttribute(boundsEClass, BOUNDS__WIDTH);
        createEAttribute(boundsEClass, BOUNDS__HEIGHT);

        lockableEClass = createEClass(LOCKABLE);
        createEAttribute(lockableEClass, LOCKABLE__LOCKED);

        archimateDiagramModelEClass = createEClass(ARCHIMATE_DIAGRAM_MODEL);
        createEAttribute(archimateDiagramModelEClass, ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT);

        diagramModelArchimateObjectEClass = createEClass(DIAGRAM_MODEL_ARCHIMATE_OBJECT);
        createEReference(diagramModelArchimateObjectEClass, DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT);
        createEAttribute(diagramModelArchimateObjectEClass, DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE);

        diagramModelArchimateConnectionEClass = createEClass(DIAGRAM_MODEL_ARCHIMATE_CONNECTION);
        createEReference(diagramModelArchimateConnectionEClass, DIAGRAM_MODEL_ARCHIMATE_CONNECTION__RELATIONSHIP);

        sketchModelEClass = createEClass(SKETCH_MODEL);
        createEAttribute(sketchModelEClass, SKETCH_MODEL__BACKGROUND);

        sketchModelStickyEClass = createEClass(SKETCH_MODEL_STICKY);

        sketchModelActorEClass = createEClass(SKETCH_MODEL_ACTOR);

        // Create enums
        folderTypeEEnum = createEEnum(FOLDER_TYPE);

        // Create data types
        fileEDataType = createEDataType(FILE);
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

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        archimateModelEClass.getESuperTypes().add(this.getFolderContainer());
        archimateModelEClass.getESuperTypes().add(this.getNameable());
        archimateModelEClass.getESuperTypes().add(this.getIdentifier());
        archimateModelEClass.getESuperTypes().add(this.getArchimateModelElement());
        archimateModelEClass.getESuperTypes().add(this.getProperties());
        archimateModelElementEClass.getESuperTypes().add(this.getAdapter());
        folderEClass.getESuperTypes().add(this.getArchimateModelElement());
        folderEClass.getESuperTypes().add(this.getFolderContainer());
        folderEClass.getESuperTypes().add(this.getNameable());
        folderEClass.getESuperTypes().add(this.getIdentifier());
        folderEClass.getESuperTypes().add(this.getDocumentable());
        folderEClass.getESuperTypes().add(this.getProperties());
        archimateElementEClass.getESuperTypes().add(this.getArchimateModelElement());
        archimateElementEClass.getESuperTypes().add(this.getIdentifier());
        archimateElementEClass.getESuperTypes().add(this.getCloneable());
        archimateElementEClass.getESuperTypes().add(this.getNameable());
        archimateElementEClass.getESuperTypes().add(this.getDocumentable());
        archimateElementEClass.getESuperTypes().add(this.getProperties());
        junctionElementEClass.getESuperTypes().add(this.getArchimateElement());
        interfaceElementEClass.getESuperTypes().add(this.getArchimateElement());
        serviceElementEClass.getESuperTypes().add(this.getArchimateElement());
        junctionEClass.getESuperTypes().add(this.getJunctionElement());
        andJunctionEClass.getESuperTypes().add(this.getJunctionElement());
        orJunctionEClass.getESuperTypes().add(this.getJunctionElement());
        relationshipEClass.getESuperTypes().add(this.getArchimateElement());
        accessRelationshipEClass.getESuperTypes().add(this.getRelationship());
        aggregationRelationshipEClass.getESuperTypes().add(this.getRelationship());
        assignmentRelationshipEClass.getESuperTypes().add(this.getRelationship());
        associationRelationshipEClass.getESuperTypes().add(this.getRelationship());
        compositionRelationshipEClass.getESuperTypes().add(this.getRelationship());
        flowRelationshipEClass.getESuperTypes().add(this.getRelationship());
        realisationRelationshipEClass.getESuperTypes().add(this.getRelationship());
        specialisationRelationshipEClass.getESuperTypes().add(this.getRelationship());
        triggeringRelationshipEClass.getESuperTypes().add(this.getRelationship());
        usedByRelationshipEClass.getESuperTypes().add(this.getRelationship());
        influenceRelationshipEClass.getESuperTypes().add(this.getRelationship());
        businessLayerElementEClass.getESuperTypes().add(this.getArchimateElement());
        businessActivityEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessActorEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessCollaborationEClass.getESuperTypes().add(this.getBusinessLayerElement());
        contractEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessEventEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessFunctionEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessInteractionEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessInterfaceEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessInterfaceEClass.getESuperTypes().add(this.getInterfaceElement());
        meaningEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessObjectEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessProcessEClass.getESuperTypes().add(this.getBusinessLayerElement());
        productEClass.getESuperTypes().add(this.getBusinessLayerElement());
        representationEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessRoleEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessServiceEClass.getESuperTypes().add(this.getBusinessLayerElement());
        businessServiceEClass.getESuperTypes().add(this.getServiceElement());
        valueEClass.getESuperTypes().add(this.getBusinessLayerElement());
        locationEClass.getESuperTypes().add(this.getBusinessLayerElement());
        applicationLayerElementEClass.getESuperTypes().add(this.getArchimateElement());
        applicationCollaborationEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationComponentEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationFunctionEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationInteractionEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationInterfaceEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationInterfaceEClass.getESuperTypes().add(this.getInterfaceElement());
        dataObjectEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationServiceEClass.getESuperTypes().add(this.getApplicationLayerElement());
        applicationServiceEClass.getESuperTypes().add(this.getServiceElement());
        technologyLayerElementEClass.getESuperTypes().add(this.getArchimateElement());
        artifactEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        communicationPathEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        networkEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        infrastructureInterfaceEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        infrastructureInterfaceEClass.getESuperTypes().add(this.getInterfaceElement());
        infrastructureServiceEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        infrastructureServiceEClass.getESuperTypes().add(this.getServiceElement());
        infrastructureFunctionEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        nodeEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        systemSoftwareEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        deviceEClass.getESuperTypes().add(this.getTechnologyLayerElement());
        motivationElementEClass.getESuperTypes().add(this.getArchimateElement());
        stakeholderEClass.getESuperTypes().add(this.getMotivationElement());
        driverEClass.getESuperTypes().add(this.getMotivationElement());
        assessmentEClass.getESuperTypes().add(this.getMotivationElement());
        goalEClass.getESuperTypes().add(this.getMotivationElement());
        requirementEClass.getESuperTypes().add(this.getMotivationElement());
        constraintEClass.getESuperTypes().add(this.getMotivationElement());
        principleEClass.getESuperTypes().add(this.getMotivationElement());
        implementationMigrationElementEClass.getESuperTypes().add(this.getArchimateElement());
        workPackageEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        deliverableEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        plateauEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        gapEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        diagramModelComponentEClass.getESuperTypes().add(this.getIdentifier());
        diagramModelComponentEClass.getESuperTypes().add(this.getCloneable());
        diagramModelComponentEClass.getESuperTypes().add(this.getAdapter());
        diagramModelComponentEClass.getESuperTypes().add(this.getNameable());
        diagramModelContainerEClass.getESuperTypes().add(this.getDiagramModelComponent());
        diagramModelEClass.getESuperTypes().add(this.getArchimateModelElement());
        diagramModelEClass.getESuperTypes().add(this.getDiagramModelContainer());
        diagramModelEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelEClass.getESuperTypes().add(this.getProperties());
        diagramModelReferenceEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelObjectEClass.getESuperTypes().add(this.getDiagramModelComponent());
        diagramModelObjectEClass.getESuperTypes().add(this.getFontAttribute());
        diagramModelObjectEClass.getESuperTypes().add(this.getLineObject());
        diagramModelGroupEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelGroupEClass.getESuperTypes().add(this.getDiagramModelContainer());
        diagramModelGroupEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelGroupEClass.getESuperTypes().add(this.getProperties());
        diagramModelNoteEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelNoteEClass.getESuperTypes().add(this.getTextContent());
        diagramModelImageEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelImageEClass.getESuperTypes().add(this.getBorderObject());
        diagramModelImageEClass.getESuperTypes().add(this.getDiagramModelImageProvider());
        diagramModelConnectionEClass.getESuperTypes().add(this.getDiagramModelComponent());
        diagramModelConnectionEClass.getESuperTypes().add(this.getFontAttribute());
        diagramModelConnectionEClass.getESuperTypes().add(this.getProperties());
        diagramModelConnectionEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelConnectionEClass.getESuperTypes().add(this.getLineObject());
        diagramModelBendpointEClass.getESuperTypes().add(this.getCloneable());
        archimateDiagramModelEClass.getESuperTypes().add(this.getDiagramModel());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getDiagramModelContainer());
        diagramModelArchimateConnectionEClass.getESuperTypes().add(this.getDiagramModelConnection());
        sketchModelEClass.getESuperTypes().add(this.getDiagramModel());
        sketchModelStickyEClass.getESuperTypes().add(this.getDiagramModelObject());
        sketchModelStickyEClass.getESuperTypes().add(this.getDiagramModelContainer());
        sketchModelStickyEClass.getESuperTypes().add(this.getTextContent());
        sketchModelStickyEClass.getESuperTypes().add(this.getProperties());
        sketchModelActorEClass.getESuperTypes().add(this.getDiagramModelObject());
        sketchModelActorEClass.getESuperTypes().add(this.getDocumentable());
        sketchModelActorEClass.getESuperTypes().add(this.getProperties());

        // Initialize classes and features; add operations and parameters
        initEClass(adapterEClass, IAdapter.class, "Adapter", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        EOperation op = addEOperation(adapterEClass, ecorePackage.getEJavaObject(), "getAdapter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEJavaObject(), "adapter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(adapterEClass, null, "setAdapter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEJavaObject(), "adapter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEJavaObject(), "object", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(identifierEClass, IIdentifier.class, "Identifier", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getIdentifier_Id(), ecorePackage.getEString(), "id", null, 0, 1, IIdentifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(propertyEClass, IProperty.class, "Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getProperty_Key(), ecorePackage.getEString(), "key", "", 0, 1, IProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getProperty_Value(), ecorePackage.getEString(), "value", "", 0, 1, IProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(propertiesEClass, IProperties.class, "Properties", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getProperties_Properties(), this.getProperty(), null, "properties", null, 0, -1, IProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(metadataEClass, IMetadata.class, "Metadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getMetadata_Entries(), this.getProperty(), null, "entries", null, 0, -1, IMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(nameableEClass, INameable.class, "Nameable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getNameable_Name(), ecorePackage.getEString(), "name", "", 0, 1, INameable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(textContentEClass, ITextContent.class, "TextContent", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getTextContent_Content(), ecorePackage.getEString(), "content", "", 0, 1, ITextContent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(documentableEClass, IDocumentable.class, "Documentable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDocumentable_Documentation(), ecorePackage.getEString(), "documentation", "", 0, 1, IDocumentable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(cloneableEClass, ICloneable.class, "Cloneable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        addEOperation(cloneableEClass, ecorePackage.getEObject(), "getCopy", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(folderContainerEClass, IFolderContainer.class, "FolderContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getFolderContainer_Folders(), this.getFolder(), null, "folders", null, 0, -1, IFolderContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(archimateModelEClass, IArchimateModel.class, "ArchimateModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getArchimateModel_Purpose(), ecorePackage.getEString(), "purpose", null, 0, 1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getArchimateModel_File(), this.getFile(), "file", null, 0, 1, IArchimateModel.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getArchimateModel_Version(), ecorePackage.getEString(), "version", "", 0, 1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEReference(getArchimateModel_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, null, "setDefaults", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, this.getFolder(), "addDerivedRelationsFolder", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, null, "removeDerivedRelationsFolder", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(archimateModelEClass, this.getFolder(), "getDefaultFolderForElement", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEObject(), "element", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, this.getDiagramModel(), "getDefaultDiagramModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, this.getDiagramModel(), "getDiagramModels", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(archimateModelEClass, this.getFolder(), "getFolder", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getFolderType(), "type", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(archimateModelElementEClass, IArchimateModelElement.class, "ArchimateModelElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getArchimateModelElement_ArchimateModel(), this.getArchimateModel(), null, "archimateModel", null, 0, 1, IArchimateModelElement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(folderEClass, IFolder.class, "Folder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getFolder_Elements(), ecorePackage.getEObject(), null, "elements", null, 0, -1, IFolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getFolder_Type(), this.getFolderType(), "type", null, 0, 1, IFolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(archimateElementEClass, IArchimateElement.class, "ArchimateElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(junctionElementEClass, IJunctionElement.class, "JunctionElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(interfaceElementEClass, IInterfaceElement.class, "InterfaceElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getInterfaceElement_InterfaceType(), ecorePackage.getEInt(), "interfaceType", "0", 0, 1, IInterfaceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(serviceElementEClass, IServiceElement.class, "ServiceElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(junctionEClass, IJunction.class, "Junction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(andJunctionEClass, IAndJunction.class, "AndJunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(orJunctionEClass, IOrJunction.class, "OrJunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(relationshipEClass, IRelationship.class, "Relationship", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getRelationship_Source(), this.getArchimateElement(), null, "source", null, 0, 1, IRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getRelationship_Target(), this.getArchimateElement(), null, "target", null, 0, 1, IRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(accessRelationshipEClass, IAccessRelationship.class, "AccessRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getAccessRelationship_AccessType(), ecorePackage.getEInt(), "accessType", "0", 0, 1, IAccessRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(aggregationRelationshipEClass, IAggregationRelationship.class, "AggregationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(assignmentRelationshipEClass, IAssignmentRelationship.class, "AssignmentRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(associationRelationshipEClass, IAssociationRelationship.class, "AssociationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(compositionRelationshipEClass, ICompositionRelationship.class, "CompositionRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(flowRelationshipEClass, IFlowRelationship.class, "FlowRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(realisationRelationshipEClass, IRealisationRelationship.class, "RealisationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(specialisationRelationshipEClass, ISpecialisationRelationship.class, "SpecialisationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(triggeringRelationshipEClass, ITriggeringRelationship.class, "TriggeringRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(usedByRelationshipEClass, IUsedByRelationship.class, "UsedByRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(influenceRelationshipEClass, IInfluenceRelationship.class, "InfluenceRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessLayerElementEClass, IBusinessLayerElement.class, "BusinessLayerElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessActivityEClass, IBusinessActivity.class, "BusinessActivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessActorEClass, IBusinessActor.class, "BusinessActor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessCollaborationEClass, IBusinessCollaboration.class, "BusinessCollaboration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(contractEClass, IContract.class, "Contract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessEventEClass, IBusinessEvent.class, "BusinessEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessFunctionEClass, IBusinessFunction.class, "BusinessFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessInteractionEClass, IBusinessInteraction.class, "BusinessInteraction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessInterfaceEClass, IBusinessInterface.class, "BusinessInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(meaningEClass, IMeaning.class, "Meaning", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessObjectEClass, IBusinessObject.class, "BusinessObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessProcessEClass, IBusinessProcess.class, "BusinessProcess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(productEClass, IProduct.class, "Product", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(representationEClass, IRepresentation.class, "Representation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessRoleEClass, IBusinessRole.class, "BusinessRole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessServiceEClass, IBusinessService.class, "BusinessService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(valueEClass, IValue.class, "Value", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(locationEClass, ILocation.class, "Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationLayerElementEClass, IApplicationLayerElement.class, "ApplicationLayerElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationCollaborationEClass, IApplicationCollaboration.class, "ApplicationCollaboration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationComponentEClass, IApplicationComponent.class, "ApplicationComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationFunctionEClass, IApplicationFunction.class, "ApplicationFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationInteractionEClass, IApplicationInteraction.class, "ApplicationInteraction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationInterfaceEClass, IApplicationInterface.class, "ApplicationInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(dataObjectEClass, IDataObject.class, "DataObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationServiceEClass, IApplicationService.class, "ApplicationService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyLayerElementEClass, ITechnologyLayerElement.class, "TechnologyLayerElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(artifactEClass, IArtifact.class, "Artifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(communicationPathEClass, ICommunicationPath.class, "CommunicationPath", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(networkEClass, INetwork.class, "Network", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(infrastructureInterfaceEClass, IInfrastructureInterface.class, "InfrastructureInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(infrastructureServiceEClass, IInfrastructureService.class, "InfrastructureService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(infrastructureFunctionEClass, IInfrastructureFunction.class, "InfrastructureFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(nodeEClass, INode.class, "Node", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(systemSoftwareEClass, ISystemSoftware.class, "SystemSoftware", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(deviceEClass, IDevice.class, "Device", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(motivationElementEClass, IMotivationElement.class, "MotivationElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(stakeholderEClass, IStakeholder.class, "Stakeholder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(driverEClass, IDriver.class, "Driver", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(assessmentEClass, IAssessment.class, "Assessment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(goalEClass, IGoal.class, "Goal", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(requirementEClass, IRequirement.class, "Requirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(constraintEClass, IConstraint.class, "Constraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(principleEClass, IPrinciple.class, "Principle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(implementationMigrationElementEClass, IImplementationMigrationElement.class, "ImplementationMigrationElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(workPackageEClass, IWorkPackage.class, "WorkPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(deliverableEClass, IDeliverable.class, "Deliverable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(plateauEClass, IPlateau.class, "Plateau", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(gapEClass, IGap.class, "Gap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(diagramModelComponentEClass, IDiagramModelComponent.class, "DiagramModelComponent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelComponent_DiagramModel(), this.getDiagramModel(), null, "diagramModel", null, 0, 1, IDiagramModelComponent.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelContainerEClass, IDiagramModelContainer.class, "DiagramModelContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelContainer_Children(), this.getDiagramModelObject(), null, "children", null, 0, -1, IDiagramModelContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelEClass, IDiagramModel.class, "DiagramModel", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDiagramModel_ConnectionRouterType(), ecorePackage.getEInt(), "connectionRouterType", null, 0, 1, IDiagramModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelReferenceEClass, IDiagramModelReference.class, "DiagramModelReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelReference_ReferencedModel(), this.getDiagramModel(), null, "referencedModel", null, 0, 1, IDiagramModelReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelObjectEClass, IDiagramModelObject.class, "DiagramModelObject", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelObject_Bounds(), this.getBounds(), null, "bounds", null, 0, 1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelObject_SourceConnections(), this.getDiagramModelConnection(), null, "sourceConnections", null, 0, -1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelObject_TargetConnections(), this.getDiagramModelConnection(), null, "targetConnections", null, 0, -1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelObject_FillColor(), ecorePackage.getEString(), "fillColor", null, 0, 1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelObjectEClass, null, "addConnection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getDiagramModelConnection(), "connection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelObjectEClass, null, "removeConnection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getDiagramModelConnection(), "connection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelObjectEClass, null, "setBounds", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "x", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "y", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "width", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "height", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelGroupEClass, IDiagramModelGroup.class, "DiagramModelGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(diagramModelNoteEClass, IDiagramModelNote.class, "DiagramModelNote", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(diagramModelImageEClass, IDiagramModelImage.class, "DiagramModelImage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(diagramModelConnectionEClass, IDiagramModelConnection.class, "DiagramModelConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDiagramModelConnection_Text(), ecorePackage.getEString(), "text", "", 0, 1, IDiagramModelConnection.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEReference(getDiagramModelConnection_Source(), this.getDiagramModelObject(), null, "source", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelConnection_Target(), this.getDiagramModelObject(), null, "target", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelConnection_Bendpoints(), this.getDiagramModelBendpoint(), null, "bendpoints", null, 0, -1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelConnection_Type(), ecorePackage.getEInt(), "type", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelConnectionEClass, null, "connect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getDiagramModelObject(), "source", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getDiagramModelObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(diagramModelConnectionEClass, null, "disconnect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(diagramModelConnectionEClass, null, "reconnect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelBendpointEClass, IDiagramModelBendpoint.class, "DiagramModelBendpoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDiagramModelBendpoint_StartX(), ecorePackage.getEInt(), "startX", null, 0, 1, IDiagramModelBendpoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelBendpoint_StartY(), ecorePackage.getEInt(), "startY", null, 0, 1, IDiagramModelBendpoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelBendpoint_EndX(), ecorePackage.getEInt(), "endX", null, 0, 1, IDiagramModelBendpoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelBendpoint_EndY(), ecorePackage.getEInt(), "endY", null, 0, 1, IDiagramModelBendpoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(lineObjectEClass, ILineObject.class, "LineObject", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getLineObject_LineWidth(), ecorePackage.getEInt(), "lineWidth", "1", 0, 1, ILineObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getLineObject_LineColor(), ecorePackage.getEString(), "lineColor", null, 0, 1, ILineObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(fontAttributeEClass, IFontAttribute.class, "FontAttribute", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getFontAttribute_Font(), ecorePackage.getEString(), "font", null, 0, 1, IFontAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getFontAttribute_FontColor(), ecorePackage.getEString(), "fontColor", null, 0, 1, IFontAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getFontAttribute_TextAlignment(), ecorePackage.getEInt(), "textAlignment", null, 0, 1, IFontAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getFontAttribute_TextPosition(), ecorePackage.getEInt(), "textPosition", null, 0, 1, IFontAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        addEOperation(fontAttributeEClass, ecorePackage.getEInt(), "getDefaultTextAlignment", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(borderObjectEClass, IBorderObject.class, "BorderObject", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getBorderObject_BorderColor(), ecorePackage.getEString(), "borderColor", null, 0, 1, IBorderObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelImageProviderEClass, IDiagramModelImageProvider.class, "DiagramModelImageProvider", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDiagramModelImageProvider_ImagePath(), ecorePackage.getEString(), "imagePath", null, 0, 1, IDiagramModelImageProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(boundsEClass, IBounds.class, "Bounds", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getBounds_X(), ecorePackage.getEInt(), "x", null, 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getBounds_Y(), ecorePackage.getEInt(), "y", null, 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getBounds_Width(), ecorePackage.getEInt(), "width", "-1", 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getBounds_Height(), ecorePackage.getEInt(), "height", "-1", 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        addEOperation(boundsEClass, this.getBounds(), "getCopy", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(lockableEClass, ILockable.class, "Lockable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getLockable_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, ILockable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(archimateDiagramModelEClass, IArchimateDiagramModel.class, "ArchimateDiagramModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getArchimateDiagramModel_Viewpoint(), ecorePackage.getEInt(), "viewpoint", null, 0, 1, IArchimateDiagramModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelArchimateObjectEClass, IDiagramModelArchimateObject.class, "DiagramModelArchimateObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelArchimateObject_ArchimateElement(), this.getArchimateElement(), null, "archimateElement", null, 0, 1, IDiagramModelArchimateObject.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelArchimateObject_Type(), ecorePackage.getEInt(), "type", "0", 0, 1, IDiagramModelArchimateObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        op = addEOperation(diagramModelArchimateObjectEClass, null, "addArchimateElementToModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getFolder(), "parent", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(diagramModelArchimateObjectEClass, null, "removeArchimateElementFromModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelArchimateConnectionEClass, IDiagramModelArchimateConnection.class, "DiagramModelArchimateConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelArchimateConnection_Relationship(), this.getRelationship(), null, "relationship", null, 0, 1, IDiagramModelArchimateConnection.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelArchimateConnectionEClass, null, "addRelationshipToModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getFolder(), "parent", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(diagramModelArchimateConnectionEClass, null, "removeRelationshipFromModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(sketchModelEClass, ISketchModel.class, "SketchModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getSketchModel_Background(), ecorePackage.getEInt(), "background", "1", 0, 1, ISketchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(sketchModelStickyEClass, ISketchModelSticky.class, "SketchModelSticky", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(sketchModelActorEClass, ISketchModelActor.class, "SketchModelActor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        // Initialize enums and add enum literals
        initEEnum(folderTypeEEnum, FolderType.class, "FolderType"); //$NON-NLS-1$
        addEEnumLiteral(folderTypeEEnum, FolderType.USER);
        addEEnumLiteral(folderTypeEEnum, FolderType.BUSINESS);
        addEEnumLiteral(folderTypeEEnum, FolderType.APPLICATION);
        addEEnumLiteral(folderTypeEEnum, FolderType.TECHNOLOGY);
        addEEnumLiteral(folderTypeEEnum, FolderType.CONNECTORS);
        addEEnumLiteral(folderTypeEEnum, FolderType.RELATIONS);
        addEEnumLiteral(folderTypeEEnum, FolderType.DIAGRAMS);
        addEEnumLiteral(folderTypeEEnum, FolderType.DERIVED);
        addEEnumLiteral(folderTypeEEnum, FolderType.MOTIVATION);
        addEEnumLiteral(folderTypeEEnum, FolderType.IMPLEMENTATION_MIGRATION);

        // Initialize data types
        initEDataType(fileEDataType, File.class, "File", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

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
          (getProperties_Properties(), 
           source, 
           new String[] {
             "name", "property", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getMetadata_Entries(), 
           source, 
           new String[] {
             "name", "entry", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getTextContent_Content(), 
           source, 
           new String[] {
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getDocumentable_Documentation(), 
           source, 
           new String[] {
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getFolderContainer_Folders(), 
           source, 
           new String[] {
             "name", "folder", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (archimateModelEClass, 
           source, 
           new String[] {
             "name", "model" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getArchimateModel_Purpose(), 
           source, 
           new String[] {
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getFolder_Elements(), 
           source, 
           new String[] {
             "name", "element", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getDiagramModelContainer_Children(), 
           source, 
           new String[] {
             "name", "child", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getDiagramModelReference_ReferencedModel(), 
           source, 
           new String[] {
             "name", "model", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "attribute" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getDiagramModelObject_SourceConnections(), 
           source, 
           new String[] {
             "name", "sourceConnection", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (diagramModelGroupEClass, 
           source, 
           new String[] {
             "name", "Group" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (diagramModelNoteEClass, 
           source, 
           new String[] {
             "name", "Note" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (getDiagramModelConnection_Bendpoints(), 
           source, 
           new String[] {
             "name", "bendpoint", //$NON-NLS-1$ //$NON-NLS-2$
             "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (diagramModelArchimateObjectEClass, 
           source, 
           new String[] {
             "name", "DiagramObject" //$NON-NLS-1$ //$NON-NLS-2$
           });		
        addAnnotation
          (diagramModelArchimateConnectionEClass, 
           source, 
           new String[] {
             "name", "Connection" //$NON-NLS-1$ //$NON-NLS-2$
           });
    }

} //ArchimatePackage
