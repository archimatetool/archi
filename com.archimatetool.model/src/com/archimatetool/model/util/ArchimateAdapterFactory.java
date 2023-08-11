/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IActiveStructureElement;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IApplicationCollaboration;
import com.archimatetool.model.IApplicationComponent;
import com.archimatetool.model.IApplicationElement;
import com.archimatetool.model.IApplicationEvent;
import com.archimatetool.model.IApplicationFunction;
import com.archimatetool.model.IApplicationInteraction;
import com.archimatetool.model.IApplicationInterface;
import com.archimatetool.model.IApplicationProcess;
import com.archimatetool.model.IApplicationService;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IArtifact;
import com.archimatetool.model.IAssessment;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IBehaviorElement;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.IBorderType;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessCollaboration;
import com.archimatetool.model.IBusinessElement;
import com.archimatetool.model.IBusinessEvent;
import com.archimatetool.model.IBusinessFunction;
import com.archimatetool.model.IBusinessInteraction;
import com.archimatetool.model.IBusinessInterface;
import com.archimatetool.model.IBusinessObject;
import com.archimatetool.model.IBusinessProcess;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IBusinessService;
import com.archimatetool.model.ICapability;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.ICommunicationNetwork;
import com.archimatetool.model.ICompositeElement;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IConstraint;
import com.archimatetool.model.IContract;
import com.archimatetool.model.ICourseOfAction;
import com.archimatetool.model.IDataObject;
import com.archimatetool.model.IDeliverable;
import com.archimatetool.model.IDependendencyRelationship;
import com.archimatetool.model.IDevice;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
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
import com.archimatetool.model.IDistributionNetwork;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IDriver;
import com.archimatetool.model.IDynamicRelationship;
import com.archimatetool.model.IEquipment;
import com.archimatetool.model.IFacility;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFlowRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IGap;
import com.archimatetool.model.IGoal;
import com.archimatetool.model.IGrouping;
import com.archimatetool.model.IHintProvider;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IImplementationEvent;
import com.archimatetool.model.IImplementationMigrationElement;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILocation;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.IMaterial;
import com.archimatetool.model.IMeaning;
import com.archimatetool.model.IMetadata;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.INameable;
import com.archimatetool.model.INode;
import com.archimatetool.model.IOtherRelationship;
import com.archimatetool.model.IOutcome;
import com.archimatetool.model.IPassiveStructureElement;
import com.archimatetool.model.IPath;
import com.archimatetool.model.IPhysicalElement;
import com.archimatetool.model.IPlateau;
import com.archimatetool.model.IPrinciple;
import com.archimatetool.model.IProduct;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRealizationRelationship;
import com.archimatetool.model.IRepresentation;
import com.archimatetool.model.IRequirement;
import com.archimatetool.model.IResource;
import com.archimatetool.model.IServingRelationship;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;
import com.archimatetool.model.ISpecializationRelationship;
import com.archimatetool.model.IStakeholder;
import com.archimatetool.model.IStrategyBehaviorElement;
import com.archimatetool.model.IStrategyElement;
import com.archimatetool.model.IStructuralRelationship;
import com.archimatetool.model.IStructureElement;
import com.archimatetool.model.ISystemSoftware;
import com.archimatetool.model.ITechnologyCollaboration;
import com.archimatetool.model.ITechnologyElement;
import com.archimatetool.model.ITechnologyEvent;
import com.archimatetool.model.ITechnologyFunction;
import com.archimatetool.model.ITechnologyInteraction;
import com.archimatetool.model.ITechnologyInterface;
import com.archimatetool.model.ITechnologyObject;
import com.archimatetool.model.ITechnologyProcess;
import com.archimatetool.model.ITechnologyService;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextContent;
import com.archimatetool.model.ITextPosition;
import com.archimatetool.model.ITriggeringRelationship;
import com.archimatetool.model.IValue;
import com.archimatetool.model.IValueStream;
import com.archimatetool.model.IWorkPackage;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.archimatetool.model.IArchimatePackage
 * @generated
 */
