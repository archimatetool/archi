/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

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
     * @parameter ePackage the package in question.
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
            case IArchimatePackage.ARCHIMATE_MODEL: {
                IArchimateModel archimateModel = (IArchimateModel)theEObject;
                T result = caseArchimateModel(archimateModel);
                if (result == null) result = caseFolderContainer(archimateModel);
                if (result == null) result = caseNameable(archimateModel);
                if (result == null) result = caseIdentifier(archimateModel);
                if (result == null) result = caseArchimateModelElement(archimateModel);
                if (result == null) result = caseProperties(archimateModel);
                if (result == null) result = caseAdapter(archimateModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_MODEL_ELEMENT: {
                IArchimateModelElement archimateModelElement = (IArchimateModelElement)theEObject;
                T result = caseArchimateModelElement(archimateModelElement);
                if (result == null) result = caseAdapter(archimateModelElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FOLDER: {
                IFolder folder = (IFolder)theEObject;
                T result = caseFolder(folder);
                if (result == null) result = caseArchimateModelElement(folder);
                if (result == null) result = caseFolderContainer(folder);
                if (result == null) result = caseNameable(folder);
                if (result == null) result = caseIdentifier(folder);
                if (result == null) result = caseDocumentable(folder);
                if (result == null) result = caseProperties(folder);
                if (result == null) result = caseAdapter(folder);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARCHIMATE_ELEMENT: {
                IArchimateElement archimateElement = (IArchimateElement)theEObject;
                T result = caseArchimateElement(archimateElement);
                if (result == null) result = caseArchimateModelElement(archimateElement);
                if (result == null) result = caseIdentifier(archimateElement);
                if (result == null) result = caseCloneable(archimateElement);
                if (result == null) result = caseNameable(archimateElement);
                if (result == null) result = caseDocumentable(archimateElement);
                if (result == null) result = caseProperties(archimateElement);
                if (result == null) result = caseAdapter(archimateElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.JUNCTION_ELEMENT: {
                IJunctionElement junctionElement = (IJunctionElement)theEObject;
                T result = caseJunctionElement(junctionElement);
                if (result == null) result = caseArchimateElement(junctionElement);
                if (result == null) result = caseArchimateModelElement(junctionElement);
                if (result == null) result = caseIdentifier(junctionElement);
                if (result == null) result = caseCloneable(junctionElement);
                if (result == null) result = caseNameable(junctionElement);
                if (result == null) result = caseDocumentable(junctionElement);
                if (result == null) result = caseProperties(junctionElement);
                if (result == null) result = caseAdapter(junctionElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.INTERFACE_ELEMENT: {
                IInterfaceElement interfaceElement = (IInterfaceElement)theEObject;
                T result = caseInterfaceElement(interfaceElement);
                if (result == null) result = caseArchimateElement(interfaceElement);
                if (result == null) result = caseArchimateModelElement(interfaceElement);
                if (result == null) result = caseIdentifier(interfaceElement);
                if (result == null) result = caseCloneable(interfaceElement);
                if (result == null) result = caseNameable(interfaceElement);
                if (result == null) result = caseDocumentable(interfaceElement);
                if (result == null) result = caseProperties(interfaceElement);
                if (result == null) result = caseAdapter(interfaceElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SERVICE_ELEMENT: {
                IServiceElement serviceElement = (IServiceElement)theEObject;
                T result = caseServiceElement(serviceElement);
                if (result == null) result = caseArchimateElement(serviceElement);
                if (result == null) result = caseArchimateModelElement(serviceElement);
                if (result == null) result = caseIdentifier(serviceElement);
                if (result == null) result = caseCloneable(serviceElement);
                if (result == null) result = caseNameable(serviceElement);
                if (result == null) result = caseDocumentable(serviceElement);
                if (result == null) result = caseProperties(serviceElement);
                if (result == null) result = caseAdapter(serviceElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.JUNCTION: {
                IJunction junction = (IJunction)theEObject;
                T result = caseJunction(junction);
                if (result == null) result = caseJunctionElement(junction);
                if (result == null) result = caseArchimateElement(junction);
                if (result == null) result = caseArchimateModelElement(junction);
                if (result == null) result = caseIdentifier(junction);
                if (result == null) result = caseCloneable(junction);
                if (result == null) result = caseNameable(junction);
                if (result == null) result = caseDocumentable(junction);
                if (result == null) result = caseProperties(junction);
                if (result == null) result = caseAdapter(junction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.AND_JUNCTION: {
                IAndJunction andJunction = (IAndJunction)theEObject;
                T result = caseAndJunction(andJunction);
                if (result == null) result = caseJunctionElement(andJunction);
                if (result == null) result = caseArchimateElement(andJunction);
                if (result == null) result = caseArchimateModelElement(andJunction);
                if (result == null) result = caseIdentifier(andJunction);
                if (result == null) result = caseCloneable(andJunction);
                if (result == null) result = caseNameable(andJunction);
                if (result == null) result = caseDocumentable(andJunction);
                if (result == null) result = caseProperties(andJunction);
                if (result == null) result = caseAdapter(andJunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.OR_JUNCTION: {
                IOrJunction orJunction = (IOrJunction)theEObject;
                T result = caseOrJunction(orJunction);
                if (result == null) result = caseJunctionElement(orJunction);
                if (result == null) result = caseArchimateElement(orJunction);
                if (result == null) result = caseArchimateModelElement(orJunction);
                if (result == null) result = caseIdentifier(orJunction);
                if (result == null) result = caseCloneable(orJunction);
                if (result == null) result = caseNameable(orJunction);
                if (result == null) result = caseDocumentable(orJunction);
                if (result == null) result = caseProperties(orJunction);
                if (result == null) result = caseAdapter(orJunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.RELATIONSHIP: {
                IRelationship relationship = (IRelationship)theEObject;
                T result = caseRelationship(relationship);
                if (result == null) result = caseArchimateElement(relationship);
                if (result == null) result = caseArchimateModelElement(relationship);
                if (result == null) result = caseIdentifier(relationship);
                if (result == null) result = caseCloneable(relationship);
                if (result == null) result = caseNameable(relationship);
                if (result == null) result = caseDocumentable(relationship);
                if (result == null) result = caseProperties(relationship);
                if (result == null) result = caseAdapter(relationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ACCESS_RELATIONSHIP: {
                IAccessRelationship accessRelationship = (IAccessRelationship)theEObject;
                T result = caseAccessRelationship(accessRelationship);
                if (result == null) result = caseRelationship(accessRelationship);
                if (result == null) result = caseArchimateElement(accessRelationship);
                if (result == null) result = caseArchimateModelElement(accessRelationship);
                if (result == null) result = caseIdentifier(accessRelationship);
                if (result == null) result = caseCloneable(accessRelationship);
                if (result == null) result = caseNameable(accessRelationship);
                if (result == null) result = caseDocumentable(accessRelationship);
                if (result == null) result = caseProperties(accessRelationship);
                if (result == null) result = caseAdapter(accessRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.AGGREGATION_RELATIONSHIP: {
                IAggregationRelationship aggregationRelationship = (IAggregationRelationship)theEObject;
                T result = caseAggregationRelationship(aggregationRelationship);
                if (result == null) result = caseRelationship(aggregationRelationship);
                if (result == null) result = caseArchimateElement(aggregationRelationship);
                if (result == null) result = caseArchimateModelElement(aggregationRelationship);
                if (result == null) result = caseIdentifier(aggregationRelationship);
                if (result == null) result = caseCloneable(aggregationRelationship);
                if (result == null) result = caseNameable(aggregationRelationship);
                if (result == null) result = caseDocumentable(aggregationRelationship);
                if (result == null) result = caseProperties(aggregationRelationship);
                if (result == null) result = caseAdapter(aggregationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP: {
                IAssignmentRelationship assignmentRelationship = (IAssignmentRelationship)theEObject;
                T result = caseAssignmentRelationship(assignmentRelationship);
                if (result == null) result = caseRelationship(assignmentRelationship);
                if (result == null) result = caseArchimateElement(assignmentRelationship);
                if (result == null) result = caseArchimateModelElement(assignmentRelationship);
                if (result == null) result = caseIdentifier(assignmentRelationship);
                if (result == null) result = caseCloneable(assignmentRelationship);
                if (result == null) result = caseNameable(assignmentRelationship);
                if (result == null) result = caseDocumentable(assignmentRelationship);
                if (result == null) result = caseProperties(assignmentRelationship);
                if (result == null) result = caseAdapter(assignmentRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP: {
                IAssociationRelationship associationRelationship = (IAssociationRelationship)theEObject;
                T result = caseAssociationRelationship(associationRelationship);
                if (result == null) result = caseRelationship(associationRelationship);
                if (result == null) result = caseArchimateElement(associationRelationship);
                if (result == null) result = caseArchimateModelElement(associationRelationship);
                if (result == null) result = caseIdentifier(associationRelationship);
                if (result == null) result = caseCloneable(associationRelationship);
                if (result == null) result = caseNameable(associationRelationship);
                if (result == null) result = caseDocumentable(associationRelationship);
                if (result == null) result = caseProperties(associationRelationship);
                if (result == null) result = caseAdapter(associationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.COMPOSITION_RELATIONSHIP: {
                ICompositionRelationship compositionRelationship = (ICompositionRelationship)theEObject;
                T result = caseCompositionRelationship(compositionRelationship);
                if (result == null) result = caseRelationship(compositionRelationship);
                if (result == null) result = caseArchimateElement(compositionRelationship);
                if (result == null) result = caseArchimateModelElement(compositionRelationship);
                if (result == null) result = caseIdentifier(compositionRelationship);
                if (result == null) result = caseCloneable(compositionRelationship);
                if (result == null) result = caseNameable(compositionRelationship);
                if (result == null) result = caseDocumentable(compositionRelationship);
                if (result == null) result = caseProperties(compositionRelationship);
                if (result == null) result = caseAdapter(compositionRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.FLOW_RELATIONSHIP: {
                IFlowRelationship flowRelationship = (IFlowRelationship)theEObject;
                T result = caseFlowRelationship(flowRelationship);
                if (result == null) result = caseRelationship(flowRelationship);
                if (result == null) result = caseArchimateElement(flowRelationship);
                if (result == null) result = caseArchimateModelElement(flowRelationship);
                if (result == null) result = caseIdentifier(flowRelationship);
                if (result == null) result = caseCloneable(flowRelationship);
                if (result == null) result = caseNameable(flowRelationship);
                if (result == null) result = caseDocumentable(flowRelationship);
                if (result == null) result = caseProperties(flowRelationship);
                if (result == null) result = caseAdapter(flowRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.REALISATION_RELATIONSHIP: {
                IRealisationRelationship realisationRelationship = (IRealisationRelationship)theEObject;
                T result = caseRealisationRelationship(realisationRelationship);
                if (result == null) result = caseRelationship(realisationRelationship);
                if (result == null) result = caseArchimateElement(realisationRelationship);
                if (result == null) result = caseArchimateModelElement(realisationRelationship);
                if (result == null) result = caseIdentifier(realisationRelationship);
                if (result == null) result = caseCloneable(realisationRelationship);
                if (result == null) result = caseNameable(realisationRelationship);
                if (result == null) result = caseDocumentable(realisationRelationship);
                if (result == null) result = caseProperties(realisationRelationship);
                if (result == null) result = caseAdapter(realisationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SPECIALISATION_RELATIONSHIP: {
                ISpecialisationRelationship specialisationRelationship = (ISpecialisationRelationship)theEObject;
                T result = caseSpecialisationRelationship(specialisationRelationship);
                if (result == null) result = caseRelationship(specialisationRelationship);
                if (result == null) result = caseArchimateElement(specialisationRelationship);
                if (result == null) result = caseArchimateModelElement(specialisationRelationship);
                if (result == null) result = caseIdentifier(specialisationRelationship);
                if (result == null) result = caseCloneable(specialisationRelationship);
                if (result == null) result = caseNameable(specialisationRelationship);
                if (result == null) result = caseDocumentable(specialisationRelationship);
                if (result == null) result = caseProperties(specialisationRelationship);
                if (result == null) result = caseAdapter(specialisationRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TRIGGERING_RELATIONSHIP: {
                ITriggeringRelationship triggeringRelationship = (ITriggeringRelationship)theEObject;
                T result = caseTriggeringRelationship(triggeringRelationship);
                if (result == null) result = caseRelationship(triggeringRelationship);
                if (result == null) result = caseArchimateElement(triggeringRelationship);
                if (result == null) result = caseArchimateModelElement(triggeringRelationship);
                if (result == null) result = caseIdentifier(triggeringRelationship);
                if (result == null) result = caseCloneable(triggeringRelationship);
                if (result == null) result = caseNameable(triggeringRelationship);
                if (result == null) result = caseDocumentable(triggeringRelationship);
                if (result == null) result = caseProperties(triggeringRelationship);
                if (result == null) result = caseAdapter(triggeringRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.USED_BY_RELATIONSHIP: {
                IUsedByRelationship usedByRelationship = (IUsedByRelationship)theEObject;
                T result = caseUsedByRelationship(usedByRelationship);
                if (result == null) result = caseRelationship(usedByRelationship);
                if (result == null) result = caseArchimateElement(usedByRelationship);
                if (result == null) result = caseArchimateModelElement(usedByRelationship);
                if (result == null) result = caseIdentifier(usedByRelationship);
                if (result == null) result = caseCloneable(usedByRelationship);
                if (result == null) result = caseNameable(usedByRelationship);
                if (result == null) result = caseDocumentable(usedByRelationship);
                if (result == null) result = caseProperties(usedByRelationship);
                if (result == null) result = caseAdapter(usedByRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.INFLUENCE_RELATIONSHIP: {
                IInfluenceRelationship influenceRelationship = (IInfluenceRelationship)theEObject;
                T result = caseInfluenceRelationship(influenceRelationship);
                if (result == null) result = caseRelationship(influenceRelationship);
                if (result == null) result = caseArchimateElement(influenceRelationship);
                if (result == null) result = caseArchimateModelElement(influenceRelationship);
                if (result == null) result = caseIdentifier(influenceRelationship);
                if (result == null) result = caseCloneable(influenceRelationship);
                if (result == null) result = caseNameable(influenceRelationship);
                if (result == null) result = caseDocumentable(influenceRelationship);
                if (result == null) result = caseProperties(influenceRelationship);
                if (result == null) result = caseAdapter(influenceRelationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_LAYER_ELEMENT: {
                IBusinessLayerElement businessLayerElement = (IBusinessLayerElement)theEObject;
                T result = caseBusinessLayerElement(businessLayerElement);
                if (result == null) result = caseArchimateElement(businessLayerElement);
                if (result == null) result = caseArchimateModelElement(businessLayerElement);
                if (result == null) result = caseIdentifier(businessLayerElement);
                if (result == null) result = caseCloneable(businessLayerElement);
                if (result == null) result = caseNameable(businessLayerElement);
                if (result == null) result = caseDocumentable(businessLayerElement);
                if (result == null) result = caseProperties(businessLayerElement);
                if (result == null) result = caseAdapter(businessLayerElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_ACTIVITY: {
                IBusinessActivity businessActivity = (IBusinessActivity)theEObject;
                T result = caseBusinessActivity(businessActivity);
                if (result == null) result = caseBusinessLayerElement(businessActivity);
                if (result == null) result = caseArchimateElement(businessActivity);
                if (result == null) result = caseArchimateModelElement(businessActivity);
                if (result == null) result = caseIdentifier(businessActivity);
                if (result == null) result = caseCloneable(businessActivity);
                if (result == null) result = caseNameable(businessActivity);
                if (result == null) result = caseDocumentable(businessActivity);
                if (result == null) result = caseProperties(businessActivity);
                if (result == null) result = caseAdapter(businessActivity);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_ACTOR: {
                IBusinessActor businessActor = (IBusinessActor)theEObject;
                T result = caseBusinessActor(businessActor);
                if (result == null) result = caseBusinessLayerElement(businessActor);
                if (result == null) result = caseArchimateElement(businessActor);
                if (result == null) result = caseArchimateModelElement(businessActor);
                if (result == null) result = caseIdentifier(businessActor);
                if (result == null) result = caseCloneable(businessActor);
                if (result == null) result = caseNameable(businessActor);
                if (result == null) result = caseDocumentable(businessActor);
                if (result == null) result = caseProperties(businessActor);
                if (result == null) result = caseAdapter(businessActor);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_COLLABORATION: {
                IBusinessCollaboration businessCollaboration = (IBusinessCollaboration)theEObject;
                T result = caseBusinessCollaboration(businessCollaboration);
                if (result == null) result = caseBusinessLayerElement(businessCollaboration);
                if (result == null) result = caseArchimateElement(businessCollaboration);
                if (result == null) result = caseArchimateModelElement(businessCollaboration);
                if (result == null) result = caseIdentifier(businessCollaboration);
                if (result == null) result = caseCloneable(businessCollaboration);
                if (result == null) result = caseNameable(businessCollaboration);
                if (result == null) result = caseDocumentable(businessCollaboration);
                if (result == null) result = caseProperties(businessCollaboration);
                if (result == null) result = caseAdapter(businessCollaboration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CONTRACT: {
                IContract contract = (IContract)theEObject;
                T result = caseContract(contract);
                if (result == null) result = caseBusinessLayerElement(contract);
                if (result == null) result = caseArchimateElement(contract);
                if (result == null) result = caseArchimateModelElement(contract);
                if (result == null) result = caseIdentifier(contract);
                if (result == null) result = caseCloneable(contract);
                if (result == null) result = caseNameable(contract);
                if (result == null) result = caseDocumentable(contract);
                if (result == null) result = caseProperties(contract);
                if (result == null) result = caseAdapter(contract);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_EVENT: {
                IBusinessEvent businessEvent = (IBusinessEvent)theEObject;
                T result = caseBusinessEvent(businessEvent);
                if (result == null) result = caseBusinessLayerElement(businessEvent);
                if (result == null) result = caseArchimateElement(businessEvent);
                if (result == null) result = caseArchimateModelElement(businessEvent);
                if (result == null) result = caseIdentifier(businessEvent);
                if (result == null) result = caseCloneable(businessEvent);
                if (result == null) result = caseNameable(businessEvent);
                if (result == null) result = caseDocumentable(businessEvent);
                if (result == null) result = caseProperties(businessEvent);
                if (result == null) result = caseAdapter(businessEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_FUNCTION: {
                IBusinessFunction businessFunction = (IBusinessFunction)theEObject;
                T result = caseBusinessFunction(businessFunction);
                if (result == null) result = caseBusinessLayerElement(businessFunction);
                if (result == null) result = caseArchimateElement(businessFunction);
                if (result == null) result = caseArchimateModelElement(businessFunction);
                if (result == null) result = caseIdentifier(businessFunction);
                if (result == null) result = caseCloneable(businessFunction);
                if (result == null) result = caseNameable(businessFunction);
                if (result == null) result = caseDocumentable(businessFunction);
                if (result == null) result = caseProperties(businessFunction);
                if (result == null) result = caseAdapter(businessFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_INTERACTION: {
                IBusinessInteraction businessInteraction = (IBusinessInteraction)theEObject;
                T result = caseBusinessInteraction(businessInteraction);
                if (result == null) result = caseBusinessLayerElement(businessInteraction);
                if (result == null) result = caseArchimateElement(businessInteraction);
                if (result == null) result = caseArchimateModelElement(businessInteraction);
                if (result == null) result = caseIdentifier(businessInteraction);
                if (result == null) result = caseCloneable(businessInteraction);
                if (result == null) result = caseNameable(businessInteraction);
                if (result == null) result = caseDocumentable(businessInteraction);
                if (result == null) result = caseProperties(businessInteraction);
                if (result == null) result = caseAdapter(businessInteraction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_INTERFACE: {
                IBusinessInterface businessInterface = (IBusinessInterface)theEObject;
                T result = caseBusinessInterface(businessInterface);
                if (result == null) result = caseBusinessLayerElement(businessInterface);
                if (result == null) result = caseInterfaceElement(businessInterface);
                if (result == null) result = caseArchimateElement(businessInterface);
                if (result == null) result = caseArchimateModelElement(businessInterface);
                if (result == null) result = caseIdentifier(businessInterface);
                if (result == null) result = caseCloneable(businessInterface);
                if (result == null) result = caseNameable(businessInterface);
                if (result == null) result = caseDocumentable(businessInterface);
                if (result == null) result = caseProperties(businessInterface);
                if (result == null) result = caseAdapter(businessInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.MEANING: {
                IMeaning meaning = (IMeaning)theEObject;
                T result = caseMeaning(meaning);
                if (result == null) result = caseBusinessLayerElement(meaning);
                if (result == null) result = caseArchimateElement(meaning);
                if (result == null) result = caseArchimateModelElement(meaning);
                if (result == null) result = caseIdentifier(meaning);
                if (result == null) result = caseCloneable(meaning);
                if (result == null) result = caseNameable(meaning);
                if (result == null) result = caseDocumentable(meaning);
                if (result == null) result = caseProperties(meaning);
                if (result == null) result = caseAdapter(meaning);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_OBJECT: {
                IBusinessObject businessObject = (IBusinessObject)theEObject;
                T result = caseBusinessObject(businessObject);
                if (result == null) result = caseBusinessLayerElement(businessObject);
                if (result == null) result = caseArchimateElement(businessObject);
                if (result == null) result = caseArchimateModelElement(businessObject);
                if (result == null) result = caseIdentifier(businessObject);
                if (result == null) result = caseCloneable(businessObject);
                if (result == null) result = caseNameable(businessObject);
                if (result == null) result = caseDocumentable(businessObject);
                if (result == null) result = caseProperties(businessObject);
                if (result == null) result = caseAdapter(businessObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_PROCESS: {
                IBusinessProcess businessProcess = (IBusinessProcess)theEObject;
                T result = caseBusinessProcess(businessProcess);
                if (result == null) result = caseBusinessLayerElement(businessProcess);
                if (result == null) result = caseArchimateElement(businessProcess);
                if (result == null) result = caseArchimateModelElement(businessProcess);
                if (result == null) result = caseIdentifier(businessProcess);
                if (result == null) result = caseCloneable(businessProcess);
                if (result == null) result = caseNameable(businessProcess);
                if (result == null) result = caseDocumentable(businessProcess);
                if (result == null) result = caseProperties(businessProcess);
                if (result == null) result = caseAdapter(businessProcess);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PRODUCT: {
                IProduct product = (IProduct)theEObject;
                T result = caseProduct(product);
                if (result == null) result = caseBusinessLayerElement(product);
                if (result == null) result = caseArchimateElement(product);
                if (result == null) result = caseArchimateModelElement(product);
                if (result == null) result = caseIdentifier(product);
                if (result == null) result = caseCloneable(product);
                if (result == null) result = caseNameable(product);
                if (result == null) result = caseDocumentable(product);
                if (result == null) result = caseProperties(product);
                if (result == null) result = caseAdapter(product);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.REPRESENTATION: {
                IRepresentation representation = (IRepresentation)theEObject;
                T result = caseRepresentation(representation);
                if (result == null) result = caseBusinessLayerElement(representation);
                if (result == null) result = caseArchimateElement(representation);
                if (result == null) result = caseArchimateModelElement(representation);
                if (result == null) result = caseIdentifier(representation);
                if (result == null) result = caseCloneable(representation);
                if (result == null) result = caseNameable(representation);
                if (result == null) result = caseDocumentable(representation);
                if (result == null) result = caseProperties(representation);
                if (result == null) result = caseAdapter(representation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_ROLE: {
                IBusinessRole businessRole = (IBusinessRole)theEObject;
                T result = caseBusinessRole(businessRole);
                if (result == null) result = caseBusinessLayerElement(businessRole);
                if (result == null) result = caseArchimateElement(businessRole);
                if (result == null) result = caseArchimateModelElement(businessRole);
                if (result == null) result = caseIdentifier(businessRole);
                if (result == null) result = caseCloneable(businessRole);
                if (result == null) result = caseNameable(businessRole);
                if (result == null) result = caseDocumentable(businessRole);
                if (result == null) result = caseProperties(businessRole);
                if (result == null) result = caseAdapter(businessRole);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.BUSINESS_SERVICE: {
                IBusinessService businessService = (IBusinessService)theEObject;
                T result = caseBusinessService(businessService);
                if (result == null) result = caseBusinessLayerElement(businessService);
                if (result == null) result = caseServiceElement(businessService);
                if (result == null) result = caseArchimateElement(businessService);
                if (result == null) result = caseArchimateModelElement(businessService);
                if (result == null) result = caseIdentifier(businessService);
                if (result == null) result = caseCloneable(businessService);
                if (result == null) result = caseNameable(businessService);
                if (result == null) result = caseDocumentable(businessService);
                if (result == null) result = caseProperties(businessService);
                if (result == null) result = caseAdapter(businessService);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.VALUE: {
                IValue value = (IValue)theEObject;
                T result = caseValue(value);
                if (result == null) result = caseBusinessLayerElement(value);
                if (result == null) result = caseArchimateElement(value);
                if (result == null) result = caseArchimateModelElement(value);
                if (result == null) result = caseIdentifier(value);
                if (result == null) result = caseCloneable(value);
                if (result == null) result = caseNameable(value);
                if (result == null) result = caseDocumentable(value);
                if (result == null) result = caseProperties(value);
                if (result == null) result = caseAdapter(value);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.LOCATION: {
                ILocation location = (ILocation)theEObject;
                T result = caseLocation(location);
                if (result == null) result = caseBusinessLayerElement(location);
                if (result == null) result = caseArchimateElement(location);
                if (result == null) result = caseArchimateModelElement(location);
                if (result == null) result = caseIdentifier(location);
                if (result == null) result = caseCloneable(location);
                if (result == null) result = caseNameable(location);
                if (result == null) result = caseDocumentable(location);
                if (result == null) result = caseProperties(location);
                if (result == null) result = caseAdapter(location);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_LAYER_ELEMENT: {
                IApplicationLayerElement applicationLayerElement = (IApplicationLayerElement)theEObject;
                T result = caseApplicationLayerElement(applicationLayerElement);
                if (result == null) result = caseArchimateElement(applicationLayerElement);
                if (result == null) result = caseArchimateModelElement(applicationLayerElement);
                if (result == null) result = caseIdentifier(applicationLayerElement);
                if (result == null) result = caseCloneable(applicationLayerElement);
                if (result == null) result = caseNameable(applicationLayerElement);
                if (result == null) result = caseDocumentable(applicationLayerElement);
                if (result == null) result = caseProperties(applicationLayerElement);
                if (result == null) result = caseAdapter(applicationLayerElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_COLLABORATION: {
                IApplicationCollaboration applicationCollaboration = (IApplicationCollaboration)theEObject;
                T result = caseApplicationCollaboration(applicationCollaboration);
                if (result == null) result = caseApplicationLayerElement(applicationCollaboration);
                if (result == null) result = caseArchimateElement(applicationCollaboration);
                if (result == null) result = caseArchimateModelElement(applicationCollaboration);
                if (result == null) result = caseIdentifier(applicationCollaboration);
                if (result == null) result = caseCloneable(applicationCollaboration);
                if (result == null) result = caseNameable(applicationCollaboration);
                if (result == null) result = caseDocumentable(applicationCollaboration);
                if (result == null) result = caseProperties(applicationCollaboration);
                if (result == null) result = caseAdapter(applicationCollaboration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_COMPONENT: {
                IApplicationComponent applicationComponent = (IApplicationComponent)theEObject;
                T result = caseApplicationComponent(applicationComponent);
                if (result == null) result = caseApplicationLayerElement(applicationComponent);
                if (result == null) result = caseArchimateElement(applicationComponent);
                if (result == null) result = caseArchimateModelElement(applicationComponent);
                if (result == null) result = caseIdentifier(applicationComponent);
                if (result == null) result = caseCloneable(applicationComponent);
                if (result == null) result = caseNameable(applicationComponent);
                if (result == null) result = caseDocumentable(applicationComponent);
                if (result == null) result = caseProperties(applicationComponent);
                if (result == null) result = caseAdapter(applicationComponent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_FUNCTION: {
                IApplicationFunction applicationFunction = (IApplicationFunction)theEObject;
                T result = caseApplicationFunction(applicationFunction);
                if (result == null) result = caseApplicationLayerElement(applicationFunction);
                if (result == null) result = caseArchimateElement(applicationFunction);
                if (result == null) result = caseArchimateModelElement(applicationFunction);
                if (result == null) result = caseIdentifier(applicationFunction);
                if (result == null) result = caseCloneable(applicationFunction);
                if (result == null) result = caseNameable(applicationFunction);
                if (result == null) result = caseDocumentable(applicationFunction);
                if (result == null) result = caseProperties(applicationFunction);
                if (result == null) result = caseAdapter(applicationFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_INTERACTION: {
                IApplicationInteraction applicationInteraction = (IApplicationInteraction)theEObject;
                T result = caseApplicationInteraction(applicationInteraction);
                if (result == null) result = caseApplicationLayerElement(applicationInteraction);
                if (result == null) result = caseArchimateElement(applicationInteraction);
                if (result == null) result = caseArchimateModelElement(applicationInteraction);
                if (result == null) result = caseIdentifier(applicationInteraction);
                if (result == null) result = caseCloneable(applicationInteraction);
                if (result == null) result = caseNameable(applicationInteraction);
                if (result == null) result = caseDocumentable(applicationInteraction);
                if (result == null) result = caseProperties(applicationInteraction);
                if (result == null) result = caseAdapter(applicationInteraction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_INTERFACE: {
                IApplicationInterface applicationInterface = (IApplicationInterface)theEObject;
                T result = caseApplicationInterface(applicationInterface);
                if (result == null) result = caseApplicationLayerElement(applicationInterface);
                if (result == null) result = caseInterfaceElement(applicationInterface);
                if (result == null) result = caseArchimateElement(applicationInterface);
                if (result == null) result = caseArchimateModelElement(applicationInterface);
                if (result == null) result = caseIdentifier(applicationInterface);
                if (result == null) result = caseCloneable(applicationInterface);
                if (result == null) result = caseNameable(applicationInterface);
                if (result == null) result = caseDocumentable(applicationInterface);
                if (result == null) result = caseProperties(applicationInterface);
                if (result == null) result = caseAdapter(applicationInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DATA_OBJECT: {
                IDataObject dataObject = (IDataObject)theEObject;
                T result = caseDataObject(dataObject);
                if (result == null) result = caseApplicationLayerElement(dataObject);
                if (result == null) result = caseArchimateElement(dataObject);
                if (result == null) result = caseArchimateModelElement(dataObject);
                if (result == null) result = caseIdentifier(dataObject);
                if (result == null) result = caseCloneable(dataObject);
                if (result == null) result = caseNameable(dataObject);
                if (result == null) result = caseDocumentable(dataObject);
                if (result == null) result = caseProperties(dataObject);
                if (result == null) result = caseAdapter(dataObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.APPLICATION_SERVICE: {
                IApplicationService applicationService = (IApplicationService)theEObject;
                T result = caseApplicationService(applicationService);
                if (result == null) result = caseApplicationLayerElement(applicationService);
                if (result == null) result = caseServiceElement(applicationService);
                if (result == null) result = caseArchimateElement(applicationService);
                if (result == null) result = caseArchimateModelElement(applicationService);
                if (result == null) result = caseIdentifier(applicationService);
                if (result == null) result = caseCloneable(applicationService);
                if (result == null) result = caseNameable(applicationService);
                if (result == null) result = caseDocumentable(applicationService);
                if (result == null) result = caseProperties(applicationService);
                if (result == null) result = caseAdapter(applicationService);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.TECHNOLOGY_LAYER_ELEMENT: {
                ITechnologyLayerElement technologyLayerElement = (ITechnologyLayerElement)theEObject;
                T result = caseTechnologyLayerElement(technologyLayerElement);
                if (result == null) result = caseArchimateElement(technologyLayerElement);
                if (result == null) result = caseArchimateModelElement(technologyLayerElement);
                if (result == null) result = caseIdentifier(technologyLayerElement);
                if (result == null) result = caseCloneable(technologyLayerElement);
                if (result == null) result = caseNameable(technologyLayerElement);
                if (result == null) result = caseDocumentable(technologyLayerElement);
                if (result == null) result = caseProperties(technologyLayerElement);
                if (result == null) result = caseAdapter(technologyLayerElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ARTIFACT: {
                IArtifact artifact = (IArtifact)theEObject;
                T result = caseArtifact(artifact);
                if (result == null) result = caseTechnologyLayerElement(artifact);
                if (result == null) result = caseArchimateElement(artifact);
                if (result == null) result = caseArchimateModelElement(artifact);
                if (result == null) result = caseIdentifier(artifact);
                if (result == null) result = caseCloneable(artifact);
                if (result == null) result = caseNameable(artifact);
                if (result == null) result = caseDocumentable(artifact);
                if (result == null) result = caseProperties(artifact);
                if (result == null) result = caseAdapter(artifact);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.COMMUNICATION_PATH: {
                ICommunicationPath communicationPath = (ICommunicationPath)theEObject;
                T result = caseCommunicationPath(communicationPath);
                if (result == null) result = caseTechnologyLayerElement(communicationPath);
                if (result == null) result = caseArchimateElement(communicationPath);
                if (result == null) result = caseArchimateModelElement(communicationPath);
                if (result == null) result = caseIdentifier(communicationPath);
                if (result == null) result = caseCloneable(communicationPath);
                if (result == null) result = caseNameable(communicationPath);
                if (result == null) result = caseDocumentable(communicationPath);
                if (result == null) result = caseProperties(communicationPath);
                if (result == null) result = caseAdapter(communicationPath);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.NETWORK: {
                INetwork network = (INetwork)theEObject;
                T result = caseNetwork(network);
                if (result == null) result = caseTechnologyLayerElement(network);
                if (result == null) result = caseArchimateElement(network);
                if (result == null) result = caseArchimateModelElement(network);
                if (result == null) result = caseIdentifier(network);
                if (result == null) result = caseCloneable(network);
                if (result == null) result = caseNameable(network);
                if (result == null) result = caseDocumentable(network);
                if (result == null) result = caseProperties(network);
                if (result == null) result = caseAdapter(network);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.INFRASTRUCTURE_INTERFACE: {
                IInfrastructureInterface infrastructureInterface = (IInfrastructureInterface)theEObject;
                T result = caseInfrastructureInterface(infrastructureInterface);
                if (result == null) result = caseTechnologyLayerElement(infrastructureInterface);
                if (result == null) result = caseInterfaceElement(infrastructureInterface);
                if (result == null) result = caseArchimateElement(infrastructureInterface);
                if (result == null) result = caseArchimateModelElement(infrastructureInterface);
                if (result == null) result = caseIdentifier(infrastructureInterface);
                if (result == null) result = caseCloneable(infrastructureInterface);
                if (result == null) result = caseNameable(infrastructureInterface);
                if (result == null) result = caseDocumentable(infrastructureInterface);
                if (result == null) result = caseProperties(infrastructureInterface);
                if (result == null) result = caseAdapter(infrastructureInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.INFRASTRUCTURE_SERVICE: {
                IInfrastructureService infrastructureService = (IInfrastructureService)theEObject;
                T result = caseInfrastructureService(infrastructureService);
                if (result == null) result = caseTechnologyLayerElement(infrastructureService);
                if (result == null) result = caseServiceElement(infrastructureService);
                if (result == null) result = caseArchimateElement(infrastructureService);
                if (result == null) result = caseArchimateModelElement(infrastructureService);
                if (result == null) result = caseIdentifier(infrastructureService);
                if (result == null) result = caseCloneable(infrastructureService);
                if (result == null) result = caseNameable(infrastructureService);
                if (result == null) result = caseDocumentable(infrastructureService);
                if (result == null) result = caseProperties(infrastructureService);
                if (result == null) result = caseAdapter(infrastructureService);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.INFRASTRUCTURE_FUNCTION: {
                IInfrastructureFunction infrastructureFunction = (IInfrastructureFunction)theEObject;
                T result = caseInfrastructureFunction(infrastructureFunction);
                if (result == null) result = caseTechnologyLayerElement(infrastructureFunction);
                if (result == null) result = caseArchimateElement(infrastructureFunction);
                if (result == null) result = caseArchimateModelElement(infrastructureFunction);
                if (result == null) result = caseIdentifier(infrastructureFunction);
                if (result == null) result = caseCloneable(infrastructureFunction);
                if (result == null) result = caseNameable(infrastructureFunction);
                if (result == null) result = caseDocumentable(infrastructureFunction);
                if (result == null) result = caseProperties(infrastructureFunction);
                if (result == null) result = caseAdapter(infrastructureFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.NODE: {
                INode node = (INode)theEObject;
                T result = caseNode(node);
                if (result == null) result = caseTechnologyLayerElement(node);
                if (result == null) result = caseArchimateElement(node);
                if (result == null) result = caseArchimateModelElement(node);
                if (result == null) result = caseIdentifier(node);
                if (result == null) result = caseCloneable(node);
                if (result == null) result = caseNameable(node);
                if (result == null) result = caseDocumentable(node);
                if (result == null) result = caseProperties(node);
                if (result == null) result = caseAdapter(node);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SYSTEM_SOFTWARE: {
                ISystemSoftware systemSoftware = (ISystemSoftware)theEObject;
                T result = caseSystemSoftware(systemSoftware);
                if (result == null) result = caseTechnologyLayerElement(systemSoftware);
                if (result == null) result = caseArchimateElement(systemSoftware);
                if (result == null) result = caseArchimateModelElement(systemSoftware);
                if (result == null) result = caseIdentifier(systemSoftware);
                if (result == null) result = caseCloneable(systemSoftware);
                if (result == null) result = caseNameable(systemSoftware);
                if (result == null) result = caseDocumentable(systemSoftware);
                if (result == null) result = caseProperties(systemSoftware);
                if (result == null) result = caseAdapter(systemSoftware);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DEVICE: {
                IDevice device = (IDevice)theEObject;
                T result = caseDevice(device);
                if (result == null) result = caseTechnologyLayerElement(device);
                if (result == null) result = caseArchimateElement(device);
                if (result == null) result = caseArchimateModelElement(device);
                if (result == null) result = caseIdentifier(device);
                if (result == null) result = caseCloneable(device);
                if (result == null) result = caseNameable(device);
                if (result == null) result = caseDocumentable(device);
                if (result == null) result = caseProperties(device);
                if (result == null) result = caseAdapter(device);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.MOTIVATION_ELEMENT: {
                IMotivationElement motivationElement = (IMotivationElement)theEObject;
                T result = caseMotivationElement(motivationElement);
                if (result == null) result = caseArchimateElement(motivationElement);
                if (result == null) result = caseArchimateModelElement(motivationElement);
                if (result == null) result = caseIdentifier(motivationElement);
                if (result == null) result = caseCloneable(motivationElement);
                if (result == null) result = caseNameable(motivationElement);
                if (result == null) result = caseDocumentable(motivationElement);
                if (result == null) result = caseProperties(motivationElement);
                if (result == null) result = caseAdapter(motivationElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.STAKEHOLDER: {
                IStakeholder stakeholder = (IStakeholder)theEObject;
                T result = caseStakeholder(stakeholder);
                if (result == null) result = caseMotivationElement(stakeholder);
                if (result == null) result = caseArchimateElement(stakeholder);
                if (result == null) result = caseArchimateModelElement(stakeholder);
                if (result == null) result = caseIdentifier(stakeholder);
                if (result == null) result = caseCloneable(stakeholder);
                if (result == null) result = caseNameable(stakeholder);
                if (result == null) result = caseDocumentable(stakeholder);
                if (result == null) result = caseProperties(stakeholder);
                if (result == null) result = caseAdapter(stakeholder);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DRIVER: {
                IDriver driver = (IDriver)theEObject;
                T result = caseDriver(driver);
                if (result == null) result = caseMotivationElement(driver);
                if (result == null) result = caseArchimateElement(driver);
                if (result == null) result = caseArchimateModelElement(driver);
                if (result == null) result = caseIdentifier(driver);
                if (result == null) result = caseCloneable(driver);
                if (result == null) result = caseNameable(driver);
                if (result == null) result = caseDocumentable(driver);
                if (result == null) result = caseProperties(driver);
                if (result == null) result = caseAdapter(driver);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.ASSESSMENT: {
                IAssessment assessment = (IAssessment)theEObject;
                T result = caseAssessment(assessment);
                if (result == null) result = caseMotivationElement(assessment);
                if (result == null) result = caseArchimateElement(assessment);
                if (result == null) result = caseArchimateModelElement(assessment);
                if (result == null) result = caseIdentifier(assessment);
                if (result == null) result = caseCloneable(assessment);
                if (result == null) result = caseNameable(assessment);
                if (result == null) result = caseDocumentable(assessment);
                if (result == null) result = caseProperties(assessment);
                if (result == null) result = caseAdapter(assessment);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.GOAL: {
                IGoal goal = (IGoal)theEObject;
                T result = caseGoal(goal);
                if (result == null) result = caseMotivationElement(goal);
                if (result == null) result = caseArchimateElement(goal);
                if (result == null) result = caseArchimateModelElement(goal);
                if (result == null) result = caseIdentifier(goal);
                if (result == null) result = caseCloneable(goal);
                if (result == null) result = caseNameable(goal);
                if (result == null) result = caseDocumentable(goal);
                if (result == null) result = caseProperties(goal);
                if (result == null) result = caseAdapter(goal);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.REQUIREMENT: {
                IRequirement requirement = (IRequirement)theEObject;
                T result = caseRequirement(requirement);
                if (result == null) result = caseMotivationElement(requirement);
                if (result == null) result = caseArchimateElement(requirement);
                if (result == null) result = caseArchimateModelElement(requirement);
                if (result == null) result = caseIdentifier(requirement);
                if (result == null) result = caseCloneable(requirement);
                if (result == null) result = caseNameable(requirement);
                if (result == null) result = caseDocumentable(requirement);
                if (result == null) result = caseProperties(requirement);
                if (result == null) result = caseAdapter(requirement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.CONSTRAINT: {
                IConstraint constraint = (IConstraint)theEObject;
                T result = caseConstraint(constraint);
                if (result == null) result = caseMotivationElement(constraint);
                if (result == null) result = caseArchimateElement(constraint);
                if (result == null) result = caseArchimateModelElement(constraint);
                if (result == null) result = caseIdentifier(constraint);
                if (result == null) result = caseCloneable(constraint);
                if (result == null) result = caseNameable(constraint);
                if (result == null) result = caseDocumentable(constraint);
                if (result == null) result = caseProperties(constraint);
                if (result == null) result = caseAdapter(constraint);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PRINCIPLE: {
                IPrinciple principle = (IPrinciple)theEObject;
                T result = casePrinciple(principle);
                if (result == null) result = caseMotivationElement(principle);
                if (result == null) result = caseArchimateElement(principle);
                if (result == null) result = caseArchimateModelElement(principle);
                if (result == null) result = caseIdentifier(principle);
                if (result == null) result = caseCloneable(principle);
                if (result == null) result = caseNameable(principle);
                if (result == null) result = caseDocumentable(principle);
                if (result == null) result = caseProperties(principle);
                if (result == null) result = caseAdapter(principle);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.IMPLEMENTATION_MIGRATION_ELEMENT: {
                IImplementationMigrationElement implementationMigrationElement = (IImplementationMigrationElement)theEObject;
                T result = caseImplementationMigrationElement(implementationMigrationElement);
                if (result == null) result = caseArchimateElement(implementationMigrationElement);
                if (result == null) result = caseArchimateModelElement(implementationMigrationElement);
                if (result == null) result = caseIdentifier(implementationMigrationElement);
                if (result == null) result = caseCloneable(implementationMigrationElement);
                if (result == null) result = caseNameable(implementationMigrationElement);
                if (result == null) result = caseDocumentable(implementationMigrationElement);
                if (result == null) result = caseProperties(implementationMigrationElement);
                if (result == null) result = caseAdapter(implementationMigrationElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.WORK_PACKAGE: {
                IWorkPackage workPackage = (IWorkPackage)theEObject;
                T result = caseWorkPackage(workPackage);
                if (result == null) result = caseImplementationMigrationElement(workPackage);
                if (result == null) result = caseArchimateElement(workPackage);
                if (result == null) result = caseArchimateModelElement(workPackage);
                if (result == null) result = caseIdentifier(workPackage);
                if (result == null) result = caseCloneable(workPackage);
                if (result == null) result = caseNameable(workPackage);
                if (result == null) result = caseDocumentable(workPackage);
                if (result == null) result = caseProperties(workPackage);
                if (result == null) result = caseAdapter(workPackage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DELIVERABLE: {
                IDeliverable deliverable = (IDeliverable)theEObject;
                T result = caseDeliverable(deliverable);
                if (result == null) result = caseImplementationMigrationElement(deliverable);
                if (result == null) result = caseArchimateElement(deliverable);
                if (result == null) result = caseArchimateModelElement(deliverable);
                if (result == null) result = caseIdentifier(deliverable);
                if (result == null) result = caseCloneable(deliverable);
                if (result == null) result = caseNameable(deliverable);
                if (result == null) result = caseDocumentable(deliverable);
                if (result == null) result = caseProperties(deliverable);
                if (result == null) result = caseAdapter(deliverable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.PLATEAU: {
                IPlateau plateau = (IPlateau)theEObject;
                T result = casePlateau(plateau);
                if (result == null) result = caseImplementationMigrationElement(plateau);
                if (result == null) result = caseArchimateElement(plateau);
                if (result == null) result = caseArchimateModelElement(plateau);
                if (result == null) result = caseIdentifier(plateau);
                if (result == null) result = caseCloneable(plateau);
                if (result == null) result = caseNameable(plateau);
                if (result == null) result = caseDocumentable(plateau);
                if (result == null) result = caseProperties(plateau);
                if (result == null) result = caseAdapter(plateau);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.GAP: {
                IGap gap = (IGap)theEObject;
                T result = caseGap(gap);
                if (result == null) result = caseImplementationMigrationElement(gap);
                if (result == null) result = caseArchimateElement(gap);
                if (result == null) result = caseArchimateModelElement(gap);
                if (result == null) result = caseIdentifier(gap);
                if (result == null) result = caseCloneable(gap);
                if (result == null) result = caseNameable(gap);
                if (result == null) result = caseDocumentable(gap);
                if (result == null) result = caseProperties(gap);
                if (result == null) result = caseAdapter(gap);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_COMPONENT: {
                IDiagramModelComponent diagramModelComponent = (IDiagramModelComponent)theEObject;
                T result = caseDiagramModelComponent(diagramModelComponent);
                if (result == null) result = caseIdentifier(diagramModelComponent);
                if (result == null) result = caseCloneable(diagramModelComponent);
                if (result == null) result = caseAdapter(diagramModelComponent);
                if (result == null) result = caseNameable(diagramModelComponent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_CONTAINER: {
                IDiagramModelContainer diagramModelContainer = (IDiagramModelContainer)theEObject;
                T result = caseDiagramModelContainer(diagramModelContainer);
                if (result == null) result = caseDiagramModelComponent(diagramModelContainer);
                if (result == null) result = caseIdentifier(diagramModelContainer);
                if (result == null) result = caseCloneable(diagramModelContainer);
                if (result == null) result = caseAdapter(diagramModelContainer);
                if (result == null) result = caseNameable(diagramModelContainer);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL: {
                IDiagramModel diagramModel = (IDiagramModel)theEObject;
                T result = caseDiagramModel(diagramModel);
                if (result == null) result = caseArchimateModelElement(diagramModel);
                if (result == null) result = caseDiagramModelContainer(diagramModel);
                if (result == null) result = caseDocumentable(diagramModel);
                if (result == null) result = caseProperties(diagramModel);
                if (result == null) result = caseDiagramModelComponent(diagramModel);
                if (result == null) result = caseAdapter(diagramModel);
                if (result == null) result = caseIdentifier(diagramModel);
                if (result == null) result = caseCloneable(diagramModel);
                if (result == null) result = caseNameable(diagramModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE: {
                IDiagramModelReference diagramModelReference = (IDiagramModelReference)theEObject;
                T result = caseDiagramModelReference(diagramModelReference);
                if (result == null) result = caseDiagramModelObject(diagramModelReference);
                if (result == null) result = caseDiagramModelComponent(diagramModelReference);
                if (result == null) result = caseFontAttribute(diagramModelReference);
                if (result == null) result = caseLineObject(diagramModelReference);
                if (result == null) result = caseIdentifier(diagramModelReference);
                if (result == null) result = caseCloneable(diagramModelReference);
                if (result == null) result = caseAdapter(diagramModelReference);
                if (result == null) result = caseNameable(diagramModelReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_OBJECT: {
                IDiagramModelObject diagramModelObject = (IDiagramModelObject)theEObject;
                T result = caseDiagramModelObject(diagramModelObject);
                if (result == null) result = caseDiagramModelComponent(diagramModelObject);
                if (result == null) result = caseFontAttribute(diagramModelObject);
                if (result == null) result = caseLineObject(diagramModelObject);
                if (result == null) result = caseIdentifier(diagramModelObject);
                if (result == null) result = caseCloneable(diagramModelObject);
                if (result == null) result = caseAdapter(diagramModelObject);
                if (result == null) result = caseNameable(diagramModelObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_GROUP: {
                IDiagramModelGroup diagramModelGroup = (IDiagramModelGroup)theEObject;
                T result = caseDiagramModelGroup(diagramModelGroup);
                if (result == null) result = caseDiagramModelObject(diagramModelGroup);
                if (result == null) result = caseDiagramModelContainer(diagramModelGroup);
                if (result == null) result = caseDocumentable(diagramModelGroup);
                if (result == null) result = caseProperties(diagramModelGroup);
                if (result == null) result = caseDiagramModelComponent(diagramModelGroup);
                if (result == null) result = caseFontAttribute(diagramModelGroup);
                if (result == null) result = caseLineObject(diagramModelGroup);
                if (result == null) result = caseIdentifier(diagramModelGroup);
                if (result == null) result = caseCloneable(diagramModelGroup);
                if (result == null) result = caseAdapter(diagramModelGroup);
                if (result == null) result = caseNameable(diagramModelGroup);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_NOTE: {
                IDiagramModelNote diagramModelNote = (IDiagramModelNote)theEObject;
                T result = caseDiagramModelNote(diagramModelNote);
                if (result == null) result = caseDiagramModelObject(diagramModelNote);
                if (result == null) result = caseTextContent(diagramModelNote);
                if (result == null) result = caseDiagramModelComponent(diagramModelNote);
                if (result == null) result = caseFontAttribute(diagramModelNote);
                if (result == null) result = caseLineObject(diagramModelNote);
                if (result == null) result = caseIdentifier(diagramModelNote);
                if (result == null) result = caseCloneable(diagramModelNote);
                if (result == null) result = caseAdapter(diagramModelNote);
                if (result == null) result = caseNameable(diagramModelNote);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_IMAGE: {
                IDiagramModelImage diagramModelImage = (IDiagramModelImage)theEObject;
                T result = caseDiagramModelImage(diagramModelImage);
                if (result == null) result = caseDiagramModelObject(diagramModelImage);
                if (result == null) result = caseBorderObject(diagramModelImage);
                if (result == null) result = caseDiagramModelImageProvider(diagramModelImage);
                if (result == null) result = caseDiagramModelComponent(diagramModelImage);
                if (result == null) result = caseFontAttribute(diagramModelImage);
                if (result == null) result = caseLineObject(diagramModelImage);
                if (result == null) result = caseIdentifier(diagramModelImage);
                if (result == null) result = caseCloneable(diagramModelImage);
                if (result == null) result = caseAdapter(diagramModelImage);
                if (result == null) result = caseNameable(diagramModelImage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION: {
                IDiagramModelConnection diagramModelConnection = (IDiagramModelConnection)theEObject;
                T result = caseDiagramModelConnection(diagramModelConnection);
                if (result == null) result = caseDiagramModelComponent(diagramModelConnection);
                if (result == null) result = caseFontAttribute(diagramModelConnection);
                if (result == null) result = caseProperties(diagramModelConnection);
                if (result == null) result = caseDocumentable(diagramModelConnection);
                if (result == null) result = caseLineObject(diagramModelConnection);
                if (result == null) result = caseIdentifier(diagramModelConnection);
                if (result == null) result = caseCloneable(diagramModelConnection);
                if (result == null) result = caseAdapter(diagramModelConnection);
                if (result == null) result = caseNameable(diagramModelConnection);
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
            case IArchimatePackage.BORDER_OBJECT: {
                IBorderObject borderObject = (IBorderObject)theEObject;
                T result = caseBorderObject(borderObject);
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
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL: {
                IArchimateDiagramModel archimateDiagramModel = (IArchimateDiagramModel)theEObject;
                T result = caseArchimateDiagramModel(archimateDiagramModel);
                if (result == null) result = caseDiagramModel(archimateDiagramModel);
                if (result == null) result = caseArchimateModelElement(archimateDiagramModel);
                if (result == null) result = caseDiagramModelContainer(archimateDiagramModel);
                if (result == null) result = caseDocumentable(archimateDiagramModel);
                if (result == null) result = caseProperties(archimateDiagramModel);
                if (result == null) result = caseDiagramModelComponent(archimateDiagramModel);
                if (result == null) result = caseAdapter(archimateDiagramModel);
                if (result == null) result = caseIdentifier(archimateDiagramModel);
                if (result == null) result = caseCloneable(archimateDiagramModel);
                if (result == null) result = caseNameable(archimateDiagramModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_OBJECT: {
                IDiagramModelArchimateObject diagramModelArchimateObject = (IDiagramModelArchimateObject)theEObject;
                T result = caseDiagramModelArchimateObject(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelObject(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelContainer(diagramModelArchimateObject);
                if (result == null) result = caseDiagramModelComponent(diagramModelArchimateObject);
                if (result == null) result = caseFontAttribute(diagramModelArchimateObject);
                if (result == null) result = caseLineObject(diagramModelArchimateObject);
                if (result == null) result = caseIdentifier(diagramModelArchimateObject);
                if (result == null) result = caseCloneable(diagramModelArchimateObject);
                if (result == null) result = caseAdapter(diagramModelArchimateObject);
                if (result == null) result = caseNameable(diagramModelArchimateObject);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.DIAGRAM_MODEL_ARCHIMATE_CONNECTION: {
                IDiagramModelArchimateConnection diagramModelArchimateConnection = (IDiagramModelArchimateConnection)theEObject;
                T result = caseDiagramModelArchimateConnection(diagramModelArchimateConnection);
                if (result == null) result = caseDiagramModelConnection(diagramModelArchimateConnection);
                if (result == null) result = caseDiagramModelComponent(diagramModelArchimateConnection);
                if (result == null) result = caseFontAttribute(diagramModelArchimateConnection);
                if (result == null) result = caseProperties(diagramModelArchimateConnection);
                if (result == null) result = caseDocumentable(diagramModelArchimateConnection);
                if (result == null) result = caseLineObject(diagramModelArchimateConnection);
                if (result == null) result = caseIdentifier(diagramModelArchimateConnection);
                if (result == null) result = caseCloneable(diagramModelArchimateConnection);
                if (result == null) result = caseAdapter(diagramModelArchimateConnection);
                if (result == null) result = caseNameable(diagramModelArchimateConnection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SKETCH_MODEL: {
                ISketchModel sketchModel = (ISketchModel)theEObject;
                T result = caseSketchModel(sketchModel);
                if (result == null) result = caseDiagramModel(sketchModel);
                if (result == null) result = caseArchimateModelElement(sketchModel);
                if (result == null) result = caseDiagramModelContainer(sketchModel);
                if (result == null) result = caseDocumentable(sketchModel);
                if (result == null) result = caseProperties(sketchModel);
                if (result == null) result = caseDiagramModelComponent(sketchModel);
                if (result == null) result = caseAdapter(sketchModel);
                if (result == null) result = caseIdentifier(sketchModel);
                if (result == null) result = caseCloneable(sketchModel);
                if (result == null) result = caseNameable(sketchModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SKETCH_MODEL_STICKY: {
                ISketchModelSticky sketchModelSticky = (ISketchModelSticky)theEObject;
                T result = caseSketchModelSticky(sketchModelSticky);
                if (result == null) result = caseDiagramModelObject(sketchModelSticky);
                if (result == null) result = caseDiagramModelContainer(sketchModelSticky);
                if (result == null) result = caseTextContent(sketchModelSticky);
                if (result == null) result = caseProperties(sketchModelSticky);
                if (result == null) result = caseDiagramModelComponent(sketchModelSticky);
                if (result == null) result = caseFontAttribute(sketchModelSticky);
                if (result == null) result = caseLineObject(sketchModelSticky);
                if (result == null) result = caseIdentifier(sketchModelSticky);
                if (result == null) result = caseCloneable(sketchModelSticky);
                if (result == null) result = caseAdapter(sketchModelSticky);
                if (result == null) result = caseNameable(sketchModelSticky);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IArchimatePackage.SKETCH_MODEL_ACTOR: {
                ISketchModelActor sketchModelActor = (ISketchModelActor)theEObject;
                T result = caseSketchModelActor(sketchModelActor);
                if (result == null) result = caseDiagramModelObject(sketchModelActor);
                if (result == null) result = caseDocumentable(sketchModelActor);
                if (result == null) result = caseProperties(sketchModelActor);
                if (result == null) result = caseDiagramModelComponent(sketchModelActor);
                if (result == null) result = caseFontAttribute(sketchModelActor);
                if (result == null) result = caseLineObject(sketchModelActor);
                if (result == null) result = caseIdentifier(sketchModelActor);
                if (result == null) result = caseCloneable(sketchModelActor);
                if (result == null) result = caseAdapter(sketchModelActor);
                if (result == null) result = caseNameable(sketchModelActor);
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
     * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArchimateModelElement(IArchimateModelElement object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Business Activity</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Activity</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessActivity(IBusinessActivity object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Business Layer Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Business Layer Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBusinessLayerElement(IBusinessLayerElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Application Layer Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Application Layer Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseApplicationLayerElement(IApplicationLayerElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Technology Layer Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Technology Layer Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTechnologyLayerElement(ITechnologyLayerElement object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Communication Path</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Communication Path</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCommunicationPath(ICommunicationPath object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Network</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Network</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNetwork(INetwork object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Infrastructure Interface</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Infrastructure Interface</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInfrastructureInterface(IInfrastructureInterface object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Infrastructure Service</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Infrastructure Service</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInfrastructureService(IInfrastructureService object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Infrastructure Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Infrastructure Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInfrastructureFunction(IInfrastructureFunction object) {
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
    public T caseRelationship(IRelationship object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Realisation Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Realisation Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRealisationRelationship(IRealisationRelationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specialisation Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specialisation Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecialisationRelationship(ISpecialisationRelationship object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Used By Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Used By Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUsedByRelationship(IUsedByRelationship object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Junction Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Junction Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJunctionElement(IJunctionElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interface Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interface Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInterfaceElement(IInterfaceElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Service Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseServiceElement(IServiceElement object) {
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
     * Returns the result of interpreting the object as an instance of '<em>And Junction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>And Junction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAndJunction(IAndJunction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Or Junction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Or Junction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOrJunction(IOrJunction object) {
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
