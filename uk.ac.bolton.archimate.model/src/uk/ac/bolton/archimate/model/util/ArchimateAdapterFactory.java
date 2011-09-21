/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import uk.ac.bolton.archimate.model.IAccessRelationship;
import uk.ac.bolton.archimate.model.IAdapter;
import uk.ac.bolton.archimate.model.IAggregationRelationship;
import uk.ac.bolton.archimate.model.IAndJunction;
import uk.ac.bolton.archimate.model.IApplicationCollaboration;
import uk.ac.bolton.archimate.model.IApplicationComponent;
import uk.ac.bolton.archimate.model.IApplicationFunction;
import uk.ac.bolton.archimate.model.IApplicationInteraction;
import uk.ac.bolton.archimate.model.IApplicationInterface;
import uk.ac.bolton.archimate.model.IApplicationLayerElement;
import uk.ac.bolton.archimate.model.IApplicationService;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IArtifact;
import uk.ac.bolton.archimate.model.IAssignmentRelationship;
import uk.ac.bolton.archimate.model.IAssociationRelationship;
import uk.ac.bolton.archimate.model.IBounds;
import uk.ac.bolton.archimate.model.IBusinessActivity;
import uk.ac.bolton.archimate.model.IBusinessActor;
import uk.ac.bolton.archimate.model.IBusinessCollaboration;
import uk.ac.bolton.archimate.model.IBusinessEvent;
import uk.ac.bolton.archimate.model.IBusinessFunction;
import uk.ac.bolton.archimate.model.IBusinessInteraction;
import uk.ac.bolton.archimate.model.IBusinessInterface;
import uk.ac.bolton.archimate.model.IBusinessLayerElement;
import uk.ac.bolton.archimate.model.IBusinessObject;
import uk.ac.bolton.archimate.model.IBusinessProcess;
import uk.ac.bolton.archimate.model.IBusinessRole;
import uk.ac.bolton.archimate.model.IBusinessService;
import uk.ac.bolton.archimate.model.ICloneable;
import uk.ac.bolton.archimate.model.ICommunicationPath;
import uk.ac.bolton.archimate.model.ICompositionRelationship;
import uk.ac.bolton.archimate.model.IContract;
import uk.ac.bolton.archimate.model.IDataObject;
import uk.ac.bolton.archimate.model.IDevice;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelBendpoint;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.IFlowRelationship;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IFolderContainer;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.IIdentifier;
import uk.ac.bolton.archimate.model.IInfrastructureInterface;
import uk.ac.bolton.archimate.model.IInfrastructureService;
import uk.ac.bolton.archimate.model.IInterfaceElement;
import uk.ac.bolton.archimate.model.IJunction;
import uk.ac.bolton.archimate.model.IJunctionElement;
import uk.ac.bolton.archimate.model.IMeaning;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.INetwork;
import uk.ac.bolton.archimate.model.INode;
import uk.ac.bolton.archimate.model.IOrJunction;
import uk.ac.bolton.archimate.model.IProduct;
import uk.ac.bolton.archimate.model.IProperties;
import uk.ac.bolton.archimate.model.IProperty;
import uk.ac.bolton.archimate.model.IRealisationRelationship;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.IRepresentation;
import uk.ac.bolton.archimate.model.ISketchModel;
import uk.ac.bolton.archimate.model.ISketchModelActor;
import uk.ac.bolton.archimate.model.ISketchModelSticky;
import uk.ac.bolton.archimate.model.ISpecialisationRelationship;
import uk.ac.bolton.archimate.model.ISystemSoftware;
import uk.ac.bolton.archimate.model.ITechnologyLayerElement;
import uk.ac.bolton.archimate.model.ITextContent;
import uk.ac.bolton.archimate.model.ITriggeringRelationship;
import uk.ac.bolton.archimate.model.IUsedByRelationship;
import uk.ac.bolton.archimate.model.IValue;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see uk.ac.bolton.archimate.model.IArchimatePackage
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
            public Adapter caseFolderContainer(IFolderContainer object) {
                return createFolderContainerAdapter();
            }
            @Override
            public Adapter caseArchimateModel(IArchimateModel object) {
                return createArchimateModelAdapter();
            }
            @Override
            public Adapter caseArchimateModelElement(IArchimateModelElement object) {
                return createArchimateModelElementAdapter();
            }
            @Override
            public Adapter caseFolder(IFolder object) {
                return createFolderAdapter();
            }
            @Override
            public Adapter caseArchimateElement(IArchimateElement object) {
                return createArchimateElementAdapter();
            }
            @Override
            public Adapter caseJunctionElement(IJunctionElement object) {
                return createJunctionElementAdapter();
            }
            @Override
            public Adapter caseInterfaceElement(IInterfaceElement object) {
                return createInterfaceElementAdapter();
            }
            @Override
            public Adapter caseJunction(IJunction object) {
                return createJunctionAdapter();
            }
            @Override
            public Adapter caseAndJunction(IAndJunction object) {
                return createAndJunctionAdapter();
            }
            @Override
            public Adapter caseOrJunction(IOrJunction object) {
                return createOrJunctionAdapter();
            }
            @Override
            public Adapter caseRelationship(IRelationship object) {
                return createRelationshipAdapter();
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
            public Adapter caseRealisationRelationship(IRealisationRelationship object) {
                return createRealisationRelationshipAdapter();
            }
            @Override
            public Adapter caseSpecialisationRelationship(ISpecialisationRelationship object) {
                return createSpecialisationRelationshipAdapter();
            }
            @Override
            public Adapter caseTriggeringRelationship(ITriggeringRelationship object) {
                return createTriggeringRelationshipAdapter();
            }
            @Override
            public Adapter caseUsedByRelationship(IUsedByRelationship object) {
                return createUsedByRelationshipAdapter();
            }
            @Override
            public Adapter caseBusinessLayerElement(IBusinessLayerElement object) {
                return createBusinessLayerElementAdapter();
            }
            @Override
            public Adapter caseBusinessActivity(IBusinessActivity object) {
                return createBusinessActivityAdapter();
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
            public Adapter caseContract(IContract object) {
                return createContractAdapter();
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
            public Adapter caseMeaning(IMeaning object) {
                return createMeaningAdapter();
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
            public Adapter caseProduct(IProduct object) {
                return createProductAdapter();
            }
            @Override
            public Adapter caseRepresentation(IRepresentation object) {
                return createRepresentationAdapter();
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
            public Adapter caseValue(IValue object) {
                return createValueAdapter();
            }
            @Override
            public Adapter caseApplicationLayerElement(IApplicationLayerElement object) {
                return createApplicationLayerElementAdapter();
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
            public Adapter caseDataObject(IDataObject object) {
                return createDataObjectAdapter();
            }
            @Override
            public Adapter caseApplicationService(IApplicationService object) {
                return createApplicationServiceAdapter();
            }
            @Override
            public Adapter caseTechnologyLayerElement(ITechnologyLayerElement object) {
                return createTechnologyLayerElementAdapter();
            }
            @Override
            public Adapter caseArtifact(IArtifact object) {
                return createArtifactAdapter();
            }
            @Override
            public Adapter caseCommunicationPath(ICommunicationPath object) {
                return createCommunicationPathAdapter();
            }
            @Override
            public Adapter caseNetwork(INetwork object) {
                return createNetworkAdapter();
            }
            @Override
            public Adapter caseInfrastructureInterface(IInfrastructureInterface object) {
                return createInfrastructureInterfaceAdapter();
            }
            @Override
            public Adapter caseInfrastructureService(IInfrastructureService object) {
                return createInfrastructureServiceAdapter();
            }
            @Override
            public Adapter caseNode(INode object) {
                return createNodeAdapter();
            }
            @Override
            public Adapter caseSystemSoftware(ISystemSoftware object) {
                return createSystemSoftwareAdapter();
            }
            @Override
            public Adapter caseDevice(IDevice object) {
                return createDeviceAdapter();
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
            public Adapter caseDiagramModelConnection(IDiagramModelConnection object) {
                return createDiagramModelConnectionAdapter();
            }
            @Override
            public Adapter caseDiagramModelBendpoint(IDiagramModelBendpoint object) {
                return createDiagramModelBendpointAdapter();
            }
            @Override
            public Adapter caseFontAttribute(IFontAttribute object) {
                return createFontAttributeAdapter();
            }
            @Override
            public Adapter caseBounds(IBounds object) {
                return createBoundsAdapter();
            }
            @Override
            public Adapter caseArchimateDiagramModel(IArchimateDiagramModel object) {
                return createArchimateDiagramModelAdapter();
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
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IAdapter <em>Adapter</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IAdapter
     * @generated
     */
    public Adapter createAdapterAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IIdentifier
     * @generated
     */
    public Adapter createIdentifierAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IProperties
     * @generated
     */
    public Adapter createPropertiesAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.INameable <em>Nameable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.INameable
     * @generated
     */
    public Adapter createNameableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ITextContent <em>Text Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ITextContent
     * @generated
     */
    public Adapter createTextContentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDocumentable <em>Documentable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDocumentable
     * @generated
     */
    public Adapter createDocumentableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ICloneable <em>Cloneable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ICloneable
     * @generated
     */
    public Adapter createCloneableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IFolderContainer <em>Folder Container</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IFolderContainer
     * @generated
     */
    public Adapter createFolderContainerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IArchimateModelElement <em>Model Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IArchimateModelElement
     * @generated
     */
    public Adapter createArchimateModelElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IProperty <em>Property</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IProperty
     * @generated
     */
    public Adapter createPropertyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IArchimateModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IArchimateModel
     * @generated
     */
    public Adapter createArchimateModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IFolder <em>Folder</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IFolder
     * @generated
     */
    public Adapter createFolderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IArchimateElement <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IArchimateElement
     * @generated
     */
    public Adapter createArchimateElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessActivity <em>Business Activity</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessActivity
     * @generated
     */
    public Adapter createBusinessActivityAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessLayerElement <em>Business Layer Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessLayerElement
     * @generated
     */
    public Adapter createBusinessLayerElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationLayerElement <em>Application Layer Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationLayerElement
     * @generated
     */
    public Adapter createApplicationLayerElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ITechnologyLayerElement <em>Technology Layer Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ITechnologyLayerElement
     * @generated
     */
    public Adapter createTechnologyLayerElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IArtifact <em>Artifact</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IArtifact
     * @generated
     */
    public Adapter createArtifactAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ICommunicationPath <em>Communication Path</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ICommunicationPath
     * @generated
     */
    public Adapter createCommunicationPathAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.INetwork <em>Network</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.INetwork
     * @generated
     */
    public Adapter createNetworkAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IInfrastructureInterface <em>Infrastructure Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IInfrastructureInterface
     * @generated
     */
    public Adapter createInfrastructureInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IInfrastructureService <em>Infrastructure Service</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IInfrastructureService
     * @generated
     */
    public Adapter createInfrastructureServiceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.INode <em>Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.INode
     * @generated
     */
    public Adapter createNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ISystemSoftware <em>System Software</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ISystemSoftware
     * @generated
     */
    public Adapter createSystemSoftwareAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDevice <em>Device</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDevice
     * @generated
     */
    public Adapter createDeviceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModel
     * @generated
     */
    public Adapter createDiagramModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IArchimateDiagramModel <em>Diagram Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IArchimateDiagramModel
     * @generated
     */
    public Adapter createArchimateDiagramModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelReference <em>Diagram Model Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelReference
     * @generated
     */
    public Adapter createDiagramModelReferenceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelComponent <em>Diagram Model Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelComponent
     * @generated
     */
    public Adapter createDiagramModelComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelObject <em>Diagram Model Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelObject
     * @generated
     */
    public Adapter createDiagramModelObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateObject <em>Diagram Model Archimate Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateObject
     * @generated
     */
    public Adapter createDiagramModelArchimateObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelContainer <em>Diagram Model Container</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelContainer
     * @generated
     */
    public Adapter createDiagramModelContainerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelGroup <em>Diagram Model Group</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelGroup
     * @generated
     */
    public Adapter createDiagramModelGroupAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelNote <em>Diagram Model Note</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelNote
     * @generated
     */
    public Adapter createDiagramModelNoteAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection <em>Diagram Model Connection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelConnection
     * @generated
     */
    public Adapter createDiagramModelConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection <em>Diagram Model Archimate Connection</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection
     * @generated
     */
    public Adapter createDiagramModelArchimateConnectionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint <em>Diagram Model Bendpoint</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDiagramModelBendpoint
     * @generated
     */
    public Adapter createDiagramModelBendpointAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IFontAttribute <em>Font Attribute</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IFontAttribute
     * @generated
     */
    public Adapter createFontAttributeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBounds <em>Bounds</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBounds
     * @generated
     */
    public Adapter createBoundsAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ISketchModel <em>Sketch Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ISketchModel
     * @generated
     */
    public Adapter createSketchModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ISketchModelSticky <em>Sketch Model Sticky</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ISketchModelSticky
     * @generated
     */
    public Adapter createSketchModelStickyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ISketchModelActor <em>Sketch Model Actor</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ISketchModelActor
     * @generated
     */
    public Adapter createSketchModelActorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessActor <em>Business Actor</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessActor
     * @generated
     */
    public Adapter createBusinessActorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessCollaboration <em>Business Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessCollaboration
     * @generated
     */
    public Adapter createBusinessCollaborationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IContract <em>Contract</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IContract
     * @generated
     */
    public Adapter createContractAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessEvent <em>Business Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessEvent
     * @generated
     */
    public Adapter createBusinessEventAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessFunction <em>Business Function</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessFunction
     * @generated
     */
    public Adapter createBusinessFunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessInteraction <em>Business Interaction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessInteraction
     * @generated
     */
    public Adapter createBusinessInteractionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessInterface <em>Business Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessInterface
     * @generated
     */
    public Adapter createBusinessInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IMeaning
     * @generated
     */
    public Adapter createMeaningAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessObject <em>Business Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessObject
     * @generated
     */
    public Adapter createBusinessObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessProcess <em>Business Process</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessProcess
     * @generated
     */
    public Adapter createBusinessProcessAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IProduct <em>Product</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IProduct
     * @generated
     */
    public Adapter createProductAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IRepresentation <em>Representation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IRepresentation
     * @generated
     */
    public Adapter createRepresentationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessRole <em>Business Role</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessRole
     * @generated
     */
    public Adapter createBusinessRoleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IBusinessService <em>Business Service</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IBusinessService
     * @generated
     */
    public Adapter createBusinessServiceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IValue
     * @generated
     */
    public Adapter createValueAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationComponent <em>Application Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationComponent
     * @generated
     */
    public Adapter createApplicationComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationFunction <em>Application Function</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationFunction
     * @generated
     */
    public Adapter createApplicationFunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationInteraction <em>Application Interaction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationInteraction
     * @generated
     */
    public Adapter createApplicationInteractionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationInterface <em>Application Interface</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationInterface
     * @generated
     */
    public Adapter createApplicationInterfaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IDataObject <em>Data Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IDataObject
     * @generated
     */
    public Adapter createDataObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationService <em>Application Service</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationService
     * @generated
     */
    public Adapter createApplicationServiceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IApplicationCollaboration <em>Application Collaboration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IApplicationCollaboration
     * @generated
     */
    public Adapter createApplicationCollaborationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IRelationship <em>Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IRelationship
     * @generated
     */
    public Adapter createRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IAccessRelationship <em>Access Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IAccessRelationship
     * @generated
     */
    public Adapter createAccessRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IAggregationRelationship <em>Aggregation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IAggregationRelationship
     * @generated
     */
    public Adapter createAggregationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IAssignmentRelationship <em>Assignment Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IAssignmentRelationship
     * @generated
     */
    public Adapter createAssignmentRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IAssociationRelationship <em>Association Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IAssociationRelationship
     * @generated
     */
    public Adapter createAssociationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ICompositionRelationship <em>Composition Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ICompositionRelationship
     * @generated
     */
    public Adapter createCompositionRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IFlowRelationship <em>Flow Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IFlowRelationship
     * @generated
     */
    public Adapter createFlowRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IRealisationRelationship <em>Realisation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IRealisationRelationship
     * @generated
     */
    public Adapter createRealisationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ISpecialisationRelationship <em>Specialisation Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ISpecialisationRelationship
     * @generated
     */
    public Adapter createSpecialisationRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.ITriggeringRelationship <em>Triggering Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.ITriggeringRelationship
     * @generated
     */
    public Adapter createTriggeringRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IUsedByRelationship <em>Used By Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IUsedByRelationship
     * @generated
     */
    public Adapter createUsedByRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IJunctionElement <em>Junction Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IJunctionElement
     * @generated
     */
    public Adapter createJunctionElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IInterfaceElement <em>Interface Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IInterfaceElement
     * @generated
     */
    public Adapter createInterfaceElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IJunction <em>Junction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IJunction
     * @generated
     */
    public Adapter createJunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IAndJunction <em>And Junction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IAndJunction
     * @generated
     */
    public Adapter createAndJunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link uk.ac.bolton.archimate.model.IOrJunction <em>Or Junction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see uk.ac.bolton.archimate.model.IOrJunction
     * @generated
     */
    public Adapter createOrJunctionAdapter() {
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
