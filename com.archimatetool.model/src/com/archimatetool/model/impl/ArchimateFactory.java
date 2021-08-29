/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IApplicationCollaboration;
import com.archimatetool.model.IApplicationComponent;
import com.archimatetool.model.IApplicationEvent;
import com.archimatetool.model.IApplicationFunction;
import com.archimatetool.model.IApplicationInteraction;
import com.archimatetool.model.IApplicationInterface;
import com.archimatetool.model.IApplicationProcess;
import com.archimatetool.model.IApplicationService;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArtifact;
import com.archimatetool.model.IAssessment;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessCollaboration;
import com.archimatetool.model.IBusinessEvent;
import com.archimatetool.model.IBusinessFunction;
import com.archimatetool.model.IBusinessInteraction;
import com.archimatetool.model.IBusinessInterface;
import com.archimatetool.model.IBusinessObject;
import com.archimatetool.model.IBusinessProcess;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IBusinessService;
import com.archimatetool.model.ICapability;
import com.archimatetool.model.ICommunicationNetwork;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IConstraint;
import com.archimatetool.model.IContract;
import com.archimatetool.model.ICourseOfAction;
import com.archimatetool.model.IDataObject;
import com.archimatetool.model.IDeliverable;
import com.archimatetool.model.IDevice;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IDistributionNetwork;
import com.archimatetool.model.IDriver;
import com.archimatetool.model.IEquipment;
import com.archimatetool.model.IFacility;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFlowRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IGap;
import com.archimatetool.model.IGoal;
import com.archimatetool.model.IGrouping;
import com.archimatetool.model.IImplementationEvent;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.ILocation;
import com.archimatetool.model.IMaterial;
import com.archimatetool.model.IMeaning;
import com.archimatetool.model.IMetadata;
import com.archimatetool.model.INode;
import com.archimatetool.model.IOutcome;
import com.archimatetool.model.IPath;
import com.archimatetool.model.IPlateau;
import com.archimatetool.model.IPrinciple;
import com.archimatetool.model.IProduct;
import com.archimatetool.model.IProfile;
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
import com.archimatetool.model.ISystemSoftware;
import com.archimatetool.model.ITechnologyCollaboration;
import com.archimatetool.model.ITechnologyEvent;
import com.archimatetool.model.ITechnologyFunction;
import com.archimatetool.model.ITechnologyInteraction;
import com.archimatetool.model.ITechnologyInterface;
import com.archimatetool.model.ITechnologyProcess;
import com.archimatetool.model.ITechnologyService;
import com.archimatetool.model.ITriggeringRelationship;
import com.archimatetool.model.IValue;
import com.archimatetool.model.IValueStream;
import com.archimatetool.model.IWorkPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ArchimateFactory extends EFactoryImpl implements IArchimateFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static IArchimateFactory init() {
        try {
            IArchimateFactory theArchimateFactory = (IArchimateFactory)EPackage.Registry.INSTANCE.getEFactory(IArchimatePackage.eNS_URI);
            if (theArchimateFactory != null) {
                return theArchimateFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ArchimateFactory();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArchimateFactory() {
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
            case IArchimatePackage.PROPERTY: return createProperty();
            case IArchimatePackage.FEATURE: return createFeature();
            case IArchimatePackage.METADATA: return createMetadata();
            case IArchimatePackage.PROFILE: return createProfile();
            case IArchimatePackage.FOLDER: return createFolder();
            case IArchimatePackage.ARCHIMATE_MODEL: return createArchimateModel();
            case IArchimatePackage.JUNCTION: return createJunction();
            case IArchimatePackage.APPLICATION_COLLABORATION: return createApplicationCollaboration();
            case IArchimatePackage.APPLICATION_COMPONENT: return createApplicationComponent();
            case IArchimatePackage.APPLICATION_EVENT: return createApplicationEvent();
            case IArchimatePackage.APPLICATION_FUNCTION: return createApplicationFunction();
            case IArchimatePackage.APPLICATION_INTERACTION: return createApplicationInteraction();
            case IArchimatePackage.APPLICATION_INTERFACE: return createApplicationInterface();
            case IArchimatePackage.APPLICATION_PROCESS: return createApplicationProcess();
            case IArchimatePackage.APPLICATION_SERVICE: return createApplicationService();
            case IArchimatePackage.ARTIFACT: return createArtifact();
            case IArchimatePackage.ASSESSMENT: return createAssessment();
            case IArchimatePackage.BUSINESS_ACTOR: return createBusinessActor();
            case IArchimatePackage.BUSINESS_COLLABORATION: return createBusinessCollaboration();
            case IArchimatePackage.BUSINESS_EVENT: return createBusinessEvent();
            case IArchimatePackage.BUSINESS_FUNCTION: return createBusinessFunction();
            case IArchimatePackage.BUSINESS_INTERACTION: return createBusinessInteraction();
            case IArchimatePackage.BUSINESS_INTERFACE: return createBusinessInterface();
            case IArchimatePackage.BUSINESS_OBJECT: return createBusinessObject();
            case IArchimatePackage.BUSINESS_PROCESS: return createBusinessProcess();
            case IArchimatePackage.BUSINESS_ROLE: return createBusinessRole();
            case IArchimatePackage.BUSINESS_SERVICE: return createBusinessService();
            case IArchimatePackage.CAPABILITY: return createCapability();
            case IArchimatePackage.COMMUNICATION_NETWORK: return createCommunicationNetwork();
            case IArchimatePackage.CONTRACT: return createContract();
            case IArchimatePackage.CONSTRAINT: return createConstraint();
            case IArchimatePackage.COURSE_OF_ACTION: return createCourseOfAction();
            case IArchimatePackage.DATA_OBJECT: return createDataObject();
            case IArchimatePackage.DELIVERABLE: return createDeliverable();
            case IArchimatePackage.DEVICE: return createDevice();
            case IArchimatePackage.DISTRIBUTION_NETWORK: return createDistributionNetwork();
            case IArchimatePackage.DRIVER: return createDriver();
            case IArchimatePackage.EQUIPMENT: return createEquipment();
            case IArchimatePackage.FACILITY: return createFacility();
            case IArchimatePackage.GAP: return createGap();
            case IArchimatePackage.GOAL: return createGoal();
            case IArchimatePackage.GROUPING: return createGrouping();
            case IArchimatePackage.IMPLEMENTATION_EVENT: return createImplementationEvent();
            case IArchimatePackage.LOCATION: return createLocation();
            case IArchimatePackage.MATERIAL: return createMaterial();
            case IArchimatePackage.MEANING: return createMeaning();
            case IArchimatePackage.NODE: return createNode();
            case IArchimatePackage.OUTCOME: return createOutcome();
            case IArchimatePackage.PATH: return createPath();
            case IArchimatePackage.PLATEAU: return createPlateau();
            case IArchimatePackage.PRINCIPLE: return createPrinciple();
            case IArchimatePackage.PRODUCT: return createProduct();
            case IArchimatePackage.REPRESENTATION: return createRepresentation();
            case IArchimatePackage.RESOURCE: return createResource();
            case IArchimatePackage.REQUIREMENT: return createRequirement();
            case IArchimatePackage.STAKEHOLDER: return createStakeholder();
            case IArchimatePackage.SYSTEM_SOFTWARE: return createSystemSoftware();
            case IArchimatePackage.TECHNOLOGY_COLLABORATION: return createTechnologyCollaboration();
            case IArchimatePackage.TECHNOLOGY_EVENT: return createTechnologyEvent();
            case IArchimatePackage.TECHNOLOGY_FUNCTION: return createTechnologyFunction();
            case IArchimatePackage.TECHNOLOGY_INTERFACE: return createTechnologyInterface();
            case IArchimatePackage.TECHNOLOGY_INTERACTION: return createTechnologyInteraction();
            case IArchimatePackage.TECHNOLOGY_PROCESS: return createTechnologyProcess();
            case IArchimatePackage.TECHNOLOGY_SERVICE: return createTechnologyService();
            case IArchimatePackage.VALUE: return createValue();
            case IArchimatePackage.VALUE_STREAM: return createValueStream();
            case IArchimatePackage.WORK_PACKAGE: return createWorkPackage();
            case IArchimatePackage.ACCESS_RELATIONSHIP: return createAccessRelationship();
            case IArchimatePackage.AGGREGATION_RELATIONSHIP: return createAggregationRelationship();
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP: return createAssignmentRelationship();
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP: return createAssociationRelationship();
            case IArchimatePackage.COMPOSITION_RELATIONSHIP: return createCompositionRelationship();
            case IArchimatePackage.FLOW_RELATIONSHIP: return createFlowRelationship();
            case IArchimatePackage.INFLUENCE_RELATIONSHIP: return createInfluenceRelationship();
            case IArchimatePackage.REALIZATION_RELATIONSHIP: return createRealizationRelationship();
            case IArchimatePackage.SERVING_RELATIONSHIP: return createServingRelationship();
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP: return createSpecializationRelationship();
            case IArchimatePackage.TRIGGERING_RELATIONSHIP: return createTriggeringRelationship();
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE: return createDiagramModelReference();
            case IArchimatePackage.DIAGRAM_MODEL_GROUP: return createDiagramModelGroup();
            case IArchimatePackage.DIAGRAM_MODEL_NOTE: return createDiagramModelNote();
            case IArchimatePackage.DIAGRAM_MODEL_IMAGE: return createDiagramModelImage();
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION: return createDiagramModelConnection();
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT: return createDiagramModelBendpoint();
            case IArchimatePackage.BOUNDS: return createBounds();
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL: return createArchimateDiagramModel();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT: return createDiagramModelArchimateObject();
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION: return createDiagramModelArchimateConnection();
            case IArchimatePackage.SKETCH_MODEL: return createSketchModel();
            case IArchimatePackage.SKETCH_MODEL_STICKY: return createSketchModelSticky();
            case IArchimatePackage.SKETCH_MODEL_ACTOR: return createSketchModelActor();
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
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case IArchimatePackage.FOLDER_TYPE:
                return createFolderTypeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case IArchimatePackage.FOLDER_TYPE:
                return convertFolderTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IProperty createProperty() {
        Property property = new Property();
        return property;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IFeature createFeature() {
        Feature feature = new Feature();
        return feature;
    }

    /* (non-Javadoc)
     * @see com.archimatetool.model.IArchimateFactory#createProperty(java.lang.String, java.lang.String)
     */
    @Override
    public IProperty createProperty(String key, String value) {
        IProperty property = createProperty();
        property.setKey(key);
        property.setValue(value);
        return property;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IMetadata createMetadata() {
        Metadata metadata = new Metadata();
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IProfile createProfile() {
        Profile profile = new Profile();
        return profile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IArchimateModel createArchimateModel() {
        ArchimateModel archimateModel = new ArchimateModel();
        return archimateModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IJunction createJunction() {
        Junction junction = new Junction();
        return junction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IResource createResource() {
        Resource resource = new Resource();
        return resource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IFolder createFolder() {
        Folder folder = new Folder();
        return folder;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessActor createBusinessActor() {
        BusinessActor businessActor = new BusinessActor();
        return businessActor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessCollaboration createBusinessCollaboration() {
        BusinessCollaboration businessCollaboration = new BusinessCollaboration();
        return businessCollaboration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IContract createContract() {
        Contract contract = new Contract();
        return contract;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessEvent createBusinessEvent() {
        BusinessEvent businessEvent = new BusinessEvent();
        return businessEvent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessFunction createBusinessFunction() {
        BusinessFunction businessFunction = new BusinessFunction();
        return businessFunction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessInteraction createBusinessInteraction() {
        BusinessInteraction businessInteraction = new BusinessInteraction();
        return businessInteraction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessInterface createBusinessInterface() {
        BusinessInterface businessInterface = new BusinessInterface();
        return businessInterface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IMeaning createMeaning() {
        Meaning meaning = new Meaning();
        return meaning;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessObject createBusinessObject() {
        BusinessObject businessObject = new BusinessObject();
        return businessObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessProcess createBusinessProcess() {
        BusinessProcess businessProcess = new BusinessProcess();
        return businessProcess;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IProduct createProduct() {
        Product product = new Product();
        return product;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IRepresentation createRepresentation() {
        Representation representation = new Representation();
        return representation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessRole createBusinessRole() {
        BusinessRole businessRole = new BusinessRole();
        return businessRole;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBusinessService createBusinessService() {
        BusinessService businessService = new BusinessService();
        return businessService;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICapability createCapability() {
        Capability capability = new Capability();
        return capability;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICommunicationNetwork createCommunicationNetwork() {
        CommunicationNetwork communicationNetwork = new CommunicationNetwork();
        return communicationNetwork;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IValue createValue() {
        Value value = new Value();
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IValueStream createValueStream() {
        ValueStream valueStream = new ValueStream();
        return valueStream;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ILocation createLocation() {
        Location location = new Location();
        return location;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IMaterial createMaterial() {
        Material material = new Material();
        return material;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationComponent createApplicationComponent() {
        ApplicationComponent applicationComponent = new ApplicationComponent();
        return applicationComponent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationEvent createApplicationEvent() {
        ApplicationEvent applicationEvent = new ApplicationEvent();
        return applicationEvent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationFunction createApplicationFunction() {
        ApplicationFunction applicationFunction = new ApplicationFunction();
        return applicationFunction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationInteraction createApplicationInteraction() {
        ApplicationInteraction applicationInteraction = new ApplicationInteraction();
        return applicationInteraction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationInterface createApplicationInterface() {
        ApplicationInterface applicationInterface = new ApplicationInterface();
        return applicationInterface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationProcess createApplicationProcess() {
        ApplicationProcess applicationProcess = new ApplicationProcess();
        return applicationProcess;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDataObject createDataObject() {
        DataObject dataObject = new DataObject();
        return dataObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationService createApplicationService() {
        ApplicationService applicationService = new ApplicationService();
        return applicationService;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IArtifact createArtifact() {
        Artifact artifact = new Artifact();
        return artifact;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public INode createNode() {
        Node node = new Node();
        return node;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IOutcome createOutcome() {
        Outcome outcome = new Outcome();
        return outcome;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ISystemSoftware createSystemSoftware() {
        SystemSoftware systemSoftware = new SystemSoftware();
        return systemSoftware;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyCollaboration createTechnologyCollaboration() {
        TechnologyCollaboration technologyCollaboration = new TechnologyCollaboration();
        return technologyCollaboration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyEvent createTechnologyEvent() {
        TechnologyEvent technologyEvent = new TechnologyEvent();
        return technologyEvent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyFunction createTechnologyFunction() {
        TechnologyFunction technologyFunction = new TechnologyFunction();
        return technologyFunction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyInterface createTechnologyInterface() {
        TechnologyInterface technologyInterface = new TechnologyInterface();
        return technologyInterface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyInteraction createTechnologyInteraction() {
        TechnologyInteraction technologyInteraction = new TechnologyInteraction();
        return technologyInteraction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyProcess createTechnologyProcess() {
        TechnologyProcess technologyProcess = new TechnologyProcess();
        return technologyProcess;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITechnologyService createTechnologyService() {
        TechnologyService technologyService = new TechnologyService();
        return technologyService;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDevice createDevice() {
        Device device = new Device();
        return device;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDistributionNetwork createDistributionNetwork() {
        DistributionNetwork distributionNetwork = new DistributionNetwork();
        return distributionNetwork;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IEquipment createEquipment() {
        Equipment equipment = new Equipment();
        return equipment;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IFacility createFacility() {
        Facility facility = new Facility();
        return facility;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IStakeholder createStakeholder() {
        Stakeholder stakeholder = new Stakeholder();
        return stakeholder;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDriver createDriver() {
        Driver driver = new Driver();
        return driver;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IAssessment createAssessment() {
        Assessment assessment = new Assessment();
        return assessment;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IGoal createGoal() {
        Goal goal = new Goal();
        return goal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IGrouping createGrouping() {
        Grouping grouping = new Grouping();
        return grouping;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IImplementationEvent createImplementationEvent() {
        ImplementationEvent implementationEvent = new ImplementationEvent();
        return implementationEvent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IRequirement createRequirement() {
        Requirement requirement = new Requirement();
        return requirement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IConstraint createConstraint() {
        Constraint constraint = new Constraint();
        return constraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICourseOfAction createCourseOfAction() {
        CourseOfAction courseOfAction = new CourseOfAction();
        return courseOfAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IPrinciple createPrinciple() {
        Principle principle = new Principle();
        return principle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IWorkPackage createWorkPackage() {
        WorkPackage workPackage = new WorkPackage();
        return workPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDeliverable createDeliverable() {
        Deliverable deliverable = new Deliverable();
        return deliverable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IPlateau createPlateau() {
        Plateau plateau = new Plateau();
        return plateau;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IGap createGap() {
        Gap gap = new Gap();
        return gap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IArchimateDiagramModel createArchimateDiagramModel() {
        ArchimateDiagramModel archimateDiagramModel = new ArchimateDiagramModel();
        return archimateDiagramModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelReference createDiagramModelReference() {
        DiagramModelReference diagramModelReference = new DiagramModelReference();
        return diagramModelReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelArchimateObject createDiagramModelArchimateObject() {
        DiagramModelArchimateObject diagramModelArchimateObject = new DiagramModelArchimateObject();
        return diagramModelArchimateObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelGroup createDiagramModelGroup() {
        DiagramModelGroup diagramModelGroup = new DiagramModelGroup();
        return diagramModelGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelNote createDiagramModelNote() {
        DiagramModelNote diagramModelNote = new DiagramModelNote();
        return diagramModelNote;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelImage createDiagramModelImage() {
        DiagramModelImage diagramModelImage = new DiagramModelImage();
        return diagramModelImage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelConnection createDiagramModelConnection() {
        DiagramModelConnection diagramModelConnection = new DiagramModelConnection();
        return diagramModelConnection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelArchimateConnection createDiagramModelArchimateConnection() {
        DiagramModelArchimateConnection diagramModelArchimateConnection = new DiagramModelArchimateConnection();
        return diagramModelArchimateConnection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IDiagramModelBendpoint createDiagramModelBendpoint() {
        DiagramModelBendpoint diagramModelBendpoint = new DiagramModelBendpoint();
        return diagramModelBendpoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IBounds createBounds() {
        Bounds bounds = new Bounds();
        return bounds;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ISketchModel createSketchModel() {
        SketchModel sketchModel = new SketchModel();
        return sketchModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ISketchModelSticky createSketchModelSticky() {
        SketchModelSticky sketchModelSticky = new SketchModelSticky();
        return sketchModelSticky;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ISketchModelActor createSketchModelActor() {
        SketchModelActor sketchModelActor = new SketchModelActor();
        return sketchModelActor;
    }

    /* (non-Javadoc)
     * @see com.archimatetool.model.IArchimateFactory#createBounds(int, int, int, int)
     */
    @Override
    public IBounds createBounds(int x, int y, int width, int height) {
        IBounds bounds = createBounds();
        bounds.setX(x);
        bounds.setY(y);
        bounds.setWidth(width);
        bounds.setHeight(height);
        return bounds;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FolderType createFolderTypeFromString(EDataType eDataType, String initialValue) {
        FolderType result = FolderType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFolderTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IApplicationCollaboration createApplicationCollaboration() {
        ApplicationCollaboration applicationCollaboration = new ApplicationCollaboration();
        return applicationCollaboration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IPath createPath() {
        Path path = new Path();
        return path;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IAccessRelationship createAccessRelationship() {
        AccessRelationship accessRelationship = new AccessRelationship();
        return accessRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IAggregationRelationship createAggregationRelationship() {
        AggregationRelationship aggregationRelationship = new AggregationRelationship();
        return aggregationRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IAssignmentRelationship createAssignmentRelationship() {
        AssignmentRelationship assignmentRelationship = new AssignmentRelationship();
        return assignmentRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IAssociationRelationship createAssociationRelationship() {
        AssociationRelationship associationRelationship = new AssociationRelationship();
        return associationRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ICompositionRelationship createCompositionRelationship() {
        CompositionRelationship compositionRelationship = new CompositionRelationship();
        return compositionRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IFlowRelationship createFlowRelationship() {
        FlowRelationship flowRelationship = new FlowRelationship();
        return flowRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ITriggeringRelationship createTriggeringRelationship() {
        TriggeringRelationship triggeringRelationship = new TriggeringRelationship();
        return triggeringRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IInfluenceRelationship createInfluenceRelationship() {
        InfluenceRelationship influenceRelationship = new InfluenceRelationship();
        return influenceRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IRealizationRelationship createRealizationRelationship() {
        RealizationRelationship realizationRelationship = new RealizationRelationship();
        return realizationRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IServingRelationship createServingRelationship() {
        ServingRelationship servingRelationship = new ServingRelationship();
        return servingRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ISpecializationRelationship createSpecializationRelationship() {
        SpecializationRelationship specializationRelationship = new SpecializationRelationship();
        return specializationRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IArchimatePackage getArchimatePackage() {
        return (IArchimatePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static IArchimatePackage getPackage() {
        return IArchimatePackage.eINSTANCE;
    }

} //ArchimateFactory