public class ArchimateAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static IArchimatePackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArchimateAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = IArchimatePackage.eINSTANCE;
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
    protected ArchimateSwitch<Adapter> modelSwitch =
        new ArchimateSwitch<Adapter>() {
            @Override
            public Adapter caseAdapter(IAdapter object) {
                return createAdapterAdapter();
            }
            @Override
            public Adapter caseIdentifier(IIdentifier object) {
                return createIdentifierAdapter();
            }
            @Override
            public Adapter caseProperty(IProperty object) {
                return createPropertyAdapter();
            }
            @Override
            public Adapter caseProperties(IProperties object) {
                return createPropertiesAdapter();
            }
            @Override
            public Adapter caseFeature(IFeature object) {
                return createFeatureAdapter();
            }
            @Override
            public Adapter caseFeatures(IFeatures object) {
                return createFeaturesAdapter();
            }
            @Override
            public Adapter caseMetadata(IMetadata object) {
                return createMetadataAdapter();
            }
            @Override
            public Adapter caseNameable(INameable object) {
                return createNameableAdapter();
            }
            @Override
            public Adapter caseTextContent(ITextContent object) {
                return createTextContentAdapter();
            }
            @Override
            public Adapter caseDocumentable(IDocumentable object) {
                return createDocumentableAdapter();
            }
            @Override
            public Adapter caseCloneable(ICloneable object) {
                return createCloneableAdapter();
            }
            @Override
            public Adapter caseProfile(IProfile object) {
                return createProfileAdapter();
            }
            @Override
            public Adapter caseProfiles(IProfiles object) {
                return createProfilesAdapter();
            }
            @Override
            public Adapter caseFolderContainer(IFolderContainer object) {
                return createFolderContainerAdapter();
            }
            @Override
            public Adapter caseFolder(IFolder object) {
                return createFolderAdapter();
            }
            @Override
            public Adapter caseArchimateModelObject(IArchimateModelObject object) {
                return createArchimateModelObjectAdapter();
            }
            @Override
            public Adapter caseArchimateConcept(IArchimateConcept object) {
                return createArchimateConceptAdapter();
            }
            @Override
            public Adapter caseArchimateElement(IArchimateElement object) {
                return createArchimateElementAdapter();
            }
            @Override
            public Adapter caseArchimateRelationship(IArchimateRelationship object) {
                return createArchimateRelationshipAdapter();
            }
            @Override
            public Adapter caseStrategyElement(IStrategyElement object) {
                return createStrategyElementAdapter();
            }
            @Override
            public Adapter caseBusinessElement(IBusinessElement object) {
                return createBusinessElementAdapter();
            }
            @Override
            public Adapter caseApplicationElement(IApplicationElement object) {
                return createApplicationElementAdapter();
            }
            @Override
            public Adapter caseTechnologyElement(ITechnologyElement object) {
                return createTechnologyElementAdapter();
            }
            @Override
            public Adapter caseTechnologyObject(ITechnologyObject object) {
                return createTechnologyObjectAdapter();
            }
            @Override
            public Adapter casePhysicalElement(IPhysicalElement object) {
                return createPhysicalElementAdapter();
            }
            @Override
            public Adapter caseMotivationElement(IMotivationElement object) {
                return createMotivationElementAdapter();
            }
            @Override
            public Adapter caseImplementationMigrationElement(IImplementationMigrationElement object) {
                return createImplementationMigrationElementAdapter();
            }
            @Override
            public Adapter caseCompositeElement(ICompositeElement object) {
                return createCompositeElementAdapter();
            }
            @Override
            public Adapter caseBehaviorElement(IBehaviorElement object) {
                return createBehaviorElementAdapter();
            }
            @Override
            public Adapter caseStrategyBehaviorElement(IStrategyBehaviorElement object) {
                return createStrategyBehaviorElementAdapter();
            }
            @Override
            public Adapter caseStructureElement(IStructureElement object) {
                return createStructureElementAdapter();
            }
            @Override
            public Adapter caseActiveStructureElement(IActiveStructureElement object) {
                return createActiveStructureElementAdapter();
            }
            @Override
            public Adapter casePassiveStructureElement(IPassiveStructureElement object) {
                return createPassiveStructureElementAdapter();
            }
            @Override
            public Adapter caseStructuralRelationship(IStructuralRelationship object) {
                return createStructuralRelationshipAdapter();
            }
            @Override
            public Adapter caseDependendencyRelationship(IDependendencyRelationship object) {
                return createDependendencyRelationshipAdapter();
            }
            @Override
            public Adapter caseDynamicRelationship(IDynamicRelationship object) {
                return createDynamicRelationshipAdapter();
            }
            @Override
            public Adapter caseOtherRelationship(IOtherRelationship object) {
                return createOtherRelationshipAdapter();
            }
            @Override
            public Adapter caseArchimateModel(IArchimateModel object) {
                return createArchimateModelAdapter();
            }
            @Override
            public Adapter caseJunction(IJunction object) {
                return createJunctionAdapter();
            }
            @Override
            public Adapter caseApplicationCollaboration(IApplicationCollaboration object) {
                return createApplicationCollaborationAdapter();
            }
            @Override
            public Adapter caseApplicationComponent(IApplicationComponent object) {
                return createApplicationComponentAdapter();
            }
            @Override
            public Adapter caseApplicationEvent(IApplicationEvent object) {
                return createApplicationEventAdapter();
            }
            @Override
            public Adapter caseApplicationFunction(IApplicationFunction object) {
                return createApplicationFunctionAdapter();
            }
            @Override
            public Adapter caseApplicationInteraction(IApplicationInteraction object) {
                return createApplicationInteractionAdapter();
            }
            @Override
            public Adapter caseApplicationInterface(IApplicationInterface object) {
                return createApplicationInterfaceAdapter();
            }
            @Override
            public Adapter caseApplicationProcess(IApplicationProcess object) {
                return createApplicationProcessAdapter();
            }
            @Override
            public Adapter caseApplicationService(IApplicationService object) {
                return createApplicationServiceAdapter();
            }
            @Override
            public Adapter caseArtifact(IArtifact object) {
                return createArtifactAdapter();
            }
            @Override
            public Adapter caseAssessment(IAssessment object) {
                return createAssessmentAdapter();
            }
            @Override
            public Adapter caseBusinessActor(IBusinessActor object) {
                return createBusinessActorAdapter();
            }
            @Override
            public Adapter caseBusinessCollaboration(IBusinessCollaboration object) {
                return createBusinessCollaborationAdapter();
            }
            @Override
            public Adapter caseBusinessEvent(IBusinessEvent object) {
                return createBusinessEventAdapter();
            }
            @Override
            public Adapter caseBusinessFunction(IBusinessFunction object) {
                return createBusinessFunctionAdapter();
            }
            @Override
            public Adapter caseBusinessInteraction(IBusinessInteraction object) {
                return createBusinessInteractionAdapter();
            }
            @Override
            public Adapter caseBusinessInterface(IBusinessInterface object) {
                return createBusinessInterfaceAdapter();
            }
            @Override
            public Adapter caseBusinessObject(IBusinessObject object) {
                return createBusinessObjectAdapter();
            }
            @Override
            public Adapter caseBusinessProcess(IBusinessProcess object) {
                return createBusinessProcessAdapter();
            }
            @Override
            public Adapter caseBusinessRole(IBusinessRole object) {
                return createBusinessRoleAdapter();
            }
            @Override
            public Adapter caseBusinessService(IBusinessService object) {
                return createBusinessServiceAdapter();
            }
            @Override
            public Adapter caseCapability(ICapability object) {
                return createCapabilityAdapter();
            }
            @Override
            public Adapter caseCommunicationNetwork(ICommunicationNetwork object) {
                return createCommunicationNetworkAdapter();
            }
            @Override
            public Adapter caseContract(IContract object) {
                return createContractAdapter();
            }
            @Override
            public Adapter caseConstraint(IConstraint object) {
                return createConstraintAdapter();
            }
            @Override
            public Adapter caseCourseOfAction(ICourseOfAction object) {
                return createCourseOfActionAdapter();
            }
            @Override
            public Adapter caseDataObject(IDataObject object) {
                return createDataObjectAdapter();
            }
            @Override
            public Adapter caseDeliverable(IDeliverable object) {
                return createDeliverableAdapter();
            }
            @Override
            public Adapter caseDevice(IDevice object) {
                return createDeviceAdapter();
            }
            @Override
            public Adapter caseDistributionNetwork(IDistributionNetwork object) {
                return createDistributionNetworkAdapter();
            }
            @Override
            public Adapter caseDriver(IDriver object) {
                return createDriverAdapter();
            }
            @Override
            public Adapter caseEquipment(IEquipment object) {
                return createEquipmentAdapter();
            }
            @Override
            public Adapter caseFacility(IFacility object) {
                return createFacilityAdapter();
            }
            @Override
            public Adapter caseGap(IGap object) {
                return createGapAdapter();
            }
            @Override
            public Adapter caseGoal(IGoal object) {
                return createGoalAdapter();
            }
            @Override
            public Adapter caseGrouping(IGrouping object) {
                return createGroupingAdapter();
            }
            @Override
            public Adapter caseImplementationEvent(IImplementationEvent object) {
                return createImplementationEventAdapter();
            }
            @Override
            public Adapter caseLocation(ILocation object) {
                return createLocationAdapter();
            }
            @Override
            public Adapter caseMaterial(IMaterial object) {
                return createMaterialAdapter();
            }
            @Override
            public Adapter caseMeaning(IMeaning object) {
                return createMeaningAdapter();
            }
            @Override
            public Adapter caseNode(INode object) {
                return createNodeAdapter();
            }
            @Override
            public Adapter caseOutcome(IOutcome object) {
                return createOutcomeAdapter();
            }
            @Override
            public Adapter casePath(IPath object) {
                return createPathAdapter();
            }
            @Override
            public Adapter casePlateau(IPlateau object) {
                return createPlateauAdapter();
            }
            @Override
            public Adapter casePrinciple(IPrinciple object) {
                return createPrincipleAdapter();
            }
            @Override
            public Adapter caseProduct(IProduct object) {
                return createProductAdapter();
            }
            @Override
            public Adapter caseRepresentation(IRepresentation object) {
                return createRepresentationAdapter();
            }
            @Override
            public Adapter caseResource(IResource object) {
                return createResourceAdapter();
            }
            @Override
            public Adapter caseRequirement(IRequirement object) {
                return createRequirementAdapter();
            }
            @Override
            public Adapter caseStakeholder(IStakeholder object) {
                return createStakeholderAdapter();
            }
            @Override
            public Adapter caseSystemSoftware(ISystemSoftware object) {
                return createSystemSoftwareAdapter();
            }
            @Override
            public Adapter caseTechnologyCollaboration(ITechnologyCollaboration object) {
                return createTechnologyCollaborationAdapter();
            }
            @Override
            public Adapter caseTechnologyEvent(ITechnologyEvent object) {
                return createTechnologyEventAdapter();
            }
            @Override
            public Adapter caseTechnologyFunction(ITechnologyFunction object) {
                return createTechnologyFunctionAdapter();
            }
            @Override
            public Adapter caseTechnologyInterface(ITechnologyInterface object) {
                return createTechnologyInterfaceAdapter();
            }
            @Override
            public Adapter caseTechnologyInteraction(ITechnologyInteraction object) {
                return createTechnologyInteractionAdapter();
            }
            @Override
            public Adapter caseTechnologyProcess(ITechnologyProcess object) {
                return createTechnologyProcessAdapter();
            }
            @Override
            public Adapter caseTechnologyService(ITechnologyService object) {
                return createTechnologyServiceAdapter();
            }
            @Override
            public Adapter caseValue(IValue object) {
                return createValueAdapter();
            }
            @Override
            public Adapter caseValueStream(IValueStream object) {
                return createValueStreamAdapter();
            }
            @Override
            public Adapter caseWorkPackage(IWorkPackage object) {
                return createWorkPackageAdapter();
            }
            @Override
            public Adapter caseAccessRelationship(IAccessRelationship object) {
                return createAccessRelationshipAdapter();
            }
            @Override
            public Adapter caseAggregationRelationship(IAggregationRelationship object) {
                return createAggregationRelationshipAdapter();
            }
            @Override
            public Adapter caseAssignmentRelationship(IAssignmentRelationship object) {
                return createAssignmentRelationshipAdapter();
            }
            @Override
            public Adapter caseAssociationRelationship(IAssociationRelationship object) {
                return createAssociationRelationshipAdapter();
            }
            @Override
            public Adapter caseCompositionRelationship(ICompositionRelationship object) {
                return createCompositionRelationshipAdapter();
            }
            @Override
            public Adapter caseFlowRelationship(IFlowRelationship object) {
                return createFlowRelationshipAdapter();
            }
            @Override
            public Adapter caseInfluenceRelationship(IInfluenceRelationship object) {
                return createInfluenceRelationshipAdapter();
            }
            @Override
            public Adapter caseRealizationRelationship(IRealizationRelationship object) {
                return createRealizationRelationshipAdapter();
            }
            @Override
            public Adapter caseServingRelationship(IServingRelationship object) {
                return createServingRelationshipAdapter();
            }
            @Override
            public Adapter caseSpecializationRelationship(ISpecializationRelationship object) {
                return createSpecializationRelationshipAdapter();
            }
            @Override
            public Adapter caseTriggeringRelationship(ITriggeringRelationship object) {
                return createTriggeringRelationshipAdapter();
            }
            @Override
            public Adapter caseDiagramModelComponent(IDiagramModelComponent object) {
                return createDiagramModelComponentAdapter();
            }
            @Override
            public Adapter caseConnectable(IConnectable object) {
                return createConnectableAdapter();
            }
            @Override
            public Adapter caseDiagramModelContainer(IDiagramModelContainer object) {
                return createDiagramModelContainerAdapter();
            }
            @Override
            public Adapter caseDiagramModel(IDiagramModel object) {
                return createDiagramModelAdapter();
            }
            @Override
            public Adapter caseDiagramModelReference(IDiagramModelReference object) {
                return createDiagramModelReferenceAdapter();
            }
            @Override
            public Adapter caseDiagramModelObject(IDiagramModelObject object) {
                return createDiagramModelObjectAdapter();
            }
            @Override
            public Adapter caseDiagramModelGroup(IDiagramModelGroup object) {
                return createDiagramModelGroupAdapter();
            }
            @Override
            public Adapter caseDiagramModelNote(IDiagramModelNote object) {
                return createDiagramModelNoteAdapter();
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
            public Adapter caseDiagramModelBendpoint(IDiagramModelBendpoint object) {
                return createDiagramModelBendpointAdapter();
            }
            @Override
            public Adapter caseLineObject(ILineObject object) {
                return createLineObjectAdapter();
            }
            @Override
            public Adapter caseFontAttribute(IFontAttribute object) {
                return createFontAttributeAdapter();
            }
            @Override
            public Adapter caseTextPosition(ITextPosition object) {
                return createTextPositionAdapter();
            }
            @Override
            public Adapter caseTextAlignment(ITextAlignment object) {
                return createTextAlignmentAdapter();
            }
            @Override
            public Adapter caseBorderObject(IBorderObject object) {
                return createBorderObjectAdapter();
            }
            @Override
            public Adapter caseBorderType(IBorderType object) {
                return createBorderTypeAdapter();
            }
            @Override
            public Adapter caseDiagramModelImageProvider(IDiagramModelImageProvider object) {
                return createDiagramModelImageProviderAdapter();
            }
            @Override
            public Adapter caseBounds(IBounds object) {
                return createBoundsAdapter();
            }
            @Override
            public Adapter caseLockable(ILockable object) {
                return createLockableAdapter();
            }
            @Override
            public Adapter caseIconic(IIconic object) {
                return createIconicAdapter();
            }
            @Override
            public Adapter caseArchimateDiagramModel(IArchimateDiagramModel object) {
                return createArchimateDiagramModelAdapter();
            }
            @Override
            public Adapter caseDiagramModelArchimateComponent(IDiagramModelArchimateComponent object) {
                return createDiagramModelArchimateComponentAdapter();
            }
            @Override
            public Adapter caseDiagramModelArchimateObject(IDiagramModelArchimateObject object) {
                return createDiagramModelArchimateObjectAdapter();
            }
            @Override
            public Adapter caseDiagramModelArchimateConnection(IDiagramModelArchimateConnection object) {
                return createDiagramModelArchimateConnectionAdapter();
            }
            @Override
            public Adapter caseSketchModel(ISketchModel object) {
                return createSketchModelAdapter();
            }
            @Override
            public Adapter caseSketchModelSticky(ISketchModelSticky object) {
                return createSketchModelStickyAdapter();
            }
            @Override
            public Adapter caseSketchModelActor(ISketchModelActor object) {
                return createSketchModelActorAdapter();
            }
            @Override
            public Adapter caseHintProvider(IHintProvider object) {
                return createHintProviderAdapter();
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFeature <em>Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFeature
     * @generated
     */
    public Adapter createFeatureAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IMetadata
     * @generated
     */
    public Adapter createMetadataAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IProfile <em>Profile</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IProfile
     * @generated
     */
    public Adapter createProfileAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IProfiles <em>Profiles</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IProfiles
     * @generated
     */
    public Adapter createProfilesAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFolderContainer <em>Folder Container</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFolderContainer
     * @generated
     */
    public Adapter createFolderContainerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IProperty <em>Property</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IProperty
     * @generated
     */
    public Adapter createPropertyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArchimateModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArchimateModel
     * @generated
     */
    public Adapter createArchimateModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IJunction <em>Junction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IJunction
     * @generated
     */
    public Adapter createJunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IResource <em>Resource</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IResource
     * @generated
     */
    public Adapter createResourceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFolder <em>Folder</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFolder
     * @generated
     */
    public Adapter createFolderAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArchimateConcept <em>Concept</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArchimateConcept
     * @generated
     */
    public Adapter createArchimateConceptAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArchimateElement <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArchimateElement
     * @generated
     */
    public Adapter createArchimateElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArtifact <em>Artifact</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArtifact
     * @generated
     */
    public Adapter createArtifactAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.INode <em>Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.INode
     * @generated
     */
    public Adapter createNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IOutcome <em>Outcome</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IOutcome
     * @generated
     */
    public Adapter createOutcomeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ISystemSoftware <em>System Software</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ISystemSoftware
     * @generated
     */
    public Adapter createSystemSoftwareAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyCollaboration <em>Technology Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyCollaboration
     * @generated
     */
    public Adapter createTechnologyCollaborationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyEvent <em>Technology Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyEvent
     * @generated
     */
    public Adapter createTechnologyEventAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyFunction <em>Technology Function</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyFunction
     * @generated
     */
    public Adapter createTechnologyFunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyInterface <em>Technology Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyInterface
     * @generated
     */
    public Adapter createTechnologyInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyInteraction <em>Technology Interaction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyInteraction
     * @generated
     */
    public Adapter createTechnologyInteractionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyObject <em>Technology Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyObject
     * @generated
     */
    public Adapter createTechnologyObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyProcess <em>Technology Process</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyProcess
     * @generated
     */
    public Adapter createTechnologyProcessAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyService <em>Technology Service</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyService
     * @generated
     */
    public Adapter createTechnologyServiceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDevice <em>Device</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDevice
     * @generated
     */
    public Adapter createDeviceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDistributionNetwork <em>Distribution Network</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDistributionNetwork
     * @generated
     */
    public Adapter createDistributionNetworkAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IEquipment <em>Equipment</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IEquipment
     * @generated
     */
    public Adapter createEquipmentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFacility <em>Facility</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFacility
     * @generated
     */
    public Adapter createFacilityAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IMotivationElement <em>Motivation Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IMotivationElement
     * @generated
     */
    public Adapter createMotivationElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IStakeholder <em>Stakeholder</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IStakeholder
     * @generated
     */
    public Adapter createStakeholderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDriver <em>Driver</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDriver
     * @generated
     */
    public Adapter createDriverAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IAssessment <em>Assessment</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IAssessment
     * @generated
     */
    public Adapter createAssessmentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IGoal <em>Goal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IGoal
     * @generated
     */
    public Adapter createGoalAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IGrouping <em>Grouping</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IGrouping
     * @generated
     */
    public Adapter createGroupingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IImplementationEvent <em>Implementation Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IImplementationEvent
     * @generated
     */
    public Adapter createImplementationEventAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IRequirement <em>Requirement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IRequirement
     * @generated
     */
    public Adapter createRequirementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IConstraint
     * @generated
     */
    public Adapter createConstraintAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ICourseOfAction <em>Course Of Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ICourseOfAction
     * @generated
     */
    public Adapter createCourseOfActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IPrinciple <em>Principle</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IPrinciple
     * @generated
     */
    public Adapter createPrincipleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IImplementationMigrationElement <em>Implementation Migration Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IImplementationMigrationElement
     * @generated
     */
    public Adapter createImplementationMigrationElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ICompositeElement <em>Composite Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ICompositeElement
     * @generated
     */
    public Adapter createCompositeElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBehaviorElement <em>Behavior Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBehaviorElement
     * @generated
     */
    public Adapter createBehaviorElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IStrategyBehaviorElement <em>Strategy Behavior Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IStrategyBehaviorElement
     * @generated
     */
    public Adapter createStrategyBehaviorElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IStructureElement <em>Structure Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IStructureElement
     * @generated
     */
    public Adapter createStructureElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IActiveStructureElement <em>Active Structure Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IActiveStructureElement
     * @generated
     */
    public Adapter createActiveStructureElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IPassiveStructureElement <em>Passive Structure Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IPassiveStructureElement
     * @generated
     */
    public Adapter createPassiveStructureElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IStructuralRelationship <em>Structural Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IStructuralRelationship
     * @generated
     */
    public Adapter createStructuralRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDependendencyRelationship <em>Dependendency Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDependendencyRelationship
     * @generated
     */
    public Adapter createDependendencyRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDynamicRelationship <em>Dynamic Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDynamicRelationship
     * @generated
     */
    public Adapter createDynamicRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IOtherRelationship <em>Other Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IOtherRelationship
     * @generated
     */
    public Adapter createOtherRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IStrategyElement <em>Strategy Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IStrategyElement
     * @generated
     */
    public Adapter createStrategyElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IWorkPackage <em>Work Package</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IWorkPackage
     * @generated
     */
    public Adapter createWorkPackageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDeliverable <em>Deliverable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDeliverable
     * @generated
     */
    public Adapter createDeliverableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IPlateau <em>Plateau</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IPlateau
     * @generated
     */
    public Adapter createPlateauAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IGap <em>Gap</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IGap
     * @generated
     */
    public Adapter createGapAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArchimateRelationship <em>Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArchimateRelationship
     * @generated
     */
    public Adapter createArchimateRelationshipAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IArchimateDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IArchimateDiagramModel
     * @generated
     */
    public Adapter createArchimateDiagramModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelArchimateComponent <em>Diagram Model Archimate Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelArchimateComponent
     * @generated
     */
    public Adapter createDiagramModelArchimateComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelReference <em>Diagram Model Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelReference
     * @generated
     */
    public Adapter createDiagramModelReferenceAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelArchimateObject
     * @generated
     */
    public Adapter createDiagramModelArchimateObjectAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelGroup <em>Diagram Model Group</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelGroup
     * @generated
     */
    public Adapter createDiagramModelGroupAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelNote <em>Diagram Model Note</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelNote
     * @generated
     */
    public Adapter createDiagramModelNoteAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelArchimateConnection
     * @generated
     */
    public Adapter createDiagramModelArchimateConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDiagramModelBendpoint <em>Diagram Model Bendpoint</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDiagramModelBendpoint
     * @generated
     */
    public Adapter createDiagramModelBendpointAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBorderType <em>Border Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBorderType
     * @generated
     */
    public Adapter createBorderTypeAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBounds <em>Bounds</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBounds
     * @generated
     */
    public Adapter createBoundsAdapter() {
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
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ISketchModel <em>Sketch Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ISketchModel
     * @generated
     */
    public Adapter createSketchModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ISketchModelSticky <em>Sketch Model Sticky</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ISketchModelSticky
     * @generated
     */
    public Adapter createSketchModelStickyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ISketchModelActor <em>Sketch Model Actor</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ISketchModelActor
     * @generated
     */
    public Adapter createSketchModelActorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IHintProvider <em>Hint Provider</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IHintProvider
     * @generated
     */
    public Adapter createHintProviderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessActor <em>Business Actor</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessActor
     * @generated
     */
    public Adapter createBusinessActorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessCollaboration <em>Business Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessCollaboration
     * @generated
     */
    public Adapter createBusinessCollaborationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IContract <em>Contract</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IContract
     * @generated
     */
    public Adapter createContractAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessEvent <em>Business Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessEvent
     * @generated
     */
    public Adapter createBusinessEventAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessFunction <em>Business Function</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessFunction
     * @generated
     */
    public Adapter createBusinessFunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessInteraction <em>Business Interaction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessInteraction
     * @generated
     */
    public Adapter createBusinessInteractionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessInterface <em>Business Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessInterface
     * @generated
     */
    public Adapter createBusinessInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IMeaning
     * @generated
     */
    public Adapter createMeaningAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessObject <em>Business Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessObject
     * @generated
     */
    public Adapter createBusinessObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessProcess <em>Business Process</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessProcess
     * @generated
     */
    public Adapter createBusinessProcessAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IProduct <em>Product</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IProduct
     * @generated
     */
    public Adapter createProductAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IRepresentation <em>Representation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IRepresentation
     * @generated
     */
    public Adapter createRepresentationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessRole <em>Business Role</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessRole
     * @generated
     */
    public Adapter createBusinessRoleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessService <em>Business Service</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessService
     * @generated
     */
    public Adapter createBusinessServiceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ICapability <em>Capability</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ICapability
     * @generated
     */
    public Adapter createCapabilityAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ICommunicationNetwork <em>Communication Network</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ICommunicationNetwork
     * @generated
     */
    public Adapter createCommunicationNetworkAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IValue
     * @generated
     */
    public Adapter createValueAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IValueStream <em>Value Stream</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IValueStream
     * @generated
     */
    public Adapter createValueStreamAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ILocation <em>Location</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ILocation
     * @generated
     */
    public Adapter createLocationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IMaterial <em>Material</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IMaterial
     * @generated
     */
    public Adapter createMaterialAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationComponent <em>Application Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationComponent
     * @generated
     */
    public Adapter createApplicationComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationEvent <em>Application Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationEvent
     * @generated
     */
    public Adapter createApplicationEventAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationFunction <em>Application Function</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationFunction
     * @generated
     */
    public Adapter createApplicationFunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationInteraction <em>Application Interaction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationInteraction
     * @generated
     */
    public Adapter createApplicationInteractionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationInterface <em>Application Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationInterface
     * @generated
     */
    public Adapter createApplicationInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationProcess <em>Application Process</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationProcess
     * @generated
     */
    public Adapter createApplicationProcessAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IDataObject <em>Data Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IDataObject
     * @generated
     */
    public Adapter createDataObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationService <em>Application Service</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationService
     * @generated
     */
    public Adapter createApplicationServiceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationCollaboration <em>Application Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationCollaboration
     * @generated
     */
    public Adapter createApplicationCollaborationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IAccessRelationship <em>Access Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IAccessRelationship
     * @generated
     */
    public Adapter createAccessRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IAggregationRelationship <em>Aggregation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IAggregationRelationship
     * @generated
     */
    public Adapter createAggregationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IAssignmentRelationship <em>Assignment Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IAssignmentRelationship
     * @generated
     */
    public Adapter createAssignmentRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IAssociationRelationship <em>Association Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IAssociationRelationship
     * @generated
     */
    public Adapter createAssociationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ICompositionRelationship <em>Composition Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ICompositionRelationship
     * @generated
     */
    public Adapter createCompositionRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IFlowRelationship <em>Flow Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IFlowRelationship
     * @generated
     */
    public Adapter createFlowRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITriggeringRelationship <em>Triggering Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITriggeringRelationship
     * @generated
     */
    public Adapter createTriggeringRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IInfluenceRelationship <em>Influence Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IInfluenceRelationship
     * @generated
     */
    public Adapter createInfluenceRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IRealizationRelationship <em>Realization Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IRealizationRelationship
     * @generated
     */
    public Adapter createRealizationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IServingRelationship <em>Serving Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IServingRelationship
     * @generated
     */
    public Adapter createServingRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ISpecializationRelationship <em>Specialization Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ISpecializationRelationship
     * @generated
     */
    public Adapter createSpecializationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IBusinessElement <em>Business Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IBusinessElement
     * @generated
     */
    public Adapter createBusinessElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IApplicationElement <em>Application Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IApplicationElement
     * @generated
     */
    public Adapter createApplicationElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.ITechnologyElement <em>Technology Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.ITechnologyElement
     * @generated
     */
    public Adapter createTechnologyElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IPhysicalElement <em>Physical Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IPhysicalElement
     * @generated
     */
    public Adapter createPhysicalElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link com.archimatetool.model.IPath <em>Path</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see com.archimatetool.model.IPath
     * @generated
     */
    public Adapter createPathAdapter() {
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

} //ArchimateAdapterFactory
