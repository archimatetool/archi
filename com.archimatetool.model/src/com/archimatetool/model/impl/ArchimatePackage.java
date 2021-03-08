/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import com.archimatetool.model.FolderType;
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
import com.archimatetool.model.IArchimateFactory;
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
    private EClass featureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featuresEClass = null;

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
    private EClass profileEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass profilesEClass = null;

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
    private EClass junctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass resourceEClass = null;

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
    private EClass archimateModelObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass archimateConceptEClass = null;

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
    private EClass artifactEClass = null;

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
    private EClass outcomeEClass = null;

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
    private EClass technologyCollaborationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyEventEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyFunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyInterfaceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyInteractionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyProcessEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyServiceEClass = null;

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
    private EClass distributionNetworkEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass equipmentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass facilityEClass = null;

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
    private EClass groupingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass implementationEventEClass = null;

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
    private EClass courseOfActionEClass = null;

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
    private EClass compositeElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass behaviorElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass strategyBehaviorElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass structureElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass activeStructureElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass passiveStructureElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass structuralRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dependendencyRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dynamicRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass otherRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass strategyElementEClass = null;

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
    private EClass archimateRelationshipEClass = null;

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
    private EClass diagramModelArchimateComponentEClass = null;

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
    private EClass connectableEClass = null;

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
    private EClass textPositionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass textAlignmentEClass = null;

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
    private EClass borderTypeEClass = null;

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
    private EClass iconicEClass = null;

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
    private EClass capabilityEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass communicationNetworkEClass = null;

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
    private EClass valueStreamEClass = null;

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
    private EClass materialEClass = null;

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
    private EClass applicationEventEClass = null;

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
    private EClass applicationProcessEClass = null;

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
    private EClass triggeringRelationshipEClass = null;

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
    private EClass realizationRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass servingRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specializationRelationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass businessElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass applicationElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass technologyElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass physicalElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass pathEClass = null;

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
        Object registeredArchimatePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        ArchimatePackage theArchimatePackage = registeredArchimatePackage instanceof ArchimatePackage ? (ArchimatePackage)registeredArchimatePackage : new ArchimatePackage();

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
    @Override
    public EClass getAdapter() {
        return adapterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIdentifier() {
        return identifierEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIdentifier_Id() {
        return (EAttribute)identifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProperties() {
        return propertiesEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProperties_Properties() {
        return (EReference)propertiesEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeature() {
        return featureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_Name() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_Value() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatures() {
        return featuresEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatures_Features() {
        return (EReference)featuresEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMetadata() {
        return metadataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMetadata_Entries() {
        return (EReference)metadataEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNameable() {
        return nameableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getNameable_Name() {
        return (EAttribute)nameableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTextContent() {
        return textContentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTextContent_Content() {
        return (EAttribute)textContentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDocumentable() {
        return documentableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDocumentable_Documentation() {
        return (EAttribute)documentableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCloneable() {
        return cloneableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProfile() {
        return profileEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getProfile_Specialization() {
        return (EAttribute)profileEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getProfile_ConceptType() {
        return (EAttribute)profileEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProfiles() {
        return profilesEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProfiles_Profiles() {
        return (EReference)profilesEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFolderContainer() {
        return folderContainerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFolderContainer_Folders() {
        return (EReference)folderContainerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProperty() {
        return propertyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getProperty_Key() {
        return (EAttribute)propertyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getProperty_Value() {
        return (EAttribute)propertyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchimateModel() {
        return archimateModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getArchimateModel_Purpose() {
        return (EAttribute)archimateModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getArchimateModel_File() {
        return (EAttribute)archimateModelEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getArchimateModel_Version() {
        return (EAttribute)archimateModelEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchimateModel_Metadata() {
        return (EReference)archimateModelEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchimateModel_Profiles() {
        return (EReference)archimateModelEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getJunction() {
        return junctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getJunction_Type() {
        return (EAttribute)junctionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getResource() {
        return resourceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFolder() {
        return folderEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFolder_Elements() {
        return (EReference)folderEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFolder_Type() {
        return (EAttribute)folderEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchimateModelObject() {
        return archimateModelObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchimateConcept() {
        return archimateConceptEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchimateElement() {
        return archimateElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArtifact() {
        return artifactEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNode() {
        return nodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getOutcome() {
        return outcomeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSystemSoftware() {
        return systemSoftwareEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyCollaboration() {
        return technologyCollaborationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyEvent() {
        return technologyEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyFunction() {
        return technologyFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyInterface() {
        return technologyInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyInteraction() {
        return technologyInteractionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyObject() {
        return technologyObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyProcess() {
        return technologyProcessEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyService() {
        return technologyServiceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDevice() {
        return deviceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDistributionNetwork() {
        return distributionNetworkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEquipment() {
        return equipmentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFacility() {
        return facilityEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMotivationElement() {
        return motivationElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStakeholder() {
        return stakeholderEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDriver() {
        return driverEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssessment() {
        return assessmentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getGoal() {
        return goalEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getGrouping() {
        return groupingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getImplementationEvent() {
        return implementationEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRequirement() {
        return requirementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConstraint() {
        return constraintEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCourseOfAction() {
        return courseOfActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPrinciple() {
        return principleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getImplementationMigrationElement() {
        return implementationMigrationElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompositeElement() {
        return compositeElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBehaviorElement() {
        return behaviorElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStrategyBehaviorElement() {
        return strategyBehaviorElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStructureElement() {
        return structureElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getActiveStructureElement() {
        return activeStructureElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPassiveStructureElement() {
        return passiveStructureElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStructuralRelationship() {
        return structuralRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDependendencyRelationship() {
        return dependendencyRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDynamicRelationship() {
        return dynamicRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getOtherRelationship() {
        return otherRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStrategyElement() {
        return strategyElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getWorkPackage() {
        return workPackageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDeliverable() {
        return deliverableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPlateau() {
        return plateauEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getGap() {
        return gapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchimateRelationship() {
        return archimateRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchimateRelationship_Source() {
        return (EReference)archimateRelationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getArchimateRelationship_Target() {
        return (EReference)archimateRelationshipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModel() {
        return diagramModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModel_ConnectionRouterType() {
        return (EAttribute)diagramModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getArchimateDiagramModel() {
        return archimateDiagramModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getArchimateDiagramModel_Viewpoint() {
        return (EAttribute)archimateDiagramModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelArchimateComponent() {
        return diagramModelArchimateComponentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelReference() {
        return diagramModelReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelReference_ReferencedModel() {
        return (EReference)diagramModelReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelComponent() {
        return diagramModelComponentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConnectable() {
        return connectableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnectable_SourceConnections() {
        return (EReference)connectableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnectable_TargetConnections() {
        return (EReference)connectableEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelObject() {
        return diagramModelObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelObject_Bounds() {
        return (EReference)diagramModelObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelObject_FillColor() {
        return (EAttribute)diagramModelObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelObject_Alpha() {
        return (EAttribute)diagramModelObjectEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelArchimateObject() {
        return diagramModelArchimateObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelArchimateObject_ArchimateElement() {
        return (EReference)diagramModelArchimateObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelArchimateObject_Type() {
        return (EAttribute)diagramModelArchimateObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelContainer() {
        return diagramModelContainerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelContainer_Children() {
        return (EReference)diagramModelContainerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelGroup() {
        return diagramModelGroupEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelNote() {
        return diagramModelNoteEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelImage() {
        return diagramModelImageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelConnection() {
        return diagramModelConnectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelConnection_Text() {
        return (EAttribute)diagramModelConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelConnection_TextPosition() {
        return (EAttribute)diagramModelConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelConnection_Source() {
        return (EReference)diagramModelConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelConnection_Target() {
        return (EReference)diagramModelConnectionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelConnection_Bendpoints() {
        return (EReference)diagramModelConnectionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelConnection_Type() {
        return (EAttribute)diagramModelConnectionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelArchimateConnection() {
        return diagramModelArchimateConnectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiagramModelArchimateConnection_ArchimateRelationship() {
        return (EReference)diagramModelArchimateConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelBendpoint() {
        return diagramModelBendpointEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelBendpoint_StartX() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelBendpoint_StartY() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelBendpoint_EndX() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelBendpoint_EndY() {
        return (EAttribute)diagramModelBendpointEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLineObject() {
        return lineObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLineObject_LineWidth() {
        return (EAttribute)lineObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLineObject_LineColor() {
        return (EAttribute)lineObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFontAttribute() {
        return fontAttributeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFontAttribute_Font() {
        return (EAttribute)fontAttributeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFontAttribute_FontColor() {
        return (EAttribute)fontAttributeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTextPosition() {
        return textPositionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTextPosition_TextPosition() {
        return (EAttribute)textPositionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTextAlignment() {
        return textAlignmentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTextAlignment_TextAlignment() {
        return (EAttribute)textAlignmentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBorderObject() {
        return borderObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBorderObject_BorderColor() {
        return (EAttribute)borderObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBorderType() {
        return borderTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBorderType_BorderType() {
        return (EAttribute)borderTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiagramModelImageProvider() {
        return diagramModelImageProviderEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDiagramModelImageProvider_ImagePath() {
        return (EAttribute)diagramModelImageProviderEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBounds() {
        return boundsEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBounds_X() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBounds_Y() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBounds_Width() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBounds_Height() {
        return (EAttribute)boundsEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLockable() {
        return lockableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLockable_Locked() {
        return (EAttribute)lockableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIconic() {
        return iconicEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIconic_ImagePosition() {
        return (EAttribute)iconicEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSketchModel() {
        return sketchModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getSketchModel_Background() {
        return (EAttribute)sketchModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSketchModelSticky() {
        return sketchModelStickyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSketchModelActor() {
        return sketchModelActorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getFolderType() {
        return folderTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getFile() {
        return fileEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessActor() {
        return businessActorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessCollaboration() {
        return businessCollaborationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getContract() {
        return contractEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessEvent() {
        return businessEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessFunction() {
        return businessFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessInteraction() {
        return businessInteractionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessInterface() {
        return businessInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMeaning() {
        return meaningEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessObject() {
        return businessObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessProcess() {
        return businessProcessEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProduct() {
        return productEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRepresentation() {
        return representationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessRole() {
        return businessRoleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessService() {
        return businessServiceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCapability() {
        return capabilityEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCommunicationNetwork() {
        return communicationNetworkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getValue() {
        return valueEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getValueStream() {
        return valueStreamEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLocation() {
        return locationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMaterial() {
        return materialEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationComponent() {
        return applicationComponentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationEvent() {
        return applicationEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationFunction() {
        return applicationFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationInteraction() {
        return applicationInteractionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationInterface() {
        return applicationInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationProcess() {
        return applicationProcessEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDataObject() {
        return dataObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationService() {
        return applicationServiceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationCollaboration() {
        return applicationCollaborationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAccessRelationship() {
        return accessRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getAccessRelationship_AccessType() {
        return (EAttribute)accessRelationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAggregationRelationship() {
        return aggregationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssignmentRelationship() {
        return assignmentRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssociationRelationship() {
        return associationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getAssociationRelationship_Directed() {
        return (EAttribute)associationRelationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompositionRelationship() {
        return compositionRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFlowRelationship() {
        return flowRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTriggeringRelationship() {
        return triggeringRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInfluenceRelationship() {
        return influenceRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getInfluenceRelationship_Strength() {
        return (EAttribute)influenceRelationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRealizationRelationship() {
        return realizationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getServingRelationship() {
        return servingRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSpecializationRelationship() {
        return specializationRelationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBusinessElement() {
        return businessElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getApplicationElement() {
        return applicationElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTechnologyElement() {
        return technologyElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPhysicalElement() {
        return physicalElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPath() {
        return pathEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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

        featureEClass = createEClass(FEATURE);
        createEAttribute(featureEClass, FEATURE__NAME);
        createEAttribute(featureEClass, FEATURE__VALUE);

        featuresEClass = createEClass(FEATURES);
        createEReference(featuresEClass, FEATURES__FEATURES);

        metadataEClass = createEClass(METADATA);
        createEReference(metadataEClass, METADATA__ENTRIES);

        nameableEClass = createEClass(NAMEABLE);
        createEAttribute(nameableEClass, NAMEABLE__NAME);

        textContentEClass = createEClass(TEXT_CONTENT);
        createEAttribute(textContentEClass, TEXT_CONTENT__CONTENT);

        documentableEClass = createEClass(DOCUMENTABLE);
        createEAttribute(documentableEClass, DOCUMENTABLE__DOCUMENTATION);

        cloneableEClass = createEClass(CLONEABLE);

        profileEClass = createEClass(PROFILE);
        createEAttribute(profileEClass, PROFILE__SPECIALIZATION);
        createEAttribute(profileEClass, PROFILE__CONCEPT_TYPE);

        profilesEClass = createEClass(PROFILES);
        createEReference(profilesEClass, PROFILES__PROFILES);

        folderContainerEClass = createEClass(FOLDER_CONTAINER);
        createEReference(folderContainerEClass, FOLDER_CONTAINER__FOLDERS);

        folderEClass = createEClass(FOLDER);
        createEReference(folderEClass, FOLDER__ELEMENTS);
        createEAttribute(folderEClass, FOLDER__TYPE);

        archimateModelObjectEClass = createEClass(ARCHIMATE_MODEL_OBJECT);

        archimateConceptEClass = createEClass(ARCHIMATE_CONCEPT);

        archimateElementEClass = createEClass(ARCHIMATE_ELEMENT);

        archimateRelationshipEClass = createEClass(ARCHIMATE_RELATIONSHIP);
        createEReference(archimateRelationshipEClass, ARCHIMATE_RELATIONSHIP__SOURCE);
        createEReference(archimateRelationshipEClass, ARCHIMATE_RELATIONSHIP__TARGET);

        strategyElementEClass = createEClass(STRATEGY_ELEMENT);

        businessElementEClass = createEClass(BUSINESS_ELEMENT);

        applicationElementEClass = createEClass(APPLICATION_ELEMENT);

        technologyElementEClass = createEClass(TECHNOLOGY_ELEMENT);

        technologyObjectEClass = createEClass(TECHNOLOGY_OBJECT);

        physicalElementEClass = createEClass(PHYSICAL_ELEMENT);

        motivationElementEClass = createEClass(MOTIVATION_ELEMENT);

        implementationMigrationElementEClass = createEClass(IMPLEMENTATION_MIGRATION_ELEMENT);

        compositeElementEClass = createEClass(COMPOSITE_ELEMENT);

        behaviorElementEClass = createEClass(BEHAVIOR_ELEMENT);

        strategyBehaviorElementEClass = createEClass(STRATEGY_BEHAVIOR_ELEMENT);

        structureElementEClass = createEClass(STRUCTURE_ELEMENT);

        activeStructureElementEClass = createEClass(ACTIVE_STRUCTURE_ELEMENT);

        passiveStructureElementEClass = createEClass(PASSIVE_STRUCTURE_ELEMENT);

        structuralRelationshipEClass = createEClass(STRUCTURAL_RELATIONSHIP);

        dependendencyRelationshipEClass = createEClass(DEPENDENDENCY_RELATIONSHIP);

        dynamicRelationshipEClass = createEClass(DYNAMIC_RELATIONSHIP);

        otherRelationshipEClass = createEClass(OTHER_RELATIONSHIP);

        archimateModelEClass = createEClass(ARCHIMATE_MODEL);
        createEAttribute(archimateModelEClass, ARCHIMATE_MODEL__PURPOSE);
        createEAttribute(archimateModelEClass, ARCHIMATE_MODEL__FILE);
        createEAttribute(archimateModelEClass, ARCHIMATE_MODEL__VERSION);
        createEReference(archimateModelEClass, ARCHIMATE_MODEL__METADATA);
        createEReference(archimateModelEClass, ARCHIMATE_MODEL__PROFILES);

        junctionEClass = createEClass(JUNCTION);
        createEAttribute(junctionEClass, JUNCTION__TYPE);

        applicationCollaborationEClass = createEClass(APPLICATION_COLLABORATION);

        applicationComponentEClass = createEClass(APPLICATION_COMPONENT);

        applicationEventEClass = createEClass(APPLICATION_EVENT);

        applicationFunctionEClass = createEClass(APPLICATION_FUNCTION);

        applicationInteractionEClass = createEClass(APPLICATION_INTERACTION);

        applicationInterfaceEClass = createEClass(APPLICATION_INTERFACE);

        applicationProcessEClass = createEClass(APPLICATION_PROCESS);

        applicationServiceEClass = createEClass(APPLICATION_SERVICE);

        artifactEClass = createEClass(ARTIFACT);

        assessmentEClass = createEClass(ASSESSMENT);

        businessActorEClass = createEClass(BUSINESS_ACTOR);

        businessCollaborationEClass = createEClass(BUSINESS_COLLABORATION);

        businessEventEClass = createEClass(BUSINESS_EVENT);

        businessFunctionEClass = createEClass(BUSINESS_FUNCTION);

        businessInteractionEClass = createEClass(BUSINESS_INTERACTION);

        businessInterfaceEClass = createEClass(BUSINESS_INTERFACE);

        businessObjectEClass = createEClass(BUSINESS_OBJECT);

        businessProcessEClass = createEClass(BUSINESS_PROCESS);

        businessRoleEClass = createEClass(BUSINESS_ROLE);

        businessServiceEClass = createEClass(BUSINESS_SERVICE);

        capabilityEClass = createEClass(CAPABILITY);

        communicationNetworkEClass = createEClass(COMMUNICATION_NETWORK);

        contractEClass = createEClass(CONTRACT);

        constraintEClass = createEClass(CONSTRAINT);

        courseOfActionEClass = createEClass(COURSE_OF_ACTION);

        dataObjectEClass = createEClass(DATA_OBJECT);

        deliverableEClass = createEClass(DELIVERABLE);

        deviceEClass = createEClass(DEVICE);

        distributionNetworkEClass = createEClass(DISTRIBUTION_NETWORK);

        driverEClass = createEClass(DRIVER);

        equipmentEClass = createEClass(EQUIPMENT);

        facilityEClass = createEClass(FACILITY);

        gapEClass = createEClass(GAP);

        goalEClass = createEClass(GOAL);

        groupingEClass = createEClass(GROUPING);

        implementationEventEClass = createEClass(IMPLEMENTATION_EVENT);

        locationEClass = createEClass(LOCATION);

        materialEClass = createEClass(MATERIAL);

        meaningEClass = createEClass(MEANING);

        nodeEClass = createEClass(NODE);

        outcomeEClass = createEClass(OUTCOME);

        pathEClass = createEClass(PATH);

        plateauEClass = createEClass(PLATEAU);

        principleEClass = createEClass(PRINCIPLE);

        productEClass = createEClass(PRODUCT);

        representationEClass = createEClass(REPRESENTATION);

        resourceEClass = createEClass(RESOURCE);

        requirementEClass = createEClass(REQUIREMENT);

        stakeholderEClass = createEClass(STAKEHOLDER);

        systemSoftwareEClass = createEClass(SYSTEM_SOFTWARE);

        technologyCollaborationEClass = createEClass(TECHNOLOGY_COLLABORATION);

        technologyEventEClass = createEClass(TECHNOLOGY_EVENT);

        technologyFunctionEClass = createEClass(TECHNOLOGY_FUNCTION);

        technologyInterfaceEClass = createEClass(TECHNOLOGY_INTERFACE);

        technologyInteractionEClass = createEClass(TECHNOLOGY_INTERACTION);

        technologyProcessEClass = createEClass(TECHNOLOGY_PROCESS);

        technologyServiceEClass = createEClass(TECHNOLOGY_SERVICE);

        valueEClass = createEClass(VALUE);

        valueStreamEClass = createEClass(VALUE_STREAM);

        workPackageEClass = createEClass(WORK_PACKAGE);

        accessRelationshipEClass = createEClass(ACCESS_RELATIONSHIP);
        createEAttribute(accessRelationshipEClass, ACCESS_RELATIONSHIP__ACCESS_TYPE);

        aggregationRelationshipEClass = createEClass(AGGREGATION_RELATIONSHIP);

        assignmentRelationshipEClass = createEClass(ASSIGNMENT_RELATIONSHIP);

        associationRelationshipEClass = createEClass(ASSOCIATION_RELATIONSHIP);
        createEAttribute(associationRelationshipEClass, ASSOCIATION_RELATIONSHIP__DIRECTED);

        compositionRelationshipEClass = createEClass(COMPOSITION_RELATIONSHIP);

        flowRelationshipEClass = createEClass(FLOW_RELATIONSHIP);

        influenceRelationshipEClass = createEClass(INFLUENCE_RELATIONSHIP);
        createEAttribute(influenceRelationshipEClass, INFLUENCE_RELATIONSHIP__STRENGTH);

        realizationRelationshipEClass = createEClass(REALIZATION_RELATIONSHIP);

        servingRelationshipEClass = createEClass(SERVING_RELATIONSHIP);

        specializationRelationshipEClass = createEClass(SPECIALIZATION_RELATIONSHIP);

        triggeringRelationshipEClass = createEClass(TRIGGERING_RELATIONSHIP);

        diagramModelComponentEClass = createEClass(DIAGRAM_MODEL_COMPONENT);

        connectableEClass = createEClass(CONNECTABLE);
        createEReference(connectableEClass, CONNECTABLE__SOURCE_CONNECTIONS);
        createEReference(connectableEClass, CONNECTABLE__TARGET_CONNECTIONS);

        diagramModelContainerEClass = createEClass(DIAGRAM_MODEL_CONTAINER);
        createEReference(diagramModelContainerEClass, DIAGRAM_MODEL_CONTAINER__CHILDREN);

        diagramModelEClass = createEClass(DIAGRAM_MODEL);
        createEAttribute(diagramModelEClass, DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE);

        diagramModelReferenceEClass = createEClass(DIAGRAM_MODEL_REFERENCE);
        createEReference(diagramModelReferenceEClass, DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL);

        diagramModelObjectEClass = createEClass(DIAGRAM_MODEL_OBJECT);
        createEReference(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__BOUNDS);
        createEAttribute(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__FILL_COLOR);
        createEAttribute(diagramModelObjectEClass, DIAGRAM_MODEL_OBJECT__ALPHA);

        diagramModelGroupEClass = createEClass(DIAGRAM_MODEL_GROUP);

        diagramModelNoteEClass = createEClass(DIAGRAM_MODEL_NOTE);

        diagramModelImageEClass = createEClass(DIAGRAM_MODEL_IMAGE);

        diagramModelConnectionEClass = createEClass(DIAGRAM_MODEL_CONNECTION);
        createEAttribute(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__TEXT);
        createEAttribute(diagramModelConnectionEClass, DIAGRAM_MODEL_CONNECTION__TEXT_POSITION);
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

        textPositionEClass = createEClass(TEXT_POSITION);
        createEAttribute(textPositionEClass, TEXT_POSITION__TEXT_POSITION);

        textAlignmentEClass = createEClass(TEXT_ALIGNMENT);
        createEAttribute(textAlignmentEClass, TEXT_ALIGNMENT__TEXT_ALIGNMENT);

        borderObjectEClass = createEClass(BORDER_OBJECT);
        createEAttribute(borderObjectEClass, BORDER_OBJECT__BORDER_COLOR);

        borderTypeEClass = createEClass(BORDER_TYPE);
        createEAttribute(borderTypeEClass, BORDER_TYPE__BORDER_TYPE);

        diagramModelImageProviderEClass = createEClass(DIAGRAM_MODEL_IMAGE_PROVIDER);
        createEAttribute(diagramModelImageProviderEClass, DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH);

        boundsEClass = createEClass(BOUNDS);
        createEAttribute(boundsEClass, BOUNDS__X);
        createEAttribute(boundsEClass, BOUNDS__Y);
        createEAttribute(boundsEClass, BOUNDS__WIDTH);
        createEAttribute(boundsEClass, BOUNDS__HEIGHT);

        lockableEClass = createEClass(LOCKABLE);
        createEAttribute(lockableEClass, LOCKABLE__LOCKED);

        iconicEClass = createEClass(ICONIC);
        createEAttribute(iconicEClass, ICONIC__IMAGE_POSITION);

        archimateDiagramModelEClass = createEClass(ARCHIMATE_DIAGRAM_MODEL);
        createEAttribute(archimateDiagramModelEClass, ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT);

        diagramModelArchimateComponentEClass = createEClass(DIAGRAM_MODEL_ARCHIMATE_COMPONENT);

        diagramModelArchimateObjectEClass = createEClass(DIAGRAM_MODEL_ARCHIMATE_OBJECT);
        createEReference(diagramModelArchimateObjectEClass, DIAGRAM_MODEL_ARCHIMATE_OBJECT__ARCHIMATE_ELEMENT);
        createEAttribute(diagramModelArchimateObjectEClass, DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE);

        diagramModelArchimateConnectionEClass = createEClass(DIAGRAM_MODEL_ARCHIMATE_CONNECTION);
        createEReference(diagramModelArchimateConnectionEClass, DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP);

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
        profileEClass.getESuperTypes().add(this.getArchimateModelObject());
        profileEClass.getESuperTypes().add(this.getDiagramModelImageProvider());
        folderEClass.getESuperTypes().add(this.getArchimateModelObject());
        folderEClass.getESuperTypes().add(this.getFolderContainer());
        folderEClass.getESuperTypes().add(this.getDocumentable());
        folderEClass.getESuperTypes().add(this.getProperties());
        archimateModelObjectEClass.getESuperTypes().add(this.getAdapter());
        archimateModelObjectEClass.getESuperTypes().add(this.getNameable());
        archimateModelObjectEClass.getESuperTypes().add(this.getIdentifier());
        archimateModelObjectEClass.getESuperTypes().add(this.getFeatures());
        archimateConceptEClass.getESuperTypes().add(this.getArchimateModelObject());
        archimateConceptEClass.getESuperTypes().add(this.getCloneable());
        archimateConceptEClass.getESuperTypes().add(this.getDocumentable());
        archimateConceptEClass.getESuperTypes().add(this.getProperties());
        archimateConceptEClass.getESuperTypes().add(this.getProfiles());
        archimateElementEClass.getESuperTypes().add(this.getArchimateConcept());
        archimateRelationshipEClass.getESuperTypes().add(this.getArchimateConcept());
        strategyElementEClass.getESuperTypes().add(this.getArchimateElement());
        businessElementEClass.getESuperTypes().add(this.getArchimateElement());
        applicationElementEClass.getESuperTypes().add(this.getArchimateElement());
        technologyElementEClass.getESuperTypes().add(this.getArchimateElement());
        technologyObjectEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyObjectEClass.getESuperTypes().add(this.getPassiveStructureElement());
        physicalElementEClass.getESuperTypes().add(this.getArchimateElement());
        motivationElementEClass.getESuperTypes().add(this.getArchimateElement());
        implementationMigrationElementEClass.getESuperTypes().add(this.getArchimateElement());
        compositeElementEClass.getESuperTypes().add(this.getArchimateElement());
        behaviorElementEClass.getESuperTypes().add(this.getArchimateElement());
        strategyBehaviorElementEClass.getESuperTypes().add(this.getBehaviorElement());
        strategyBehaviorElementEClass.getESuperTypes().add(this.getStrategyElement());
        structureElementEClass.getESuperTypes().add(this.getArchimateElement());
        activeStructureElementEClass.getESuperTypes().add(this.getStructureElement());
        passiveStructureElementEClass.getESuperTypes().add(this.getStructureElement());
        structuralRelationshipEClass.getESuperTypes().add(this.getArchimateRelationship());
        dependendencyRelationshipEClass.getESuperTypes().add(this.getArchimateRelationship());
        dynamicRelationshipEClass.getESuperTypes().add(this.getArchimateRelationship());
        otherRelationshipEClass.getESuperTypes().add(this.getArchimateRelationship());
        archimateModelEClass.getESuperTypes().add(this.getFolderContainer());
        archimateModelEClass.getESuperTypes().add(this.getArchimateModelObject());
        archimateModelEClass.getESuperTypes().add(this.getProperties());
        junctionEClass.getESuperTypes().add(this.getArchimateElement());
        applicationCollaborationEClass.getESuperTypes().add(this.getApplicationElement());
        applicationCollaborationEClass.getESuperTypes().add(this.getActiveStructureElement());
        applicationComponentEClass.getESuperTypes().add(this.getApplicationElement());
        applicationComponentEClass.getESuperTypes().add(this.getActiveStructureElement());
        applicationEventEClass.getESuperTypes().add(this.getApplicationElement());
        applicationEventEClass.getESuperTypes().add(this.getBehaviorElement());
        applicationFunctionEClass.getESuperTypes().add(this.getApplicationElement());
        applicationFunctionEClass.getESuperTypes().add(this.getBehaviorElement());
        applicationInteractionEClass.getESuperTypes().add(this.getApplicationElement());
        applicationInteractionEClass.getESuperTypes().add(this.getBehaviorElement());
        applicationInterfaceEClass.getESuperTypes().add(this.getApplicationElement());
        applicationInterfaceEClass.getESuperTypes().add(this.getActiveStructureElement());
        applicationProcessEClass.getESuperTypes().add(this.getApplicationElement());
        applicationProcessEClass.getESuperTypes().add(this.getBehaviorElement());
        applicationServiceEClass.getESuperTypes().add(this.getApplicationElement());
        applicationServiceEClass.getESuperTypes().add(this.getBehaviorElement());
        artifactEClass.getESuperTypes().add(this.getTechnologyObject());
        assessmentEClass.getESuperTypes().add(this.getMotivationElement());
        businessActorEClass.getESuperTypes().add(this.getBusinessElement());
        businessActorEClass.getESuperTypes().add(this.getActiveStructureElement());
        businessCollaborationEClass.getESuperTypes().add(this.getBusinessElement());
        businessCollaborationEClass.getESuperTypes().add(this.getActiveStructureElement());
        businessEventEClass.getESuperTypes().add(this.getBusinessElement());
        businessEventEClass.getESuperTypes().add(this.getBehaviorElement());
        businessFunctionEClass.getESuperTypes().add(this.getBusinessElement());
        businessFunctionEClass.getESuperTypes().add(this.getBehaviorElement());
        businessInteractionEClass.getESuperTypes().add(this.getBusinessElement());
        businessInteractionEClass.getESuperTypes().add(this.getBehaviorElement());
        businessInterfaceEClass.getESuperTypes().add(this.getBusinessElement());
        businessInterfaceEClass.getESuperTypes().add(this.getActiveStructureElement());
        businessObjectEClass.getESuperTypes().add(this.getBusinessElement());
        businessObjectEClass.getESuperTypes().add(this.getPassiveStructureElement());
        businessProcessEClass.getESuperTypes().add(this.getBusinessElement());
        businessProcessEClass.getESuperTypes().add(this.getBehaviorElement());
        businessRoleEClass.getESuperTypes().add(this.getBusinessElement());
        businessRoleEClass.getESuperTypes().add(this.getActiveStructureElement());
        businessServiceEClass.getESuperTypes().add(this.getBusinessElement());
        businessServiceEClass.getESuperTypes().add(this.getBehaviorElement());
        capabilityEClass.getESuperTypes().add(this.getStrategyBehaviorElement());
        communicationNetworkEClass.getESuperTypes().add(this.getTechnologyElement());
        communicationNetworkEClass.getESuperTypes().add(this.getActiveStructureElement());
        contractEClass.getESuperTypes().add(this.getBusinessObject());
        constraintEClass.getESuperTypes().add(this.getMotivationElement());
        courseOfActionEClass.getESuperTypes().add(this.getStrategyElement());
        courseOfActionEClass.getESuperTypes().add(this.getBehaviorElement());
        dataObjectEClass.getESuperTypes().add(this.getApplicationElement());
        dataObjectEClass.getESuperTypes().add(this.getPassiveStructureElement());
        deliverableEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        deliverableEClass.getESuperTypes().add(this.getPassiveStructureElement());
        deviceEClass.getESuperTypes().add(this.getTechnologyElement());
        deviceEClass.getESuperTypes().add(this.getActiveStructureElement());
        distributionNetworkEClass.getESuperTypes().add(this.getPhysicalElement());
        distributionNetworkEClass.getESuperTypes().add(this.getActiveStructureElement());
        driverEClass.getESuperTypes().add(this.getMotivationElement());
        equipmentEClass.getESuperTypes().add(this.getPhysicalElement());
        equipmentEClass.getESuperTypes().add(this.getActiveStructureElement());
        facilityEClass.getESuperTypes().add(this.getPhysicalElement());
        facilityEClass.getESuperTypes().add(this.getActiveStructureElement());
        gapEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        gapEClass.getESuperTypes().add(this.getPassiveStructureElement());
        goalEClass.getESuperTypes().add(this.getMotivationElement());
        groupingEClass.getESuperTypes().add(this.getCompositeElement());
        implementationEventEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        locationEClass.getESuperTypes().add(this.getCompositeElement());
        materialEClass.getESuperTypes().add(this.getPhysicalElement());
        materialEClass.getESuperTypes().add(this.getTechnologyObject());
        meaningEClass.getESuperTypes().add(this.getMotivationElement());
        nodeEClass.getESuperTypes().add(this.getTechnologyElement());
        nodeEClass.getESuperTypes().add(this.getActiveStructureElement());
        outcomeEClass.getESuperTypes().add(this.getMotivationElement());
        pathEClass.getESuperTypes().add(this.getTechnologyElement());
        pathEClass.getESuperTypes().add(this.getActiveStructureElement());
        plateauEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        plateauEClass.getESuperTypes().add(this.getCompositeElement());
        principleEClass.getESuperTypes().add(this.getMotivationElement());
        productEClass.getESuperTypes().add(this.getBusinessElement());
        productEClass.getESuperTypes().add(this.getCompositeElement());
        representationEClass.getESuperTypes().add(this.getBusinessElement());
        representationEClass.getESuperTypes().add(this.getPassiveStructureElement());
        resourceEClass.getESuperTypes().add(this.getStrategyElement());
        resourceEClass.getESuperTypes().add(this.getStructureElement());
        requirementEClass.getESuperTypes().add(this.getMotivationElement());
        stakeholderEClass.getESuperTypes().add(this.getMotivationElement());
        stakeholderEClass.getESuperTypes().add(this.getActiveStructureElement());
        systemSoftwareEClass.getESuperTypes().add(this.getTechnologyElement());
        systemSoftwareEClass.getESuperTypes().add(this.getActiveStructureElement());
        technologyCollaborationEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyCollaborationEClass.getESuperTypes().add(this.getActiveStructureElement());
        technologyEventEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyEventEClass.getESuperTypes().add(this.getBehaviorElement());
        technologyFunctionEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyFunctionEClass.getESuperTypes().add(this.getBehaviorElement());
        technologyInterfaceEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyInterfaceEClass.getESuperTypes().add(this.getActiveStructureElement());
        technologyInteractionEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyInteractionEClass.getESuperTypes().add(this.getBehaviorElement());
        technologyProcessEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyProcessEClass.getESuperTypes().add(this.getBehaviorElement());
        technologyServiceEClass.getESuperTypes().add(this.getTechnologyElement());
        technologyServiceEClass.getESuperTypes().add(this.getBehaviorElement());
        valueEClass.getESuperTypes().add(this.getMotivationElement());
        valueStreamEClass.getESuperTypes().add(this.getStrategyBehaviorElement());
        workPackageEClass.getESuperTypes().add(this.getImplementationMigrationElement());
        workPackageEClass.getESuperTypes().add(this.getBehaviorElement());
        accessRelationshipEClass.getESuperTypes().add(this.getDependendencyRelationship());
        aggregationRelationshipEClass.getESuperTypes().add(this.getStructuralRelationship());
        assignmentRelationshipEClass.getESuperTypes().add(this.getStructuralRelationship());
        associationRelationshipEClass.getESuperTypes().add(this.getDependendencyRelationship());
        compositionRelationshipEClass.getESuperTypes().add(this.getStructuralRelationship());
        flowRelationshipEClass.getESuperTypes().add(this.getDynamicRelationship());
        influenceRelationshipEClass.getESuperTypes().add(this.getDependendencyRelationship());
        realizationRelationshipEClass.getESuperTypes().add(this.getStructuralRelationship());
        servingRelationshipEClass.getESuperTypes().add(this.getDependendencyRelationship());
        specializationRelationshipEClass.getESuperTypes().add(this.getOtherRelationship());
        triggeringRelationshipEClass.getESuperTypes().add(this.getDynamicRelationship());
        diagramModelComponentEClass.getESuperTypes().add(this.getIdentifier());
        diagramModelComponentEClass.getESuperTypes().add(this.getCloneable());
        diagramModelComponentEClass.getESuperTypes().add(this.getAdapter());
        diagramModelComponentEClass.getESuperTypes().add(this.getNameable());
        diagramModelComponentEClass.getESuperTypes().add(this.getArchimateModelObject());
        connectableEClass.getESuperTypes().add(this.getDiagramModelComponent());
        diagramModelContainerEClass.getESuperTypes().add(this.getDiagramModelComponent());
        diagramModelEClass.getESuperTypes().add(this.getArchimateModelObject());
        diagramModelEClass.getESuperTypes().add(this.getDiagramModelContainer());
        diagramModelEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelEClass.getESuperTypes().add(this.getProperties());
        diagramModelReferenceEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelReferenceEClass.getESuperTypes().add(this.getTextPosition());
        diagramModelReferenceEClass.getESuperTypes().add(this.getIconic());
        diagramModelObjectEClass.getESuperTypes().add(this.getConnectable());
        diagramModelObjectEClass.getESuperTypes().add(this.getFontAttribute());
        diagramModelObjectEClass.getESuperTypes().add(this.getLineObject());
        diagramModelObjectEClass.getESuperTypes().add(this.getTextAlignment());
        diagramModelGroupEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelGroupEClass.getESuperTypes().add(this.getDiagramModelContainer());
        diagramModelGroupEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelGroupEClass.getESuperTypes().add(this.getProperties());
        diagramModelGroupEClass.getESuperTypes().add(this.getTextPosition());
        diagramModelGroupEClass.getESuperTypes().add(this.getBorderType());
        diagramModelGroupEClass.getESuperTypes().add(this.getIconic());
        diagramModelNoteEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelNoteEClass.getESuperTypes().add(this.getTextContent());
        diagramModelNoteEClass.getESuperTypes().add(this.getTextPosition());
        diagramModelNoteEClass.getESuperTypes().add(this.getProperties());
        diagramModelNoteEClass.getESuperTypes().add(this.getBorderType());
        diagramModelNoteEClass.getESuperTypes().add(this.getIconic());
        diagramModelImageEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelImageEClass.getESuperTypes().add(this.getBorderObject());
        diagramModelImageEClass.getESuperTypes().add(this.getDiagramModelImageProvider());
        diagramModelImageEClass.getESuperTypes().add(this.getProperties());
        diagramModelImageEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelConnectionEClass.getESuperTypes().add(this.getConnectable());
        diagramModelConnectionEClass.getESuperTypes().add(this.getFontAttribute());
        diagramModelConnectionEClass.getESuperTypes().add(this.getProperties());
        diagramModelConnectionEClass.getESuperTypes().add(this.getDocumentable());
        diagramModelConnectionEClass.getESuperTypes().add(this.getLineObject());
        diagramModelBendpointEClass.getESuperTypes().add(this.getCloneable());
        iconicEClass.getESuperTypes().add(this.getDiagramModelObject());
        iconicEClass.getESuperTypes().add(this.getDiagramModelImageProvider());
        archimateDiagramModelEClass.getESuperTypes().add(this.getDiagramModel());
        diagramModelArchimateComponentEClass.getESuperTypes().add(this.getConnectable());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getDiagramModelObject());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getDiagramModelContainer());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getDiagramModelArchimateComponent());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getTextPosition());
        diagramModelArchimateObjectEClass.getESuperTypes().add(this.getIconic());
        diagramModelArchimateConnectionEClass.getESuperTypes().add(this.getDiagramModelConnection());
        diagramModelArchimateConnectionEClass.getESuperTypes().add(this.getDiagramModelArchimateComponent());
        sketchModelEClass.getESuperTypes().add(this.getDiagramModel());
        sketchModelStickyEClass.getESuperTypes().add(this.getDiagramModelObject());
        sketchModelStickyEClass.getESuperTypes().add(this.getDiagramModelContainer());
        sketchModelStickyEClass.getESuperTypes().add(this.getTextContent());
        sketchModelStickyEClass.getESuperTypes().add(this.getProperties());
        sketchModelStickyEClass.getESuperTypes().add(this.getTextPosition());
        sketchModelStickyEClass.getESuperTypes().add(this.getIconic());
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

        initEClass(featureEClass, IFeature.class, "Feature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getFeature_Name(), ecorePackage.getEString(), "name", "", 0, 1, IFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getFeature_Value(), ecorePackage.getEString(), "value", "", 0, 1, IFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(featuresEClass, IFeatures.class, "Features", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getFeatures_Features(), this.getFeature(), null, "features", null, 0, -1, IFeatures.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

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

        initEClass(profileEClass, IProfile.class, "Profile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getProfile_Specialization(), ecorePackage.getEBoolean(), "specialization", "true", 1, 1, IProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getProfile_ConceptType(), ecorePackage.getEString(), "conceptType", null, 0, 1, IProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(profilesEClass, IProfiles.class, "Profiles", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getProfiles_Profiles(), this.getProfile(), null, "profiles", null, 0, -1, IProfiles.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(folderContainerEClass, IFolderContainer.class, "FolderContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getFolderContainer_Folders(), this.getFolder(), null, "folders", null, 0, -1, IFolderContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(folderEClass, IFolder.class, "Folder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getFolder_Elements(), ecorePackage.getEObject(), null, "elements", null, 0, -1, IFolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getFolder_Type(), this.getFolderType(), "type", null, 0, 1, IFolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(archimateModelObjectEClass, IArchimateModelObject.class, "ArchimateModelObject", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        addEOperation(archimateModelObjectEClass, this.getArchimateModel(), "getArchimateModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(archimateConceptEClass, IArchimateConcept.class, "ArchimateConcept", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(archimateElementEClass, IArchimateElement.class, "ArchimateElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(archimateRelationshipEClass, IArchimateRelationship.class, "ArchimateRelationship", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getArchimateRelationship_Source(), this.getArchimateConcept(), null, "source", null, 0, 1, IArchimateRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getArchimateRelationship_Target(), this.getArchimateConcept(), null, "target", null, 0, 1, IArchimateRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(archimateRelationshipEClass, null, "connect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getArchimateConcept(), "source", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getArchimateConcept(), "target", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateRelationshipEClass, null, "reconnect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateRelationshipEClass, null, "disconnect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(strategyElementEClass, IStrategyElement.class, "StrategyElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessElementEClass, IBusinessElement.class, "BusinessElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationElementEClass, IApplicationElement.class, "ApplicationElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyElementEClass, ITechnologyElement.class, "TechnologyElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyObjectEClass, ITechnologyObject.class, "TechnologyObject", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(physicalElementEClass, IPhysicalElement.class, "PhysicalElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(motivationElementEClass, IMotivationElement.class, "MotivationElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(implementationMigrationElementEClass, IImplementationMigrationElement.class, "ImplementationMigrationElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(compositeElementEClass, ICompositeElement.class, "CompositeElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(behaviorElementEClass, IBehaviorElement.class, "BehaviorElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(strategyBehaviorElementEClass, IStrategyBehaviorElement.class, "StrategyBehaviorElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(structureElementEClass, IStructureElement.class, "StructureElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(activeStructureElementEClass, IActiveStructureElement.class, "ActiveStructureElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(passiveStructureElementEClass, IPassiveStructureElement.class, "PassiveStructureElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(structuralRelationshipEClass, IStructuralRelationship.class, "StructuralRelationship", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(dependendencyRelationshipEClass, IDependendencyRelationship.class, "DependendencyRelationship", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(dynamicRelationshipEClass, IDynamicRelationship.class, "DynamicRelationship", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(otherRelationshipEClass, IOtherRelationship.class, "OtherRelationship", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(archimateModelEClass, IArchimateModel.class, "ArchimateModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getArchimateModel_Purpose(), ecorePackage.getEString(), "purpose", "", 0, 1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getArchimateModel_File(), this.getFile(), "file", null, 0, 1, IArchimateModel.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getArchimateModel_Version(), ecorePackage.getEString(), "version", "", 0, 1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEReference(getArchimateModel_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getArchimateModel_Profiles(), this.getProfile(), null, "profiles", null, 0, -1, IArchimateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, null, "setDefaults", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(archimateModelEClass, this.getFolder(), "getDefaultFolderForObject", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEObject(), "object", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, this.getDiagramModel(), "getDefaultDiagramModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(archimateModelEClass, this.getDiagramModel(), "getDiagramModels", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(archimateModelEClass, this.getFolder(), "getFolder", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getFolderType(), "type", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(junctionEClass, IJunction.class, "Junction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getJunction_Type(), ecorePackage.getEString(), "type", "", 0, 1, IJunction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(applicationCollaborationEClass, IApplicationCollaboration.class, "ApplicationCollaboration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationComponentEClass, IApplicationComponent.class, "ApplicationComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationEventEClass, IApplicationEvent.class, "ApplicationEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationFunctionEClass, IApplicationFunction.class, "ApplicationFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationInteractionEClass, IApplicationInteraction.class, "ApplicationInteraction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationInterfaceEClass, IApplicationInterface.class, "ApplicationInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationProcessEClass, IApplicationProcess.class, "ApplicationProcess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(applicationServiceEClass, IApplicationService.class, "ApplicationService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(artifactEClass, IArtifact.class, "Artifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(assessmentEClass, IAssessment.class, "Assessment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessActorEClass, IBusinessActor.class, "BusinessActor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessCollaborationEClass, IBusinessCollaboration.class, "BusinessCollaboration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessEventEClass, IBusinessEvent.class, "BusinessEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessFunctionEClass, IBusinessFunction.class, "BusinessFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessInteractionEClass, IBusinessInteraction.class, "BusinessInteraction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessInterfaceEClass, IBusinessInterface.class, "BusinessInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessObjectEClass, IBusinessObject.class, "BusinessObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessProcessEClass, IBusinessProcess.class, "BusinessProcess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessRoleEClass, IBusinessRole.class, "BusinessRole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(businessServiceEClass, IBusinessService.class, "BusinessService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(capabilityEClass, ICapability.class, "Capability", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(communicationNetworkEClass, ICommunicationNetwork.class, "CommunicationNetwork", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(contractEClass, IContract.class, "Contract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(constraintEClass, IConstraint.class, "Constraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(courseOfActionEClass, ICourseOfAction.class, "CourseOfAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(dataObjectEClass, IDataObject.class, "DataObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(deliverableEClass, IDeliverable.class, "Deliverable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(deviceEClass, IDevice.class, "Device", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(distributionNetworkEClass, IDistributionNetwork.class, "DistributionNetwork", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(driverEClass, IDriver.class, "Driver", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(equipmentEClass, IEquipment.class, "Equipment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(facilityEClass, IFacility.class, "Facility", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(gapEClass, IGap.class, "Gap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(goalEClass, IGoal.class, "Goal", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(groupingEClass, IGrouping.class, "Grouping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(implementationEventEClass, IImplementationEvent.class, "ImplementationEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(locationEClass, ILocation.class, "Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(materialEClass, IMaterial.class, "Material", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(meaningEClass, IMeaning.class, "Meaning", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(nodeEClass, INode.class, "Node", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(outcomeEClass, IOutcome.class, "Outcome", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(pathEClass, IPath.class, "Path", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(plateauEClass, IPlateau.class, "Plateau", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(principleEClass, IPrinciple.class, "Principle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(productEClass, IProduct.class, "Product", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(representationEClass, IRepresentation.class, "Representation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(resourceEClass, IResource.class, "Resource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(requirementEClass, IRequirement.class, "Requirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(stakeholderEClass, IStakeholder.class, "Stakeholder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(systemSoftwareEClass, ISystemSoftware.class, "SystemSoftware", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyCollaborationEClass, ITechnologyCollaboration.class, "TechnologyCollaboration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyEventEClass, ITechnologyEvent.class, "TechnologyEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyFunctionEClass, ITechnologyFunction.class, "TechnologyFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyInterfaceEClass, ITechnologyInterface.class, "TechnologyInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyInteractionEClass, ITechnologyInteraction.class, "TechnologyInteraction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyProcessEClass, ITechnologyProcess.class, "TechnologyProcess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(technologyServiceEClass, ITechnologyService.class, "TechnologyService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(valueEClass, IValue.class, "Value", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(valueStreamEClass, IValueStream.class, "ValueStream", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(workPackageEClass, IWorkPackage.class, "WorkPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(accessRelationshipEClass, IAccessRelationship.class, "AccessRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getAccessRelationship_AccessType(), ecorePackage.getEInt(), "accessType", "0", 0, 1, IAccessRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(aggregationRelationshipEClass, IAggregationRelationship.class, "AggregationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(assignmentRelationshipEClass, IAssignmentRelationship.class, "AssignmentRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(associationRelationshipEClass, IAssociationRelationship.class, "AssociationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getAssociationRelationship_Directed(), ecorePackage.getEBoolean(), "directed", null, 0, 1, IAssociationRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(compositionRelationshipEClass, ICompositionRelationship.class, "CompositionRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(flowRelationshipEClass, IFlowRelationship.class, "FlowRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(influenceRelationshipEClass, IInfluenceRelationship.class, "InfluenceRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getInfluenceRelationship_Strength(), ecorePackage.getEString(), "strength", "", 0, 1, IInfluenceRelationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(realizationRelationshipEClass, IRealizationRelationship.class, "RealizationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(servingRelationshipEClass, IServingRelationship.class, "ServingRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(specializationRelationshipEClass, ISpecializationRelationship.class, "SpecializationRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(triggeringRelationshipEClass, ITriggeringRelationship.class, "TriggeringRelationship", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(diagramModelComponentEClass, IDiagramModelComponent.class, "DiagramModelComponent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        addEOperation(diagramModelComponentEClass, this.getDiagramModel(), "getDiagramModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(connectableEClass, IConnectable.class, "Connectable", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getConnectable_SourceConnections(), this.getDiagramModelConnection(), null, "sourceConnections", null, 0, -1, IConnectable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getConnectable_TargetConnections(), this.getDiagramModelConnection(), null, "targetConnections", null, 0, -1, IConnectable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(connectableEClass, null, "addConnection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getDiagramModelConnection(), "connection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(connectableEClass, null, "removeConnection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getDiagramModelConnection(), "connection", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelContainerEClass, IDiagramModelContainer.class, "DiagramModelContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelContainer_Children(), this.getDiagramModelObject(), null, "children", null, 0, -1, IDiagramModelContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelEClass, IDiagramModel.class, "DiagramModel", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDiagramModel_ConnectionRouterType(), ecorePackage.getEInt(), "connectionRouterType", null, 0, 1, IDiagramModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelReferenceEClass, IDiagramModelReference.class, "DiagramModelReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelReference_ReferencedModel(), this.getDiagramModel(), null, "referencedModel", null, 0, 1, IDiagramModelReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelObjectEClass, IDiagramModelObject.class, "DiagramModelObject", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelObject_Bounds(), this.getBounds(), null, "bounds", null, 0, 1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelObject_FillColor(), ecorePackage.getEString(), "fillColor", null, 0, 1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelObject_Alpha(), ecorePackage.getEInt(), "alpha", "255", 0, 1, IDiagramModelObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

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
        initEAttribute(getDiagramModelConnection_TextPosition(), ecorePackage.getEInt(), "textPosition", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelConnection_Source(), this.getConnectable(), null, "source", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelConnection_Target(), this.getConnectable(), null, "target", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEReference(getDiagramModelConnection_Bendpoints(), this.getDiagramModelBendpoint(), null, "bendpoints", null, 0, -1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelConnection_Type(), ecorePackage.getEInt(), "type", null, 0, 1, IDiagramModelConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelConnectionEClass, null, "connect", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getConnectable(), "source", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getConnectable(), "target", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

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

        initEClass(textPositionEClass, ITextPosition.class, "TextPosition", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getTextPosition_TextPosition(), ecorePackage.getEInt(), "textPosition", null, 0, 1, ITextPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(textAlignmentEClass, ITextAlignment.class, "TextAlignment", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getTextAlignment_TextAlignment(), ecorePackage.getEInt(), "textAlignment", "2", 0, 1, ITextAlignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(borderObjectEClass, IBorderObject.class, "BorderObject", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getBorderObject_BorderColor(), ecorePackage.getEString(), "borderColor", null, 0, 1, IBorderObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(borderTypeEClass, IBorderType.class, "BorderType", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getBorderType_BorderType(), ecorePackage.getEInt(), "borderType", null, 0, 1, IBorderType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelImageProviderEClass, IDiagramModelImageProvider.class, "DiagramModelImageProvider", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getDiagramModelImageProvider_ImagePath(), ecorePackage.getEString(), "imagePath", null, 0, 1, IDiagramModelImageProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(boundsEClass, IBounds.class, "Bounds", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getBounds_X(), ecorePackage.getEInt(), "x", null, 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getBounds_Y(), ecorePackage.getEInt(), "y", null, 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getBounds_Width(), ecorePackage.getEInt(), "width", "-1", 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
        initEAttribute(getBounds_Height(), ecorePackage.getEInt(), "height", "-1", 0, 1, IBounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        op = addEOperation(boundsEClass, null, "setLocation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "x", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "y", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(boundsEClass, null, "setSize", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "width", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, ecorePackage.getEInt(), "height", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(boundsEClass, this.getBounds(), "getCopy", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(lockableEClass, ILockable.class, "Lockable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getLockable_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, ILockable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(iconicEClass, IIconic.class, "Iconic", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getIconic_ImagePosition(), ecorePackage.getEInt(), "imagePosition", "2", 0, 1, IIconic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(archimateDiagramModelEClass, IArchimateDiagramModel.class, "ArchimateDiagramModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getArchimateDiagramModel_Viewpoint(), ecorePackage.getEString(), "viewpoint", "", 0, 1, IArchimateDiagramModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(diagramModelArchimateComponentEClass, IDiagramModelArchimateComponent.class, "DiagramModelArchimateComponent", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        addEOperation(diagramModelArchimateComponentEClass, this.getArchimateConcept(), "getArchimateConcept", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelArchimateComponentEClass, null, "setArchimateConcept", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getArchimateConcept(), "concept", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        op = addEOperation(diagramModelArchimateComponentEClass, null, "addArchimateConceptToModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
        addEParameter(op, this.getFolder(), "parent", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        addEOperation(diagramModelArchimateComponentEClass, null, "removeArchimateConceptFromModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

        initEClass(diagramModelArchimateObjectEClass, IDiagramModelArchimateObject.class, "DiagramModelArchimateObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelArchimateObject_ArchimateElement(), this.getArchimateElement(), null, "archimateElement", null, 0, 1, IDiagramModelArchimateObject.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getDiagramModelArchimateObject_Type(), ecorePackage.getEInt(), "type", "0", 0, 1, IDiagramModelArchimateObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(diagramModelArchimateConnectionEClass, IDiagramModelArchimateConnection.class, "DiagramModelArchimateConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEReference(getDiagramModelArchimateConnection_ArchimateRelationship(), this.getArchimateRelationship(), null, "archimateRelationship", null, 0, 1, IDiagramModelArchimateConnection.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(sketchModelEClass, ISketchModel.class, "SketchModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getSketchModel_Background(), ecorePackage.getEInt(), "background", "1", 0, 1, ISketchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

        initEClass(sketchModelStickyEClass, ISketchModelSticky.class, "SketchModelSticky", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        initEClass(sketchModelActorEClass, ISketchModelActor.class, "SketchModelActor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

        // Initialize enums and add enum literals
        initEEnum(folderTypeEEnum, FolderType.class, "FolderType"); //$NON-NLS-1$
        addEEnumLiteral(folderTypeEEnum, FolderType.USER);
        addEEnumLiteral(folderTypeEEnum, FolderType.STRATEGY);
        addEEnumLiteral(folderTypeEEnum, FolderType.BUSINESS);
        addEEnumLiteral(folderTypeEEnum, FolderType.APPLICATION);
        addEEnumLiteral(folderTypeEEnum, FolderType.TECHNOLOGY);
        addEEnumLiteral(folderTypeEEnum, FolderType.RELATIONS);
        addEEnumLiteral(folderTypeEEnum, FolderType.OTHER);
        addEEnumLiteral(folderTypeEEnum, FolderType.DIAGRAMS);
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
          (getFeatures_Features(),
           source,
           new String[] {
               "name", "feature", //$NON-NLS-1$ //$NON-NLS-2$
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
          (getFolder_Elements(),
           source,
           new String[] {
               "name", "element", //$NON-NLS-1$ //$NON-NLS-2$
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
          (getArchimateModel_Profiles(),
           source,
           new String[] {
               "name", "profile", //$NON-NLS-1$ //$NON-NLS-2$
               "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
           });
        addAnnotation
          (getConnectable_SourceConnections(),
           source,
           new String[] {
               "name", "sourceConnection", //$NON-NLS-1$ //$NON-NLS-2$
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
