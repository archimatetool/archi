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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.archimatetool.model.IArchimatePackage
 * @generated
 */
public class ArchimateSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static IArchimatePackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArchimateSwitch() {
        if (modelPackage == null) {
            modelPackage = IArchimatePackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case IArchimatePackage.ADAPTER: {
                IAdapter adapter = (IAdapter)theEObject;
                T result = caseAdapter(adapter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.IDENTIFIER: {
                IIdentifier identifier = (IIdentifier)theEObject;
                T result = caseIdentifier(identifier);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PROPERTY: {
                IProperty property = (IProperty)theEObject;
                T result = caseProperty(property);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PROPERTIES: {
                IProperties properties = (IProperties)theEObject;
                T result = caseProperties(properties);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FEATURE: {
                IFeature feature = (IFeature)theEObject;
                T result = caseFeature(feature);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FEATURES: {
                IFeatures features = (IFeatures)theEObject;
                T result = caseFeatures(features);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.METADATA: {
                IMetadata metadata = (IMetadata)theEObject;
                T result = caseMetadata(metadata);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.NAMEABLE: {
                INameable nameable = (INameable)theEObject;
                T result = caseNameable(nameable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TEXT_CONTENT: {
                ITextContent textContent = (ITextContent)theEObject;
                T result = caseTextContent(textContent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DOCUMENTABLE: {
                IDocumentable documentable = (IDocumentable)theEObject;
                T result = caseDocumentable(documentable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CLONEABLE: {
                ICloneable cloneable = (ICloneable)theEObject;
                T result = caseCloneable(cloneable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FOLDER_CONTAINER: {
                IFolderContainer folderContainer = (IFolderContainer)theEObject;
                T result = caseFolderContainer(folderContainer);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FOLDER: {
                IFolder folder = (IFolder)theEObject;
                T result = caseFolder(folder);
                if (result == null) result = caseArchimateModelObject(folder);
                if (result == null) result = caseFolderContainer(folder);
                if (result == null) result = caseDocumentable(folder);
                if (result == null) result = caseProperties(folder);
                if (result == null) result = caseAdapter(folder);
                if (result == null) result = caseNameable(folder);
                if (result == null) result = caseIdentifier(folder);
                if (result == null) result = caseFeatures(folder);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_MODEL_OBJECT: {
                IArchimateModelObject archimateModelObject = (IArchimateModelObject)theEObject;
                T result = caseArchimateModelObject(archimateModelObject);
                if (result == null) result = caseAdapter(archimateModelObject);
                if (result == null) result = caseNameable(archimateModelObject);
                if (result == null) result = caseIdentifier(archimateModelObject);
                if (result == null) result = caseFeatures(archimateModelObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_CONCEPT: {
                IArchimateConcept archimateConcept = (IArchimateConcept)theEObject;
                T result = caseArchimateConcept(archimateConcept);
                if (result == null) result = caseArchimateModelObject(archimateConcept);
                if (result == null) result = caseCloneable(archimateConcept);
                if (result == null) result = caseDocumentable(archimateConcept);
                if (result == null) result = caseProperties(archimateConcept);
                if (result == null) result = caseAdapter(archimateConcept);
                if (result == null) result = caseNameable(archimateConcept);
                if (result == null) result = caseIdentifier(archimateConcept);
                if (result == null) result = caseFeatures(archimateConcept);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_ELEMENT: {
                IArchimateElement archimateElement = (IArchimateElement)theEObject;
                T result = caseArchimateElement(archimateElement);
                if (result == null) result = caseArchimateConcept(archimateElement);
                if (result == null) result = caseArchimateModelObject(archimateElement);
                if (result == null) result = caseCloneable(archimateElement);
                if (result == null) result = caseDocumentable(archimateElement);
                if (result == null) result = caseProperties(archimateElement);
                if (result == null) result = caseAdapter(archimateElement);
                if (result == null) result = caseNameable(archimateElement);
                if (result == null) result = caseIdentifier(archimateElement);
                if (result == null) result = caseFeatures(archimateElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_RELATIONSHIP: {
                IArchimateRelationship archimateRelationship = (IArchimateRelationship)theEObject;
                T result = caseArchimateRelationship(archimateRelationship);
                if (result == null) result = caseArchimateConcept(archimateRelationship);
                if (result == null) result = caseArchimateModelObject(archimateRelationship);
                if (result == null) result = caseCloneable(archimateRelationship);
                if (result == null) result = caseDocumentable(archimateRelationship);
                if (result == null) result = caseProperties(archimateRelationship);
                if (result == null) result = caseAdapter(archimateRelationship);
                if (result == null) result = caseNameable(archimateRelationship);
                if (result == null) result = caseIdentifier(archimateRelationship);
                if (result == null) result = caseFeatures(archimateRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.STRATEGY_ELEMENT: {
                IStrategyElement strategyElement = (IStrategyElement)theEObject;
                T result = caseStrategyElement(strategyElement);
                if (result == null) result = caseArchimateElement(strategyElement);
                if (result == null) result = caseArchimateConcept(strategyElement);
                if (result == null) result = caseArchimateModelObject(strategyElement);
                if (result == null) result = caseCloneable(strategyElement);
                if (result == null) result = caseDocumentable(strategyElement);
                if (result == null) result = caseProperties(strategyElement);
                if (result == null) result = caseAdapter(strategyElement);
                if (result == null) result = caseNameable(strategyElement);
                if (result == null) result = caseIdentifier(strategyElement);
                if (result == null) result = caseFeatures(strategyElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_ELEMENT: {
                IBusinessElement businessElement = (IBusinessElement)theEObject;
                T result = caseBusinessElement(businessElement);
                if (result == null) result = caseArchimateElement(businessElement);
                if (result == null) result = caseArchimateConcept(businessElement);
                if (result == null) result = caseArchimateModelObject(businessElement);
                if (result == null) result = caseCloneable(businessElement);
                if (result == null) result = caseDocumentable(businessElement);
                if (result == null) result = caseProperties(businessElement);
                if (result == null) result = caseAdapter(businessElement);
                if (result == null) result = caseNameable(businessElement);
                if (result == null) result = caseIdentifier(businessElement);
                if (result == null) result = caseFeatures(businessElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_ELEMENT: {
                IApplicationElement applicationElement = (IApplicationElement)theEObject;
                T result = caseApplicationElement(applicationElement);
                if (result == null) result = caseArchimateElement(applicationElement);
                if (result == null) result = caseArchimateConcept(applicationElement);
                if (result == null) result = caseArchimateModelObject(applicationElement);
                if (result == null) result = caseCloneable(applicationElement);
                if (result == null) result = caseDocumentable(applicationElement);
                if (result == null) result = caseProperties(applicationElement);
                if (result == null) result = caseAdapter(applicationElement);
                if (result == null) result = caseNameable(applicationElement);
                if (result == null) result = caseIdentifier(applicationElement);
                if (result == null) result = caseFeatures(applicationElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_ELEMENT: {
                ITechnologyElement technologyElement = (ITechnologyElement)theEObject;
                T result = caseTechnologyElement(technologyElement);
                if (result == null) result = caseArchimateElement(technologyElement);
                if (result == null) result = caseArchimateConcept(technologyElement);
                if (result == null) result = caseArchimateModelObject(technologyElement);
                if (result == null) result = caseCloneable(technologyElement);
                if (result == null) result = caseDocumentable(technologyElement);
                if (result == null) result = caseProperties(technologyElement);
                if (result == null) result = caseAdapter(technologyElement);
                if (result == null) result = caseNameable(technologyElement);
                if (result == null) result = caseIdentifier(technologyElement);
                if (result == null) result = caseFeatures(technologyElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_OBJECT: {
                ITechnologyObject technologyObject = (ITechnologyObject)theEObject;
                T result = caseTechnologyObject(technologyObject);
                if (result == null) result = caseTechnologyElement(technologyObject);
                if (result == null) result = casePassiveStructureElement(technologyObject);
                if (result == null) result = caseStructureElement(technologyObject);
                if (result == null) result = caseArchimateElement(technologyObject);
                if (result == null) result = caseArchimateConcept(technologyObject);
                if (result == null) result = caseArchimateModelObject(technologyObject);
                if (result == null) result = caseCloneable(technologyObject);
                if (result == null) result = caseDocumentable(technologyObject);
                if (result == null) result = caseProperties(technologyObject);
                if (result == null) result = caseAdapter(technologyObject);
                if (result == null) result = caseNameable(technologyObject);
                if (result == null) result = caseIdentifier(technologyObject);
                if (result == null) result = caseFeatures(technologyObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PHYSICAL_ELEMENT: {
                IPhysicalElement physicalElement = (IPhysicalElement)theEObject;
                T result = casePhysicalElement(physicalElement);
                if (result == null) result = caseArchimateElement(physicalElement);
                if (result == null) result = caseArchimateConcept(physicalElement);
                if (result == null) result = caseArchimateModelObject(physicalElement);
                if (result == null) result = caseCloneable(physicalElement);
                if (result == null) result = caseDocumentable(physicalElement);
                if (result == null) result = caseProperties(physicalElement);
                if (result == null) result = caseAdapter(physicalElement);
                if (result == null) result = caseNameable(physicalElement);
                if (result == null) result = caseIdentifier(physicalElement);
                if (result == null) result = caseFeatures(physicalElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.MOTIVATION_ELEMENT: {
                IMotivationElement motivationElement = (IMotivationElement)theEObject;
                T result = caseMotivationElement(motivationElement);
                if (result == null) result = caseArchimateElement(motivationElement);
                if (result == null) result = caseArchimateConcept(motivationElement);
                if (result == null) result = caseArchimateModelObject(motivationElement);
                if (result == null) result = caseCloneable(motivationElement);
                if (result == null) result = caseDocumentable(motivationElement);
                if (result == null) result = caseProperties(motivationElement);
                if (result == null) result = caseAdapter(motivationElement);
                if (result == null) result = caseNameable(motivationElement);
                if (result == null) result = caseIdentifier(motivationElement);
                if (result == null) result = caseFeatures(motivationElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.IMPLEMENTATION_MIGRATION_ELEMENT: {
                IImplementationMigrationElement implementationMigrationElement = (IImplementationMigrationElement)theEObject;
                T result = caseImplementationMigrationElement(implementationMigrationElement);
                if (result == null) result = caseArchimateElement(implementationMigrationElement);
                if (result == null) result = caseArchimateConcept(implementationMigrationElement);
                if (result == null) result = caseArchimateModelObject(implementationMigrationElement);
                if (result == null) result = caseCloneable(implementationMigrationElement);
                if (result == null) result = caseDocumentable(implementationMigrationElement);
                if (result == null) result = caseProperties(implementationMigrationElement);
                if (result == null) result = caseAdapter(implementationMigrationElement);
                if (result == null) result = caseNameable(implementationMigrationElement);
                if (result == null) result = caseIdentifier(implementationMigrationElement);
                if (result == null) result = caseFeatures(implementationMigrationElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.COMPOSITE_ELEMENT: {
                ICompositeElement compositeElement = (ICompositeElement)theEObject;
                T result = caseCompositeElement(compositeElement);
                if (result == null) result = caseArchimateElement(compositeElement);
                if (result == null) result = caseArchimateConcept(compositeElement);
                if (result == null) result = caseArchimateModelObject(compositeElement);
                if (result == null) result = caseCloneable(compositeElement);
                if (result == null) result = caseDocumentable(compositeElement);
                if (result == null) result = caseProperties(compositeElement);
                if (result == null) result = caseAdapter(compositeElement);
                if (result == null) result = caseNameable(compositeElement);
                if (result == null) result = caseIdentifier(compositeElement);
                if (result == null) result = caseFeatures(compositeElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BEHAVIOR_ELEMENT: {
                IBehaviorElement behaviorElement = (IBehaviorElement)theEObject;
                T result = caseBehaviorElement(behaviorElement);
                if (result == null) result = caseArchimateElement(behaviorElement);
                if (result == null) result = caseArchimateConcept(behaviorElement);
                if (result == null) result = caseArchimateModelObject(behaviorElement);
                if (result == null) result = caseCloneable(behaviorElement);
                if (result == null) result = caseDocumentable(behaviorElement);
                if (result == null) result = caseProperties(behaviorElement);
                if (result == null) result = caseAdapter(behaviorElement);
                if (result == null) result = caseNameable(behaviorElement);
                if (result == null) result = caseIdentifier(behaviorElement);
                if (result == null) result = caseFeatures(behaviorElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.STRATEGY_BEHAVIOR_ELEMENT: {
                IStrategyBehaviorElement strategyBehaviorElement = (IStrategyBehaviorElement)theEObject;
                T result = caseStrategyBehaviorElement(strategyBehaviorElement);
                if (result == null) result = caseBehaviorElement(strategyBehaviorElement);
                if (result == null) result = caseStrategyElement(strategyBehaviorElement);
                if (result == null) result = caseArchimateElement(strategyBehaviorElement);
                if (result == null) result = caseArchimateConcept(strategyBehaviorElement);
                if (result == null) result = caseArchimateModelObject(strategyBehaviorElement);
                if (result == null) result = caseCloneable(strategyBehaviorElement);
                if (result == null) result = caseDocumentable(strategyBehaviorElement);
                if (result == null) result = caseProperties(strategyBehaviorElement);
                if (result == null) result = caseAdapter(strategyBehaviorElement);
                if (result == null) result = caseNameable(strategyBehaviorElement);
                if (result == null) result = caseIdentifier(strategyBehaviorElement);
                if (result == null) result = caseFeatures(strategyBehaviorElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.STRUCTURE_ELEMENT: {
                IStructureElement structureElement = (IStructureElement)theEObject;
                T result = caseStructureElement(structureElement);
                if (result == null) result = caseArchimateElement(structureElement);
                if (result == null) result = caseArchimateConcept(structureElement);
                if (result == null) result = caseArchimateModelObject(structureElement);
                if (result == null) result = caseCloneable(structureElement);
                if (result == null) result = caseDocumentable(structureElement);
                if (result == null) result = caseProperties(structureElement);
                if (result == null) result = caseAdapter(structureElement);
                if (result == null) result = caseNameable(structureElement);
                if (result == null) result = caseIdentifier(structureElement);
                if (result == null) result = caseFeatures(structureElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ACTIVE_STRUCTURE_ELEMENT: {
                IActiveStructureElement activeStructureElement = (IActiveStructureElement)theEObject;
                T result = caseActiveStructureElement(activeStructureElement);
                if (result == null) result = caseStructureElement(activeStructureElement);
                if (result == null) result = caseArchimateElement(activeStructureElement);
                if (result == null) result = caseArchimateConcept(activeStructureElement);
                if (result == null) result = caseArchimateModelObject(activeStructureElement);
                if (result == null) result = caseCloneable(activeStructureElement);
                if (result == null) result = caseDocumentable(activeStructureElement);
                if (result == null) result = caseProperties(activeStructureElement);
                if (result == null) result = caseAdapter(activeStructureElement);
                if (result == null) result = caseNameable(activeStructureElement);
                if (result == null) result = caseIdentifier(activeStructureElement);
                if (result == null) result = caseFeatures(activeStructureElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PASSIVE_STRUCTURE_ELEMENT: {
                IPassiveStructureElement passiveStructureElement = (IPassiveStructureElement)theEObject;
                T result = casePassiveStructureElement(passiveStructureElement);
                if (result == null) result = caseStructureElement(passiveStructureElement);
                if (result == null) result = caseArchimateElement(passiveStructureElement);
                if (result == null) result = caseArchimateConcept(passiveStructureElement);
                if (result == null) result = caseArchimateModelObject(passiveStructureElement);
                if (result == null) result = caseCloneable(passiveStructureElement);
                if (result == null) result = caseDocumentable(passiveStructureElement);
                if (result == null) result = caseProperties(passiveStructureElement);
                if (result == null) result = caseAdapter(passiveStructureElement);
                if (result == null) result = caseNameable(passiveStructureElement);
                if (result == null) result = caseIdentifier(passiveStructureElement);
                if (result == null) result = caseFeatures(passiveStructureElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.STRUCTURAL_RELATIONSHIP: {
                IStructuralRelationship structuralRelationship = (IStructuralRelationship)theEObject;
                T result = caseStructuralRelationship(structuralRelationship);
                if (result == null) result = caseArchimateRelationship(structuralRelationship);
                if (result == null) result = caseArchimateConcept(structuralRelationship);
                if (result == null) result = caseArchimateModelObject(structuralRelationship);
                if (result == null) result = caseCloneable(structuralRelationship);
                if (result == null) result = caseDocumentable(structuralRelationship);
                if (result == null) result = caseProperties(structuralRelationship);
                if (result == null) result = caseAdapter(structuralRelationship);
                if (result == null) result = caseNameable(structuralRelationship);
                if (result == null) result = caseIdentifier(structuralRelationship);
                if (result == null) result = caseFeatures(structuralRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DEPENDENDENCY_RELATIONSHIP: {
                IDependendencyRelationship dependendencyRelationship = (IDependendencyRelationship)theEObject;
                T result = caseDependendencyRelationship(dependendencyRelationship);
                if (result == null) result = caseArchimateRelationship(dependendencyRelationship);
                if (result == null) result = caseArchimateConcept(dependendencyRelationship);
                if (result == null) result = caseArchimateModelObject(dependendencyRelationship);
                if (result == null) result = caseCloneable(dependendencyRelationship);
                if (result == null) result = caseDocumentable(dependendencyRelationship);
                if (result == null) result = caseProperties(dependendencyRelationship);
                if (result == null) result = caseAdapter(dependendencyRelationship);
                if (result == null) result = caseNameable(dependendencyRelationship);
                if (result == null) result = caseIdentifier(dependendencyRelationship);
                if (result == null) result = caseFeatures(dependendencyRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DYNAMIC_RELATIONSHIP: {
                IDynamicRelationship dynamicRelationship = (IDynamicRelationship)theEObject;
                T result = caseDynamicRelationship(dynamicRelationship);
                if (result == null) result = caseArchimateRelationship(dynamicRelationship);
                if (result == null) result = caseArchimateConcept(dynamicRelationship);
                if (result == null) result = caseArchimateModelObject(dynamicRelationship);
                if (result == null) result = caseCloneable(dynamicRelationship);
                if (result == null) result = caseDocumentable(dynamicRelationship);
                if (result == null) result = caseProperties(dynamicRelationship);
                if (result == null) result = caseAdapter(dynamicRelationship);
                if (result == null) result = caseNameable(dynamicRelationship);
                if (result == null) result = caseIdentifier(dynamicRelationship);
                if (result == null) result = caseFeatures(dynamicRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.OTHER_RELATIONSHIP: {
                IOtherRelationship otherRelationship = (IOtherRelationship)theEObject;
                T result = caseOtherRelationship(otherRelationship);
                if (result == null) result = caseArchimateRelationship(otherRelationship);
                if (result == null) result = caseArchimateConcept(otherRelationship);
                if (result == null) result = caseArchimateModelObject(otherRelationship);
                if (result == null) result = caseCloneable(otherRelationship);
                if (result == null) result = caseDocumentable(otherRelationship);
                if (result == null) result = caseProperties(otherRelationship);
                if (result == null) result = caseAdapter(otherRelationship);
                if (result == null) result = caseNameable(otherRelationship);
                if (result == null) result = caseIdentifier(otherRelationship);
                if (result == null) result = caseFeatures(otherRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_MODEL: {
                IArchimateModel archimateModel = (IArchimateModel)theEObject;
                T result = caseArchimateModel(archimateModel);
                if (result == null) result = caseFolderContainer(archimateModel);
                if (result == null) result = caseArchimateModelObject(archimateModel);
                if (result == null) result = caseProperties(archimateModel);
                if (result == null) result = caseAdapter(archimateModel);
                if (result == null) result = caseNameable(archimateModel);
                if (result == null) result = caseIdentifier(archimateModel);
                if (result == null) result = caseFeatures(archimateModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.JUNCTION: {
                IJunction junction = (IJunction)theEObject;
                T result = caseJunction(junction);
                if (result == null) result = caseArchimateElement(junction);
                if (result == null) result = caseArchimateConcept(junction);
                if (result == null) result = caseArchimateModelObject(junction);
                if (result == null) result = caseCloneable(junction);
                if (result == null) result = caseDocumentable(junction);
                if (result == null) result = caseProperties(junction);
                if (result == null) result = caseAdapter(junction);
                if (result == null) result = caseNameable(junction);
                if (result == null) result = caseIdentifier(junction);
                if (result == null) result = caseFeatures(junction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_COLLABORATION: {
                IApplicationCollaboration applicationCollaboration = (IApplicationCollaboration)theEObject;
                T result = caseApplicationCollaboration(applicationCollaboration);
                if (result == null) result = caseApplicationElement(applicationCollaboration);
                if (result == null) result = caseActiveStructureElement(applicationCollaboration);
                if (result == null) result = caseStructureElement(applicationCollaboration);
                if (result == null) result = caseArchimateElement(applicationCollaboration);
                if (result == null) result = caseArchimateConcept(applicationCollaboration);
                if (result == null) result = caseArchimateModelObject(applicationCollaboration);
                if (result == null) result = caseCloneable(applicationCollaboration);
                if (result == null) result = caseDocumentable(applicationCollaboration);
                if (result == null) result = caseProperties(applicationCollaboration);
                if (result == null) result = caseAdapter(applicationCollaboration);
                if (result == null) result = caseNameable(applicationCollaboration);
                if (result == null) result = caseIdentifier(applicationCollaboration);
                if (result == null) result = caseFeatures(applicationCollaboration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_COMPONENT: {
                IApplicationComponent applicationComponent = (IApplicationComponent)theEObject;
                T result = caseApplicationComponent(applicationComponent);
                if (result == null) result = caseApplicationElement(applicationComponent);
                if (result == null) result = caseActiveStructureElement(applicationComponent);
                if (result == null) result = caseStructureElement(applicationComponent);
                if (result == null) result = caseArchimateElement(applicationComponent);
                if (result == null) result = caseArchimateConcept(applicationComponent);
                if (result == null) result = caseArchimateModelObject(applicationComponent);
                if (result == null) result = caseCloneable(applicationComponent);
                if (result == null) result = caseDocumentable(applicationComponent);
                if (result == null) result = caseProperties(applicationComponent);
                if (result == null) result = caseAdapter(applicationComponent);
                if (result == null) result = caseNameable(applicationComponent);
                if (result == null) result = caseIdentifier(applicationComponent);
                if (result == null) result = caseFeatures(applicationComponent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_EVENT: {
                IApplicationEvent applicationEvent = (IApplicationEvent)theEObject;
                T result = caseApplicationEvent(applicationEvent);
                if (result == null) result = caseApplicationElement(applicationEvent);
                if (result == null) result = caseBehaviorElement(applicationEvent);
                if (result == null) result = caseArchimateElement(applicationEvent);
                if (result == null) result = caseArchimateConcept(applicationEvent);
                if (result == null) result = caseArchimateModelObject(applicationEvent);
                if (result == null) result = caseCloneable(applicationEvent);
                if (result == null) result = caseDocumentable(applicationEvent);
                if (result == null) result = caseProperties(applicationEvent);
                if (result == null) result = caseAdapter(applicationEvent);
                if (result == null) result = caseNameable(applicationEvent);
                if (result == null) result = caseIdentifier(applicationEvent);
                if (result == null) result = caseFeatures(applicationEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_FUNCTION: {
                IApplicationFunction applicationFunction = (IApplicationFunction)theEObject;
                T result = caseApplicationFunction(applicationFunction);
                if (result == null) result = caseApplicationElement(applicationFunction);
                if (result == null) result = caseBehaviorElement(applicationFunction);
                if (result == null) result = caseArchimateElement(applicationFunction);
                if (result == null) result = caseArchimateConcept(applicationFunction);
                if (result == null) result = caseArchimateModelObject(applicationFunction);
                if (result == null) result = caseCloneable(applicationFunction);
                if (result == null) result = caseDocumentable(applicationFunction);
                if (result == null) result = caseProperties(applicationFunction);
                if (result == null) result = caseAdapter(applicationFunction);
                if (result == null) result = caseNameable(applicationFunction);
                if (result == null) result = caseIdentifier(applicationFunction);
                if (result == null) result = caseFeatures(applicationFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_INTERACTION: {
                IApplicationInteraction applicationInteraction = (IApplicationInteraction)theEObject;
                T result = caseApplicationInteraction(applicationInteraction);
                if (result == null) result = caseApplicationElement(applicationInteraction);
                if (result == null) result = caseBehaviorElement(applicationInteraction);
                if (result == null) result = caseArchimateElement(applicationInteraction);
                if (result == null) result = caseArchimateConcept(applicationInteraction);
                if (result == null) result = caseArchimateModelObject(applicationInteraction);
                if (result == null) result = caseCloneable(applicationInteraction);
                if (result == null) result = caseDocumentable(applicationInteraction);
                if (result == null) result = caseProperties(applicationInteraction);
                if (result == null) result = caseAdapter(applicationInteraction);
                if (result == null) result = caseNameable(applicationInteraction);
                if (result == null) result = caseIdentifier(applicationInteraction);
                if (result == null) result = caseFeatures(applicationInteraction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_INTERFACE: {
                IApplicationInterface applicationInterface = (IApplicationInterface)theEObject;
                T result = caseApplicationInterface(applicationInterface);
                if (result == null) result = caseApplicationElement(applicationInterface);
                if (result == null) result = caseActiveStructureElement(applicationInterface);
                if (result == null) result = caseStructureElement(applicationInterface);
                if (result == null) result = caseArchimateElement(applicationInterface);
                if (result == null) result = caseArchimateConcept(applicationInterface);
                if (result == null) result = caseArchimateModelObject(applicationInterface);
                if (result == null) result = caseCloneable(applicationInterface);
                if (result == null) result = caseDocumentable(applicationInterface);
                if (result == null) result = caseProperties(applicationInterface);
                if (result == null) result = caseAdapter(applicationInterface);
                if (result == null) result = caseNameable(applicationInterface);
                if (result == null) result = caseIdentifier(applicationInterface);
                if (result == null) result = caseFeatures(applicationInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_PROCESS: {
                IApplicationProcess applicationProcess = (IApplicationProcess)theEObject;
                T result = caseApplicationProcess(applicationProcess);
                if (result == null) result = caseApplicationElement(applicationProcess);
                if (result == null) result = caseBehaviorElement(applicationProcess);
                if (result == null) result = caseArchimateElement(applicationProcess);
                if (result == null) result = caseArchimateConcept(applicationProcess);
                if (result == null) result = caseArchimateModelObject(applicationProcess);
                if (result == null) result = caseCloneable(applicationProcess);
                if (result == null) result = caseDocumentable(applicationProcess);
                if (result == null) result = caseProperties(applicationProcess);
                if (result == null) result = caseAdapter(applicationProcess);
                if (result == null) result = caseNameable(applicationProcess);
                if (result == null) result = caseIdentifier(applicationProcess);
                if (result == null) result = caseFeatures(applicationProcess);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_SERVICE: {
                IApplicationService applicationService = (IApplicationService)theEObject;
                T result = caseApplicationService(applicationService);
                if (result == null) result = caseApplicationElement(applicationService);
                if (result == null) result = caseBehaviorElement(applicationService);
                if (result == null) result = caseArchimateElement(applicationService);
                if (result == null) result = caseArchimateConcept(applicationService);
                if (result == null) result = caseArchimateModelObject(applicationService);
                if (result == null) result = caseCloneable(applicationService);
                if (result == null) result = caseDocumentable(applicationService);
                if (result == null) result = caseProperties(applicationService);
                if (result == null) result = caseAdapter(applicationService);
                if (result == null) result = caseNameable(applicationService);
                if (result == null) result = caseIdentifier(applicationService);
                if (result == null) result = caseFeatures(applicationService);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARTIFACT: {
                IArtifact artifact = (IArtifact)theEObject;
                T result = caseArtifact(artifact);
                if (result == null) result = caseTechnologyObject(artifact);
                if (result == null) result = caseTechnologyElement(artifact);
                if (result == null) result = casePassiveStructureElement(artifact);
                if (result == null) result = caseStructureElement(artifact);
                if (result == null) result = caseArchimateElement(artifact);
                if (result == null) result = caseArchimateConcept(artifact);
                if (result == null) result = caseArchimateModelObject(artifact);
                if (result == null) result = caseCloneable(artifact);
                if (result == null) result = caseDocumentable(artifact);
                if (result == null) result = caseProperties(artifact);
                if (result == null) result = caseAdapter(artifact);
                if (result == null) result = caseNameable(artifact);
                if (result == null) result = caseIdentifier(artifact);
                if (result == null) result = caseFeatures(artifact);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ASSESSMENT: {
                IAssessment assessment = (IAssessment)theEObject;
                T result = caseAssessment(assessment);
                if (result == null) result = caseMotivationElement(assessment);
                if (result == null) result = caseArchimateElement(assessment);
                if (result == null) result = caseArchimateConcept(assessment);
                if (result == null) result = caseArchimateModelObject(assessment);
                if (result == null) result = caseCloneable(assessment);
                if (result == null) result = caseDocumentable(assessment);
                if (result == null) result = caseProperties(assessment);
                if (result == null) result = caseAdapter(assessment);
                if (result == null) result = caseNameable(assessment);
                if (result == null) result = caseIdentifier(assessment);
                if (result == null) result = caseFeatures(assessment);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_ACTOR: {
                IBusinessActor businessActor = (IBusinessActor)theEObject;
                T result = caseBusinessActor(businessActor);
                if (result == null) result = caseBusinessElement(businessActor);
                if (result == null) result = caseActiveStructureElement(businessActor);
                if (result == null) result = caseStructureElement(businessActor);
                if (result == null) result = caseArchimateElement(businessActor);
                if (result == null) result = caseArchimateConcept(businessActor);
                if (result == null) result = caseArchimateModelObject(businessActor);
                if (result == null) result = caseCloneable(businessActor);
                if (result == null) result = caseDocumentable(businessActor);
                if (result == null) result = caseProperties(businessActor);
                if (result == null) result = caseAdapter(businessActor);
                if (result == null) result = caseNameable(businessActor);
                if (result == null) result = caseIdentifier(businessActor);
                if (result == null) result = caseFeatures(businessActor);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_COLLABORATION: {
                IBusinessCollaboration businessCollaboration = (IBusinessCollaboration)theEObject;
                T result = caseBusinessCollaboration(businessCollaboration);
                if (result == null) result = caseBusinessElement(businessCollaboration);
                if (result == null) result = caseActiveStructureElement(businessCollaboration);
                if (result == null) result = caseStructureElement(businessCollaboration);
                if (result == null) result = caseArchimateElement(businessCollaboration);
                if (result == null) result = caseArchimateConcept(businessCollaboration);
                if (result == null) result = caseArchimateModelObject(businessCollaboration);
                if (result == null) result = caseCloneable(businessCollaboration);
                if (result == null) result = caseDocumentable(businessCollaboration);
                if (result == null) result = caseProperties(businessCollaboration);
                if (result == null) result = caseAdapter(businessCollaboration);
                if (result == null) result = caseNameable(businessCollaboration);
                if (result == null) result = caseIdentifier(businessCollaboration);
                if (result == null) result = caseFeatures(businessCollaboration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_EVENT: {
                IBusinessEvent businessEvent = (IBusinessEvent)theEObject;
                T result = caseBusinessEvent(businessEvent);
                if (result == null) result = caseBusinessElement(businessEvent);
                if (result == null) result = caseBehaviorElement(businessEvent);
                if (result == null) result = caseArchimateElement(businessEvent);
                if (result == null) result = caseArchimateConcept(businessEvent);
                if (result == null) result = caseArchimateModelObject(businessEvent);
                if (result == null) result = caseCloneable(businessEvent);
                if (result == null) result = caseDocumentable(businessEvent);
                if (result == null) result = caseProperties(businessEvent);
                if (result == null) result = caseAdapter(businessEvent);
                if (result == null) result = caseNameable(businessEvent);
                if (result == null) result = caseIdentifier(businessEvent);
                if (result == null) result = caseFeatures(businessEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_FUNCTION: {
                IBusinessFunction businessFunction = (IBusinessFunction)theEObject;
                T result = caseBusinessFunction(businessFunction);
                if (result == null) result = caseBusinessElement(businessFunction);
                if (result == null) result = caseBehaviorElement(businessFunction);
                if (result == null) result = caseArchimateElement(businessFunction);
                if (result == null) result = caseArchimateConcept(businessFunction);
                if (result == null) result = caseArchimateModelObject(businessFunction);
                if (result == null) result = caseCloneable(businessFunction);
                if (result == null) result = caseDocumentable(businessFunction);
                if (result == null) result = caseProperties(businessFunction);
                if (result == null) result = caseAdapter(businessFunction);
                if (result == null) result = caseNameable(businessFunction);
                if (result == null) result = caseIdentifier(businessFunction);
                if (result == null) result = caseFeatures(businessFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_INTERACTION: {
                IBusinessInteraction businessInteraction = (IBusinessInteraction)theEObject;
                T result = caseBusinessInteraction(businessInteraction);
                if (result == null) result = caseBusinessElement(businessInteraction);
                if (result == null) result = caseBehaviorElement(businessInteraction);
                if (result == null) result = caseArchimateElement(businessInteraction);
                if (result == null) result = caseArchimateConcept(businessInteraction);
                if (result == null) result = caseArchimateModelObject(businessInteraction);
                if (result == null) result = caseCloneable(businessInteraction);
                if (result == null) result = caseDocumentable(businessInteraction);
                if (result == null) result = caseProperties(businessInteraction);
                if (result == null) result = caseAdapter(businessInteraction);
                if (result == null) result = caseNameable(businessInteraction);
                if (result == null) result = caseIdentifier(businessInteraction);
                if (result == null) result = caseFeatures(businessInteraction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_INTERFACE: {
                IBusinessInterface businessInterface = (IBusinessInterface)theEObject;
                T result = caseBusinessInterface(businessInterface);
                if (result == null) result = caseBusinessElement(businessInterface);
                if (result == null) result = caseActiveStructureElement(businessInterface);
                if (result == null) result = caseStructureElement(businessInterface);
                if (result == null) result = caseArchimateElement(businessInterface);
                if (result == null) result = caseArchimateConcept(businessInterface);
                if (result == null) result = caseArchimateModelObject(businessInterface);
                if (result == null) result = caseCloneable(businessInterface);
                if (result == null) result = caseDocumentable(businessInterface);
                if (result == null) result = caseProperties(businessInterface);
                if (result == null) result = caseAdapter(businessInterface);
                if (result == null) result = caseNameable(businessInterface);
                if (result == null) result = caseIdentifier(businessInterface);
                if (result == null) result = caseFeatures(businessInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_OBJECT: {
                IBusinessObject businessObject = (IBusinessObject)theEObject;
                T result = caseBusinessObject(businessObject);
                if (result == null) result = caseBusinessElement(businessObject);
                if (result == null) result = casePassiveStructureElement(businessObject);
                if (result == null) result = caseStructureElement(businessObject);
                if (result == null) result = caseArchimateElement(businessObject);
                if (result == null) result = caseArchimateConcept(businessObject);
                if (result == null) result = caseArchimateModelObject(businessObject);
                if (result == null) result = caseCloneable(businessObject);
                if (result == null) result = caseDocumentable(businessObject);
                if (result == null) result = caseProperties(businessObject);
                if (result == null) result = caseAdapter(businessObject);
                if (result == null) result = caseNameable(businessObject);
                if (result == null) result = caseIdentifier(businessObject);
                if (result == null) result = caseFeatures(businessObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_PROCESS: {
                IBusinessProcess businessProcess = (IBusinessProcess)theEObject;
                T result = caseBusinessProcess(businessProcess);
                if (result == null) result = caseBusinessElement(businessProcess);
                if (result == null) result = caseBehaviorElement(businessProcess);
                if (result == null) result = caseArchimateElement(businessProcess);
                if (result == null) result = caseArchimateConcept(businessProcess);
                if (result == null) result = caseArchimateModelObject(businessProcess);
                if (result == null) result = caseCloneable(businessProcess);
                if (result == null) result = caseDocumentable(businessProcess);
                if (result == null) result = caseProperties(businessProcess);
                if (result == null) result = caseAdapter(businessProcess);
                if (result == null) result = caseNameable(businessProcess);
                if (result == null) result = caseIdentifier(businessProcess);
                if (result == null) result = caseFeatures(businessProcess);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_ROLE: {
                IBusinessRole businessRole = (IBusinessRole)theEObject;
                T result = caseBusinessRole(businessRole);
                if (result == null) result = caseBusinessElement(businessRole);
                if (result == null) result = caseActiveStructureElement(businessRole);
                if (result == null) result = caseStructureElement(businessRole);
                if (result == null) result = caseArchimateElement(businessRole);
                if (result == null) result = caseArchimateConcept(businessRole);
                if (result == null) result = caseArchimateModelObject(businessRole);
                if (result == null) result = caseCloneable(businessRole);
                if (result == null) result = caseDocumentable(businessRole);
                if (result == null) result = caseProperties(businessRole);
                if (result == null) result = caseAdapter(businessRole);
                if (result == null) result = caseNameable(businessRole);
                if (result == null) result = caseIdentifier(businessRole);
                if (result == null) result = caseFeatures(businessRole);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_SERVICE: {
                IBusinessService businessService = (IBusinessService)theEObject;
                T result = caseBusinessService(businessService);
                if (result == null) result = caseBusinessElement(businessService);
                if (result == null) result = caseBehaviorElement(businessService);
                if (result == null) result = caseArchimateElement(businessService);
                if (result == null) result = caseArchimateConcept(businessService);
                if (result == null) result = caseArchimateModelObject(businessService);
                if (result == null) result = caseCloneable(businessService);
                if (result == null) result = caseDocumentable(businessService);
                if (result == null) result = caseProperties(businessService);
                if (result == null) result = caseAdapter(businessService);
                if (result == null) result = caseNameable(businessService);
                if (result == null) result = caseIdentifier(businessService);
                if (result == null) result = caseFeatures(businessService);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CAPABILITY: {
                ICapability capability = (ICapability)theEObject;
                T result = caseCapability(capability);
                if (result == null) result = caseStrategyBehaviorElement(capability);
                if (result == null) result = caseBehaviorElement(capability);
                if (result == null) result = caseStrategyElement(capability);
                if (result == null) result = caseArchimateElement(capability);
                if (result == null) result = caseArchimateConcept(capability);
                if (result == null) result = caseArchimateModelObject(capability);
                if (result == null) result = caseCloneable(capability);
                if (result == null) result = caseDocumentable(capability);
                if (result == null) result = caseProperties(capability);
                if (result == null) result = caseAdapter(capability);
                if (result == null) result = caseNameable(capability);
                if (result == null) result = caseIdentifier(capability);
                if (result == null) result = caseFeatures(capability);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.COMMUNICATION_NETWORK: {
                ICommunicationNetwork communicationNetwork = (ICommunicationNetwork)theEObject;
                T result = caseCommunicationNetwork(communicationNetwork);
                if (result == null) result = caseTechnologyElement(communicationNetwork);
                if (result == null) result = caseActiveStructureElement(communicationNetwork);
                if (result == null) result = caseStructureElement(communicationNetwork);
                if (result == null) result = caseArchimateElement(communicationNetwork);
                if (result == null) result = caseArchimateConcept(communicationNetwork);
                if (result == null) result = caseArchimateModelObject(communicationNetwork);
                if (result == null) result = caseCloneable(communicationNetwork);
                if (result == null) result = caseDocumentable(communicationNetwork);
                if (result == null) result = caseProperties(communicationNetwork);
                if (result == null) result = caseAdapter(communicationNetwork);
                if (result == null) result = caseNameable(communicationNetwork);
                if (result == null) result = caseIdentifier(communicationNetwork);
                if (result == null) result = caseFeatures(communicationNetwork);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CONTRACT: {
                IContract contract = (IContract)theEObject;
                T result = caseContract(contract);
                if (result == null) result = caseBusinessObject(contract);
                if (result == null) result = caseBusinessElement(contract);
                if (result == null) result = casePassiveStructureElement(contract);
                if (result == null) result = caseStructureElement(contract);
                if (result == null) result = caseArchimateElement(contract);
                if (result == null) result = caseArchimateConcept(contract);
                if (result == null) result = caseArchimateModelObject(contract);
                if (result == null) result = caseCloneable(contract);
                if (result == null) result = caseDocumentable(contract);
                if (result == null) result = caseProperties(contract);
                if (result == null) result = caseAdapter(contract);
                if (result == null) result = caseNameable(contract);
                if (result == null) result = caseIdentifier(contract);
                if (result == null) result = caseFeatures(contract);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CONSTRAINT: {
                IConstraint constraint = (IConstraint)theEObject;
                T result = caseConstraint(constraint);
                if (result == null) result = caseMotivationElement(constraint);
                if (result == null) result = caseArchimateElement(constraint);
                if (result == null) result = caseArchimateConcept(constraint);
                if (result == null) result = caseArchimateModelObject(constraint);
                if (result == null) result = caseCloneable(constraint);
                if (result == null) result = caseDocumentable(constraint);
                if (result == null) result = caseProperties(constraint);
                if (result == null) result = caseAdapter(constraint);
                if (result == null) result = caseNameable(constraint);
                if (result == null) result = caseIdentifier(constraint);
                if (result == null) result = caseFeatures(constraint);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.COURSE_OF_ACTION: {
                ICourseOfAction courseOfAction = (ICourseOfAction)theEObject;
                T result = caseCourseOfAction(courseOfAction);
                if (result == null) result = caseStrategyElement(courseOfAction);
                if (result == null) result = caseBehaviorElement(courseOfAction);
                if (result == null) result = caseArchimateElement(courseOfAction);
                if (result == null) result = caseArchimateConcept(courseOfAction);
                if (result == null) result = caseArchimateModelObject(courseOfAction);
                if (result == null) result = caseCloneable(courseOfAction);
                if (result == null) result = caseDocumentable(courseOfAction);
                if (result == null) result = caseProperties(courseOfAction);
                if (result == null) result = caseAdapter(courseOfAction);
                if (result == null) result = caseNameable(courseOfAction);
                if (result == null) result = caseIdentifier(courseOfAction);
                if (result == null) result = caseFeatures(courseOfAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DATA_OBJECT: {
                IDataObject dataObject = (IDataObject)theEObject;
                T result = caseDataObject(dataObject);
                if (result == null) result = caseApplicationElement(dataObject);
                if (result == null) result = casePassiveStructureElement(dataObject);
                if (result == null) result = caseStructureElement(dataObject);
                if (result == null) result = caseArchimateElement(dataObject);
                if (result == null) result = caseArchimateConcept(dataObject);
                if (result == null) result = caseArchimateModelObject(dataObject);
                if (result == null) result = caseCloneable(dataObject);
                if (result == null) result = caseDocumentable(dataObject);
                if (result == null) result = caseProperties(dataObject);
                if (result == null) result = caseAdapter(dataObject);
                if (result == null) result = caseNameable(dataObject);
                if (result == null) result = caseIdentifier(dataObject);
                if (result == null) result = caseFeatures(dataObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DELIVERABLE: {
                IDeliverable deliverable = (IDeliverable)theEObject;
                T result = caseDeliverable(deliverable);
                if (result == null) result = caseImplementationMigrationElement(deliverable);
                if (result == null) result = casePassiveStructureElement(deliverable);
                if (result == null) result = caseStructureElement(deliverable);
                if (result == null) result = caseArchimateElement(deliverable);
                if (result == null) result = caseArchimateConcept(deliverable);
                if (result == null) result = caseArchimateModelObject(deliverable);
                if (result == null) result = caseCloneable(deliverable);
                if (result == null) result = caseDocumentable(deliverable);
                if (result == null) result = caseProperties(deliverable);
                if (result == null) result = caseAdapter(deliverable);
                if (result == null) result = caseNameable(deliverable);
                if (result == null) result = caseIdentifier(deliverable);
                if (result == null) result = caseFeatures(deliverable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DEVICE: {
                IDevice device = (IDevice)theEObject;
                T result = caseDevice(device);
                if (result == null) result = caseTechnologyElement(device);
                if (result == null) result = caseActiveStructureElement(device);
                if (result == null) result = caseStructureElement(device);
                if (result == null) result = caseArchimateElement(device);
                if (result == null) result = caseArchimateConcept(device);
                if (result == null) result = caseArchimateModelObject(device);
                if (result == null) result = caseCloneable(device);
                if (result == null) result = caseDocumentable(device);
                if (result == null) result = caseProperties(device);
                if (result == null) result = caseAdapter(device);
                if (result == null) result = caseNameable(device);
                if (result == null) result = caseIdentifier(device);
                if (result == null) result = caseFeatures(device);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DISTRIBUTION_NETWORK: {
                IDistributionNetwork distributionNetwork = (IDistributionNetwork)theEObject;
                T result = caseDistributionNetwork(distributionNetwork);
                if (result == null) result = casePhysicalElement(distributionNetwork);
                if (result == null) result = caseActiveStructureElement(distributionNetwork);
                if (result == null) result = caseStructureElement(distributionNetwork);
                if (result == null) result = caseArchimateElement(distributionNetwork);
                if (result == null) result = caseArchimateConcept(distributionNetwork);
                if (result == null) result = caseArchimateModelObject(distributionNetwork);
                if (result == null) result = caseCloneable(distributionNetwork);
                if (result == null) result = caseDocumentable(distributionNetwork);
                if (result == null) result = caseProperties(distributionNetwork);
                if (result == null) result = caseAdapter(distributionNetwork);
                if (result == null) result = caseNameable(distributionNetwork);
                if (result == null) result = caseIdentifier(distributionNetwork);
                if (result == null) result = caseFeatures(distributionNetwork);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DRIVER: {
                IDriver driver = (IDriver)theEObject;
                T result = caseDriver(driver);
                if (result == null) result = caseMotivationElement(driver);
                if (result == null) result = caseArchimateElement(driver);
                if (result == null) result = caseArchimateConcept(driver);
                if (result == null) result = caseArchimateModelObject(driver);
                if (result == null) result = caseCloneable(driver);
                if (result == null) result = caseDocumentable(driver);
                if (result == null) result = caseProperties(driver);
                if (result == null) result = caseAdapter(driver);
                if (result == null) result = caseNameable(driver);
                if (result == null) result = caseIdentifier(driver);
                if (result == null) result = caseFeatures(driver);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.EQUIPMENT: {
                IEquipment equipment = (IEquipment)theEObject;
                T result = caseEquipment(equipment);
                if (result == null) result = casePhysicalElement(equipment);
                if (result == null) result = caseActiveStructureElement(equipment);
                if (result == null) result = caseStructureElement(equipment);
                if (result == null) result = caseArchimateElement(equipment);
                if (result == null) result = caseArchimateConcept(equipment);
                if (result == null) result = caseArchimateModelObject(equipment);
                if (result == null) result = caseCloneable(equipment);
                if (result == null) result = caseDocumentable(equipment);
                if (result == null) result = caseProperties(equipment);
                if (result == null) result = caseAdapter(equipment);
                if (result == null) result = caseNameable(equipment);
                if (result == null) result = caseIdentifier(equipment);
                if (result == null) result = caseFeatures(equipment);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FACILITY: {
                IFacility facility = (IFacility)theEObject;
                T result = caseFacility(facility);
                if (result == null) result = casePhysicalElement(facility);
                if (result == null) result = caseActiveStructureElement(facility);
                if (result == null) result = caseStructureElement(facility);
                if (result == null) result = caseArchimateElement(facility);
                if (result == null) result = caseArchimateConcept(facility);
                if (result == null) result = caseArchimateModelObject(facility);
                if (result == null) result = caseCloneable(facility);
                if (result == null) result = caseDocumentable(facility);
                if (result == null) result = caseProperties(facility);
                if (result == null) result = caseAdapter(facility);
                if (result == null) result = caseNameable(facility);
                if (result == null) result = caseIdentifier(facility);
                if (result == null) result = caseFeatures(facility);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.GAP: {
                IGap gap = (IGap)theEObject;
                T result = caseGap(gap);
                if (result == null) result = caseImplementationMigrationElement(gap);
                if (result == null) result = casePassiveStructureElement(gap);
                if (result == null) result = caseStructureElement(gap);
                if (result == null) result = caseArchimateElement(gap);
                if (result == null) result = caseArchimateConcept(gap);
                if (result == null) result = caseArchimateModelObject(gap);
                if (result == null) result = caseCloneable(gap);
                if (result == null) result = caseDocumentable(gap);
                if (result == null) result = caseProperties(gap);
                if (result == null) result = caseAdapter(gap);
                if (result == null) result = caseNameable(gap);
                if (result == null) result = caseIdentifier(gap);
                if (result == null) result = caseFeatures(gap);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.GOAL: {
                IGoal goal = (IGoal)theEObject;
                T result = caseGoal(goal);
                if (result == null) result = caseMotivationElement(goal);
                if (result == null) result = caseArchimateElement(goal);
                if (result == null) result = caseArchimateConcept(goal);
                if (result == null) result = caseArchimateModelObject(goal);
                if (result == null) result = caseCloneable(goal);
                if (result == null) result = caseDocumentable(goal);
                if (result == null) result = caseProperties(goal);
                if (result == null) result = caseAdapter(goal);
                if (result == null) result = caseNameable(goal);
                if (result == null) result = caseIdentifier(goal);
                if (result == null) result = caseFeatures(goal);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.GROUPING: {
                IGrouping grouping = (IGrouping)theEObject;
                T result = caseGrouping(grouping);
                if (result == null) result = caseCompositeElement(grouping);
                if (result == null) result = caseArchimateElement(grouping);
                if (result == null) result = caseArchimateConcept(grouping);
                if (result == null) result = caseArchimateModelObject(grouping);
                if (result == null) result = caseCloneable(grouping);
                if (result == null) result = caseDocumentable(grouping);
                if (result == null) result = caseProperties(grouping);
                if (result == null) result = caseAdapter(grouping);
                if (result == null) result = caseNameable(grouping);
                if (result == null) result = caseIdentifier(grouping);
                if (result == null) result = caseFeatures(grouping);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.IMPLEMENTATION_EVENT: {
                IImplementationEvent implementationEvent = (IImplementationEvent)theEObject;
                T result = caseImplementationEvent(implementationEvent);
                if (result == null) result = caseImplementationMigrationElement(implementationEvent);
                if (result == null) result = caseArchimateElement(implementationEvent);
                if (result == null) result = caseArchimateConcept(implementationEvent);
                if (result == null) result = caseArchimateModelObject(implementationEvent);
                if (result == null) result = caseCloneable(implementationEvent);
                if (result == null) result = caseDocumentable(implementationEvent);
                if (result == null) result = caseProperties(implementationEvent);
                if (result == null) result = caseAdapter(implementationEvent);
                if (result == null) result = caseNameable(implementationEvent);
                if (result == null) result = caseIdentifier(implementationEvent);
                if (result == null) result = caseFeatures(implementationEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.LOCATION: {
                ILocation location = (ILocation)theEObject;
                T result = caseLocation(location);
                if (result == null) result = caseCompositeElement(location);
                if (result == null) result = caseArchimateElement(location);
                if (result == null) result = caseArchimateConcept(location);
                if (result == null) result = caseArchimateModelObject(location);
                if (result == null) result = caseCloneable(location);
                if (result == null) result = caseDocumentable(location);
                if (result == null) result = caseProperties(location);
                if (result == null) result = caseAdapter(location);
                if (result == null) result = caseNameable(location);
                if (result == null) result = caseIdentifier(location);
                if (result == null) result = caseFeatures(location);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.MATERIAL: {
                IMaterial material = (IMaterial)theEObject;
                T result = caseMaterial(material);
                if (result == null) result = casePhysicalElement(material);
                if (result == null) result = caseTechnologyObject(material);
                if (result == null) result = caseTechnologyElement(material);
                if (result == null) result = casePassiveStructureElement(material);
                if (result == null) result = caseArchimateConcept(material);
                if (result == null) result = caseStructureElement(material);
                if (result == null) result = caseArchimateElement(material);
                if (result == null) result = caseArchimateModelObject(material);
                if (result == null) result = caseCloneable(material);
                if (result == null) result = caseDocumentable(material);
                if (result == null) result = caseProperties(material);
                if (result == null) result = caseAdapter(material);
                if (result == null) result = caseNameable(material);
                if (result == null) result = caseIdentifier(material);
                if (result == null) result = caseFeatures(material);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.MEANING: {
                IMeaning meaning = (IMeaning)theEObject;
                T result = caseMeaning(meaning);
                if (result == null) result = caseMotivationElement(meaning);
                if (result == null) result = caseArchimateElement(meaning);
                if (result == null) result = caseArchimateConcept(meaning);
                if (result == null) result = caseArchimateModelObject(meaning);
                if (result == null) result = caseCloneable(meaning);
                if (result == null) result = caseDocumentable(meaning);
                if (result == null) result = caseProperties(meaning);
                if (result == null) result = caseAdapter(meaning);
                if (result == null) result = caseNameable(meaning);
                if (result == null) result = caseIdentifier(meaning);
                if (result == null) result = caseFeatures(meaning);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.NODE: {
                INode node = (INode)theEObject;
                T result = caseNode(node);
                if (result == null) result = caseTechnologyElement(node);
                if (result == null) result = caseActiveStructureElement(node);
                if (result == null) result = caseStructureElement(node);
                if (result == null) result = caseArchimateElement(node);
                if (result == null) result = caseArchimateConcept(node);
                if (result == null) result = caseArchimateModelObject(node);
                if (result == null) result = caseCloneable(node);
                if (result == null) result = caseDocumentable(node);
                if (result == null) result = caseProperties(node);
                if (result == null) result = caseAdapter(node);
                if (result == null) result = caseNameable(node);
                if (result == null) result = caseIdentifier(node);
                if (result == null) result = caseFeatures(node);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.OUTCOME: {
                IOutcome outcome = (IOutcome)theEObject;
                T result = caseOutcome(outcome);
                if (result == null) result = caseMotivationElement(outcome);
                if (result == null) result = caseArchimateElement(outcome);
                if (result == null) result = caseArchimateConcept(outcome);
                if (result == null) result = caseArchimateModelObject(outcome);
                if (result == null) result = caseCloneable(outcome);
                if (result == null) result = caseDocumentable(outcome);
                if (result == null) result = caseProperties(outcome);
                if (result == null) result = caseAdapter(outcome);
                if (result == null) result = caseNameable(outcome);
                if (result == null) result = caseIdentifier(outcome);
                if (result == null) result = caseFeatures(outcome);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PATH: {
                IPath path = (IPath)theEObject;
                T result = casePath(path);
                if (result == null) result = caseTechnologyElement(path);
                if (result == null) result = caseActiveStructureElement(path);
                if (result == null) result = caseStructureElement(path);
                if (result == null) result = caseArchimateElement(path);
                if (result == null) result = caseArchimateConcept(path);
                if (result == null) result = caseArchimateModelObject(path);
                if (result == null) result = caseCloneable(path);
                if (result == null) result = caseDocumentable(path);
                if (result == null) result = caseProperties(path);
                if (result == null) result = caseAdapter(path);
                if (result == null) result = caseNameable(path);
                if (result == null) result = caseIdentifier(path);
                if (result == null) result = caseFeatures(path);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PLATEAU: {
                IPlateau plateau = (IPlateau)theEObject;
                T result = casePlateau(plateau);
                if (result == null) result = caseImplementationMigrationElement(plateau);
                if (result == null) result = caseCompositeElement(plateau);
                if (result == null) result = caseArchimateElement(plateau);
                if (result == null) result = caseArchimateConcept(plateau);
                if (result == null) result = caseArchimateModelObject(plateau);
                if (result == null) result = caseCloneable(plateau);
                if (result == null) result = caseDocumentable(plateau);
                if (result == null) result = caseProperties(plateau);
                if (result == null) result = caseAdapter(plateau);
                if (result == null) result = caseNameable(plateau);
                if (result == null) result = caseIdentifier(plateau);
                if (result == null) result = caseFeatures(plateau);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PRINCIPLE: {
                IPrinciple principle = (IPrinciple)theEObject;
                T result = casePrinciple(principle);
                if (result == null) result = caseMotivationElement(principle);
                if (result == null) result = caseArchimateElement(principle);
                if (result == null) result = caseArchimateConcept(principle);
                if (result == null) result = caseArchimateModelObject(principle);
                if (result == null) result = caseCloneable(principle);
                if (result == null) result = caseDocumentable(principle);
                if (result == null) result = caseProperties(principle);
                if (result == null) result = caseAdapter(principle);
                if (result == null) result = caseNameable(principle);
                if (result == null) result = caseIdentifier(principle);
                if (result == null) result = caseFeatures(principle);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PRODUCT: {
                IProduct product = (IProduct)theEObject;
                T result = caseProduct(product);
                if (result == null) result = caseBusinessElement(product);
                if (result == null) result = caseCompositeElement(product);
                if (result == null) result = caseArchimateElement(product);
                if (result == null) result = caseArchimateConcept(product);
                if (result == null) result = caseArchimateModelObject(product);
                if (result == null) result = caseCloneable(product);
                if (result == null) result = caseDocumentable(product);
                if (result == null) result = caseProperties(product);
                if (result == null) result = caseAdapter(product);
                if (result == null) result = caseNameable(product);
                if (result == null) result = caseIdentifier(product);
                if (result == null) result = caseFeatures(product);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.REPRESENTATION: {
                IRepresentation representation = (IRepresentation)theEObject;
                T result = caseRepresentation(representation);
                if (result == null) result = caseBusinessElement(representation);
                if (result == null) result = casePassiveStructureElement(representation);
                if (result == null) result = caseStructureElement(representation);
                if (result == null) result = caseArchimateElement(representation);
                if (result == null) result = caseArchimateConcept(representation);
                if (result == null) result = caseArchimateModelObject(representation);
                if (result == null) result = caseCloneable(representation);
                if (result == null) result = caseDocumentable(representation);
                if (result == null) result = caseProperties(representation);
                if (result == null) result = caseAdapter(representation);
                if (result == null) result = caseNameable(representation);
                if (result == null) result = caseIdentifier(representation);
                if (result == null) result = caseFeatures(representation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.RESOURCE: {
                IResource resource = (IResource)theEObject;
                T result = caseResource(resource);
                if (result == null) result = caseStrategyElement(resource);
                if (result == null) result = caseStructureElement(resource);
                if (result == null) result = caseArchimateElement(resource);
                if (result == null) result = caseArchimateConcept(resource);
                if (result == null) result = caseArchimateModelObject(resource);
                if (result == null) result = caseCloneable(resource);
                if (result == null) result = caseDocumentable(resource);
                if (result == null) result = caseProperties(resource);
                if (result == null) result = caseAdapter(resource);
                if (result == null) result = caseNameable(resource);
                if (result == null) result = caseIdentifier(resource);
                if (result == null) result = caseFeatures(resource);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.REQUIREMENT: {
                IRequirement requirement = (IRequirement)theEObject;
                T result = caseRequirement(requirement);
                if (result == null) result = caseMotivationElement(requirement);
                if (result == null) result = caseArchimateElement(requirement);
                if (result == null) result = caseArchimateConcept(requirement);
                if (result == null) result = caseArchimateModelObject(requirement);
                if (result == null) result = caseCloneable(requirement);
                if (result == null) result = caseDocumentable(requirement);
                if (result == null) result = caseProperties(requirement);
                if (result == null) result = caseAdapter(requirement);
                if (result == null) result = caseNameable(requirement);
                if (result == null) result = caseIdentifier(requirement);
                if (result == null) result = caseFeatures(requirement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.STAKEHOLDER: {
                IStakeholder stakeholder = (IStakeholder)theEObject;
                T result = caseStakeholder(stakeholder);
                if (result == null) result = caseMotivationElement(stakeholder);
                if (result == null) result = caseActiveStructureElement(stakeholder);
                if (result == null) result = caseStructureElement(stakeholder);
                if (result == null) result = caseArchimateElement(stakeholder);
                if (result == null) result = caseArchimateConcept(stakeholder);
                if (result == null) result = caseArchimateModelObject(stakeholder);
                if (result == null) result = caseCloneable(stakeholder);
                if (result == null) result = caseDocumentable(stakeholder);
                if (result == null) result = caseProperties(stakeholder);
                if (result == null) result = caseAdapter(stakeholder);
                if (result == null) result = caseNameable(stakeholder);
                if (result == null) result = caseIdentifier(stakeholder);
                if (result == null) result = caseFeatures(stakeholder);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SYSTEM_SOFTWARE: {
                ISystemSoftware systemSoftware = (ISystemSoftware)theEObject;
                T result = caseSystemSoftware(systemSoftware);
                if (result == null) result = caseTechnologyElement(systemSoftware);
                if (result == null) result = caseActiveStructureElement(systemSoftware);
                if (result == null) result = caseStructureElement(systemSoftware);
                if (result == null) result = caseArchimateElement(systemSoftware);
                if (result == null) result = caseArchimateConcept(systemSoftware);
                if (result == null) result = caseArchimateModelObject(systemSoftware);
                if (result == null) result = caseCloneable(systemSoftware);
                if (result == null) result = caseDocumentable(systemSoftware);
                if (result == null) result = caseProperties(systemSoftware);
                if (result == null) result = caseAdapter(systemSoftware);
                if (result == null) result = caseNameable(systemSoftware);
                if (result == null) result = caseIdentifier(systemSoftware);
                if (result == null) result = caseFeatures(systemSoftware);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_COLLABORATION: {
                ITechnologyCollaboration technologyCollaboration = (ITechnologyCollaboration)theEObject;
                T result = caseTechnologyCollaboration(technologyCollaboration);
                if (result == null) result = caseTechnologyElement(technologyCollaboration);
                if (result == null) result = caseActiveStructureElement(technologyCollaboration);
                if (result == null) result = caseStructureElement(technologyCollaboration);
                if (result == null) result = caseArchimateElement(technologyCollaboration);
                if (result == null) result = caseArchimateConcept(technologyCollaboration);
                if (result == null) result = caseArchimateModelObject(technologyCollaboration);
                if (result == null) result = caseCloneable(technologyCollaboration);
                if (result == null) result = caseDocumentable(technologyCollaboration);
                if (result == null) result = caseProperties(technologyCollaboration);
                if (result == null) result = caseAdapter(technologyCollaboration);
                if (result == null) result = caseNameable(technologyCollaboration);
                if (result == null) result = caseIdentifier(technologyCollaboration);
                if (result == null) result = caseFeatures(technologyCollaboration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_EVENT: {
                ITechnologyEvent technologyEvent = (ITechnologyEvent)theEObject;
                T result = caseTechnologyEvent(technologyEvent);
                if (result == null) result = caseTechnologyElement(technologyEvent);
                if (result == null) result = caseBehaviorElement(technologyEvent);
                if (result == null) result = caseArchimateElement(technologyEvent);
                if (result == null) result = caseArchimateConcept(technologyEvent);
                if (result == null) result = caseArchimateModelObject(technologyEvent);
                if (result == null) result = caseCloneable(technologyEvent);
                if (result == null) result = caseDocumentable(technologyEvent);
                if (result == null) result = caseProperties(technologyEvent);
                if (result == null) result = caseAdapter(technologyEvent);
                if (result == null) result = caseNameable(technologyEvent);
                if (result == null) result = caseIdentifier(technologyEvent);
                if (result == null) result = caseFeatures(technologyEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_FUNCTION: {
                ITechnologyFunction technologyFunction = (ITechnologyFunction)theEObject;
                T result = caseTechnologyFunction(technologyFunction);
                if (result == null) result = caseTechnologyElement(technologyFunction);
                if (result == null) result = caseBehaviorElement(technologyFunction);
                if (result == null) result = caseArchimateElement(technologyFunction);
                if (result == null) result = caseArchimateConcept(technologyFunction);
                if (result == null) result = caseArchimateModelObject(technologyFunction);
                if (result == null) result = caseCloneable(technologyFunction);
                if (result == null) result = caseDocumentable(technologyFunction);
                if (result == null) result = caseProperties(technologyFunction);
                if (result == null) result = caseAdapter(technologyFunction);
                if (result == null) result = caseNameable(technologyFunction);
                if (result == null) result = caseIdentifier(technologyFunction);
                if (result == null) result = caseFeatures(technologyFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_INTERFACE: {
                ITechnologyInterface technologyInterface = (ITechnologyInterface)theEObject;
                T result = caseTechnologyInterface(technologyInterface);
                if (result == null) result = caseTechnologyElement(technologyInterface);
                if (result == null) result = caseActiveStructureElement(technologyInterface);
                if (result == null) result = caseStructureElement(technologyInterface);
                if (result == null) result = caseArchimateElement(technologyInterface);
                if (result == null) result = caseArchimateConcept(technologyInterface);
                if (result == null) result = caseArchimateModelObject(technologyInterface);
                if (result == null) result = caseCloneable(technologyInterface);
                if (result == null) result = caseDocumentable(technologyInterface);
                if (result == null) result = caseProperties(technologyInterface);
                if (result == null) result = caseAdapter(technologyInterface);
                if (result == null) result = caseNameable(technologyInterface);
                if (result == null) result = caseIdentifier(technologyInterface);
                if (result == null) result = caseFeatures(technologyInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_INTERACTION: {
                ITechnologyInteraction technologyInteraction = (ITechnologyInteraction)theEObject;
                T result = caseTechnologyInteraction(technologyInteraction);
                if (result == null) result = caseTechnologyElement(technologyInteraction);
                if (result == null) result = caseBehaviorElement(technologyInteraction);
                if (result == null) result = caseArchimateElement(technologyInteraction);
                if (result == null) result = caseArchimateConcept(technologyInteraction);
                if (result == null) result = caseArchimateModelObject(technologyInteraction);
                if (result == null) result = caseCloneable(technologyInteraction);
                if (result == null) result = caseDocumentable(technologyInteraction);
                if (result == null) result = caseProperties(technologyInteraction);
                if (result == null) result = caseAdapter(technologyInteraction);
                if (result == null) result = caseNameable(technologyInteraction);
                if (result == null) result = caseIdentifier(technologyInteraction);
                if (result == null) result = caseFeatures(technologyInteraction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_PROCESS: {
                ITechnologyProcess technologyProcess = (ITechnologyProcess)theEObject;
                T result = caseTechnologyProcess(technologyProcess);
                if (result == null) result = caseTechnologyElement(technologyProcess);
                if (result == null) result = caseBehaviorElement(technologyProcess);
                if (result == null) result = caseArchimateElement(technologyProcess);
                if (result == null) result = caseArchimateConcept(technologyProcess);
                if (result == null) result = caseArchimateModelObject(technologyProcess);
                if (result == null) result = caseCloneable(technologyProcess);
                if (result == null) result = caseDocumentable(technologyProcess);
                if (result == null) result = caseProperties(technologyProcess);
                if (result == null) result = caseAdapter(technologyProcess);
                if (result == null) result = caseNameable(technologyProcess);
                if (result == null) result = caseIdentifier(technologyProcess);
                if (result == null) result = caseFeatures(technologyProcess);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_SERVICE: {
                ITechnologyService technologyService = (ITechnologyService)theEObject;
                T result = caseTechnologyService(technologyService);
                if (result == null) result = caseTechnologyElement(technologyService);
                if (result == null) result = caseBehaviorElement(technologyService);
                if (result == null) result = caseArchimateElement(technologyService);
                if (result == null) result = caseArchimateConcept(technologyService);
                if (result == null) result = caseArchimateModelObject(technologyService);
                if (result == null) result = caseCloneable(technologyService);
                if (result == null) result = caseDocumentable(technologyService);
                if (result == null) result = caseProperties(technologyService);
                if (result == null) result = caseAdapter(technologyService);
                if (result == null) result = caseNameable(technologyService);
                if (result == null) result = caseIdentifier(technologyService);
                if (result == null) result = caseFeatures(technologyService);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.VALUE: {
                IValue value = (IValue)theEObject;
                T result = caseValue(value);
                if (result == null) result = caseMotivationElement(value);
                if (result == null) result = caseArchimateElement(value);
                if (result == null) result = caseArchimateConcept(value);
                if (result == null) result = caseArchimateModelObject(value);
                if (result == null) result = caseCloneable(value);
                if (result == null) result = caseDocumentable(value);
                if (result == null) result = caseProperties(value);
                if (result == null) result = caseAdapter(value);
                if (result == null) result = caseNameable(value);
                if (result == null) result = caseIdentifier(value);
                if (result == null) result = caseFeatures(value);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.VALUE_STREAM: {
                IValueStream valueStream = (IValueStream)theEObject;
                T result = caseValueStream(valueStream);
                if (result == null) result = caseStrategyBehaviorElement(valueStream);
                if (result == null) result = caseBehaviorElement(valueStream);
                if (result == null) result = caseStrategyElement(valueStream);
                if (result == null) result = caseArchimateElement(valueStream);
                if (result == null) result = caseArchimateConcept(valueStream);
                if (result == null) result = caseArchimateModelObject(valueStream);
                if (result == null) result = caseCloneable(valueStream);
                if (result == null) result = caseDocumentable(valueStream);
                if (result == null) result = caseProperties(valueStream);
                if (result == null) result = caseAdapter(valueStream);
                if (result == null) result = caseNameable(valueStream);
                if (result == null) result = caseIdentifier(valueStream);
                if (result == null) result = caseFeatures(valueStream);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.WORK_PACKAGE: {
                IWorkPackage workPackage = (IWorkPackage)theEObject;
                T result = caseWorkPackage(workPackage);
                if (result == null) result = caseImplementationMigrationElement(workPackage);
                if (result == null) result = caseBehaviorElement(workPackage);
                if (result == null) result = caseArchimateElement(workPackage);
                if (result == null) result = caseArchimateConcept(workPackage);
                if (result == null) result = caseArchimateModelObject(workPackage);
                if (result == null) result = caseCloneable(workPackage);
                if (result == null) result = caseDocumentable(workPackage);
                if (result == null) result = caseProperties(workPackage);
                if (result == null) result = caseAdapter(workPackage);
                if (result == null) result = caseNameable(workPackage);
                if (result == null) result = caseIdentifier(workPackage);
                if (result == null) result = caseFeatures(workPackage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ACCESS_RELATIONSHIP: {
                IAccessRelationship accessRelationship = (IAccessRelationship)theEObject;
                T result = caseAccessRelationship(accessRelationship);
                if (result == null) result = caseDependendencyRelationship(accessRelationship);
                if (result == null) result = caseArchimateRelationship(accessRelationship);
                if (result == null) result = caseArchimateConcept(accessRelationship);
                if (result == null) result = caseArchimateModelObject(accessRelationship);
                if (result == null) result = caseCloneable(accessRelationship);
                if (result == null) result = caseDocumentable(accessRelationship);
                if (result == null) result = caseProperties(accessRelationship);
                if (result == null) result = caseAdapter(accessRelationship);
                if (result == null) result = caseNameable(accessRelationship);
                if (result == null) result = caseIdentifier(accessRelationship);
                if (result == null) result = caseFeatures(accessRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.AGGREGATION_RELATIONSHIP: {
                IAggregationRelationship aggregationRelationship = (IAggregationRelationship)theEObject;
                T result = caseAggregationRelationship(aggregationRelationship);
                if (result == null) result = caseStructuralRelationship(aggregationRelationship);
                if (result == null) result = caseArchimateRelationship(aggregationRelationship);
                if (result == null) result = caseArchimateConcept(aggregationRelationship);
                if (result == null) result = caseArchimateModelObject(aggregationRelationship);
                if (result == null) result = caseCloneable(aggregationRelationship);
                if (result == null) result = caseDocumentable(aggregationRelationship);
                if (result == null) result = caseProperties(aggregationRelationship);
                if (result == null) result = caseAdapter(aggregationRelationship);
                if (result == null) result = caseNameable(aggregationRelationship);
                if (result == null) result = caseIdentifier(aggregationRelationship);
                if (result == null) result = caseFeatures(aggregationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP: {
                IAssignmentRelationship assignmentRelationship = (IAssignmentRelationship)theEObject;
                T result = caseAssignmentRelationship(assignmentRelationship);
                if (result == null) result = caseStructuralRelationship(assignmentRelationship);
                if (result == null) result = caseArchimateRelationship(assignmentRelationship);
                if (result == null) result = caseArchimateConcept(assignmentRelationship);
                if (result == null) result = caseArchimateModelObject(assignmentRelationship);
                if (result == null) result = caseCloneable(assignmentRelationship);
                if (result == null) result = caseDocumentable(assignmentRelationship);
                if (result == null) result = caseProperties(assignmentRelationship);
                if (result == null) result = caseAdapter(assignmentRelationship);
                if (result == null) result = caseNameable(assignmentRelationship);
                if (result == null) result = caseIdentifier(assignmentRelationship);
                if (result == null) result = caseFeatures(assignmentRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP: {
                IAssociationRelationship associationRelationship = (IAssociationRelationship)theEObject;
                T result = caseAssociationRelationship(associationRelationship);
                if (result == null) result = caseDependendencyRelationship(associationRelationship);
                if (result == null) result = caseArchimateRelationship(associationRelationship);
                if (result == null) result = caseArchimateConcept(associationRelationship);
                if (result == null) result = caseArchimateModelObject(associationRelationship);
                if (result == null) result = caseCloneable(associationRelationship);
                if (result == null) result = caseDocumentable(associationRelationship);
                if (result == null) result = caseProperties(associationRelationship);
                if (result == null) result = caseAdapter(associationRelationship);
                if (result == null) result = caseNameable(associationRelationship);
                if (result == null) result = caseIdentifier(associationRelationship);
                if (result == null) result = caseFeatures(associationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.COMPOSITION_RELATIONSHIP: {
                ICompositionRelationship compositionRelationship = (ICompositionRelationship)theEObject;
                T result = caseCompositionRelationship(compositionRelationship);
                if (result == null) result = caseStructuralRelationship(compositionRelationship);
                if (result == null) result = caseArchimateRelationship(compositionRelationship);
                if (result == null) result = caseArchimateConcept(compositionRelationship);
                if (result == null) result = caseArchimateModelObject(compositionRelationship);
                if (result == null) result = caseCloneable(compositionRelationship);
                if (result == null) result = caseDocumentable(compositionRelationship);
                if (result == null) result = caseProperties(compositionRelationship);
                if (result == null) result = caseAdapter(compositionRelationship);
                if (result == null) result = caseNameable(compositionRelationship);
                if (result == null) result = caseIdentifier(compositionRelationship);
                if (result == null) result = caseFeatures(compositionRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FLOW_RELATIONSHIP: {
                IFlowRelationship flowRelationship = (IFlowRelationship)theEObject;
                T result = caseFlowRelationship(flowRelationship);
                if (result == null) result = caseDynamicRelationship(flowRelationship);
                if (result == null) result = caseArchimateRelationship(flowRelationship);
                if (result == null) result = caseArchimateConcept(flowRelationship);
                if (result == null) result = caseArchimateModelObject(flowRelationship);
                if (result == null) result = caseCloneable(flowRelationship);
                if (result == null) result = caseDocumentable(flowRelationship);
                if (result == null) result = caseProperties(flowRelationship);
                if (result == null) result = caseAdapter(flowRelationship);
                if (result == null) result = caseNameable(flowRelationship);
                if (result == null) result = caseIdentifier(flowRelationship);
                if (result == null) result = caseFeatures(flowRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.INFLUENCE_RELATIONSHIP: {
                IInfluenceRelationship influenceRelationship = (IInfluenceRelationship)theEObject;
                T result = caseInfluenceRelationship(influenceRelationship);
                if (result == null) result = caseDependendencyRelationship(influenceRelationship);
                if (result == null) result = caseArchimateRelationship(influenceRelationship);
                if (result == null) result = caseArchimateConcept(influenceRelationship);
                if (result == null) result = caseArchimateModelObject(influenceRelationship);
                if (result == null) result = caseCloneable(influenceRelationship);
                if (result == null) result = caseDocumentable(influenceRelationship);
                if (result == null) result = caseProperties(influenceRelationship);
                if (result == null) result = caseAdapter(influenceRelationship);
                if (result == null) result = caseNameable(influenceRelationship);
                if (result == null) result = caseIdentifier(influenceRelationship);
                if (result == null) result = caseFeatures(influenceRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.REALIZATION_RELATIONSHIP: {
                IRealizationRelationship realizationRelationship = (IRealizationRelationship)theEObject;
                T result = caseRealizationRelationship(realizationRelationship);
                if (result == null) result = caseStructuralRelationship(realizationRelationship);
                if (result == null) result = caseArchimateRelationship(realizationRelationship);
                if (result == null) result = caseArchimateConcept(realizationRelationship);
                if (result == null) result = caseArchimateModelObject(realizationRelationship);
                if (result == null) result = caseCloneable(realizationRelationship);
                if (result == null) result = caseDocumentable(realizationRelationship);
                if (result == null) result = caseProperties(realizationRelationship);
                if (result == null) result = caseAdapter(realizationRelationship);
                if (result == null) result = caseNameable(realizationRelationship);
                if (result == null) result = caseIdentifier(realizationRelationship);
                if (result == null) result = caseFeatures(realizationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SERVING_RELATIONSHIP: {
                IServingRelationship servingRelationship = (IServingRelationship)theEObject;
                T result = caseServingRelationship(servingRelationship);
                if (result == null) result = caseDependendencyRelationship(servingRelationship);
                if (result == null) result = caseArchimateRelationship(servingRelationship);
                if (result == null) result = caseArchimateConcept(servingRelationship);
                if (result == null) result = caseArchimateModelObject(servingRelationship);
                if (result == null) result = caseCloneable(servingRelationship);
                if (result == null) result = caseDocumentable(servingRelationship);
                if (result == null) result = caseProperties(servingRelationship);
                if (result == null) result = caseAdapter(servingRelationship);
                if (result == null) result = caseNameable(servingRelationship);
                if (result == null) result = caseIdentifier(servingRelationship);
                if (result == null) result = caseFeatures(servingRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP: {
                ISpecializationRelationship specializationRelationship = (ISpecializationRelationship)theEObject;
                T result = caseSpecializationRelationship(specializationRelationship);
                if (result == null) result = caseOtherRelationship(specializationRelationship);
                if (result == null) result = caseArchimateRelationship(specializationRelationship);
                if (result == null) result = caseArchimateConcept(specializationRelationship);
                if (result == null) result = caseArchimateModelObject(specializationRelationship);
                if (result == null) result = caseCloneable(specializationRelationship);
                if (result == null) result = caseDocumentable(specializationRelationship);
                if (result == null) result = caseProperties(specializationRelationship);
                if (result == null) result = caseAdapter(specializationRelationship);
                if (result == null) result = caseNameable(specializationRelationship);
                if (result == null) result = caseIdentifier(specializationRelationship);
                if (result == null) result = caseFeatures(specializationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TRIGGERING_RELATIONSHIP: {
                ITriggeringRelationship triggeringRelationship = (ITriggeringRelationship)theEObject;
                T result = caseTriggeringRelationship(triggeringRelationship);
                if (result == null) result = caseDynamicRelationship(triggeringRelationship);
                if (result == null) result = caseArchimateRelationship(triggeringRelationship);
                if (result == null) result = caseArchimateConcept(triggeringRelationship);
                if (result == null) result = caseArchimateModelObject(triggeringRelationship);
                if (result == null) result = caseCloneable(triggeringRelationship);
                if (result == null) result = caseDocumentable(triggeringRelationship);
                if (result == null) result = caseProperties(triggeringRelationship);
                if (result == null) result = caseAdapter(triggeringRelationship);
                if (result == null) result = caseNameable(triggeringRelationship);
                if (result == null) result = caseIdentifier(triggeringRelationship);
                if (result == null) result = caseFeatures(triggeringRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_COMPONENT: {
                IDiagramModelComponent diagramModelComponent = (IDiagramModelComponent)theEObject;
                T result = caseDiagramModelComponent(diagramModelComponent);
                if (result == null) result = caseCloneable(diagramModelComponent);
                if (result == null) result = caseArchimateModelObject(diagramModelComponent);
                if (result == null) result = caseIdentifier(diagramModelComponent);
                if (result == null) result = caseAdapter(diagramModelComponent);
                if (result == null) result = caseNameable(diagramModelComponent);
                if (result == null) result = caseFeatures(diagramModelComponent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CONNECTABLE: {
                IConnectable connectable = (IConnectable)theEObject;
                T result = caseConnectable(connectable);
                if (result == null) result = caseDiagramModelComponent(connectable);
                if (result == null) result = caseCloneable(connectable);
                if (result == null) result = caseArchimateModelObject(connectable);
                if (result == null) result = caseIdentifier(connectable);
                if (result == null) result = caseAdapter(connectable);
                if (result == null) result = caseNameable(connectable);
                if (result == null) result = caseFeatures(connectable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_CONTAINER: {
                IDiagramModelContainer diagramModelContainer = (IDiagramModelContainer)theEObject;
                T result = caseDiagramModelContainer(diagramModelContainer);
                if (result == null) result = caseDiagramModelComponent(diagramModelContainer);
                if (result == null) result = caseCloneable(diagramModelContainer);
                if (result == null) result = caseArchimateModelObject(diagramModelContainer);
                if (result == null) result = caseIdentifier(diagramModelContainer);
                if (result == null) result = caseAdapter(diagramModelContainer);
                if (result == null) result = caseNameable(diagramModelContainer);
                if (result == null) result = caseFeatures(diagramModelContainer);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL: {
                IDiagramModel diagramModel = (IDiagramModel)theEObject;
                T result = caseDiagramModel(diagramModel);
                if (result == null) result = caseDiagramModelContainer(diagramModel);
                if (result == null) result = caseDocumentable(diagramModel);
                if (result == null) result = caseProperties(diagramModel);
                if (result == null) result = caseFeatures(diagramModel);
                if (result == null) result = caseDiagramModelComponent(diagramModel);
                if (result == null) result = caseArchimateModelObject(diagramModel);
                if (result == null) result = caseAdapter(diagramModel);
                if (result == null) result = caseNameable(diagramModel);
                if (result == null) result = caseIdentifier(diagramModel);
                if (result == null) result = caseCloneable(diagramModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE: {
                IDiagramModelReference diagramModelReference = (IDiagramModelReference)theEObject;
                T result = caseDiagramModelReference(diagramModelReference);
                if (result == null) result = caseDiagramModelObject(diagramModelReference);
                if (result == null) result = caseTextPosition(diagramModelReference);
                if (result == null) result = caseConnectable(diagramModelReference);
                if (result == null) result = caseFontAttribute(diagramModelReference);
                if (result == null) result = caseLineObject(diagramModelReference);
                if (result == null) result = caseTextAlignment(diagramModelReference);
                if (result == null) result = caseDiagramModelComponent(diagramModelReference);
                if (result == null) result = caseCloneable(diagramModelReference);
                if (result == null) result = caseArchimateModelObject(diagramModelReference);
                if (result == null) result = caseIdentifier(diagramModelReference);
                if (result == null) result = caseAdapter(diagramModelReference);
                if (result == null) result = caseNameable(diagramModelReference);
                if (result == null) result = caseFeatures(diagramModelReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT: {
                IDiagramModelObject diagramModelObject = (IDiagramModelObject)theEObject;
                T result = caseDiagramModelObject(diagramModelObject);
                if (result == null) result = caseConnectable(diagramModelObject);
                if (result == null) result = caseFontAttribute(diagramModelObject);
                if (result == null) result = caseLineObject(diagramModelObject);
                if (result == null) result = caseTextAlignment(diagramModelObject);
                if (result == null) result = caseDiagramModelComponent(diagramModelObject);
                if (result == null) result = caseCloneable(diagramModelObject);
                if (result == null) result = caseArchimateModelObject(diagramModelObject);
                if (result == null) result = caseIdentifier(diagramModelObject);
                if (result == null) result = caseAdapter(diagramModelObject);
                if (result == null) result = caseNameable(diagramModelObject);
                if (result == null) result = caseFeatures(diagramModelObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_GROUP: {
                IDiagramModelGroup diagramModelGroup = (IDiagramModelGroup)theEObject;
                T result = caseDiagramModelGroup(diagramModelGroup);
                if (result == null) result = caseDiagramModelContainer(diagramModelGroup);
                if (result == null) result = caseDocumentable(diagramModelGroup);
                if (result == null) result = caseProperties(diagramModelGroup);
                if (result == null) result = caseTextPosition(diagramModelGroup);
                if (result == null) result = caseBorderType(diagramModelGroup);
                if (result == null) result = caseIconic(diagramModelGroup);
                if (result == null) result = caseDiagramModelObject(diagramModelGroup);
                if (result == null) result = caseConnectable(diagramModelGroup);
                if (result == null) result = caseFontAttribute(diagramModelGroup);
                if (result == null) result = caseLineObject(diagramModelGroup);
                if (result == null) result = caseTextAlignment(diagramModelGroup);
                if (result == null) result = caseDiagramModelImageProvider(diagramModelGroup);
                if (result == null) result = caseDiagramModelComponent(diagramModelGroup);
                if (result == null) result = caseCloneable(diagramModelGroup);
                if (result == null) result = caseArchimateModelObject(diagramModelGroup);
                if (result == null) result = caseIdentifier(diagramModelGroup);
                if (result == null) result = caseAdapter(diagramModelGroup);
                if (result == null) result = caseNameable(diagramModelGroup);
                if (result == null) result = caseFeatures(diagramModelGroup);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_NOTE: {
                IDiagramModelNote diagramModelNote = (IDiagramModelNote)theEObject;
                T result = caseDiagramModelNote(diagramModelNote);
                if (result == null) result = caseTextContent(diagramModelNote);
                if (result == null) result = caseTextPosition(diagramModelNote);
                if (result == null) result = caseProperties(diagramModelNote);
                if (result == null) result = caseBorderType(diagramModelNote);
                if (result == null) result = caseIconic(diagramModelNote);
                if (result == null) result = caseDiagramModelObject(diagramModelNote);
                if (result == null) result = caseConnectable(diagramModelNote);
                if (result == null) result = caseFontAttribute(diagramModelNote);
                if (result == null) result = caseLineObject(diagramModelNote);
                if (result == null) result = caseTextAlignment(diagramModelNote);
                if (result == null) result = caseDiagramModelImageProvider(diagramModelNote);
                if (result == null) result = caseDiagramModelComponent(diagramModelNote);
                if (result == null) result = caseCloneable(diagramModelNote);
                if (result == null) result = caseArchimateModelObject(diagramModelNote);
                if (result == null) result = caseIdentifier(diagramModelNote);
                if (result == null) result = caseAdapter(diagramModelNote);
                if (result == null) result = caseNameable(diagramModelNote);
                if (result == null) result = caseFeatures(diagramModelNote);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_IMAGE: {
                IDiagramModelImage diagramModelImage = (IDiagramModelImage)theEObject;
                T result = caseDiagramModelImage(diagramModelImage);
                if (result == null) result = caseDiagramModelObject(diagramModelImage);
                if (result == null) result = caseBorderObject(diagramModelImage);
                if (result == null) result = caseDiagramModelImageProvider(diagramModelImage);
                if (result == null) result = caseProperties(diagramModelImage);
                if (result == null) result = caseDocumentable(diagramModelImage);
                if (result == null) result = caseConnectable(diagramModelImage);
                if (result == null) result = caseFontAttribute(diagramModelImage);
                if (result == null) result = caseLineObject(diagramModelImage);
                if (result == null) result = caseTextAlignment(diagramModelImage);
                if (result == null) result = caseDiagramModelComponent(diagramModelImage);
                if (result == null) result = caseCloneable(diagramModelImage);
                if (result == null) result = caseArchimateModelObject(diagramModelImage);
                if (result == null) result = caseIdentifier(diagramModelImage);
                if (result == null) result = caseAdapter(diagramModelImage);
                if (result == null) result = caseNameable(diagramModelImage);
                if (result == null) result = caseFeatures(diagramModelImage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION: {
                IDiagramModelConnection diagramModelConnection = (IDiagramModelConnection)theEObject;
                T result = caseDiagramModelConnection(diagramModelConnection);
                if (result == null) result = caseConnectable(diagramModelConnection);
                if (result == null) result = caseFontAttribute(diagramModelConnection);
                if (result == null) result = caseProperties(diagramModelConnection);
                if (result == null) result = caseDocumentable(diagramModelConnection);
                if (result == null) result = caseLineObject(diagramModelConnection);
                if (result == null) result = caseDiagramModelComponent(diagramModelConnection);
                if (result == null) result = caseCloneable(diagramModelConnection);
                if (result == null) result = caseArchimateModelObject(diagramModelConnection);
                if (result == null) result = caseIdentifier(diagramModelConnection);
                if (result == null) result = caseAdapter(diagramModelConnection);
                if (result == null) result = caseNameable(diagramModelConnection);
                if (result == null) result = caseFeatures(diagramModelConnection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_BENDPOINT: {
                IDiagramModelBendpoint diagramModelBendpoint = (IDiagramModelBendpoint)theEObject;
                T result = caseDiagramModelBendpoint(diagramModelBendpoint);
                if (result == null) result = caseCloneable(diagramModelBendpoint);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.LINE_OBJECT: {
                ILineObject lineObject = (ILineObject)theEObject;
                T result = caseLineObject(lineObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FONT_ATTRIBUTE: {
                IFontAttribute fontAttribute = (IFontAttribute)theEObject;
                T result = caseFontAttribute(fontAttribute);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TEXT_POSITION: {
                ITextPosition textPosition = (ITextPosition)theEObject;
                T result = caseTextPosition(textPosition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TEXT_ALIGNMENT: {
                ITextAlignment textAlignment = (ITextAlignment)theEObject;
                T result = caseTextAlignment(textAlignment);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BORDER_OBJECT: {
                IBorderObject borderObject = (IBorderObject)theEObject;
                T result = caseBorderObject(borderObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BORDER_TYPE: {
                IBorderType borderType = (IBorderType)theEObject;
                T result = caseBorderType(borderType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER: {
                IDiagramModelImageProvider diagramModelImageProvider = (IDiagramModelImageProvider)theEObject;
                T result = caseDiagramModelImageProvider(diagramModelImageProvider);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BOUNDS: {
                IBounds bounds = (IBounds)theEObject;
                T result = caseBounds(bounds);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.LOCKABLE: {
                ILockable lockable = (ILockable)theEObject;
                T result = caseLockable(lockable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ICONIC: {
                IIconic iconic = (IIconic)theEObject;
                T result = caseIconic(iconic);
                if (result == null) result = caseDiagramModelObject(iconic);
                if (result == null) result = caseDiagramModelImageProvider(iconic);
                if (result == null) result = caseConnectable(iconic);
                if (result == null) result = caseFontAttribute(iconic);
                if (result == null) result = caseLineObject(iconic);
                if (result == null) result = caseTextAlignment(iconic);
                if (result == null) result = caseDiagramModelComponent(iconic);
                if (result == null) result = caseCloneable(iconic);
                if (result == null) result = caseArchimateModelObject(iconic);
                if (result == null) result = caseIdentifier(iconic);
                if (result == null) result = caseAdapter(iconic);
                if (result == null) result = caseNameable(iconic);
                if (result == null) result = caseFeatures(iconic);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL: {
                IArchimateDiagramModel archimateDiagramModel = (IArchimateDiagramModel)theEObject;
                T result = caseArchimateDiagramModel(archimateDiagramModel);
                if (result == null) result = caseDiagramModel(archimateDiagramModel);
                if (result == null) result = caseDiagramModelContainer(archimateDiagramModel);
                if (result == null) result = caseDocumentable(archimateDiagramModel);
                if (result == null) result = caseProperties(archimateDiagramModel);
                if (result == null) result = caseFeatures(archimateDiagramModel);
                if (result == null) result = caseDiagramModelComponent(archimateDiagramModel);
                if (result == null) result = caseArchimateModelObject(archimateDiagramModel);
                if (result == null) result = caseAdapter(archimateDiagramModel);
                if (result == null) result = caseNameable(archimateDiagramModel);
                if (result == null) result = caseIdentifier(archimateDiagramModel);
                if (result == null) result = caseCloneable(archimateDiagramModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_COMPONENT: {
                IDiagramModelArchimateComponent diagramModelArchimateComponent = (IDiagramModelArchimateComponent)theEObject;
                T result = caseDiagramModelArchimateComponent(diagramModelArchimateComponent);
                if (result == null) result = caseConnectable(diagramModelArchimateComponent);
                if (result == null) result = caseDiagramModelComponent(diagramModelArchimateComponent);
                if (result == null) result = caseCloneable(diagramModelArchimateComponent);
                if (result == null) result = caseArchimateModelObject(diagramModelArchimateComponent);
                if (result == null) result = caseIdentifier(diagramModelArchimateComponent);
                if (result == null) result = caseAdapter(diagramModelArchimateComponent);
                if (result == null) result = caseNameable(diagramModelArchimateComponent);
                if (result == null) result = caseFeatures(diagramModelArchimateComponent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT: {
                IDiagramModelArchimateObject diagramModelArchimateObject = (IDiagramModelArchimateObject)theEObject;
                T result = caseDiagramModelArchimateObject(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelContainer(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelArchimateComponent(diagramModelArchimateObject);
                if (result == null) result = caseTextPosition(diagramModelArchimateObject);
                if (result == null) result = caseIconic(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelObject(diagramModelArchimateObject);
                if (result == null) result = caseConnectable(diagramModelArchimateObject);
                if (result == null) result = caseFontAttribute(diagramModelArchimateObject);
                if (result == null) result = caseLineObject(diagramModelArchimateObject);
                if (result == null) result = caseTextAlignment(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelImageProvider(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelComponent(diagramModelArchimateObject);
                if (result == null) result = caseCloneable(diagramModelArchimateObject);
                if (result == null) result = caseArchimateModelObject(diagramModelArchimateObject);
                if (result == null) result = caseIdentifier(diagramModelArchimateObject);
                if (result == null) result = caseAdapter(diagramModelArchimateObject);
                if (result == null) result = caseNameable(diagramModelArchimateObject);
                if (result == null) result = caseFeatures(diagramModelArchimateObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION: {
                IDiagramModelArchimateConnection diagramModelArchimateConnection = (IDiagramModelArchimateConnection)theEObject;
                T result = caseDiagramModelArchimateConnection(diagramModelArchimateConnection);
                if (result == null) result = caseDiagramModelConnection(diagramModelArchimateConnection);
                if (result == null) result = caseDiagramModelArchimateComponent(diagramModelArchimateConnection);
                if (result == null) result = caseConnectable(diagramModelArchimateConnection);
                if (result == null) result = caseFontAttribute(diagramModelArchimateConnection);
                if (result == null) result = caseProperties(diagramModelArchimateConnection);
                if (result == null) result = caseDocumentable(diagramModelArchimateConnection);
                if (result == null) result = caseLineObject(diagramModelArchimateConnection);
                if (result == null) result = caseDiagramModelComponent(diagramModelArchimateConnection);
                if (result == null) result = caseCloneable(diagramModelArchimateConnection);
                if (result == null) result = caseArchimateModelObject(diagramModelArchimateConnection);
                if (result == null) result = caseIdentifier(diagramModelArchimateConnection);
                if (result == null) result = caseAdapter(diagramModelArchimateConnection);
                if (result == null) result = caseNameable(diagramModelArchimateConnection);
                if (result == null) result = caseFeatures(diagramModelArchimateConnection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SKETCH_MODEL: {
                ISketchModel sketchModel = (ISketchModel)theEObject;
                T result = caseSketchModel(sketchModel);
                if (result == null) result = caseDiagramModel(sketchModel);
                if (result == null) result = caseDiagramModelContainer(sketchModel);
                if (result == null) result = caseDocumentable(sketchModel);
                if (result == null) result = caseProperties(sketchModel);
                if (result == null) result = caseFeatures(sketchModel);
                if (result == null) result = caseDiagramModelComponent(sketchModel);
                if (result == null) result = caseArchimateModelObject(sketchModel);
                if (result == null) result = caseAdapter(sketchModel);
                if (result == null) result = caseNameable(sketchModel);
                if (result == null) result = caseIdentifier(sketchModel);
                if (result == null) result = caseCloneable(sketchModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SKETCH_MODEL_STICKY: {
                ISketchModelSticky sketchModelSticky = (ISketchModelSticky)theEObject;
                T result = caseSketchModelSticky(sketchModelSticky);
                if (result == null) result = caseDiagramModelContainer(sketchModelSticky);
                if (result == null) result = caseTextContent(sketchModelSticky);
                if (result == null) result = caseProperties(sketchModelSticky);
                if (result == null) result = caseTextPosition(sketchModelSticky);
                if (result == null) result = caseIconic(sketchModelSticky);
                if (result == null) result = caseDiagramModelObject(sketchModelSticky);
                if (result == null) result = caseConnectable(sketchModelSticky);
                if (result == null) result = caseFontAttribute(sketchModelSticky);
                if (result == null) result = caseLineObject(sketchModelSticky);
                if (result == null) result = caseTextAlignment(sketchModelSticky);
                if (result == null) result = caseDiagramModelImageProvider(sketchModelSticky);
                if (result == null) result = caseDiagramModelComponent(sketchModelSticky);
                if (result == null) result = caseCloneable(sketchModelSticky);
                if (result == null) result = caseArchimateModelObject(sketchModelSticky);
                if (result == null) result = caseIdentifier(sketchModelSticky);
                if (result == null) result = caseAdapter(sketchModelSticky);
                if (result == null) result = caseNameable(sketchModelSticky);
                if (result == null) result = caseFeatures(sketchModelSticky);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SKETCH_MODEL_ACTOR: {
                ISketchModelActor sketchModelActor = (ISketchModelActor)theEObject;
                T result = caseSketchModelActor(sketchModelActor);
                if (result == null) result = caseDiagramModelObject(sketchModelActor);
                if (result == null) result = caseDocumentable(sketchModelActor);
                if (result == null) result = caseProperties(sketchModelActor);
                if (result == null) result = caseConnectable(sketchModelActor);
                if (result == null) result = caseFontAttribute(sketchModelActor);
                if (result == null) result = caseLineObject(sketchModelActor);
                if (result == null) result = caseTextAlignment(sketchModelActor);
                if (result == null) result = caseDiagramModelComponent(sketchModelActor);
                if (result == null) result = caseCloneable(sketchModelActor);
                if (result == null) result = caseArchimateModelObject(sketchModelActor);
                if (result == null) result = caseIdentifier(sketchModelActor);
                if (result == null) result = caseAdapter(sketchModelActor);
                if (result == null) result = caseNameable(sketchModelActor);
                if (result == null) result = caseFeatures(sketchModelActor);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Adapter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Adapter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAdapter(IAdapter object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identifier</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identifier</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIdentifier(IIdentifier object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Property</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProperty(IProperty object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Properties</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Properties</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProperties(IProperties object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeature(IFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Features</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Features</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatures(IFeatures object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadata(IMetadata object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Nameable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Nameable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNameable(INameable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Content</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Content</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextContent(ITextContent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Documentable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Documentable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentable(IDocumentable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cloneable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cloneable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCloneable(ICloneable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Folder Container</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Folder Container</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFolderContainer(IFolderContainer object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateModel(IArchimateModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Junction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Junction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJunction(IJunction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Resource</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Resource</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResource(IResource object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Folder</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Folder</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFolder(IFolder object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateModelObject(IArchimateModelObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concept</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concept</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateConcept(IArchimateConcept object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateElement(IArchimateElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Artifact</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Artifact</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArtifact(IArtifact object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNode(INode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Outcome</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Outcome</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOutcome(IOutcome object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>System Software</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>System Software</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSystemSoftware(ISystemSoftware object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Collaboration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Collaboration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyCollaboration(ITechnologyCollaboration object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyEvent(ITechnologyEvent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyFunction(ITechnologyFunction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Interface</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Interface</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyInterface(ITechnologyInterface object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Interaction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Interaction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyInteraction(ITechnologyInteraction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyObject(ITechnologyObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Process</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Process</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyProcess(ITechnologyProcess object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Service</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Service</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyService(ITechnologyService object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Device</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Device</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDevice(IDevice object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Distribution Network</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Distribution Network</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDistributionNetwork(IDistributionNetwork object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Equipment</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Equipment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEquipment(IEquipment object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Facility</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Facility</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFacility(IFacility object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Motivation Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Motivation Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMotivationElement(IMotivationElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Stakeholder</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Stakeholder</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStakeholder(IStakeholder object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Driver</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Driver</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDriver(IDriver object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assessment</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assessment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssessment(IAssessment object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Goal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Goal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGoal(IGoal object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grouping</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grouping</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGrouping(IGrouping object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Implementation Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Implementation Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImplementationEvent(IImplementationEvent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirement(IRequirement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constraint</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constraint</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstraint(IConstraint object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Course Of Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Course Of Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCourseOfAction(ICourseOfAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Principle</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Principle</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePrinciple(IPrinciple object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Implementation Migration Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Implementation Migration Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImplementationMigrationElement(IImplementationMigrationElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeElement(ICompositeElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Behavior Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Behavior Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBehaviorElement(IBehaviorElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Strategy Behavior Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Strategy Behavior Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStrategyBehaviorElement(IStrategyBehaviorElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Structure Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Structure Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStructureElement(IStructureElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Active Structure Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Active Structure Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActiveStructureElement(IActiveStructureElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Passive Structure Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Passive Structure Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePassiveStructureElement(IPassiveStructureElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Structural Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Structural Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStructuralRelationship(IStructuralRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dependendency Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dependendency Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDependendencyRelationship(IDependendencyRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dynamic Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dynamic Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDynamicRelationship(IDynamicRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Other Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Other Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOtherRelationship(IOtherRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Strategy Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Strategy Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStrategyElement(IStrategyElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Work Package</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Work Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWorkPackage(IWorkPackage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Deliverable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Deliverable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeliverable(IDeliverable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Plateau</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Plateau</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePlateau(IPlateau object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Gap</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Gap</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGap(IGap object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateRelationship(IArchimateRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModel(IDiagramModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateDiagramModel(IArchimateDiagramModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Archimate Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Archimate Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelArchimateComponent(IDiagramModelArchimateComponent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelReference(IDiagramModelReference object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelComponent(IDiagramModelComponent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connectable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connectable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectable(IConnectable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelObject(IDiagramModelObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Archimate Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Archimate Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelArchimateObject(IDiagramModelArchimateObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Container</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Container</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelContainer(IDiagramModelContainer object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Group</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Group</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelGroup(IDiagramModelGroup object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Note</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Note</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelNote(IDiagramModelNote object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Image</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Image</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelImage(IDiagramModelImage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Connection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelConnection(IDiagramModelConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Archimate Connection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Archimate Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelArchimateConnection(IDiagramModelArchimateConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Bendpoint</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Bendpoint</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelBendpoint(IDiagramModelBendpoint object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Line Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Line Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLineObject(ILineObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Font Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Font Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFontAttribute(IFontAttribute object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Position</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Position</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextPosition(ITextPosition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Alignment</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Alignment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextAlignment(ITextAlignment object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Border Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Border Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBorderObject(IBorderObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Border Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Border Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBorderType(IBorderType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Diagram Model Image Provider</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Diagram Model Image Provider</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiagramModelImageProvider(IDiagramModelImageProvider object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bounds</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bounds</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBounds(IBounds object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Lockable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Lockable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLockable(ILockable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iconic</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iconic</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIconic(IIconic object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sketch Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sketch Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSketchModel(ISketchModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sketch Model Sticky</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sketch Model Sticky</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSketchModelSticky(ISketchModelSticky object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sketch Model Actor</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sketch Model Actor</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSketchModelActor(ISketchModelActor object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Actor</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Actor</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessActor(IBusinessActor object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Collaboration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Collaboration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessCollaboration(IBusinessCollaboration object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Contract</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Contract</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContract(IContract object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessEvent(IBusinessEvent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessFunction(IBusinessFunction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Interaction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Interaction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessInteraction(IBusinessInteraction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Interface</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Interface</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessInterface(IBusinessInterface object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Meaning</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Meaning</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMeaning(IMeaning object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessObject(IBusinessObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Process</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Process</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessProcess(IBusinessProcess object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Product</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Product</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProduct(IProduct object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Representation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Representation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRepresentation(IRepresentation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Role</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Role</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessRole(IBusinessRole object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Service</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Service</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessService(IBusinessService object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Capability</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Capability</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCapability(ICapability object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Communication Network</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Communication Network</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCommunicationNetwork(ICommunicationNetwork object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValue(IValue object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Stream</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Stream</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValueStream(IValueStream object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Location</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Location</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLocation(ILocation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Material</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Material</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaterial(IMaterial object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationComponent(IApplicationComponent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationEvent(IApplicationEvent object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationFunction(IApplicationFunction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Interaction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Interaction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationInteraction(IApplicationInteraction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Interface</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Interface</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationInterface(IApplicationInterface object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Process</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Process</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationProcess(IApplicationProcess object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataObject(IDataObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Service</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Service</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationService(IApplicationService object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Collaboration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Collaboration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationCollaboration(IApplicationCollaboration object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Access Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Access Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAccessRelationship(IAccessRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Aggregation Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Aggregation Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAggregationRelationship(IAggregationRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignmentRelationship(IAssignmentRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Association Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Association Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssociationRelationship(IAssociationRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composition Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composition Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositionRelationship(ICompositionRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Flow Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Flow Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFlowRelationship(IFlowRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Triggering Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Triggering Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTriggeringRelationship(ITriggeringRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Influence Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Influence Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInfluenceRelationship(IInfluenceRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Realization Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Realization Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRealizationRelationship(IRealizationRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Serving Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Serving Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseServingRelationship(IServingRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specialization Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specialization Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecializationRelationship(ISpecializationRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessElement(IBusinessElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationElement(IApplicationElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyElement(ITechnologyElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Physical Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Physical Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePhysicalElement(IPhysicalElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Path</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Path</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePath(IPath object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //ArchimateSwitch
